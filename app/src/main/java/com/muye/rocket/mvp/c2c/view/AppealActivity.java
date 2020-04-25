package com.muye.rocket.mvp.c2c.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

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

import static com.luck.picture.lib.config.PictureMimeType.ofImage;

public class AppealActivity extends BaseActivity implements UploadImageContract.View, UpdateOrderStatusContract.View {
    private static final int RC_CAMERA_AND_CONTACTS = 1;

    @BindView(R.id.content_edit_text)
    EditText contentEditText;
    @BindView(R.id.qr_image)
    ImageView qrImageView;
    @BindView(R.id.tips_view)
    LinearLayout tipsView;
    @BindView(R.id.submit_button)
    Button submitButton;

    String mOrderID;
    String mQRImageUrl;
    UploadImageContract.Presenter mUploadImagePresenter;
    UpdateOrderStatusContract.Presenter mUpdateOrderStatusPresenter;

    public static void openAppealActivity(Context context, String orderID) {
        Intent intent = new Intent(context, AppealActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("orderID", orderID);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.c2c_activity_appeal;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.c2c_appeal));
        setupInputListener();
    }

    @Override
    protected void handleIntent(Bundle bundle) {
        super.handleIntent(bundle);
        if (bundle != null) {
            mOrderID = bundle.getString("orderID", "");
        }
    }

    private void setupInputListener() {
        contentEditText.addTextChangedListener(new TextWatcher() {
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
        });
    }

    private void setSubmitButtonEnable() {
        submitButton.setEnabled(!TextUtils.isEmpty(contentEditText.getText().toString()));
    }

    @OnClick({R.id.qr_container, R.id.submit_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.qr_container:
                openPhotoChoose();
                break;
            case R.id.submit_button:
                if (mUpdateOrderStatusPresenter == null) {
                    mUpdateOrderStatusPresenter = new UpdateOrderStatusPresenter(this);
                }
                mUpdateOrderStatusPresenter.updateOrderStatus(mOrderID,"", contentEditText.getText().toString(), mQRImageUrl, "3");
                break;
        }
    }

    @AfterPermissionGranted(RC_CAMERA_AND_CONTACTS)
    private void openPhotoChoose() {
        if (EasyPermissions.hasPermissions(this, Constant.CAMERA_AND_CONTACTS)) {
            PictureSelector.create(this).openGallery(ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .imageSpanCount(4)// 每行显示个数 int
                    .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                    .previewImage(true)// 是否可预览图片 true or false
                    .isCamera(true)// 是否显示拍照按钮 true or false
                    .imageFormat(PictureMimeType.JPEG)// 拍照保存图片格式后缀,默认jpeg
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                    .compress(true)// 是否压缩 true or false
                    .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                    .synOrAsy(true)//同步true或异步false 压缩 默认同步
                    .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.c2c_photos_permission), RC_CAMERA_AND_CONTACTS, Constant.CAMERA_AND_CONTACTS);
        }

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

    private void bindQRImage(String url) {
        tipsView.setVisibility(View.GONE);
        mQRImageUrl = url;
        GlideLoadUtils.getInstance().glideLoad(this, mQRImageUrl, qrImageView);
        setSubmitButtonEnable();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onUploadImageSuccess(String fileID, String url, int tag) {
        PictureFileUtils.deleteCacheDirFile(this);
        bindQRImage(url);
    }

    @Override
    public void onUploadImageFail() {
        PictureFileUtils.deleteCacheDirFile(this);
    }

    @Override
    public void onUpdateOrderStatusSuccess() {
        showToast(getString(R.string.c2c_submit_success));
        finish();
    }

    @Override
    protected void onDestroy() {
        if (mUpdateOrderStatusPresenter != null) mUpdateOrderStatusPresenter.dropView();
        if (mUploadImagePresenter != null) mUploadImagePresenter.dropView();
        super.onDestroy();
    }
}
