package projects.android.myshop.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;

import androidx.annotation.StringRes;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayoutMediator;

import projects.android.myshop.R;
import projects.android.myshop.databinding.ActivityMainBinding;
import projects.android.myshop.ui.base.BaseActivity;
import projects.android.myshop.utils.DataUtils;

public class MainActivity extends BaseActivity {

    private static final @StringRes int[] PAGE_TITLES = {R.string.login, R.string.signup};

    private ActivityMainBinding binding;

    private final BroadcastReceiver signupBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (intent.getAction() != null) {
                    if (intent.getAction().equals(SignupFragment.ACTION_SUCCESSFULLY_SIGNUP)) {
                        binding.viewPager.setCurrentItem(0, true);
                    }
                }
            }
        }
    };

    @Override
    protected void initClicks() {

    }

    @Override
    protected void initViews() {

        if (DataUtils.isLoggedIn()) {
            Intent activityIntent;
            if (DataUtils.isAdmin()) {
                activityIntent = new Intent(this, AdminContainerActivity.class);
            } else {
                activityIntent = new Intent(this, UserContainerActivity.class);
            }
            startActivity(activityIntent);
            finish();
        } else {
            binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
            setViewPager();
        }
    }

    @Override
    protected void initMethods() {
    }

    @Override
    protected void onStart() {
        if (!DataUtils.isLoggedIn()) {
            IntentFilter filter = new IntentFilter(SignupFragment.ACTION_SUCCESSFULLY_SIGNUP);
            LocalBroadcastManager.getInstance(this).registerReceiver(signupBroadcastReceiver, filter);
        }
        super.onStart();
    }

    @Override
    protected void onPause() {
        if (!DataUtils.isLoggedIn()) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(signupBroadcastReceiver);
        }
        super.onPause();
    }

    @Override
    protected void subscribe() {

    }


    @Override
    public void onClick(View v) {

    }

    private void setViewPager() {
        ViewPager2 viewPager2 = binding.viewPager;
        viewPager2.setAdapter(new ViewPager2Adapter(getSupportFragmentManager(), getLifecycle()));
        new TabLayoutMediator(binding.tabsLayout, viewPager2, (tab, position) -> tab.setText(PAGE_TITLES[position])).attach();
        viewPager2.setSaveEnabled(true);
    }


}