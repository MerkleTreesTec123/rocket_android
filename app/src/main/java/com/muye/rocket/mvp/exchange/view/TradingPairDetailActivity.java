package com.muye.rocket.mvp.exchange.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dhh.websocket.RxWebSocket;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.muye.rocket.R;
import com.muye.rocket.api.RURLConfig;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.cache.KLineCache;
import com.muye.rocket.entity.exchange.ExcCoinIntroduction;
import com.muye.rocket.entity.exchange.ExcTradingPair;
import com.muye.rocket.entity.exchange.ExcTradingPairDetail;
import com.muye.rocket.entity.exchange.KLineEntity;
import com.muye.rocket.event.KLineData;
import com.muye.rocket.mvp.exchange.contract.ExcExchangeContract;
import com.muye.rocket.mvp.exchange.contract.TradingPairDetailContract;
import com.muye.rocket.mvp.exchange.presenter.TradingPairDetailPresenter;
import com.muye.rocket.tools.SPUtils;
import com.muye.rocket.tools.ThreadUtils;
import com.muye.rocket.widget.TradingPairPicker;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

//K线图界面
public class TradingPairDetailActivity extends BaseActivity implements TradingPairPicker.OnTradingPairSelectedListener, TradingPairDetailContract.View {
    @BindView(R.id.title_text_view)
    TextView titleTextView;
    @BindView(R.id.toolbar_container)
    RelativeLayout toolbarContainer;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.container)
    LinearLayout mPageContainer;

    String mTradingPairID;
    String mSellName;
    String mBuyName;
    String mSellCoinID;
    TradingPairPicker mTradingPairPicker;
    TradingPairDetailAdapter mAdapter;
    TradingPairDetailContract.Presenter mDetailPresenter;

    /*
     * 轮询
     * */
    Disposable disposable;

    public static void openTradingPairDetailActivity(Context context, String tradingPairID, String sellName, String buyName, String sellCoinID) {
        Intent intent = new Intent(context, TradingPairDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("tradingPairID", tradingPairID);
        bundle.putString("sellName", sellName);
        bundle.putString("buyName", buyName);
        bundle.putString("sellCoinID", sellCoinID);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected int getStatusBarColor() {
        return getResources().getColor(R.color.white);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.exc_activity_trading_pair_detail;
    }

    @Override
    protected void handleIntent(Bundle bundle) {
        super.handleIntent(bundle);
        if (bundle != null) {
            mTradingPairID = bundle.getString("tradingPairID", "");
            Log.e("symbol-->>", mTradingPairID);

            mSellName = bundle.getString("sellName", "");
            mBuyName = bundle.getString("buyName", "");
            mSellCoinID = bundle.getString("sellCoinID", "");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mImmersionBar.statusBarDarkFont(false, 0.2f).init();
        startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        initView();
        bindTradingPair();

        EventBus.getDefault().register(this);

        startTimer();

        //先读取本地数据展示
        ExcTradingPairDetail tradingPairDetail = SPUtils.getObject(this, mTradingPairID + "klineheader");
        mAdapter.setTradingPair(tradingPairDetail);
        List<KLineEntity> dataList = SPUtils.getDataList(getApplicationContext(), mTradingPairID);
        ThreadUtils.getUiThreadHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.changeKLineData(mTradingPairID, dataList);
            }
        }, 1000);
    }

    private void sendWSocket(String client_id, String time) {
        String text = "";
        if (mTradingPairID.equals("46")){
            text = "{\"client_id\":\"" + client_id + "\",\"type\":\"" + 2 + "\",\"symbol\":\"" + mTradingPairID + "\",\"second\":\"" + time + "\"}";
        }else {
            text = "{\"client_id\":\"" + client_id + "\",\"type\":\"" + 5 + "\",\"symbol\":\"" + mTradingPairID + "\",\"step\":\"" + time + "\"}";
        }
        RxWebSocket.asyncSend(RURLConfig.WS_BASE_URL, text);
    }

    private void initView() {
        mAdapter = new TradingPairDetailAdapter(this);
        mAdapter.changeTradingPair(mTradingPairID, mSellName, mBuyName);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
    }

    private void bindTradingPair() {
        if (!TextUtils.isEmpty(mSellName) && !TextUtils.isEmpty(mBuyName)) {
            titleTextView.setText(mSellName + "/" + mBuyName);
        }
        if (mAdapter != null) {
            mAdapter.changeTradingPair(mTradingPairID, mSellName, mBuyName);
        }
        if (mDetailPresenter != null) {
            mDetailPresenter.dropView();
        }
        if (MMKVTools.isOpenWs() == 0) {
            fetchTradingPairDetail(false);//注释，换成socket
            fetchKLineData(false);//注释，换成socket
        }
        //币种简介
        fetchCoinIntroduction();
    }

    /*
     * 请求头部数据
     * */
    private void fetchTradingPairDetail(boolean isDelay) {
        if (mDetailPresenter == null) {
            mDetailPresenter = new TradingPairDetailPresenter(this);
        }
        mDetailPresenter.fetchTradingPairDetail(mTradingPairID, isDelay);
    }

    /*
     * 头部数据
     * */
    @Override
    public void bindTradingPairDetail(String tradingPairID, ExcTradingPairDetail tradingPairDetail) {
        if (tradingPairDetail == null) {//读取缓存数据
            tradingPairDetail = SPUtils.getObject(this, tradingPairID + "klineheader");
            mAdapter.setTradingPair(tradingPairDetail);
        } else {
            if (!TextUtils.isEmpty(mTradingPairID) && mTradingPairID.equals(tradingPairID)) {
                mAdapter.setTradingPair(tradingPairDetail);
                //缓存本地数据
                SPUtils.putObject(this, tradingPairID + "klineheader", tradingPairDetail);
            }
            //循环请求
            fetchTradingPairDetail(true);
        }
    }


    /**
     * 执行K线图中 1M ， 5M ， 15M ， 30M ， 60M ， 1D
     *
     * @param isDelay
     */
    private void fetchKLineData(boolean isDelay) {
        fetchKLineData(KLineCache.K_LINE_DATA_TIME_1MIN, isDelay);
        fetchKLineData(KLineCache.K_LINE_DATA_TIME_5MIN, isDelay);
        fetchKLineData(KLineCache.K_LINE_DATA_TIME_15MIN, isDelay);
        fetchKLineData(KLineCache.K_LINE_DATA_TIME_30MIN, isDelay);
        fetchKLineData(KLineCache.K_LINE_DATA_TIME_60MIN, isDelay);
        fetchKLineData(KLineCache.K_LINE_DATA_TIME_1DAY, isDelay);
//        fetchKLineData(KLineCache.K_LINE_DATA_TIME_1WEEK, isDelay);
//        fetchKLineData(KLineCache.K_LINE_DATA_TIME_1MON, isDelay);
    }

    /**
     * 分别请求6次 获取对应时间下的K线图
     *
     * @param time
     * @param isDelay
     */
    private void fetchKLineData(String time, boolean isDelay) {
        if (mDetailPresenter == null) {
            mDetailPresenter = new TradingPairDetailPresenter(this);
        }
        if (networkAvailable()) {
            mDetailPresenter.fetchKLineData(mTradingPairID, time, isDelay);
        }
    }

    private int tag = 0;

    /**
     * K线图获取结果回调
     *
     * @param tradingPairID
     * @param time
     * @param kLineData
     */
    @Override
    public void bindKLineData(String tradingPairID, String time, List<KLineEntity> kLineData) {
        if (kLineData != null && kLineData.size() > 0) {
            // 刚进来直接获取显示
            if (tag < 6) {
                mAdapter.changeKLineData(tradingPairID, time, kLineData);
                fetchKLineData(time, true);
                tag++;
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 在次获取6次 K线图数据
                        mAdapter.changeKLineData(tradingPairID, time, kLineData);
                        fetchKLineData(time, true);
                    }
                }, 5000);
            }
        } else {//设置缓存数据
            List<KLineEntity> dataList = SPUtils.getDataList(getApplicationContext(), mTradingPairID);
            mAdapter.changeKLineData(mTradingPairID, dataList);
        }
    }

    /*
     * 币种简介
     * */
    private void fetchCoinIntroduction() {
        if (mDetailPresenter == null) {
            mDetailPresenter = new TradingPairDetailPresenter(this);
        }
        if (networkAvailable()) {
            mDetailPresenter.fetchCoinIntroduction(mSellCoinID);
        }
    }

    /*
     * 币种简介
     * */
    @Override
    public void bindCoinIntroduction(ExcCoinIntroduction introduction) {
        mAdapter.setCoinIntroduction(introduction);
    }

    @OnClick({R.id.back_button, R.id.title_text_view, R.id.buy_button, R.id.sell_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_button:
                onBackPressed();
                break;
            case R.id.title_text_view://头部点击
                showTradingPairPicker();
                break;
            case R.id.buy_button:
                // 暂时关闭

                if (TextUtils.isEmpty(mTradingPairID) || TextUtils.isEmpty(mSellName) || TextUtils.isEmpty(mBuyName))
                    return;
                if (MMKVTools.hasBindPhone()) {
//                    if (MMKVTools.isOpenTrade()) {
                    ExchangeActivity.openExchangeActivity(this, mTradingPairID, mSellName, mBuyName, ExcExchangeContract.EXCHANGE_TYPE_BUY);
//                    }else showToast(getString(R.string.watting_for_));
                } else {
                    showToast(getString(R.string.bind_phone_));
                }
                break;
            case R.id.sell_button:
                if (TextUtils.isEmpty(mTradingPairID) || TextUtils.isEmpty(mSellName) || TextUtils.isEmpty(mBuyName)) {
                    return;
                }
                if (MMKVTools.hasBindPhone()) {
//                    if (MMKVTools.isOpenTrade()) {
                    ExchangeActivity.openExchangeActivity(this, mTradingPairID, mSellName, mBuyName, ExcExchangeContract.EXCHANGE_TYPE_SELL);
//                    }else showToast(getString(R.string.watting_for_));
                } else {
                    showToast(getString(R.string.bind_phone_));
                }
                break;
        }
    }

    /*
     * 头部点击
     * */
    private void showTradingPairPicker() {
        if (mTradingPairPicker == null) {
            mTradingPairPicker = new TradingPairPicker(this);
            mTradingPairPicker.setOnTradingPairSelectedListener(this);
            mTradingPairPicker.setBgColorType(1);
        }
        if (!mTradingPairPicker.isShowing()) {
            mTradingPairPicker.show();
        }
    }


    /**
     * K线图 顶部中间选择币种
     *
     * @param tradingPair
     */
    @Override
    public void onTradingPairSelected(ExcTradingPair tradingPair) {
        if (tradingPair != null) {
            mTradingPairID = tradingPair.getId();
            mSellName = tradingPair.getSellShortName();
            mBuyName = tradingPair.getBuyShortName();
            mSellCoinID = tradingPair.getSellCoinId();
            mAdapter.setCoinIntroduction(null);
            bindTradingPair();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getData(KLineData kLineData) {
        Log.e("测试", "getData: " + kLineData.getBigDecimal().size());
        List<List<BigDecimal>> data = kLineData.getBigDecimal();
        List<KLineEntity> kLineEntityList = new ArrayList<>();
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                List<BigDecimal> bigDecimalList = data.get(i);
                if (bigDecimalList != null && bigDecimalList.size() == 6) {
                    long date = bigDecimalList.get(0) != null ? bigDecimalList.get(0).longValue() : 0;
                    float open = bigDecimalList.get(1) != null ? bigDecimalList.get(1).floatValue() : 0;
                    float high = bigDecimalList.get(2) != null ? bigDecimalList.get(2).floatValue() : 0;
                    float low = bigDecimalList.get(3) != null ? bigDecimalList.get(3).floatValue() : 0;
                    float close = bigDecimalList.get(4) != null ? bigDecimalList.get(4).floatValue() : 0;
                    float vol = bigDecimalList.get(5) != null ? bigDecimalList.get(5).floatValue() : 0;
                    kLineEntityList.add(new KLineEntity(date, open, high, low, close, vol));
                }
            }
        }

        if (kLineEntityList != null && kLineEntityList.size() > 0) {
            mAdapter.changeKLineData(kLineData.getSymbol(), kLineData.getSecond(), kLineEntityList);
        } else {//设置缓存数据
            List<KLineEntity> dataList = SPUtils.getDataList(getApplicationContext(), mTradingPairID);
            mAdapter.changeKLineData(mTradingPairID, dataList);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getDepthData(ExcTradingPairDetail detail) {
        Log.e("测试", "getData: " + detail.getBuy());
        if (detail == null) {//读取缓存数据
            detail = SPUtils.getObject(this, mTradingPairID + "klineheader");
            mAdapter.setTradingPair(detail);
        } else {
            if (!TextUtils.isEmpty(mTradingPairID)) {
                mAdapter.setTradingPair(detail);
                //缓存本地数据
                SPUtils.putObject(this, mTradingPairID + "klineheader", detail);
            }
        }
    }

    /*
     * 轮询
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
                                Log.e("轮询", "count: " + count);
                                String client_id = MMKV.defaultMMKV().decodeString("client_id", "");

                                //请求头部数据
                                String text = "";
                                if (mTradingPairID.equals("46")){
                                    text = "{\"client_id\":\"" + client_id + "\",\"type\":\"3\",\"symbol\":\"" + mTradingPairID + "\"}";
                                }else {
                                    text = "{\"client_id\":\"" + client_id + "\",\"type\":\"6\",\"symbol\":\"" + mTradingPairID + "\",\"successcount\":\"100\"}";
                                }
                                RxWebSocket.asyncSend(RURLConfig.WS_BASE_URL, text);

                                //请求K线数据
                                sendWSocket(client_id, KLineCache.K_LINE_DATA_TIME_15MIN);
                                sendWSocket(client_id, KLineCache.K_LINE_DATA_TIME_1MIN);
                                sendWSocket(client_id, KLineCache.K_LINE_DATA_TIME_5MIN);
                                sendWSocket(client_id, KLineCache.K_LINE_DATA_TIME_30MIN);
                                sendWSocket(client_id, KLineCache.K_LINE_DATA_TIME_60MIN);
                                sendWSocket(client_id, KLineCache.K_LINE_DATA_TIME_1DAY);

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

    @Override
    protected void onDestroy() {
        if (mDetailPresenter != null) {
            mDetailPresenter.dropView();
        }
        stopTimer();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
