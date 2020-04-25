package com.muye.rocket.mvp.me.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ifenduo.lib_base.tools.MMKVTools;
import com.muye.rocket.Constant;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.entity.SafeSettingEntity;
import com.muye.rocket.mvp.account.view.CreateLockActivity;
import com.muye.rocket.mvp.me.auth.RealNameAuthActivity;
import com.muye.rocket.mvp.me.contract.MeContract;
import com.muye.rocket.mvp.me.presenter.MePresenter;
import com.muye.rocket.tools.ACache;
import com.muye.rocket.widget.SafeLevelView;

import butterknife.BindView;
import butterknife.OnClick;

/*
* 账户与安全
* */
public class SafeCenterActivity extends BaseActivity implements MeContract.View {
    @BindView(R.id.safe_level_text_view)
    TextView safeLevelTextView;
    @BindView(R.id.safe_level_view)
    SafeLevelView safeLevelView;
    @BindView(R.id.phone_status_text_view)
    TextView phoneStatusTextView;
    @BindView(R.id.email_status_text_view)
    TextView emailStatusTextView;
    @BindView(R.id.google_status_text_view)
    TextView googleStatusTextView;
    @BindView(R.id.rel_name_status_text_view)
    TextView relNameStatusTextView;
    @BindView(R.id.tv_gesture_lock)
    TextView tvGestureLock;

    @BindView(R.id.pay_password_status_text_view)
    TextView payPasswordStatusTextView;
    @BindView(R.id.gesture_switch)
    ImageButton gestureSwitch;
    @BindView(R.id.fingerprint_switch)
    ImageView fingerprintSwitch;
    @BindView(R.id.uid_txt)
    TextView uidTxt;
    @BindView(R.id.net_error)
    ImageView netError;
    @BindView(R.id.net_ok)
    ImageView netOk;
    @BindView(R.id.gesture_container)
    RelativeLayout gestureContainer;

