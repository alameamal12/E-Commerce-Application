package projects.android.myshop.ui.home;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import projects.android.myshop.R;
import projects.android.myshop.data.ShopApplication;
import projects.android.myshop.databinding.FragmentUserHomeBinding;
import projects.android.myshop.db.entity.CategoryEntity;
import projects.android.myshop.ui.base.BaseFragment;
import projects.android.myshop.ui.category.CategoryViewModel;
import projects.android.myshop.ui.category.CategoryViewModelFactory;
import projects.android.myshop.ui.product.UserProductsActivity;

public class UserHomeFragment extends BaseFragment implements HomeItemAdapter.HomeItemClickListener {
    private static final String TAG = "UserCategoriesFragment";

    private FragmentUserHomeBinding binding;
    private HomeItemAdapter homeItemAdapter;
    private CategoryViewModel categoryViewModel;

    public UserHomeFragment() {
        // Required empty public constructor
        super(R.layout.fragment_user_home);
    }

    @Override
    protected void initClicks() {

    }

    @Override
    protected void initViews(@NonNull View view) {
        binding = FragmentUserHomeBinding.bind(view);

        categoryViewModel = new ViewModelProvider(this, new CategoryViewModelFactory((ShopApplication) requireActivity().getApplication())).get(CategoryViewModel.class);

        homeItemAdapter = new HomeItemAdapter(this);
        binding.setHomeItemAdapter(homeItemAdapter);
    }

    @Override
    protected void initMethods() {

    }

    @Override
    protected void subscribe() {
        disposable.add(categoryViewModel.getAllCategories().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(categoryEntities -> {
            if (categoryEntities.isEmpty()) {
                // Hide the RecyclerView and show the empty message
                binding.recyclerview.setVisibility(View.GONE);
                binding.homeEmptyTv.setVisibility(View.VISIBLE);
            } else {
                // Show the RecyclerView and hide the empty message
                binding.homeEmptyTv.setVisibility(View.GONE);
                binding.recyclerview.setVisibility(View.VISIBLE);
            }
            // Update the category list in the adapter
            homeItemAdapter.updateCategories(categoryEntities);
        }, throwable -> logE(TAG, "Unable to get Categories", throwable)));
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
    public void onCategoryClicked(@NonNull CategoryEntity category) {
        // Handle category click event by starting UserProductsActivity
        Intent activityIntent = new Intent(requireContext(), UserProductsActivity.class);
        activityIntent.putExtra(UserProductsActivity.CATEGORY_EXTRA, category);
        startActivity(activityIntent);
    }


}