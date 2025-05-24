package projects.android.myshop.ui.order;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import projects.android.myshop.data.ShopApplication;


// factory class for OrderProductViewModel
public class OrderProductViewModelFactory implements ViewModelProvider.Factory {

    private final ShopApplication application;

    public OrderProductViewModelFactory(ShopApplication application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(OrderProductViewModel.class)) {
            return (T) new OrderProductViewModel(application.orderProductRepository());
        }
        throw new IllegalArgumentException("Unknown ViewModel Class");
    }
}
