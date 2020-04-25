package com.muye.rocket.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.ifenduo.common.adapter.base.ViewHolder;
import com.ifenduo.common.tool.StatusBarTool;
import com.ifenduo.lib_base.widget.tablayout.TabEntity;
import com.muye.rocket.R;
import com.muye.rocket.cache.ExcCache;
import com.muye.rocket.entity.exchange.ExcTradingPair;
import com.muye.rocket.tools.ThreadUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TradingPairPicker extends Dialog implements OnTabSelectListener, View.OnClickListener {
    CommonTabLayout mTabLayout;
    RecyclerView mRecyclerView;
    TradingPairAdapter mAdapter;
    List<String> tradingAreaList;
    Map<String, List<ExcTradingPair>> mCacheData;
    OnTradingPairSelectedListener mOnTradingPairSelectedListener;
    TextView tvCoinSelect;
    View viewLine;
    int pageType;
    Context context;

    public TradingPairPicker(@NonNull Context context) {
        super(context, R.style.TradingPairPicker);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exc_trading_pair_picker);
        mTabLayout = findViewById(R.id.tab_layout);
        mRecyclerView = findViewById(R.id.recycler_view);

        tvCoinSelect = findViewById(R.id.tv_coin_select);
        viewLine = findViewById(R.id.view_line);

        mAdapter = new TradingPairAdapter();
        mRecyclerView.setAdapter(mAdapter);

        View statusBar = null;
        if (pageType == 1){

        }else {
            statusBar = findViewById(R.id.status_bar);
            tvCoinSelect.setBackgroundColor(Color.WHITE);
            tvCoinSelect.setTextColor(Color.BLACK);
            mTabLayout.setBackgroundColor(Color.WHITE);
            viewLine.setBackgroundColor(context.getResources().getColor(R.color.excColorF1F1F1));
            mRecyclerView.setBackground(context.getResources().getDrawable(R.drawable.exc_shape_ffffff_bottom_radius_4));
        }

        findViewById(R.id.empty_view).setOnClickListener(this);
        mTabLayout.setOnTabSelectListener(this);

        setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setGravity(Gravity.TOP);

        if (pageType != 1) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                statusBar.setVisibility(View.GONE);
            } else {
                statusBar.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams statusBarLayoutParams = (LinearLayout.LayoutParams) statusBar.getLayoutParams();
                statusBarLayoutParams.height = StatusBarTool.getStatusBarHeight(getContext());
                statusBar.setLayoutParams(statusBarLayoutParams);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            }
        }

        getWindow().setAttributes(layoutParams);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        //布局位于状态栏下方
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        //全屏
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        //隐藏导航栏
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                uiOptions |= 0x00001000;
                getWindow().getDecorView().setSystemUiVisibility(uiOptions);
            }
        });
        buildData();
    }


    public void setOnTradingPairSelectedListener(OnTradingPairSelectedListener onTradingPairSelectedListener) {
        mOnTradingPairSelectedListener = onTradingPairSelectedListener;
    }

    private void buildData() {
        ThreadUtils.getUiThreadHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mCacheData = ExcCache.getTradingAreaCache();
                tradingAreaList = new ArrayList<>();
                for (Map.Entry<String, List<ExcTradingPair>> entry : mCacheData.entrySet()) {
                    tradingAreaList.add(entry.getKey());
                }

                if (tradingAreaList.size() > 0) {
                    ArrayList<CustomTabEntity> tabEntityList = new ArrayList<>();
                    for (int i = 0; i < tradingAreaList.size(); i++) {
                        tabEntityList.add(new TabEntity(tradingAreaList.get(i)));
                    }
                    mTabLayout.setTabData(tabEntityList);
                    mAdapter.setData(mCacheData.get(tradingAreaList.get(0)));
                }
            }
        },600);
    }

    @Override
    public void onTabSelect(int position) {
        if (tradingAreaList != null && tradingAreaList.size() > position) {
            mAdapter.setData(mCacheData.get(tradingAreaList.get(position)));
        }
    }

    @Override
    public void onTabReselect(int position) {

    }

    public void setBgColorType(int type){
        this.pageType = type;
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    class TradingPairAdapter extends RecyclerView.Adapter<ViewHolder> {

        List<ExcTradingPair> mData;

        public void setData(List<ExcTradingPair> data) {
            this.mData = data;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.exc_item_trading_picker, viewGroup, false);
            return new ViewHolder(getContext(), itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
            ExcTradingPair tradingPair = null;
            if (mData != null && mData.size() > i) {
                tradingPair = mData.get(i);
            }

            TextView sellName =  holder.getView(R.id.sell_name_text_view);
            View divider8 = holder.getView(R.id.divider8);

            if (tradingPair != null) {
                sellName.setText(tradingPair.getSellShortName());
//                holder.setText(R.id.sell_name_text_view, tradingPair.getSellShortName());
                holder.setText(R.id.buy_name_text_view, "/" + tradingPair.getBuyShortName());
            } else {
                holder.setText(R.id.sell_name_text_view, "--");
                holder.setText(R.id.buy_name_text_view, "/--");
            }
            if (pageType == 1){

            }else {
                divider8.setBackgroundColor(context.getResources().getColor(R.color.excColorF6F6FB));
                sellName.setTextColor(Color.BLACK);
            }

            ExcTradingPair finalTradingPair = tradingPair;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (finalTradingPair == null) return;
                    if (mOnTradingPairSelectedListener != null) {
                        mOnTradingPairSelectedListener.onTradingPairSelected(finalTradingPair);
                        dismiss();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }
    }

    public interface OnTradingPairSelectedListener {
        void onTradingPairSelected(ExcTradingPair tradingPair);
    }
}
