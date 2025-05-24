package projects.android.myshop.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Upsert;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import projects.android.myshop.db.entity.UserEntity;


// data access object for users
@Dao
public interface UserDao {

    // insert or update users
    @Upsert
    Completable upsert(UserEntity userEntity);


    // delete user
    @Delete
    Completable delete(UserEntity userEntity);

    // get all users
    @Query("SELECT * FROM " + UserEntity.TABLE_NAME)
    Flowable<List<UserEntity>> getAllUsers();

    // get user by userId
    @Query("SELECT * FROM " + UserEntity.TABLE_NAME + " WHERE " + UserEntity.ID + "=:userId")
    Flowable<UserEntity> getUser(Long userId);

    //check email is exists or not in user table
    @Query("SELECT EXISTS(SELECT 1 FROM " + UserEntity.TABLE_NAME + " WHERE " + UserEntity.EMAIL + " = :email)")
    boolean checkEmailExists(String email);

    // check email , password is correct or not for users
    @Query("SELECT EXISTS(SELECT 1 FROM " + UserEntity.TABLE_NAME + " WHERE " + UserEntity.EMAIL + " = :email AND " + UserEntity.PASSWORD + " = :password LIMIT 1)")
    boolean checkUserCredentials(String email, String password);

    // get user by email
    @Query("SELECT * FROM " + UserEntity.TABLE_NAME + " WHERE " + UserEntity.EMAIL + "=:email")
    Flowable<UserEntity> getUser(String email);
}
