package projects.android.myshop.data.repository;

import androidx.annotation.NonNull;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import projects.android.myshop.db.ShopRoomDatabase;
import projects.android.myshop.db.dao.CategoryDao;
import projects.android.myshop.db.entity.CategoryEntity;
import projects.android.myshop.model.CategoryWithProductCount;

public class CategoryRepository {
    private static volatile CategoryRepository instance;
    private final CategoryDao categoryDao;

    public CategoryRepository(final ShopRoomDatabase database) {
        categoryDao = database.categoryDao();
    }

    public static CategoryRepository getInstance(final ShopRoomDatabase database) {
        if (instance == null) {
            synchronized (CategoryRepository.class) {
                if (instance == null) {
                    instance = new CategoryRepository(database);
                }
            }
        }
        return instance;
    }

    // insert or update category
    public Completable upsert(@NonNull CategoryEntity category) {
        return categoryDao.upsert(category);
    }

    // delete category
    public Completable delete(@NonNull CategoryEntity category) {
        return categoryDao.delete(category);
    }

    // get all categories
    public Flowable<List<CategoryEntity>> getAllCategories() {
        return categoryDao.getAllCategories();
    }


    // getAllCategoriesWithProductCount
    public   Flowable<List<CategoryWithProductCount>> getAllCategoriesWithProductCount(){
        return categoryDao.getAllCategoriesWithProductCount();
    }

}
