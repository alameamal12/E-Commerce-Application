package projects.android.myshop.data.repository;

import androidx.annotation.NonNull;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import projects.android.myshop.db.ShopRoomDatabase;
import projects.android.myshop.db.dao.ProductDao;
import projects.android.myshop.db.entity.ProductEntity;

public class ProductRepository {
    private static volatile ProductRepository instance;
    private final ProductDao productDao;

    public ProductRepository(final ShopRoomDatabase database) {
        productDao = database.productDao();
    }

    public static ProductRepository getInstance(final ShopRoomDatabase database) {
        if (instance == null) {
            synchronized (ProductRepository.class) {
                if (instance == null) {
                    instance = new ProductRepository(database);
                }
            }
        }
        return instance;
    }

    // insert or update product
    public Completable upsert(@NonNull ProductEntity product) {
        return productDao.upsert(product);
    }

    // delete product
    public Completable delete(@NonNull ProductEntity product) {
        return productDao.delete(product);
    }

    // get all products by category id
    public Flowable<List<ProductEntity>> getProductsByCategoryId(@NonNull Long categoryId) {
        return productDao.getProductsByCategoryId(categoryId);
    }

}
