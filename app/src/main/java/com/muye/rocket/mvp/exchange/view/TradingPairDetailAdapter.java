package com.muye.rocket.mvp.exchange.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.github.tifezh.kchartlib.chart.KChartView;
import com.github.tifezh.kchartlib.chart.formatter.DateFormatter;
import com.ifenduo.common.adapter.base.ViewHolder;
import com.ifenduo.lib_base.tools.LanguageUtil;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_base.tools.NumberUtil;
import com.ifenduo.lib_base.widget.tablayout.TabEntity;
import com.muye.rocket.R;
import com.muye.rocket.cache.KLineCache;
import com.muye.rocket.entity.exchange.DepthDataBean;
import com.muye.rocket.entity.exchange.ExcCoinIntroduction;
import com.muye.rocket.entity.exchange.ExcTradingPairDetail;
import com.muye.rocket.entity.exchange.KLineEntity;
import com.muye.rocket.entity.exchange.TradesBean;

import java.util.ArrayList;
import java.util.List;

public class TradingPairDetailAdapter extends RecyclerView.Adapter<ViewHolder> {

    private static final int ITEM_TYPE_HEADER = 1;
    private static final int ITEM_TYPE_RECORD = 2;
    private static final int ITEM_TYPE_INTRODUCTION = 3;
    private static final int ITEM_TYPE_DEPTH = 4;

    private Context mContext;
    private String mTradingPairID;
    private String mSellName;
    private String mBuyName;
    private ExcTradingPairDetail mTradingPair;
    private ExcCoinIntroduction mCoinIntroduction;

    private HeaderHolder mHeaderHolder;
    private IntroductionHolder mIntroductionHolder;
    private DepthHolder mDepthHolder;

    public TradingPairDetailAdapter(Context context) {
        this.mContext = context;
    }

    public void changeTradingPair(String tradingPairID, String sellName, String buyName) {
        mTradingPairID = tradingPairID;
        mSellName = sellName;
        mBuyName = buyName;
        if (mHeaderHolder != null) {
            mHeaderHolder.kChartAdapter.setDatas(new ArrayList<>());
            mTradingPair = null;
            mHeaderHolder.bindTradingPair();
        }
        notifyDataSetChanged();
    }

    public void changeKLineData(String tradingPairID, String time, List<KLineEntity> data) {
        KLineCache.saveKLineData(tradingPairID, time, data,mContext);
        if (mHeaderHolder != null) {
            if (mHeaderHolder.getTime().equals(time) && !TextUtils.isEmpty(tradingPairID) && tradingPairID.equals(mTradingPairID)) {
                mHeaderHolder.bindKLineData();
            }
        } else {
            notifyDataSetChanged();
        }
    }

    public void changeKLineData(String tradingPairID,List<KLineEntity> data){
        if (mHeaderHolder != null){
            if (mHeaderHolder.getTime().equals(KLineCache.K_LINE_DATA_TIME_15MIN) && tradingPairID.equals(mTradingPairID)){
                mHeaderHolder.bindKLineData2(data);
            }
        }else {
            notifyDataSetChanged();
        }
    }

    public void setTradingPair(ExcTradingPairDetail tradingPairDetail) {
        this.mTradingPair = tradingPairDetail;
        if (mHeaderHolder != null) {
            mHeaderHolder.bindTradingPair();
        }

        if (mDepthHolder != null) {
            mDepthHolder.bindData();
        }
        notifyDataSetChanged();
    }

