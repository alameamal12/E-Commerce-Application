package projects.android.myshop.db.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Upsert;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import projects.android.myshop.db.entity.OrderEntity;
import projects.android.myshop.db.entity.ProductEntity;


// data access object for orders
@Dao
public interface OrderDao {

    // insert or update order
    @Upsert
    Single<Long> upsert(OrderEntity order);


    // get order by orderId
    @Query("SELECT * FROM " + OrderEntity.TABLE_NAME + " WHERE " + OrderEntity.ID + " = :orderId")
    Flowable<OrderEntity> getOrderById(long orderId);

    // get all orders by userId
    @Query("SELECT * FROM " + OrderEntity.TABLE_NAME + " WHERE " + OrderEntity.USER_ID + " = :userId ORDER BY " + OrderEntity.DATE_ORDERED + " DESC")
    Flowable<List<OrderEntity>> getOrdersByUserId(long userId);


    // getAllOrders
    @Query("SELECT * FROM " + OrderEntity.TABLE_NAME + " ORDER BY " + OrderEntity.DATE_ORDERED + " DESC")
    Flowable<List<OrderEntity>> getAllOrders();


    // getProductsByIds
    @Query("SELECT * FROM " + ProductEntity.TABLE_NAME + " WHERE " + ProductEntity.ID + " IN (:productIds)")
    Flowable<List<ProductEntity>> getProductsByIds(List<Long> productIds);


}
