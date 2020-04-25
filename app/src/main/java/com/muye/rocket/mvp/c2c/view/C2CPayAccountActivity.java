package com.muye.rocket.mvp.c2c.view;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.entity.c2c.C2CPayAccount;
import com.muye.rocket.entity.c2c.C2CPayAccountInfo;
import com.muye.rocket.mvp.c2c.contract.C2CPayAccountContract;
import com.muye.rocket.mvp.c2c.contract.C2CUpdatePayAccountContract;
import com.muye.rocket.mvp.c2c.presenter.C2CPayAccountPresenter;
import com.muye.rocket.mvp.c2c.presenter.C2CUpdatePayAccountPresenter;
import com.muye.rocket.tools.GlideLoadUtils;
import com.muye.rocket.tools.StringTools;
import com.muye.rocket.widget.ShowQrCodeDialog;
import com.muye.rocket.widget.dialog.C2CAddAccountDialog;
import com.muye.rocket.widget.dialog.PayPasswordDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * C2C收款地址
 */
public class C2CPayAccountActivity extends BaseActivity implements View.OnClickListener, C2CPayAccountContract.View, C2CUpdatePayAccountContract.View {

    @BindView(R.id.bank_view)
    RelativeLayout bankView;
    @BindView(R.id.ali_view)
    RelativeLayout aliView;
    @BindView(R.id.wx_view)
    RelativeLayout wxView;

    @BindView(R.id.bankUName_text_view)
    TextView bankUNameTextView;
    @BindView(R.id.bankNumber_text_view)
    TextView bankNumberTextView;
    @BindView(R.id.bankName_text_view)
    TextView bankNameTextView;

    @BindView(R.id.aliAccount_text_view)
    TextView aliAccountTextView;
    @BindView(R.id.aliUName_text_view)
    TextView aliUNameTextView;

    @BindView(R.id.wxAccount_text_view)
    TextView wxAccountTextView;
    @BindView(R.id.wxUName_text_view)
    TextView wxUNameTextView;

    @BindView(R.id.empty_image_view)
    ImageView emptyImageView;
    @BindView(R.id.empty_text_view)
    TextView emptyTextView;
    @BindView(R.id.empty_view)
    FrameLayout emptyView;
    @BindView(R.id.account_container)
    LinearLayout accountContainer;
    @BindView(R.id.bottom_container)
    FrameLayout buttonContainer;

