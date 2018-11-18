package com.rc.buyermarket.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.rc.buyermarket.R;
import com.rc.buyermarket.base.BaseActivity;
import com.rc.buyermarket.fragment.HomeFragment;
import com.rc.buyermarket.fragment.ProfileFragment;
import com.rc.buyermarket.fragment.PropertyListFragment;
import com.rc.buyermarket.model.AddProperty;
import com.rc.buyermarket.util.AllConstants;
import com.rc.buyermarket.util.AppPref;
import com.rc.buyermarket.view.CanaroTextView;

import cn.ymex.popup.controller.AlertController;
import cn.ymex.popup.dialog.PopupDialog;

import static com.rc.buyermarket.util.AllConstants.INTENT_KEY_PROPERTY;
import static com.rc.buyermarket.util.AllConstants.INTENT_REQUEST_CODE_ADD_PROPERTY;
import static com.rc.buyermarket.util.AllConstants.INTENT_REQUEST_CODE_LOGIN;

public class MainActivity extends BaseActivity {
    // initialize Toolbar
    CanaroTextView tvTitle;
    Toolbar toolbar;

    // initialize Drawer
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    private Fragment fragment = null;
    private FrameLayout fm;
    private int currentMenuId = 0;
    boolean doubleBackToExitPressedOnce = false;

    LinearLayout linLogOut;

    @Override
    public String initActivityTag() {
        return MainActivity.class.getSimpleName();
    }

    @Override
    public int initActivityLayout() {
        return R.layout.activity_drawer;
    }

    @Override
    public void initStatusBarView() {

    }

    @Override
    public void initNavigationBarView() {
    }

    @Override
    public void initIntentData(Bundle savedInstanceState, Intent intent) {

    }

    @Override
    public void initActivityViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (CanaroTextView) findViewById(R.id.tv_title);
        drawerLayout = findViewById(R.id.drawerLayoutId);
        navigationView = findViewById(R.id.navigation_menu);
        fm = (FrameLayout) findViewById(R.id.fragment_container);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        linLogOut = (LinearLayout) findViewById(R.id.lin_log_out);
        bindMenu();
    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        tvTitle.setText(getResources().getString(R.string.title_buyer_market));

    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {

        if (navigationView != null) {

            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem menuItem) {

                            navigationMenuChanged(menuItem);
                            return true;
                        }
                    });
        }
        linLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //doLogout();
                logOutDialog();
            }
        });

    }

    @Override
    public void initActivityOnResult(int requestCode, int resultCode, Intent data) {
        Log.d(ActivityUserLogin.class.getSimpleName(), "(ActivityUserLogin)got result " + requestCode);
        switch (requestCode) {
            case INTENT_REQUEST_CODE_LOGIN:
                Log.d(ActivityUserLogin.class.getSimpleName(), "(ActivityUserLogin)got result(INTENT_REQUEST_CODE_LOGIN)");
                if (resultCode == RESULT_OK) {
                    Log.d(ActivityUserLogin.class.getSimpleName(), "(ActivityUserLogin)got result(INTENT_REQUEST_CODE_LOGIN)(RESULT_OK)");
                    bindMenu();
                }
                break;
            case INTENT_REQUEST_CODE_ADD_PROPERTY:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        AddProperty addProperty = data.getParcelableExtra(INTENT_KEY_PROPERTY);
                        if (addProperty != null) {
                            //after adding property send user to the property list screen
                            openFragment(R.id.nav_property_buyer);
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void initActivityBackPress() {
        finish();
    }

    @Override
    public void initActivityDestroyTasks() {

    }

    @Override
    public void initActivityPermissionResult(int requestCode, String[] permissions, int[] grantResults) {

    }

    // Neet to check
    public void refreshProfileData() {

        if (fragment instanceof ProfileFragment) {
            Log.e("Refresh Data.", "Refresh" + "");

            // ((ProfileFragment) fragment).bindData();
        }
    }


    // This function will change the menu based on the user is logged in or not.
    public void bindMenu() {
        if (AppPref.getBooleanSetting(MainActivity.this, AllConstants.SESSION_IS_LOGGED_IN, false)) {
            if (AppPref.getPreferences(getActivity(), AllConstants.SESSION_USER_TYPE).equalsIgnoreCase("0")) {
                navigationView.getMenu().setGroupVisible(R.id.group_after_login, true);
                navigationView.getMenu().setGroupVisible(R.id.group_before_login, false);
                navigationView.getMenu().setGroupVisible(R.id.group_after_seller_login, false);

                navigationMenuChanged(navigationView.getMenu().findItem(R.id.nav_home_seller));
            } else if (AppPref.getPreferences(getActivity(), AllConstants.SESSION_USER_TYPE).equalsIgnoreCase("1")) {
                navigationView.getMenu().setGroupVisible(R.id.group_after_login, false);
                navigationView.getMenu().setGroupVisible(R.id.group_before_login, false);
                navigationView.getMenu().setGroupVisible(R.id.group_after_seller_login, true);

                navigationMenuChanged(navigationView.getMenu().findItem(R.id.nav_home_buyer));
            }
            linLogOut.setVisibility(View.VISIBLE);
        } else {
            navigationView.getMenu().setGroupVisible(R.id.group_before_login, true);
            navigationView.getMenu().setGroupVisible(R.id.group_after_login, false);
            navigationView.getMenu().setGroupVisible(R.id.group_after_seller_login, false);
            linLogOut.setVisibility(View.GONE);

            navigationMenuChanged(navigationView.getMenu().findItem(R.id.nav_home));
        }
    }


    private void navigationMenuChanged(MenuItem menuItem) {
        openFragment(menuItem.getItemId());
        menuItem.setChecked(true);
        drawerLayout.closeDrawers();
    }


    public void openFragment(int menuId) {

        switch (menuId) {
            case R.id.nav_home_seller:
            case R.id.nav_home:
            case R.id.nav_home_buyer:
                fragment = new HomeFragment();
                tvTitle.setText(getResources().getString(R.string.title_buyer_market));
                break;

            case R.id.nav_property_buyer:
                fragment = new PropertyListFragment();
                tvTitle.setText(getResources().getString(R.string.menu_property));
                break;

            case R.id.nav_profile_seller:
            case R.id.nav_profile_buyer:
                fragment = new ProfileFragment();
                tvTitle.setText(getResources().getString(R.string.menu_profile));
                break;
        }

        if (currentMenuId != menuId) {
            currentMenuId = menuId;

            updateFragment(fragment);

            try {
                navigationView.getMenu().findItem(menuId).setChecked(true);
            } catch (Exception e) {
            }
        }
    }

    private void updateFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    public void doLogout() {
        AppPref.setBooleanSetting(getActivity(), AllConstants.SESSION_IS_LOGGED_IN, false);
        AppPref.savePreferences(getActivity(), AllConstants.SESSION_USER_TYPE, null);
        bindMenu();
    }

    /**
     * Log out
     */
    public void logOutDialog() {
        PopupDialog.create(this)
                .outsideTouchHide(false)
                .dismissTime(1000 * 10)
                .controller(AlertController.build()
                        .title("Logout\n")
                        .message("Thanks for staying with us")
                        .negativeButton(getString(R.string.dialog_cancel), null)
                        .positiveButton(getString(R.string.title_ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                doLogout();
                            }
                        }))
                .show();

    }
}
