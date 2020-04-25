package com.muye.rocket.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ifenduo.common.tool.DimensionTool;
import com.muye.rocket.R;

public class MessageDialog extends Dialog implements View.OnClickListener {
    TextView mTitleTextView;
    TextView mMessageTextView;
    TextView mCancelButton;
    TextView mOkButton;
    LinearLayout mContainer;
    View mTitleDivider;
    View mBottomDivider;
    View mButtonDivider;

    String title;
    String message;
    String cancelText;
    String okText;

    @ColorInt
    int titleColor;
    @ColorInt
    int messageColor;
    @ColorInt
    int cancelColor;
    @ColorInt
    int okColor;
    @ColorInt
    int dividerColor;
    @DrawableRes
    int backgroundRes;


    OnCancelButtonClickListener mOnCancelButtonClickListener;
    OnOkButtonClickListener mOnOkButtonClickListener;

    public MessageDialog(@NonNull Context context) {
        super(context,R.style.MessageDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_message);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = (int) (DimensionTool.getScreenWidth(getContext()) * 0.8);
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(layoutParams);
        initView();
    }

    public MessageDialog setOnTouchCancel(boolean isCancel){
        setCanceledOnTouchOutside(isCancel);
        return this;
    }

    private void initView() {
        mTitleDivider = findViewById(R.id.title_divider);
        mBottomDivider = findViewById(R.id.bottom_divider);
        mButtonDivider = findViewById(R.id.button_divider);

        mTitleTextView = findViewById(R.id.title_text_view);
        mMessageTextView = findViewById(R.id.message_text_view);
        mCancelButton = findViewById(R.id.cancel_button);
        mOkButton = findViewById(R.id.submit_button);

        mContainer = findViewById(R.id.container);

        mCancelButton.setOnClickListener(this);
        mOkButton.setOnClickListener(this);

        if (backgroundRes != 0) {
            mContainer.setBackgroundResource(backgroundRes);
        }

        if (titleColor != 0) {
            mTitleTextView.setTextColor(titleColor);
        }
        if (messageColor != 0) {
            mMessageTextView.setTextColor(messageColor);
        }
        if (cancelColor != 0) {
            mCancelButton.setTextColor(cancelColor);
        }
        if (okColor != 0) {
            mOkButton.setTextColor(okColor);
        }
        if (dividerColor != 0) {
            mTitleDivider.setBackgroundColor(dividerColor);
            mBottomDivider.setBackgroundColor(dividerColor);
            mButtonDivider.setBackgroundColor(dividerColor);
        }

        if (TextUtils.isEmpty(message)) {
            mMessageTextView.setVisibility(View.GONE);
        } else {
            mMessageTextView.setVisibility(View.VISIBLE);
            mMessageTextView.setText(message);
        }

        if (TextUtils.isEmpty(title)) {
            mTitleTextView.setVisibility(View.GONE);
            mTitleDivider.setVisibility(View.GONE);
        } else {
            mTitleTextView.setVisibility(View.VISIBLE);
            mTitleDivider.setVisibility(View.VISIBLE);
            mTitleTextView.setText(title);
        }

        if (TextUtils.isEmpty(cancelText)) {
            mCancelButton.setVisibility(View.GONE);
        } else {
            mCancelButton.setVisibility(View.VISIBLE);
            mCancelButton.setText(cancelText);
        }

        if (TextUtils.isEmpty(okText)) {
            mOkButton.setVisibility(View.GONE);
        } else {
            mOkButton.setVisibility(View.VISIBLE);
            mOkButton.setText(okText);
        }

        setButtonDividerVisibility();

    }

    public MessageDialog setOnOkButtonClickListener(OnOkButtonClickListener onOkButtonClickListener) {
        mOnOkButtonClickListener = onOkButtonClickListener;
        return this;
    }

    public MessageDialog setOnCancelButtonClickListener(OnCancelButtonClickListener onCancelButtonClickListener) {
        mOnCancelButtonClickListener = onCancelButtonClickListener;
        return this;
    }

    public MessageDialog setBackgroundRes(@DrawableRes int backgroundRes) {
        this.backgroundRes = backgroundRes;
        if (mContainer != null) {
            mContainer.setBackgroundResource(backgroundRes);
        }
        return this;
    }

