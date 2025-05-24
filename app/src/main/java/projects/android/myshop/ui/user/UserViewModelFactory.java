package projects.android.myshop.ui.user;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import projects.android.myshop.data.ShopApplication;


// factory class for UserViewModel
public class UserViewModelFactory implements ViewModelProvider.Factory {
    private final ShopApplication application;

    public UserViewModelFactory(ShopApplication application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserViewModel.class)) {
            return (T) new UserViewModel(application.userRepository());
        }
        throw new IllegalArgumentException("Unknown ViewModel Class");
    }
}
