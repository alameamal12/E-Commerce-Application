package projects.android.myshop.db.entity;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;


// represent product table
@Entity(tableName = ProductEntity.TABLE_NAME, foreignKeys = @ForeignKey(entity = CategoryEntity.class, parentColumns = CategoryEntity.ID, childColumns = ProductEntity.CATEGORY_ID, onDelete = ForeignKey.CASCADE), indices = {@Index(ProductEntity.ID), @Index(ProductEntity.CATEGORY_ID)})
public class ProductEntity implements Parcelable {

    public static final String TABLE_NAME = "product_table";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String DESC = "desc";
    public static final String PRICE = "price";
    public static final String LIST_PRICE = "list_price";
    public static final String RETAIL_PRICE = "retail_price";
    public static final String DATE_CREATE = "date_create";
    public static final String DATE_UPDATE = "date_update";
    public static final String CATEGORY_ID = "category_id";
    public static final String IMAGE_URL = "image_uri";
    public static final Creator<ProductEntity> CREATOR = new Creator<>() {
        @Override
        public ProductEntity createFromParcel(Parcel in) {
            return new ProductEntity(in);
        }

        @Override
        public ProductEntity[] newArray(int size) {
            return new ProductEntity[size];
        }
    };
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    private long id;
    @ColumnInfo(name = NAME)
    private String name;
    @ColumnInfo(name = DESC)
    private String description;
    @ColumnInfo(name = IMAGE_URL)
    private Uri imageUri;
    @ColumnInfo(name = PRICE)
    private Double price;
    @ColumnInfo(name = LIST_PRICE)
    private Double listPrice;
    @ColumnInfo(name = RETAIL_PRICE)
    private Double retailPrice;
    @ColumnInfo(name = DATE_CREATE)
    private Date dateCreate;
    @ColumnInfo(name = DATE_UPDATE)
    private Date dateUpdate;
    @ColumnInfo(name = CATEGORY_ID)
    private long categoryId;

    @Ignore
    public ProductEntity() {
    }

    public ProductEntity(long id, @NonNull String name, @NonNull String description, @NonNull Uri imageUri, @NonNull Double price, @NonNull Double listPrice, @NonNull Double retailPrice, @NonNull Date dateCreate, @NonNull Date dateUpdate, long categoryId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUri = imageUri;
        this.price = price;
        this.listPrice = listPrice;
        this.retailPrice = retailPrice;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;
        this.categoryId = categoryId;
    }

    protected ProductEntity(Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        imageUri = in.readParcelable(Uri.class.getClassLoader());
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readDouble();
        }
        if (in.readByte() == 0) {
            listPrice = null;
        } else {
            listPrice = in.readDouble();
        }
        if (in.readByte() == 0) {
            retailPrice = null;
        } else {
            retailPrice = in.readDouble();
        }
        long tmpDateCreate = in.readLong();
        dateCreate = tmpDateCreate != -1 ? new Date(tmpDateCreate) : null;
        long tmpDateUpdate = in.readLong();
        dateUpdate = tmpDateUpdate != -1 ? new Date(tmpDateUpdate) : null;
        categoryId = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeParcelable(imageUri, flags);
        if (price != null) {
            dest.writeByte((byte) 1);
            dest.writeDouble(price);
        } else {
            dest.writeByte((byte) 0);
        }
        if (listPrice != null) {
            dest.writeByte((byte) 1);
            dest.writeDouble(listPrice);
        } else {
            dest.writeByte((byte) 0);
        }
        if (retailPrice != null) {
            dest.writeByte((byte) 1);
            dest.writeDouble(retailPrice);
        } else {
            dest.writeByte((byte) 0);
        }
        dest.writeLong(dateCreate != null ? dateCreate.getTime() : -1);
        dest.writeLong(dateUpdate != null ? dateUpdate.getTime() : -1);
        dest.writeLong(categoryId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getListPrice() {
        return listPrice;
    }

    public void setListPrice(Double listPrice) {
        this.listPrice = listPrice;
    }

    public Double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(Double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    @NonNull
    @Override
    public String toString() {
        return "ProductEntity{" + "id=" + id + ", name='" + name + '\'' + ", description='" + description + '\'' + ", imageUri=" + imageUri + ", price=" + price + ", listPrice=" + listPrice + ", retailPrice=" + retailPrice + ", dateCreate=" + dateCreate + ", dateUpdate=" + dateUpdate + ", categoryId=" + categoryId + '}';
    }
}
