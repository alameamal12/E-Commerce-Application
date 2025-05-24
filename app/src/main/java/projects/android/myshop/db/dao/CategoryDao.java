package projects.android.myshop.db.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Upsert;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import projects.android.myshop.db.entity.CategoryEntity;
import projects.android.myshop.db.entity.ProductEntity;
import projects.android.myshop.model.CategoryWithProductCount;


// data access object for Category
@Dao
public interface CategoryDao {

    // insert or update category
    @Upsert
    Completable upsert(CategoryEntity category);

    // insert or update list of categories
    @Upsert
    Single<List<Long>> upsertAll(List<CategoryEntity> categoryEntities);

    // delete category
    @Delete
    Completable delete(CategoryEntity category);

    // get all categories
    @Query("SELECT * FROM " + CategoryEntity.TABLE_NAME)
    Flowable<List<CategoryEntity>> getAllCategories();

    // getAllCategoriesWithProductCount
    @Query("SELECT " + CategoryEntity.TABLE_NAME + ".*, COUNT(" + ProductEntity.TABLE_NAME + "." + ProductEntity.ID + ") AS productCount " +
            "FROM " + CategoryEntity.TABLE_NAME + " LEFT JOIN " + ProductEntity.TABLE_NAME + " ON " + CategoryEntity.TABLE_NAME + "." + CategoryEntity.ID + " = " + ProductEntity.TABLE_NAME + "." + ProductEntity.CATEGORY_ID + " GROUP BY " + CategoryEntity.TABLE_NAME + "." + CategoryEntity.ID)
    Flowable<List<CategoryWithProductCount>> getAllCategoriesWithProductCount();

}
