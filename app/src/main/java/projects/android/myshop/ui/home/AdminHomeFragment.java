package projects.android.myshop.ui.home;

import android.content.Intent;
import android.os.Parcelable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import projects.android.myshop.R;
import projects.android.myshop.data.ShopApplication;
import projects.android.myshop.databinding.FragmentAdminHomeBinding;
import projects.android.myshop.db.entity.CategoryEntity;
import projects.android.myshop.ui.base.BaseFragment;
import projects.android.myshop.ui.category.AdminCategoriesDetailsActivity;
import projects.android.myshop.ui.category.CategoryViewModel;
import projects.android.myshop.ui.category.CategoryViewModelFactory;
import projects.android.myshop.utils.ActionType;


public class AdminHomeFragment extends BaseFragment implements HomeItemAdapter.HomeItemClickListener {
    private static final String TAG = "AdminHomeFragment";
    private FragmentAdminHomeBinding binding;
    private HomeItemAdapter homeItemAdapter;
    private CategoryViewModel categoryViewModel;

    public AdminHomeFragment() {
        // Required empty public constructor
        super(R.layout.fragment_admin_home);
    }

    @Override
    protected void initClicks() {
        // Initialize click listeners


    }

    @Override
    protected void initViews(@NonNull View view) {
        binding = FragmentAdminHomeBinding.bind(view);

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
                // If there are no categories, show appropriate views
                binding.recyclerview.setVisibility(View.GONE);
                binding.homeEmptyTv.setVisibility(View.VISIBLE);
            } else {
                // If there are categories, show appropriate views
                binding.homeEmptyTv.setVisibility(View.GONE);
                binding.recyclerview.setVisibility(View.VISIBLE);
            }
            homeItemAdapter.updateCategories(categoryEntities);
        }, throwable -> logE(TAG, "Unable to get Categories", throwable)));
    }


    @Override
    public void onDestroyView() {
        // Clean up binding
        binding = null;
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        // Handle click events
    }

    @Override
    public void onCategoryClicked(@NonNull CategoryEntity category) {
        // Open the admin categories details activity to update or delete a category
        Intent activityIntent = new Intent(requireContext(), AdminCategoriesDetailsActivity.class);
        activityIntent.putExtra(AdminCategoriesDetailsActivity.CATEGORY_EXTRA, category);
        activityIntent.putExtra(AdminCategoriesDetailsActivity.CATEGORY_ACTION_TYPE_EXTRA, (Parcelable) ActionType.UPDATE_DELETE);
        startActivity(activityIntent);
    }

}