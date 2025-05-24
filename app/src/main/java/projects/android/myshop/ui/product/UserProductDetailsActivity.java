package projects.android.myshop.ui.product;

import android.content.Intent;
import android.os.Build;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;

import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import projects.android.myshop.R;
import projects.android.myshop.data.ShopApplication;
import projects.android.myshop.databinding.ActivityUserProductDetailsBinding;
import projects.android.myshop.db.entity.CartEntity;
import projects.android.myshop.db.entity.ProductEntity;
import projects.android.myshop.ui.CheckOutActivity;
import projects.android.myshop.ui.base.BaseActivity;
import projects.android.myshop.ui.cart.CartViewModel;
import projects.android.myshop.ui.cart.CartViewModelFactory;
import projects.android.myshop.utils.DataUtils;

public class UserProductDetailsActivity extends BaseActivity {

    public static final String PRODUCT_EXTRA = "product_extra";
    private static final String TAG = "UserProductDetailsActivity";
    private ActivityUserProductDetailsBinding binding;
    private ProductEntity product;
    private boolean isProductInCart = false;
    private CartViewModel cartViewModel;

    @Override
    protected void initClicks() {
        binding.cartBtn.setOnClickListener(this);
        binding.buyNow.setOnClickListener(this);
    }

    @Override
    protected void initViews() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_product_details);

        setSupportActionBar(binding.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(null);
        }

        cartViewModel = new ViewModelProvider(this, new CartViewModelFactory((ShopApplication) getApplication())).get(CartViewModel.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            product = getIntent().getParcelableExtra(PRODUCT_EXTRA, ProductEntity.class);
        } else {
            product = getIntent().getParcelableExtra(PRODUCT_EXTRA);
        }

        if (product != null) {
            binding.productImg.setImageURI(product.getImageUri());
            binding.productNameTv.setText(String.format(Locale.getDefault(), "Name - %s", product.getName()));
            binding.productDescTv.setText(String.format(Locale.getDefault(), "Product description - %s", product.getDescription()));
            binding.productCreatedDateTv.setText(String.format(Locale.getDefault(), "Product Created date - %s", product.getDateCreate().toString()));
            binding.productUpdatedDateTv.setText(String.format(Locale.getDefault(), "Product Updated date - %s", product.getDateUpdate().toString()));
            binding.priceTv.setText(String.format(Locale.getDefault(), "Price - %.2f", product.getPrice()));
            binding.listPriceTv.setText(String.format(Locale.getDefault(), "List Price - %.2f", product.getListPrice()));
            binding.retailPriceTv.setText(String.format(Locale.getDefault(), "Retail Price - %.2f", product.getRetailPrice()));
        }

    }

    @Override
    protected void initMethods() {

    }

    private void startCheckoutActivity() {
        Intent intent = new Intent(this, CheckOutActivity.class);
        startActivity(intent);
    }

    @Override
    protected void subscribe() {
        binding.cartBtn.setEnabled(false);
        disposable.add(cartViewModel.isProductInCart(product.getId(), DataUtils.getUserId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(isProductInCart -> {
            this.isProductInCart = isProductInCart;
            onProductInCartResult();
        }, throwable -> logE(TAG, "Unable to get product details", throwable)));

    }

    private void onProductInCartResult() {
        MaterialButton cartBtn = binding.cartBtn;
        if (isProductInCart) {
            cartBtn.setText(R.string.go_to_cart);
        } else {
            cartBtn.setText(R.string.add_to_cart);
        }
        cartBtn.setEnabled(true);
        binding.buyNow.setEnabled(true);
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
    public void onClick(View v) {
        int id = v.getId();
        if (id == binding.cartBtn.getId()) {
            if (isProductInCart) {
                goToCart();
            } else {
                addToCart();
            }
        } else if (id == binding.buyNow.getId()) {
            buyNow();
        }
    }

    private void buyNow() {
        if (isProductInCart) {
            startCheckoutActivity();
        } else {
            binding.cartBtn.setEnabled(false);
            binding.buyNow.setEnabled(false);
            CartEntity cart = new CartEntity();
            cart.setProductId(product.getId());
            cart.setUserId(DataUtils.getUserId());

            disposable.add(cartViewModel.upsert(cart).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                isProductInCart = true;
                startCheckoutActivity();
                onProductInCartResult();
            }, throwable -> logE(TAG, "Unable to upsert product in cart", throwable)));

        }
    }

    private void addToCart() {
        binding.cartBtn.setEnabled(false);
        binding.buyNow.setEnabled(false);
        CartEntity cart = new CartEntity();
        cart.setProductId(product.getId());
        cart.setUserId(DataUtils.getUserId());

        disposable.add(cartViewModel.upsert(cart).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
            isProductInCart = true;
            onProductInCartResult();
        }, throwable -> logE(TAG, "Unable to upsert product in cart", throwable)));

    }

    private void goToCart() {
        makeToast("goToCart");
    }
}