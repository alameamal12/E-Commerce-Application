package projects.android.myshop.ui.product;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import projects.android.myshop.data.repository.ProductRepository;
import projects.android.myshop.db.entity.ProductEntity;

// viewmodel for ProductViewModel
public class ProductViewModel extends ViewModel {

    private final ProductRepository productRepository;

    public ProductViewModel(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // insert or update product
    public Completable upsert(@NonNull ProductEntity product) {
        return productRepository.upsert(product);
    }

    // delete product
    public Completable delete(@NonNull ProductEntity product) {
        return productRepository.delete(product);
    }

    // get all products by category id
    public Flowable<List<ProductEntity>> getProductsByCategoryId(@NonNull Long categoryId) {
        return productRepository.getProductsByCategoryId(categoryId);
    }
}
