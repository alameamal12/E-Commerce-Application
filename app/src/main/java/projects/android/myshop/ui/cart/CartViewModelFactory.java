package projects.android.myshop.ui.cart;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import projects.android.myshop.data.ShopApplication;

// factory class for CartViewModel
public class CartViewModelFactory implements ViewModelProvider.Factory {
    private final ShopApplication application;

    public CartViewModelFactory(ShopApplication application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CartViewModel.class)) {
            return (T) new CartViewModel(application.cartRepository());
        }
        throw new IllegalArgumentException("Unknown ViewModel Class");
    }
}
