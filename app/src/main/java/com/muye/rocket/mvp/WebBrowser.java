package com.muye.rocket.mvp;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hjq.toast.ToastUtils;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import me.jingbin.progress.WebProgress;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class WebBrowser extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.progress)
    WebProgress mProgress;

    private static final int RC_EXTERNAL_STORAGE_PERM = 1025;
    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private ValueCallback<Uri[]> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private final static int CODE_FOR_WRITE_PERMISSION = 2;
    public static final String TAG = "WebBrowser";

    private String mUrl = "";
    private String mTitle = "";


    public static void openWebBrowser(Context context, String url, String title) {
        Intent intent = new Intent(context, WebBrowser.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("title", title);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        if (TextUtils.isEmpty(mTitle)) {
            mToolBarContainer.setVisibility(View.VISIBLE);
        } else {
            mToolBarContainer.setVisibility(View.VISIBLE);
            setNavigationCenter(mTitle);
        }
        webView.setBackgroundColor(getResources().getColor(R.color.colorPageBackground));
        setNavigationRight(getString(R.string.exc_wal_save_screenshot), getResources().getColor(R.color.black), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBitmapFromView();
            }
        });

        mProgress.setColor("#D81B60");             // 设置颜色
        mProgress.setColor("#00D81B60","#D81B60"); // 设置渐变色

        WebSettings s = webView.getSettings();
        s.setBuiltInZoomControls(true); //
        s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        s.setUseWideViewPort(true); //
        s.setLoadWithOverviewMode(true);//
        s.setSavePassword(true);
        s.setSaveFormData(true);
        s.setJavaScriptEnabled(true); //
        s.setBuiltInZoomControls(false);
        //暴露JS接口
        JavaScriptInterface javaScriptInterface = new JavaScriptInterface();
        webView.addJavascriptInterface(javaScriptInterface, "JSInterface");
        //enable navigator.geolocation
        s.setGeolocationEnabled(true);
        s.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");
        s.setAllowContentAccess(true);
        s.setAllowFileAccess(true);
        s.setAllowFileAccessFromFileURLs(true);
        // enable Web Storage: localStorage, sessionStorage
        s.setDomStorageEnabled(true); //设置允许WebStorage，否则可能会出现点击失效
        webView.requestFocus();//
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        /*WebSettings webSettings = webView.getSettings();
        webView.getSettings().setSupportZoom(true);
        //webSettings.setBuiltInZoomControls(true);
        //webSettings.setUseWideViewPort(true);
        //webSettings.setLoadWithOverviewMode(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        //webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        //webView.requestFocus();*/

        //需要设置webView的一些属性
        webView.loadUrl(mUrl);
        mProgress.show();
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new MyWebViewClient()); //网页中的连接，在本客户端打开，而不是开启系统的浏览器打开
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }

    @Override
    protected void handleIntent(Bundle bundle) {
        super.handleIntent(bundle);
        if (bundle != null) {
            mUrl = bundle.getString("url", "'");
            mTitle = bundle.getString("title", "");
        }
    }

    private String getSignParam() {
        StringBuilder param = new StringBuilder();
//        Map<String, String> map = SignInterceptor.createSignParam();
//        if (map != null && map.size() > 0) {
//            for (Map.Entry<String, String> entry : map.entrySet()) {
//                if (!TextUtils.isEmpty(entry.getKey()) && !TextUtils.isEmpty(entry.getValue())) {
//                    param.append("&");
//                    param.append(entry.getKey());
//                    param.append("=");
//                    param.append(entry.getValue());
//                }
//            }
//        }

        return param.toString();
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_web_browser;
    }

    //点击返回键，是网页回退，而不是界面退出
    @Override
    protected void onClickBack() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onClickBack();
        }
    }

    public class JavaScriptInterface {
        JavaScriptInterface() {
        }

        @JavascriptInterface
        public String getToken() {
            return MMKVTools.getToken();
        }

        @JavascriptInterface
        public void tokeError() {
            onError(101, "登录失效");
        }
    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                //处理intent协议
                if (url.startsWith("intent://")) {
                    Intent intent;
                    try {
                        intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        intent.addCategory("android.intent.category.BROWSABLE");
                        intent.setComponent(null);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                            intent.setSelector(null);
                        }
                        List<ResolveInfo> resolves = getPackageManager().queryIntentActivities(intent, 0);
                        if (resolves.size() > 0) {
                            startActivityIfNeeded(intent, -1);
                        }
                        return true;
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                // 处理自定义scheme协议
                if (!url.startsWith("http")) {
                    try {
                        // 以下固定写法
                        final Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(url));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                    } catch (Exception e) {
                        // 防止没有安装的情况
                        e.printStackTrace();
                        ToastUtils.show("您所打开的第三方App未安装！");
                    }
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//            super.onReceivedSslError(view, handler, error);
            handler.proceed();
        }

        /**
         * 当页面加载完成的时候回调
         *
         * @param view
         * @param url
         */
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d(TAG, "onPageFinished url=" + url);
//            animationDrawable.stop();
            mProgress.hide();
        }

    }

    class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            mProgress.setWebProgress(newProgress);
        }

        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            Log.d("onShowFileChooser", "onShowFileChooser");
            mUploadMessage = filePathCallback;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            return true; //一定要返回true, false得不到最终的结果值
        }

    }


    //根据URI获取图片路径
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    //是否是外部存储的uri
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    //是否是下载的URI
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    //是否是Media的URI
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    //是否在拍照的URI
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;

            if (intent == null || resultCode != RESULT_OK) {
                mUploadMessage.onReceiveValue(null);
                return;
            }
            Uri originalUri = intent.getData();
            String path = null;
            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(this, originalUri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(originalUri)) {
                    final String docId = DocumentsContract.getDocumentId(originalUri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        path = Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                }
                // DownloadsProvider
                else if (isDownloadsDocument(originalUri)) {
                    final String id = DocumentsContract.getDocumentId(originalUri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    path = getDataColumn(this, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(originalUri)) {
                    final String docId = DocumentsContract.getDocumentId(originalUri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};
                    path = getDataColumn(this, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(originalUri.getScheme())) {
                // Return the remote address
                if (isGooglePhotosUri(originalUri))
                    path = originalUri.getLastPathSegment();
                path = getDataColumn(this, originalUri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(originalUri.getScheme())) {
                path = originalUri.getPath();
            }
            if (path != null) {
                Uri mUri = Uri.fromFile(new File(path));
                Log.d("mUri", mUri.toString());
                mUploadMessage.onReceiveValue(new Uri[]{mUri});
            }
        }if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            if (hasStoragePermission()) {
                saveBitmapFromView();
            }
        }
    }

    @AfterPermissionGranted(RC_EXTERNAL_STORAGE_PERM)
    public void saveBitmapFromView() {
        if (!hasStoragePermission()) {
            EasyPermissions.requestPermissions(this, getString(R.string.request_read_write_permissions),
                    RC_EXTERNAL_STORAGE_PERM, PERMISSIONS);
        } else {
            saveImageView();
        }
    }

    private void saveImageView() {
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            saveBitmap(captureWebViewLollipop(), format.format(new Date(Calendar.getInstance().getTimeInMillis())) + ".JPEG");
        } else {
            saveBitmap(captureWebViewKitKat(), format.format(new Date(Calendar.getInstance().getTimeInMillis())) + ".JPEG");
        }
    }

    private Bitmap captureWebViewLollipop() {
        float scale = webView.getScale();
        int width = webView.getWidth();
        int height = (int) (webView.getContentHeight() * scale + 0.5);
        if (width > 0 && height > 0) {
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            webView.draw(canvas);
            return bitmap;
        }else {
            showToast("保存失败");
            return null;
        }
    }

    private Bitmap captureWebViewKitKat() {
        Picture picture = webView.capturePicture();
        int width = picture.getWidth();
        int height = picture.getHeight();
        if (width > 0 && height > 0) {
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            picture.draw(canvas);
            return bitmap;
        }
        return null;
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
            if (bitmap!= null&&bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
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
    public void finish() {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        super.finish();
    }
}
