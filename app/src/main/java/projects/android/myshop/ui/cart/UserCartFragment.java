package projects.android.myshop.ui.cart;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import projects.android.myshop.R;
import projects.android.myshop.data.ShopApplication;
import projects.android.myshop.databinding.FragmentUserCartBinding;
import projects.android.myshop.db.entity.ProductEntity;
import projects.android.myshop.ui.CheckOutActivity;
import projects.android.myshop.ui.base.BaseFragment;
import projects.android.myshop.ui.product.UserProductDetailsActivity;
import projects.android.myshop.utils.DataUtils;

public class UserCartFragment extends BaseFragment implements CartProductAdapter.OnCartProductItemClickListener {
    private static final String TAG = "UserCartFragment";
    private FragmentUserCartBinding binding;
    private CartViewModel cartViewModel;
    private CartProductAdapter cartProductAdapter;
    private Double totalPrice, totalListPrice;

    public UserCartFragment() {
        // Required empty public constructor
        super(R.layout.fragment_user_cart);
    }

    @Override
    protected void initClicks() {
        // Initialize click listeners
        binding.placeOrderBtn.setOnClickListener(this);
    }

    @Override
    protected void initViews(@NonNull View view) {
        binding = FragmentUserCartBinding.bind(view);

        cartViewModel = new ViewModelProvider(this, new CartViewModelFactory((ShopApplication) requireActivity().getApplication())).get(CartViewModel.class);

        cartProductAdapter = new CartProductAdapter(this, false);
        binding.setAdapter(cartProductAdapter);
    }

    @Override
    protected void initMethods() {
        // Initialize any additional methods or functionality

    }

    @Override
    protected void subscribe() {
        disposable.add(cartViewModel.getProductsInCartByUserId(DataUtils.getUserId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(allProducts -> {

            if (allProducts.isEmpty()) {
                // If the cart is empty, show appropriate views
                binding.contentMain.setVisibility(View.GONE);
                binding.cartEmptyTv.setVisibility(View.VISIBLE);
            } else {
                // If the cart is not empty, show appropriate views
                binding.cartEmptyTv.setVisibility(View.GONE);
                binding.contentMain.setVisibility(View.VISIBLE);
            }
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


    }


    @Override
    public void onDestroyView() {
        // Clean up binding
        binding = null;
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == binding.placeOrderBtn.getId()) {
            // Start the checkout activity
            startActivity(new Intent(requireContext(), CheckOutActivity.class));
        }
    }


    @Override
    public void onCartProductClicked(@NonNull ProductEntity product) {
        // Open the product details activity when a cart product is clicked
        Intent intent = new Intent(requireContext(), UserProductDetailsActivity.class);
        intent.putExtra(UserProductDetailsActivity.PRODUCT_EXTRA, product);
        startActivity(intent);
    }

    @Override
    public void onRemoveProductClicked(@NonNull ProductEntity product) {
        disposable.add(cartViewModel.deleteCartByUserAndProduct(DataUtils.getUserId(), product.getId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
        }, throwable -> logE(TAG, "Unable to delete cart", throwable)));

    }

    private void onTotalAmountChanged() {
        // Update the total amount views with the calculated values
        binding.listPriceTv.setText(String.format(Locale.getDefault(), "%.2f", totalListPrice));
        binding.sellingPriceTv.setText(String.format(Locale.getDefault(), "%.2f", totalPrice));
        binding.totalAmountTv.setText(String.format(Locale.getDefault(), "%.2f", totalPrice));
        binding.totalAmountTv2.setText(String.format(Locale.getDefault(), "%.2f", totalPrice));
    }

}