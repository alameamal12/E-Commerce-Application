package projects.android.myshop.db.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


// represent address table
@Entity(tableName = AddressEntity.TABLE_NAME)
public class AddressEntity {
    public static final String TABLE_NAME = "address_table";
    public static final String ID = "id";
    public static final String USER_ID = "user_id";
    public static final String NAME = "name";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String POSTCODE = "postcode";
    public static final String STATE = "state";
    public static final String CITY = "city";
    public static final String HOME_ADDRESS = "home_address";
    public static final String AREA_ADDRESS = "area_address";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    private long addressId;
    @ColumnInfo(name = USER_ID)
    private long userId;

    @ColumnInfo(name = NAME)
    private String name;

    @ColumnInfo(name = PHONE_NUMBER)
    private String phoneNumber;

    @ColumnInfo(name = POSTCODE)
    private String postcode;

    @ColumnInfo(name = STATE)
    private String state;

    @ColumnInfo(name = CITY)
    private String city;

    @ColumnInfo(name = HOME_ADDRESS)
    private String homeAddress;

    @ColumnInfo(name = AREA_ADDRESS)
    private String areaAddress;

    @Ignore
    public AddressEntity() {
    }

    public AddressEntity(long addressId, long userId, String name, String phoneNumber, String postcode, String state, String city, String homeAddress, String areaAddress) {
        this.addressId = addressId;
        this.userId = userId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.postcode = postcode;
        this.state = state;
        this.city = city;
        this.homeAddress = homeAddress;
        this.areaAddress = areaAddress;
    }

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getAreaAddress() {
        return areaAddress;
    }

    public void setAreaAddress(String areaAddress) {
        this.areaAddress = areaAddress;
    }
}
