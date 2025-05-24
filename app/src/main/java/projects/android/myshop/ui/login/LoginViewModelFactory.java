package projects.android.myshop.ui.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import projects.android.myshop.data.ShopApplication;

// factory class for LoginViewModel
public class LoginViewModelFactory implements ViewModelProvider.Factory {
    private final ShopApplication application;

    public LoginViewModelFactory(ShopApplication application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(application.userRepository());
        }
        throw new IllegalArgumentException("Unknown ViewModel Class");
    }
}
