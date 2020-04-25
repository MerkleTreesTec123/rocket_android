package com.muye.rocket.mvp.account.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.muye.rocket.Constant;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.tools.ACache;
import com.star.lockpattern.util.LockPatternUtil;
import com.star.lockpattern.widget.LockPatternIndicator;
import com.star.lockpattern.widget.LockPatternView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/*
 * 创建密码
 * */
public class CreateLockActivity extends BaseActivity implements LockPatternView.OnPatternListener {

    @BindView(R.id.lpi_indicator)
    LockPatternIndicator lpiIndicator;
    @BindView(R.id.tv_message)
    TextView tvMessage;
    @BindView(R.id.lpv_lock)
    LockPatternView lpvLock;
    @BindView(R.id.btn_reset)
    Button btnReset;
    private ACache aCache;
    private static final long DELAYTIME = 600L;
    private List<LockPatternView.Cell> mChosenPattern = null;
    private String mOpenTag;
    private int resetBtnTag = 0;//0重置，1确认密码
    private List<LockPatternView.Cell> mPattern;//密码

    public static void openCreateLockActivity(Context context, String openTag) {
        Intent intent = new Intent(context, CreateLockActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("open", openTag);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void handleIntent(Bundle bundle) {
        super.handleIntent(bundle);
        if (bundle != null) {
            mOpenTag = bundle.getString("open", "");
        }
    }

    private enum Status {
        //默认的状态，刚开始的时候（初始化状态）
        DEFAULT(R.string.create_gesture_default, R.color.hint_color),
        //第一次记录成功
        CORRECT(R.string.create_gesture_correct, R.color.hint_color),
        //连接的点数小于4（二次确认的时候就不再提示连接的点数小于4，而是提示确认错误）
        LESSERROR(R.string.create_gesture_less_error, R.color.colorEF2357),
        //二次确认错误
        CONFIRMERROR(R.string.create_gesture_confirm_error, R.color.colorEF2357),
        //二次确认正确
        CONFIRMCORRECT(R.string.create_gesture_confirm_correct, R.color.hint_color);

        private Status(int strId, int colorId) {
            this.strId = strId;
            this.colorId = colorId;
        }
        private int strId;
        private int colorId;
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_create_lock;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        resetBtnTag = 0;
        btnReset.setText(getString(R.string.create_gesture_reset));

        aCache = ACache.get(this);
        lpvLock.setOnPatternListener(this);

        if (!mOpenTag.equals("set")){
            setNavigationRight(getString(R.string.skip), getResources().getColor(R.color.color292A49), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //直接跳转注册成功
                    goRegistSuccessActivity();
                }
            });
        }
    }

    @OnClick(R.id.btn_reset)
    public void onViewClicked() {
        if (resetBtnTag == 0){//重新设置
            mChosenPattern = null;
            lpiIndicator.setDefaultIndicator();
            updateStatus(Status.DEFAULT, null);
            lpvLock.setPattern(LockPatternView.DisplayMode.DEFAULT);
        }else {//确认密码
            //保存手势密码
            saveChosenPattern(mPattern);
            lpvLock.setPattern(LockPatternView.DisplayMode.DEFAULT);
            if (mOpenTag.equals("set")){//从安全中心进来的确认完成直接关闭
                showToast(getString(R.string.create_gesture_confirm_correct));
                finish();
            }else {//从登录页进来的跳转注册成功页面
                //直接跳转注册成功
                goRegistSuccessActivity();
            }
        }
    }

    /*****************手势监听开始********************/
    @Override
    public void onPatternStart() {
        lpvLock.removePostClearPatternRunnable();
        lpvLock.setPattern(LockPatternView.DisplayMode.DEFAULT);
    }

    @Override
    public void onPatternComplete(List<LockPatternView.Cell> pattern) {
        if(mChosenPattern == null && pattern.size() >= 4) {
            mChosenPattern = new ArrayList<LockPatternView.Cell>(pattern);
            updateStatus(Status.CORRECT, pattern);
        } else if (mChosenPattern == null && pattern.size() < 4) {
            updateStatus(Status.LESSERROR, pattern);
        } else if (mChosenPattern != null) {
            if (mChosenPattern.equals(pattern)) {
                updateStatus(Status.CONFIRMCORRECT, pattern);
            } else {
                updateStatus(Status.CONFIRMERROR, pattern);
            }
        }
    }
    /*****************手势监听结束********************/

    /**
     * 更新状态
     */
    private void updateStatus(Status status, List<LockPatternView.Cell> pattern) {
        tvMessage.setTextColor(getResources().getColor(status.colorId));
        tvMessage.setText(status.strId);
        switch (status) {
            case DEFAULT:
                lpvLock.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case CORRECT:
                updateLockPatternIndicator();
                lpvLock.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case LESSERROR:
                lpvLock.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case CONFIRMERROR:
                lpvLock.setPattern(LockPatternView.DisplayMode.ERROR);
                lpvLock.postClearPatternRunnable(DELAYTIME);
                break;
            case CONFIRMCORRECT:
                //设置密码成功
                resetBtnTag = 1;
                btnReset.setText(getString(R.string.confirm_password));
                mPattern = pattern;
                break;
        }
    }

    /**
     * 更新 Indicator
     */
    private void updateLockPatternIndicator() {
        if (mChosenPattern == null)
            return;
        lpiIndicator.setIndicator(mChosenPattern);
    }

    /**
     * 保存手势密码
     */
    private void saveChosenPattern(List<LockPatternView.Cell> cells) {
        byte[] bytes = LockPatternUtil.patternToHash(cells);
        aCache.put(Constant.GESTURE_PASSWORD, bytes);
    }

    /*
    * 跳转注册成功
    * */
    private void goRegistSuccessActivity(){
        Intent intent = new Intent(this, RegistSuccessActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onClickBack() {
        if (mOpenTag.equals("set")){
            super.onClickBack();
        }else {
            goRegistSuccessActivity();
        }
    }
}
