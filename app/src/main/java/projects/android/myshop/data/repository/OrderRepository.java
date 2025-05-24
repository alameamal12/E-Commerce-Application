package projects.android.myshop.data.repository;

import androidx.annotation.NonNull;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import projects.android.myshop.db.ShopRoomDatabase;
import projects.android.myshop.db.dao.OrderDao;
import projects.android.myshop.db.entity.OrderEntity;
import projects.android.myshop.db.entity.ProductEntity;

public class OrderRepository {

    private static volatile OrderRepository instance;
    private final OrderDao orderDao;

    public OrderRepository(final ShopRoomDatabase database) {
        orderDao = database.orderDao();
    }

    public static OrderRepository getInstance(final ShopRoomDatabase database) {
        if (instance == null) {
            synchronized (OrderRepository.class) {
                if (instance == null) {
                    instance = new OrderRepository(database);
                }
            }
        }
        return instance;
    }


    // insert or update order
    public Single<Long> upsert(@NonNull OrderEntity order) {
        return orderDao.upsert(order);
    }

    // getAllOrders
    public Flowable<List<OrderEntity>> getAllOrders() {
        return orderDao.getAllOrders();
    }


    // get order by orderId
    public Flowable<OrderEntity> getOrderById(long orderId) {
        return orderDao.getOrderById(orderId);
    }


    // get all orders by userId
    public Flowable<List<OrderEntity>> getOrdersByUserId(long userId) {
        return orderDao.getOrdersByUserId(userId);
    }

    // getProductsByIds
    public Flowable<List<ProductEntity>> getProductsByIds(@NonNull List<Long> productIds) {
        return orderDao.getProductsByIds(productIds);
    }

}
