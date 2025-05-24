package projects.android.myshop.ui.order;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import projects.android.myshop.R;
import projects.android.myshop.data.ShopApplication;
import projects.android.myshop.databinding.ActivityUserOrdersBinding;
import projects.android.myshop.db.entity.OrderEntity;
import projects.android.myshop.db.entity.ProductEntity;
import projects.android.myshop.model.OrderWithFirstProduct;
import projects.android.myshop.ui.base.BaseActivity;
import projects.android.myshop.utils.DataUtils;

public class UserOrdersActivity extends BaseActivity implements OrderAdapter.OnOrderItemClickListener {

    private static final String TAG = "UserOrdersActivity";
    private ActivityUserOrdersBinding binding;
    private OrderAdapter orderAdapter;
    private OrderViewModel orderViewModel;

    @Override
    public void onOrderClicked(OrderWithFirstProduct order) {
        Intent intent = new Intent(this, OrderDetailsActivity.class);
        intent.putExtra(OrderDetailsActivity.ORDER_ID_EXTRA, order.getOrder().getId());
        startActivity(intent);
    }

    @Override
    protected void initClicks() {

    }

    @Override
    protected void initViews() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_orders);

        setSupportActionBar(binding.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.my_orders);
        }

        orderViewModel = new ViewModelProvider(this, new OrderViewModelFactory((ShopApplication) getApplication())).get(OrderViewModel.class);


        orderAdapter = new OrderAdapter(this);
        binding.setOrderAdapter(orderAdapter);
    }

    @Override
    protected void initMethods() {

    }

    @Override
    protected void subscribe() {
        disposable.add(orderViewModel.getOrdersByUserId(DataUtils.getUserId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(orders -> {
            if (orders.isEmpty()) {
                binding.recyclerview.setVisibility(View.GONE);
                binding.orderEmptyTv.setVisibility(View.VISIBLE);
            } else {
                binding.orderEmptyTv.setVisibility(View.GONE);
                binding.recyclerview.setVisibility(View.VISIBLE);
            }
            getOrderWithFirstProducts(orders);
        }, throwable -> logE(TAG, "Unable to get all orders", throwable)));
    }

    private void getOrderWithFirstProducts(List<OrderEntity> orders) {

        Set<Long> productIds = new HashSet<>();
        for (OrderEntity order : orders) {
            productIds.add(order.getFirstProductId());
        }

        List<Long> productIdList = new ArrayList<>(productIds);


        disposable.add(orderViewModel.getProductsByIds(productIdList).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(products -> {

            Map<Long, ProductEntity> productMap = new HashMap<>();
            for (ProductEntity product : products) {
                productMap.put(product.getId(), product);
            }

            List<OrderWithFirstProduct> orderProducts = new ArrayList<>();

            for (OrderEntity order : orders) {
                long productId = order.getFirstProductId();
                if (productMap.containsKey(productId)) {
                    ProductEntity product = productMap.get(productId);
                    OrderWithFirstProduct orderProduct = new OrderWithFirstProduct(order, product);
                    orderProducts.add(orderProduct);
                }
            }
            orderProducts.sort((o1, o2) -> o2.getOrder().getDateOrdered().compareTo(o1.getOrder().getDateOrdered()));
            orderAdapter.updateOrders(orderProducts);

        }, throwable -> logE(TAG, "Unable to get all products", throwable)));
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}