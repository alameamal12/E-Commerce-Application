package projects.android.myshop.model;

import androidx.annotation.NonNull;

import projects.android.myshop.db.entity.OrderEntity;
import projects.android.myshop.db.entity.ProductEntity;


// model for order with the first product in that order
public class OrderWithFirstProduct {
    private OrderEntity order;

    private ProductEntity firstProduct;

    public OrderWithFirstProduct(OrderEntity order, ProductEntity firstProduct) {
        this.order = order;
        this.firstProduct = firstProduct;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public ProductEntity getFirstProduct() {
        return firstProduct;
    }

    public void setFirstProduct(ProductEntity firstProduct) {
        this.firstProduct = firstProduct;
    }

    @NonNull
    @Override
    public String toString() {
        return "OrderWithFirstProduct{" + "order=" + order + ", firstProduct=" + firstProduct + '}';
    }
}
