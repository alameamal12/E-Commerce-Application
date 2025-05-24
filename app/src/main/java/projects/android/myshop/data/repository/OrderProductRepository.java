package projects.android.myshop.data.repository;

import androidx.annotation.NonNull;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import projects.android.myshop.db.ShopRoomDatabase;
import projects.android.myshop.db.dao.OrderProductDao;
import projects.android.myshop.db.entity.OrderProductEntity;
import projects.android.myshop.db.entity.ProductEntity;

public class OrderProductRepository {

    private static volatile OrderProductRepository instance;
    private final OrderProductDao orderProductDao;

    public OrderProductRepository(final ShopRoomDatabase database) {
        orderProductDao = database.orderProductDao();
    }

    public static OrderProductRepository getInstance(final ShopRoomDatabase database) {
        if (instance == null) {
            synchronized (OrderProductRepository.class) {
                if (instance == null) {
                    instance = new OrderProductRepository(database);
                }
            }
        }
        return instance;
    }

    // insert or update all orders
    public Completable insertAll(@NonNull List<OrderProductEntity> orderProducts) {
        return orderProductDao.insertAll(orderProducts);
    }

    // get all products by orderId
    public Flowable<List<ProductEntity>> getProductsForOrder(long orderId) {
        return orderProductDao.getProductsForOrder(orderId);
    }

    // getProductsPriceSumByOrderId
    public Flowable<Double> getProductsPriceSumByOrderId(long orderId) {
        return orderProductDao.getProductsPriceSumByOrderId(orderId);
    }

    // getProductsListPriceSumByOrderId
    public Flowable<Double> getProductsListPriceSumByOrderId(long orderId) {
        return orderProductDao.getProductsListPriceSumByOrderId(orderId);
    }


}
