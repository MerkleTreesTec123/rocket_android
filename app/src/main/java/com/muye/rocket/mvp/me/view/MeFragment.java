package com.muye.rocket.mvp.me.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ifenduo.common.view.CircleImageView;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseFragment;
import com.muye.rocket.entity.SafeSettingEntity;
import com.muye.rocket.mvp.me.auth.RealNameAuthActivity;
import com.muye.rocket.mvp.me.contract.MeContract;
import com.muye.rocket.mvp.me.presenter.MePresenter;
import com.muye.rocket.tools.StringTools;
import com.muye.rocket.tools.Validator;
import com.muye.rocket.widget.dialog.ReamNameDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MeFragment extends BaseFragment implements MeContract.View, ReamNameDialog.OnRealNameClickListener {
    @BindView(R.id.avatar_image_view)
    CircleImageView avatarImageView;
    @BindView(R.id.nick_text_view)
    TextView nickTextView;
    @BindView(R.id.status_text_view)
    TextView statusTextView;
    Unbinder unbinder;

    MeContract.Presenter mPresenter;
    ReamNameDialog nameDialog;
    String realName;

    public static MeFragment newInstance() {
        Bundle args = new Bundle();
        MeFragment fragment = new MeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getRootViewLayout() {
        return R.layout.fragment_me;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            mImmersionBar.statusBarDarkFont(true, 0.2f).init();
        }
    }

    @Override
    protected void initView(View parentView) {
        unbinder = ButterKnife.bind(this, parentView);

        getTbTitle().setText(getString(R.string.account));
        getTbTitle().setTextColor(getResources().getColor(R.color.black));
        getTbBack().setVisibility(View.GONE);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onStart() {
        super.onStart();
        fetchSafeSetting();
        bindUserInfo();
    }

    @OnClick({R.id.safe_center_container, R.id.invite_list_container, R.id.help_container,
            R.id.invite_container, R.id.service_container, R.id.setting_container,R.id.personal_data_button,
            R.id.bind_wallet_container})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.bind_wallet_container:   // 绑定钱包 BindWalletQrCodeActivity
                if (MMKVTools.hasBindPhone()) {
                    if ("10".equals(MMKVTools.getRelNameStatus())) {
                        intent = new Intent(getContext(), BindWalletQrCodeActivity.class);
                        startActivity(intent);
                    }else showToast(getString(R.string.play_after_realname));
                }else showToast(getString(R.string.bind_you_phone));
                break;
            case R.id.safe_center_container://账户与安全
                intent = new Intent(getContext(), SafeCenterActivity.class);
                startActivity(intent);
                break;
            case R.id.invite_list_container://邀请榜
                intent = new Intent(getContext(), InviteRankingActivity.class);
                startActivity(intent);
                break;
            case R.id.help_container://帮助与支持
                intent = new Intent(getContext(), HelpSupportActivity.class);
                startActivity(intent);
                break;
            case R.id.invite_container://邀请好友
                intent = new Intent(getContext(), InviteActivity.class);
                startActivity(intent);
                break;
            case R.id.service_container:
                intent = new Intent(getContext(), CustomerServiceActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_container:
                intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.personal_data_button:
                intent = new Intent(getContext(), PersonalActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void fetchSafeSetting() {
        if (mPresenter == null) {
            mPresenter = new MePresenter(this);
        }
        mPresenter.fetchSafeSetting();
    }

    private void bindUserInfo() {
        nickTextView.setText("Hi," + StringTools.phoneNumberFormat(!TextUtils.isEmpty(MMKVTools.getPhone())?MMKVTools.getPhone():MMKVTools.getEmail()));
        if ("0".equals(MMKVTools.getRelNameStatus())) {
            //待审核
            statusTextView.setText(R.string.pending_review);
//            statusTextView.setTextColor(getResources().getColor(R.color.colorEF2357));
        } else if ("2".equals(MMKVTools.getRelNameStatus())) {
            //已驳回
            statusTextView.setText(R.string.rejected);
//            statusTextView.setTextColor(getResources().getColor(R.color.colorEF2357));
        } else if ("10".equals(MMKVTools.getRelNameStatus())) {
            //认证成功
            String real = getString(R.string.certified) + "姓名："+ realName;
            statusTextView.setText(real);
//            statusTextView.setTextColor(getResources().getColor(R.color.colorB7B7C4));
        } else {
            //未认证
            statusTextView.setText(R.string.uncertified);
//            statusTextView.setTextColor(getResources().getColor(R.color.colorEF2357));
        }
    }

    public void isShowRealName(){
        // 增加提示框，提醒用户未实名
        if (Validator.IS_REALNAME == 0) {
            if (nameDialog == null) {
                nameDialog = new ReamNameDialog(getContext());
                nameDialog.setOnRealNameListener(this);
            }
            if (!nameDialog.isShowing()) {
                nameDialog.show();
            }
            Validator.IS_REALNAME = 1;
        }
    }

    @Override
    public void toRealNameClick(ReamNameDialog dialog) {
        dialog.dismiss();
        Intent intent = new Intent(getContext(), RealNameAuthActivity.class);
        startActivity(intent);
    }

    @Override
    public void sayLater(ReamNameDialog dialog) {
        if (nameDialog != null&&nameDialog.isShowing()){
            nameDialog.dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.dropView();
        }
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void bindSafeSetting(SafeSettingEntity safeSetting) {
        if (safeSetting != null) {
            if (safeSetting.getIdentity()!= null && safeSetting.getIdentity().getFname() != null)
            realName = safeSetting.getIdentity().getFname();
            if (!TextUtils.isEmpty(realName)) {
                MMKVTools.setRealName(realName);
            }
            bindUserInfo();
            if (!"0".equals(MMKVTools.getRelNameStatus()) && !"2".equals(MMKVTools.getRelNameStatus()) && !"10".equals(MMKVTools.getRelNameStatus())){
                isShowRealName();
            }

        }
    }

}
