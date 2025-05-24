package projects.android.myshop.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import projects.android.myshop.R;
import projects.android.myshop.data.ShopApplication;
import projects.android.myshop.data.result.SignupResult;
import projects.android.myshop.databinding.FragmentSignupBinding;
import projects.android.myshop.ui.base.BaseFragment;
import projects.android.myshop.ui.signup.SignupViewModel;
import projects.android.myshop.ui.signup.SignupViewModelFactory;


public class SignupFragment extends BaseFragment {

    public static final String ACTION_SUCCESSFULLY_SIGNUP = "projects.android.myshop.ACTION_SUCCESSFULLY_SIGNUP";
    private FragmentSignupBinding binding;
    private SignupViewModel signupViewModel;

    public SignupFragment() {
        super(R.layout.fragment_signup);
    }

    @NonNull
    public static Fragment newInstance() {
        return new SignupFragment();
    }

    @Override
    protected void initClicks() {
        binding.signupBtn.setOnClickListener(this);
    }

    @Override
    protected void initViews(@NonNull View view) {
        binding = FragmentSignupBinding.bind(view);

        signupViewModel = new ViewModelProvider(getViewModelStore(), new SignupViewModelFactory((ShopApplication) requireActivity().getApplication())).get(SignupViewModel.class);

        addTextChangerListeners();
    }

    @Override
    protected void initMethods() {
        observeSignupResult();
    }

    @Override
    protected void subscribe() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == binding.signupBtn.getId()) {
            signUp();
        }
    }

    private void signUp() {
        AppCompatEditText nameEt = binding.nameEt, emailEt = binding.emailEt, postCodeEt = binding.postCodeEt, addressEt = binding.addressEt, passwordEt = binding.passwordEt;
        String name, postcode, address, password;

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

        String email = emailEt.getText().toString().trim();
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


        nameEt.setEnabled(false);
        emailEt.setEnabled(false);
        postCodeEt.setEnabled(false);
        addressEt.setEnabled(false);
        passwordEt.setEnabled(false);
        binding.signupBtn.setEnabled(false);

        signupViewModel.signUp(name, email, postcode, address, password);

    }

    private void observeSignupResult() {
        signupViewModel.signupResult.observe(getViewLifecycleOwner(), signupResult -> {
            if (signupResult == null) {
                return;
            }
            if (signupResult == SignupResult.SUCCESS) {
                makeToast("Successfully signed up");
                onSuccessfullySignedUp();
            } else if (signupResult == SignupResult.ALREADY_EXIST) {
                binding.emailInputLayout.setError("Email already exists. Please use a different email or sign in with your existing account.");
                binding.nameEt.setEnabled(true);
                binding.emailEt.setEnabled(true);
                binding.postCodeEt.setEnabled(true);
                binding.addressEt.setEnabled(true);
                binding.passwordEt.setEnabled(true);
                binding.signupBtn.setEnabled(true);
            } else if (signupResult == SignupResult.FAILED) {
                makeToast("Something went wrong");
                binding.nameEt.setEnabled(true);
                binding.emailEt.setEnabled(true);
                binding.postCodeEt.setEnabled(true);
                binding.addressEt.setEnabled(true);
                binding.passwordEt.setEnabled(true);
                binding.signupBtn.setEnabled(true);
            }
        });
    }

    private void onSuccessfullySignedUp() {
        binding.nameEt.setEnabled(true);
        binding.emailEt.setEnabled(true);
        binding.postCodeEt.setEnabled(true);
        binding.addressEt.setEnabled(true);
        binding.passwordEt.setEnabled(true);
        binding.signupBtn.setEnabled(true);
        binding.nameEt.setText(null);
        binding.emailEt.setText(null);
        binding.postCodeEt.setText(null);
        binding.addressEt.setText(null);
        binding.passwordEt.setText(null);
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(new Intent(ACTION_SUCCESSFULLY_SIGNUP));
    }

    private void addTextChangerListeners() {
        afterTextChanged(binding.nameEt, binding.nameInputLayout, null);
        afterTextChanged(binding.emailEt, binding.emailInputLayout, null);
        afterTextChanged(binding.postCodeEt, binding.postCodeInputLayout, null);
        afterTextChanged(binding.addressEt, binding.addressInputLayout, null);
        afterTextChanged(binding.passwordEt, binding.passwordInputLayout, null);
    }

}