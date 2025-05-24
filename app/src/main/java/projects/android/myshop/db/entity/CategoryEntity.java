package projects.android.myshop.db.entity;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


// represent category table
@Entity(tableName = CategoryEntity.TABLE_NAME)
public class CategoryEntity implements Parcelable {

    public static final String TABLE_NAME = "category_table";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String IMAGE_URI = "image_uri";
    public static final Creator<CategoryEntity> CREATOR = new Creator<>() {
        @Override
        public CategoryEntity createFromParcel(Parcel in) {
            return new CategoryEntity(in);
        }

        @Override
        public CategoryEntity[] newArray(int size) {
            return new CategoryEntity[size];
        }
    };
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    private long id;
    @ColumnInfo(name = NAME)
    private String name;
    @ColumnInfo(name = IMAGE_URI)
    private Uri imageUri;

    @Ignore
    public CategoryEntity() {
    }

    public CategoryEntity(long id, String name, Uri imageUri) {
        this.id = id;
        this.name = name;
        this.imageUri = imageUri;
    }

    protected CategoryEntity(Parcel in) {
        id = in.readLong();
        name = in.readString();
        imageUri = in.readParcelable(Uri.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeParcelable(imageUri, flags);
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

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}
