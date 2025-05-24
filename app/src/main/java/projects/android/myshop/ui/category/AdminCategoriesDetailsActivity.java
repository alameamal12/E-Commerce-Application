package projects.android.myshop.ui.category;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import projects.android.myshop.R;
import projects.android.myshop.data.ShopApplication;
import projects.android.myshop.databinding.ActivityAdminCategoriesDetailsBinding;
import projects.android.myshop.db.entity.CategoryEntity;
import projects.android.myshop.db.entity.ProductEntity;
import projects.android.myshop.ui.base.BaseActivity;
import projects.android.myshop.ui.product.AdminProductDetailsActivity;
import projects.android.myshop.ui.product.ProductAdapter;
import projects.android.myshop.ui.product.ProductViewModel;
import projects.android.myshop.ui.product.ProductViewModelFactory;
import projects.android.myshop.utils.ActionType;
import projects.android.myshop.utils.FileUtil;
import projects.android.myshop.utils.ImagePickerUtils;
import projects.android.myshop.utils.Util;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class AdminCategoriesDetailsActivity extends BaseActivity implements ProductAdapter.OnProductItemClickListener, ImagePickerUtils.OnImageSelectedListener, EasyPermissions.PermissionCallbacks {

    public static final String CATEGORY_EXTRA = "category_extra";
    public static final String CATEGORY_ACTION_TYPE_EXTRA = "category_action_type_extra";
    public static final int READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 12;
    private static final String TAG = "AdminCategoriesDetailsActivity";
    private Uri selectedImageUri;
    private ActivityAdminCategoriesDetailsBinding binding;
    private ProductViewModel productViewModel;
    private CategoryViewModel categoryViewModel;
    private ProductAdapter productAdapter;
    private CategoryEntity category;
    private ActionType categoryActionType;

    @Override
    protected void initClicks() {
        // Initialize click listeners
        binding.addProductBtn.setOnClickListener(this);
        binding.categoryCard.setOnClickListener(this);
        binding.btn.setOnClickListener(this);
    }

    @Override
    protected void initViews() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_categories_details);
        setSupportActionBar(binding.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        productViewModel = new ViewModelProvider(this, new ProductViewModelFactory((ShopApplication) getApplication())).get(ProductViewModel.class);
        categoryViewModel = new ViewModelProvider(this, new CategoryViewModelFactory((ShopApplication) getApplication())).get(CategoryViewModel.class);

        addTextChangerListeners();

        if (getIntent() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                categoryActionType = getIntent().getParcelableExtra(CATEGORY_ACTION_TYPE_EXTRA, ActionType.class);
            } else {
                categoryActionType = getIntent().getParcelableExtra(CATEGORY_ACTION_TYPE_EXTRA);
            }

            if (categoryActionType.equals(ActionType.ADD)) {
                if (actionBar != null) {
                    actionBar.setTitle(R.string.add_category);
                }
                binding.addProductBtn.setVisibility(View.GONE);
                binding.btn.setText(R.string.add_category);
                binding.productsRecyclerview.setVisibility(View.GONE);
            } else {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    category = getIntent().getParcelableExtra(CATEGORY_EXTRA, CategoryEntity.class);
                } else {
                    category = getIntent().getParcelableExtra(CATEGORY_EXTRA);
                }

                selectedImageUri = category.getImageUri();
                binding.categoryImg.setImageURI(selectedImageUri);

                if (actionBar != null) {
                    actionBar.setTitle(R.string.category_details);
                }
                binding.btn.setText(R.string.update_category);

                binding.productsRecyclerview.setVisibility(View.VISIBLE);
            }

        }


        productAdapter = new ProductAdapter(this);
        binding.setProductAdapter(productAdapter);
        binding.setCategory(category);

    }

    private void addTextChangerListeners() {
        // Add listeners to track text changes
        afterTextChanged(binding.titleEt, binding.titleInputLayout, null);
    }

    @Override
    protected void initMethods() {
        // Initialize any additional methods

    }

    @Override
    protected void subscribe() {
        if (categoryActionType.equals(ActionType.UPDATE_DELETE)) {
            disposable.add(productViewModel.getProductsByCategoryId(category.getId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(products -> productAdapter.updateProducts(products), throwable -> logE(TAG, "Unable to get products", throwable)));
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (binding.addProductBtn.getId() == id) {
            // Open the admin product details activity to add a new product
            Intent intent = new Intent(this, AdminProductDetailsActivity.class);
            intent.putExtra(AdminProductDetailsActivity.PRODUCT_ACTION_TYPE_EXTRA, (Parcelable) ActionType.ADD);
            intent.putExtra(AdminProductDetailsActivity.CATEGORY_ID_EXTRA, category.getId());
            startActivity(intent);
        } else if (binding.categoryCard.getId() == id) {
            // Handle the category image selection

            if (Util.isPermissionGranted(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ImagePickerUtils.pickImage(this, "Select Category Image");
            } else {
                requestReadExternalStoragePermission();
            }
        } else if (binding.btn.getId() == id) {
            // Add or update the category

            addOrUpdateCategory();
        }
    }

    private void addOrUpdateCategory() {

        if (selectedImageUri == null) {
            makeToast("Please select a category image.");
            return;
        }

        AppCompatEditText titleEt = binding.titleEt;

        if (titleEt.getText() == null) {
            makeToast("Something went wrong");
            return;
        }

        String title = titleEt.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            binding.titleInputLayout.setError("Please enter your category title");
            return;
        }

        binding.btn.setEnabled(false);

        boolean isDataChanged, isImageUpdateAddNeeded = false;
        File image = null;

        if (categoryActionType.equals(ActionType.ADD)) {
            isImageUpdateAddNeeded = true;
        } else {
            if (!selectedImageUri.equals(category.getImageUri())) {
                isImageUpdateAddNeeded = true;
            }
        }

        if (isImageUpdateAddNeeded) {
            try {
                image = new File(FileUtil.createCategoryImagesDirectory(this), System.currentTimeMillis() + ".jpeg");
                FileUtil.copyFile(FileUtil.getFileFromUri(this, selectedImageUri), image);
                image.isFile();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                if (image != null) {
                    image.delete();
                }
                binding.btn.setEnabled(true);
                makeToast("Something went wrong");
                return;
            }
        }

        if (categoryActionType.equals(ActionType.UPDATE_DELETE)) {
            isDataChanged = isDataChanged(title, selectedImageUri);
        } else {
            isDataChanged = true;
            category = new CategoryEntity();
        }


        if (isDataChanged) {

            category.setName(title);

            if (isImageUpdateAddNeeded) {
                category.setImageUri(Uri.fromFile(image));
            }

            disposable.add(categoryViewModel.upsert(category).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                binding.btn.setEnabled(true);
                if (categoryActionType.equals(ActionType.ADD)) {
                    makeToast("Successfully category added");
                } else {
                    makeToast("Category details successfully updated");
                }
                finish();
            }, throwable -> logE(TAG, "Unable to upsert category", throwable)));


        } else {
            finish();
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.delete && categoryActionType != ActionType.ADD) {
            showConfirmationDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (categoryActionType == ActionType.UPDATE_DELETE) {
            getMenuInflater().inflate(R.menu.delete_option_menu, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    private void deleteCategory() {
        disposable.add(categoryViewModel.delete(category).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
            try {
                File image = FileUtil.getFileFromUri(AdminCategoriesDetailsActivity.this, binding.getCategory().getImageUri());
                if (image != null && image.exists()) {
                    image.delete();
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            makeToast("Category Successfully Deleted.");
            finish();
        }, throwable -> logE(TAG, "Unable to delete category", throwable)));
    }


    private boolean isDataChanged(@NonNull String title, @NonNull Uri selectedImageUri) {
        if (!title.equals(category.getName())) {
            return true;
        }

        return !selectedImageUri.equals(category.getImageUri());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImagePickerUtils.onActivityResult(requestCode, resultCode, data, this);
    }

    @Override
    public void onProductClicked(@NonNull ProductEntity product) {
        Intent intent = new Intent(this, AdminProductDetailsActivity.class);
        intent.putExtra(AdminProductDetailsActivity.PRODUCT_EXTRA, product);
        intent.putExtra(AdminProductDetailsActivity.PRODUCT_ACTION_TYPE_EXTRA, (Parcelable) ActionType.UPDATE_DELETE);
        startActivity(intent);
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to delete this category?");
        builder.setPositiveButton("Delete", (dialog, which) -> deleteCategory());
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onImageSelected(@Nullable Uri selectedImageUri) {
        this.selectedImageUri = selectedImageUri;
        binding.categoryImg.setImageURI(selectedImageUri);
    }

    private void requestReadExternalStoragePermission() {
        String[] perms = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            PermissionRequest request = new PermissionRequest.Builder(
                    this,
                    READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE,
                    perms
            ).setRationale(R.string.read_external_storage_permission_explain_msg).build();
            EasyPermissions.requestPermissions(request);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE) {
            if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                new AppSettingsDialog.Builder(this).setRationale(getString(R.string.read_external_storage_permission_explain_msg)).build().show();
            }
        }
    }
}