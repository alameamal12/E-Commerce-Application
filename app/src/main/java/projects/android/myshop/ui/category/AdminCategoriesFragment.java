package projects.android.myshop.ui.category;

import android.content.Intent;
import android.os.Parcelable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import projects.android.myshop.R;
import projects.android.myshop.data.ShopApplication;
import projects.android.myshop.databinding.FragmentAdminCategoriesBinding;
import projects.android.myshop.db.entity.CategoryEntity;
import projects.android.myshop.ui.base.BaseFragment;
import projects.android.myshop.utils.ActionType;


public class AdminCategoriesFragment extends BaseFragment implements CategoryAdapter.OnCategoryItemClickListener {
    private static final String TAG = "AdminCategoriesFragment";
    private FragmentAdminCategoriesBinding binding;
    private CategoryViewModel categoryViewModel;
    private CategoryAdapter categoryAdapter;

    public AdminCategoriesFragment() {
        // Required empty public constructor
        super(R.layout.fragment_admin_categories);
    }

    @Override
    protected void initClicks() {
        // Initialize click listeners
        binding.addCategoryBtn.setOnClickListener(this);
    }

    @Override
    protected void initViews(@NonNull View view) {
        binding = FragmentAdminCategoriesBinding.bind(view);
        categoryViewModel = new ViewModelProvider(this, new CategoryViewModelFactory((ShopApplication) requireActivity().getApplication())).get(CategoryViewModel.class);

        categoryAdapter = new CategoryAdapter(this);
        binding.setCategoryAdapter(categoryAdapter);
    }

    @Override
    protected void initMethods() {

    }

    @Override
    protected void subscribe() {
        disposable.add(categoryViewModel.getAllCategoriesWithProductCount().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(categoryWithProductCounts -> {
            if (categoryWithProductCounts.isEmpty()) {
                // If there are no categories with products, show appropriate views
                binding.recyclerview.setVisibility(View.GONE);
                binding.categoryEmptyTv.setVisibility(View.VISIBLE);
            } else {
                // If there are categories with products, show appropriate views
                binding.categoryEmptyTv.setVisibility(View.GONE);
                binding.recyclerview.setVisibility(View.VISIBLE);
            }
            categoryAdapter.updateCategory(categoryWithProductCounts);
        }, throwable -> logE(TAG, "Unable to get getAllCategoriesWithProductCount", throwable)));
    }


    @Override
    public void onDestroyView() {
        // Clean up binding

        binding = null;
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == binding.addCategoryBtn.getId()) {
            // Open the admin categories details activity to add a new category
            Intent activityIntent = new Intent(requireContext(), AdminCategoriesDetailsActivity.class);
            activityIntent.putExtra(AdminCategoriesDetailsActivity.CATEGORY_ACTION_TYPE_EXTRA, (Parcelable) ActionType.ADD);
            startActivity(activityIntent);
        }
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