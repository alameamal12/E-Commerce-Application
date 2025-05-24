package projects.android.myshop.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;


// represent user table
@Entity(tableName = UserEntity.TABLE_NAME)
public class UserEntity {

    public static final String TABLE_NAME = "user_table";
    public static final String ID = "id";
    public static final String FULL_NAME = "full_name";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String DATE_REGISTER = "date_register";
    public static final String DATE_UPDATE = "date_update";
    public static final String POSTCODE = "postcode";
    public static final String ADDRESS = "address";
    @PrimaryKey(autoGenerate = true)

    @ColumnInfo(name = ID)
    private long id;
    @ColumnInfo(name = FULL_NAME)
    private String fullName;
    @ColumnInfo(name = EMAIL)
    private String email;
    @ColumnInfo(name = PASSWORD)
    private String password;
    @ColumnInfo(name = DATE_REGISTER)
    private Date dateRegister;
    @ColumnInfo(name = DATE_UPDATE)
    private Date dateUpdate;
    @ColumnInfo(name = POSTCODE)
    private String postcode;
    @ColumnInfo(name = ADDRESS)
    private String address;

    @Ignore
    public UserEntity() {
    }

    public UserEntity(long id, @NonNull String fullName, @NonNull String email, @NonNull String password, @NonNull Date dateRegister, @NonNull Date dateUpdate, @NonNull String postcode, @NonNull String address) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.dateRegister = dateRegister;
        this.dateUpdate = dateUpdate;
        this.postcode = postcode;
        this.address = address;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDateRegister() {
        return dateRegister;
    }

    public void setDateRegister(Date dateRegister) {
        this.dateRegister = dateRegister;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}



