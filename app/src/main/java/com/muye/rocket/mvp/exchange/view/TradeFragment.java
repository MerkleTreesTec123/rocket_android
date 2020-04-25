package com.muye.rocket.mvp.exchange.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dhh.websocket.RxWebSocket;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_base.tools.NumberUtil;
import com.ifenduo.lib_base.widget.tablayout.TabEntity;
import com.muye.rocket.Constant;
import com.muye.rocket.R;
import com.muye.rocket.api.RURLConfig;
import com.muye.rocket.base.BaseFragment;
import com.muye.rocket.cache.ExcCache;
import com.muye.rocket.entity.exc_wallet.DealOperaEntity;
import com.muye.rocket.entity.exchange.ExcOrder;
import com.muye.rocket.entity.exchange.ExcOrderEntity;
import com.muye.rocket.entity.exchange.ExcTradingPair;
import com.muye.rocket.entity.exchange.ExcTradingPairBalance;
import com.muye.rocket.entity.exchange.ExcTradingPairDetail;
import com.muye.rocket.mvp.exchange.contract.ExcBalanceContract;
import com.muye.rocket.mvp.exchange.contract.ExcExchangeContract;
import com.muye.rocket.mvp.exchange.contract.ExcOrderContract;
import com.muye.rocket.mvp.exchange.presenter.ExcBalancePresenter;
import com.muye.rocket.mvp.exchange.presenter.ExcExchangePresenter;
import com.muye.rocket.mvp.exchange.presenter.ExcOrderPresenter;
import com.muye.rocket.tools.filter.NumberFilter;
import com.muye.rocket.widget.ExcHandicapView;
import com.muye.rocket.widget.TradingPairPicker;
import com.muye.rocket.widget.dialog.PayPasswordDialog;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/*
 * 交易
 * */
