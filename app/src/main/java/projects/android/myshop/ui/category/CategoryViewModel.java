package projects.android.myshop.ui.category;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import projects.android.myshop.data.repository.CategoryRepository;
import projects.android.myshop.db.entity.CategoryEntity;
import projects.android.myshop.model.CategoryWithProductCount;


// viewmodel for CategoryViewModel
public class CategoryViewModel extends ViewModel {

    private final CategoryRepository categoryRepository;

    public CategoryViewModel(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // insert or update category
    public Completable upsert(@NonNull CategoryEntity category) {
        return categoryRepository.upsert(category);
    }

    // delete category
    public Completable delete(@NonNull CategoryEntity category) {
        return categoryRepository.delete(category);
    }

    // get all categories
    public Flowable<List<CategoryEntity>> getAllCategories() {
        return categoryRepository.getAllCategories();
    }

    // getAllCategoriesWithProductCount
    public Flowable<List<CategoryWithProductCount>> getAllCategoriesWithProductCount() {
        return categoryRepository.getAllCategoriesWithProductCount();
    }


}
