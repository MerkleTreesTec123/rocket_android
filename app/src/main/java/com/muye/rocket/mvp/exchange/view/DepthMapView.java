package com.muye.rocket.mvp.exchange.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


import com.ifenduo.common.tool.DimensionTool;
import com.muye.rocket.R;
import com.muye.rocket.entity.exchange.DepthDataBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class DepthMapView extends View {
    public static final String TAG = "DepthMapView";
    private int mWidth;
    //圆点半径
    private int mDotRadius = 2;
    //圆圈半径
    private int mCircleRadius = 8;
    private float mGridWidth;
    private float mBuyGridWidth;
    private float mSellGridWidth;
    //底部价格区域高度
    private int mBottomPriceHeight;
    //右侧委托量绘制个数
    private int mLineCount;
    //背景颜色
    private int mBackgroundColor;

    private boolean mIsHave;
    //是否是长按
    private boolean mIsLongPress;

    //最大的委托量
    private float mMaxVolume;
    private float mMultiple;
    private int mDrawWidth = 0;
    private int mDrawHeight;
    //触摸点的X轴值
    private float mEventX;

    //文案绘制画笔
    private Paint mTextPaint;
    //买入区域边线绘制画笔
    private Paint mBuyLinePaint;
    //卖出区域边线绘制画笔
    private Paint mSellLinePaint;
    //买入区域绘制画笔
    private Paint mBuyPathPaint;
    //卖出取悦绘制画笔
    private Paint mSellPathPaint;
    //选中时圆点绘制画笔
    private Paint mRadioPaint;
    //选中时中间文案背景画笔
    private Paint mSelectorBackgroundPaint;

    private Path mBuyPath = new Path();
    private Path mSellPath = new Path();

    private List<DepthDataBean> mBuyData;
    private List<DepthDataBean> mSellData;

    //    价格显示精度限制
    public int mPriceLimit = 4;
//    private int mVolumeLimit = 5;

    private HashMap<Float, DepthDataBean> mMapX;
    private HashMap<Float, Float> mMapY;
    private Float[] mBottomPrice;
    private GestureDetector mGestureDetector;

    public DepthMapView(Context context) {
        this(context, null);
    }

    public DepthMapView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DepthMapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @SuppressLint("UseSparseArrays")
    private void init(AttributeSet attrs) {
        mBottomPriceHeight = 40;
        mMapX = new HashMap<>();
        mMapY = new HashMap<>();
        mBottomPrice = new Float[4];
        mBuyData = new ArrayList<>();
        mSellData = new ArrayList<>();
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {


            @Override
            public void onLongPress(MotionEvent e) {
                mIsLongPress = true;
                invalidate();
            }


        });

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mBuyLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBuyLinePaint.setStyle(Paint.Style.STROKE);
        mBuyLinePaint.setTextAlign(Paint.Align.CENTER);
        mBuyPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mSellLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSellLinePaint.setStyle(Paint.Style.STROKE);
        mSellLinePaint.setTextAlign(Paint.Align.CENTER);
        mSellPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mRadioPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRadioPaint.setTextAlign(Paint.Align.CENTER);
        mSelectorBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.DepthMapView);
        if (typedArray != null) {
            try {
                mLineCount = typedArray.getInt(R.styleable.DepthMapView_line_count, 4);
                mDotRadius = typedArray.getDimensionPixelSize(R.styleable.DepthMapView_dot_radius, DimensionTool.dp2px(getContext(), mDotRadius));
                mCircleRadius = typedArray.getDimensionPixelSize(R.styleable.DepthMapView_circle_radius, DimensionTool.dp2px(getContext(), mCircleRadius));
                mBackgroundColor = typedArray.getColor(R.styleable.DepthMapView_background_color, getResources().getColor(R.color.depth_background));
                mBuyLinePaint.setStrokeWidth(typedArray.getDimensionPixelSize(R.styleable.DepthMapView_line_width, DimensionTool.dp2px(getContext(), 1)));
                mSellLinePaint.setStrokeWidth(typedArray.getDimensionPixelSize(R.styleable.DepthMapView_line_width, DimensionTool.dp2px(getContext(), 1)));
                mTextPaint.setColor(typedArray.getColor(R.styleable.DepthMapView_text_color, getResources().getColor(R.color.depth_text_color)));
                mTextPaint.setTextSize(typedArray.getDimension(R.styleable.DepthMapView_text_size, 12));
                mBuyLinePaint.setColor(typedArray.getColor(R.styleable.DepthMapView_buy_line_color, getResources().getColor(R.color.depth_buy_line)));
                mSellLinePaint.setColor(typedArray.getColor(R.styleable.DepthMapView_sell_line_color, getResources().getColor(R.color.depth_sell_line)));
                mBuyPathPaint.setColor(typedArray.getColor(R.styleable.DepthMapView_buy_path_color, getResources().getColor(R.color.depth_buy_path)));
                mSellPathPaint.setColor(typedArray.getColor(R.styleable.DepthMapView_sell_path_color, getResources().getColor(R.color.depth_sell_path)));
                mSelectorBackgroundPaint.setColor(typedArray.getColor(R.styleable.DepthMapView_selector_background_color, getResources().getColor(R.color.depth_selector)));

            } finally {
                typedArray.recycle();
            }
        }
        if (mLineCount <= 0) {
            mLineCount = 4;
        }


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        mDrawWidth = mWidth / 2 - 1;
        mDrawHeight = h - mBottomPriceHeight;
    }

    public void setData(List<DepthDataBean> buyData, List<DepthDataBean> sellData) {
        float vol = 0;
        mBuyData.clear();
        mSellData.clear();
        if (buyData.size() > 0) {
            //买入数据按价格进行排序
            Collections.sort(buyData, new comparePrice());
            DepthDataBean depthDataBean;
            //累加买入委托量
            for (int index = buyData.size() - 1; index >= 0; index--) {
                depthDataBean = buyData.get(index);
                vol += depthDataBean.getVolume();
                depthDataBean.setVolume(vol);
                mBuyData.add(0, depthDataBean);
            }
            //修改底部买入价格展示
            mBottomPrice[0] = mBuyData.get(0).getPrice();
            mBottomPrice[1] = mBuyData.get(mBuyData.size() > 1 ? mBuyData.size() - 1 : 0).getPrice();
            mMaxVolume = mBuyData.get(0).getVolume();
        }

        if (sellData.size() > 0) {
            vol = 0;
            //卖出数据按价格进行排序
            Collections.sort(sellData, new comparePrice());
            //累加卖出委托量
            for (DepthDataBean depthDataBean : sellData) {
                vol += depthDataBean.getVolume();
                depthDataBean.setVolume(vol);
                mSellData.add(depthDataBean);
            }
            //修改底部卖出价格展示
            mBottomPrice[2] = mSellData.get(0).getPrice();
            mBottomPrice[3] = mSellData.get(mSellData.size() > 1 ? mSellData.size() - 1 : 0).getPrice();
            mMaxVolume = Math.max(mMaxVolume, mSellData.get(mSellData.size() - 1).getVolume());
        }
        mMaxVolume = mMaxVolume * 1.20f;
        mMultiple = mMaxVolume / mLineCount;
        initData();
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mEventX = event.getX();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mIsLongPress = true;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
//                if (event.getPointerCount() == 1) {
////                    mIsLongPress = true;
//                    invalidate();
//                }
                mIsLongPress = true;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                mIsLongPress = false;
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                mIsLongPress = false;
                invalidate();
                break;
        }
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(mBackgroundColor);
        canvas.save();
        //绘制买入区域
