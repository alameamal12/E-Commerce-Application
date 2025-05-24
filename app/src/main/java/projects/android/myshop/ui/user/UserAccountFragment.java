package projects.android.myshop.ui.user;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import projects.android.myshop.R;
import projects.android.myshop.data.ShopApplication;
import projects.android.myshop.databinding.FragmentUserAccountBinding;
import projects.android.myshop.ui.base.BaseFragment;
import projects.android.myshop.ui.order.UserOrdersActivity;
import projects.android.myshop.utils.DataUtils;


public class UserAccountFragment extends BaseFragment {
    private static final String TAG = "UserAccountFragment";
    private FragmentUserAccountBinding binding;
    private UserViewModel userViewModel;

    public UserAccountFragment() {
        // Required empty public constructor
        super(R.layout.fragment_user_account);
    }

    @Override
    protected void initClicks() {
        binding.ordersCard.setOnClickListener(this);
    }

    @Override
    protected void initViews(@NonNull View view) {
        binding = FragmentUserAccountBinding.bind(view);
        userViewModel = new ViewModelProvider(this, new UserViewModelFactory((ShopApplication) requireActivity().getApplication())).get(UserViewModel.class);
    }

    @Override
    protected void initMethods() {

    }

    @Override
    protected void subscribe() {
        disposable.add(userViewModel.getUser(DataUtils.getEmail()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(user -> {
            binding.nameTv.setText(user.getFullName());
            binding.emailTv.setText(user.getEmail());
        }, throwable -> logE(TAG, "Unable to get user", throwable)));

    }


    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == binding.ordersCard.getId()) {
            Intent activityIntent = new Intent(requireContext(), UserOrdersActivity.class);
            startActivity(activityIntent);
        }
    }
}