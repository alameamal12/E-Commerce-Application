package projects.android.myshop.db.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Upsert;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import projects.android.myshop.db.entity.CartEntity;
import projects.android.myshop.db.entity.ProductEntity;


// data access object for carts
@Dao
public interface CartDao {

    // insert or update cart
    @Upsert
    Completable upsert(CartEntity cart);


    // delete cart
    @Delete
    Completable delete(CartEntity cart);


    // deleteCartsByUserId
    @Query("DELETE FROM " + CartEntity.TABLE_NAME + " WHERE " + CartEntity.USER_ID + " = :userId")
    Completable deleteCartsByUserId(long userId);


    // getProductsInCartByUserId
    @Transaction
    @Query("SELECT " + ProductEntity.TABLE_NAME + ".* FROM " + ProductEntity.TABLE_NAME + " INNER JOIN " + CartEntity.TABLE_NAME + " ON " + ProductEntity.TABLE_NAME + "." + ProductEntity.ID + " = " + CartEntity.TABLE_NAME + "." + CartEntity.PRODUCT_ID + " WHERE " + CartEntity.TABLE_NAME + "." + CartEntity.USER_ID + " = :userId")
    Flowable<List<ProductEntity>> getProductsInCartByUserId(Long userId);

    // check is the product in cart of a user or not
    @Query("SELECT EXISTS(SELECT 1 FROM " + CartEntity.TABLE_NAME + " WHERE " + CartEntity.PRODUCT_ID + " = :productId AND " + CartEntity.USER_ID + " = :userId LIMIT 1)")
    Flowable<Boolean> isProductInCart(long productId, long userId);


    // deleteCartByUserAndProduct
    @Query("DELETE FROM " + CartEntity.TABLE_NAME + " WHERE " + CartEntity.USER_ID + " = :userId AND " + CartEntity.PRODUCT_ID + " = :productId")
    Completable deleteCartByUserAndProduct(long userId, long productId);


    // getProductsPriceSumByUserId
    @Query("SELECT SUM(" + ProductEntity.TABLE_NAME + "." + ProductEntity.PRICE + ") FROM " + ProductEntity.TABLE_NAME + " INNER JOIN " + CartEntity.TABLE_NAME + " ON " + ProductEntity.TABLE_NAME + "." + ProductEntity.ID + " = " + CartEntity.TABLE_NAME + "." + CartEntity.PRODUCT_ID + " WHERE " + CartEntity.TABLE_NAME + "." + CartEntity.USER_ID + " = :userId")
    Flowable<Double> getProductsPriceSumByUserId(long userId);


    // getProductsListPriceSumByUserId
    @Query("SELECT SUM(" + ProductEntity.TABLE_NAME + "." + ProductEntity.LIST_PRICE + ") FROM " + ProductEntity.TABLE_NAME + " INNER JOIN " + CartEntity.TABLE_NAME + " ON " + ProductEntity.TABLE_NAME + "." + ProductEntity.ID + " = " + CartEntity.TABLE_NAME + "." + CartEntity.PRODUCT_ID + " WHERE " + CartEntity.TABLE_NAME + "." + CartEntity.USER_ID + " = :userId")
    Flowable<Double> getProductsListPriceSumByUserId(long userId);

    // getCartProductIdsByUserId
    @Query("SELECT " + CartEntity.PRODUCT_ID + " FROM " + CartEntity.TABLE_NAME + " WHERE " + CartEntity.USER_ID + " = :userId")
    Flowable<List<Long>> getCartProductIdsByUserId(long userId);

}
