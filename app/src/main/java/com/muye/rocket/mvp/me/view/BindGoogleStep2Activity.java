package com.muye.rocket.mvp.me.view;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.R;
import com.muye.rocket.entity.GoogleDevice;
import com.muye.rocket.event.BindGoogleSuccessEvent;
import com.muye.rocket.mvp.me.contract.BindGoogleContract;
import com.muye.rocket.mvp.me.presenter.BindGooglePresenter;
import com.muye.rocket.widget.dialog.MessageDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class BindGoogleStep2Activity extends BaseActivity implements BindGoogleContract.View {

    private static final int RC_EXTERNAL_STORAGE_PERM = 1;
    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @BindView(R.id.qr_image_view)
    ImageView qrImageView;
    @BindView(R.id.key_text_view)
    TextView keyTextView;
    @BindView(R.id.submit_button)
    TextView submitButton;
    @BindView(R.id.linear_view)
    LinearLayout scrollView;
    ClipboardManager mClipboardManager;
    BindGoogleContract.Presenter mPresenter;

    MessageDialog hintDialog;
    Disposable disposable;

    String mKey;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_bind_google_step_2;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.google_authenticator));
        EventBus.getDefault().register(this);
        mClipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        setNavigationRight(getString(R.string.save_screenshot), getResources().getColor(R.color.color292A49), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBitMap();
            }
        });

        mPresenter = new BindGooglePresenter(this);
        mPresenter.fetchGoogleDevice();

        showMessage();

        //提示
        hintMess();
    }

    @AfterPermissionGranted(RC_EXTERNAL_STORAGE_PERM)
    private void saveBitMap() {
        if (!hasStoragePermission()) {
            EasyPermissions.requestPermissions(this, getString(R.string.request_read_write_permissions),
                    RC_EXTERNAL_STORAGE_PERM, PERMISSIONS);
        } else {
            saveImageView();
        }
    }

    private boolean hasStoragePermission() {
        return EasyPermissions.hasPermissions(this, PERMISSIONS);
    }

    private void saveImageView() {
        scrollView.setDrawingCacheEnabled(true);
        Bitmap bmp = Bitmap.createBitmap(scrollView.getDrawingCache());
        scrollView.setDrawingCacheEnabled(false);
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        saveBitmap(bmp, format.format(new Date(Calendar.getInstance().getTimeInMillis())) + ".JPEG");
    }

    private void showMessage() {
        if (hintDialog == null){
            hintDialog = new MessageDialog(this)
                    .setCancelText(getString(R.string.cancel))
                    .setOkText(getString(R.string.ok))
                    .setMessage(getString(R.string.hintCodeMess))
                    .setOnOkButtonClickListener((dialog) -> {
                       dialog.dismiss();
                    });
        }
        if (!hintDialog.isShowing()){
            hintDialog.show();
        }
    }

    @OnClick({R.id.copy_button, R.id.submit_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.copy_button:
                // 创建普通字符型ClipData
                ClipData clipData = ClipData.newPlainText("ufo_google_key", keyTextView.getText().toString());
                // 将ClipData内容放到系统剪贴板里。
                mClipboardManager.setPrimaryClip(clipData);
                showToast(getString(R.string.exc_wal_copy_success));
                break;
            case R.id.submit_button:
                BindGoogleStep3Activity.openBindGoogleStep3Activity(this, mKey);
                break;
        }
    }

    @Override
    public void bindGoogleDevice(GoogleDevice device) {
        if (device != null) {
            mKey = device.getTotpKey();
            keyTextView.setText(device.getTotpKey());
            createAddressImage(device.getQecode());
        } else {
            mKey = "";
            createAddressImage("");
            keyTextView.setText("");
        }
        submitButton.setEnabled(device != null);
    }

    @Override
    public void onBindGoogleSuccess() {

    }

    private void createAddressImage(String address) {
        if (!TextUtils.isEmpty(address)) {
            Observable.just(address).map(new Function<String, Bitmap>() {
                @Override
                public Bitmap apply(String s) {
                    return QRCodeEncoder.syncEncodeQRCode(s, 700, Color.BLACK, getResources().getColor(R.color.transparent), null);
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Bitmap>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Bitmap bitmap) {
                    qrImageView.setImageBitmap(bitmap);
                }

                @Override
                public void onError(Throwable e) {
                    qrImageView.setImageBitmap(null);
                }

                @Override
                public void onComplete() {

                }
            });
        } else {
            qrImageView.setImageBitmap(null);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBindGoogleSuccess(BindGoogleSuccessEvent event) {
        finish();
    }


    private void hintMess() {
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (aLong == 3){
                            disposable.dispose();
                            if (null != hintDialog&&hintDialog.isShowing()){
                                hintDialog.dismiss();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (disposable != null)
                            disposable.dispose();
                    }

                    @Override
                    public void onComplete() {
                        if (null != hintDialog&&hintDialog.isShowing()){
                            hintDialog.dismiss();
                        }

                    }
                });
    }

    /*
     * 保存文件，文件名为当前日期
     */
    public void saveBitmap(Bitmap bitmap, String bitName) {
        String fileName;
        File file;
        if (Build.BRAND.equals("Xiaomi")) { // 小米手机
            fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + bitName;
        } else {  // Meizu 、Oppo
            fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/" + bitName;
        }
        file = new File(fileName);

        if (file.exists()) {
            file.delete();
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
                out.flush();
                out.close();
                // 插入图库
                MediaStore.Images.Media.insertImage(this.getContentResolver(), file.getAbsolutePath(), bitName, null);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }
        // 发送广播，通知刷新图库的显示
        this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));
        showToast(getString(R.string.save_screenshot_successfully));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            if (hasStoragePermission()) {
                saveBitMap();
            }
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (mPresenter != null) {
            mPresenter.dropView();
        }
        if (disposable != null)
            disposable.dispose();
        super.onDestroy();
    }
}
