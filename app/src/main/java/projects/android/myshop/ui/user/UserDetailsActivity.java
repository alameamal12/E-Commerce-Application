package projects.android.myshop.ui.user;

import android.os.Build;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import java.util.Date;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import projects.android.myshop.R;
import projects.android.myshop.data.ShopApplication;
import projects.android.myshop.databinding.ActivityUserDetailsBinding;
import projects.android.myshop.db.entity.UserEntity;
import projects.android.myshop.ui.base.BaseActivity;
import projects.android.myshop.utils.ActionType;

public class UserDetailsActivity extends BaseActivity {

    public static final String USER_ACTION_TYPE_EXTRA = "user_action_type_extra";
    public static final String USER_ID_EXTRA = "user_id_extra";
    private static final String TAG = "UserDetailsActivity";
    private ActivityUserDetailsBinding binding;
    private UserViewModel userViewModel;
    private UserEntity user;
    private ActionType userActionType;

    @Override
    protected void initClicks() {
        binding.btn.setOnClickListener(this);
    }

    @Override
    protected void initViews() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_details);
        setSupportActionBar(binding.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        userViewModel = new ViewModelProvider(this, new UserViewModelFactory((ShopApplication) getApplication())).get(UserViewModel.class);

        addTextChangerListeners();

        if (getIntent() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                userActionType = getIntent().getParcelableExtra(USER_ACTION_TYPE_EXTRA, ActionType.class);
            } else {
                userActionType = getIntent().getParcelableExtra(USER_ACTION_TYPE_EXTRA);
            }

            if (userActionType.equals(ActionType.ADD)) {
                binding.dateRegisterTv.setVisibility(View.GONE);
                if (actionBar != null) {
                    actionBar.setTitle(R.string.add_user);
                }
                binding.btn.setText(R.string.add_user);
            } else {
                binding.dateRegisterTv.setVisibility(View.VISIBLE);
                if (actionBar != null) {
                    actionBar.setTitle(R.string.user_details);
                }
                binding.btn.setText(R.string.update_user);
            }

        }


    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to delete this user?");
        builder.setPositiveButton("Delete", (dialog, which) -> deleteUser());
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.delete && userActionType != ActionType.ADD) {
            showConfirmationDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (userActionType == ActionType.UPDATE_DELETE) {
            getMenuInflater().inflate(R.menu.delete_option_menu, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    private void deleteUser() {
        disposable.add(userViewModel.delete(binding.getUser()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
            makeToast("User Successfully Deleted.");
            finish();
        }, throwable -> logE(TAG, "Unable to get user", throwable)));
    }

    @Override
    protected void initMethods() {
        if (userActionType.equals(ActionType.UPDATE_DELETE)) {
            Long userId = getIntent().getLongExtra(USER_ID_EXTRA, -1);
            disposable.add(userViewModel.getUser(userId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(user -> {
                this.user = user;
                binding.setUser(user);
                binding.dateRegisterTv.setText(String.format(Locale.getDefault(), "Date Register - %s", user.getDateRegister().toString()));
            }, throwable -> logE(TAG, "Unable to get user", throwable)));
        }
    }

    @Override
    protected void subscribe() {
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == binding.btn.getId()) {
            addOrUpdateUser();
        }
    }

    private void addOrUpdateUser() {

        if (userActionType.equals(ActionType.UPDATE_DELETE) && user == null) {
            makeToast("Something went wrong");
            return;
        }

        AppCompatEditText nameEt = binding.nameEt, emailEt = binding.emailEt, postCodeEt = binding.postCodeEt, addressEt = binding.addressEt, passwordEt = binding.passwordEt;
        String name, postcode, address, password, email;

        // name validation
        if (nameEt.getText() == null) {
            makeToast("Something went wrong");
            return;
        }

        name = nameEt.getText().toString().trim();
        nameEt.setText(name);

        if (TextUtils.isEmpty(name)) {
            binding.nameInputLayout.setError("Please enter your full name");
            return;
        }

        // email validation
        if (emailEt.getText() == null) {
            makeToast("Something went wrong");
            return;
        }

        email = emailEt.getText().toString().trim();
        emailEt.setText(email);

        if (TextUtils.isEmpty(email)) {
            binding.emailInputLayout.setError("Please enter your email");
            return;
        }

        if (!isValidEmail(email)) {
            binding.emailInputLayout.setError("Please enter valid email");
            return;
        }

        // postcode validation
        if (postCodeEt.getText() == null) {
            makeToast("Something went wrong");
            return;
        }

        postcode = postCodeEt.getText().toString().trim();
        postCodeEt.setText(postcode);

        if (TextUtils.isEmpty(postcode)) {
            binding.postCodeInputLayout.setError("Please enter your postcode");
            return;
        }


        // address validation
        if (addressEt.getText() == null) {
            makeToast("Something went wrong");
            return;
        }

        address = addressEt.getText().toString().trim();
        addressEt.setText(address);

        if (TextUtils.isEmpty(address)) {
            binding.addressInputLayout.setError("Please enter your address");
            return;
        }

        // address validation
        if (passwordEt.getText() == null) {
            makeToast("Something went wrong");
            return;
        }

        password = passwordEt.getText().toString();

        if (TextUtils.isEmpty(password)) {
            binding.passwordInputLayout.setError("Please enter your password");
            return;
        }

        boolean isDataChanged;

        if (userActionType.equals(ActionType.UPDATE_DELETE)) {
            isDataChanged = isDataChanged(name, email, postcode, address, password);
        } else {
            isDataChanged = true;
            user = new UserEntity();
            user.setDateRegister(new Date());
        }


        if (isDataChanged) {

            user.setFullName(name);
            user.setEmail(email);
            user.setPostcode(postcode);
            user.setAddress(address);
            user.setPassword(password);

            user.setDateUpdate(new Date());

            binding.btn.setEnabled(false);

            disposable.add(userViewModel.upsert(user).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                binding.btn.setEnabled(true);
                if (userActionType.equals(ActionType.ADD)) {
                    makeToast("Successfully user added");
                } else {
                    makeToast("User details successfully updated");
                }
                finish();
            }, throwable -> logE(TAG, "Unable to upsert user", throwable)));

        } else {
            finish();
        }


    }

    private boolean isDataChanged(String name, String email, String postcode, String address, String password) {
        if (!name.equals(user.getFullName())) {
            return true;
        }
        if (!email.equals(user.getEmail())) {
            return true;
        }
        if (!postcode.equals(user.getPostcode())) {
            return true;
        }
        if (!address.equals(user.getAddress())) {
            return true;
        }
        return !password.equals(user.getPassword());
    }

    private void addTextChangerListeners() {
        afterTextChanged(binding.nameEt, binding.nameInputLayout, null);
        afterTextChanged(binding.emailEt, binding.emailInputLayout, null);
        afterTextChanged(binding.postCodeEt, binding.postCodeInputLayout, null);
        afterTextChanged(binding.addressEt, binding.addressInputLayout, null);
        afterTextChanged(binding.passwordEt, binding.passwordInputLayout, null);
    }

}