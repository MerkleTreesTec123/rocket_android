package com.muye.rocket.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.hjq.toast.ToastUtils;
import com.ifenduo.lib_base.tools.AppManager;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.muye.rocket.Constant;
import com.muye.rocket.R;
import com.muye.rocket.mvp.account.view.LoginActivity;


/**
 * Created by xuefengyang on 2016/5/14.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    public ImmersionBar mImmersionBar;
    public View fragmentRootView;
    public Context mContext;
    /*
     * 头部
     * */
    private Toolbar mToolBar;
    private ImageView mTbBack;
    private TextView mTbTitle;
    private ImageView mTbSub;
    private TextView mTbNext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    public Activity getCurrentActivity() {
        return (Activity) mContext;
    }

    public void showToast(String text) {
        ToastUtils.show(text);
    }

    public void showErrorToast(String text) {

    }

    public void showNetErrorToast() {

    }

    public void showProgress() {
        if (getCurrentActivity() instanceof BaseActivity) {
            ((BaseActivity) getCurrentActivity()).showProgress();
        }
    }

    public void showProgress(String message) {
        if (getCurrentActivity() instanceof BaseActivity) {
            ((BaseActivity) getCurrentActivity()).showProgress(message);
        }
    }

    public void dismissProgress() {
        if (getCurrentActivity() instanceof BaseActivity) {
            ((BaseActivity) getCurrentActivity()).dismissProgress();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentRootView = inflater.inflate(getRootViewLayout(), container, false);

        //状态栏
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarDarkFont(true, 0.2f).init();

        //公共头
        mToolBar = (Toolbar) fragmentRootView.findViewById(R.id.toolbar);
        mTbBack = (ImageView) fragmentRootView.findViewById(R.id.tb_back);
        mTbTitle = (TextView) fragmentRootView.findViewById(R.id.tb_title);
        mTbSub = (ImageView) fragmentRootView.findViewById(R.id.tb_sub);
        mTbNext = (TextView) fragmentRootView.findViewById(R.id.tb_next);

        initView(fragmentRootView);
        initData();
        return fragmentRootView;
    }

    /*
     * 获取头部控件
     * */
    public Toolbar getToolBar() {
        return mToolBar;
    }

    public ImageView getTbBack() {
        return mTbBack;
    }

    public TextView getTbTitle() {
        return mTbTitle;
    }

    public ImageView getTbSub() {
        return mTbSub;
    }

    public TextView getTbNext() {
        return mTbNext;
    }

    protected abstract int getRootViewLayout();

    protected abstract void initView(View parentView);

    protected abstract void initData();

    protected void onViewClick(View v) {
    }

    @Override
    public void onClick(View v) {
        onViewClick(v);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

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

    /**
     * 当token过期 或者loginInfo==null时 跳转登录
     */
    protected void openLogin() {
//        MMKVTools.clearAll(getContext().getApplicationContext());

        Intent intent = new Intent(getContext(), LoginActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isReLogin", true);
        intent.putExtras(bundle);
        startActivity(intent);
        AppManager.getInstance().finishOtherActivity(LoginActivity.class);
    }

    protected void loginOut() {
        MMKVTools.clearAll(getContext().getApplicationContext());
        Constant.PAY_PASSWORD = "";
        Intent intent = new Intent(getContext(), LoginActivity.class);
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
            if (message.equals("请求超时")){
                showToast("服务器维护中");
            }else
            showToast(message);
        }
    }
}