//        drawBuy(canvas);
        //绘制卖出区域
//        drawSell(canvas);
        //绘制界面相关文案
//        drawText(canvas);
        drawDepth(canvas);
        drawAxisX(canvas);
        drawAxisY(canvas);
        if (isLongPress()) {
            drawSelectorView(canvas, getXByEventX());
        }
        canvas.restore();
    }

    private float mMaxPrice;
    private float mMinPrice;
    private float mSpacing;
    private float mMultipleSpacing;

    private void initData() {
        mMaxPrice = 0;
        mMinPrice = 0;

        if (mBuyData.size() > 0 && mSellData.size() > 0) {
            mSpacing = (mWidth - mMultipleSpacing) * 1f / (mSellData.size() + mBuyData.size() - 2);
            mMultipleSpacing = DimensionTool.dp2px(getContext(), 10);
        } else if (mBuyData.size() > 0) {
            mSpacing = mWidth * 1f / (mBuyData.size() - 1);
            mMultipleSpacing = 0;
        } else if (mSellData.size() > 0) {
            mSpacing = mWidth * 1f / (mSellData.size() - 1);
            mMultipleSpacing = 0;
        } else {
            mSpacing = mWidth;
            mMultipleSpacing = 0;
        }
    }

    private void drawDepth(Canvas canvas) {
        mBuyPath.reset();
        if (mBuyData.size() > 1) {
            //购买区域
            for (int i = 0; i < mBuyData.size(); i++) {
                //画线
                if (i >= 1) {
                    float startX = mSpacing * (i - 1);
                    float endX = mSpacing * i;
                    canvas.drawLine(startX, getY(mBuyData.get(i - 1).getVolume()), endX, getY(mBuyData.get(i).getVolume()), mBuyLinePaint);
                }

                //填充色
                if (i == 0) {
                    mBuyPath.moveTo(0, getY(mBuyData.get(0).getVolume()));
                } else {
                    mBuyPath.lineTo(i * mSpacing, getY(mBuyData.get(i).getVolume()));
                }
                if (i == mBuyData.size() - 1) {
                    mBuyPath.lineTo(i * mSpacing, mDrawHeight);
                    mBuyPath.lineTo(0, mDrawHeight);
                    mBuyPath.close();
                }
            }
            canvas.drawPath(mBuyPath, mBuyPathPaint);

            mSellPath.reset();
            float spacingPrice = 0;
            if (mSellData.size() > 1 && mBuyData.size() > 1) {
                spacingPrice = mSellData.get(0).getPrice() - mBuyData.get(mBuyData.size() - 1).getPrice();
            }
            for (int i = 0; i < mSellData.size(); i++) {
                if (i >= 1) {
                    float startX = (mBuyData.size() - 1 + i - 1) * mSpacing + mMultipleSpacing;
                    float endX = (mBuyData.size() - 1 + i) * mSpacing + mMultipleSpacing;
                    canvas.drawLine(startX, getY(mSellData.get(i - 1).getVolume()), endX, getY(mSellData.get(i).getVolume()), mSellLinePaint);
                }

                //填充色
                if (i == 0) {
                    mSellPath.moveTo((mBuyData.size() - 1 ) * mSpacing + mMultipleSpacing, getY(mSellData.get(0).getVolume()));
                } else {
                    mSellPath.lineTo((mBuyData.size() - 1 + i) * mSpacing + mMultipleSpacing, getY(mSellData.get(i).getVolume()));
                }
                if (i == mSellData.size() - 1) {
//                    mSellPath.lineTo((mSellData.get(i).getPrice() - spacingPrice - mMinPrice) / mScaleX + mSpacing, mDrawHeight);
                    mSellPath.lineTo((mBuyData.size() - 1 + i) * mSpacing + mMultipleSpacing, mDrawHeight);
                    mSellPath.lineTo((mBuyData.size() - 1) * mSpacing + mMultipleSpacing, mDrawHeight);
                    mSellPath.lineTo((mBuyData.size() - 1) * mSpacing + mMultipleSpacing, mDrawHeight);
                    mSellPath.close();
                }
            }
            canvas.drawPath(mSellPath, mSellPathPaint);
        }
    }

    private void drawAxisY(Canvas canvas) {
        for (int j = 0; j < mLineCount; j++) {
            float value = mMaxVolume - mMultiple * j;
            String str = getVolumeValue(value);
            canvas.drawText(str, mWidth-mTextPaint.measureText(str), mDrawHeight / mLineCount * j + 30, mTextPaint);
        }
    }

    private void drawAxisX(Canvas canvas) {
        int height = mDrawHeight + mBottomPriceHeight / 2 + 10;
        if (mBuyData.size() >= 1) {
            float buyMin = mBuyData.get(0).getPrice();
            float buyMax = mBuyData.get(mBuyData.size() - 1).getPrice();
            canvas.drawText(getValue(buyMin), 0, height, mTextPaint);
            canvas.drawText(getValue(buyMax), mSpacing * (mBuyData.size() - 1) - mTextPaint.measureText(getValue(buyMax)), height, mTextPaint);
        }

        float spacingPrice = 0;
        if (mSellData.size() > 1 && mBuyData.size() > 1) {
            spacingPrice = mSellData.get(0).getPrice() - mBuyData.get(mBuyData.size() - 1).getPrice();
        }
        if (mSellData.size() >= 1) {
            float sellMin = mSellData.get(0).getPrice();
            float sellMax = mSellData.get(mSellData.size() - 1).getPrice();
            if (mBuyData.size() > 1) {
                canvas.drawText(getValue(sellMin), (mBuyData.size() - 1) * mSpacing + mMultipleSpacing, height, mTextPaint);
                canvas.drawText(getValue(sellMax), (mBuyData.size() - 1 + mSellData.size() - 1) * mSpacing + mMultipleSpacing - mTextPaint.measureText(getValue(sellMax)) + mSpacing, height, mTextPaint);
            }

        }
    }

    private void drawBuy(Canvas canvas) {
        mBuyGridWidth = (mDrawWidth * 1.0f / (mBuyData.size() - 1 == 0 ? 1 : mBuyData.size() - 1));
        mBuyPath.reset();
        mMapX.clear();
        mMapY.clear();
        float x;
        float y;
        for (int i = 0; i < mBuyData.size(); i++) {
            if (i == 0) {
                mBuyPath.moveTo(0, getY(mBuyData.get(0).getVolume()));
            }
            y = getY(mBuyData.get(i).getVolume());
            if (i >= 1) {
                canvas.drawLine(mBuyGridWidth * (i - 1), getY(mBuyData.get(i - 1).getVolume()), mBuyGridWidth * i, y, mBuyLinePaint);
            }
            if (i != mBuyData.size() - 1) {
                mBuyPath.quadTo(mBuyGridWidth * i, y, mBuyGridWidth * (i + 1), getY(mBuyData.get(i + 1).getVolume()));
            }

            x = mBuyGridWidth * i;
            mMapX.put(x, mBuyData.get(i));
            mMapY.put(x, y);
            if (i == mBuyData.size() - 1) {
                mBuyPath.quadTo(mBuyGridWidth * i, y, mBuyGridWidth * i, mDrawHeight);
                mBuyPath.quadTo(mBuyGridWidth * i, mDrawHeight, 0, mDrawHeight);
                mBuyPath.close();
            }
        }
        canvas.drawPath(mBuyPath, mBuyPathPaint);
    }

    private void drawSell(Canvas canvas) {
        mSellGridWidth = (mDrawWidth * 1.0f / (mSellData.size() - 1 == 0 ? 1 : mSellData.size() - 1));
        mSellPath.reset();
        float x;
        float y;
        for (int i = 0; i < mSellData.size(); i++) {
            if (i == 0) {
                mSellPath.moveTo(mDrawWidth + 1, getY(mSellData.get(0).getVolume()));
            }
            y = getY(mSellData.get(i).getVolume());
            if (i >= 1) {
                canvas.drawLine((mSellGridWidth * (i - 1)) + mDrawWidth + 1, getY(mSellData.get(i - 1).getVolume()),
                        (mSellGridWidth * i) + mDrawWidth + 1, y, mSellLinePaint);
            }
            if (i != mSellData.size() - 1) {
                mSellPath.quadTo((mSellGridWidth * i) + mDrawWidth + 1, y,
                        (mSellGridWidth * (i + 1)) + mDrawWidth + 1, getY(mSellData.get(i + 1).getVolume()));
            }
            x = (mSellGridWidth * i) + mDrawWidth + 1;
            mMapX.put(x, mSellData.get(i));
            mMapY.put(x, y);
            if (i == mSellData.size() - 1) {
                mSellPath.quadTo(mWidth, y, (mSellGridWidth * i) + mDrawWidth + 1, mDrawHeight);
                mSellPath.quadTo((mSellGridWidth * i) + mDrawWidth + 1, mDrawHeight, mDrawWidth + 1, mDrawHeight);
                mSellPath.close();
            }
        }
        canvas.drawPath(mSellPath, mSellPathPaint);
    }

    private void drawText(Canvas canvas) {
        float value;
        String str;
        for (int j = 0; j < mLineCount; j++) {
            value = mMaxVolume - mMultiple * j;
            str = getVolumeValue(value);
            canvas.drawText(str, mWidth, mDrawHeight / mLineCount * j + 30, mTextPaint);
        }
        int size = mBottomPrice.length;
        int height = mDrawHeight + mBottomPriceHeight / 2 + 10;
        if (size > 0 && mBottomPrice[0] != null) {
            String data = getValue(mBottomPrice[0]);
            canvas.drawText(data, mTextPaint.measureText(data), height, mTextPaint);
            data = getValue(mBottomPrice[1]);
            canvas.drawText(data, mDrawWidth - 10, height, mTextPaint);
            data = getValue(mBottomPrice[2]);
            canvas.drawText(data, mDrawWidth + mTextPaint.measureText(data) + 10, height, mTextPaint);
            data = getValue(mBottomPrice[3]);
            canvas.drawText(data, mWidth, height, mTextPaint);
        }
        if (mIsLongPress) {
            mIsHave = false;
            for (float key : mMapX.keySet()) {
                if (key == mEventX) {
//                    mLastPosition = mEventX;
//                    drawSelectorView(canvas, key);
                    break;
                }
            }
            if (!mIsHave) {
//                drawSelectorView(canvas, mLastPosition);
            }
        }
    }

    private void drawSelectorView(Canvas canvas, float position) {
        mIsHave = true;
        Log.d(TAG, "drawSelectorView: eventX=" + mEventX + " position=" + position + " " + mMapY.containsKey(position));
        if (mMapY.get(position) == null) return;
        float y = mMapY.get(position);
        if (position < mDrawWidth) {
            canvas.drawCircle(position, y, mCircleRadius, mBuyLinePaint);
            mRadioPaint.setColor(mBuyLinePaint.getColor());
        } else {
            canvas.drawCircle(position, y, mCircleRadius, mSellLinePaint);
            mRadioPaint.setColor(mSellLinePaint.getColor());
        }
        canvas.drawCircle(position, y, mDotRadius, mRadioPaint);

        String volume = getContext().getString(R.string.trust_quantity) + ": " + getVolumeValue(mMapX.get(position).getVolume());
        String price = getContext().getString(R.string.trust_price) + ": " + getValue(mMapX.get(position).getPrice());
        float width = Math.max(mTextPaint.measureText(volume), mTextPaint.measureText(price));
        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        float textHeight = metrics.descent - metrics.ascent;

        int padding = DimensionTool.dp2px(getContext(), 5);
        canvas.drawRoundRect(new RectF(mDrawWidth - width / 2 - padding, 0, mDrawWidth + width / 2 + padding * 2, textHeight * 2 + padding * 2), 10, 10, mSelectorBackgroundPaint);
        canvas.drawText(getContext().getString(R.string.trust_quantity) + ": ",
                mDrawWidth - width / 2 + padding + mTextPaint.measureText(getContext().getString(R.string.trust_quantity)), textHeight + 2, mTextPaint);
        canvas.drawText(getVolumeValue(mMapX.get(position).getVolume()), mDrawWidth + width / 2 + padding, textHeight + 2, mTextPaint);
        canvas.drawText(getContext().getString(R.string.trust_price) + ": ",
                mDrawWidth - width / 2 + padding + mTextPaint.measureText(getContext().getString(R.string.trust_price)), textHeight * 2 + padding, mTextPaint);
        canvas.drawText(getValue(mMapX.get(position).getPrice()), mDrawWidth + width / 2 + padding, textHeight * 2 + padding, mTextPaint);
    }

    public class comparePrice implements Comparator<DepthDataBean> {
        @Override
        public int compare(DepthDataBean o1, DepthDataBean o2) {
            float str1 = o1.getPrice();
            float str2 = o2.getPrice();
            return Float.compare(str1, str2);
        }
    }

    private float getY(float volume) {
        return mDrawHeight - (mDrawHeight) * volume / mMaxVolume;
    }

    private String getValue(float value) {
        return String.format("%." + mPriceLimit + "f", value);
    }

    @SuppressLint("DefaultLocale")
    private String getVolumeValue(float value) {
        return String.format("%.4f", value);
    }

    private float getXByEventX() {
        float x = 0;
        if (mEventX <= mDrawWidth) {//买单区域
            x = Math.round(mEventX / mBuyGridWidth) * mBuyGridWidth;
        } else if (mEventX >= mDrawWidth + 1) {//卖单区域
            x = mDrawWidth + 1 + Math.round((mEventX - 1 - mDrawWidth) / mSellGridWidth) * mSellGridWidth;
        } else {
            x = mDrawWidth;
        }
        Log.d(TAG, "getXByEventX: eventX=" + mEventX + "  x=" + x + " mDrawWidth=" + mDrawWidth);
        return x;
    }

    public boolean isLongPress() {
        return mIsLongPress;
    }
}
