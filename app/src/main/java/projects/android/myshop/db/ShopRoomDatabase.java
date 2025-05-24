package projects.android.myshop.db;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import projects.android.myshop.R;
import projects.android.myshop.db.converter.DateConverter;
import projects.android.myshop.db.converter.UriTypeConverter;
import projects.android.myshop.db.dao.AddressDao;
import projects.android.myshop.db.dao.CartDao;
import projects.android.myshop.db.dao.CategoryDao;
import projects.android.myshop.db.dao.OrderDao;
import projects.android.myshop.db.dao.OrderProductDao;
import projects.android.myshop.db.dao.ProductDao;
import projects.android.myshop.db.dao.UserDao;
import projects.android.myshop.db.entity.AddressEntity;
import projects.android.myshop.db.entity.CartEntity;
import projects.android.myshop.db.entity.CategoryEntity;
import projects.android.myshop.db.entity.OrderEntity;
import projects.android.myshop.db.entity.OrderProductEntity;
import projects.android.myshop.db.entity.ProductEntity;
import projects.android.myshop.db.entity.UserEntity;
import projects.android.myshop.utils.Util;

@Database(entities = {UserEntity.class, CategoryEntity.class, ProductEntity.class, OrderEntity.class, CartEntity.class, AddressEntity.class, OrderProductEntity.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class, UriTypeConverter.class})
public abstract class ShopRoomDatabase extends RoomDatabase {

    private static final String TAG = "ShopRoomDatabase";

    private static final String DATABASE_NAME = "shop_database";
    private static final int NUMBER_OF_THREADS = 20;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static volatile ShopRoomDatabase INSTANCE;

    private static void insertInitialCategoriesAndProducts(@NonNull CategoryDao categoryDao, @NonNull ProductDao productDao, @NonNull Context context) {

        CategoryEntity furniture = new CategoryEntity();
        furniture.setName("Furniture");
        furniture.setImageUri(getCategoryImageUri(context, R.drawable.cat_furniture));

        CategoryEntity skincare = new CategoryEntity();
        skincare.setName("Skincare");
        skincare.setImageUri(getCategoryImageUri(context, R.drawable.cat_skincare));

        CategoryEntity electronics = new CategoryEntity();
        electronics.setName("Electronics");
        electronics.setImageUri(getCategoryImageUri(context, R.drawable.cat_electronics));

        CategoryEntity fashion = new CategoryEntity();
        fashion.setName("Fashion");
        fashion.setImageUri(getCategoryImageUri(context, R.drawable.cat_fashion));

        List<CategoryEntity> initialCategories = List.of(furniture, skincare, electronics, fashion);

        Log.e(TAG, "launch");

        categoryDao.upsertAll(initialCategories).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(categoryIds -> {
            furniture.setId(categoryIds.get(0));
            skincare.setId(categoryIds.get(1));
            electronics.setId(categoryIds.get(2));
            fashion.setId(categoryIds.get(3));
            Log.d(TAG, "Initial categories inserted.");
            insertInitialProducts(context, productDao, furniture, skincare, electronics, fashion);
        }, throwable -> Log.e(TAG, "Initial categories insertion failed.", throwable));
    }

