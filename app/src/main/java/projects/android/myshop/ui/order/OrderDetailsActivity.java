package projects.android.myshop.ui.order;

import android.icu.text.SimpleDateFormat;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import projects.android.myshop.R;
import projects.android.myshop.data.ShopApplication;
import projects.android.myshop.databinding.ActivityOrderDetailsBinding;
import projects.android.myshop.ui.address.AddressViewModel;
import projects.android.myshop.ui.address.AddressViewModelFactory;
import projects.android.myshop.ui.base.BaseActivity;
import projects.android.myshop.ui.user.UserViewModel;
import projects.android.myshop.ui.user.UserViewModelFactory;
import projects.android.myshop.utils.DataUtils;

public class OrderDetailsActivity extends BaseActivity {

    public static final String ORDER_ID_EXTRA = "order_id_extra";
    private static final String TAG = "OrderDetailsActivity ";
    protected ActivityOrderDetailsBinding binding;
    private volatile Double totalPrice, totalListPrice;
    private OrderViewModel orderViewModel;
    private AddressViewModel addressViewModel;
    private UserViewModel userViewModel;
    private OrderProductViewModel orderProductViewModel;
    private OrderProductAdapter orderProductAdapter;
    private long orderId;

    @Override
    protected void initClicks() {

    }

    @Override
    protected void initViews() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_details);

        setSupportActionBar(binding.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.order_details);
        }

        if (DataUtils.isAdmin()) {
            binding.nameTv.setVisibility(View.VISIBLE);
            binding.emailTv.setVisibility(View.VISIBLE);
        } else {
            binding.nameTv.setVisibility(View.GONE);
            binding.emailTv.setVisibility(View.GONE);
        }

        orderProductViewModel = new ViewModelProvider(this, new OrderProductViewModelFactory((ShopApplication) getApplication())).get(OrderProductViewModel.class);
        orderViewModel = new ViewModelProvider(this, new OrderViewModelFactory((ShopApplication) getApplication())).get(OrderViewModel.class);
        userViewModel = new ViewModelProvider(this, new UserViewModelFactory((ShopApplication) getApplication())).get(UserViewModel.class);
        addressViewModel = new ViewModelProvider(this, new AddressViewModelFactory((ShopApplication) getApplication())).get(AddressViewModel.class);

        if (getIntent() != null) {
            orderId = getIntent().getLongExtra(ORDER_ID_EXTRA, -1);
        }

        orderProductAdapter = new OrderProductAdapter();
        binding.setOrderProductAdapter(orderProductAdapter);

        binding.orderProductsRecyclerview.setAdapter(orderProductAdapter);

//        binding.nameTv.setText(DataUtils.getName());
        binding.emailTv.setText(DataUtils.getEmail());
        binding.orderIdTv.setText(orderId + "");

    }

    @Override
    protected void initMethods() {

    }

    @Override
    protected void subscribe() {
        disposable.add(orderViewModel.getOrderById(orderId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(order -> {
            getAddress(order.getAddressId());
            binding.orderIdTv.setText(String.format(Locale.getDefault(), "OrderId - %d", orderId));
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d' 'MMM' 'yyyy' - 'h:mma", Locale.ENGLISH);
            String formattedDate = "Ordered At : " + dateFormat.format(order.getDateOrdered());
            binding.orderDateTv.setText(formattedDate);
            if (DataUtils.isAdmin()) {
                getUserDetails(order.getUserId());
            }
        }, throwable -> logE(TAG, "Unable to get order", throwable)));


        disposable.add(orderProductViewModel.getProductsForOrder(orderId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(products -> {
            orderProductAdapter.updateProducts(products);
        }, throwable -> logE(TAG, "Unable to get all products", throwable)));

        disposable.add(orderProductViewModel.getProductsPriceSumByOrderId(DataUtils.getUserId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(totalPrice -> {
            this.totalPrice = totalPrice;
            onTotalAmountChanged();
        }, throwable -> logE(TAG, "Unable to get total Products price", throwable)));

        disposable.add(orderProductViewModel.getProductsListPriceSumByOrderId(DataUtils.getUserId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(totalListPrice -> {
            this.totalListPrice = totalListPrice;
            onTotalAmountChanged();
        }, throwable -> logE(TAG, "Unable to get total Products list price", throwable)));


    }

    private void getUserDetails(long userId) {
        disposable.add(userViewModel.getUser(userId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(user -> {
            binding.nameTv.setText(String.format(Locale.getDefault(), "Name - %s", user.getFullName()));
            binding.emailTv.setText(String.format(Locale.getDefault(), "Email - %s", user.getEmail()));
        }, throwable -> logE(TAG, "Unable to get user details", throwable)));

    }

    private void getAddress(long addressId) {
        disposable.add(addressViewModel.getAddressById(addressId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(address -> {
            binding.receiverNameTv.setText(address.getName());
            binding.numberTv.setText(address.getPhoneNumber());
            binding.addressTv.setText(String.format(Locale.getDefault(), "%s, %s, %s, %s, %s", address.getHomeAddress(), address.getAreaAddress(), address.getCity(), address.getState(), address.getPostcode()));
        }, throwable -> logE(TAG, "Unable to get address", throwable)));

    }

    @Override
    public void onClick(View v) {

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


    private void onTotalAmountChanged() {
        binding.listPriceTv.setText(String.format(Locale.getDefault(), "%.2f", totalListPrice));
        binding.sellingPriceTv.setText(String.format(Locale.getDefault(), "%.2f", totalPrice));
        binding.totalAmountTv.setText(String.format(Locale.getDefault(), "%.2f", totalPrice));
    }


}