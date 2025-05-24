package projects.android.myshop.ui.product;

import android.content.Intent;
import android.os.Build;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import projects.android.myshop.R;
import projects.android.myshop.data.ShopApplication;
import projects.android.myshop.databinding.ActivityUserProductsBinding;
import projects.android.myshop.db.entity.CategoryEntity;
import projects.android.myshop.db.entity.ProductEntity;
import projects.android.myshop.ui.base.BaseActivity;

public class UserProductsActivity extends BaseActivity implements ProductAdapter.OnProductItemClickListener {

    public static final String CATEGORY_EXTRA = "category_extra";
    private static final String TAG = "UserProductsActivity";
    private ActivityUserProductsBinding binding;
    private ProductAdapter productAdapter;
    private CategoryEntity category;
    private ProductViewModel productViewModel;

    @Override
    protected void initClicks() {

    }

    @Override
    protected void initViews() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_products);

        setSupportActionBar(binding.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        productViewModel = new ViewModelProvider(this, new ProductViewModelFactory((ShopApplication) getApplication())).get(ProductViewModel.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            category = getIntent().getParcelableExtra(CATEGORY_EXTRA, CategoryEntity.class);
        } else {
            category = getIntent().getParcelableExtra(CATEGORY_EXTRA);
        }

        if (actionBar != null) {
            actionBar.setTitle(category.getName());
        }
        productAdapter = new ProductAdapter(this);
        binding.setProductAdapter(productAdapter);
    }

    @Override
    protected void initMethods() {

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
        disposable.add(productViewModel.getProductsByCategoryId(category.getId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(allProducts -> productAdapter.updateProducts(allProducts), throwable -> logE(TAG, "Unable to get all products", throwable)));
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onProductClicked(@NonNull ProductEntity product) {
        Intent intent = new Intent(this, UserProductDetailsActivity.class);
        intent.putExtra(UserProductDetailsActivity.PRODUCT_EXTRA, product);
        startActivity(intent);
    }

}