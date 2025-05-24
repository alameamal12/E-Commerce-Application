package projects.android.myshop.data.repository;

import androidx.annotation.NonNull;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import projects.android.myshop.db.ShopRoomDatabase;
import projects.android.myshop.db.dao.CartDao;
import projects.android.myshop.db.entity.CartEntity;
import projects.android.myshop.db.entity.ProductEntity;

public class CartRepository {

    private static volatile CartRepository instance;
    private final CartDao cartDao;

    public CartRepository(final ShopRoomDatabase database) {
        cartDao = database.cartDao();
    }

    public static CartRepository getInstance(final ShopRoomDatabase database) {
        if (instance == null) {
            synchronized (CartRepository.class) {
                if (instance == null) {
                    instance = new CartRepository(database);
                }
            }
        }
        return instance;
    }


    // insert or update cart
    public Completable upsert(@NonNull CartEntity cart) {
        return cartDao.upsert(cart);
    }

    // delete cart
    public Completable delete(@NonNull CartEntity cart) {
        return cartDao.delete(cart);
    }


    // deleteCartByUserAndProduct
    public Completable deleteCartByUserAndProduct(long userId, long productId) {
        return cartDao.deleteCartByUserAndProduct(userId, productId);
    }


    // getProductsInCartByUserId
    public Flowable<List<ProductEntity>> getProductsInCartByUserId(@NonNull Long userId) {
        return cartDao.getProductsInCartByUserId(userId);
    }

    // check is the product in cart of a user or not
    public Flowable<Boolean> isProductInCart(long productId, long userId) {
        return cartDao.isProductInCart(productId, userId);
    }

    // getProductsPriceSumByUserId
    public Flowable<Double> getProductsPriceSumByUserId(long userId) {
        return cartDao.getProductsPriceSumByUserId(userId);
    }

    // getProductsListPriceSumByUserId
    public Flowable<Double> getProductsListPriceSumByUserId(long userId) {
        return cartDao.getProductsListPriceSumByUserId(userId);
    }

    // deleteCartsByUserId
    public Completable deleteCartsByUserId(long userId) {
        return cartDao.deleteCartsByUserId(userId);
    }

    // getCartProductIdsByUserId
    public Flowable<List<Long>> getCartProductIdsByUserId(long userId) {
        return cartDao.getCartProductIdsByUserId(userId);
    }


}