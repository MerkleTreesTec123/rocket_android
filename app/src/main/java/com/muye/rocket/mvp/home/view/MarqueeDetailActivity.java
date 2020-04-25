package com.muye.rocket.mvp.home.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;

import butterknife.BindView;

public class MarqueeDetailActivity extends BaseActivity {
    @BindView(R.id.web_view)
    WebView webView;

    String mTitle;
    String mContent;

    public static void openMarqueeDetailActivity(Context context, String title, String content) {
        Intent intent = new Intent(context, MarqueeDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("content", content);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_marquee_detail;
    }

    @Override
    protected void handleIntent(Bundle bundle) {
        super.handleIntent(bundle);
        if (bundle != null) {
            mTitle = bundle.getString("title", "");
            mContent = bundle.getString("content", "");
        }
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(TextUtils.isEmpty(mTitle) ? "" : mTitle);
        String css = "<style>*{margin:0;padding:0} table{width: 100% !important;} img {max-width:100%; width:auto; height:auto!important;} h1{font-size: 30px !important;color: #00BFFF!important;} p{font-size: 15px !important;} a{text-decoration: none ; color: #0086b3}</style>";
        String data = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\">" + css + "</head><body>" + mContent + "</body><script>window.location.hash = 1;$(function(){document.title = $(window).height();});</script></html>";
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.loadData(data, "text/html; charset=UTF-8", null);
        webView.setWebViewClient(new WebViewClient());
    }

    @Override
    public void finish() {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }
}
