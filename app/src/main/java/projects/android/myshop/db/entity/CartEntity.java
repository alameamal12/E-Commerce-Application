package projects.android.myshop.db.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;


// represent cart table
@Entity(tableName = CartEntity.TABLE_NAME, foreignKeys = {@ForeignKey(entity = UserEntity.class, parentColumns = UserEntity.ID, childColumns = CartEntity.USER_ID, onDelete = ForeignKey.CASCADE), @ForeignKey(entity = ProductEntity.class, parentColumns = ProductEntity.ID, childColumns = CartEntity.PRODUCT_ID, onDelete = ForeignKey.CASCADE)}, indices = {@Index(CartEntity.ID), @Index(CartEntity.PRODUCT_ID), @Index(CartEntity.USER_ID)})

public class CartEntity {
    public static final String TABLE_NAME = "cart_table";
    public static final String ID = "id";
    public static final String USER_ID = "user_id";
    public static final String PRODUCT_ID = "product_id";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    private long cartId;
    @ColumnInfo(name = USER_ID)
    private long userId;
    @ColumnInfo(name = PRODUCT_ID)
    private long productId;


    @Ignore
    public CartEntity() {
    }

    public CartEntity(long cartId, long userId, long productId) {
        this.cartId = cartId;
        this.userId = userId;
        this.productId = productId;
    }

    public long getCartId() {
        return cartId;
    }

    public void setCartId(long cartId) {
        this.cartId = cartId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
}
