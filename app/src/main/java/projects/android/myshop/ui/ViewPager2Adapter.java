package projects.android.myshop.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import projects.android.myshop.ui.login.LoginFragment;

// adapter for ViewPager
public class ViewPager2Adapter extends FragmentStateAdapter {

    private static final int TABS_NUM = 2;

    public ViewPager2Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return LoginFragment.newInstance();
        } else {
            return SignupFragment.newInstance();
        }
    }

    @Override
    public int getItemCount() {
        return TABS_NUM;
    }
}
