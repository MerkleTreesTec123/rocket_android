package com.muye.rocket.mvp.exc_wallet.view;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.entity.BindWalletBean;
import com.muye.rocket.entity.exc_wallet.ExcAssetsEntity;
import com.muye.rocket.entity.exc_wallet.ExcRechargeAddress;
import com.muye.rocket.mvp.exc_wallet.contract.ExcRechargeContract;
import com.muye.rocket.mvp.exc_wallet.presenter.ExcRechargePresenter;
import com.muye.rocket.widget.dialog.BigImgDialog;
import com.muye.rocket.widget.dialog.CoinPicker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

/*
* 充币
* */
public class ExcRechargeActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks,
        CoinPicker.OnCoinSelectedListener, ExcRechargeContract.View {
    private static final int RC_EXTERNAL_STORAGE_PERM = 1;
    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @BindView(R.id.title_text_view)
    TextView titleTextView;
    @BindView(R.id.qr_image_view)
    ImageView qrImageView;
    @BindView(R.id.address_text_view)
    TextView addressTextView;
    @BindView(R.id.hint_text_view)
    TextView hintTextView;
    @BindView(R.id.content_container)
    LinearLayout contentContainer;
    @BindView(R.id.tag_title_text_view)
    TextView tagTitleTextView;
    @BindView(R.id.tag_text_view)
    TextView tagTextView;

    //剪切板管理工具类
    private ClipboardManager mClipboardManager;
    //剪切板Data对象
    private ClipData mClipData;
    CoinPicker mCoinPicker;
    String mCoinID;
    ExcRechargeContract.Presenter mRechargePresenter;

    public static void openExcRechargeActivity(Context context, String coinID) {
        Intent intent = new Intent(context, ExcRechargeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("coinID", coinID);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.exc_wal_activity_recharge;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.exc_wal_recharge));
        setNavigationRight(R.drawable.exc_wal_ic_record, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mCoinID)) {
                    showToast(getString(R.string.exc_wal_please_select_currency));
                    return;
                }
                ExcRechargeRecordActivity.openExcRechargeRecordActivity(ExcRechargeActivity.this, mCoinID);
            }
        });
        hintTextView.setText(String.format(getString(R.string.exc_wal_recharge_hint), "--"));
        mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        fetchCoinList();
    }

    @Override
    protected void handleIntent(Bundle bundle) {
        super.handleIntent(bundle);
        if (bundle != null) {
            mCoinID = bundle.getString("coinID", "");
        }
    }

    @OnClick({R.id.coin_container, R.id.btn_copy, R.id.tag_text_view, R.id.save_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.coin_container:
                showCoinPicker();
                break;
            case R.id.btn_copy:
                if (TextUtils.isEmpty(mCoinID)) {
                    showToast(getString(R.string.exc_wal_please_select_currency));
                    return;
                }
                //创建一个新的文本clip对象
                mClipData = ClipData.newPlainText("ufo_copy_address", addressTextView.getText().toString());
                //把clip对象放在剪贴板中
                mClipboardManager.setPrimaryClip(mClipData);
                showToast(getString(R.string.exc_wal_copy_success));
                break;
            case R.id.tag_text_view:
                if (TextUtils.isEmpty(mCoinID)) {
                    showToast(getString(R.string.exc_wal_please_select_currency));
                    return;
                }
                //创建一个新的文本clip对象
                mClipData = ClipData.newPlainText("ufo_copy_label", tagTextView.getText().toString());
                //把clip对象放在剪贴板中
                mClipboardManager.setPrimaryClip(mClipData);
                showToast(getString(R.string.exc_wal_copy_success));
                break;
            case R.id.save_button:
                if (TextUtils.isEmpty(mCoinID)) {
                    showToast(getString(R.string.exc_wal_please_select_currency));
                    return;
                }
                saveBitmapFromView();
                break;
        }
    }

    private void fetchCoinList() {
        if (mRechargePresenter == null) {
            mRechargePresenter = new ExcRechargePresenter(this);
        }
        mRechargePresenter.fetchCoinList();
    }

    private void fetchAddress() {
        if (mRechargePresenter == null) {
            mRechargePresenter = new ExcRechargePresenter(this);
        }
        mRechargePresenter.fetchAddress(mCoinID);
    }

    private boolean isShow = true;
    @Override
    public void onCoinSelected(ExcAssetsEntity.WalletBean walletBean, BindWalletBean bindWalletBean) {
        bindCoin(walletBean);
        if (isShow)
            showChargeCoin();
        isShow = false;
    }

    private void showChargeCoin(){
        BigImgDialog imgDialog = new BigImgDialog(this);
        imgDialog.setImg(5);
        imgDialog.show();
    }

    @Override
    public void bindRechargeAddress(ExcRechargeAddress address) {
        if (address != null && address.getRechargeAddress() != null && !TextUtils.isEmpty(mCoinID)) {
            addressTextView.setText(address.getRechargeAddress().getFadderess());
            tagTextView.setText(address.getMemo());
            createAddressImage(address.getRechargeAddress().getFadderess());
        }
    }

    @Override
    public void bindCoinList(List<ExcAssetsEntity.WalletBean> coinList) {
        List<ExcAssetsEntity.WalletBean> walletBeanList = new ArrayList<>();
        if (coinList != null && coinList.size() > 0) {
            for (int i = 0; i < coinList.size(); i++) {
                ExcAssetsEntity.WalletBean walletBean = coinList.get(i);
                if (walletBean != null && !TextUtils.isEmpty(walletBean.getCoinId()) && walletBean.isCanDeposit()) {
                    walletBeanList.add(walletBean);
                }
            }
        }
        initCoinPicker();
        mCoinPicker.setData(walletBeanList);
        if (TextUtils.isEmpty(mCoinID)) {
            showCoinPicker();
        } else {
            if (walletBeanList.size() > 0) {
                for (ExcAssetsEntity.WalletBean walletBean : walletBeanList) {
                    if (walletBean != null && mCoinID.equals(walletBean.getCoinId())) {
                        bindCoin(walletBean);
                        break;
                    }
                }
            }
        }
    }


    private void bindCoin(ExcAssetsEntity.WalletBean walletBean) {
        String coinName = "";
        if (walletBean != null) {
            mCoinID = walletBean.getCoinId();
            coinName = walletBean.getShortName();
        }

        //修改选择只允许选择USDT
        if (!coinName.equals("USDT")){
            showToast(getString(R.string.watting_for_));
            return;
        }

        titleTextView.setText(!TextUtils.isEmpty(coinName) ? coinName : "--");
        hintTextView.setText(String.format(getString(R.string.exc_wal_recharge_hint), !TextUtils.isEmpty(coinName) ? coinName : "--"));

        tagTextView.setText("");
        tagTitleTextView.setVisibility("EOS".equals(coinName) ? View.VISIBLE : View.GONE);
        tagTextView.setVisibility("EOS".equals(coinName) ? View.VISIBLE : View.GONE);

        addressTextView.setText("");
        qrImageView.setImageBitmap(null);
        if (!TextUtils.isEmpty(mCoinID)) {
            fetchAddress();
        }
    }

    private void initCoinPicker() {
        if (mCoinPicker == null) {
            mCoinPicker = new CoinPicker(this);
            mCoinPicker.setOnCoinSelectedListener(this);
        }
    }

    private void showCoinPicker() {
        initCoinPicker();
        if (!mCoinPicker.isShowing()) {
            mCoinPicker.setSelectedCoin(mCoinID);
            mCoinPicker.setCanceledOnTouchOutside(!TextUtils.isEmpty(mCoinID));
            mCoinPicker.show();
        }
    }

    private void createAddressImage(String address) {
        if (!TextUtils.isEmpty(address)) {
            Observable.just(address).map(new Function<String, Bitmap>() {
                @Override
                public Bitmap apply(String s) {
                    return QRCodeEncoder.syncEncodeQRCode(s, 700);
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
        }
    }

    @AfterPermissionGranted(RC_EXTERNAL_STORAGE_PERM)
    public void saveBitmapFromView() {
        if (contentContainer == null) return;
        if (!hasStoragePermission()) {
            EasyPermissions.requestPermissions(this, getString(R.string.request_read_write_permissions),
                    RC_EXTERNAL_STORAGE_PERM, PERMISSIONS);
        } else {
            contentContainer.setDrawingCacheEnabled(true);
            Bitmap bmp = Bitmap.createBitmap(contentContainer.getDrawingCache());
            contentContainer.setDrawingCacheEnabled(false);
            DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            saveBitmap(bmp, format.format(new Date(Calendar.getInstance().getTimeInMillis())) + ".JPEG");
        }
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
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    protected void onDestroy() {
        if (mRechargePresenter != null) {
            mRechargePresenter.dropView();
        }
        super.onDestroy();
    }
}
