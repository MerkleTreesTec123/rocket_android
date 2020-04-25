package com.muye.rocket.base;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.hjq.toast.ToastUtils;
import com.ifenduo.common.view.LoadingDialog;
import com.ifenduo.lib_base.R;
import com.ifenduo.lib_base.tools.AppManager;
import com.ifenduo.lib_base.tools.LanguageUtil;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.muye.rocket.Constant;
import com.muye.rocket.mvp.account.view.LoginActivity;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity {
    public ImmersionBar mImmersionBar;
    protected Toolbar mToolbar;
    protected ImageView mNavigationCenterIcon;
    protected ImageView mNavigationLeftIcon;
    protected ImageView mNavigationRightIcon;
    protected TextView mNavigationCenterText;
    protected TextView mNavigationRightButton;
    protected TextView mNavigationRightButton_;
    protected LoadingDialog mProgressDialog;
    protected LinearLayout mToolBarContainer;

    public static void openActivity(Activity originActivity, Class<? extends BaseActivity> destinationClass, Bundle options) {
        Intent intent = new Intent(originActivity, destinationClass);
        if (options != null) intent.putExtras(options);
        originActivity.startActivity(intent);
    }

    public static void openActivity(Activity originActivity, Class<? extends BaseActivity> destinationClass, int requestCode, Bundle options) {
        Intent intent = new Intent(originActivity, destinationClass);
        if (options != null) intent.putExtras(options);
        originActivity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        onCreateViewBefore();
        AppManager.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(getContentViewLayoutId());

        //状态栏
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarDarkFont(true, 0.2f).init();

        setupToolbar();
        decorationWithToolBar(mToolbar);
        onReceiveRequestIntent(getIntent());
        onCreateViewAfter(savedInstanceState);

    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void cancelNotification() {
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.cancelAll();
    }

    protected abstract int getContentViewLayoutId();

    protected void onReceiveRequestIntent(Intent intent) {
    }

    protected void onCreateViewBefore() {
    }

    protected void onCreateViewAfter(Bundle savedInstanceState) {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        handleIntent(bundle);
    }

    protected void decorationWithToolBar(Toolbar toolbar) {
    }

    protected void handleIntent(Bundle bundle) {

    }

    protected boolean showNavigationIcon() {
        return true;
    }

    /**
     * 设置状态栏颜色
     *
     * @return
     */
    protected @ColorInt
    int getStatusBarColor() {
        return Color.TRANSPARENT;
    }

    protected @ColorInt
    int getToolbarColor() {
        return Color.TRANSPARENT;
    }

    protected Toolbar initToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        mNavigationLeftIcon = findViewById(R.id.img_toolbar_left_icon);
        mNavigationCenterIcon = findViewById(R.id.img_toolbar_center_icon);
        mNavigationRightIcon = findViewById(R.id.img_toolbar_right_icon);
        mNavigationCenterText = findViewById(R.id.tv_toolbar_center_text);
        mNavigationRightButton = findViewById(R.id.btn_toolbar_right);
        mNavigationRightButton_ = findViewById(R.id.btn_toolbar_right_);
        mToolBarContainer = findViewById(R.id.ll_toolbar_container);
        if (mToolbar != null){
            mToolbar.setBackgroundColor(getToolbarColor());
        }
        return mToolbar;
    }


    protected int getNavigationIcon() {
        return R.drawable.rc_back_icon_black;
    }

    private void setupToolbar() {
        Toolbar toolBar = initToolbar();
        if (toolBar != null){
            setSupportActionBar(toolBar);
            if (getSupportActionBar() != null){
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
            if (showNavigationIcon()) {
                if (getNavigationIcon() == 0) {
                    toolBar.setNavigationIcon(null);
                } else {
                    toolBar.setNavigationIcon(getNavigationIcon());
                }
                toolBar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickBack();
                    }
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
        onClickBack();
    }

    protected void onClickBack() {
        finish();
    }

    protected int obtainThemeColorPrimary() {
        TypedValue value = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, value, true);
        return value.data;
    }

    public void onViewClick(View v) {
    }


    /*****************
     * windowState
     *****************************************************************/
    public void hideKeyboardForCurrentFocus() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    /**
     * 隐藏软键盘
     */
    public static void hideKeyboard(Context context) {
        Activity activity = (Activity) context;
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive() && activity.getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                        .getWindowToken(), 0);
            }
        }
    }

    public void showKeyboardAtView(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public void forceShowKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    protected void setNavigationCenter(CharSequence s) {
        mNavigationCenterText.setVisibility(View.VISIBLE);
        mNavigationCenterText.setText(s);
    }

    protected void setNavigationCenter(CharSequence s, @ColorInt int color) {
        mNavigationCenterText.setVisibility(View.VISIBLE);
        mNavigationCenterText.setTextColor(color);
        mNavigationCenterText.setText(s);
    }

    protected void setNavigationRight(int resId, View.OnClickListener clickListener) {
        if (resId != 0) {
            mNavigationRightIcon.setImageResource(resId);
            mNavigationRightIcon.setVisibility(View.VISIBLE);
            mNavigationRightIcon.setOnClickListener(clickListener);
        } else {
            mNavigationRightIcon.setVisibility(View.GONE);
        }
    }

    protected void setNavigationRight(CharSequence s, @ColorInt int color, View.OnClickListener clickListener) {
        if (!TextUtils.isEmpty(s)) {
            mNavigationRightButton.setTextColor(color);
            mNavigationRightButton.setVisibility(View.VISIBLE);
            mNavigationRightButton.setText(s);
            mNavigationRightButton.setOnClickListener(clickListener);
        } else {
            mNavigationRightButton.setVisibility(View.GONE);
        }
    }

    /*****************
     * alert or notification
     *******************************************************/
    public void showProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = new LoadingDialog(this);
        }
        if (mProgressDialog.isShowing() || isFinishing()) {
            return;
        }
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show("");
    }


    public void showProgress(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new LoadingDialog(this);
        }
        if (mProgressDialog.isShowing() || isFinishing()) {
            return;
        }
        mProgressDialog = new LoadingDialog(this);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show(message);
    }

    public void dismissProgress() {
        if (null != mProgressDialog && mProgressDialog.isShowing() && BaseActivity.this != null && !BaseActivity.this.isFinishing()) {
            mProgressDialog.dismiss();
        }
    }

    public void showToast(CharSequence text) {
        ToastUtils.show(text);
    }

    /***************** alert or notification*******************************************************/


    /****************
     * networkAvailable
     ************************************************************/
    public boolean networkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public boolean networkAvailableWithWIFI() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_WIFI);
    }

    public boolean networkAvailableWithMobile() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /****************
     * networkAvailable
     ************************************************************/


    public Bundle buildBundleOptions(BundleParams bundleParams) {
        return null;
    }


    public static class BundleParams {
        public String key;
        public String vale;

        public BundleParams(String key, String vale) {
            this.key = key;
            this.vale = vale;
        }

    }

    protected void loginOut() {
        MMKVTools.clearAll(getApplicationContext());
        Constant.PAY_PASSWORD = "";
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        AppManager.getInstance().finishOtherActivity(LoginActivity.class);
    }

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        String language = MMKVTools.getLanguage();
//        super.attachBaseContext(LanguageUtil.attachBaseContext(newBase, language));
//    }

    protected void openLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isReLogin", true);
        intent.putExtras(bundle);
        startActivity(intent);
        AppManager.getInstance().finishOtherActivity(LoginActivity.class);
    }


    public void showLoadDialog(String message) {
        showProgress(message);
    }

    public void hideLoadDialog() {
        dismissProgress();
    }

    public void onError(int code, String message) {
        if (code == 101 || code == 401) {
            showToast("登录失效");
            openLogin();
        } else {
            showToast(message);
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        String language = MMKVTools.getLanguage();
        super.attachBaseContext(LanguageUtil.attachBaseContext(newBase, language));
    }
    @Override
    public void finish() {
        AppManager.getInstance().removeActivity(this);
        super.finish();
    }
}
