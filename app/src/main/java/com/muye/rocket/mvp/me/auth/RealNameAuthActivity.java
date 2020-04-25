package com.muye.rocket.mvp.me.auth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ifenduo.lib_base.tools.MMKVTools;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.Constant;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.mvp.me.view.RealFailReasonActivity;
import com.muye.rocket.mvp.me.view.RealNameResultActivity;
import com.muye.rocket.tools.GlideLoadUtils;
import com.muye.rocket.tools.ThreadUtils;
import com.muye.rocket.widget.dialog.CnfirmInfoDialog;
import com.muye.rocket.widget.dialog.MessageDialog;
import com.muye.rocket.widget.popup.CommonPopupWindow;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.dltech21.iddetect.IDAuth;
import io.github.dltech21.ocr.IDCardEnum;
import io.github.dltech21.ocr.IdentityInfo;
import io.github.dltech21.ocr.OnResultListener;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/*
* 实名认证
* */
public class RealNameAuthActivity extends BaseActivity implements CommonPopupWindow.ViewInterface, EasyPermissions.PermissionCallbacks, RealNameAuthContract.View, CnfirmInfoDialog.OnSelectClickListener {
    @BindView(R.id.tv_country)
    TextView tv_country;
    @BindView(R.id.tv_idcard_type)
    TextView tv_idcard_type;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_id_card)
    EditText et_id_card;

    /*
    * 其他
    * */
    @BindView(R.id.front_image_view)
    ImageView frontImageView;
    @BindView(R.id.back_image_view)
    ImageView backImageView;
    @BindView(R.id.handle_image_view)
    ImageView handleImageView;

    /*
    * 中国
    * */
    @BindView(R.id.iv_front)
    ImageView iv_front;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.iv_handle)
    ImageView iv_handle;

    @BindView(R.id.tv_submit)
    TextView tv_submit;
    @BindView(R.id.ctl_other_upload)
    ConstraintLayout ctl_other_upload;
    @BindView(R.id.ll_china_shibie)
    LinearLayout ll_china_shibie;
    private CommonPopupWindow popupWindow;
    private int countryTag = 0;//0未选择，1中国，2其他地区
    private int cardTypeTag = 0;//0未选择，1身份证，2其他

    private static final int RC_CHOOSE_FRONT_IMAGE = 1;
    private static final int RC_CHOOSE_BACK_IMAGE = 2;
    private static final int RC_CHOOSE_HANDLE_IMAGE = 3;

    String mFrontImageURL;
    String mBackImageURL;
    String mHandleImageURL;

    private RealNameAuthPresenter mRealNameAuthPresenter;
    private CnfirmInfoDialog infoDialog;
    private MessageDialog messageDialog;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_real_name_auth;
    }

    @OnClick({R.id.tv_country, R.id.tv_idcard_type, R.id.front_image_view,R.id.back_image_view,
            R.id.handle_image_view,R.id.iv_front,R.id.iv_back,R.id.iv_handle,R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.tv_country://选择国家
                selectCountryOrCardType(R.layout.popup_select_country,tv_country);
                break;
            case R.id.tv_idcard_type://选择证件类型
                if (countryTag == 0){
                    showToast(getString(R.string.select_country));
                    return;
                }
                selectCountryOrCardType(R.layout.popup_select_card_type,tv_idcard_type);
                break;

            case R.id.front_image_view://其他国家正面
                if (cardTypeTag == 0){
                    showToast(getString(R.string.select_card_type));
                    return;
                }
                chooseFrontImage();
                break;
            case R.id.back_image_view://其他国家背面
                if (cardTypeTag == 0){
                    showToast(getString(R.string.select_card_type));
                    return;
                }
                chooseBackImage();
                break;
            case R.id.handle_image_view://其他国家手持
                if (cardTypeTag == 0){
                    showToast(getString(R.string.select_card_type));
                    return;
                }
                chooseHandleImage();
                break;

            case R.id.iv_front://中国正面
                if (cardTypeTag == 0){
                    showToast(getString(R.string.select_card_type));
                    return;
                }
                chooseFrontImage();
                break;
            case R.id.iv_back://中国背面
                if (cardTypeTag == 0){
                    showToast(getString(R.string.select_card_type));
                    return;
                }
                chooseBackImage();
                break;
            case R.id.iv_handle://中国手持
                if (cardTypeTag == 0){
                    showToast(getString(R.string.select_card_type));
                    return;
                }
                chooseHandleImage();
                break;

            case R.id.tv_submit://提交
                String name = et_name.getText().toString().trim();
                String idCard = et_id_card.getText().toString().trim();

                if (countryTag == 0){
                    showToast(getString(R.string.select_country));
                    return;
                }
                if (cardTypeTag == 0){
                    showToast(getString(R.string.select_card_type));
                    return;
                }
                if (TextUtils.isEmpty(name)){
                    showToast(getString(R.string.input_real_name_hint));
                    return;
                }
                if (TextUtils.isEmpty(idCard)){
                    showToast(getString(R.string.input_card_number_hint));
                    return;
                }
                if (TextUtils.isEmpty(mFrontImageURL) || TextUtils.isEmpty(mBackImageURL) || TextUtils.isEmpty(mHandleImageURL)){
                    showToast(getString(R.string.upload_img_hint));
                    return;
                }
                if (countryTag == 1){
                    showCnfirmInfo(name,idCard);
                }else {
                    submitRealName(name,idCard);
                }
                break;
        }
    }

    /*
    * 提交数据
    * */
    private void submitRealName(String name, String idCard) {
        if (mRealNameAuthPresenter == null) {
            mRealNameAuthPresenter = new RealNameAuthPresenter(this);
        }

        if (countryTag == 1){//中国
            mRealNameAuthPresenter.submitRealName(name, idCard, mFrontImageURL, mBackImageURL, mHandleImageURL, 1);
        }else {//其他地区
            mRealNameAuthPresenter.submitRealName(name, idCard, mFrontImageURL, mBackImageURL, mHandleImageURL,2);
        }
    }

    /*
    * 确认信息
    * */
    private void showCnfirmInfo(String name, String idCard) {
        infoDialog = new CnfirmInfoDialog(this);
        infoDialog.setIdCardInfo(name,idCard);
        infoDialog.setOnClickSelect(this);
        if (infoDialog != null && !infoDialog.isShowing()){
            infoDialog.show();
        }
    }

    @AfterPermissionGranted(RC_CHOOSE_FRONT_IMAGE)
    private void chooseFrontImage() {
        openAlbum(RC_CHOOSE_FRONT_IMAGE);
    }

    @AfterPermissionGranted(RC_CHOOSE_BACK_IMAGE)
    private void chooseBackImage() {
        openAlbum(RC_CHOOSE_BACK_IMAGE);
    }

    @AfterPermissionGranted(RC_CHOOSE_HANDLE_IMAGE)
    private void chooseHandleImage() {
        openAlbum(RC_CHOOSE_HANDLE_IMAGE);
    }

    /*
    * 打开相册
    * */
    public void openAlbum(int requestCode) {
        PictureFileUtils.deleteCacheDirFile(this);
        if (!hasCameraAndStoragePermission()) {
            requestCameraAndStoragePermission(requestCode);
            return;
        }

        if (countryTag == 1){//中国
            if (requestCode == RC_CHOOSE_FRONT_IMAGE || requestCode == RC_CHOOSE_BACK_IMAGE){
                IDAuth idAuth = new IDAuth("111", "111", new OnResultListener() {
                    @Override
                    public void onResult(String isSuccess, IdentityInfo info) {
                        if (isSuccess.equals("success")) {
                            if (info.getType() == IDCardEnum.FaceEmblem) { //正面
                                et_name.setText(info.getName());
                                //可编辑
                                et_name.setFocusableInTouchMode(true);
                                et_name.setFocusable(true);
                                et_name.requestFocus();

                                et_id_card.setText(info.getCertid());
                                if (!TextUtils.isEmpty(info.getImgFront())) {
                                    uploadImage(info.getImgFront(), requestCode);
                                }
                            } else {                                         //背面
                                if (!TextUtils.isEmpty(info.getImgBehind())) {
                                    uploadImage(info.getImgBehind(), requestCode);
                                }
                            }
                        } else {
                            Log.e("mUploadPresenter", "onResult: 用户取消操作");
                        }
                    }
                });

                if (requestCode == RC_CHOOSE_FRONT_IMAGE) {//打开正面扫描
                    idAuth.iDAuthFace(this);
                } else {//打开背面扫描
                    idAuth.iDAuthNational(this);
                }
            } else {//手持
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())
                        .selectionMode(PictureConfig.SINGLE)
                        .imageSpanCount(3)
                        .previewImage(true)
                        .isCamera(true)
                        .enableCrop(true)
                        .compress(true)
                        .synOrAsy(true)
                        .withAspectRatio(3, 4)
                        .forResult(requestCode);
            }

        }else {//其他国家
            PictureSelector.create(this)
                    .openGallery(PictureMimeType.ofImage())
                    .selectionMode(PictureConfig.SINGLE)
                    .imageSpanCount(3)
                    .previewImage(true)
                    .isCamera(true)
                    .enableCrop(true)
                    .compress(true)
                    .synOrAsy(true)
                    .withAspectRatio(3,requestCode==RC_CHOOSE_HANDLE_IMAGE?4:2)
                    .forResult(requestCode);
        }
    }

    /**
     * 是否有相机和读写权限
     *
     * @return
     */
    private boolean hasCameraAndStoragePermission() {
        return EasyPermissions.hasPermissions(this, Constant.CAMERA_AND_STORAGE);
    }

    /**
     * 申请相机和读写权限
     */
    private void requestCameraAndStoragePermission(int requestCode) {
        EasyPermissions.requestPermissions(
                this, getString(R.string.request_camera_read_write_permissions),
                requestCode,
                Constant.CAMERA_AND_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RC_CHOOSE_FRONT_IMAGE || requestCode == RC_CHOOSE_BACK_IMAGE || requestCode == RC_CHOOSE_HANDLE_IMAGE) {
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

    private void uploadImage(String imagePath, int tag) {
        if (mRealNameAuthPresenter == null) {
            mRealNameAuthPresenter = new RealNameAuthPresenter(this);
        }
        mRealNameAuthPresenter.uploadImage(imagePath, tag);
    }

    /*
    * 选择国家或身份类型
    * */
    private void selectCountryOrCardType(int popLayoutId, TextView dropDownTag) {
        if (popupWindow != null && popupWindow.isShowing()) return;
        popupWindow = new CommonPopupWindow.Builder(this)
                .setView(popLayoutId)
                .setWidthAndHeight(dropDownTag.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT)
                .setAnimationStyle(R.style.AnimDown)
                .setViewOnclickListener(this)
                .create();
        popupWindow.showAsDropDown(dropDownTag);
    }

    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.popup_select_country://选择国家
                RadioGroup rg_country = view.findViewById(R.id.rg_country);
                RadioButton rb_china = view.findViewById(R.id.rb_china);
                RadioButton rb_other = view.findViewById(R.id.rb_other);

                rg_country.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.rb_china:
                                countryTag = 1;
                                if (cardTypeTag != 0){//证件类型置空
                                    cardTypeTag = 0;
                                    tv_idcard_type.setText(getString(R.string.select_card_type));
                                }
                                tv_country.setText(rb_china.getText());
                                ctl_other_upload.setVisibility(View.GONE);
                                ll_china_shibie.setVisibility(View.VISIBLE);

                                //设置不可编辑
                                et_name.setFocusable(false);
                                et_name.setFocusableInTouchMode(false);
                                et_id_card.setFocusable(false);
                                et_id_card.setFocusableInTouchMode(false);

                                ThreadUtils.getUiThreadHandler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (popupWindow != null){
                                            popupWindow.dismiss();
                                        }
                                    }
                                },500);

                                break;
                            case R.id.rb_other:
                                countryTag = 2;
                                if (cardTypeTag != 0){//证件类型置空
                                    cardTypeTag = 0;
                                    tv_idcard_type.setText(getString(R.string.select_card_type));
                                }
                                tv_country.setText(rb_other.getText());
                                ctl_other_upload.setVisibility(View.VISIBLE);
                                ll_china_shibie.setVisibility(View.GONE);

                                //设置可编辑
                                et_name.setFocusableInTouchMode(true);
                                et_name.setFocusable(true);
                                et_name.requestFocus();
                                et_id_card.setFocusableInTouchMode(true);
                                et_id_card.setFocusable(true);
                                et_id_card.requestFocus();

                                ThreadUtils.getUiThreadHandler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (popupWindow != null){
                                            popupWindow.dismiss();
                                        }
                                    }
                                },500);
                                break;
                        }
                    }
                });
                break;
            case R.layout.popup_select_card_type://选择身份类型
                RadioGroup rg_card_type = view.findViewById(R.id.rg_card_type);
                RadioButton rb_idcard = view.findViewById(R.id.rb_idcard);
                RadioButton rb_card_other = view.findViewById(R.id.rb_card_other);

                if (countryTag == 1){
                    rb_card_other.setVisibility(View.GONE);
                }else {
                    rb_card_other.setVisibility(View.VISIBLE);
                }
                rg_card_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.rb_idcard:
                                cardTypeTag = 1;
                                tv_idcard_type.setText(rb_idcard.getText());
                                ThreadUtils.getUiThreadHandler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (popupWindow != null){
                                            popupWindow.dismiss();
                                        }
                                    }
                                },500);

                                break;
                            case R.id.rb_card_other:
                                cardTypeTag = 2;
                                tv_idcard_type.setText(rb_card_other.getText());
                                ThreadUtils.getUiThreadHandler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (popupWindow != null){
                                            popupWindow.dismiss();
                                        }
                                    }
                                },500);
                                break;
                        }
                    }
                });
                break;
        }
    }

    /*
    * 上传图片成功
    * */
    @Override
    public void onUploadImageSuccess(String imageUrl, int tag) {
        PictureFileUtils.deleteCacheDirFile(this);
        if (RC_CHOOSE_FRONT_IMAGE == tag) {
            mFrontImageURL = imageUrl;
            GlideLoadUtils.getInstance().glideLoad(this, mFrontImageURL, countryTag == 1 ? iv_front : frontImageView);
        } else if (RC_CHOOSE_BACK_IMAGE == tag) {
            mBackImageURL = imageUrl;
            GlideLoadUtils.getInstance().glideLoad(this, mBackImageURL, countryTag == 1 ? iv_back : backImageView);
        } else if (RC_CHOOSE_HANDLE_IMAGE == tag) {
            mHandleImageURL = imageUrl;
            GlideLoadUtils.getInstance().glideLoad(this, mHandleImageURL, countryTag == 1 ? iv_handle : handleImageView);
        }
    }

    /*
    * 上传图片失败
    * */
    @Override
    public void onUploadImageFail(String error) {
        if (countryTag == 1){//上传身份证照片失败
            showToast("com.alibaba.sdk.android.oss.ClientException");
            if (messageDialog == null)
                messageDialog = new MessageDialog(this);
            messageDialog.setTitle(getString(R.string.faile_title));
            messageDialog.setMessage(getString(R.string.file_dialog_msg));
            messageDialog.setOkText(getString(R.string.ok));
            messageDialog.setCancelText(getString(R.string.cancel));
            messageDialog.setOnOkButtonClickListener(dialog -> {
                Intent intent = new Intent(this,RealFailReasonActivity.class);
                startActivity(intent);
                dialog.dismiss();
            });
            if (messageDialog != null&&!messageDialog.isShowing())
                messageDialog.show();
        }
        PictureFileUtils.deleteCacheDirFile(this);
    }

    /*
    * 提交成功
    * */
    @Override
    public void onSubmitRealNameSuccess() {
        MMKVTools.saveRelNameStatus("0");
        showToast(getString(R.string.exc_wal_submit_success));
        Intent intent = new Intent(this, RealNameResultActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        if (mRealNameAuthPresenter != null) {
            mRealNameAuthPresenter.dropView();
        }
        super.onDestroy();
    }

    @Override
    public void onSubmit() {
        //提交
        String name = et_name.getText().toString().trim();
        String idCard = et_id_card.getText().toString().trim();

        submitRealName(name,idCard);
    }

    @Override
    public void onUpdateInfo(CnfirmInfoDialog dialog) {
        dialog.dismiss();
        et_name.setFocusableInTouchMode(true);
        et_name.setFocusable(true);
        et_name.requestFocus();
        et_name.setSelection(et_name.getText().length());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
}
