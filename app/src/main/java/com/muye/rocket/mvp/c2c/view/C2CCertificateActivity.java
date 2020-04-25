package com.muye.rocket.mvp.c2c.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.Constant;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.mvp.c2c.contract.UpdateOrderStatusContract;
import com.muye.rocket.mvp.c2c.contract.UploadImageContract;
import com.muye.rocket.mvp.c2c.presenter.UpdateOrderStatusPresenter;
import com.muye.rocket.mvp.c2c.presenter.UploadImagePresenter;
import com.muye.rocket.tools.GlideLoadUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class C2CCertificateActivity extends BaseActivity implements UploadImageContract.View,
        EasyPermissions.PermissionCallbacks, UpdateOrderStatusContract.View {

    private static final int RC_CHOOSE_IMAGE = 1;

    @BindView(R.id.qr_image)
    ImageView voucherImageView;
    @BindView(R.id.add_tips_container)
    LinearLayout addTipsContainer;
    @BindView(R.id.bank_card_container)
    RelativeLayout bankCardContainer;
    @BindView(R.id.alipay_container)
    RelativeLayout alipayContainer;
    @BindView(R.id.wechat_container)
    RelativeLayout wechatContainer;
    @BindView(R.id.submit_button)
    Button submitButton;

    String mOrderID;
    String mPayType;
    String mImageURL;
    UploadImageContract.Presenter mUploadImagePresenter;
    UpdateOrderStatusContract.Presenter mUpdateOrderPresenter;

    public static void openC2CCertificateActivity(Context context, String orderID, String payType) {
        Intent intent = new Intent(context, C2CCertificateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("orderID", orderID);
        bundle.putString("payType", payType);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.c2c_activity_certificate;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.c2c_payment_voucher));
        bindPayType();
        submitButton.setEnabled(true);
    }

    @Override
    protected void handleIntent(Bundle bundle) {
        super.handleIntent(bundle);
        if (bundle != null) {
            mOrderID = bundle.getString("orderID", "");
            mPayType = bundle.getString("payType", "");
        }
    }

    private void bindPayType() {
        if (com.muye.rocket.Constant.C2C_PAY_TYPE_BANK_CARD.equals(mPayType)) {
            bankCardContainer.setVisibility(View.VISIBLE);
            alipayContainer.setVisibility(View.GONE);
            wechatContainer.setVisibility(View.GONE);
        } else if (com.muye.rocket.Constant.C2C_PAY_TYPE_BANK_CARD_ALIPAY.equals(mPayType)) {
            bankCardContainer.setVisibility(View.VISIBLE);
            alipayContainer.setVisibility(View.VISIBLE);
            wechatContainer.setVisibility(View.GONE);
        } else if (com.muye.rocket.Constant.C2C_PAY_TYPE_BANK_CARD_WECHAT.equals(mPayType)) {
            bankCardContainer.setVisibility(View.VISIBLE);
            alipayContainer.setVisibility(View.GONE);
            wechatContainer.setVisibility(View.VISIBLE);
        } else if (com.muye.rocket.Constant.C2C_PAY_TYPE_BANK_CARD_ALIPAY_WECHAT.equals(mPayType)) {
            bankCardContainer.setVisibility(View.VISIBLE);
            alipayContainer.setVisibility(View.VISIBLE);
            wechatContainer.setVisibility(View.VISIBLE);
        } else if (com.muye.rocket.Constant.C2C_PAY_TYPE_ALIPAY.equals(mPayType)) {
            bankCardContainer.setVisibility(View.GONE);
            alipayContainer.setVisibility(View.VISIBLE);
            wechatContainer.setVisibility(View.GONE);
        } else if (com.muye.rocket.Constant.C2C_PAY_TYPE_ALIPAY_WECHAT.equals(mPayType)) {
            bankCardContainer.setVisibility(View.GONE);
            alipayContainer.setVisibility(View.VISIBLE);
            wechatContainer.setVisibility(View.VISIBLE);
        } else if (com.muye.rocket.Constant.C2C_PAY_TYPE_WECHAT.equals(mPayType)) {
            bankCardContainer.setVisibility(View.GONE);
            alipayContainer.setVisibility(View.GONE);
            wechatContainer.setVisibility(View.VISIBLE);
        } else {
            bankCardContainer.setVisibility(View.GONE);
            alipayContainer.setVisibility(View.GONE);
            wechatContainer.setVisibility(View.GONE);
        }

    }

    @OnClick({R.id.image_container, R.id.bank_card_container, R.id.alipay_container, R.id.wechat_container, R.id.submit_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_container:
                openAlbum();
                break;
            case R.id.bank_card_container:
                bankCardContainer.setSelected(true);
                alipayContainer.setSelected(false);
                wechatContainer.setSelected(false);
                setSubmitButtonEnable();
                break;
            case R.id.alipay_container:
                bankCardContainer.setSelected(false);
                alipayContainer.setSelected(true);
                wechatContainer.setSelected(false);
                setSubmitButtonEnable();
                break;
            case R.id.wechat_container:
                bankCardContainer.setSelected(false);
                alipayContainer.setSelected(false);
                wechatContainer.setSelected(true);
                setSubmitButtonEnable();
                break;
            case R.id.submit_button:
                String payType = "";
                if (bankCardContainer.isSelected()) {
                    payType = com.muye.rocket.Constant.C2C_PAY_TYPE_BANK_CARD;
                } else if (alipayContainer.isSelected()) {
                    payType = com.muye.rocket.Constant.C2C_PAY_TYPE_ALIPAY;
                } else if (wechatContainer.isSelected()) {
                    payType = com.muye.rocket.Constant.C2C_PAY_TYPE_WECHAT;
                }

                if (TextUtils.isEmpty(mImageURL)) {
                    showToast(getString(R.string.c2c_please_upload_voucher));
                    return;
                }

                if (TextUtils.isEmpty(payType)) {
                    showToast(getString(R.string.c2c_choose_payment_method));
                    return;
                }
                if (mUpdateOrderPresenter == null) {
                    mUpdateOrderPresenter = new UpdateOrderStatusPresenter(this);
                }
                mUpdateOrderPresenter.updateOrderStatus(mOrderID, payType, "", mImageURL, "2");
                break;
        }
    }

    private void setSubmitButtonEnable() {
//        submitButton.setEnabled(bankCardContainer.isSelected() || alipayContainer.isSelected() || wechatContainer.isSelected());
        submitButton.setEnabled(true);
    }

    @AfterPermissionGranted(RC_CHOOSE_IMAGE)
    private void openAlbum() {
        PictureFileUtils.deleteCacheDirFile(this);
        if (!hasCameraAndStoragePermission()) {
            requestCameraAndStoragePermission(RC_CHOOSE_IMAGE);
            return;
        }

        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .selectionMode(PictureConfig.SINGLE)
                .imageSpanCount(3)
                .previewImage(true)
                .isCamera(true)
                .enableCrop(false)
                .compress(true)
                .synOrAsy(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    private void uploadImage(String imagePath, int tag) {
        if (mUploadImagePresenter == null) {
            mUploadImagePresenter = new UploadImagePresenter(this);
        }
        mUploadImagePresenter.uploadImage(imagePath, tag);
    }

    @Override
    public void onUploadImageSuccess(String fileID, String url, int tag) {
        mImageURL = url;
        GlideLoadUtils.getInstance().glideLoad(this, url, voucherImageView);
        addTipsContainer.setVisibility(View.GONE);
        voucherImageView.setVisibility(View.VISIBLE);
        setSubmitButtonEnable();
    }

    @Override
    public void onUploadImageFail() {

    }

    @Override
    public void onUpdateOrderStatusSuccess() {
        showToast(getString(R.string.c2c_submit_success));
        finish();
    }

    /**
     * 是否有相机和读写权限
     *
     * @return
     */
    private boolean hasCameraAndStoragePermission() {
        return EasyPermissions.hasPermissions(this, Constant.CAMERA_AND_CONTACTS);
    }

    /**
     * 申请相机和读写权限
     */
    private void requestCameraAndStoragePermission(int requestCode) {
        EasyPermissions.requestPermissions(
                this, getString(R.string.request_camera_read_write_permissions),
                requestCode,
                Constant.CAMERA_AND_CONTACTS);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureConfig.CHOOSE_REQUEST) {
            String imagePath = "";
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            if (selectList != null && selectList.size() > 0) {
                LocalMedia localMedia = selectList.get(0);
                if (localMedia != null) {
                    if (localMedia.isCompressed()) {
                        imagePath = localMedia.getCompressPath();
                    } else if (localMedia.isCut()) {
                        imagePath = localMedia.getCutPath();
                    } else {
                        imagePath = localMedia.getPath();
                    }
                }
            }
            if (!TextUtils.isEmpty(imagePath)) {
                uploadImage(imagePath, requestCode);
            }
        }
    }
}