    public MessageDialog setTitle(String title) {
        this.title = title;
        if (mTitleTextView != null) {
            if (!TextUtils.isEmpty(title)) {
                mTitleTextView.setText(title);
                mTitleTextView.setVisibility(View.VISIBLE);
                mTitleDivider.setVisibility(View.VISIBLE);
            } else {
                mTitleTextView.setVisibility(View.GONE);
                mTitleDivider.setVisibility(View.GONE);
            }
        }
        return this;
    }

    public MessageDialog setTitleColor(@ColorInt int color) {
        titleColor = color;
        if (mTitleTextView != null) {
            mTitleTextView.setTextColor(titleColor);
        }
        return this;
    }

    public MessageDialog setMessage(String message) {
        this.message = message;
        if (mMessageTextView != null) {
            if (!TextUtils.isEmpty(message)) {
                mMessageTextView.setText(message);
                mMessageTextView.setVisibility(View.VISIBLE);
            } else {
                mMessageTextView.setVisibility(View.GONE);
            }
        }
        return this;
    }

    public MessageDialog setMessageColor(@ColorInt int color) {
        messageColor = color;
        if (mMessageTextView != null) {
            mMessageTextView.setTextColor(messageColor);
        }
        return this;
    }

    public MessageDialog setCancelText(String cancelText) {
        this.cancelText = cancelText;
        if (mCancelButton != null) {
            if (!TextUtils.isEmpty(cancelText)) {
                mCancelButton.setText(cancelText);
                mCancelButton.setVisibility(View.VISIBLE);
            } else {
                mCancelButton.setVisibility(View.GONE);
            }
        }
        setButtonDividerVisibility();
        return this;
    }

    public MessageDialog setCancelColor(@ColorInt int color) {
        cancelColor = color;
        if (mCancelButton != null) {
            mCancelButton.setTextColor(cancelColor);
        }
        return this;
    }

    public MessageDialog setOkText(String okText) {
        this.okText = okText;
        if (mOkButton != null) {
            if (!TextUtils.isEmpty(okText)) {
                mOkButton.setText(okText);
                mOkButton.setVisibility(View.VISIBLE);
            } else {
                mOkButton.setVisibility(View.GONE);
            }
        }
        setButtonDividerVisibility();
        return this;
    }

    public MessageDialog setOkColor(@ColorInt int color) {
        okColor = color;
        if (mOkButton != null) {
            mOkButton.setTextColor(okColor);
        }
        return this;
    }

    public MessageDialog setDividerColor(@ColorInt int color) {
        dividerColor = color;
        if (mTitleDivider != null) {
            mTitleDivider.setBackgroundColor(okColor);
        }
        if (mBottomDivider != null) {
            mBottomDivider.setBackgroundColor(okColor);
        }
        if (mButtonDivider != null) {
            mButtonDivider.setBackgroundColor(okColor);
        }
        return this;
    }

    private void setButtonDividerVisibility() {
        if (TextUtils.isEmpty(cancelText) && TextUtils.isEmpty(okText)) {
            if (mButtonDivider != null) {
                mButtonDivider.setVisibility(View.GONE);
            }
            if (mBottomDivider != null) {
                mBottomDivider.setVisibility(View.GONE);
            }
        } else if (TextUtils.isEmpty(cancelText) || TextUtils.isEmpty(okText)) {
            if (mButtonDivider != null) {
                mButtonDivider.setVisibility(View.GONE);
            }
            if (mBottomDivider != null) {
                mBottomDivider.setVisibility(View.VISIBLE);
            }
        } else {
            if (mButtonDivider != null) {
                mButtonDivider.setVisibility(View.VISIBLE);
            }
            if (mBottomDivider != null) {
                mBottomDivider.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel_button) {
            dismiss();
            if (mOnCancelButtonClickListener != null) {
                mOnCancelButtonClickListener.onCancelButtonClick(this);
            }
        } else if (v.getId() == R.id.submit_button) {
            if (mOnOkButtonClickListener != null) {
                mOnOkButtonClickListener.onOkButtonClick(this);
            }
        }
    }

    public interface OnCancelButtonClickListener {
        void onCancelButtonClick(MessageDialog dialog);
    }

    public interface OnOkButtonClickListener {
        void onOkButtonClick(MessageDialog dialog);
    }

}