    C2CPayAccount mBankInfo;
    C2CPayAccount mAlipayInfo;
    C2CPayAccount mWechatInfo;
    private ShowQrCodeDialog mShowQrCodeDialog;
    private C2CAddAccountDialog mActionMenu;
    C2CPayAccountContract.Presenter mInfoPresenter;
    C2CUpdatePayAccountContract.Presenter mUpdatePresenter;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.c2c_activity_pay_account;
    }


    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.c2c_collection_address));

        emptyView.setVisibility(View.VISIBLE);
        accountContainer.setVisibility(View.GONE);
        emptyTextView.setText(R.string.c2c_add_account_hint);
        emptyTextView.setTextColor(getResources().getColor(R.color.color656885));
        GlideLoadUtils.getInstance().glideLoad(this, R.drawable.ic_empty, emptyImageView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchData();
    }

    private void fetchData() {
        if (mInfoPresenter == null) {
            mInfoPresenter = new C2CPayAccountPresenter(this);
        }
        mInfoPresenter.fetchC2CPayAccountInfo();
    }

    private void initActionMenu() {
        if (mActionMenu == null) {
            mActionMenu = new C2CAddAccountDialog(this);
        }
    }

    private void setupAddButton() {
        if (mBankInfo != null && mAlipayInfo != null && mWechatInfo != null) {
            //添加收款地址
            buttonContainer.setVisibility(View.GONE);
        } else {
            buttonContainer.setVisibility(View.VISIBLE);
        }

    }

    private void showActionMenu() {
        initActionMenu();
        //暂时关闭银行卡功能
//        mActionMenu.setIsShowBankCard(mBankInfo == null);
        mActionMenu.setIsShowBankCard(false);
        mActionMenu.setIsShowAlipay(mAlipayInfo == null);
        mActionMenu.setIsShowWechat(mWechatInfo == null);
        if (!mActionMenu.isShowing()) {
            mActionMenu.show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }


    @OnClick({R.id.bank_edit, R.id.bank_delete,
            R.id.ali_qr, R.id.ali_edit, R.id.ali_delete,
            R.id.wx_qr, R.id.wx_edit, R.id.wx_delete, R.id.submit_button})
    void onClicks(View view) {
        switch (view.getId()) {
            case R.id.bank_edit:  //银行卡编辑
                BankAddressActivity.openBankAddressActivity(this, mBankInfo);
                break;
            case R.id.bank_delete://银行卡删除
                deleteBankCard();
                break;
            case R.id.ali_qr:     //支付宝二维码
                if (mAlipayInfo == null) return;
                showQrCodeDialog(mAlipayInfo.getUrl(), getString(R.string.c2c_alipay_collection_qr_code));
                break;
            case R.id.ali_edit:   //支付宝编辑
                AlipayAddressActivity.openAlipayAddressActivity(this, mAlipayInfo);
                break;
            case R.id.ali_delete: //支付删除
                deleteAlipay();
                break;
            case R.id.wx_qr:      //微信二维码
                if (mWechatInfo == null) return;
                showQrCodeDialog(mWechatInfo.getUrl(), getString(R.string.c2c_wechat_collection_qr_code));
                break;
            case R.id.wx_edit:    //微信编辑
                WeChatAddressActivity.openWeChatAddressActivity(this, mWechatInfo);
                break;
            case R.id.wx_delete:  //微信删除
                deleteWechat();
                break;
            case R.id.submit_button:
                showActionMenu();
                break;
        }
    }

    //显示二维码
    private void showQrCodeDialog(String path, String title) {
        if (mShowQrCodeDialog == null) {
            mShowQrCodeDialog = new ShowQrCodeDialog(this);
        }
        if (!mShowQrCodeDialog.isShowing()) {
            mShowQrCodeDialog.show();
        }
        mShowQrCodeDialog.setQrImage(path);
        mShowQrCodeDialog.setTitle(title);
    }

    private void deleteBankCard() {
        PayPasswordDialog payPasswordDialog = new PayPasswordDialog(this);
        payPasswordDialog.setInputPayPasswordCallBack(new PayPasswordDialog.InputPayPasswordCallBack() {
            @Override
            public void getPayPassword(String payPassword) {
                if (mUpdatePresenter == null) {
                    mUpdatePresenter = new C2CUpdatePayAccountPresenter(C2CPayAccountActivity.this);
                }
                mUpdatePresenter.submitC2CPayAccount("null", "", "", payPassword);
            }
        });
        payPasswordDialog.show();
    }

    private void deleteAlipay() {
        PayPasswordDialog payPasswordDialog = new PayPasswordDialog(this);
        payPasswordDialog.setInputPayPasswordCallBack(new PayPasswordDialog.InputPayPasswordCallBack() {
            @Override
            public void getPayPassword(String payPassword) {
                if (mUpdatePresenter == null) {
                    mUpdatePresenter = new C2CUpdatePayAccountPresenter(C2CPayAccountActivity.this);
                }
                mUpdatePresenter.submitC2CPayAccount("", "null", "", payPassword);
            }
        });
        payPasswordDialog.show();
    }

    private void deleteWechat() {
        PayPasswordDialog payPasswordDialog = new PayPasswordDialog(this);
        payPasswordDialog.setInputPayPasswordCallBack(new PayPasswordDialog.InputPayPasswordCallBack() {
            @Override
            public void getPayPassword(String payPassword) {
                if (mUpdatePresenter == null) {
                    mUpdatePresenter = new C2CUpdatePayAccountPresenter(C2CPayAccountActivity.this);
                }
                mUpdatePresenter.submitC2CPayAccount("", "", "null", payPassword);
            }
        });
        payPasswordDialog.show();
    }

    @Override
    public void bindC2CPayAccountInfo(C2CPayAccountInfo accountInfo) {
        if (accountInfo != null) {
            mBankInfo = accountInfo.getBank();
            mAlipayInfo = accountInfo.getAlipay();
            mWechatInfo = accountInfo.getWxpay();
        } else {
            mBankInfo = null;
            mAlipayInfo = null;
            mWechatInfo = null;
        }

        if (mBankInfo == null && mAlipayInfo == null && mWechatInfo == null) {
            emptyView.setVisibility(View.VISIBLE);
            accountContainer.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            accountContainer.setVisibility(View.VISIBLE);
            // 暂时关闭银行卡显示
//            if (mBankInfo != null) {
//                bankView.setVisibility(View.VISIBLE);
//                bankUNameTextView.setText(mBankInfo.getUsername());
//                bankNameTextView.setText(mBankInfo.getBanname());
//                bankNumberTextView.setText(StringTools.formatBankCardNo(mBankInfo.getBannumber()));
//            } else {
                bankView.setVisibility(View.GONE);
//            }

            if (mAlipayInfo != null) {
                aliView.setVisibility(View.VISIBLE);
                aliAccountTextView.setText(StringTools.phoneNumberFormat(mAlipayInfo.getAccount()));
                aliUNameTextView.setText(mAlipayInfo.getUsername());
            } else {
                aliView.setVisibility(View.GONE);
            }

            if (mWechatInfo != null) {
                wxView.setVisibility(View.VISIBLE);
                wxAccountTextView.setText(StringTools.phoneNumberFormat(mWechatInfo.getAccount()));
                wxUNameTextView.setText(mWechatInfo.getUsername());
            } else {
                wxView.setVisibility(View.GONE);
            }
        }
        setupAddButton();
    }

    @Override
    public void onSubmitC2CPayAccountSuccess() {
        fetchData();
    }

    @Override
    protected void onDestroy() {
        if (mInfoPresenter != null) mInfoPresenter.dropView();
        super.onDestroy();
    }

}
