package projects.android.myshop.db.dao;


import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Upsert;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import projects.android.myshop.db.entity.OrderProductEntity;
import projects.android.myshop.db.entity.ProductEntity;


// data access object for orders
@Dao
public interface OrderProductDao {

    // returning list of OrderProductEntity from productIds and orderId
    static List<OrderProductEntity> fromProductList(@NonNull List<Long> productIds, long orderId) {
        List<OrderProductEntity> orderProducts = new ArrayList<>();
        for (Long productId : productIds) {
            orderProducts.add(new OrderProductEntity(orderId, productId));
        }
        return orderProducts;
    }


    // insert or update all orders
    @Upsert
    Completable insertAll(@NonNull List<OrderProductEntity> orderProducts);

    // get all products by orderId
    @Query("SELECT * FROM " + OrderProductEntity.TABLE_NAME + " INNER JOIN " + ProductEntity.TABLE_NAME + " ON " + OrderProductEntity.TABLE_NAME + "." + OrderProductEntity.PRODUCT_ID + " = " + ProductEntity.TABLE_NAME + "." + ProductEntity.ID + " WHERE " + OrderProductEntity.ORDER_ID + " = :orderId")
    Flowable<List<ProductEntity>> getProductsForOrder(long orderId);

    // getProductsPriceSumByOrderId
    @Query("SELECT SUM(" + ProductEntity.TABLE_NAME + "." + ProductEntity.PRICE + ") AS total FROM " + OrderProductEntity.TABLE_NAME + " INNER JOIN " + ProductEntity.TABLE_NAME + " ON " + OrderProductEntity.TABLE_NAME + "." + OrderProductEntity.PRODUCT_ID + " = " + ProductEntity.TABLE_NAME + "." + ProductEntity.ID + " WHERE " + OrderProductEntity.TABLE_NAME + "." + OrderProductEntity.ORDER_ID + " = :orderId")
    Flowable<Double> getProductsPriceSumByOrderId(long orderId);


    // getProductsListPriceSumByOrderId
    @Query("SELECT SUM(" + ProductEntity.TABLE_NAME + "." + ProductEntity.LIST_PRICE + ") AS total FROM " + OrderProductEntity.TABLE_NAME + " INNER JOIN " + ProductEntity.TABLE_NAME + " ON " + OrderProductEntity.TABLE_NAME + "." + OrderProductEntity.PRODUCT_ID + " = " + ProductEntity.TABLE_NAME + "." + ProductEntity.ID + " WHERE " + OrderProductEntity.TABLE_NAME + "." + OrderProductEntity.ORDER_ID + " = :orderId")
    Flowable<Double> getProductsListPriceSumByOrderId(long orderId);


}
