package com.muye.rocket.reveive;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import com.muye.rocket.R;
import com.muye.rocket.mvp.ieo.view.WebViewInterFace;
import com.tencent.mmkv.MMKV;

/**
 * 推送地址，打开网页
 */
public class PushInfoAddressActivity extends Activity {

    private WebView webView;
    private LinearLayout linearLayout;
    private WebSettings webSettings;
    ConnectivityManager connectivityManager;
    private String selectUrl = "";
    private WebViewInterFace webViewInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.push_info_address_activity);
        linearLayout = findViewById(R.id.linearLayout);
        // 动态创建WebView 控件，设置宽高为MATCH_PARENT，添加webview到父布局的linearLayout中，
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        connectivityManager = (ConnectivityManager)this.getSystemService(CONNECTIVITY_SERVICE);
        webView = new WebView(this);
        webView.setLayoutParams(layoutParams);
        // 设置WebView 背景全透明，然后根据网页
        webView.setBackgroundColor(Color.alpha(0));
        linearLayout.addView(webView);
        String webUrl = MMKV.defaultMMKV().decodeString("web_load_url", "");
        // WebView 的配置
        initSetWebView(webUrl);
        setWebViewLoad();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null){
            ViewParent parent = webView.getParent();
            if(parent != null){
                ((ViewGroup)parent).removeView(webView);
            }
            webView.stopLoading();
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearHistory();
            webView.clearView();
            webView.removeAllViews();
            webView.destroy();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("JavascriptInterface")
    private void initSetWebView(String webUrl) {
        webSettings = webView.getSettings();
        webView.setWebContentsDebuggingEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_INSET );
        // 加载初始化登录的url页面
        webView.loadUrl(webUrl);
        webView.getSettings().setDomStorageEnabled(true); //设置DOM Storage缓存
        webSettings.setJavaScriptEnabled(true); // 支持js脚本
        String ua = webSettings.getUserAgentString();
        // 设置在本WebView中加载网页时添加网页的UserAgent的标识，用于在js中判断处理
        webSettings.setUserAgentString(ua + ";rocketAndroid");
        webSettings.setAllowContentAccess(true); // 允许在WebView中访问内容URL
        webSettings.setNeedInitialFocus(false); // 调用时是否需要设置节点以获得焦点
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setAllowFileAccess(true);  // 启用文件访问
        webSettings.setAllowFileAccessFromFileURLs(false);  // 允许js访问其他url的内容
        webSettings.setAllowUniversalAccessFromFileURLs(false);
        webSettings.setSavePassword(false);
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        webView.requestFocusFromTouch();
        webView.requestFocus();
        webView.setFocusable(true);
        webView.setLongClickable(true);
        //是否使用内置的缩放机制
        // 缩放机制包括屏幕上的缩放控件（浮于WebView内容之上）和缩放手势的运用。通过setDisplayZoomControls(boolean)可以控制是否显示这些控件
        webSettings.setBuiltInZoomControls(false);
        webSettings.setUseWideViewPort(true); // 是否支持HTML的“viewport”标签或者使用wide viewport
        webSettings.setLoadWithOverviewMode(true); // 是否允许WebView度超出以概览的方式载入页面
        webSettings.setDatabaseEnabled(true);  // 设置是否启用数据库存储API
        webSettings.setGeolocationEnabled(true);  // 设置是否启用地理位置
        webView.setWebChromeClient(new android.webkit.WebChromeClient());  // 处理解析，渲染网页等
        // 创建和js交互的接口类
        webViewInterface = new WebViewInterFace(this,this);
        // 设置js和Android调用的绑定，js端通过  window.android. 加上Android端的接口类中方法即可调用
        webView.addJavascriptInterface(webViewInterface,"rocket_android");
        //webViewInterFace.setActionListener(this);
        // 加快HTML网页加载完成速度
        if (Build.VERSION.SDK_INT >= 19) {
            webSettings.setLoadsImagesAutomatically(true);
        } else {
            webSettings.setLoadsImagesAutomatically(false);
        }
    }


    private void setWebViewLoad() {
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.equals("javscript:;")){
                    selectUrl = "javscript";
                }else
                    view.loadUrl(url);
                return true;
            }
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }
        });
    }

}