    private static void insertInitialProducts(@NonNull Context context, @NonNull ProductDao productDao, CategoryEntity furniture, CategoryEntity skincare, CategoryEntity electronics, CategoryEntity fashion) {

        Log.e(TAG, "insertInitialProducts");
        ProductEntity furniture1 = new ProductEntity();
        furniture1.setName("Oak Wooden Table");
        furniture1.setCategoryId(furniture.getId());
        furniture1.setDescription("Oak Console Table 100cm, which cleverly acts as a generous workspace for one then magically transforms into a dinner table for four thanks to the easy-slide open-close mechanism. This flexible piece sits perfectly snug along a wall but also opens up to twice its size and is ideally designed to make the most of space saving living ");
        furniture1.setImageUri(getProductImageUri(context, R.drawable.furniture_1));
        furniture1.setPrice(39D);
        furniture1.setRetailPrice(99D);
        furniture1.setListPrice(123D);
        furniture1.setDateCreate(new Date());
        furniture1.setDateUpdate(new Date());


        ProductEntity furniture2 = new ProductEntity();
        furniture2.setName("Glass Stainless Dining Table");
        furniture2.setCategoryId(furniture.getId());
        furniture2.setDescription("Vandana Interiors Modern Luxury Rectangular Dining Table Stone Top with Gold Stainless Steel Pedestal. ");
        furniture2.setImageUri(getProductImageUri(context, R.drawable.furniture_2));
        furniture2.setPrice(78D);
        furniture2.setRetailPrice(88D);
        furniture2.setListPrice(100D);
        furniture2.setDateCreate(new Date());
        furniture2.setDateUpdate(new Date());

        ProductEntity furniture3 = new ProductEntity();
        furniture3.setName("Wooden Bed Frame");
        furniture3.setCategoryId(furniture.getId());
        furniture3.setDescription("Salt and Pepper Timber Double Bed Frame with Pewter Highlights");
        furniture3.setImageUri(getProductImageUri(context, R.drawable.furniture_3));
        furniture3.setPrice(12D);
        furniture3.setRetailPrice(32D);
        furniture3.setListPrice(32D);
        furniture3.setDateCreate(new Date());
        furniture3.setDateUpdate(new Date());


        ProductEntity furniture4 = new ProductEntity();
        furniture4.setName("Halo Lounge Chair");
        furniture4.setCategoryId(furniture.getId());
        furniture4.setDescription("Halo combines retro design with contemporary finishes to bring you an on-trend and comfortable arm chair. With its brushed gold metal base and round, velvet upholstered seat, this piece is comfortable while still making a design statement");
        furniture4.setImageUri(getProductImageUri(context, R.drawable.furniture_4));
        furniture4.setPrice(43D);
        furniture4.setRetailPrice(45D);
        furniture4.setListPrice(45D);
        furniture4.setDateCreate(new Date());
        furniture4.setDateUpdate(new Date());

        ProductEntity skincare1 = new ProductEntity();
        skincare1.setName("Neutrogena oil free SPF 50 Sunscreen");
        skincare1.setCategoryId(skincare.getId());
        skincare1.setDescription("For face and body, this lightweight daily sunscreen lotion helps prevent sunburn without causing breakouts on acne-prone skin. The liquid-lotion has a water-light texture that leaves a weightless, matte finish so skin can breathe. ");
        skincare1.setImageUri(getProductImageUri(context, R.drawable.skincare_1));
        skincare1.setPrice(66D);
        skincare1.setRetailPrice(66D);
        skincare1.setListPrice(66D);
        skincare1.setDateCreate(new Date());
        skincare1.setDateUpdate(new Date());

        ProductEntity skincare2 = new ProductEntity();
        skincare2.setName("CeraVe Moisturizing Cream ");
        skincare2.setCategoryId(skincare.getId());
        skincare2.setDescription("CeraVe Moisturizing Cream is a rich, non-greasy, fast-absorbing moisturizer for normal to dry skin on the face and body. Formulated with three essential ceramides, it works to lock in skin's moisture and help maintain your skin’s protective barrier. Our moisturizing  cream also features hyaluronic acid, petrolatum, and MVE Delivery Technology");
        skincare2.setImageUri(getProductImageUri(context, R.drawable.skincare_2));
        skincare2.setPrice(89D);
        skincare2.setRetailPrice(89D);
        skincare2.setListPrice(123D);
        skincare2.setDateCreate(new Date());
        skincare2.setDateUpdate(new Date());

        ProductEntity skincare3 = new ProductEntity();
        skincare3.setName("CeraVe Hydrating Facial Cleanser");
        skincare3.setCategoryId(skincare.getId());
        skincare3.setDescription("CeraVe Hydrating Cleanser was designed to cleanse and refresh the skin without over-stripping it or leaving it feeling tight and dry. Featuring hyaluronic acid to hydrate the skin and 3 essential ceramides that work together to lock in skins moisture and help restore your skin’s protective barrier. MVE Delivery Technology");
        skincare3.setImageUri(getProductImageUri(context, R.drawable.skincare_3));
        skincare3.setPrice(89D);
        skincare3.setRetailPrice(90D);
        skincare3.setListPrice(100D);
        skincare3.setDateCreate(new Date());
        skincare3.setDateUpdate(new Date());

        ProductEntity skincare4 = new ProductEntity();
        skincare4.setName("Cetaphil Hydrating Eye Cream Serum");
        skincare4.setCategoryId(skincare.getId());
        skincare4.setDescription("Targeted eye cream that reduces the appearance of dark circles and puffy skin for a refreshed look. Formulated with Hyaluronic Acid, this cream deeply replenishes skin's hydration for 48 hours and     improves the overall quality of sensitive skin. ");
        skincare4.setImageUri(getProductImageUri(context, R.drawable.skincare_4));
        skincare4.setPrice(12D);
        skincare4.setRetailPrice(12D);
        skincare4.setListPrice(12D);
        skincare4.setDateCreate(new Date());
        skincare4.setDateUpdate(new Date());

        ProductEntity electronics1 = new ProductEntity();
        electronics1.setName("Hisense 50-Inch Smart TV");
        electronics1.setCategoryId(electronics.getId());
        electronics1.setDescription("With four times more pixels (8.3 million) than standard high-definition TV’s, the Hisense 50 inch VIDAA 4K Ultra HD Smart TV is built to deliver bright colors and rich contrast. HDR*technology maximizes brightness, the UHD Upscale brings lower resolution content as close to 4K as possible, and Motion Rate 120 keeps up with the fastest sports, movies and 4K gaming ");
        electronics1.setImageUri(getProductImageUri(context, R.drawable.electronics_1));
        electronics1.setPrice(43D);
        electronics1.setRetailPrice(50D);
        electronics1.setListPrice(65D);
        electronics1.setDateCreate(new Date());
        electronics1.setDateUpdate(new Date());

        ProductEntity electronics2 = new ProductEntity();
        electronics2.setName("RCA RFR741-BLACK Apartment Size-Top Freezer-2 Door Fridge-Adjustable Thermostat Control-Black-7.5 Cubic Feet ");
        electronics2.setCategoryId(electronics.getId());
        electronics2.setDescription(" the RCA Refrigerator. It comes with a 21 x 21 x 55 inches and a 7.5 Cu. Ft. Its unique and sleek layout fits flawlessly in your apartment, kitchen or dormitory..Freezer Capacity:1 cubic_feet.Fresh Food Capacity: 6 cubic_feet.Door Hinges : Reversible ");
        electronics2.setImageUri(getProductImageUri(context, R.drawable.electronics_2));
        electronics2.setPrice(100D);
        electronics2.setRetailPrice(120d);
        electronics2.setListPrice(199D);
        electronics2.setDateCreate(new Date());
        electronics2.setDateUpdate(new Date());

        ProductEntity electronics3 = new ProductEntity();
        electronics3.setName(" iPhone 12 ");
        electronics3.setCategoryId(electronics.getId());
        electronics3.setDescription("The iPhone 12 display has rounded corners that follow a beautiful curved design, and these corners are within a standard rectangle. When measured as a standard rectangular shape, the screen is 6.06 inches diagonally ");
        electronics3.setImageUri(getProductImageUri(context, R.drawable.electronics_3));
        electronics3.setPrice(123d);
        electronics3.setRetailPrice(124d);
        electronics3.setListPrice(222d);
        electronics3.setDateCreate(new Date());
        electronics3.setDateUpdate(new Date());

        ProductEntity fashion1 = new ProductEntity();
        fashion1.setName("MOD Floral Print Contrast Binding Cami Dress ");
        fashion1.setCategoryId(fashion.getId());
        fashion1.setDescription("Black and white boho style dress with a rushed dress regular fit polyester dress. ");
        fashion1.setImageUri(getProductImageUri(context, R.drawable.fashion_1));
        fashion1.setPrice(32d);
        fashion1.setRetailPrice(45d);
        fashion1.setListPrice(55d);
        fashion1.setDateCreate(new Date());
        fashion1.setDateUpdate(new Date());

        ProductEntity fashion2 = new ProductEntity();
        fashion2.setName("Two Tone Flap Baguette Bag");
        fashion2.setCategoryId(fashion.getId());
        fashion2.setDescription("Black and white medium baguette bag with 100% polyurethane.");
        fashion2.setImageUri(getProductImageUri(context, R.drawable.fashion_2));
        fashion2.setPrice(54d);
        fashion2.setRetailPrice(60d);
        fashion2.setListPrice(78d);
        fashion2.setDateCreate(new Date());
        fashion2.setDateUpdate(new Date());

        ProductEntity fashion3 = new ProductEntity();
        fashion3.setName("SHEIN EZwear High Waist Slit Denim Skirt");
        fashion3.setCategoryId(fashion.getId());
        fashion3.setDescription("Light wash plain long high waist regular fit slit non stretch Demin skirt with 69% Cotton, 19% Viscose, 12% Polyester. ");
        fashion3.setImageUri(getProductImageUri(context, R.drawable.fashion_3));
        fashion3.setPrice(89D);
        fashion3.setRetailPrice(99D);
        fashion3.setListPrice(120D);
        fashion3.setDateCreate(new Date());
        fashion3.setDateUpdate(new Date());

        List<ProductEntity> initialProducts = List.of(furniture1, furniture2, furniture3, furniture4, skincare1, skincare2, skincare3, skincare4, electronics1, electronics2, electronics3, fashion1, fashion2, fashion3);

        productDao.upsertAll(initialProducts).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
            Log.d(TAG, "Initial products inserted.");
        }, throwable -> Log.e(TAG, "Initial products insertion failed.", throwable));

    }

    private static Uri getCategoryImageUri(@NonNull Context context, @DrawableRes int id) {
        return Uri.parse(Util.saveCategoryDrawableToFile(context, id));
    }

    private static Uri getProductImageUri(@NonNull Context context, @DrawableRes int id) {
        return Uri.parse(Util.saveProductDrawableToFile(context, id));
    }


    public static ShopRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ShopRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ShopRoomDatabase.class, DATABASE_NAME).addCallback(new RoomDatabase.Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);

                            CategoryDao categoryDao = INSTANCE.categoryDao();
                            ProductDao productDao = INSTANCE.productDao();

                            insertInitialCategoriesAndProducts(categoryDao, productDao, context);
                        }
                    }).build();
                    INSTANCE.getOpenHelper().getWritableDatabase();
                }
            }
        }
        return INSTANCE;
    }

    public abstract CategoryDao categoryDao();

    public abstract AddressDao addressDao();

    public abstract OrderDao orderDao();

    public abstract ProductDao productDao();

    public abstract UserDao userDao();

    public abstract OrderProductDao orderProductDao();

    public abstract CartDao cartDao();


}
