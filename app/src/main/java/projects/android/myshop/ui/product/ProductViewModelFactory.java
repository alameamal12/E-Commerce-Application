package projects.android.myshop.ui.product;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import projects.android.myshop.data.ShopApplication;

// factory class for ProductViewModel
public class ProductViewModelFactory implements ViewModelProvider.Factory {
    private final ShopApplication application;

    public ProductViewModelFactory(ShopApplication application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProductViewModel.class)) {
            return (T) new ProductViewModel(application.productRepository());
        }
        throw new IllegalArgumentException("Unknown ViewModel Class");
    }
}
