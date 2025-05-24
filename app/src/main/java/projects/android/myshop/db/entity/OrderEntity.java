package projects.android.myshop.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;


// represent order table
@Entity(tableName = OrderEntity.TABLE_NAME, foreignKeys = {@ForeignKey(entity = UserEntity.class, parentColumns = UserEntity.ID, childColumns = OrderEntity.USER_ID, onDelete = ForeignKey.CASCADE), @ForeignKey(entity = AddressEntity.class, parentColumns = AddressEntity.ID, childColumns = OrderEntity.ADDRESS_ID, onDelete = ForeignKey.CASCADE)}, indices = {@Index(OrderEntity.ID), @Index(OrderEntity.USER_ID), @Index(OrderEntity.ADDRESS_ID)})
public class OrderEntity {

    public static final String TABLE_NAME = "order_table";
    public static final String ID = "id";
    public static final String USER_ID = "user_id";
    public static final String ADDRESS_ID = "address_id";
    public static final String FIRST_PRODUCT_ID = "first_product_id";
    public static final String DATE_ORDERED = "date_ordered";
    public static final String ORDER_STATUS = "order_status";
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    private long id;
    @ColumnInfo(name = USER_ID)
    private long userId;
    @ColumnInfo(name = ADDRESS_ID)
    private long addressId;

    @ColumnInfo(name = FIRST_PRODUCT_ID)
    private long firstProductId;
    @ColumnInfo(name = DATE_ORDERED)
    private Date dateOrdered;
    @ColumnInfo(name = ORDER_STATUS)
    private long orderStatus;

    @Ignore
    public OrderEntity() {
    }

    public OrderEntity(long id, long userId, long addressId, long firstProductId, Date dateOrdered, long orderStatus) {
        this.id = id;
        this.userId = userId;
        this.addressId = addressId;
        this.firstProductId = firstProductId;
        this.dateOrdered = dateOrdered;
        this.orderStatus = orderStatus;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public long getFirstProductId() {
        return firstProductId;
    }

    public void setFirstProductId(long firstProductId) {
        this.firstProductId = firstProductId;
    }

    public Date getDateOrdered() {
        return dateOrdered;
    }

    public void setDateOrdered(Date dateOrdered) {
        this.dateOrdered = dateOrdered;
    }

    public long getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(long orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public String toString() {
        return "OrderEntity{" + "id=" + id + ", userId=" + userId + ", addressId=" + addressId + ", firstProductId=" + firstProductId + ", dateOrdered=" + dateOrdered + ", orderStatus=" + orderStatus + '}';
    }
}
