package com.muye.rocket.mvp.ieo.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ifenduo.lib_base.tools.MMKVTools;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseFragment;
import com.muye.rocket.entity.ieo.IEOHomeEntity;
import com.muye.rocket.entity.ieo.IEOProgress;
import com.muye.rocket.entity.ieo.IEOReleaseEntity;
import com.muye.rocket.mvp.ieo.contract.IEOHomeContract;
import com.muye.rocket.mvp.ieo.presenter.IEOHomePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class SpacePlanFragment extends BaseFragment implements IEOHomeContract.View {
    private static final String TAG = "SpacePlanFragment";
    Unbinder unbinder;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.progress)
    ProgressBar progress;
    Unbinder unbinder1;
    private WebView webView;
    private WebSettings webSettings;
    ConnectivityManager connectivityManager;
    private String selectUrl = "";
    private IEOHomePresenter palnPresenter;
    private WebViewInterFace webViewInterFace;

    public static SpacePlanFragment newInstance() {
        Bundle args = new Bundle();
        SpacePlanFragment fragment = new SpacePlanFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getRootViewLayout() {
        return R.layout.fragment_web_new;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            mImmersionBar.statusBarDarkFont(true, 0.2f).init();
        }
    }

    @Override
    protected void initView(View parentView) {
        unbinder = ButterKnife.bind(this, parentView);
        if (palnPresenter == null) palnPresenter = new IEOHomePresenter(this);
        palnPresenter.fetchShowUrl();
        // 动态创建WebView 控件，设置宽高为MATCH_PARENT，添加webview到父布局的linearLayout中，
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        connectivityManager = (ConnectivityManager)getContext().getSystemService(CONNECTIVITY_SERVICE);

        webView = new WebView(getContext());
        webView.setLayoutParams(layoutParams);
        linearLayout.addView(webView);

        // WebView 的配置
        initSetWebView();
        // 监听webview的加载状态
        setWebViewLoad();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("JavascriptInterface")
    private void initSetWebView() {
        webSettings = webView.getSettings();
        webView.setWebContentsDebuggingEnabled(true);
        webSettings.setLayoutAlgorithm(android.webkit.WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_INSET );
        // 加载初始化登录的url页面
//        webView.loadUrl(webUrl);
//        webView.getSettings().setDomStorageEnabled(true); //设置DOM Storage缓存
        webSettings.setJavaScriptEnabled(true); // 支持js脚本
//        webView.getSettings().setAppCacheMaxSize(1024*1024*8);    // ----
//        String appCachePath = getContext().getApplicationContext().getCacheDir().getAbsolutePath();//  ---
//        webView.getSettings().setAppCachePath(appCachePath);  // ---
        String ua = webSettings.getUserAgentString();
        // 设置在本WebView中加载网页时添加网页的UserAgent的标识，用于在js中判断处理
        webSettings.setUserAgentString(ua + ";catApp");
        webSettings.setAllowContentAccess(true); // 允许在WebView中访问内容URL
        webSettings.setNeedInitialFocus(false); // 调用时是否需要设置节点以获得焦点
//        webSettings.setCacheMode(android.webkit.WebSettings.LOAD_NO_CACHE);  // 设置缓存
        webSettings.setPluginState(android.webkit.WebSettings.PluginState.ON);
        webSettings.setAllowFileAccess(true);  // 启用文件访问
        webSettings.setAllowFileAccessFromFileURLs(false);  // 允许js访问其他url的内容
        webSettings.setAllowUniversalAccessFromFileURLs(false);
        webSettings.setSavePassword(false);
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        webView.requestFocusFromTouch();
        webView.requestFocus();
        webView.setFocusable(true);
        webView.setLongClickable(true);
        webSettings.setAppCacheEnabled(false); // 应用缓存API是否可用  结合setAppCachePath(String)使用
        //是否使用内置的缩放机制
        // 缩放机制包括屏幕上的缩放控件（浮于WebView内容之上）和缩放手势的运用。通过setDisplayZoomControls(boolean)可以控制是否显示这些控件
        webSettings.setBuiltInZoomControls(false);
        webSettings.setUseWideViewPort(true); // 是否支持HTML的“viewport”标签或者使用wide viewport
        webSettings.setLoadWithOverviewMode(true); // 是否允许WebView度超出以概览的方式载入页面
        webSettings.setDatabaseEnabled(true);  // 设置是否启用数据库存储API
        webSettings.setGeolocationEnabled(true);  // 设置是否启用地理位置
        webView.setWebChromeClient(new android.webkit.WebChromeClient());  // 处理解析，渲染网页等
        // 创建和js交互的接口类
        webViewInterFace = new WebViewInterFace(getContext(),getActivity());
        // 设置js和Android调用的绑定，js端通过  window.android. 加上Android端的接口类中方法即可调用
        webView.addJavascriptInterface(webViewInterFace,"rocket_android");
        webViewChromeClient();
        //webViewInterFace.setActionListener(this);
        // 加快HTML网页加载完成速度
        if (Build.VERSION.SDK_INT >= 19) {
            webSettings.setLoadsImagesAutomatically(true);
        } else {
            webSettings.setLoadsImagesAutomatically(false);
        }
    }

    @Override
    protected void initData() {

    }
    /**
     * 网页加载情况处理
     */
    private void webViewChromeClient() {
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(android.webkit.WebView view, int newProgress) {
                if (progress != null) {
                    progress.setProgress(newProgress * 100);
                    if (newProgress == 100) {
                        // 当newProgress为100时表示当前页面加完完成，隐藏即可
                        progress.setVisibility(View.GONE);
                    }
                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(android.webkit.WebView view, String title) {
                super.onReceivedTitle(view, title);
                // 获取当前当前网页的头部标题 title ，判断是否网页正常加载出来，加载失败时显示无内容界面，
                if (selectUrl.equals("javscript")){
                    return;
                }else {
                    selectUrl = "";
                }
            }
        });
    }

    /**
     * 设置webview加载状态
     */
    private void setWebViewLoad() {
        webView.setWebViewClient(new WebViewClient(){
            /**
             * 表示页面中的所有url点击跳转操作都由自己处理，不用手机系统自带的浏览器处理
             * @
             * view
             * @param url
             * @return
             */
            @Override
            public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
                if (url.equals("javscript:;")){
                    selectUrl = "javscript";

                }else
                    view.loadUrl(url);

                return true;
            }

            /**
             * 加载页面之前调用
             * @param view
             * @param url
             * @param favicon
             */
            @Override
            public void onPageStarted(android.webkit.WebView view, String url, Bitmap favicon) {
                // 在这里进行显示正在加载的Dialog
                if (progress != null)
                progress.setVisibility(View.VISIBLE);
                // 判断当前是否有可用网络，有网络时从网络获取，无网络时
                final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo == null || !networkInfo.isAvailable()){
                    // 表示无网络  根据cache-control决定是否从网络上取数据
                    webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
                }else {
                    // 表示有网络   只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据
                    webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                }
                super.onPageStarted(view, url, favicon);
            }

            /**
             * 页面加载结束调用
             * @param view
             * @param url
             */
            @Override
            public void onPageFinished(android.webkit.WebView view, String url) {
                // 在这里进行隐藏正在加载的Dialog
                if (progress != null)
                progress.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }

            /**
             * 请求网络资源的时候都会调用
             * @param view
             * @param url
             */
            @Override
            public void onLoadResource(android.webkit.WebView view, String url) {
                super.onLoadResource(view, url);
            }

        });
    }

    @Override
    public void onDestroyView() {
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
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder1 = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void bindIEOHomeData(IEOHomeEntity homeEntity) {

    }

    @Override
    public void onBuySuccess(String phase, String ufoNum) {

    }

    @Override
    public void onBuyFail(int code, String message) {

    }

    @Override
    public void bindIEOProgress(IEOProgress progress) {

    }

    @Override
    public void bindIEORelease(IEOReleaseEntity releaseEntity) {

    }

    @Override
    public void showUrl(String webUrl) {
        if (webUrl != null){
            //加载初始化登录的url页面
            webView.loadUrl(webUrl);
            MMKVTools.saveNavWebUrl(webUrl);
        }else {
//            showToast("暂无内容可以展示");
            if (MMKVTools.getNavWebUrl() != null) {
                webView.loadUrl(MMKVTools.getNavWebUrl());
            }
        }
    }
}
