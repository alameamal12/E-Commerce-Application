package projects.android.myshop.ui.category;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import projects.android.myshop.data.ShopApplication;

// factory class for CategoryViewModel
public class CategoryViewModelFactory implements ViewModelProvider.Factory {
    private final ShopApplication application;

    public CategoryViewModelFactory(ShopApplication application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CategoryViewModel.class)) {
            return (T) new CategoryViewModel(application.categoryRepository());
        }
        throw new IllegalArgumentException("Unknown ViewModel Class");
    }
}
