package projects.android.myshop.ui.category;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import projects.android.myshop.R;
import projects.android.myshop.data.ShopApplication;
import projects.android.myshop.databinding.FragmentUserCategoriesBinding;
import projects.android.myshop.db.entity.CategoryEntity;
import projects.android.myshop.ui.base.BaseFragment;
import projects.android.myshop.ui.product.UserProductsActivity;


public class UserCategoriesFragment extends BaseFragment implements CategoryAdapter.OnCategoryItemClickListener {
    private static final String TAG = "UserCategoriesFragment";
    private FragmentUserCategoriesBinding binding;
    private CategoryViewModel categoryViewModel;
    private CategoryAdapter categoryAdapter;

    public UserCategoriesFragment() {
        // Required empty public constructor
        super(R.layout.fragment_user_categories);
    }

    @Override
    protected void initClicks() {

    }

    @Override
    protected void initViews(@NonNull View view) {
        binding = FragmentUserCategoriesBinding.bind(view);

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
                binding.recyclerview.setVisibility(View.GONE);
                binding.categoryEmptyTv.setVisibility(View.VISIBLE);
            } else {
                binding.categoryEmptyTv.setVisibility(View.GONE);
                binding.recyclerview.setVisibility(View.VISIBLE);
            }
            categoryAdapter.updateCategory(categoryWithProductCounts);
        }, throwable -> logE(TAG, "Unable to get getAllCategoriesWithProductCount", throwable)));
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
        Intent activityIntent = new Intent(requireContext(), UserProductsActivity.class);
        activityIntent.putExtra(UserProductsActivity.CATEGORY_EXTRA, category);
        startActivity(activityIntent);
    }

}