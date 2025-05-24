package projects.android.myshop.ui.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import projects.android.myshop.data.Admin;
import projects.android.myshop.data.repository.UserRepository;
import projects.android.myshop.data.result.LoginResult;
import projects.android.myshop.db.ShopRoomDatabase;

// viewmodel for LoginViewModel
public class LoginViewModel extends ViewModel {

    private final UserRepository userRepository;
    private final MutableLiveData<LoginResult> _loginResult = new MutableLiveData<>();
    public LiveData<LoginResult> loginResult = _loginResult;

    public LoginViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void adminLogin(@NonNull String email, @NonNull String password) {
        ShopRoomDatabase.databaseWriteExecutor.execute(() -> {
            LoginResult result = Admin.checkAdminLogin(email, password);
            _loginResult.postValue(result);
        });
    }

    public void userLogin(@NonNull String email, @NonNull String password) {
        ShopRoomDatabase.databaseWriteExecutor.execute(() -> {
            LoginResult result;
            if (userRepository.checkEmailExists(email)) {
                if (userRepository.checkUserCredentials(email, password)) {
                    result = LoginResult.SUCCESS;
                } else {
                    result = LoginResult.WRONG_PASSWORD;
                }
            } else {
                result = LoginResult.NOT_EXIST;
            }
            _loginResult.postValue(result);
        });
    }



}
