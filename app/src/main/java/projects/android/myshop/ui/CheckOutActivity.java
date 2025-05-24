package projects.android.myshop.ui;

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import projects.android.myshop.R;
import projects.android.myshop.data.ShopApplication;
import projects.android.myshop.databinding.ActivityCheckOutBinding;
import projects.android.myshop.db.dao.OrderProductDao;
import projects.android.myshop.db.entity.AddressEntity;
import projects.android.myshop.db.entity.OrderEntity;
import projects.android.myshop.db.entity.ProductEntity;
import projects.android.myshop.ui.address.AddressViewModel;
import projects.android.myshop.ui.address.AddressViewModelFactory;
import projects.android.myshop.ui.base.BaseActivity;
import projects.android.myshop.ui.cart.CartProductAdapter;
import projects.android.myshop.ui.cart.CartViewModel;
import projects.android.myshop.ui.cart.CartViewModelFactory;
import projects.android.myshop.ui.order.OrderProductViewModel;
import projects.android.myshop.ui.order.OrderProductViewModelFactory;
import projects.android.myshop.ui.order.OrderViewModel;
import projects.android.myshop.ui.order.OrderViewModelFactory;
import projects.android.myshop.utils.DataUtils;

public class CheckOutActivity extends BaseActivity implements CartProductAdapter.OnCartProductItemClickListener {

    private static final String TAG = "CheckOutActivity";
    private volatile boolean isAddressSaved = false;
    private ActivityCheckOutBinding binding;
    private CartViewModel cartViewModel;
    private OrderViewModel orderViewModel;
    private OrderProductViewModel orderProductViewModel;
    private volatile List<Long> productIds;
    private CartProductAdapter cartProductAdapter;
    private volatile Double totalPrice, totalListPrice;
    private AddressViewModel addressViewModel;
    private AddressEntity address;

    @Override
    protected void initClicks() {
        binding.btn.setOnClickListener(this);
    }

