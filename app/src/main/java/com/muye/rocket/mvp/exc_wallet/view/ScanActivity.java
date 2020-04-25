package com.muye.rocket.mvp.exc_wallet.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.base.BaseFragment;
import com.muye.rocket.entity.exc_wallet.AddressInfo;
import com.muye.rocket.entity.exc_wallet.ExcAddress;
import com.muye.rocket.mvp.exc_wallet.contract.ExcAddressInoContract;
import com.muye.rocket.mvp.exc_wallet.presenter.ExcAddressInoPresenter;
import com.muye.rocket.tools.Validator;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by ll on 2018/3/7.
 */

public class ScanActivity extends BaseActivity implements QRCodeView.Delegate, EasyPermissions.PermissionCallbacks, ExcAddressInoContract.View {

    private static final int RC_CAMERA_PERM = 123;
    public static final String BUNDLE_KEY_RESULT = "bundle_key_result";
    private static final int ACTION_TYPE_PAY = 1;
    private static final int ACTION_TYPE_FOR_RESULT = 2;

    @BindView(R.id.zx_scan)
    QRCodeView mQRCodeView;
    int mAction;
    ExcAddressInoContract.Presenter mPresenter;

    public static void openScanQRForResult(BaseActivity activity, int requestCode) {
        Intent intent = new Intent(activity, ScanActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("action", ACTION_TYPE_FOR_RESULT);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void openScanQRForResult(BaseFragment fragment, int requestCode) {
        Intent intent = new Intent(fragment.getContext(), ScanActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("action", ACTION_TYPE_FOR_RESULT);
        intent.putExtras(bundle);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void openScanQRForAction(Context context, int action) {
        Intent intent = new Intent(context, ScanActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("action", action);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    public static void openScan(Context context) {
        Intent intent = new Intent(context, ScanActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.exc_wal_activity_scan;
    }

    @Override
    protected int getToolbarColor() {
        return Color.TRANSPARENT;
    }

    @Override
    protected int getStatusBarColor() {
        return Color.TRANSPARENT;
    }

    @Override
    protected int getNavigationIcon() {
        return R.drawable.rc_back_icon_white;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
//        StatusBarTool.setTranslucentForImageView(this, mToolbar);
        mImmersionBar.statusBarDarkFont(false, 0.2f).init();
        setNavigationRight(getString(R.string.exc_wal_import_from_album), getResources().getColor(R.color.white), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(ScanActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .selectionMode(PictureConfig.SINGLE)
                        .compress(true)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
            }
        });
        handleIntent();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mAction = bundle.getInt("action", -1);
        }
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        mQRCodeView.startSpot();
        if (TextUtils.isEmpty(result)) return;
        vibrate();
        if (!TextUtils.isEmpty(result) && Validator.isUrl(result)) {
            Intent intent;
            if (result.startsWith("https://") || result.startsWith("http://")) {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result));
            } else {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + result));
            }
            startActivity(intent);
            finish();
        } else if (mAction == ACTION_TYPE_FOR_RESULT) {
            setResult(result);
            finish();
        } else if (Validator.isCoinAddress(result)) {
            if (mPresenter == null) {
                mPresenter = new ExcAddressInoPresenter(this);
            }
            mPresenter.fetchAddressInfo(result);
//            ExcWithdrawActivity.openWithdrawActivity(this, result);
//            finish();
        } else {
            if (!TextUtils.isEmpty(result)) {
                showToast(result);
            }
        }


    }

    private void setResult(String result) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_RESULT, result);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        showToast(getString(R.string.exc_wal_error_opening_the_camera));
    }


    @Override
    protected void onStart() {
        super.onStart();
        openScan();
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void bindAddressInfo(AddressInfo addressInfo) {
        if (addressInfo != null && !TextUtils.isEmpty(addressInfo.getAddress_id()) && !TextUtils.isEmpty(addressInfo.getCoin_id())) {
            ExcAddress address = new ExcAddress();
            address.setAppLogo(addressInfo.getApp_logo());
            address.setCoinName(addressInfo.getCoin_name());
            address.setFcoinid(addressInfo.getCoin_id());
            address.setFadderess(addressInfo.getFadderess());
            address.setFid(addressInfo.getAddress_id());
            ExcWithdrawActivity.openExcWithdrawActivity(this, addressInfo.getCoin_id(), address);
            finish();
        }
    }


    static class DecodeQRCodeAsyncTask extends AsyncTask<Void, Void, String> {

        WeakReference<ScanActivity> weakReference;
        String picturePath;

        public DecodeQRCodeAsyncTask(ScanActivity scanActivity, String picturePath) {
            this.weakReference = new WeakReference<ScanActivity>(scanActivity);
            this.picturePath = picturePath.toString();
        }

        @Override
        protected String doInBackground(Void... voids) {
            return QRCodeDecoder.syncDecodeQRCode(picturePath);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.isEmpty(s) && weakReference.get() != null) {
                weakReference.get().showToast(weakReference.get().getString(R.string.exc_wal_no_qr_code_found));
            } else {
                weakReference.get().onScanQRCodeSuccess(s);
            }
        }
    }

    @AfterPermissionGranted(RC_CAMERA_PERM)
    private void openScan() {
        if (hasCameraPermission()) {
            mQRCodeView.setDelegate(this);
            mQRCodeView.startCamera();
            mQRCodeView.showScanRect();
            mQRCodeView.startSpot();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.request_camera_read_write_permissions),
                    RC_CAMERA_PERM, Manifest.permission.CAMERA);
        }
    }

    private boolean hasCameraPermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        openScan();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //当从软件设置界面，返回当前程序时候
            case AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE:
                openScan();
                break;
            case PictureConfig.CHOOSE_REQUEST:
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                if (selectList != null && selectList.size() > 0) {
                    LocalMedia media = selectList.get(0);
                    if (media != null) {
                        String path = "";
                        if (media.isCompressed()) {
                            path = media.getCompressPath();
                        } else if (media.isCut()) {
                            path = media.getCutPath();
                        } else {
                            path = media.getPath();
                        }
                        new DecodeQRCodeAsyncTask(this, path).execute();
                    }
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 把执行结果的操作给EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
