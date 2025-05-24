package projects.android.myshop.ui.cart;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import projects.android.myshop.data.repository.CartRepository;
import projects.android.myshop.db.entity.CartEntity;
import projects.android.myshop.db.entity.ProductEntity;


// viewmodel for CartViewModel
public class CartViewModel extends ViewModel {

    private final CartRepository cartRepository;

    public CartViewModel(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    // insert or update cart
    public Completable upsert(@NonNull CartEntity cart) {
        return cartRepository.upsert(cart);
    }

    // delete cart
    public Completable delete(@NonNull CartEntity cart) {
        return cartRepository.delete(cart);
    }

    // deleteCartByUserAndProduct
    public Completable deleteCartByUserAndProduct(long userId, long productId) {
        return cartRepository.deleteCartByUserAndProduct(userId, productId);
    }


    // getProductsInCartByUserId
    public Flowable<List<ProductEntity>> getProductsInCartByUserId(@NonNull Long userId) {
        return cartRepository.getProductsInCartByUserId(userId);
    }


    // check is the product in cart of a user or not
    public Flowable<Boolean> isProductInCart(long productId, long userId) {
        return cartRepository.isProductInCart(productId, userId);
    }


    // getProductsPriceSumByUserId
    public Flowable<Double> getProductsPriceSumByUserId(long userId) {
        return cartRepository.getProductsPriceSumByUserId(userId);
    }

    // getProductsListPriceSumByUserId
    public Flowable<Double> getProductsListPriceSumByUserId(long userId) {
        return cartRepository.getProductsListPriceSumByUserId(userId);
    }

    // deleteCartsByUserId
    public Completable deleteCartsByUserId(long userId) {
        return cartRepository.deleteCartsByUserId(userId);
    }

    // getCartProductIdsByUserId
    public Flowable<List<Long>> getCartProductIdsByUserId(long userId) {
        return cartRepository.getCartProductIdsByUserId(userId);
    }

}
