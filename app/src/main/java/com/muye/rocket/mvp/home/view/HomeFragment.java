package com.muye.rocket.mvp.home.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dhh.websocket.RxWebSocket;
import com.dhh.websocket.WebSocketSubscriber;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ifenduo.common.adapter.base.ViewHolder;
import com.ifenduo.common.tool.DimensionTool;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_base.tools.NumberUtil;
import com.ifenduo.lib_base.widget.tablayout.TabEntity;
import com.muye.rocket.ImageResourceViewAdHolder;
import com.muye.rocket.ImageResourceViewHolder;
import com.muye.rocket.R;
import com.muye.rocket.api.RURLConfig;
import com.muye.rocket.base.BaseListFragment;
import com.muye.rocket.cache.ExcCache;
import com.muye.rocket.entity.BannerEntity;
import com.muye.rocket.entity.MarqueeEntity;
import com.muye.rocket.entity.exchange.ExcTradingArea;
import com.muye.rocket.entity.exchange.ExcTradingPair;
import com.muye.rocket.entity.exchange.ExcTradingPairDetail;
import com.muye.rocket.entity.exchange.WSExcTradingPairDetail;
import com.muye.rocket.entity.ieo.IEOReleaseEntity;
import com.muye.rocket.event.KLineData;
import com.muye.rocket.mvp.WebBrowser;
import com.muye.rocket.mvp.exchange.contract.ExcQuotationContract;
import com.muye.rocket.mvp.exchange.presenter.ExcQuotationPresenter;
import com.muye.rocket.mvp.exchange.view.TradingPairDetailActivity;
import com.muye.rocket.mvp.home.contract.HomeContract;
import com.muye.rocket.mvp.home.presenter.HomePresenter;
import com.muye.rocket.mvp.me.splash.SplashContract;
import com.muye.rocket.mvp.me.splash.SplashPresenter;
import com.muye.rocket.mvp.me.view.HelpSupportActivity;
import com.muye.rocket.reveive.PushInfoAddressActivity;
import com.muye.rocket.reveive.PushInfoDataActivity;
import com.muye.rocket.tools.SPUtils;
import com.sunfusheng.marqueeview.MarqueeView;
import com.tencent.mmkv.MMKV;
import com.zhpan.bannerview.BannerViewPager;
import com.zhpan.bannerview.constants.IndicatorGravity;
import com.zhpan.bannerview.constants.IndicatorStyle;
import com.zhpan.bannerview.constants.PageStyle;
import com.zhpan.bannerview.utils.BannerUtils;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.WebSocket;
import rx.Subscription;

