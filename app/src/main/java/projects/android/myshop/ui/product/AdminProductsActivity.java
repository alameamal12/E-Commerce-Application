package projects.android.myshop.ui.product;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import projects.android.myshop.R;
import projects.android.myshop.data.ShopApplication;
import projects.android.myshop.databinding.ActivityAdminProductsBinding;
import projects.android.myshop.db.entity.ProductEntity;
import projects.android.myshop.ui.base.BaseActivity;

public class AdminProductsActivity extends BaseActivity implements ProductAdapter.OnProductItemClickListener {
    private static final String TAG = "AdminProductsActivity";
    private ActivityAdminProductsBinding binding;
    private ProductAdapter productAdapter;
    private ProductViewModel productViewModel;


    @Override
    protected void initClicks() {

    }

    @Override
    protected void initViews() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_products);

        productViewModel = new ViewModelProvider(this, new ProductViewModelFactory((ShopApplication) getApplication())).get(ProductViewModel.class);


        productAdapter = new ProductAdapter(this);
        binding.setProductAdapter(productAdapter);
    }

    @Override
    protected void initMethods() {

    }

    @Override
    protected void subscribe() {

    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onProductClicked(@NonNull ProductEntity product) {

    }
}