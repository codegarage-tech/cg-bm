package com.rc.buyermarket.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import com.rc.buyermarket.R;
import com.rc.buyermarket.util.RuntimePermissionManager;

import java.util.ArrayList;
import java.util.List;

import static com.rc.buyermarket.util.RuntimePermissionManager.REQUEST_CODE_PERMISSION;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public abstract class BaseActivity extends AppCompatActivity {

    private BaseActivity mActivity;
    public Bundle mSavedInstanceState;
    public String TAG = BaseActivity.class.getSimpleName();
    public ProgressDialog mProgressDialog;

    //Abstract declaration
//    public abstract Context initActivityContext();

    public abstract String initActivityTag();

    public abstract int initActivityLayout();

    public abstract void initStatusBarView();

    public abstract void initNavigationBarView();

    public abstract void initIntentData(Bundle savedInstanceState, Intent intent);

    public abstract void initActivityViews();

    public abstract void initActivityViewsData(Bundle savedInstanceState);

    public abstract void initActivityActions(Bundle savedInstanceState);

    public abstract void initActivityOnResult(int requestCode, int resultCode, Intent data);

    public abstract void initActivityBackPress();

    public abstract void initActivityDestroyTasks();

    public abstract void initActivityPermissionResult(int requestCode, String permissions[], int[] grantResults);

    public BaseActivity() {
        this.mActivity = this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initActivityLayout());

        TAG = initActivityTag();
        mSavedInstanceState = savedInstanceState;
        initStatusBarView();
        initNavigationBarView();
        initIntentData(mSavedInstanceState, getIntent());
        initActivityViews();

        if (checkAndRequestPermissions()) {
            initActivityViewsData(mSavedInstanceState);
            initActivityActions(mSavedInstanceState);
        }
    }

    public Activity getActivity() {
        return mActivity;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mSavedInstanceState = savedInstanceState;
    }

    /**********************
     * Permission methods *
     **********************/
    private boolean checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!RuntimePermissionManager.isAllPermissionsGranted(mActivity)) {
                ArrayList<String> permissionNeeded = RuntimePermissionManager.getAllUnGrantedPermissions(mActivity);
                for (int i = 0; i < permissionNeeded.size(); i++) {
                    Log.d(TAG, "Ungranted Permission: " + permissionNeeded.get(i));
                }
                ActivityCompat.requestPermissions(this, permissionNeeded.toArray(new String[permissionNeeded.size()]), REQUEST_CODE_PERMISSION);
                return false;
            }
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        initActivityPermissionResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE_PERMISSION:
                if (RuntimePermissionManager.isAllPermissionsGranted(mActivity, permissions)) {
                    initActivityViewsData(mSavedInstanceState);
                    initActivityActions(mSavedInstanceState);
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        initActivityOnResult(requestCode, resultCode, data);

        //send on activity result event to the fragment
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null) {
            for (Fragment fragment : fragmentList) {
                if (fragment instanceof BaseFragment) {
                    ((BaseFragment) fragment).onFragmentResult(requestCode, resultCode, data);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        initActivityBackPress();

        //send back press event to the fragment
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null) {
            for (Fragment fragment : fragmentList) {
                if (fragment instanceof BaseFragment) {
                    ((BaseFragment) fragment).onFragmentBackPressed();
                }
            }
        }
    }

    public void onDestroy(){
        initActivityDestroyTasks();

        super.onDestroy();
    }

    /***************************
     * Progress dialog methods *
     ***************************/
    public ProgressDialog showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(getResources().getString(R.string.view_loading));
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                        mProgressDialog = null;
                    }
                }
            });
        }

        if (mProgressDialog != null && !mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }

        return mProgressDialog;
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}