    MeContract.Presenter mPresenter;
    private ACache aCache;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_safe_center;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.account_and_security));
        uidTxt.setText(MMKVTools.getUID());
        aCache = ACache.get(this);
        String gesturePassword = aCache.getAsString(Constant.GESTURE_PASSWORD);
        tvGestureLock.setText(TextUtils.isEmpty(gesturePassword) ? R.string.not_set : R.string.reset);
        gestureSwitch.setSelected(TextUtils.isEmpty(gesturePassword) ? false : MMKVTools.isOpenGesture());

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mPresenter==null){
            mPresenter=new MePresenter(this);
        }
        mPresenter.fetchSafeSetting();
    }

    @OnClick({R.id.phone_container, R.id.google_container, R.id.rel_name_container, R.id.pay_password_container,
            R.id.login_password_container, R.id.gesture_switch, R.id.fingerprint_switch, R.id.email_container,R.id.rl_gesture_lock})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.phone_container://手机
                if (!MMKVTools.hasBindPhone()) {
                    BindPhoneActivity.openBindPEActivity(this,0);
                }
                break;
            case R.id.email_container://邮箱
                if (!MMKVTools.hasBindEmail()) {
                    BindPhoneActivity.openBindPEActivity(this,1);
                }
                break;
            case R.id.google_container://谷歌身份验证器
                if (!MMKVTools.hasBindGoogleCode()) {
                    intent = new Intent(this, BindGoogleStep1Activity.class);
                    startActivity(intent);
                }
                break;
            case R.id.rel_name_container://实名认证
                /*
                * 新版实名认证RealNameAuthActivity
                * */
                if ("0".equals(MMKVTools.getRelNameStatus()) || "2".equals(MMKVTools.getRelNameStatus()) || "10".equals(MMKVTools.getRelNameStatus())) {
                    intent = new Intent(this, RealNameResultActivity.class);
                } else {
                    intent = new Intent(this, RealNameAuthActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.pay_password_container://支付密码
                if (MMKVTools.hasSetPayPassword()) {
                    intent = new Intent(this, UpdateSafePasswordActivity.class);
                } else {
                    intent = new Intent(this, SetSafePasswordActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.login_password_container://登录密码
                intent=new Intent(this,UpdateLoginPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_gesture_lock://设置手势密码
                CreateLockActivity.openCreateLockActivity(this,"set");//set
                break;
            case R.id.gesture_switch://手势密码开关
                if (MMKVTools.isOpenGesture()){
                    MMKVTools.setOpenGesture(false);
                    gestureSwitch.setSelected(false);
                }else {
                    String gesturePassword = aCache.getAsString(Constant.GESTURE_PASSWORD);
                    if (TextUtils.isEmpty(gesturePassword)){
                        showToast(getString(R.string.set_gesture_first));
                    }else {
                        MMKVTools.setOpenGesture(true);
                        gestureSwitch.setSelected(true);
                    }
                }
                break;
            case R.id.fingerprint_switch:
                break;
        }
    }

    private void bindData() {
        phoneStatusTextView.setText(MMKVTools.hasBindPhone() ? R.string.opened : R.string.unopened);
        phoneStatusTextView.setTextColor(MMKVTools.hasBindPhone() ? getResources().getColor(R.color.colorB7B7C4) : getResources().getColor(R.color.colorEF2357));

        emailStatusTextView.setText(MMKVTools.hasBindEmail()?R.string.opened : R.string.unopened);
        emailStatusTextView.setTextColor(MMKVTools.hasBindEmail() ? getResources().getColor(R.color.colorB7B7C4) : getResources().getColor(R.color.colorEF2357));

        googleStatusTextView.setText(MMKVTools.hasBindGoogleCode() ? R.string.binded : R.string.unbinded);
        googleStatusTextView.setTextColor(MMKVTools.hasBindGoogleCode() ? getResources().getColor(R.color.colorB7B7C4) : getResources().getColor(R.color.colorEF2357));

        payPasswordStatusTextView.setText(MMKVTools.hasSetPayPassword() ? R.string.reset : R.string.not_set);
        payPasswordStatusTextView.setTextColor(MMKVTools.hasSetPayPassword() ? getResources().getColor(R.color.colorB7B7C4) : getResources().getColor(R.color.colorEF2357));

        if ("0".equals(MMKVTools.getRelNameStatus())) {
            //待审核
            relNameStatusTextView.setText(R.string.pending_review);
            relNameStatusTextView.setTextColor(getResources().getColor(R.color.colorEF2357));
        } else if ("2".equals(MMKVTools.getRelNameStatus())) {
            //已驳回
            relNameStatusTextView.setText(R.string.rejected);
            relNameStatusTextView.setTextColor(getResources().getColor(R.color.colorEF2357));
        } else if ("10".equals(MMKVTools.getRelNameStatus())) {
            //认证成功
            relNameStatusTextView.setText(R.string.certified);
            relNameStatusTextView.setTextColor(getResources().getColor(R.color.colorB7B7C4));
        } else {
            //未认证
            relNameStatusTextView.setText(R.string.uncertified);
            relNameStatusTextView.setTextColor(getResources().getColor(R.color.colorEF2357));
        }

    }

    @Override
    public void bindSafeSetting(SafeSettingEntity safeSetting) {
        if (safeSetting != null) {
            netError.setVisibility(View.GONE);
            netOk.setVisibility(View.VISIBLE);
            if (safeSetting.getSecurityLevel() <= 1) {
                safeLevelView.setSafeLevel(1);
                safeLevelTextView.setText(R.string.low);
                safeLevelTextView.setTextColor(getResources().getColor(R.color.colorEF2357));
            } else if (safeSetting.getSecurityLevel() <= 3) {
                safeLevelView.setSafeLevel(2);
                safeLevelTextView.setText(R.string.medium);
                safeLevelTextView.setTextColor(getResources().getColor(R.color.colorEF5923));
            } else {
                safeLevelView.setSafeLevel(3);
                safeLevelTextView.setText(R.string.high);
                safeLevelTextView.setTextColor(getResources().getColor(R.color.color24D95F));
            }
        }else {
            netError.setVisibility(View.VISIBLE);
            netOk.setVisibility(View.GONE);
        }

        bindData();
    }

    @Override
    protected void onDestroy() {
        if(mPresenter!=null){
            mPresenter.dropView();
        }
        super.onDestroy();
    }

}
