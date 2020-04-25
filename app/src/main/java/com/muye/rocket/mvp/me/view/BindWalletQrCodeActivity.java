package com.muye.rocket.mvp.me.view;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.muye.rocket.R;
import com.muye.rocket.api.RURLConfig;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.entity.BindWalletBean;
import com.muye.rocket.entity.CoinBean;
import com.muye.rocket.mvp.WebBrowser;
import com.muye.rocket.mvp.me.contract.BindWalletContract;
import com.muye.rocket.mvp.me.presenter.BindWalletPresenter;
import com.muye.rocket.tools.MD5Tools;
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
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
// 获取钱包地址
public class BindWalletQrCodeActivity extends BaseActivity implements BindWalletContract.View {

    private static final int RC_EXTERNAL_STORAGE_PERM = 1;
    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @BindView(R.id.rl_contain)
    RelativeLayout rlContain;
    @BindView(R.id.qr_code_bind_img)
    ImageView qrCode;
    @BindView(R.id.ll_top)
    LinearLayout ll_top;

    private BindWalletPresenter walletPresenter;

    @Override
    protected int getToolbarColor() {
        return Color.WHITE;
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.bind_wallet_qrcode_layout;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);

        setNavigationCenter(getString(R.string.bind_wallet_user));
        setNavigationRight(getString(R.string.exc_wal_save_screenshot), getResources().getColor(R.color.color0B1C39), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBitmapFromView();
            }
        });

        if (walletPresenter == null){
            walletPresenter = new BindWalletPresenter(this);
        }
        String jwt = MD5Tools.MD5(MD5Tools.MD5(MMKVTools.getUID() + "cat"));
        walletPresenter.fetchBindWallet(MMKVTools.getUID(),jwt);
    }

    @OnClick({R.id.close_img,R.id.btn_bind,R.id.binding_info})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.close_img:
                ll_top.setVisibility(View.INVISIBLE);
                break;
            case R.id.btn_bind:
                Intent intent = new Intent(this,MyBindWalletListsActivity.class);
                intent.putExtra("intent_type","1");
                startActivity(intent);
                break;
            case R.id.binding_info:
                WebBrowser.openWebBrowser(this, RURLConfig.BIND_CAT_KNOW, getString(R.string.bind_konw_cat));
                break;
        }
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
        rlContain.setDrawingCacheEnabled(true);
        Bitmap bmp = Bitmap.createBitmap(rlContain.getDrawingCache());
        rlContain.setDrawingCacheEnabled(false);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void bindWallet(String qrcode) {
        createDownloadImage(qrcode);
    }

    @Override
    public void bindCatLists(List<BindWalletBean> catLists) {

    }

    @Override
    public void inquireRemaining(List<CoinBean> remain, BindWalletBean walletBean) {

    }

    @Override
    public void reflectStatus() {

    }

    private void createDownloadImage(String address) {
        if (!TextUtils.isEmpty(address)) {
            Observable.just(address).map(new Function<String, Bitmap>() {
                @Override
                public Bitmap apply(String s) {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_qr_frame);
                    int bitmapWidth = bitmap.getWidth();
                    int qrSize = (int) (167f / 207f * bitmapWidth) + 8;
                    Bitmap qrBitmap = QRCodeEncoder.syncEncodeQRCode(s, qrSize, Color.BLACK,
                            getResources().getColor(R.color.white), null);

                    Bitmap resultBitmap = Bitmap.createBitmap(bitmapWidth, bitmapWidth, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(resultBitmap);
                    Paint qrPaint = new Paint();
                    qrPaint.setAntiAlias(true);

                    Paint paint = new Paint();
                    paint.setAntiAlias(true);
                    canvas.drawBitmap(bitmap, 0, 0, paint);
                    canvas.drawBitmap(qrBitmap, (bitmapWidth - qrSize) / 2f, (bitmapWidth - qrSize) / 2f, qrPaint);
                    return resultBitmap;

                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Bitmap>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Bitmap bitmap) {
                    qrCode.setImageBitmap(bitmap);
                }

                @Override
                public void onError(Throwable e) {
                    qrCode.setImageBitmap(null);
                }

                @Override
                public void onComplete() {

                }
            });
        }
    }
}