    @Override
    protected void initViews() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_check_out);

        setSupportActionBar(binding.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.check_out);
        }

        addTextChangerListeners();

        addressViewModel = new ViewModelProvider(this, new AddressViewModelFactory((ShopApplication) getApplication())).get(AddressViewModel.class);
        orderViewModel = new ViewModelProvider(this, new OrderViewModelFactory((ShopApplication) getApplication())).get(OrderViewModel.class);
        cartViewModel = new ViewModelProvider(this, new CartViewModelFactory((ShopApplication) getApplication())).get(CartViewModel.class);
        orderProductViewModel = new ViewModelProvider(this, new OrderProductViewModelFactory((ShopApplication) getApplication())).get(OrderProductViewModel.class);

        cartProductAdapter = new CartProductAdapter(this, true);
        binding.setAdapter(cartProductAdapter);
    }

    @Override
    protected void initMethods() {
        binding.btn.setOnClickListener(this);
        binding.placeOrderBtn.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void subscribe() {

        disposable.add(cartViewModel.getProductsInCartByUserId(DataUtils.getUserId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(allProducts -> {
            cartProductAdapter.updateCartProducts(allProducts);
        }, throwable -> logE(TAG, "Unable to get all Cart Products", throwable)));

        disposable.add(cartViewModel.getProductsPriceSumByUserId(DataUtils.getUserId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(totalPrice -> {
            this.totalPrice = totalPrice;
            onTotalAmountChanged();
        }, throwable -> logE(TAG, "Unable to get total Products price", throwable)));

        disposable.add(cartViewModel.getProductsListPriceSumByUserId(DataUtils.getUserId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(totalListPrice -> {
            this.totalListPrice = totalListPrice;
            onTotalAmountChanged();
        }, throwable -> logE(TAG, "Unable to get total Products list price", throwable)));

        disposable.add(cartViewModel.getCartProductIdsByUserId(DataUtils.getUserId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(productIds -> {
            this.productIds = productIds;
        }, throwable -> logE(TAG, "Unable to get product ids", throwable)));


    }

    private void onTotalAmountChanged() {
        binding.listPriceTv.setText(String.format(Locale.getDefault(), "%.2f", totalListPrice));
        binding.sellingPriceTv.setText(String.format(Locale.getDefault(), "%.2f", totalPrice));
        binding.totalAmountTv.setText(String.format(Locale.getDefault(), "%.2f", totalPrice));
        binding.totalAmountTv2.setText(String.format(Locale.getDefault(), "%.2f", totalPrice));
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == binding.btn.getId()) {
            saveAddress();
        } else if (id == binding.placeOrderBtn.getId()) {
            placeOrder();
        }
    }

    private void placeOrder() {
        if (!isAddressSaved) {
            makeToast("Please save address");
            return;
        }

        if (productIds == null || productIds.isEmpty()) {
            makeToast("Please try after some time");
            return;
        }

        OrderEntity order = new OrderEntity();
        order.setUserId(DataUtils.getUserId());
        order.setOrderStatus(1);
        order.setAddressId(address.getAddressId());
        order.setDateOrdered(new Date());
        order.setFirstProductId(productIds.get(0));

        binding.placeOrderBtn.setEnabled(false);

        disposable.add(orderViewModel.upsert(order).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(orderId -> {
            order.setId(orderId);
            insertOrderProducts(orderId);
        }, throwable -> logE(TAG, "Unable to upsert order", throwable)));

    }

    private void insertOrderProducts(long orderId) {
        disposable.add(orderProductViewModel.insertAll(OrderProductDao.fromProductList(productIds, orderId)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(this::deleteCarts, throwable -> logE(TAG, "Unable to upsert order", throwable)));
    }

    private void deleteCarts() {
        disposable.add(cartViewModel.deleteCartsByUserId(DataUtils.getUserId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
            makeToast("Order placed!");
            finish();
        }, throwable -> logE(TAG, "Unable to delete carts", throwable)));

    }

    private void saveAddress() {
        AppCompatEditText nameEt = binding.nameEt, numberEt = binding.numberEt, postcodeEt = binding.postcodeEt, stateEt = binding.stateEt, cityEt = binding.cityEt, homeEt = binding.homeEt, areaEt = binding.areaEt;
        String name = "", number = "", postcode = "", state = "", city = "", home = "", area = "";


        // name
        if (nameEt.getText() == null) {
            makeToast("Something went wrong");
            return;
        }

        name = nameEt.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            binding.nameInputLayout.setError("Please enter full name");
            return;
        }


        // number
        if (numberEt.getText() == null) {
            makeToast("Something went wrong");
            return;
        }

        number = numberEt.getText().toString().trim();

        if (TextUtils.isEmpty(number)) {
            binding.numberInputLayout.setError("Please enter phone number");
            return;
        }


        // postcode
        if (postcodeEt.getText() == null) {
            makeToast("Something went wrong");
            return;
        }

        postcode = postcodeEt.getText().toString().trim();

        if (TextUtils.isEmpty(postcode)) {
            binding.postcodeInputLayout.setError("Please enter postcode");
            return;
        }


        // state
        if (stateEt.getText() == null) {
            makeToast("Something went wrong");
            return;
        }

        state = stateEt.getText().toString().trim();

        if (TextUtils.isEmpty(state)) {
            binding.stateInputLayout.setError("Please enter state");
            return;
        }


        // city
        if (cityEt.getText() == null) {
            makeToast("Something went wrong");
            return;
        }

        city = cityEt.getText().toString().trim();

        if (TextUtils.isEmpty(city)) {
            binding.cityInputLayout.setError("Please enter city");
            return;
        }


        // home
        if (homeEt.getText() == null) {
            makeToast("Something went wrong");
            return;
        }

        home = homeEt.getText().toString().trim();

        if (TextUtils.isEmpty(home)) {
            binding.homeInputLayout.setError("Please enter home address");
            return;
        }


        // area
        if (areaEt.getText() == null) {
            makeToast("Something went wrong");
            return;
        }

        area = areaEt.getText().toString().trim();

        if (TextUtils.isEmpty(area)) {
            binding.areaInputLayout.setError("Please enter area address");
            return;
        }

        address = new AddressEntity();

        address.setName(name);
        address.setPhoneNumber(number);
        address.setPostcode(postcode);
        address.setState(state);
        address.setCity(city);
        address.setHomeAddress(home);
        address.setAreaAddress(area);


        binding.btn.setEnabled(false);
        disposable.add(addressViewModel.upsert(address).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(addressId -> {
            address.setAddressId(addressId);
            isAddressSaved = true;
            onAddressChanged();
            binding.btn.setEnabled(true);
        }, throwable -> logE(TAG, "Unable to save address", throwable)));


    }

    private void onAddressChanged() {
        if (isAddressSaved) {

            if (address != null) {
                binding.phoneNumberTv.setText(address.getPhoneNumber());
                binding.nameTv.setText(address.getName());
                binding.adressTv.setText(String.format(Locale.getDefault(), "%s, %s, %s, %s, %s", address.getHomeAddress(), address.getAreaAddress(), address.getCity(), address.getState(), address.getPostcode()));
            }

            binding.addressInputLl.setVisibility(View.INVISIBLE);
            binding.addressInputLl.setVisibility(View.GONE);
            binding.addressLl.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCartProductClicked(@NonNull ProductEntity product) {

    }

    @Override
    public void onRemoveProductClicked(@NonNull ProductEntity product) {

    }

    private void addTextChangerListeners() {
        afterTextChanged(binding.nameEt, binding.nameInputLayout, null);
        afterTextChanged(binding.numberEt, binding.numberInputLayout, null);
        afterTextChanged(binding.postcodeEt, binding.postcodeInputLayout, null);
        afterTextChanged(binding.stateEt, binding.stateInputLayout, null);
        afterTextChanged(binding.cityEt, binding.cityInputLayout, null);
        afterTextChanged(binding.homeEt, binding.homeInputLayout, null);
        afterTextChanged(binding.areaEt, binding.areaInputLayout, null);
    }

}