    public void setCoinIntroduction(ExcCoinIntroduction introduction) {
        mCoinIntroduction = introduction;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int itemType) {
        ViewHolder holder = null;
        if (itemType == ITEM_TYPE_HEADER) {
            if (mHeaderHolder == null) {
                View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.exc_header_trading_pair_detail, viewGroup, false);
                mHeaderHolder = new HeaderHolder(viewGroup, viewGroup.getContext(), itemView);
            }
            holder = mHeaderHolder;
        } else if (itemType == ITEM_TYPE_RECORD) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.exc_item_trading_pair_detail, viewGroup, false);
            holder = new RecordHolder(viewGroup.getContext(), itemView);
        } else if (itemType == ITEM_TYPE_INTRODUCTION) {
            if (mIntroductionHolder == null) {
                View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.exc_item_introduction, viewGroup, false);
                mIntroductionHolder = new IntroductionHolder(viewGroup.getContext(), itemView);
            }
            holder = mIntroductionHolder;
        } else if (itemType == ITEM_TYPE_DEPTH) {
            if (mDepthHolder == null) {
                View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.exc_item_depth, viewGroup, false);
                mDepthHolder = new DepthHolder(viewGroup.getContext(), viewGroup, itemView);
            }
            holder = mDepthHolder;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder instanceof HeaderHolder) {
            ((HeaderHolder) holder).bindTradingPair();
            ((HeaderHolder) holder).bindKLineData();
            ((HeaderHolder) holder).recordTitleContainer.setVisibility(((HeaderHolder) holder).contentTabLayout.getCurrentTab() == 1 ? View.VISIBLE : View.GONE);
            ((HeaderHolder) holder).volHintTextView.setText(String.format(mContext.getString(R.string.exc_price_), mBuyName));
            ((HeaderHolder) holder).priceHintTextView.setText(String.format(mContext.getString(R.string.exc_quantity_), mSellName));
        } else if (holder instanceof RecordHolder) {
            TradesBean tradesBean = null;
            if (position > 0 && mTradingPair != null && mTradingPair.getTrades() != null && mTradingPair.getTrades().size() > position - 1) {
                tradesBean = mTradingPair.getTrades().get(position - 1);
            }
            if (tradesBean != null) {
                if (MMKVTools.getLanguage().equals(LanguageUtil.ENGLISH)) {
                    holder.setText(R.id.type_text_view, tradesBean.getEn_type());
                } else {
                    holder.setText(R.id.type_text_view, tradesBean.getType());
                }

                if ("ask".equals(tradesBean.getEn_type()) || "卖出".equals(tradesBean.getType())) {
                    holder.setTextColorRes(R.id.type_text_view, R.color.excColorF95D04);
                } else {
                    holder.setTextColorRes(R.id.type_text_view, R.color.excColor00DA97);
                }

                holder.setText(R.id.time_text_view, tradesBean.getTime());
                holder.setText(R.id.price_text_view, NumberUtil.formatMoney(tradesBean.getPrice()));
                holder.setText(R.id.num_text_view, NumberUtil.formatMoney(tradesBean.getAmount()));
            } else {
                holder.setText(R.id.time_text_view, "--");
                holder.setText(R.id.type_text_view, "--");
                holder.setText(R.id.price_text_view, "--");
                holder.setText(R.id.num_text_view, "--");
                holder.setTextColorRes(R.id.type_text_view, R.color.excColorF95D04);
            }

        } else if (holder instanceof DepthHolder) {
            ((DepthHolder) holder).bindData();
        } else if (holder instanceof IntroductionHolder) {
            ((IntroductionHolder) holder).bindData();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_HEADER;
        } else {
            if (mHeaderHolder != null) {
                if (mHeaderHolder.contentTabLayout.getCurrentTab() == 2) {
                    return ITEM_TYPE_INTRODUCTION;
                } else if (mHeaderHolder.contentTabLayout.getCurrentTab() == 1) {
                    return ITEM_TYPE_RECORD;
                } else {
                    return ITEM_TYPE_DEPTH;
                }
            } else {
                return ITEM_TYPE_DEPTH;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mHeaderHolder != null && mHeaderHolder.contentTabLayout.getCurrentTab() == 1) {
            if (mTradingPair != null && mTradingPair.getTrades() != null && mTradingPair.getTrades().size() > 0) {
                return 1 + mTradingPair.getTrades().size();
            } else {
                return 1;
            }
        } else {
            return 2;
        }
    }


    class HeaderHolder extends ViewHolder {
        KChartView kChartView;
        CommonTabLayout timeTabLayout;
        CommonTabLayout contentTabLayout;
        TextView priceTextView;
        TextView gainTextView;
        TextView highestTextView;
        TextView volTextView;
        TextView lowestTextView;
        LinearLayout recordTitleContainer;
        TextView volHintTextView;
        TextView priceHintTextView;
        KChartAdapter kChartAdapter;
        ViewGroup viewGroup;

        public HeaderHolder(ViewGroup recyclerView, Context context, View itemView) {
            super(context, itemView);
            viewGroup = recyclerView;
            timeTabLayout = getView(R.id.tab_layout);
            kChartView = getView(R.id.k_chart_view);
            contentTabLayout = getView(R.id.tab_layout_);
            priceTextView = getView(R.id.price_text_view);
            gainTextView = getView(R.id.gain_text_view);
            highestTextView = getView(R.id.highest_text_view);
            volTextView = getView(R.id.num_text_view);
            lowestTextView = getView(R.id.lowest_text_view);
            recordTitleContainer = getView(R.id.title_container);
            volHintTextView = getView(R.id.vol_hint_text_view);
            priceHintTextView = getView(R.id.price_hint_text_view);

            initLineChart();
            setupTimeTabLayout();
            setupTypeTabLayout();
        }

        private void initLineChart() {
            kChartAdapter = new KChartAdapter();
            kChartView.setAdapter(kChartAdapter);
            kChartView.setDateTimeFormatter(new DateFormatter());
            kChartView.setGridRows(4);
            kChartView.setGridColumns(4);
            kChartView.setScrollEnable(true);
            kChartView.setScaleEnable(true);
            kChartView.resetLoadMoreEnd();
            kChartView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                        viewGroup.requestDisallowInterceptTouchEvent(false);
                    }else if(event.getAction() == MotionEvent.ACTION_MOVE){
                        if(kChartView.isLongPress) {
                            viewGroup.requestDisallowInterceptTouchEvent(true);
                        }
                    }
                    return false;
                }
            });
        }

        private void setupTimeTabLayout() {
            ArrayList<CustomTabEntity> tabEntityList = new ArrayList<>();
            tabEntityList.add(new TabEntity(itemView.getContext().getString(R.string.exc_1_min)));
            tabEntityList.add(new TabEntity(itemView.getContext().getString(R.string.exc_5_min)));
            tabEntityList.add(new TabEntity(itemView.getContext().getString(R.string.exc_15_min)));
            tabEntityList.add(new TabEntity(itemView.getContext().getString(R.string.exc_30_min)));
            tabEntityList.add(new TabEntity(itemView.getContext().getString(R.string.exc_60_min)));
            tabEntityList.add(new TabEntity(itemView.getContext().getString(R.string.exc_1_day)));
//            tabEntityList.add(new TabEntity(itemView.getContext().getString(R.string.exc_1_week)));
//            tabEntityList.add(new TabEntity(itemView.getContext().getString(R.string.exc_1_mon)));
            timeTabLayout.setTabData(tabEntityList);
            timeTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelect(int position) {
                    bindKLineData();
                }

                @Override
                public void onTabReselect(int position) {

                }
            });
            timeTabLayout.setCurrentTab(2);
        }

        private void setupTypeTabLayout() {
            ArrayList<CustomTabEntity> tabEntityList = new ArrayList<>();
            tabEntityList.add(new TabEntity(itemView.getContext().getString(R.string.depth)));
            tabEntityList.add(new TabEntity(itemView.getContext().getString(R.string.exc_transaction)));
            tabEntityList.add(new TabEntity(itemView.getContext().getString(R.string.exc_introduction)));
            contentTabLayout.setTabData(tabEntityList);
            contentTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelect(int position) {
                    notifyDataSetChanged();
                }

                @Override
                public void onTabReselect(int position) {

                }
            });
        }

        private void bindTradingPair() {
            if (mTradingPair != null) {
                double gain = NumberUtil.string2Double(mTradingPair.getChg());
                if (gain >= 0) {
                    priceTextView.setTextColor(mContext.getResources().getColor(R.color.excColor00DA97));
                    gainTextView.setTextColor(mContext.getResources().getColor(R.color.excColor00DA97));
                    gainTextView.setText("+" + NumberUtil.formatMoney(gain, 2) + "%");
                } else {
                    priceTextView.setTextColor(mContext.getResources().getColor(R.color.excColorF95D04));
                    gainTextView.setTextColor(mContext.getResources().getColor(R.color.excColorF95D04));
                    gainTextView.setText(NumberUtil.formatMoney(gain, 2) + "%");
                }

                priceTextView.setText(NumberUtil.formatMoney(mTradingPair.getP_new()));
                highestTextView.setText(NumberUtil.formatMoney(mTradingPair.getHigh()));
                volTextView.setText(NumberUtil.formatMoney(mTradingPair.getTotal()));
                lowestTextView.setText(NumberUtil.formatMoney(mTradingPair.getLow()));
            } else {
                priceTextView.setTextColor(mContext.getResources().getColor(R.color.excColorF95D04));
                gainTextView.setTextColor(mContext.getResources().getColor(R.color.excColorF95D04));
                priceTextView.setText("--");
                gainTextView.setText("--%");
                highestTextView.setText("--");
                volTextView.setText("--");
                lowestTextView.setText("--");
            }
        }

        private void bindKLineData() {
            if (!TextUtils.isEmpty(mTradingPairID)) {
                List<KLineEntity> entityList = KLineCache.getKLineData(mTradingPairID, getTime());
                kChartAdapter.setDatas(entityList);
            }
        }

        private void bindKLineData2(List<KLineEntity> data) {
            kChartAdapter.setDatas(data);
        }

        private String getTime() {
            String time = KLineCache.K_LINE_DATA_TIME_1MIN;
            if (timeTabLayout.getCurrentTab() == 0) {
                time = KLineCache.K_LINE_DATA_TIME_1MIN;
            } else if (timeTabLayout.getCurrentTab() == 1) {
                time = KLineCache.K_LINE_DATA_TIME_5MIN;
            } else if (timeTabLayout.getCurrentTab() == 2) {
                time = KLineCache.K_LINE_DATA_TIME_15MIN;
            } else if (timeTabLayout.getCurrentTab() == 3) {
                time = KLineCache.K_LINE_DATA_TIME_30MIN;
            } else if (timeTabLayout.getCurrentTab() == 4) {
                time = KLineCache.K_LINE_DATA_TIME_60MIN;
            } else if (timeTabLayout.getCurrentTab() == 5) {
                time = KLineCache.K_LINE_DATA_TIME_1DAY;
            }
//            else if (timeTabLayout.getCurrentTab() == 5) {
//                time = KLineCache.K_LINE_DATA_TIME_1WEEK;
//            } else if (timeTabLayout.getCurrentTab() == 6) {
//                time = KLineCache.K_LINE_DATA_TIME_1MON;
//            }
            return time;
        }
    }

    class RecordHolder extends ViewHolder {

        public RecordHolder(Context context, View itemView) {
            super(context, itemView);
        }
    }

    class IntroductionHolder extends ViewHolder {
        TextView titleTextView;
        TextView timeTextView;
        TextView numTextView;
        TextView numTextView_;
        TextView webTextView;
        TextView contentTextView;

        public IntroductionHolder(Context context, View itemView) {
            super(context, itemView);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            timeTextView = itemView.findViewById(R.id.time_text_view);
            numTextView = itemView.findViewById(R.id.num_text_view);
            numTextView_ = itemView.findViewById(R.id.num_text_view_);
            webTextView = itemView.findViewById(R.id.website_text_view);
            contentTextView = itemView.findViewById(R.id.content_text_view);
        }

        public void bindData() {
            if (mCoinIntroduction != null) {
                String title = "";
                if (TextUtils.isEmpty(mCoinIntroduction.getNameShort())) {
                    title = "--";
                } else {
                    title = mCoinIntroduction.getNameShort();
                }
                if (!TextUtils.isEmpty(mCoinIntroduction.getNameZh())) {
                    title = title + " (" + mCoinIntroduction.getNameZh() + ")";
                }
                titleTextView.setText(title);
                timeTextView.setText(mCoinIntroduction.getGmtRelease());
                numTextView.setText(TextUtils.isEmpty(mCoinIntroduction.getTotal()) ? "--" : mCoinIntroduction.getTotal());
                numTextView_.setText(TextUtils.isEmpty(mCoinIntroduction.getCirculation()) ? "--" : mCoinIntroduction.getCirculation());
                webTextView.setText(TextUtils.isEmpty(mCoinIntroduction.getLinkWebsite()) ? "--" : mCoinIntroduction.getLinkWebsite());
                contentTextView.setText(TextUtils.isEmpty(mCoinIntroduction.getInfo()) ? "--" : mCoinIntroduction.getInfo());
            } else {
                titleTextView.setText("--");
                timeTextView.setText("--");
                numTextView.setText("--");
                numTextView_.setText("--");
                webTextView.setText("--");
                contentTextView.setText("--");
            }
        }
    }


    class DepthHolder extends ViewHolder {
        DepthMapView depthMapView;
        ViewGroup viewGroup;

        public DepthHolder(Context context, ViewGroup viewGroup, View itemView) {
            super(context, itemView);
            depthMapView = itemView.findViewById(R.id.depth_view);
            this.viewGroup = viewGroup;

//            depthMapView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
//                        DepthHolder.this.viewGroup.requestDisallowInterceptTouchEvent(false);
//                    } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                        DepthHolder.this.viewGroup.requestDisallowInterceptTouchEvent(true);
//                    }
//                    return false;
//                }
//            });
        }

        public void bindData() {
            List<DepthDataBean> buyData = new ArrayList<>();
            List<DepthDataBean> sellData = new ArrayList<>();
            if (mTradingPair != null && mTradingPair.getDepth() != null) {
                ExcTradingPairDetail.DepthBean depthBean = mTradingPair.getDepth();
                if (depthBean.getAsks() != null && depthBean.getAsks().size() > 0) {
                    for (int i = 0; i < depthBean.getAsks().size(); i++) {
                        List<Double> doubleList = depthBean.getAsks().get(i);
                        if (doubleList != null && doubleList.size() == 2) {
                            DepthDataBean depthDataBean = new DepthDataBean();
                            depthDataBean.setPrice(doubleList.get(0).floatValue());
                            depthDataBean.setVolume(doubleList.get(1).floatValue());
                            sellData.add(depthDataBean);
                        }
                    }
                }

                if (depthBean.getBids() != null && depthBean.getBids().size() > 0) {
                    for (int i = 0; i < depthBean.getBids().size(); i++) {
                        List<Double> doubleList = depthBean.getBids().get(i);
                        if (doubleList != null && doubleList.size() == 2) {
                            DepthDataBean depthDataBean = new DepthDataBean();
                            depthDataBean.setPrice(doubleList.get(0).floatValue());
                            depthDataBean.setVolume(doubleList.get(1).floatValue());
                            buyData.add(depthDataBean);
                        }
                    }
                }
            }
            depthMapView.setData(buyData, sellData);
        }
    }

}
