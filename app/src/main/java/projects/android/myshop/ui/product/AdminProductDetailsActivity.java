package projects.android.myshop.ui.product;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import projects.android.myshop.R;
import projects.android.myshop.data.ShopApplication;
import projects.android.myshop.databinding.ActivityAdminProductDetailsBinding;
import projects.android.myshop.db.entity.ProductEntity;
import projects.android.myshop.ui.base.BaseActivity;
import projects.android.myshop.utils.ActionType;
import projects.android.myshop.utils.FileUtil;
import projects.android.myshop.utils.ImagePickerUtils;
import projects.android.myshop.utils.Util;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class AdminProductDetailsActivity extends BaseActivity implements ImagePickerUtils.OnImageSelectedListener, EasyPermissions.PermissionCallbacks {
    public static final String PRODUCT_EXTRA = "product_extra";
    public static final int READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 12;
    public static final String CATEGORY_ID_EXTRA = "category_id_extra";
    public static final String PRODUCT_ACTION_TYPE_EXTRA = "product_action_type_extra";
    private static final String TAG = "AdminProductDetailsActivity";
    private long categoryId;
    private ActivityAdminProductDetailsBinding binding;
    private Uri selectedImageUri;
    private ProductViewModel productViewModel;
    private ProductEntity product;
    private ActionType productActionType;

    @Override
    protected void initClicks() {
        binding.productCard.setOnClickListener(this);
        binding.btn.setOnClickListener(this);
    }

    @Override
    protected void initViews() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_product_details);


        setSupportActionBar(binding.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        productViewModel = new ViewModelProvider(this, new ProductViewModelFactory((ShopApplication) getApplication())).get(ProductViewModel.class);

        addTextChangerListeners();

        if (getIntent() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                productActionType = getIntent().getParcelableExtra(PRODUCT_ACTION_TYPE_EXTRA, ActionType.class);
            } else {
                productActionType = getIntent().getParcelableExtra(PRODUCT_ACTION_TYPE_EXTRA);
            }

            categoryId = getIntent().getLongExtra(CATEGORY_ID_EXTRA, -1);

            if (productActionType.equals(ActionType.ADD)) {
                if (actionBar != null) {
                    actionBar.setTitle(R.string.add_product);
                }
                binding.btn.setText(R.string.add_product);
            } else {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    product = getIntent().getParcelableExtra(PRODUCT_EXTRA, ProductEntity.class);
                } else {
                    product = getIntent().getParcelableExtra(PRODUCT_EXTRA);
                }

                selectedImageUri = product.getImageUri();
                binding.productImg.setImageURI(selectedImageUri);


                if (actionBar != null) {
                    actionBar.setTitle(R.string.product_details);
                }
                binding.btn.setText(R.string.update_product);
            }


        }


        binding.setProduct(product);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.delete && productActionType != ActionType.ADD) {
            showConfirmationDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (productActionType == ActionType.UPDATE_DELETE) {
            getMenuInflater().inflate(R.menu.delete_option_menu, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to delete this product?");
        builder.setPositiveButton("Delete", (dialog, which) -> deleteProduct());
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void deleteProduct() {
        disposable.add(productViewModel.delete(binding.getProduct()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
            try {
                File image = FileUtil.getFileFromUri(AdminProductDetailsActivity.this, binding.getProduct().getImageUri());
                if (image != null && image.exists()) {
                    image.delete();
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            makeToast("Product Successfully Deleted.");
            finish();
        }, throwable -> logE(TAG, "Unable to get product", throwable)));
    }


    @Override
    protected void initMethods() {

    }

    @Override
    protected void subscribe() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == binding.btn.getId()) {
            addOrUpdateProduct();
        } else if (id == binding.productCard.getId()) {
            if (Util.isPermissionGranted(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ImagePickerUtils.pickImage(this, "Select Product Image");
            } else {
                requestReadExternalStoragePermission();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImagePickerUtils.onActivityResult(requestCode, resultCode, data, this);
    }

    private void addTextChangerListeners() {
        afterTextChanged(binding.productNameEt, binding.productNameInputLayout, null);
        afterTextChanged(binding.descEt, binding.descInputLayout, null);
        afterTextChanged(binding.priceEt, binding.priceInputLayout, null);
        afterTextChanged(binding.listPriceEt, binding.listPriceInputLayout, null);
        afterTextChanged(binding.retailPriceEt, binding.retailPriceInputLayout, null);
    }

    private void addOrUpdateProduct() {
        if (selectedImageUri == null) {
            makeToast("Please select a product image.");
            return;
        }
        AppCompatEditText productNameEt = binding.productNameEt, descEt = binding.descEt, priceEt = binding.priceEt, listPriceEt = binding.listPriceEt, retailPriceEt = binding.retailPriceEt;

        String productName, desc;
        double price, listPrice, retailPrice;

        if (productNameEt.getText() == null) {
            makeToast("Something went wrong");
            return;
        }

        productName = productNameEt.getText().toString().trim();

        if (TextUtils.isEmpty(productName)) {
            binding.productNameInputLayout.setError("Please enter product name");
            return;
        }

        if (descEt.getText() == null) {
            makeToast("Something went wrong");
            return;
        }

        desc = descEt.getText().toString().trim();

        if (TextUtils.isEmpty(desc)) {
            binding.descInputLayout.setError("Please enter product's description");
            return;
        }


        if (priceEt.getText() == null) {
            makeToast("Something went wrong");
            return;
        }

        if (TextUtils.isEmpty(priceEt.getText().toString().trim()) || priceEt.getText().toString().trim().equals(".")) {
            binding.priceInputLayout.setError("Please enter product price");
            return;
        }

        try {
            price = Double.parseDouble(priceEt.getText().toString());
        } catch (NullPointerException | NumberFormatException e) {
            price = 0D;
        }

        if (listPriceEt.getText() == null) {
            makeToast("Something went wrong");
            return;
        }

        if (TextUtils.isEmpty(listPriceEt.getText().toString().trim()) || listPriceEt.getText().toString().trim().equals(".")) {
            binding.listPriceInputLayout.setError("Please enter product list price");
            return;
        }


        try {
            listPrice = Double.parseDouble(listPriceEt.getText().toString());
        } catch (NullPointerException | NumberFormatException e) {
            listPrice = 0D;
        }


        if (retailPriceEt.getText() == null) {
            makeToast("Something went wrong");
            return;
        }

        if (TextUtils.isEmpty(retailPriceEt.getText().toString().trim()) || retailPriceEt.getText().toString().trim().equals(".")) {
            binding.retailPriceInputLayout.setError("Please enter product retail price");
            return;
        }


        try {
            retailPrice = Double.parseDouble(retailPriceEt.getText().toString());
        } catch (NullPointerException | NumberFormatException e) {
            retailPrice = 0D;
        }

        binding.btn.setEnabled(false);

        boolean isDataChanged, isImageUpdateAddNeeded = false;
        File image = null;

        if (productActionType.equals(ActionType.ADD)) {
            isImageUpdateAddNeeded = true;
        } else {
            if (!selectedImageUri.equals(product.getImageUri())) {
                isImageUpdateAddNeeded = true;
            }
        }

        if (isImageUpdateAddNeeded) {
            try {
                image = new File(FileUtil.createProductImagesDirectory(this), System.currentTimeMillis() + ".jpeg");
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

        if (productActionType.equals(ActionType.UPDATE_DELETE)) {
            isDataChanged = isDataChanged(selectedImageUri, productName, desc, price, listPrice, retailPrice);
        } else {
            isDataChanged = true;
            product = new ProductEntity();
            product.setDateCreate(new Date());
            product.setCategoryId(categoryId);
        }


        if (isDataChanged) {

            product.setName(productName);
            product.setDescription(desc);
            product.setPrice(price);
            product.setListPrice(listPrice);
            product.setRetailPrice(retailPrice);

            if (isImageUpdateAddNeeded) {
                product.setImageUri(Uri.fromFile(image));
            }

            product.setDateUpdate(new Date());

            disposable.add(productViewModel.upsert(product).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                binding.btn.setEnabled(true);
                if (productActionType.equals(ActionType.ADD)) {
                    makeToast("Successfully product added");
                } else {
                    makeToast("Product details successfully updated");
                }
                finish();
            }, throwable -> logE(TAG, "Unable to upsert product", throwable)));


        } else {
            finish();
        }
    }

    private boolean isDataChanged(Uri selectedImageUri, String productName, String desc, Double price, Double listPrice, Double retailPrice) {
        if (!productName.equals(product.getName())) {
            return true;
        }

        if (!desc.equals(product.getDescription())) {
            return true;
        }

        if (!price.equals(product.getPrice())) {
            return true;
        }

        if (!listPrice.equals(product.getListPrice())) {
            return true;
        }

        if (!retailPrice.equals(product.getRetailPrice())) {
            return true;
        }
        return !selectedImageUri.equals(product.getImageUri());
    }


    @Override
    public void onImageSelected(@Nullable Uri selectedImageUri) {
        this.selectedImageUri = selectedImageUri;
        binding.productImg.setImageURI(selectedImageUri);
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