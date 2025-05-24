package projects.android.myshop.data.repository;

import androidx.annotation.NonNull;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import projects.android.myshop.db.ShopRoomDatabase;
import projects.android.myshop.db.dao.AddressDao;
import projects.android.myshop.db.entity.AddressEntity;


public class AddressRepository {
    private static volatile AddressRepository instance;
    private final AddressDao addressDao;

    public AddressRepository(final ShopRoomDatabase database) {
        addressDao = database.addressDao();
    }

    public static AddressRepository getInstance(final ShopRoomDatabase database) {
        if (instance == null) {
            synchronized (CategoryRepository.class) {
                if (instance == null) {
                    instance = new AddressRepository(database);
                }
            }
        }
        return instance;
    }

    // insert or update address
    public Single<Long> upsert(@NonNull AddressEntity address) {
        return addressDao.upsert(address);
    }

    // get address by addressId
    public Flowable<AddressEntity> getAddressById(long addressId) {
        return addressDao.getAddressById(addressId);
    }

}
