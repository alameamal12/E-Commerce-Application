package projects.android.myshop.ui.login;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import projects.android.myshop.R;
import projects.android.myshop.data.ShopApplication;
import projects.android.myshop.data.result.LoginResult;
import projects.android.myshop.databinding.FragmentLoginBinding;
import projects.android.myshop.ui.AdminContainerActivity;
import projects.android.myshop.ui.UserContainerActivity;
import projects.android.myshop.ui.base.BaseFragment;
import projects.android.myshop.ui.user.UserViewModel;
import projects.android.myshop.ui.user.UserViewModelFactory;
import projects.android.myshop.utils.DataUtils;


public class LoginFragment extends BaseFragment {

    private static final String TAG = "LoginFragment";

    private UserType userType = UserType.USER;
    private FragmentLoginBinding binding;
    private String email;

    private LoginViewModel loginViewModel;
    private UserViewModel userViewModel;

    public LoginFragment() {
        super(R.layout.fragment_login);
    }

    @NonNull
    public static Fragment newInstance() {
        return new LoginFragment();
    }

    @Override
    protected void initClicks() {
        binding.loginBtn.setOnClickListener(this);
    }

    @Override
    protected void initViews(@NonNull View view) {
        binding = FragmentLoginBinding.bind(view);

        loginViewModel = new ViewModelProvider(getViewModelStore(), new LoginViewModelFactory((ShopApplication) requireActivity().getApplication())).get(LoginViewModel.class);
        userViewModel = new ViewModelProvider(getViewModelStore(), new UserViewModelFactory((ShopApplication) requireActivity().getApplication())).get(UserViewModel.class);

        binding.userTypeTv.setText("Admin");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.user_type_list));
        binding.userTypeTv.setAdapter(adapter);


        addTextChangerListeners();
    }

    @Override
    protected void initMethods() {
        observeLoginResult();
    }

    @Override
    protected void subscribe() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == binding.loginBtn.getId()) {
            logIn();
        }
    }

    private void logIn() {
        AppCompatEditText emailEt = binding.emailEt, passwordEt = binding.passwordEt;

        String userTypeText = binding.userTypeTv.getText().toString();

        if (userTypeText.equals("Admin")) {
            userType = UserType.ADMIN;
        } else if (userTypeText.equals("User")) {
            userType = UserType.USER;
        } else {
            makeToast("Please select user type");
            return;
        }


        if (emailEt.getText() == null) {
            makeToast("Something went wrong");
            return;
        }

        email = emailEt.getText().toString().trim();
        emailEt.setText(email);

        if (TextUtils.isEmpty(email)) {
            binding.emailInputLayout.setError("Please enter you email");
            return;
        }

        if (!isValidEmail(email) && userType != UserType.ADMIN) {
            binding.emailInputLayout.setError("Please enter valid email");
            return;
        }


        if (passwordEt.getText() == null) {
            makeToast("Something went wrong");
            return;
        }

        String password = passwordEt.getText().toString();


        if (TextUtils.isEmpty(password)) {
            binding.passwordInputLayout.setError("Please enter you password");
            return;
        }

        emailEt.setEnabled(false);
        passwordEt.setEnabled(false);
        binding.loginBtn.setEnabled(false);

        if (userType.equals(UserType.ADMIN)) {
            loginViewModel.adminLogin(email, password);
        } else {
            loginViewModel.userLogin(email, password);
        }


    }

    private void addTextChangerListeners() {
        afterTextChanged(binding.emailEt, binding.emailInputLayout, null);
        afterTextChanged(binding.passwordEt, binding.passwordInputLayout, null);
    }

    private void observeLoginResult() {
        loginViewModel.loginResult.observe(getViewLifecycleOwner(), loginResult -> {
            if (loginResult == null) {
                return;
            }
            if (loginResult == LoginResult.SUCCESS) {
                makeToast("Successfully logged in");
                onSuccessfullyLoggedIn();
            } else if (loginResult == LoginResult.WRONG_PASSWORD) {
                binding.passwordInputLayout.setError("Incorrect password. Please try again.");
                binding.emailEt.setEnabled(true);
                binding.passwordEt.setEnabled(true);
                binding.loginBtn.setEnabled(true);
            } else if (loginResult == LoginResult.NOT_EXIST) {
                makeToast("Invalid email or password");
                binding.emailEt.setEnabled(true);
                binding.passwordEt.setEnabled(true);
                binding.loginBtn.setEnabled(true);
            }
        });
    }

    private void onSuccessfullyLoggedIn() {
        DataUtils.setEmail(email);
        DataUtils.setIsLoggedIn(true);
        DataUtils.setIsAdmin(userType == UserType.ADMIN);

        if (DataUtils.isAdmin()) {
            Intent activityIntent = new Intent(requireContext(), AdminContainerActivity.class);
            startActivity(activityIntent);
            requireActivity().finish();
            return;
        }

        disposable.add(userViewModel.getUser(email).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(user -> {
            DataUtils.setUserId(user.getId());
            Intent activityIntent = new Intent(requireContext(), UserContainerActivity.class);
            startActivity(activityIntent);
            requireActivity().finish();
        }, throwable -> logE(TAG, "Unable to get user", throwable)));

    }

    public enum UserType {
        USER, ADMIN
    }

}