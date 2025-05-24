package projects.android.myshop.ui.order;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import projects.android.myshop.data.ShopApplication;


// factory class for OrderViewModel
public class OrderViewModelFactory implements ViewModelProvider.Factory {

    private final ShopApplication application;

    public OrderViewModelFactory(ShopApplication application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(OrderViewModel.class)) {
            return (T) new OrderViewModel(application.orderRepository());
        }
        throw new IllegalArgumentException("Unknown ViewModel Class");
    }
}
