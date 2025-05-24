package projects.android.myshop.ui.order;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import projects.android.myshop.data.repository.OrderProductRepository;
import projects.android.myshop.db.entity.OrderProductEntity;
import projects.android.myshop.db.entity.ProductEntity;

public class OrderProductViewModel extends ViewModel {

    private final OrderProductRepository orderProductRepository;

    public OrderProductViewModel(OrderProductRepository orderProductRepository) {
        this.orderProductRepository = orderProductRepository;
    }

    // insert or update all orders
    public Completable insertAll(@NonNull List<OrderProductEntity> orderProducts) {
        return orderProductRepository.insertAll(orderProducts);
    }

    // get all products by orderId
    public Flowable<List<ProductEntity>> getProductsForOrder(long orderId) {
        return orderProductRepository.getProductsForOrder(orderId);
    }


    // getProductsPriceSumByOrderId
    public Flowable<Double> getProductsPriceSumByOrderId(long orderId) {
        return orderProductRepository.getProductsPriceSumByOrderId(orderId);
    }

    // getProductsListPriceSumByOrderId
    public Flowable<Double> getProductsListPriceSumByOrderId(long orderId) {
        return orderProductRepository.getProductsListPriceSumByOrderId(orderId);
    }


}
