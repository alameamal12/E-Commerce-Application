package projects.android.myshop.db.dao;


import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Upsert;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import projects.android.myshop.db.entity.AddressEntity;


// data access object for Address
@Dao
public interface AddressDao {

    // insert or update address
    @Upsert
    Single<Long> upsert(AddressEntity address);

    // get address by addressId
    @Query("SELECT * FROM " + AddressEntity.TABLE_NAME + " WHERE " + AddressEntity.ID + " = :addressId")
    Flowable<AddressEntity> getAddressById(long addressId);

}
