package projects.android.myshop.ui.signup;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import projects.android.myshop.data.repository.UserRepository;
import projects.android.myshop.data.result.SignupResult;
import projects.android.myshop.db.ShopRoomDatabase;
import projects.android.myshop.db.entity.UserEntity;

// viewmodel for SignupViewModel
public class SignupViewModel extends ViewModel {
    private static final String TAG = "SignupViewModel";
    private final UserRepository userRepository;
    private final MutableLiveData<SignupResult> _signupResult = new MutableLiveData<>();
    public LiveData<SignupResult> signupResult = _signupResult;

    public SignupViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void signUp(@NonNull String name, @NonNull String email, @NonNull String postcode, @NonNull String address, @NonNull String password) {
        ShopRoomDatabase.databaseWriteExecutor.execute(() -> {
            SignupResult result;
            if (userRepository.checkEmailExists(email)) {
                result = SignupResult.ALREADY_EXIST;
                _signupResult.postValue(result);
            } else {
                UserEntity user = new UserEntity();
                user.setFullName(name);
                user.setEmail(email);
                user.setPostcode(postcode);
                user.setAddress(address);
                user.setPassword(password);
                user.setDateRegister(new Date());
                user.setDateUpdate(new Date());

                userRepository.upsert(user).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                    _signupResult.postValue(SignupResult.SUCCESS);
                }, throwable -> {
                    Log.e(TAG, "signup failed", throwable);
                    _signupResult.postValue(SignupResult.FAILED);
                });

            }
        });
    }


}
