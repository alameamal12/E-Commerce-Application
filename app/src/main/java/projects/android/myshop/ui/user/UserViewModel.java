package projects.android.myshop.ui.user;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import projects.android.myshop.data.repository.UserRepository;
import projects.android.myshop.db.entity.UserEntity;

// viewmodel for UserViewModel
public class UserViewModel extends ViewModel {

    private final UserRepository userRepository;

    public UserViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // insert or update users
    public Completable upsert(@NonNull UserEntity user) {
        return userRepository.upsert(user);
    }

    // delete user
    public Completable delete(@NonNull UserEntity user) {
        return userRepository.delete(user);
    }

    // get all users
    public Flowable<List<UserEntity>> getAllUsers() {
        return userRepository.getAllUsers();
    }

    // get user by userId
    public Flowable<UserEntity> getUser(@NonNull Long userId) {
        return userRepository.getUser(userId);
    }

    // get user by email
    public Flowable<UserEntity> getUser(@NonNull String email) {
        return userRepository.getUser(email);
    }


}

