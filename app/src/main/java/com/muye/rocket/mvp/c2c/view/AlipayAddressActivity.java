package com.muye.rocket.mvp.c2c.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.Constant;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.entity.c2c.C2CPayAccount;
import com.muye.rocket.mvp.c2c.contract.C2CUpdatePayAccountContract;
import com.muye.rocket.mvp.c2c.contract.UploadImageContract;
import com.muye.rocket.mvp.c2c.presenter.C2CUpdatePayAccountPresenter;
import com.muye.rocket.mvp.c2c.presenter.UploadImagePresenter;
import com.muye.rocket.tools.GlideLoadUtils;
import com.muye.rocket.tools.filter.EditTextFilter;
import com.muye.rocket.widget.dialog.PayPasswordDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.luck.picture.lib.config.PictureMimeType.ofImage;

/**
 * C2C 添加/修改支付宝收款地址
 */
public class AlipayAddressActivity extends BaseActivity implements UploadImageContract.View,
        C2CUpdatePayAccountContract.View, PayPasswordDialog.InputPayPasswordCallBack {
    private static final int RC_CAMERA_AND_CONTACTS = 1;
    @BindView(R.id.accountName_edit_text)
    EditText accountNameEditText;
    @BindView(R.id.account_edit_text)
    EditText accountEditText;

    @BindView(R.id.qr_image)
    ImageView qrImageView;
    @BindView(R.id.addQrCodeTips_view)
    LinearLayout addQrCodeTipsView;

    @BindView(R.id.submit_button)
    Button submitButton;

    C2CPayAccount mAccount;
    private String mQRImageUrl;
    PayPasswordDialog mPasswordDialog;
    UploadImageContract.Presenter mUploadImagePresenter;
    C2CUpdatePayAccountContract.Presenter mC2CPayAccountPresenter;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.c2c_activity_alipay_address;
    }

    @Override
    protected int getToolbarColor() {
        return Color.WHITE;
    }

    public static void openAlipayAddressActivity(Context context, C2CPayAccount account) {
        Intent intent = new Intent(context, AlipayAddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("account", account);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.c2c_alipay));
        accountNameEditText.setFilters(new InputFilter[]{new EditTextFilter(100)});
        accountEditText.setFilters(new InputFilter[]{new EditTextFilter(80)});
        setupInputListener();

        if(mAccount!=null){
            accountNameEditText.setText(mAccount.getUsername());
            accountEditText.setText(mAccount.getAccount());
            bindQRImage(mAccount.getUrl());
        }
    }

    @Override
    protected void handleIntent(Bundle bundle) {
        super.handleIntent(bundle);
        if (bundle!=null){
            mAccount=bundle.getParcelable("account");
        }
    }

    private void setupInputListener() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setSubmitButtonEnable();
            }
        };

        accountEditText.addTextChangedListener(textWatcher);
        accountNameEditText.addTextChangedListener(textWatcher);
    }


    @OnClick({R.id.add_ali_qr, R.id.submit_button})
    void onClicks(View view) {
        switch (view.getId()) {
            case R.id.add_ali_qr:
                //选择图片
                if (EasyPermissions.hasPermissions(this, Constant.CAMERA_AND_CONTACTS)) {
                    openPhotoChoose();
                } else {
                    EasyPermissions.requestPermissions(this, getString(R.string.c2c_photos_permission), RC_CAMERA_AND_CONTACTS, Constant.CAMERA_AND_CONTACTS);
                }
                break;
            case R.id.submit_button:
                showPasswordDialog();
                break;
        }
    }

    private void setSubmitButtonEnable() {
        String accountName = accountNameEditText.getText().toString();
        String account = accountEditText.getText().toString();
        submitButton.setEnabled(accountName.length() > 0
                && account.length() > 0
                && !TextUtils.isEmpty(mQRImageUrl));
    }

    private void showPasswordDialog() {
        if (mPasswordDialog == null) {
            mPasswordDialog = new PayPasswordDialog(this);
            mPasswordDialog.setInputPayPasswordCallBack(this);
        }
        if (!mPasswordDialog.isShowing()) {
            mPasswordDialog.show();
        }
    }

    @AfterPermissionGranted(RC_CAMERA_AND_CONTACTS)
    private void openPhotoChoose() {
        PictureSelector.create(this).openGallery(ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.JPEG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .enableCrop(true)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .cropWH(375, 375)// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                if (data == null) return;
                String fileUrl;
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                if (selectList.get(0).isCompressed()) {
                    fileUrl = selectList.get(0).getCompressPath();
                } else if (selectList.get(0).isCut()) {
                    fileUrl = selectList.get(0).getCutPath();
                } else {
                    fileUrl = selectList.get(0).getPath();
                }

                if (mUploadImagePresenter == null) {
                    mUploadImagePresenter = new UploadImagePresenter(this);
                }
                mUploadImagePresenter.uploadImage(fileUrl, PictureConfig.CHOOSE_REQUEST);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults,this);
    }

    @Override
    public void getPayPassword(String payPassword) {
        if (mC2CPayAccountPresenter == null) {
            mC2CPayAccountPresenter = new C2CUpdatePayAccountPresenter(this);
        }
        String account = accountEditText.getText().toString();
        String nick = accountNameEditText.getText().toString();
        C2CPayAccount payAccount = new C2CPayAccount();
        payAccount.setUsername(nick);
        payAccount.setUrl(mQRImageUrl);
        payAccount.setAccount(account);
        Gson gson = new Gson();
        mC2CPayAccountPresenter.submitC2CPayAccount("", gson.toJson(payAccount), "", payPassword);
    }

    @Override
    public void onUploadImageSuccess(String fileID, String url, int tag) {
        PictureFileUtils.deleteCacheDirFile(AlipayAddressActivity.this);
        bindQRImage(url);
    }

    @Override
    public void onUploadImageFail() {
        PictureFileUtils.deleteCacheDirFile(AlipayAddressActivity.this);
    }
    private void bindQRImage(String url){
        addQrCodeTipsView.setVisibility(View.GONE);
        mQRImageUrl = url;
        GlideLoadUtils.getInstance().glideLoad(this, mQRImageUrl, qrImageView);
        setSubmitButtonEnable();
    }

    @Override
    public void onSubmitC2CPayAccountSuccess() {
        showToast(getString(R.string.c2c_submit_success));
        finish();
    }

    @Override
    protected void onDestroy() {
        if (mC2CPayAccountPresenter != null) mC2CPayAccountPresenter.dropView();
        if (mUploadImagePresenter != null) mUploadImagePresenter.dropView();
        super.onDestroy();
    }
}
