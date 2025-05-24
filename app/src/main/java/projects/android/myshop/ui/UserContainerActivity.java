package projects.android.myshop.ui;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import projects.android.myshop.utils.DataUtils;
import projects.android.myshop.R;
import projects.android.myshop.databinding.ActivityUserContainerBinding;
import projects.android.myshop.ui.MainActivity;
import projects.android.myshop.ui.base.BaseActivity;

public class UserContainerActivity extends BaseActivity {
    private static final String TAG = "UserContainerActivity";
    private ActivityUserContainerBinding binding;

    @Override
    protected void initClicks() {

    }

    @Override
    protected void initViews() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_container);

        setSupportActionBar(binding.toolbar);

        setBottomNav();
    }

    @Override
    protected void initMethods() {

    }

    @Override
    protected void subscribe() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.log_out) {
            signOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void signOut() {
        DataUtils.setEmail(null);
        DataUtils.setIsLoggedIn(false);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void setBottomNav() {
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.user_navigation_home, R.id.user_navigation_categories, R.id.user_navigation_cart, R.id.user_navigation_account).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_user);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}