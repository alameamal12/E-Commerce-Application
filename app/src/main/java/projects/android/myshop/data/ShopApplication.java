package projects.android.myshop.data;

import android.app.Application;

import projects.android.myshop.data.repository.AddressRepository;
import projects.android.myshop.data.repository.CartRepository;
import projects.android.myshop.data.repository.CategoryRepository;
import projects.android.myshop.data.repository.OrderProductRepository;
import projects.android.myshop.data.repository.OrderRepository;
import projects.android.myshop.data.repository.ProductRepository;
import projects.android.myshop.data.repository.UserRepository;
import projects.android.myshop.db.ShopRoomDatabase;
import projects.android.myshop.utils.DataUtils;


// Application for whole app
public class ShopApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        DataUtils.initialize(this);
    }

    // getting database instance
    public ShopRoomDatabase database() {
        return ShopRoomDatabase.getDatabase(this);
    }

    // getting UserRepository instance
    public UserRepository userRepository() {
        return UserRepository.getInstance(database());
    }

    // getting ProductRepository instance
    public ProductRepository productRepository() {
        return ProductRepository.getInstance(database());
    }

    // getting OrderRepository instance
    public OrderRepository orderRepository() {
        return OrderRepository.getInstance(database());
    }

    // getting OrderProductRepository instance
    public OrderProductRepository orderProductRepository() {
        return OrderProductRepository.getInstance(database());
    }

    // getting CategoryRepository instance
    public CategoryRepository categoryRepository() {
        return CategoryRepository.getInstance(database());
    }

    // getting CartRepository instance
    public CartRepository cartRepository() {
        return CartRepository.getInstance(database());
    }

    // getting AddressRepository instance
    public AddressRepository addressRepository() {
        return AddressRepository.getInstance(database());
    }


}
