package com.muye.rocket.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ifenduo.common.tool.DimensionTool;
import com.muye.rocket.R;

public class UnderLineEditText extends LinearLayout implements View.OnFocusChangeListener {
    private EditText mEditText;
    private View mUnderLine;

    private @ColorInt
    int mFocusColor;
    private @ColorInt
    int mUnFocusColor;
    private @ColorInt
    int mTextColor;
    private int mTextSize;
    private int mTextPaddingTop;
    private int mTextPaddingLeft;
    private int mTextPaddingRight;
    private int mTextPaddingBottom;

    public UnderLineEditText(Context context) {
        this(context, null);
    }

    public UnderLineEditText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UnderLineEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        initialization();
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UnderLineEditText);
        mFocusColor = typedArray.getColor(R.styleable.UnderLineEditText_focusColor, Color.parseColor("#F1F1F1"));
        mUnFocusColor = typedArray.getColor(R.styleable.UnderLineEditText_unFocusColor, getResources().getColor(R.color.colorPrimary));

        mTextColor = typedArray.getColor(R.styleable.UnderLineEditText_textColor, getResources().getColor(R.color.colorPrimary));
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.UnderLineEditText_textSize, DimensionTool.sp2px(getContext(), 14));
        mTextPaddingTop = typedArray.getDimensionPixelSize(R.styleable.UnderLineEditText_textPaddingTop, 0);
        mTextPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.UnderLineEditText_textPaddingLeft, 0);
        mTextPaddingRight = typedArray.getDimensionPixelSize(R.styleable.UnderLineEditText_textPaddingRight, 0);
        mTextPaddingBottom = typedArray.getDimensionPixelSize(R.styleable.UnderLineEditText_textPaddingBottom, 0);
    }

    private void initialization() {
        setOrientation(VERTICAL);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_under_line_edit_text, this, true);
        mEditText = view.findViewById(R.id.edit_text);
        mUnderLine = view.findViewById(R.id.under_line);
        mUnderLine.setBackgroundColor(mUnFocusColor);

        mEditText.setOnFocusChangeListener(this);
        mEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        mEditText.setTextColor(mTextColor);
        mEditText.setPadding(mTextPaddingLeft, mTextPaddingTop, mTextPaddingRight, mTextPaddingBottom);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        mUnderLine.setBackgroundColor(hasFocus ? mFocusColor : mUnFocusColor);
    }

    public void setFilters(InputFilter[] inputFilters) {
        mEditText.setFilters(inputFilters);
    }

    public EditText getEditText() {
        return mEditText;
    }

    public String getInputText() {
        return mEditText.getText().toString();
    }
}
