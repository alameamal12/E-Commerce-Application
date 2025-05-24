package projects.android.myshop.ui.user;

import android.content.Intent;
import android.os.Parcelable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import projects.android.myshop.R;
import projects.android.myshop.data.ShopApplication;
import projects.android.myshop.databinding.FragmentAdminUsersBinding;
import projects.android.myshop.db.entity.UserEntity;
import projects.android.myshop.ui.base.BaseFragment;
import projects.android.myshop.utils.ActionType;


public class AdminUsersFragment extends BaseFragment implements UserAdapter.OnUserItemClickListener {
    private static final String TAG = "AdminUsersFragment";

    private FragmentAdminUsersBinding binding;
    private UserViewModel userViewModel;
    private UserAdapter userAdapter;


    public AdminUsersFragment() {
        // Required empty public constructor
        super(R.layout.fragment_admin_users);
    }

    @Override
    protected void initClicks() {
        binding.addUserBtn.setOnClickListener(this);
    }

    @Override
    protected void initViews(@NonNull View view) {
        binding = FragmentAdminUsersBinding.bind(view);
        userViewModel = new ViewModelProvider(this, new UserViewModelFactory((ShopApplication) requireActivity().getApplication())).get(UserViewModel.class);

        userAdapter = new UserAdapter(this);
        binding.setUserAdapter(userAdapter);
    }

    @Override
    protected void initMethods() {

    }

    @Override
    protected void subscribe() {
        disposable.add(userViewModel.getAllUsers().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(allUsers -> {
            if (allUsers.isEmpty()) {
                binding.recyclerview.setVisibility(View.GONE);
                binding.userEmptyTv.setVisibility(View.VISIBLE);
            } else {
                binding.userEmptyTv.setVisibility(View.GONE);
                binding.recyclerview.setVisibility(View.VISIBLE);
            }
            userAdapter.updateUsers((ArrayList<UserEntity>) allUsers);
        }, throwable -> logE(TAG, "Unable to get user list", throwable)));
    }


    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == binding.addUserBtn.getId()) {
            Intent activityIntent = new Intent(requireContext(), UserDetailsActivity.class);
            activityIntent.putExtra(UserDetailsActivity.USER_ACTION_TYPE_EXTRA, (Parcelable) ActionType.ADD);
            startActivity(activityIntent);
        }
    }

    @Override
    public void onUserClicked(UserEntity user) {
        Intent intent = new Intent(requireContext(), UserDetailsActivity.class);
        intent.putExtra(UserDetailsActivity.USER_ID_EXTRA, user.getId());
        intent.putExtra(UserDetailsActivity.USER_ACTION_TYPE_EXTRA, (Parcelable) ActionType.UPDATE_DELETE);
        startActivity(intent);
    }
}