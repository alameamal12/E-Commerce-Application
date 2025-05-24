package projects.android.myshop.ui.signup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import projects.android.myshop.data.ShopApplication;

// factory class for SignupViewModel
public class SignupViewModelFactory implements ViewModelProvider.Factory {
    private final ShopApplication application;

    public SignupViewModelFactory(ShopApplication application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SignupViewModel.class)) {
            return (T) new SignupViewModel(application.userRepository());
        }
        throw new IllegalArgumentException("Unknown ViewModel Class");
    }
}
