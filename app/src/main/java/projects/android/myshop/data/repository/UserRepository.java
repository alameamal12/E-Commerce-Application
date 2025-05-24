package projects.android.myshop.data.repository;

import androidx.annotation.NonNull;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import projects.android.myshop.db.ShopRoomDatabase;
import projects.android.myshop.db.dao.UserDao;
import projects.android.myshop.db.entity.UserEntity;

public class UserRepository {
    private static volatile UserRepository instance;
    private final UserDao userDao;


    public UserRepository(final ShopRoomDatabase database) {
        userDao = database.userDao();
    }

    public static UserRepository getInstance(final ShopRoomDatabase database) {
        if (instance == null) {
            synchronized (UserRepository.class) {
                if (instance == null) {
                    instance = new UserRepository(database);
                }
            }
        }
        return instance;
    }

    // insert or update users
    public Completable upsert(@NonNull UserEntity user) {
        return userDao.upsert(user);
    }

    // delete user
    public Completable delete(@NonNull UserEntity user) {
        return userDao.delete(user);
    }

    // get all users
    public Flowable<List<UserEntity>> getAllUsers() {
        return userDao.getAllUsers();
    }

    // get user by userId
    public Flowable<UserEntity> getUser(@NonNull Long userId) {
        return userDao.getUser(userId);
    }


    // check email , password is correct or not for users
    public boolean checkUserCredentials(@NonNull String email, @NonNull String password) {
        return userDao.checkUserCredentials(email, password);
    }

    //check email is exists or not in user table
    public boolean checkEmailExists(@NonNull String email) {
        return userDao.checkEmailExists(email);
    }

    // get user by email
    public Flowable<UserEntity> getUser(@NonNull String email) {
        return userDao.getUser(email);
    }
}
