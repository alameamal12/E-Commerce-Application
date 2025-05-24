package projects.android.myshop.db.entity;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;


// represent order product table
@Entity(tableName = OrderProductEntity.TABLE_NAME, primaryKeys = {OrderProductEntity.ORDER_ID, OrderProductEntity.PRODUCT_ID}, foreignKeys = {@ForeignKey(entity = OrderEntity.class, parentColumns = OrderEntity.ID, childColumns = OrderProductEntity.ORDER_ID, onDelete = ForeignKey.CASCADE), @ForeignKey(entity = ProductEntity.class, parentColumns = ProductEntity.ID, childColumns = OrderProductEntity.PRODUCT_ID, onDelete = ForeignKey.CASCADE)}, indices = {@Index(OrderProductEntity.ORDER_ID), @Index(OrderProductEntity.PRODUCT_ID)})
public class OrderProductEntity {
    public final static String TABLE_NAME = "order_product_table";
    public final static String ORDER_ID = "order_id";
    public final static String PRODUCT_ID = "product_id";

    @ColumnInfo(name = ORDER_ID)
    @NonNull
    private Long orderId;
    @ColumnInfo(name = PRODUCT_ID)
    @NonNull
    private Long productId;

    @Ignore
    public OrderProductEntity() {
    }

    public OrderProductEntity(@NonNull Long orderId, @NonNull Long productId) {
        this.orderId = orderId;
        this.productId = productId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
