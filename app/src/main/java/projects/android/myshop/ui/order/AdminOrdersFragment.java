package projects.android.myshop.ui.order;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
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
import projects.android.myshop.databinding.FragmentAdminOrdersBinding;
import projects.android.myshop.db.entity.OrderEntity;
import projects.android.myshop.db.entity.ProductEntity;
import projects.android.myshop.model.OrderWithFirstProduct;
import projects.android.myshop.ui.base.BaseFragment;


public class AdminOrdersFragment extends BaseFragment implements OrderAdapter.OnOrderItemClickListener {
    private static final String TAG = "AdminOrdersFragment";

    private FragmentAdminOrdersBinding binding;
    private OrderViewModel orderViewModel;
    private OrderAdapter orderAdapter;


    public AdminOrdersFragment() {
        // Required empty public constructor
        super(R.layout.fragment_admin_orders);
    }

    @Override
    protected void initClicks() {

    }

    @Override
    protected void initViews(@NonNull View view) {
        binding = FragmentAdminOrdersBinding.bind(view);
        orderViewModel = new ViewModelProvider(this, new OrderViewModelFactory((ShopApplication) requireActivity().getApplication())).get(OrderViewModel.class);


        orderAdapter = new OrderAdapter(this);
        binding.setOrderAdapter(orderAdapter);
    }

    @Override
    protected void initMethods() {

    }

    @Override
    protected void subscribe() {
        disposable.add(orderViewModel.getAllOrders().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(orders -> {
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
            Log.e(TAG, orderProducts.toString());
            orderProducts.sort((o1, o2) -> o2.getOrder().getDateOrdered().compareTo(o1.getOrder().getDateOrdered()));
            orderAdapter.updateOrders(orderProducts);

        }, throwable -> logE(TAG, "Unable to get all products", throwable)));
    }


    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onOrderClicked(OrderWithFirstProduct order) {
        Intent intent = new Intent(requireContext(), OrderDetailsActivity.class);
        intent.putExtra(OrderDetailsActivity.ORDER_ID_EXTRA, order.getOrder().getId());
        startActivity(intent);
    }
}