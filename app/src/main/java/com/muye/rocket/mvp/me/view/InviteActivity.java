package com.muye.rocket.mvp.me.view;

import android.Manifest;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ifenduo.common.tool.DimensionTool;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.entity.InviteRankEntity;
import com.muye.rocket.mvp.me.contract.InviteRankContract;
import com.muye.rocket.mvp.me.presenter.InviteRankPresenter;
import com.muye.rocket.tools.GlideLoadUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/*
* 邀请好友（专属邀请海报）
* */
public class InviteActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks, InviteRankContract.View {
    private static final int RC_EXTERNAL_STORAGE_PERM = 1;
    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    String mInviteCode;
    String mQRImageURL;
    @BindView(R.id.qr_image_view)
    ImageView qrImageView;
    @BindView(R.id.content_container)
    LinearLayout contentContainer;

    int mDp2px4;
    InviteRankContract.Presenter mPresenter;

    @Override
    protected int getToolbarColor() {
        return Color.TRANSPARENT;
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_invite;
    }

    @Override
    protected int getNavigationIcon() {
        return R.drawable.rc_back_icon_white;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
//        StatusBarTool.setTranslucentForImageView(this, mToolBarContainer);
        mImmersionBar.statusBarDarkFont(false, 0.2f).init();
        setNavigationRight(getString(R.string.exc_wal_save_screenshot), getResources().getColor(R.color.white), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBitmapFromView();
            }
        });
        mPresenter = new InviteRankPresenter(this);
        mPresenter.fetchInviteRank();
    }

    @Override
    protected void onReceiveRequestIntent(Intent intent) {
        super.onReceiveRequestIntent(intent);
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mInviteCode = bundle.getString("inviteCode", "");
            mQRImageURL = bundle.getString("qrURL", "");
        }
        mDp2px4 = DimensionTool.dp2px(this, 4);

    }

    @AfterPermissionGranted(RC_EXTERNAL_STORAGE_PERM)
    public void saveBitmapFromView() {
        if (!hasStoragePermission()) {
            EasyPermissions.requestPermissions(this, "需要读写权限",
                    RC_EXTERNAL_STORAGE_PERM, PERMISSIONS);
        } else {
            saveImageView();
        }
    }

    private void saveImageView() {
        contentContainer.setDrawingCacheEnabled(true);
        Bitmap bmp = Bitmap.createBitmap(contentContainer.getDrawingCache());
        contentContainer.setDrawingCacheEnabled(false);
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        saveBitmap(bmp, format.format(new Date(Calendar.getInstance().getTimeInMillis())) + ".JPEG");
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
        showToast(getString(R.string.save_success));
    }

    private boolean hasStoragePermission() {
        return EasyPermissions.hasPermissions(this, PERMISSIONS);
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
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            if (hasStoragePermission()) {
                saveBitmapFromView();
            }
        }
    }

    @Override
    public void bindInviteRank(InviteRankEntity inviteRankEntity) {
        if (inviteRankEntity != null) {
            mInviteCode = inviteRankEntity.getYaoqingma();
            mQRImageURL = inviteRankEntity.getQr_code();
        } else {
            mInviteCode = "";
            mQRImageURL = "";
        }
        GlideLoadUtils.getInstance().glideLoad(this, mQRImageURL, qrImageView);
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.dropView();
        }
        super.onDestroy();
    }
}
