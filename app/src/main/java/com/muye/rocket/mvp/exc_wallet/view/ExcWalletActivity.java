package com.muye.rocket.mvp.exc_wallet.view;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.ifenduo.common.tool.DimensionTool;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.R;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ExcWalletActivity extends BaseActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {
    private static final int RC_SCAN_PERM = 123;
    private static final String[] CAMERA_AND_STORAGE =
            {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    EasyPopup mActionMenu;
    int mDp2px16;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.exc_wal_activity_wallet;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.exc_wal_assets));
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container, ExcWalletFragment.newInstance()).commit();
        setNavigationRight(R.drawable.exc_wal_ic_more_point, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //暂未开放
//                if (MMKVTools.isOpenTrade()) {

                    showMenu();
//                }else {
//                    showToast(getString(R.string.waiting));
//                }
            }
        });
        mDp2px16 = DimensionTool.dp2px(this, 16);
    }

    private void showMenu() {
        if (mActionMenu == null) {
            mActionMenu = EasyPopup.create(this)
                    .setContentView(R.layout.exc_wallet_menu_action)
                    .setAnchorView(mNavigationRightIcon)
                    .setBackgroundDimEnable(true)
                    .apply();

            mActionMenu.getContentView().findViewById(R.id.scan_button).setOnClickListener(this);
            mActionMenu.getContentView().findViewById(R.id.qr_button).setOnClickListener(this);
            mActionMenu.getContentView().findViewById(R.id.address_button).setOnClickListener(this);
        }
        if (!mActionMenu.isShowing()) {
            mActionMenu.showAtAnchorView(mToolBarContainer, YGravity.BELOW, XGravity.ALIGN_RIGHT, -mDp2px16, 0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scan_button:
                if (mActionMenu != null) {
                    mActionMenu.dismiss();
                }
                openScanPage();
                break;
            case R.id.qr_button:
                if (mActionMenu != null) {
                    mActionMenu.dismiss();
                }
                ExcRechargeActivity.openExcRechargeActivity(this, "");
                break;
            case R.id.address_button:
                if (mActionMenu != null) {
                    mActionMenu.dismiss();
                }
                ExcAddressBookActivity.openExcAddressBookActivity(this, "");
                break;
        }
    }

    @AfterPermissionGranted(RC_SCAN_PERM)
    private void openScanPage() {
        if (hasCameraStoragePermission()) {
            ScanActivity.openScan(this);
        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(this, getString(R.string.request_camera_read_write_permissions),
                    RC_SCAN_PERM, CAMERA_AND_STORAGE);
        }
    }

    private boolean hasCameraStoragePermission() {
        return EasyPermissions.hasPermissions(this, CAMERA_AND_STORAGE);
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
}
