package com.ifenduo.lib_base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ifenduo.common.tool.DimensionTool;
import com.ifenduo.lib_base.R;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class CustomEditText extends LinearLayout implements View.OnClickListener, View.OnFocusChangeListener {

    public static final int INPUT_TYPE_TEXT = 0;
    public static final int INPUT_TYPE_PASSWORD = 1;
    public static final int INPUT_TYPE_PHONE = 2;
    public static final int INPUT_TYPE_NUMBER = 3;
    public static final int INPUT_TYPE_NUMBER_DECIMAL = 4;
    public static final int INPUT_TYPE_EMAIL = 5;
    TextView leftActionButton;
    EditText editText;
    ImageButton cleanButton;
    ImageButton eyeButton;
    View bottomLine;
    TextView errorTextView;
    LinearLayout levelContainer;
    View lowLevelView;
    View middleLevelView;
    View highLevelView;

    private int leftActionButtonTextSize;
    private @ColorInt
    int leftActionButtonTextColor;
    private String leftActionButtonHint;
    private @ColorInt
    int leftActionButtonHintColor;
    private Drawable leftActionButtonLeftDrawable;
    private Drawable leftActionButtonRightDrawable;
    private Drawable leftActionButtonTopDrawable;
    private Drawable leftActionButtonBottomDrawable;
    private int leftActionButtonDrawablePadding;
    private int leftActionButtonLeftPadding;
    private int leftActionButtonRightPadding;
    private int leftActionButtonTopPadding;
    private int leftActionButtonBottomPadding;
    private boolean leftActionButtonEnable;

    private int editTextTextSize;
    private @ColorInt
    int editTextTextColor;
    private String editTextHintText;
    private @ColorInt
    int editTextHintColor;
    private int editTextLeftPadding;
    private int editTextRightPadding;
    private int editTextTopPadding;
    private int editTextBottomPadding;
    private boolean editTextIsSingleLine;
    private @InputType
    int editTextInputType;
    private String editTextDigits;
    private int editTextHeight;
    private int maxLength;

    private boolean cleanButtonEnable;
    private @DrawableRes
    int cleanButtonDrawable;
    private boolean eyeButtonEnable;
    private @DrawableRes
    int eyeButtonDrawable;

    private @ColorInt
    int bottomLineFocusColor;
    private @ColorInt
    int bottomLineUnFocusColor;
    private @ColorInt
    int bottomLineErrorColor;

    private int errorTextSize;
    private @ColorInt
    int errorTextColor;
    private Drawable errorLeftDrawable;
    private Drawable errorRightDrawable;
    private Drawable errorTopDrawable;
    private Drawable errorBottomDrawable;
    private int errorDrawablePadding;

    private boolean levelViewEnable;
    private @ColorInt
    int levelNormalColor;
    private @ColorInt
    int lowLevelColor;
    private @ColorInt
    int middleLevelColor;
    private @ColorInt
    int highLevelColor;
    private int levelViewHeight;
    private int levelViewMargin;

    private OnFocusChangeListener onFocusChangeListener;
    private OnLeftActionClickListener onLeftActionClickListener;

    public CustomEditText(Context context) {
        this(context, null);
    }

    public CustomEditText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        initialization();
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText);
        leftActionButtonTextSize = typedArray.getDimensionPixelSize(R.styleable.CustomEditText_ce_left_button_text_size, DimensionTool.sp2px(context, 14));
        leftActionButtonTextColor = typedArray.getColor(R.styleable.CustomEditText_ce_left_button_text_color, Color.WHITE);
        leftActionButtonLeftDrawable = typedArray.getDrawable(R.styleable.CustomEditText_ce_left_button_drawable_left);
        leftActionButtonRightDrawable = typedArray.getDrawable(R.styleable.CustomEditText_ce_left_button_drawable_right);
        leftActionButtonTopDrawable = typedArray.getDrawable(R.styleable.CustomEditText_ce_left_button_drawable_top);
        leftActionButtonBottomDrawable = typedArray.getDrawable(R.styleable.CustomEditText_ce_left_button_drawable_bottom);
        leftActionButtonDrawablePadding = typedArray.getDimensionPixelSize(R.styleable.CustomEditText_ce_left_button_drawable_padding, DimensionTool.dp2px(context, 6));
        leftActionButtonLeftPadding = typedArray.getDimensionPixelSize(R.styleable.CustomEditText_ce_left_button_left_padding, 0);
        leftActionButtonRightPadding = typedArray.getDimensionPixelSize(R.styleable.CustomEditText_ce_left_button_right_padding, 0);
        leftActionButtonTopPadding = typedArray.getDimensionPixelSize(R.styleable.CustomEditText_ce_left_button_top_padding, 0);
        leftActionButtonBottomPadding = typedArray.getDimensionPixelSize(R.styleable.CustomEditText_ce_left_button_bottom_padding, 0);
        leftActionButtonHint = typedArray.getString(R.styleable.CustomEditText_ce_left_button_hint);
        leftActionButtonHintColor = typedArray.getColor(R.styleable.CustomEditText_ce_left_button_hint_color, Color.parseColor("#484B68"));
        leftActionButtonEnable = typedArray.getBoolean(R.styleable.CustomEditText_ce_left_button_enable, false);

        editTextTextSize = typedArray.getDimensionPixelSize(R.styleable.CustomEditText_ce_edit_text_size, DimensionTool.sp2px(context, 14));
        editTextTextColor = typedArray.getColor(R.styleable.CustomEditText_ce_edit_text_color, Color.WHITE);
        editTextHintText = typedArray.getString(R.styleable.CustomEditText_ce_edit_hint);
        editTextHintColor = typedArray.getColor(R.styleable.CustomEditText_ce_edit_hint_color, Color.parseColor("#484B68"));
        editTextLeftPadding = typedArray.getDimensionPixelSize(R.styleable.CustomEditText_ce_edit_left_padding, 0);
        editTextRightPadding = typedArray.getDimensionPixelSize(R.styleable.CustomEditText_ce_edit_right_padding, 0);
        editTextTopPadding = typedArray.getDimensionPixelSize(R.styleable.CustomEditText_ce_edit_top_padding, 0);
        editTextBottomPadding = typedArray.getDimensionPixelSize(R.styleable.CustomEditText_ce_edit_bottom_padding, 0);
        editTextIsSingleLine = typedArray.getBoolean(R.styleable.CustomEditText_ce_edit_single_line, true);
        editTextInputType = typedArray.getInt(R.styleable.CustomEditText_ce_edit_input_type, INPUT_TYPE_TEXT);
        editTextDigits = typedArray.getString(R.styleable.CustomEditText_ce_edit_digits);
        editTextHeight = typedArray.getDimensionPixelSize(R.styleable.CustomEditText_ce_edit_height, DimensionTool.dp2px(context, 44));
        maxLength = typedArray.getInteger(R.styleable.CustomEditText_ce_edit_max_length, 0);

        cleanButtonEnable = typedArray.getBoolean(R.styleable.CustomEditText_ce_clean_button_enable, true);
        cleanButtonDrawable = typedArray.getResourceId(R.styleable.CustomEditText_ce_clean_button_drawable, 0);

        eyeButtonEnable = typedArray.getBoolean(R.styleable.CustomEditText_ce_eye_button_enable, true);
        eyeButtonDrawable = typedArray.getResourceId(R.styleable.CustomEditText_ce_eye_button_drawable, 0);

        bottomLineFocusColor = typedArray.getColor(R.styleable.CustomEditText_ce_bottom_line_focus_color, Color.parseColor("#2777B7"));
        bottomLineUnFocusColor = typedArray.getColor(R.styleable.CustomEditText_ce_bottom_line_unfocus_color, Color.parseColor("#31344C"));
        bottomLineErrorColor = typedArray.getColor(R.styleable.CustomEditText_ce_bottom_line_error_color, Color.RED);

        errorTextSize = typedArray.getDimensionPixelSize(R.styleable.CustomEditText_ce_error_text_size, DimensionTool.sp2px(context, 12));
        errorTextColor = typedArray.getColor(R.styleable.CustomEditText_ce_error_text_color, Color.RED);
        errorLeftDrawable = typedArray.getDrawable(R.styleable.CustomEditText_ce_error_drawable_left);
        errorRightDrawable = typedArray.getDrawable(R.styleable.CustomEditText_ce_error_drawable_right);
        errorTopDrawable = typedArray.getDrawable(R.styleable.CustomEditText_ce_error_drawable_top);
        errorBottomDrawable = typedArray.getDrawable(R.styleable.CustomEditText_ce_error_drawable_bottom);
        errorDrawablePadding = typedArray.getDimensionPixelSize(R.styleable.CustomEditText_ce_error_drawable_padding, DimensionTool.dp2px(context, 6));

        levelViewEnable = typedArray.getBoolean(R.styleable.CustomEditText_ce_level_view_enable, false);
        levelNormalColor = typedArray.getColor(R.styleable.CustomEditText_ce_level_normal_color, Color.parseColor("#F1F1F1"));
        lowLevelColor = typedArray.getColor(R.styleable.CustomEditText_ce_low_level_color, Color.parseColor("#EF2357"));
        middleLevelColor = typedArray.getColor(R.styleable.CustomEditText_ce_middle_level_color, Color.parseColor("#EF5923"));
        highLevelColor = typedArray.getColor(R.styleable.CustomEditText_ce_high_level_color, Color.parseColor("#24D95F"));
        levelViewHeight = typedArray.getDimensionPixelSize(R.styleable.CustomEditText_ce_level_view_height, DimensionTool.dp2px(context, 6));
        levelViewMargin = typedArray.getDimensionPixelSize(R.styleable.CustomEditText_ce_level_view_margin, DimensionTool.dp2px(context, 4));

        typedArray.recycle();
    }

    public int getMaxCount() {
        return maxLength;
    }

    private void initialization() {
        setOrientation(VERTICAL);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_custom_edit_text, this, true);
        leftActionButton = view.findViewById(R.id.left_action_button);
        editText = view.findViewById(R.id.edit_text);
        cleanButton = view.findViewById(R.id.clean_button);
        eyeButton = view.findViewById(R.id.eye_button);
        bottomLine = view.findViewById(R.id.bottom_line);
        errorTextView = view.findViewById(R.id.error_text_view);
        levelContainer = view.findViewById(R.id.level_container);
        lowLevelView = view.findViewById(R.id.low_level_view);
        middleLevelView = view.findViewById(R.id.middle_level_view);
        highLevelView = view.findViewById(R.id.high_level_view);

        leftActionButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftActionButtonTextSize);
        leftActionButton.setTextColor(leftActionButtonTextColor);
        leftActionButton.setHintTextColor(leftActionButtonHintColor);
        leftActionButton.setHint(leftActionButtonHint);
        leftActionButton.setCompoundDrawablePadding(leftActionButtonDrawablePadding);
        leftActionButton.setCompoundDrawablesWithIntrinsicBounds(leftActionButtonLeftDrawable, leftActionButtonTopDrawable, leftActionButtonRightDrawable, leftActionButtonBottomDrawable);
        leftActionButton.setPadding(leftActionButtonLeftPadding, leftActionButtonTopPadding, leftActionButtonRightPadding, leftActionButtonBottomPadding);
        leftActionButton.setVisibility(leftActionButtonEnable ? VISIBLE : GONE);


        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextTextSize);
        editText.setTextColor(editTextTextColor);
        editText.setHintTextColor(editTextHintColor);
        editText.setHint(editTextHintText);
        editText.setPadding(editTextLeftPadding, editTextTopPadding, editTextRightPadding, editTextBottomPadding);
        editText.setSingleLine(editTextIsSingleLine);
        if (maxLength > 0) {
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        }
        if (!TextUtils.isEmpty(editTextDigits)) {
            editText.setKeyListener(DigitsKeyListener.getInstance(editTextDigits));
        }
        setInputType(editTextInputType);
        LinearLayout.LayoutParams layoutParams = (LayoutParams) editText.getLayoutParams();
        layoutParams.height = editTextHeight;
        editText.setLayoutParams(layoutParams);

        cleanButton.setVisibility(cleanButtonEnable ? VISIBLE : GONE);
        cleanButton.setImageResource(cleanButtonDrawable);

        eyeButton.setVisibility(eyeButtonEnable ? VISIBLE : GONE);
        eyeButton.setImageResource(eyeButtonDrawable);

        bottomLine.setBackgroundColor(bottomLineUnFocusColor);

        errorTextView.setTextColor(errorTextColor);
        errorTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, errorTextSize);
        errorTextView.setCompoundDrawablePadding(errorDrawablePadding);
        errorTextView.setCompoundDrawablesWithIntrinsicBounds(errorLeftDrawable, errorTopDrawable, errorRightDrawable, errorBottomDrawable);

        bottomLine.setVisibility(levelViewEnable ? GONE : VISIBLE);
        levelContainer.setVisibility(levelViewEnable ? VISIBLE : GONE);
        lowLevelView.setVisibility(levelViewEnable ? VISIBLE : GONE);
        middleLevelView.setVisibility(levelViewEnable ? VISIBLE : GONE);
        highLevelView.setVisibility(levelViewEnable ? VISIBLE : GONE);
        LayoutParams levelViewLayoutParams = (LayoutParams) levelContainer.getLayoutParams();
        levelViewLayoutParams.height = levelViewHeight;
        levelContainer.setLayoutParams(levelViewLayoutParams);
        MarginLayoutParams middleLayoutParams = (MarginLayoutParams) middleLevelView.getLayoutParams();
        middleLayoutParams.rightMargin = levelViewMargin;
        middleLayoutParams.leftMargin = levelViewMargin;
        middleLevelView.setLayoutParams(middleLayoutParams);

        leftActionButton.setOnClickListener(this);
        cleanButton.setOnClickListener(this);
        eyeButton.setOnClickListener(this);
        editText.setOnFocusChangeListener(this);
    }

    public TextView getLeftActionButton() {
        return leftActionButton;
    }

    public EditText getEditText() {
        return editText;
    }

    public ImageButton getCleanButton() {
        return cleanButton;
    }

    public ImageButton getEyeButton() {
        return eyeButton;
    }

    public String getInputText() {
        return editText.getText().toString();
    }

    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        this.onFocusChangeListener = onFocusChangeListener;
    }

    public void setOnLeftActionClickListener(OnLeftActionClickListener onLeftActionClickListener) {
        this.onLeftActionClickListener = onLeftActionClickListener;
    }

    public CustomEditText setLeftActionButtonText(String leftActionButtonText) {
        leftActionButton.setText(leftActionButtonText);
        return this;
    }

    public CustomEditText setFilters(InputFilter[] filters) {
        editText.setFilters(filters);
        return this;
    }

    public CustomEditText setEditTextHintColor(@ColorInt int hintColor) {
        editTextHintColor = hintColor;
        editText.setHintTextColor(editTextHintColor);
        return this;
    }

    public CustomEditText setEditTextHint(String hint) {
        editTextHintText = hint;
        editText.setHint(editTextHintText);
        return this;
    }

    public CustomEditText setEditTextSingleLine(boolean isSingleLine) {
        editText.setSingleLine(isSingleLine);
        return this;
    }

    public CustomEditText setInputType(@InputType int inputType) {
        editTextInputType = inputType;
        if (editTextInputType == INPUT_TYPE_PHONE) {
            editText.setInputType(android.text.InputType.TYPE_CLASS_PHONE);
        } else if (editTextInputType == INPUT_TYPE_PASSWORD) {
            editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else if (editTextInputType == INPUT_TYPE_NUMBER) {
            editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        } else if (editTextInputType == INPUT_TYPE_NUMBER_DECIMAL) {
            editText.setInputType(android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        } else if (editTextInputType == INPUT_TYPE_EMAIL){
            editText.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        }else {
            editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
        }
        return this;
    }

    public CustomEditText setCleanButtonEnable(boolean enable) {
        cleanButtonEnable = enable;
        cleanButton.setVisibility(cleanButtonEnable ? VISIBLE : GONE);
        return this;
    }

    public CustomEditText setCleanButtonDrawable(@DrawableRes int drawable) {
        cleanButtonDrawable = drawable;
        cleanButton.setImageResource(cleanButtonDrawable);
        return this;
    }

    public CustomEditText setEyeButtonEnable(boolean enable) {
        eyeButtonEnable = enable;
        eyeButton.setVisibility(eyeButtonEnable ? VISIBLE : GONE);
        return this;
    }

    public CustomEditText setEyeButtonDrawable(@DrawableRes int drawable) {
        eyeButtonDrawable = drawable;
        eyeButton.setImageResource(eyeButtonDrawable);
        return this;
    }

    public CustomEditText showErrorText(String errorText) {
        errorTextView.setText(errorText);
        errorTextView.setVisibility(VISIBLE);
        bottomLine.setBackgroundColor(bottomLineErrorColor);
        return this;
    }

    public CustomEditText hideErrorText() {
        errorTextView.setVisibility(INVISIBLE);
        bottomLine.setBackgroundColor(editText.hasFocus() ? bottomLineFocusColor : bottomLineUnFocusColor);
        return this;
    }

    public void setLevel(@IntRange(from = 0, to = 3) int level) {
        if (level == 1) {
            lowLevelView.setBackgroundColor(lowLevelColor);
            middleLevelView.setBackgroundColor(levelNormalColor);
            highLevelView.setBackgroundColor(levelNormalColor);
        } else if (level == 2) {
            lowLevelView.setBackgroundColor(middleLevelColor);
            middleLevelView.setBackgroundColor(middleLevelColor);
            highLevelView.setBackgroundColor(levelNormalColor);
        } else if (level == 3) {
            lowLevelView.setBackgroundColor(highLevelColor);
            middleLevelView.setBackgroundColor(highLevelColor);
            highLevelView.setBackgroundColor(highLevelColor);
        } else {
            lowLevelView.setBackgroundColor(levelNormalColor);
            middleLevelView.setBackgroundColor(levelNormalColor);
            highLevelView.setBackgroundColor(levelNormalColor);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.left_action_button) {
            if (onLeftActionClickListener != null) {
                onLeftActionClickListener.onLeftActionClick(this, leftActionButton);
            }
        } else if (v.getId() == R.id.clean_button) {
            editText.setText("");
        } else if (v.getId() == R.id.eye_button) {
            changePasswordTransformationMethod();
        }
    }

    /**
     * 显示隐藏密码
     */
    private void changePasswordTransformationMethod() {
        eyeButton.setSelected(!eyeButton.isSelected());
        if (eyeButton.isSelected()) {
            //显示密码
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            //隐藏密码
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        //移动光标
        editText.setSelection(editText.getText().toString().length());
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == R.id.edit_text) {
            if (onFocusChangeListener == null) {
                if (hasFocus) {
                    bottomLine.setBackgroundColor(errorTextView.getVisibility() == VISIBLE ? bottomLineErrorColor : bottomLineFocusColor);
                } else {
                    bottomLine.setBackgroundColor(errorTextView.getVisibility() == VISIBLE ? bottomLineErrorColor : bottomLineUnFocusColor);
                }
            } else {
                if (!onFocusChangeListener.onFocusChange(this, editText, hasFocus)) {
                    if (hasFocus) {
                        bottomLine.setBackgroundColor(errorTextView.getVisibility() == VISIBLE ? bottomLineErrorColor : bottomLineFocusColor);
                    } else {
                        bottomLine.setBackgroundColor(errorTextView.getVisibility() == VISIBLE ? bottomLineErrorColor : bottomLineUnFocusColor);
                    }
                }
            }
        }
    }

    @Retention(SOURCE)
    @IntDef({INPUT_TYPE_TEXT, INPUT_TYPE_PASSWORD, INPUT_TYPE_PHONE, INPUT_TYPE_NUMBER, INPUT_TYPE_NUMBER_DECIMAL})
    public @interface InputType {

    }

    public interface OnFocusChangeListener {
        /**
         * @param customEditText
         * @param editText
         * @param hasFocus
         * @return true 拦截，false不拦截
         */
        boolean onFocusChange(CustomEditText customEditText, EditText editText, boolean hasFocus);
    }

    public interface OnLeftActionClickListener {
        void onLeftActionClick(CustomEditText customEditText, TextView leftActionButton);
    }
}
