package com.muye.rocket.widget;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ifenduo.lib_base.tools.NumberUtil;
import com.muye.rocket.R;
import com.muye.rocket.entity.exchange.ExcTradingPairDetail;

public class ExcHandicapView extends ConstraintLayout {
    ExcProgressBar mProgressBar;
    TextView mPriceTextView;
    TextView mNumTextView;
    double price;
    double num;

    @ColorInt
    int mPriceTextColor;
    @ColorInt
    int mNumTextColor;
    @ColorInt
    int mProgressColor;

    public ExcHandicapView(Context context) {
        this(context, null);
    }

    public ExcHandicapView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExcHandicapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.exc_view_handicap, this, true);
        mProgressBar = view.findViewById(R.id.progress_bar);
        mPriceTextView = view.findViewById(R.id.price_text_view);
        mNumTextView = view.findViewById(R.id.num_text_view);
    }

    public void setProgressColor(@ColorInt int progressColor) {
        this.mProgressColor = progressColor;
        mProgressBar.setProgressColor(mProgressColor);
    }

    public void setPriceTextColor(int priceTextColor) {
        this.mPriceTextColor = priceTextColor;
        mPriceTextView.setTextColor(mPriceTextColor);
    }

    public void setNumTextColor(int numTextColor) {
        this.mNumTextColor = numTextColor;
        mNumTextView.setTextColor(mNumTextColor);
    }

    public void setProgress(float progress) {
        mProgressBar.setProgress(progress);
    }

    public void bindData(ExcTradingPairDetail.DepthItemEntity entity) {
        if (entity != null) {
            num = entity.getNum();
            price = entity.getPrice();

            String numStr = num > 1000 ? (NumberUtil.formatMoney(num / 1000) + "K") : NumberUtil.formatMoney(num);
            mNumTextView.setText(numStr);
            mPriceTextView.setText(NumberUtil.formatMoney(price));
            mProgressBar.setProgress(entity.getProgress());
        } else {
            price = 0;
            num = 0;
            mNumTextView.setText("");
            mPriceTextView.setText("");
            mProgressBar.setProgress(0);
        }
    }

    public double getNum() {
        return num;
    }

    public double getPrice() {
        return price;
    }
}
