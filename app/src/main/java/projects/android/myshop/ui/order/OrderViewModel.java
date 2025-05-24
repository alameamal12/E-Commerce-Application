package projects.android.myshop.ui.order;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import projects.android.myshop.data.repository.OrderRepository;
import projects.android.myshop.db.entity.OrderEntity;
import projects.android.myshop.db.entity.ProductEntity;

// viewmodel for OrderViewModel
public class OrderViewModel extends ViewModel {

    private final OrderRepository orderRepository;

    public OrderViewModel(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    // insert or update order
    public Single<Long> upsert(@NonNull OrderEntity order) {
        return orderRepository.upsert(order);
    }


    // getAllOrders
    public Flowable<List<OrderEntity>> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    // get order by orderId
    public Flowable<OrderEntity> getOrderById(long orderId) {
        return orderRepository.getOrderById(orderId);
    }

    // get all orders by userId
    public Flowable<List<OrderEntity>> getOrdersByUserId(long userId) {
        return orderRepository.getOrdersByUserId(userId);
    }

    // getProductsByIds
    public Flowable<List<ProductEntity>> getProductsByIds(@NonNull List<Long> productIds) {
        return orderRepository.getProductsByIds(productIds);
    }

}