public class TradeFragment extends BaseFragment implements ExchangeAdapter.OnCancelButtonClickListener, OnTabSelectListener,
        PayPasswordDialog.InputPayPasswordCallBack, View.OnClickListener, ExcBalanceContract.View, ExcExchangeContract.View,
        TradingPairPicker.OnTradingPairSelectedListener, ExcOrderContract.View {
    Unbinder unbinder;

    @BindView(R.id.title_text_view)
    TextView titleTextView;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    String mTradingPairID = "";
    String mSellName = "CAT";
    String mBuyName = "";
    ExchangeAdapter mAdapter;
    int mExchangeType = ExcExchangeContract.EXCHANGE_TYPE_BUY;
    View mHeaderView;

    TextView buyButton;
    TextView sellButton;
    TextView balanceTextView;
    TextView balanceUnitTextView;
    TextView priceTextView;
    TextView priceTextView_;
    TextView rateButton25;
    TextView rateButton50;
    TextView rateButton75;
    TextView rateButton100;
    TextView amountTextView;
    Button submitButton;
    EditText priceEditText;
    EditText numEditText;
    TextView numUnitTextView;
    ImageButton lessButton;
    ImageButton addButton;
    LinearLayout sellContainer;
    LinearLayout buyContainer;
    CommonTabLayout tabLayout;
    LinearLayout priceEditContainer;
    LinearLayout numEditContainer;

    ExcTradingPairBalance mBalance;
    List<ExcOrder> mCurrentOrderList;
    List<ExcOrder> mHistoryOrderList;

    ExcExchangeContract.Presenter mExchangePresenter;
    ExcBalanceContract.Presenter mBalancePresenter;
    ExcOrderContract.Presenter mOrderPresenter;
    TradingPairPicker mTradingPairPicker;
    PayPasswordDialog mPasswordDialog;
    /*
     * 轮询
     * */
    Disposable disposable;
    private boolean tag = false;

    @Override
    public void onResume() {
        super.onResume();
        if (tag) {
            startTimer();
            tag = true;
            Log.e("交易", "可见1");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopTimer();
        Log.e("交易", "不可见1");
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mImmersionBar.statusBarDarkFont(true, 0.2f).init();
            startTimer();
            Log.e("交易", "可见");
            tag = true;
        } else {
            stopTimer();
            Log.e("交易", "不可见");
        }
    }

    public static TradeFragment newInstance() {
        Bundle args = new Bundle();
        args.putString("coinID", "");
        TradeFragment fragment = new TradeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getRootViewLayout() {
        return R.layout.fragment_trade;
    }

    @Override
    protected void initView(View parentView) {
        unbinder = ButterKnife.bind(this, parentView);

        EventBus.getDefault().register(this);

        String coinID = "";
        ExcTradingPair tradingPair = ExcCache.getTradingPairByCoinID(coinID);
        if (tradingPair != null) {
            mTradingPairID = tradingPair.getId();
            mSellName = tradingPair.getSellShortName();
            mBuyName = tradingPair.getBuyShortName();
        } else {
            mTradingPairID = "46";
            mSellName = "CAT";
            mBuyName = "USDT";
        }

    }

    @Override
    protected void initData() {
        initAdapter();
        recyclerView.setAdapter(mAdapter);
        changeExchangeType(mExchangeType);
        bindTradingPair();
        startTimer();
    }

    private void initAdapter() {
        initHeaderView();
        mAdapter = new ExchangeAdapter() {
            @Override
            View getHeaderView() {
                return mHeaderView;
            }

            @Override
            View getEmptyView() {
                View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.exc_empty_exchange, recyclerView, false);
                /*ViewGroup.LayoutParams layoutParams = emptyView.getLayoutParams();
                layoutParams.height = recyclerView.getHeight() - mHeaderView.getHeight();
                emptyView.setMinimumHeight(DimensionTool.dp2px(getContext(), 170));
                emptyView.setLayoutParams(layoutParams);*/
                return emptyView;
            }
        };
        mAdapter.setOnCancelButtonClickListener(this);
    }

    private void initHeaderView() {
        if (mHeaderView == null) {
            mHeaderView = LayoutInflater.from(getContext()).inflate(R.layout.exc_header_exchange, recyclerView, false);
            buyButton = mHeaderView.findViewById(R.id.buy_text_view);
            sellButton = mHeaderView.findViewById(R.id.sell_text_view);
            balanceTextView = mHeaderView.findViewById(R.id.balance_text_view);
            balanceUnitTextView = mHeaderView.findViewById(R.id.balance_unit_text_view);
            priceTextView = mHeaderView.findViewById(R.id.price_text_view);
            priceTextView_ = mHeaderView.findViewById(R.id.price_text_view_);
            rateButton25 = mHeaderView.findViewById(R.id.rate_text_view_25);
            rateButton50 = mHeaderView.findViewById(R.id.rate_text_view_50);
            rateButton75 = mHeaderView.findViewById(R.id.rate_text_view_75);
            rateButton100 = mHeaderView.findViewById(R.id.rate_text_view_100);
            amountTextView = mHeaderView.findViewById(R.id.amount_text_view);
            submitButton = mHeaderView.findViewById(R.id.submit_button);
            priceEditText = mHeaderView.findViewById(R.id.price_edit_text);
            numEditText = mHeaderView.findViewById(R.id.num_edit_text);
            numUnitTextView = mHeaderView.findViewById(R.id.num_unit_text_view);
            lessButton = mHeaderView.findViewById(R.id.less_button);
            addButton = mHeaderView.findViewById(R.id.add_button);
            sellContainer = mHeaderView.findViewById(R.id.sell_container);
            buyContainer = mHeaderView.findViewById(R.id.buy_container);
            tabLayout = mHeaderView.findViewById(R.id.tab_layout);
            priceEditContainer = mHeaderView.findViewById(R.id.price_edit_container);
            numEditContainer = mHeaderView.findViewById(R.id.num_edit_container);

            lessButton.setOnClickListener(this);
            addButton.setOnClickListener(this);
            buyButton.setOnClickListener(this);
            sellButton.setOnClickListener(this);
            submitButton.setOnClickListener(this);
            rateButton25.setOnClickListener(this);
            rateButton50.setOnClickListener(this);
            rateButton75.setOnClickListener(this);
            rateButton100.setOnClickListener(this);
            numEditText.setFilters(new InputFilter[]{new NumberFilter()});
            priceEditText.setFilters(new InputFilter[]{new NumberFilter()});
            setupTabLayout();

            priceEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (priceEditContainer != null) {
                        priceEditContainer.setBackgroundResource(hasFocus ? R.drawable.exc_shape_00da97_frame_radius_2
                                : R.drawable.exc_shape_f1f1f1_frame_radius_2);
                    }
                }
            });

            numEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (numEditContainer != null) {
                        numEditContainer.setBackgroundResource(hasFocus ? R.drawable.exc_shape_00da97_frame_radius_2
                                : R.drawable.exc_shape_f1f1f1_frame_radius_2);
                    }
                }
            });

            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    calculationAmount();
                }
            };

            numEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_NEXT) {
                        return true;
                    }
                    return false;
                }
            });
            priceEditText.addTextChangedListener(textWatcher);
            numEditText.addTextChangedListener(textWatcher);
        }
    }

    private void setupTabLayout() {
        ArrayList<CustomTabEntity> tabEntityList = new ArrayList<>();
        tabEntityList.add(new TabEntity(getString(R.string.exc_current_commission)));
        tabEntityList.add(new TabEntity(getString(R.string.exc_historical_commission)));
        if (tabLayout != null) {
            tabLayout.setTabData(tabEntityList);
            tabLayout.setOnTabSelectListener(this);
        }
    }

    private double calculationAmount() {
        double amount = 0;
        if (priceEditText != null && numEditText != null && amountTextView != null) {
            String price = priceEditText.getText().toString();
            String num = numEditText.getText().toString();
            if (!TextUtils.isEmpty(price) && !TextUtils.isEmpty(num)) {
                BigDecimal priceBD = new BigDecimal(NumberUtil.formatMoney(price));
                BigDecimal numBD = new BigDecimal(NumberUtil.formatMoney(num));
                amount = priceBD.multiply(numBD).doubleValue();
                if (!TextUtils.isEmpty(mBuyName)) {
                    amountTextView.setText(NumberUtil.formatMoney(amount) + " " + mBuyName);
                } else {
                    amountTextView.setText(NumberUtil.formatMoney(amount));
                }
            } else {
                amountTextView.setText("--");
            }
        }
        return amount;
    }

    /**
     * 选择交易类型
     *
     * @param exchangeType
     */
    private void changeExchangeType(@ExcExchangeContract.ExchangeType int exchangeType) {
        mExchangeType = exchangeType;
        if (buyButton != null) {
            buyButton.setSelected(exchangeType != ExcExchangeContract.EXCHANGE_TYPE_SELL);
        }

        if (sellButton != null) {
            sellButton.setSelected(exchangeType == ExcExchangeContract.EXCHANGE_TYPE_SELL);
        }

        if (submitButton != null) {
            submitButton.setText(exchangeType == ExcExchangeContract.EXCHANGE_TYPE_SELL ? R.string.exc_sell : R.string.exc_buy);
            submitButton.setBackgroundResource(exchangeType == ExcExchangeContract.EXCHANGE_TYPE_SELL ?
                    R.drawable.exc_shape_f3752e_e95602_radius_2 : R.drawable.exc_shape_00da97_04d192_radius_2);
        }

        bindBalance(mBalance);
    }

    /**
     * 绑定交易对
     */
    private void bindTradingPair() {
        String buyName = "--";
        String sellName = "--";
        if (!TextUtils.isEmpty(mTradingPairID) && !TextUtils.isEmpty(mBuyName) && !TextUtils.isEmpty(mSellName)) {
            buyName = mBuyName;
            sellName = mSellName;
        }
        //标题
        titleTextView.setText(sellName + "/" + buyName);

        //输入框数量的单位
        if (numUnitTextView != null) {
            numUnitTextView.setText(sellName);
        }

        //余额的单位
        if (balanceUnitTextView != null) {
            balanceUnitTextView.setText(mExchangeType == ExcExchangeContract.EXCHANGE_TYPE_SELL ? sellName : buyName);
        }

        //清空订单列表
        mCurrentOrderList = null;
        mHistoryOrderList = null;
        notifyDataSetChanged(sellName, buyName);

        //余额致空
        bindBalance(null);
        if (balanceTextView != null) {
            balanceTextView.setText("--");
        }

        //清空输入框
        if (numEditText != null) {
            numEditText.setText("");
        }
        if (priceEditText != null) {
            priceEditText.setText("");
        }

        //重新获取数据
        if (mExchangePresenter != null) {
            mExchangePresenter.dropView();
        }

        if (MMKVTools.isOpenWs() == 0) {
            fetchTradingPairDetail();//获取右侧数据换成socket
        }
        fetchBalance(false);
        fetchOrder(ExcExchangeContract.EXC_ORDER_TYPE_ALL, false);
    }

    private void submitExchange() {
        if (TextUtils.isEmpty(mTradingPairID) || TextUtils.isEmpty(mSellName) || TextUtils.isEmpty(mBuyName))
            return;
        String price = priceEditText.getText().toString();
        String num = numEditText.getText().toString();
        if (TextUtils.isEmpty(price)) {
            showToast(getString(R.string.exc_please_enter_the_price));
            return;
        }
        if (TextUtils.isEmpty(num)) {
            showToast(getString(R.string.exc_please_enter_quantity));
            return;
        }

        double price_ = new BigDecimal(NumberUtil.formatMoney(price)).doubleValue();
        double num_ = new BigDecimal(NumberUtil.formatMoney(num)).doubleValue();
        if (price_ <= 0) {
            showToast(getString(R.string.exc_price_must_be_greater_than_0));
            return;
        }
        if (num_ <= 0) {
            showToast(getString(R.string.exc_quantity_must_be_greater_than_0));
            return;
        }
        if (mBalance == null) {
            showToast(getString(R.string.exc_insufficient_balance));
            return;
        }
        if (mExchangeType == ExcExchangeContract.EXCHANGE_TYPE_SELL) {
            if (mBalance.getSellCoin() == null || mBalance.getSellCoin().getTotal() < num_) {
                showToast(getString(R.string.exc_insufficient_balance));
                return;
            }
        } else {
            if (mBalance.getBuyCoin() == null || mBalance.getBuyCoin().getTotal() < (price_ * num_)) {
                showToast(getString(R.string.exc_insufficient_balance));
                return;
            }
        }
        if (TextUtils.isEmpty(Constant.PAY_PASSWORD)) {
            showPasswordDialog();   // 买 弹框输入密码
        } else {
            submitExchange(Constant.PAY_PASSWORD); // 卖
        }
    }

    //请求是否可以操作
    private void requestIsDeal() {
        if (mOrderPresenter == null) {
            mBalancePresenter = new ExcBalancePresenter(this);
        }
        mBalancePresenter.fetchDealRealt();
    }

    //提交买卖接口
    private void submitExchange(String password) {
        String price = NumberUtil.formatMoney(priceEditText.getText().toString());
        String num = NumberUtil.formatMoney(numEditText.getText().toString());
        if (mExchangePresenter == null) {
            mExchangePresenter = new ExcExchangePresenter(this);
        }
        mExchangePresenter.submitExchange(mSellName, mBuyName, num, price, mExchangeType, password);
    }

    private void showTradingPairPicker() {
        if (mTradingPairPicker == null) {
            mTradingPairPicker = new TradingPairPicker(getContext());
            mTradingPairPicker.setOnTradingPairSelectedListener(this);
        }
        if (!mTradingPairPicker.isShowing()) {
            mTradingPairPicker.show();
        }
    }

    /**
     * 获取买 / 卖界面的右侧上下两个区域数据
     * 第一次请求 直接进行，后面进行2 秒延迟请求一次
     */
    private void fetchTradingPairDetail() {
        if (mExchangePresenter == null) {
            mExchangePresenter = new ExcExchangePresenter(this);
        }
        if (TextUtils.isEmpty(mTradingPairID)) return;
        mExchangePresenter.fetchTradingPairDetail(mTradingPairID);
    }

    private void fetchOrder(@ExcExchangeContract.ExcOrderType String type, boolean isDelay) {
        if (mOrderPresenter == null) {
            mOrderPresenter = new ExcOrderPresenter(this);
        } else {
            mOrderPresenter.dropView();
        }
        mOrderPresenter.fetchOrder(mSellName, mBuyName, type, isDelay);
    }

    private void fetchBalance(boolean isDelay) {
        if (mBalancePresenter == null) {
            mBalancePresenter = new ExcBalancePresenter(this);
        } else {
            mBalancePresenter.dropView();
        }
        mBalancePresenter.fetchBalance(mTradingPairID, isDelay);
    }

    private void setupSellExcHandicap(ExcTradingPairDetail tradingPairDetail) {
        if (sellContainer.getChildCount() == 0) {
            for (int i = 0; i < 5; i++) {
                ExcHandicapView handicapView = createHandicapView();
                handicapView.setNumTextColor(getResources().getColor(R.color.excColor9298B3));
                handicapView.setPriceTextColor(getResources().getColor(R.color.excColorF95D04));
                handicapView.setProgressColor(Color.parseColor("#26f95d04"));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                layoutParams.weight = 1;
                sellContainer.addView(handicapView, layoutParams);
            }
        }

        for (int i = 0; i < sellContainer.getChildCount(); i++) {
            ExcTradingPairDetail.DepthItemEntity entity = null;
            if (tradingPairDetail != null && tradingPairDetail.getAskDepthItemEntity() != null && i < tradingPairDetail.getAskDepthItemEntity().size()) {
                List<ExcTradingPairDetail.DepthItemEntity> depthItemEntityList = tradingPairDetail.getAskDepthItemEntity();
                entity = depthItemEntityList.get(i);
            }

            View childView = sellContainer.getChildAt(i);
            if (childView instanceof ExcHandicapView) {
                ((ExcHandicapView) childView).bindData(entity);
            }
        }
    }

    private ExcHandicapView createHandicapView() {
        ExcHandicapView handicapView = new ExcHandicapView(getContext());
        handicapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = NumberUtil.formatMoney(handicapView.getNum());
                String price = NumberUtil.formatMoney(handicapView.getPrice());
                numEditText.setText(num);
                priceEditText.setText(price);
                if (numEditText.hasFocus()) {
                    numEditText.setSelection(num.length());
                }
                if (priceEditText.hasFocus()) {
                    priceEditText.setSelection(price.length());
                }
            }
        });
        return handicapView;
    }

    private void setupBuyExcHandicap(ExcTradingPairDetail tradingPairDetail) {
        if (buyContainer.getChildCount() == 0) {
            for (int i = 0; i < 5; i++) {
                ExcHandicapView handicapView = createHandicapView();
                handicapView.setNumTextColor(getResources().getColor(R.color.excColor9298B3));
                handicapView.setPriceTextColor(getResources().getColor(R.color.excColor00DA97));
                handicapView.setProgressColor(Color.parseColor("#2600DA97"));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                layoutParams.weight = 1;
                buyContainer.addView(handicapView, layoutParams);
            }
        }

        for (int i = 0; i < buyContainer.getChildCount(); i++) {
            ExcTradingPairDetail.DepthItemEntity entity = null;
            if (tradingPairDetail != null && tradingPairDetail.getBidDepthItemEntity() != null && i < tradingPairDetail.getBidDepthItemEntity().size()) {
                List<ExcTradingPairDetail.DepthItemEntity> depthItemEntityList = tradingPairDetail.getBidDepthItemEntity();
                entity = depthItemEntityList.get(i);
            }

            View childView = buyContainer.getChildAt(i);
            if (childView instanceof ExcHandicapView) {
                ((ExcHandicapView) childView).bindData(entity);
            }
        }
    }

    private void bindBalance(ExcTradingPairBalance balance) {
        mBalance = balance;
        if (mExchangeType == ExcExchangeContract.EXCHANGE_TYPE_BUY) {
            if (mBalance != null && mBalance.getBuyCoin() != null) {
                balanceTextView.setText(NumberUtil.formatRMBDown(mBalance.getBuyCoin().getTotal()));
            } else {
                balanceTextView.setText("--");
            }
        } else {
            if (mBalance != null && mBalance.getSellCoin() != null) {
                balanceTextView.setText(NumberUtil.formatRMBDown(mBalance.getSellCoin().getTotal()));
            } else {
                balanceTextView.setText("--");
            }
        }

        //单位
        if (!TextUtils.isEmpty(mTradingPairID) && !TextUtils.isEmpty(mBuyName) && !TextUtils.isEmpty(mSellName) && balanceUnitTextView != null) {
            balanceUnitTextView.setText(mExchangeType == ExcExchangeContract.EXCHANGE_TYPE_SELL ? mSellName : mBuyName);
        } else {
            balanceUnitTextView.setText("--");
        }
    }

    private void notifyDataSetChanged(String sellName, String buyName) {
        if (tabLayout != null) {
            if (tabLayout.getCurrentTab() == 1) {
                mAdapter.setData(mHistoryOrderList, sellName, buyName);
            } else {
                mAdapter.setData(mCurrentOrderList, sellName, buyName);
            }
        }
    }

    private void showPasswordDialog() {
        if (mPasswordDialog == null) {
            mPasswordDialog = new PayPasswordDialog(getContext());
            mPasswordDialog.setInputPayPasswordCallBack(this);
        }
        if (!mPasswordDialog.isShowing()) {
            mPasswordDialog.show();
        }
    }

    @Override
    public void oCancelButtonClick(String orderID) {
        if (mOrderPresenter == null) {
            mOrderPresenter = new ExcOrderPresenter(this);
        }
        mOrderPresenter.cancelOrder(orderID);
    }

    @Override
    public void onTabSelect(int position) {
        notifyDataSetChanged(mSellName, mBuyName);
    }

    @Override
    public void onTabReselect(int position) {

    }

    @Override
    public void getPayPassword(String payPassword) {
        submitExchange(payPassword);
    }

    @Override
    public void bindBalance(String tradingPairID, ExcTradingPairBalance balance) {
        if (!TextUtils.isEmpty(mTradingPairID) && mTradingPairID.equals(tradingPairID)) {
            bindBalance(balance);
        }

        fetchBalance(true);
    }

    //是否可以买卖操作
    @Override
    public void isDeal(DealOperaEntity operaEntity) {
        if (operaEntity != null) {
            if (operaEntity.getBuy() == 1) {  // 1 表示可以买
                submitExchange();
            } else if (operaEntity.getBuy() == 0) { // 0 表示停止买卖
                showToast(operaEntity.getBuy_msg());
            }
            if (operaEntity.getSell() == 1) { // 1 表示可以卖
                submitExchange();
            } else if (operaEntity.getSell() == 0) { // 0 表示停止买卖
                showToast(operaEntity.getSell_msg());
            }
        }
    }

    @Override
    public void bindTradingPairDetail(String tradingPairID, ExcTradingPairDetail tradingPairDetail) {
        if (!TextUtils.isEmpty(mTradingPairID) && mTradingPairID.equals(tradingPairID)) {
            if (tradingPairDetail != null) {
                if (priceTextView != null) {
                    if (NumberUtil.string2Double(tradingPairDetail.getChg()) < 0) {
                        priceTextView.setTextColor(getResources().getColor(R.color.excColorF95D04));
                    } else {
                        priceTextView.setTextColor(getResources().getColor(R.color.excColor00DA97));
                    }
                    priceTextView.setText(NumberUtil.formatMoney(tradingPairDetail.getP_new()));
                }

                if (priceTextView_ != null) {
                    double price = new BigDecimal(tradingPairDetail.getP_new()).doubleValue();
                    double buyCny = ExcCache.getRMBPriceCache(mBuyName);
                    priceTextView_.setText("≈" + NumberUtil.formatMoney(price * buyCny) + "CNY");
                }
            } else {
                if (priceTextView != null) {
                    priceTextView.setText("--");
                    priceTextView.setTextColor(getResources().getColor(R.color.excColor00DA97));
                }

                if (priceTextView_ != null) {
                    priceTextView_.setText("--");
                }
            }

            setupBuyExcHandicap(tradingPairDetail);
            setupSellExcHandicap(tradingPairDetail);
        }

        fetchTradingPairDetail();
    }

    @Override
    public void onSubmitExchangeSuccess(String sellName, String buyName) {
        if (!TextUtils.isEmpty(sellName) || !TextUtils.isEmpty(buyName)){
            showToast(getString(R.string.exc_exchange_submit_success));
            numEditText.setText("");
            priceEditText.setText("");
            fetchOrder(ExcExchangeContract.EXC_ORDER_TYPE_ALL, false);
            fetchBalance(false);
        }
    }

    @Override
    public void onTradingPairSelected(ExcTradingPair tradingPair) {
        if (tradingPair != null && !TextUtils.isEmpty(tradingPair.getId()) && !TextUtils.isEmpty(tradingPair.getBuyShortName())
                && !TextUtils.isEmpty(tradingPair.getSellShortName())) {
            mTradingPairID = tradingPair.getId();
            mSellName = tradingPair.getSellShortName();
            mBuyName = tradingPair.getBuyShortName();
            bindTradingPair();
        }
    }

    @Override
    public void bindOrder(String sellName, String buyName, ExcOrderEntity orderEntity, String type) {
        if (TextUtils.isEmpty(mSellName) || TextUtils.isEmpty(mBuyName) || !mSellName.equals(sellName) || !mBuyName.equals(buyName)) {
            return;
        }
        if (ExcExchangeContract.EXC_ORDER_TYPE_HISTORY.equals(type)) {
            mHistoryOrderList = orderEntity == null ? null : orderEntity.getEntrutsHis();
        } else if (ExcExchangeContract.EXC_ORDER_TYPE_CURRENT.equals(type)) {
            mCurrentOrderList = orderEntity == null ? null : orderEntity.getEntrutsCur();
        } else {
            mCurrentOrderList = orderEntity == null ? null : orderEntity.getEntrutsCur();
            mHistoryOrderList = orderEntity == null ? null : orderEntity.getEntrutsHis();
        }
        notifyDataSetChanged(sellName, buyName);

        fetchOrder(ExcExchangeContract.EXC_ORDER_TYPE_ALL, true);
    }

    @Override
    public void onCancelOrderSuccess() {
        showToast(getString(R.string.exc_cancel_order_success));
        fetchOrder(ExcExchangeContract.EXC_ORDER_TYPE_ALL, false);
        fetchBalance(false);
    }

    @OnClick({R.id.title_text_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_text_view:
                showTradingPairPicker();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buy_text_view:
                // 暂时关闭
//                if (MMKVTools.isOpenTrade()){
                changeExchangeType(ExcExchangeContract.EXCHANGE_TYPE_BUY);
//                }else {
//                    showToast(getString(R.string.watting_for_));
//                }
                break;
            case R.id.sell_text_view:
                // 暂时关闭
//                if (MMKVTools.isOpenTrade()){
                changeExchangeType(ExcExchangeContract.EXCHANGE_TYPE_SELL);
//                }else {
//                    showToast(getString(R.string.watting_for_));
//                }
                break;
            case R.id.rate_text_view_25:
                if (numEditText == null) return;
                if (mBalance != null && mBalance.getSellCoin() != null) {
                    double balance = new BigDecimal(NumberUtil.formatMoneyDown(mBalance.getSellCoin().getTotal())).doubleValue();
                    String num = NumberUtil.formatMoney(balance * 0.25);
                    numEditText.setText(num);
                    numEditText.setSelection(num.length());
                } else {
                    numEditText.setText("");
                }
                break;
            case R.id.rate_text_view_50:
                if (numEditText == null) return;
                if (mBalance != null && mBalance.getSellCoin() != null) {
                    double balance = new BigDecimal(NumberUtil.formatMoneyDown(mBalance.getSellCoin().getTotal())).doubleValue();
                    String num = NumberUtil.formatMoney(balance * 0.5);
                    numEditText.setText(num);
                    numEditText.setSelection(num.length());
                } else {
                    numEditText.setText("");
                }
                break;
            case R.id.rate_text_view_75:
                if (numEditText == null) return;
                if (mBalance != null && mBalance.getSellCoin() != null) {
                    double balance = new BigDecimal(NumberUtil.formatMoneyDown(mBalance.getSellCoin().getTotal())).doubleValue();
                    String num = NumberUtil.formatMoney(balance * 0.75);
                    numEditText.setText(num);
                    numEditText.setSelection(num.length());
                } else {
                    numEditText.setText("");
                }
                break;
            case R.id.rate_text_view_100:
                if (numEditText == null) return;
                if (mBalance != null && mBalance.getSellCoin() != null) {
                    String balance_ = NumberUtil.formatMoneyDown(mBalance.getSellCoin().getTotal());
                    numEditText.setText(balance_);
                    numEditText.setSelection(balance_.length());
                } else {
                    numEditText.setText("");
                }
                break;
            case R.id.submit_button:
                if (MMKVTools.hasBindPhone()) {


                    // 暂时关闭
//                if (MMKVTools.isOpenTrade()){
                    if (mSellName.equals("CAT")) {
                        requestIsDeal();  // 请求是否可以操作买卖
                    } else showToast(getString(R.string.watting_for_));
//                    submitExchange();  // 关闭直接点击进行买卖操作，通过接口进行判断
//                }else {
//                    showToast(getString(R.string.watting_for_));
//                }
                } else {
                    showToast(getString(R.string.bind_phone_));
                }
                break;
            case R.id.less_button:
                String priceStr = NumberUtil.formatMoney(priceEditText.getText().toString());
                double price = new BigDecimal(priceStr).doubleValue();
                if (price > 0.0001) {
                    String priceStr_ = NumberUtil.formatMoney(price - 0.0001);
                    priceEditText.setText(priceStr_);
                    priceEditText.setSelection(priceStr_.length());
                }
                break;
            case R.id.add_button:
                String addPriceStr = NumberUtil.formatMoney(priceEditText.getText().toString());
                double addPrice = new BigDecimal(addPriceStr).doubleValue();
                String addPriceStr_ = NumberUtil.formatMoney(addPrice + 0.0001);
                priceEditText.setText(addPriceStr_);
                priceEditText.setSelection(addPriceStr_.length());
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getRightData(ExcTradingPairDetail detail) {
        Log.e("测试", "getData: " + detail.getBuy());
        if (!TextUtils.isEmpty(mTradingPairID) && mTradingPairID.equals(detail.getTradeId())) {
            if (detail != null) {
                if (priceTextView != null) {
                    if (NumberUtil.string2Double(detail.getChg()) < 0) {
                        priceTextView.setTextColor(getResources().getColor(R.color.excColorF95D04));
                    } else {
                        priceTextView.setTextColor(getResources().getColor(R.color.excColor00DA97));
                    }
                    priceTextView.setText(NumberUtil.formatMoney(detail.getP_new()));
                }

                if (priceTextView_ != null) {
                    double price = new BigDecimal(detail.getP_new()).doubleValue();
                    double buyCny = ExcCache.getRMBPriceCache(mBuyName);
                    priceTextView_.setText("≈" + NumberUtil.formatMoney(price * buyCny) + "CNY");
                }
            } else {
                if (priceTextView != null) {
                    priceTextView.setText("--");
                    priceTextView.setTextColor(getResources().getColor(R.color.excColor00DA97));
                }

                if (priceTextView_ != null) {
                    priceTextView_.setText("--");
                }
            }

            setupBuyExcHandicap(detail);
            setupSellExcHandicap(detail);
        }
    }

    /*
     * 轮询
     * */
    private void startTimer() {
        if (MMKVTools.isOpenWs() == 1){
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
                                String type = mTradingPairID.equals("46") ? "4" : "6";
                                String text = "{\"client_id\":\"" + client_id + "\",\"type\":\"" + type + "\",\"symbol\":\"" + mTradingPairID + "\",\"successcount\":\"100\"}";
                                if (tag){
                                    RxWebSocket.asyncSend(RURLConfig.WS_BASE_URL, text);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mExchangePresenter != null) {
            mExchangePresenter.dropView();
        }
        if (mOrderPresenter != null) {
            mOrderPresenter.dropView();
        }
        if (mBalancePresenter != null) {
            mBalancePresenter.dropView();
        }
        stopTimer();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }
}
