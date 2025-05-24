package projects.android.myshop.ui.address;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import projects.android.myshop.data.ShopApplication;


// factory class for AddressViewModel
public class AddressViewModelFactory implements ViewModelProvider.Factory {
    private final ShopApplication application;

    public AddressViewModelFactory(ShopApplication application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AddressViewModel.class)) {
            return (T) new AddressViewModel(application.addressRepository());
        }
        throw new IllegalArgumentException("Unknown ViewModel Class");
    }
}
