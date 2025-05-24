package projects.android.myshop.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Upsert;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import projects.android.myshop.db.entity.ProductEntity;


// data access object for products
@Dao
public interface ProductDao {

    // insert or update product
    @Upsert
    Completable upsert(ProductEntity productEntity);

    // insert or update list of product
    @Upsert
    Completable upsertAll(List<ProductEntity> products);

    // delete product
    @Delete
    Completable delete(ProductEntity productEntity);

    // get all products by category id
    @Query("SELECT * FROM " + ProductEntity.TABLE_NAME + " WHERE " + ProductEntity.CATEGORY_ID + " = :categoryId")
    Flowable<List<ProductEntity>> getProductsByCategoryId(Long categoryId);

}
