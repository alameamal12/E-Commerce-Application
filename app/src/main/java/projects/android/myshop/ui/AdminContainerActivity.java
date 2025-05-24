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
import projects.android.myshop.databinding.ActivityAdminContainerBinding;
import projects.android.myshop.ui.MainActivity;
import projects.android.myshop.ui.base.BaseActivity;

public class AdminContainerActivity extends BaseActivity {
    private static final String TAG = "AdminContainerActivity";
    private ActivityAdminContainerBinding binding;

    @Override
    protected void initClicks() {

    }

    @Override
    protected void initViews() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_container);

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
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.admin_navigation_home, R.id.admin_navigation_categories, R.id.admin_navigation_orders, R.id.admin_navigation_users).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_admin);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}