public class HomeFragment extends BaseListFragment<ExcTradingPair> implements ExcQuotationContract.View,
        HomeContract.View, OnTabSelectListener, MarqueeView.OnItemClickListener, SplashContract.View {
    View mHeaderView;
    MarqueeView mMarqueeView;
    CommonTabLayout mTabLayout;

    int mSp2px14;
    int mSp2px10;
    int mDp2px4;
    int mDp2px6;

    List<MarqueeEntity> mMarqueeList;
    List<ExcTradingArea> mTradingAreaList;
    ExcQuotationContract.Presenter mQuotationPresenter;
    HomeContract.Presenter mHomePresenter;
    private SplashPresenter splashPresenter;
    BannerViewPager viewBanner, bannerAdvert;
    private List<Integer> bannerList, advertList;
    private List<String> bannerEntitys, adBannerEntitys;
    private List<BannerEntity> mAdBannerEntityList, bannerEntities;

    private List<ExcTradingPair> tempTradingPairList;//临时数据
    private String tempTradingAreaName;
    List<String> usdtAreaExcTradingPair = new ArrayList<>();
    private Disposable disposable;//轮询
    private Subscription mSubscription;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getRootViewLayout() {
        return R.layout.exc_fragment_quotation;
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.exc_item_quotation;
    }

    @Override
    public boolean isSupportRefresh() {
        return true;
    }

    @Override
    public boolean isSupportLoadMore() {
        return false;
    }

    @Override
    protected int getEmptyString() {
        return super.getEmptyString();
    }

    @Override
    public void onResume() {
        super.onResume();
        startTimer();
        Log.e("首页", "可见1");
    }

    @Override
    public void onPause() {
        super.onPause();
        stopTimer();
        Log.e("首页", "不可见1");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mImmersionBar.statusBarDarkFont(true, 0.2f).init();
            startTimer();
            Log.e("首页", "可见");
        } else {
            stopTimer();
            Log.e("首页", "不可见");
        }
    }

    /*
     * 首页头部
     * */
    @Override
    public View getHeaderView() {
        if (mHeaderView == null) {
            mHeaderView = LayoutInflater.from(getContext()).inflate(R.layout.exc_header_quotation, mRecyclerView, false);
            mMarqueeView = mHeaderView.findViewById(R.id.marquee_view);
            mTabLayout = mHeaderView.findViewById(R.id.tab_layout);
            viewBanner = mHeaderView.findViewById(R.id.banner_view);
            bannerAdvert = mHeaderView.findViewById(R.id.banner_advert);
            int dp3 = BannerUtils.dp2px(3);

            bannerAdvert.setIndicatorMargin(dp3,dp3,dp3,dp3);
            mTabLayout.setOnTabSelectListener(this);
            mHeaderView.findViewById(R.id.exchange_container).setOnClickListener(this);
            mHeaderView.findViewById(R.id.c2c_container).setOnClickListener(this);
            mHeaderView.findViewById(R.id.ll_help).setOnClickListener(this);
            mMarqueeView.setOnItemClickListener(this);
            //设置ViewPager
            setBanner(0);
            setAdvert(0);
            viewBanner.setOnPageClickListener(new BannerViewPager.OnPageClickListener() {
                @Override
                public void onPageClick(int position) {
                    if (bannerEntities != null) {
                        BannerEntity bannerEntity = bannerEntities.get(position);
                        if (!TextUtils.isEmpty(bannerEntity.getUrl())) {
                            WebBrowser.openWebBrowser(getContext(), bannerEntity.getUrl(), bannerEntity.getName());
                        }
                    }
                }
            });

            bannerAdvert.setOnPageClickListener(new BannerViewPager.OnPageClickListener() {
                @Override
                public void onPageClick(int position) {
                    if (mAdBannerEntityList != null) {
                        BannerEntity bannerEntity = mAdBannerEntityList.get(position);

                        if (!TextUtils.isEmpty(bannerEntity.getUrl())) {
                            WebBrowser.openWebBrowser(getContext(), bannerEntity.getUrl(), bannerEntity.getName());
                        }
                    }
                }
            });

        }
        return mHeaderView;
    }

    @Override
    protected void initView(View parentView) {
        super.initView(parentView);

        mSp2px10 = DimensionTool.sp2px(getContext(), 10);
        mSp2px14 = DimensionTool.sp2px(getContext(), 14);
        mDp2px4 = DimensionTool.dp2px(getContext(), 4);
        mDp2px6 = DimensionTool.dp2px(getContext(), 6);

        if (MMKVTools.isOpenWs() == 1) {
            wsClient();
        }

        //跑马灯
        fetchMarquee();
        fetchIEORelease();
        // 发布版开通，让用户不可见，已经开通交易，可以不调用
//        mHomePresenter.fetchShow();
        // 刚进入设置默认的死数据（交易区域）
        setupTabLayout();
        fetchTradingPairList();
        isShowPushInfo();
        isShowPushWebUrl();
    }

    /*
     * 页面进入请求（获取交易区）
     * */
    @Override
    public void onRequest(int page) {
        fetchTradingArea();

        if (splashPresenter == null) {
            splashPresenter = new SplashPresenter(this);
        }
        splashPresenter.getOpenWs();
        Log.e("測試", "onRequest: 下拉刷新");
    }

    /**
     * 获取banner数据，公告文字
     */
    private void fetchMarquee() {
        if (mHomePresenter == null) {
            mHomePresenter = new HomePresenter(this);
        }
        mHomePresenter.fetchBanner();
        mHomePresenter.fetchAdBanner();
        mHomePresenter.fetchMarquee();
        mHomePresenter.saveUserPushId();//保存推送id
    }

    /*
     * 保存一些数据(不知道啥用)
     * */
    private void fetchIEORelease() {
        if (mHomePresenter == null) {
            mHomePresenter = new HomePresenter(this);
        }
        mHomePresenter.fetchIEORelease();
    }

    /*
     * 设置tab
     * */
    private void setupTabLayout() {
        if (mTradingAreaList == null || mTradingAreaList.size() <= 0) {
            String strJson = "[{\"code\":\"1\",\"name\":\"USDT\"},{\"code\":\"2\",\"name\":\"BTC\"},{\"code\":\"3\",\"name\":\"ETH\"}]";
            Gson gson = new Gson();
            mTradingAreaList = gson.fromJson(strJson, new TypeToken<List<ExcTradingArea>>() {
            }.getType());

        }
        if (mTradingAreaList != null && mTradingAreaList.size() > 0) {
            ArrayList<CustomTabEntity> tabEntityList = new ArrayList<>();
            for (int i = 0; i < mTradingAreaList.size(); i++) {
                ExcTradingArea tradingArea = mTradingAreaList.get(i);
                if (tradingArea != null) {
                    tabEntityList.add(new TabEntity(tradingArea.getName()));
                }
            }
            mTabLayout.setTabData(tabEntityList);
        }
    }

    /**
     * 根据获取的交易区域集合，遍历请求每个下面的展示币种数据
     * （目前是获取 USDT 区域下面的 CAT 数据）
     */
    private void fetchTradingPairList() {
        if (mTradingAreaList != null && mTradingAreaList.size() > 0) {
            for (int i = 0; i < mTradingAreaList.size(); i++) {
                ExcTradingArea tradingArea = mTradingAreaList.get(i);
                if (tradingArea != null && !TextUtils.isEmpty(tradingArea.getName()) && !TextUtils.isEmpty(tradingArea.getCode())) {
                    fetchTradingPairList(tradingArea.getCode(), tradingArea.getName());
                }
            }
        }
        if (mRefreshLayout != null)
            mRefreshLayout.finishRefresh();
        // 若无数据。。。。
    }

    /*
     * 检查是否有未点击确认的推送消息
     * */
    private void isShowPushInfo() {
        String pushImgUrl = MMKV.defaultMMKV().decodeString("push_address_url", "");
        if (!TextUtils.isEmpty(pushImgUrl) && !SPUtils.isClickPushNotific()) {
            Intent intent = new Intent(getContext(), PushInfoDataActivity.class);
            startActivity(intent);
        }
    }

    private void isShowPushWebUrl() {
        String pushImgUrl = MMKV.defaultMMKV().decodeString("web_load_url", "");
        if (!TextUtils.isEmpty(pushImgUrl) && !SPUtils.isClickPushWeb()) {
            Intent intent = new Intent(getContext(), PushInfoAddressActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 顶部banner图数据
     */
    @Override
    public void bindBanner(List<BannerEntity> bannerEntityList) {
        bannerEntities = bannerEntityList;
        if (bannerEntityList == null) {
            setBanner(0);
        } else {
            bannerEntitys = new ArrayList<>();
            for (int i = 0; i < bannerEntityList.size(); i++) {
                BannerEntity bannerEntity = bannerEntityList.get(i);
                if (bannerEntity != null) {
                    bannerEntitys.add(bannerEntity.getThumb());
                }
            }
            setBanner(1);
        }
    }

    /**
     * 中间的banner图数据
     */
    @Override
    public void bindAdBanner(List<BannerEntity> bannerEntityList) {
        mAdBannerEntityList = bannerEntityList;
        if (bannerEntityList == null) {
            setAdvert(0);
        } else {
            adBannerEntitys = new ArrayList<>();
            for (int i = 0; i < bannerEntityList.size(); i++) {
                BannerEntity bannerEntity = bannerEntityList.get(i);
                if (bannerEntity != null) {
                    adBannerEntitys.add(bannerEntity.getThumb());
                }
            }
            bannerAdvert.setVisibility(View.VISIBLE);
            setAdvert(1);
        }
    }

    /*
     * 跑马灯
     * */
    @Override
    public void bindMarqueeData(List<MarqueeEntity> marqueeList) {
        if (marqueeList == null) {

        }
        mMarqueeList = new ArrayList<>();
        if (marqueeList != null && marqueeList.size() > 0) {
            for (int i = 0; i < marqueeList.size(); i++) {
                MarqueeEntity entity = marqueeList.get(i);
                if (entity != null && !TextUtils.isEmpty(entity.getTitle())) {

                    Log.e("測試", "bindMarqueeData: " + entity.getTitle() + ">>" + entity.getContent());
                    mMarqueeList.add(entity);
                }
            }
        }
        mMarqueeView.startWithList(mMarqueeList);
    }

    /*
     * 不知道啥用的接口
     * */
    @Override
    public void bindIEORelease(IEOReleaseEntity releaseEntity) {
//        EventBus.getDefault().post(new RefreshIEOReleaseEvent());
//        fetchIEORelease();
    }

    /**
     * 获取交易区,目前请求有三个数据   USDT 、 BTC 、 ETH  （现在主要使用USDT下CAT数据）
     * /v1/market/area.html
     */
    private void fetchTradingArea() {
        if (mQuotationPresenter == null) {
            mQuotationPresenter = new ExcQuotationPresenter(this);
        }
        mQuotationPresenter.fetchTradingArea();
    }

    /**
     * 获取交易区域数据，USDT  ， BTC  ， ETH  进行显示
     */
    @Override
    public void bindTradingArea(List<ExcTradingArea> tradingAreaList) {
        if (mTradingAreaList != null) {
            mTradingAreaList.clear();
        } else {
            mTradingAreaList = new ArrayList<>();
        }
        if (tradingAreaList != null && tradingAreaList.size() > 0) {

        } else {//获取缓存数据
            String strJson = "[{\"code\":\"1\",\"name\":\"USDT\"},{\"code\":\"2\",\"name\":\"BTC\"},{\"code\":\"3\",\"name\":\"ETH\"}]";
            Gson gson = new Gson();
            tradingAreaList = gson.fromJson(strJson, new TypeToken<List<ExcTradingArea>>() {
            }.getType());
        }
        if (tradingAreaList != null && tradingAreaList.size() > 0) {
            for (int i = 0; i < tradingAreaList.size(); i++) {
                ExcTradingArea tradingArea = tradingAreaList.get(i);
                if (tradingArea != null && !TextUtils.isEmpty(tradingArea.getName()) && !TextUtils.isEmpty(tradingArea.getCode())) {
                    mTradingAreaList.add(tradingArea);
                    ExcCache.saveTradingPair(tradingArea.getName(), null);
                }
            }
        }
        fetchTradingPairList();
        setupTabLayout();
    }

    /**
     * 0: 本地图片加载
     * 1：线上轮播地址
     */
    private void setBanner(int type) {
        if (type == 0) {
            bannerList = new ArrayList<>();
            bannerList.add(R.drawable.image_home_banner_top);
            bannerList.add(R.drawable.columbu_rocket);
        }

        viewBanner.setIndicatorStyle(IndicatorStyle.ROUND_RECT)
                .setIndicatorGravity(IndicatorGravity.CENTER)
                .setIndicatorGap(BannerUtils.dp2px(4))
                .setHolderCreator(() -> new ImageResourceViewHolder(getContext()))
                .setPageMargin(BannerUtils.dp2px(10))
                .setRevealWidth(BannerUtils.dp2px(10))
                .setPageStyle(PageStyle.MULTI_PAGE_SCALE)
                .setIndicatorHeight(BannerUtils.dp2px(4f))
                .setIndicatorWidth(BannerUtils.dp2px(4), BannerUtils.dp2px(10))
                .setInterval(2000);

        if (type == 0) {
            viewBanner.create(bannerList);
        } else {
            viewBanner.create(bannerEntitys);
        }
        viewBanner.setAutoPlay(true);
    }

    /**
     * 中间小轮播图
     */
    private void setAdvert(int type) {
        if (type == 0) {
            advertList = new ArrayList<>();
            advertList.add(R.drawable.advert_gas);
            advertList.add(R.drawable.advert_two);
        }

        bannerAdvert.setIndicatorStyle(IndicatorStyle.ROUND_RECT)
                .setIndicatorGravity(IndicatorGravity.CENTER)
                .setIndicatorGap(BannerUtils.dp2px(4))
                .setHolderCreator(() -> new ImageResourceViewAdHolder(getContext()))
                .setPageMargin(0)
                .setRoundCorner(BannerUtils.dp2px(6))
                .setIndicatorHeight(BannerUtils.dp2px(4f))
                .setIndicatorWidth(BannerUtils.dp2px(4), BannerUtils.dp2px(10))
                .setInterval(2000);

        if (type == 0) {
            bannerAdvert.create(advertList);
        } else {
            bannerAdvert.create(adBannerEntitys);
        }
        bannerAdvert.setAutoPlay(true);

        if (type == 0) {
            bannerAdvert.setAutoPlay(false);
            bannerAdvert.setVisibility(View.GONE);
        }
    }

    /**
     * 交易区下的交易对
     * 不停的请求
     */
    private void fetchTradingPairList(String tradingAreaCode, String tradingAreaName) {
        if (!TextUtils.isEmpty(tradingAreaCode) && !TextUtils.isEmpty(tradingAreaName)) {
            if (mQuotationPresenter == null) {
                mQuotationPresenter = new ExcQuotationPresenter(this);
            }
//            List<ExcTradingPair> tradingpair = null;
//            if (tradingpair == null || tradingpair.size() <= 0){
//                String strJson = "[{\"blockName\":\"主板\",\"buyCoinId\":\"3\",\"buyFee\":\"0.004\",\"buyName\":\"USDT\",\"buyShortName\":\"USDT\",\"buySymbol\":\"0\",\"id\":\"46\",\"sellAppLogo\":\"https://rocket-static.oss-ap-southeast-1.aliyuncs.com/rocketcoin/upload/coin/d6b227f5814a47b1abaa94cc6b89addfcat.png\",\"sellCoinId\":\"21\",\"sellFee\":\"0.004\",\"sellName\":\"CAT\",\"sellShortName\":\"CAT\",\"sellSymbol\":\"\",\"tradeBlock\":\"1\",\"typeName\":\"对USDT交易区\"}]";
//                Gson gson = new Gson();
//                tradingpair = gson.fromJson(strJson, new TypeToken<List<ExcTradingPair>>() {}.getType());
//            }
//            //直接先赋值 假数据
//            bindTradingPair(tradingAreaName,tradingpair);
            mQuotationPresenter.fetchTradingPairList(tradingAreaCode, tradingAreaName);
        }
    }

    /**
     * 交易对信息
     */
    @Override
    public void bindTradingPair(int tag, String tradingAreaName, List<ExcTradingPair> tradingPairList) {

        Log.e("测试正式服", "bindTradingPair: " + tag + ">>" + tradingAreaName);
        if (tag == 0 && tradingPairList != null && tradingPairList.size() > 0) {
            tempTradingAreaName = tradingAreaName;
            tempTradingPairList = tradingPairList;
        }

        if (!TextUtils.isEmpty(tradingAreaName)) {
            //保存交易区的交易对列表
            ExcCache.saveTradingPair(tradingAreaName, tradingPairList);
            //是当前显示的交易区，刷新数据
            ExcTradingArea currentTradingArea = getCurrentTradingArea();
            if (currentTradingArea != null && !TextUtils.isEmpty(currentTradingArea.getName()) && tradingAreaName.equals(currentTradingArea.getName())) {
                dispatchResult(tradingPairList);
            }
        }
        ExcTradingArea tradingArea = null;
        if (mTradingAreaList != null && mTradingAreaList.size() > 0) {
            for (ExcTradingArea area : mTradingAreaList) {
                if (area != null && tradingAreaName.equals(area.getName())) {
                    tradingArea = area;
                    break;
                }
            }
        }
       /* if (tradingArea != null && !AppTool.isApkInDebug(getContext())) {
            Log.e("测试正式服1", "bindTradingPair: " + "走走走" );
            fetchTradingPairList(tradingArea.getCode(), tradingArea.getName());
        }*/
    }

    /*
     * 获取当前区域数据
     * */
    private ExcTradingArea getCurrentTradingArea() {
        int position = mTabLayout.getCurrentTab();
        if (mTradingAreaList != null && mTradingAreaList.size() > position) {
            return mTradingAreaList.get(position);
        }
        return null;
    }

    @Override
    public void onBindView(ViewHolder holder, ExcTradingPair tradingPair, int position) {
        TextView titleTextView = holder.getView(R.id.title_text_view);
        TextView gainTextView = holder.getView(R.id.gain_text_view);
        if (tradingPair != null) {
            holder.setText(R.id.num_text_view, getString(R.string.quantity_24) + " " + NumberUtil.formatMoney(tradingPair.getTotal()));
            holder.setText(R.id.price_text_view, NumberUtil.formatMoney(tradingPair.getP_new()));
            holder.setText(R.id.price_text_view_, "¥" + NumberUtil.formatRMB(ExcCache.getRMBPriceCache(tradingPair.getSellShortName())));

            String sellName = tradingPair.getSellShortName();
            String buyName = tradingPair.getBuyShortName();
            String title = "";
            SpannableString spannableString = null;
            if (TextUtils.isEmpty(sellName)) {
                sellName = "--";
            }
            if (TextUtils.isEmpty(buyName)) {
                buyName = "--";
            }
            title = sellName + "/" + buyName;
            spannableString = new SpannableString(title);
            ForegroundColorSpan sellColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.color474A57));
            spannableString.setSpan(sellColorSpan, 0, sellName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            AbsoluteSizeSpan sellSizeSpan = new AbsoluteSizeSpan(mSp2px14);
            spannableString.setSpan(sellSizeSpan, 0, sellName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            ForegroundColorSpan buyColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.colorA0A0A0));
            spannableString.setSpan(buyColorSpan, sellName.length(), title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            AbsoluteSizeSpan buySizeSpan = new AbsoluteSizeSpan(mSp2px10);
            spannableString.setSpan(buySizeSpan, sellName.length(), title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            titleTextView.setText(spannableString);

            double gain = NumberUtil.string2Double(tradingPair.getChg());

            if (gain >= 0) {
                gainTextView.setBackgroundColor(getResources().getColor(R.color.color07C087));
                gainTextView.setText("+" + NumberUtil.formatMoney(gain, 2) + "%");
            } else {
                gainTextView.setBackgroundColor(getResources().getColor(R.color.colorEA4E00));
                gainTextView.setText(NumberUtil.formatMoney(gain, 2) + "%");
            }

        } else {
            holder.setText(R.id.num_text_view, getString(R.string.quantity_24) + " --");
            holder.setText(R.id.price_text_view, "--");
            holder.setText(R.id.price_text_view_, "¥--");
            titleTextView.setText("--");
            titleTextView.setTextColor(getResources().getColor(R.color.color474A57));
            titleTextView.setTextSize(14);
            gainTextView.setText("--%");
            gainTextView.setBackgroundColor(getResources().getColor(R.color.color07C087));
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.exchange_container) {//币币交易
//            ExchangeActivity.openExchangeActivity(getContext(), "");
            ((HomeActivity) getCurrentActivity()).showTrade();
        } else if (v.getId() == R.id.c2c_container) {//法币交易
            ((HomeActivity) getCurrentActivity()).showC2C();
        } else if (v.getId() == R.id.ll_help) {//帮助中心
            Intent intent = new Intent(getContext(), HelpSupportActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 顶部banner下面的公告点击
     *
     * @param position
     * @param textView
     */
    @Override
    public void onItemClick(int position, TextView textView) {
        if (mMarqueeList != null && mMarqueeList.size() > position) {
            MarqueeEntity entity = mMarqueeList.get(position);
            if (entity != null) {
                MarqueeDetailActivity.openMarqueeDetailActivity(getContext(), getString(R.string.notice), entity.getContent());
            }
        }
    }

    /*
     * 条目点击（跳转行情图）
     * */
    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position, ExcTradingPair tradingPair) {
        if (tradingPair != null) {
            TradingPairDetailActivity.openTradingPairDetailActivity(getContext(), tradingPair.getId(), tradingPair.getSellShortName(), tradingPair.getBuyShortName(), tradingPair.getSellCoinId());
            //关闭定时器
            if (disposable != null) {
                disposable.dispose();
            }
        }
    }

    /**
     * HomeFragment下面的币种导航tab 选择点击
     *
     * @param position
     */
    @Override
    public void onTabSelect(int position) {
        ExcTradingArea currentTradingArea = getCurrentTradingArea();
        if (currentTradingArea != null && !TextUtils.isEmpty(currentTradingArea.getName())) {
            if (ExcCache.hasTradingArea(currentTradingArea.getName())) {
                dispatchResult(ExcCache.getTradingPairCache(currentTradingArea.getName()));
            } else {
                dispatchResult(new ArrayList<>());
            }
        }
    }

    @Override
    public void onTabReselect(int position) {

    }

    /*
     * 暂时不用
     * */
    @Override
    public void isShowTrader(Integer isShow) {
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mMarqueeView != null) {
            mMarqueeView.startFlipping();
        }
        if (viewBanner != null) {
            if (bannerEntitys != null && bannerEntitys.size() > 0) {
                setBanner(1);
            } else {
                setBanner(0);
            }
            viewBanner.setAutoPlay(true);
        }
        if (bannerAdvert != null) {
            if (adBannerEntitys != null && adBannerEntitys.size() > 0) {
                setAdvert(1);
            } else {
                setAdvert(0);
            }
            bannerAdvert.setAutoPlay(true);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mMarqueeView != null) {
            mMarqueeView.stopFlipping();
        }
        if (bannerAdvert != null) {
            bannerAdvert.setAutoPlay(false);
        }
        if (viewBanner != null) {
            viewBanner.setAutoPlay(false);
        }
    }

    @Override
    public void onDestroyView() {
        if (mQuotationPresenter != null) {
            mQuotationPresenter.dropView();
        }
        if (mHomePresenter != null) {
            mHomePresenter.dropView();
        }
        if (disposable != null) {
            disposable.dispose();
        }
        //断开连接
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }

        super.onDestroyView();
    }


    private void onReceveMessage(String message) {
        if (!message.contains("heart")) {
            com.alibaba.fastjson.JSONObject outJson = com.alibaba.fastjson.JSONObject.parseObject(message);
            if (outJson != null) {
                String id = outJson.getString("client_id");
                if (!TextUtils.isEmpty(id)) {
                    //存值
                    MMKV.defaultMMKV().encode("client_id", id);
                }
                Log.e("测试", "onMessage: " + id);

                String type = outJson.getString("type");
                if (type.equals("1")) {//首页数据
                    String data1 = outJson.getString("data");
                    List<ExcTradingPair> temp = new Gson().fromJson(data1, new TypeToken<List<ExcTradingPair>>() {
                    }.getType());
                    bindTradingPair(1, tempTradingAreaName, buildTradingPair(tempTradingPairList, temp));
                } else if (type.equals("2") || type.equals("5")) {//2cat行情走势  //5 btc和eth 行情走势
                    String symbol = outJson.getString("symbol");

                    String data1 = "";
                    String second = "";
                    if (type.equals("2")) {
                        data1 = outJson.getString("data");
                        second = outJson.getString("second");
                    } else {
                        data1 = outJson.getJSONArray("data").toJSONString();
                        second = outJson.getString("step");
                    }

                    List<List<BigDecimal>> bigDecimal = new Gson().fromJson(data1, new TypeToken<List<List<BigDecimal>>>() {
                    }.getType());
                    KLineData kLineData = new KLineData(symbol, second, bigDecimal);

                    //发送数据到K线图页面
                    EventBus.getDefault().post(kLineData);
                } else if (type.equals("3") || type.equals("4")) {//3k线图头部数据与深度  //4买卖数据
                    String data1 = outJson.getString("data");

                    WSExcTradingPairDetail wsExcTradingPairDetail = new Gson().fromJson(data1, WSExcTradingPairDetail.class);
                    WSExcTradingPairDetail.DepthBean depth = wsExcTradingPairDetail.getDepth();
                    String asks = depth.getAsks();
                    String bids = depth.getBids();

                    List<List<Double>> asksList = new Gson().fromJson(asks, new TypeToken<List<List<Double>>>() {
                    }.getType());
                    List<List<Double>> bidsList = new Gson().fromJson(bids, new TypeToken<List<List<Double>>>() {
                    }.getType());

                    ExcTradingPairDetail.DepthBean depthBean = new ExcTradingPairDetail.DepthBean();
                    depthBean.setDate(depth.getDate());
                    depthBean.setAsks(asksList);
                    depthBean.setBids(bidsList);

                    ExcTradingPairDetail excTradingPairDetail = new ExcTradingPairDetail();
                    excTradingPairDetail.setBlock(wsExcTradingPairDetail.getBlock());
                    excTradingPairDetail.setBuy(wsExcTradingPairDetail.getBuy());
                    excTradingPairDetail.setBuySymbol(wsExcTradingPairDetail.getBuySymbol());
                    excTradingPairDetail.setChg(wsExcTradingPairDetail.getChg());
                    excTradingPairDetail.setHigh(wsExcTradingPairDetail.getHigh());
                    excTradingPairDetail.setLow(wsExcTradingPairDetail.getLow());
                    excTradingPairDetail.setP_new(wsExcTradingPairDetail.getP_new());
                    excTradingPairDetail.setP_open(wsExcTradingPairDetail.getP_open());
                    excTradingPairDetail.setSell(wsExcTradingPairDetail.getSell());
                    excTradingPairDetail.setSellSymbol(wsExcTradingPairDetail.getSellSymbol());
                    excTradingPairDetail.setSymbol(wsExcTradingPairDetail.getSymbol());
                    excTradingPairDetail.setTotal(wsExcTradingPairDetail.getTotal());
                    excTradingPairDetail.setTradeId(wsExcTradingPairDetail.getTradeId());
                    excTradingPairDetail.setTrades(wsExcTradingPairDetail.getTrades());
                    excTradingPairDetail.setDepth(depthBean);
                    Log.e("连接", "onMessage: " + wsExcTradingPairDetail.getBuy());

                    //发送数据
                    EventBus.getDefault().post(excTradingPairDetail);
                } else if (type.equals("6")) {//6 btc和eth k线图头部数据与深度  买卖数据
                    String data1 = outJson.getString("data");

                    ExcTradingPairDetail wsExcTradingPairDetail = new Gson().fromJson(data1, ExcTradingPairDetail.class);
                    ExcTradingPairDetail.DepthBean depth = wsExcTradingPairDetail.getDepth();

                    List<List<Double>> asksList = depth.getAsks();
                    List<List<Double>> bidsList = depth.getBids();

                    ExcTradingPairDetail.DepthBean depthBean = new ExcTradingPairDetail.DepthBean();
                    depthBean.setDate(depth.getDate());
                    depthBean.setAsks(asksList);
                    depthBean.setBids(bidsList);

                    ExcTradingPairDetail excTradingPairDetail = new ExcTradingPairDetail();
                    excTradingPairDetail.setBlock(wsExcTradingPairDetail.getBlock());
                    excTradingPairDetail.setBuy(wsExcTradingPairDetail.getBuy());
                    excTradingPairDetail.setBuySymbol(wsExcTradingPairDetail.getBuySymbol());
                    excTradingPairDetail.setChg(wsExcTradingPairDetail.getChg());
                    excTradingPairDetail.setHigh(wsExcTradingPairDetail.getHigh());
                    excTradingPairDetail.setLow(wsExcTradingPairDetail.getLow());
                    excTradingPairDetail.setP_new(wsExcTradingPairDetail.getP_new());
                    excTradingPairDetail.setP_open(wsExcTradingPairDetail.getP_open());
                    excTradingPairDetail.setSell(wsExcTradingPairDetail.getSell());
                    excTradingPairDetail.setSellSymbol(wsExcTradingPairDetail.getSellSymbol());
                    excTradingPairDetail.setSymbol(wsExcTradingPairDetail.getSymbol());
                    excTradingPairDetail.setTotal(wsExcTradingPairDetail.getTotal());
                    excTradingPairDetail.setTradeId(wsExcTradingPairDetail.getTradeId());
                    excTradingPairDetail.setTrades(wsExcTradingPairDetail.getTrades());
                    excTradingPairDetail.setDepth(depthBean);
                    Log.e("连接", "onMessage: " + wsExcTradingPairDetail.getBuy());

                    //发送数据
                    EventBus.getDefault().post(excTradingPairDetail);
                }
            }
        }
    }

    /**
     * 拼接交易对ID
     */
    public String buildTradingPairIds(List<ExcTradingPair> tradingPairList) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (tradingPairList != null && tradingPairList.size() > 0) {
            int size = tradingPairList.size();
            for (int i = 0; i < size; i++) {
                ExcTradingPair tradingPair = tradingPairList.get(i);
                if (tradingPair != null && !TextUtils.isEmpty(tradingPair.getId())) {
                    stringBuilder.append(String.valueOf(tradingPair.getId()));
                    if (i != size - 1) {
                        stringBuilder.append(",");
                    }
                }
            }
        }
        return stringBuilder.toString();
    }

    /*
     * 组合list
     * */
    private List<ExcTradingPair> buildTradingPair(List<ExcTradingPair> tradingPairList, List<ExcTradingPair> tradingPairInfoList) {
        if (tradingPairList != null && tradingPairList.size() > 0 && tradingPairInfoList != null && tradingPairInfoList.size() > 0) {
            for (ExcTradingPair tradingPair : tradingPairList) {
                if (tradingPair != null && !TextUtils.isEmpty(tradingPair.getSellShortName()) && !TextUtils.isEmpty(tradingPair.getBuyShortName())) {
                    for (ExcTradingPair tradingPairInfo : tradingPairInfoList) {
                        if (tradingPairInfo != null && tradingPair.getSellShortName().equals(tradingPairInfo.getSellSymbol())
                                && tradingPair.getBuyShortName().equals(tradingPairInfo.getBuySymbol())) {
                            tradingPair.setChg(tradingPairInfo.getChg());
                            tradingPair.setTotal(tradingPairInfo.getTotal());
                            tradingPair.setP_new(tradingPairInfo.getP_new());
                            tradingPair.setBuy(tradingPairInfo.getBuy());
                            tradingPair.setSell(tradingPairInfo.getSell());
                            tradingPair.setSellSymbol(tradingPairInfo.getSellSymbol());
                            tradingPair.setBuySymbol(tradingPairInfo.getBuySymbol());
                            tradingPair.setHigh(tradingPairInfo.getHigh());
                            tradingPair.setP_open(tradingPairInfo.getP_open());
                            tradingPair.setLow(tradingPairInfo.getLow());
                            tradingPair.setBlock(tradingPairInfo.getBlock());
                            tradingPair.setTradeId(tradingPairInfo.getTradeId());
                            saveRMBPriceCache(tradingPair);
                            break;
                        }
                    }
                }
            }
        }
        return tradingPairList;
    }

    private void saveRMBPriceCache(ExcTradingPair tradingPair) {
        if (tradingPair != null && !TextUtils.isEmpty(tradingPair.getBuyShortName()) && !TextUtils.isEmpty(tradingPair.getSellShortName())) {
            if ("USDT".equals(tradingPair.getBuyShortName())) {
                if (!usdtAreaExcTradingPair.contains(tradingPair.getSellShortName())) {
                    usdtAreaExcTradingPair.add(tradingPair.getSellShortName());
                }
                double price = NumberUtil.string2Double(tradingPair.getP_new()) * ExcCache.getUsdt2RmbCache();
                ExcCache.saveRMBPriceCache(tradingPair.getSellShortName(), price);
            } else {
                if (!usdtAreaExcTradingPair.contains(tradingPair.getSellShortName()) && usdtAreaExcTradingPair.contains(tradingPair.getBuyShortName())) {
                    double price = ExcCache.getRMBPriceCache(tradingPair.getBuyShortName()) * NumberUtil.string2Double(tradingPair.getP_new());
                    ExcCache.saveRMBPriceCache(tradingPair.getSellShortName(), price);
                }
            }
        }
    }


    /*
     * 开启轮询
     * */
    private void startTimer() {
        if (MMKVTools.isOpenWs() == 1) {
            if (disposable == null || (disposable != null && disposable.isDisposed())) {
                disposable = Observable.interval(0, 3, TimeUnit.SECONDS)
                        .map(new Function<Long, Long>() {
                            @Override
                            public Long apply(Long aLong) throws Exception {
                                return aLong + 1;
                            }
                        })
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long count) throws Exception {
                                Log.e("轮询1", "count: " + count + ">>" + tempTradingAreaName);

                                if (!TextUtils.isEmpty(tempTradingAreaName)) {
                                    String ids = buildTradingPairIds(tempTradingPairList);
                                    if (TextUtils.isEmpty(ids)) {

                                        tempTradingAreaName = "USDT";
                                        String data = "[{\"symbol\":\"0\",\"chg\":0.42,\"p_new\":7404.68,\"buy\":7371.86,\"sell\":7416.61,\"sellSymbol\":\"BTC\",\"buySymbol\":\"USDT\",\"total\":10224.0818,\"high\":7416.61,\"p_open\":7373.57,\"low\":7371.86,\"block\":1,\"tradeId\":1},{\"symbol\":\"0\",\"chg\":0.01,\"p_new\":147.21,\"buy\":146.85,\"sell\":147.5,\"sellSymbol\":\"ETH\",\"buySymbol\":\"USDT\",\"total\":56605.4503,\"high\":147.5,\"p_open\":147.19,\"low\":146.85,\"block\":1,\"tradeId\":2},{\"symbol\":\"0\",\"chg\":13.16,\"p_new\":36.3550,\"buy\":28.134,\"sell\":36.3550,\"sellSymbol\":\"CAT\",\"buySymbol\":\"USDT\",\"total\":12156.0889,\"high\":36.3550,\"p_open\":36.3550,\"low\":18.5,\"block\":1,\"tradeId\":46}]";
                                        List<ExcTradingPair> info = new Gson().fromJson(data, new TypeToken<List<ExcTradingPair>>() {
                                        }.getType());

                                        bindTradingPair(0, tempTradingAreaName, buildTradingPair(tempTradingPairList, info));
//                                    bindTradingPair(0, tempTradingAreaName, tempTradingPairList);
                                        return;
                                    }
                                    //測試
                                    String client_id = MMKV.defaultMMKV().decodeString("client_id", "");
                                    Log.e("轮询1", "ids=>>" + ids + "--" + client_id);
                                    if (!TextUtils.isEmpty(client_id) && !TextUtils.isEmpty(ids)) {
                                        String text = "{\"client_id\":\"" + client_id + "\",\"type\":\"" + 1 + "\",\"symbol\":\"" + ids + "\"}";
                                        RxWebSocket.asyncSend(RURLConfig.WS_BASE_URL, text);
                                        Log.e("轮询1", "发送成功");
                                    }
                                }
                            }
                        });
            }
        }
    }

    /*
     * 停止轮询
     * */
    private void stopTimer() {
        if (disposable != null) {
            disposable.dispose();
        }
    }

    /*
     * 下拉刷新是否开启长连接
     * */
    @Override
    public void saveCode(int code) {
        //开启长连接
        MMKVTools.saveOpenWs(code);
        Log.e("刷新code", "saveCode: " + code);
        if (code == 1) {
            wsClient();
            startTimer();
        }
    }

    /*
    * 连接ws
    * */
    private void wsClient() {
        //注销
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        //连接

        mSubscription = RxWebSocket.get(RURLConfig.WS_BASE_URL)
                .subscribe(new WebSocketSubscriber() {
                    @Override
                    public void onOpen(@NonNull WebSocket webSocket) {
                        Log.e("測試", "连接上了");
                    }

                    @Override
                    public void onMessage(@NonNull String text) {
                        Log.e("測試", "接收文本消息>>" + text);
                        onReceveMessage(text);
                    }

                    @Override
                    protected void onReconnect() {
                        Log.e("MainActivity", "重连:");
                    }

                    @Override
                    protected void onClose() {
                        Log.e("測試", "断开连接");
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null) {
                            Log.e("測試", "连接失败" + e.toString());
                        } else {
                            Log.e("測試", "连接失败:null");
                        }
                    }
                });
    }
}
