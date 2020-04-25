package com.muye.rocket.mvp.account.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.muye.rocket.Constant;
import com.muye.rocket.MainActivity;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.tools.ACache;
import com.star.lockpattern.util.LockPatternUtil;
import com.star.lockpattern.widget.LockPatternView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/*
 * 解锁页面
 * */
public class LockLoginActivity extends BaseActivity implements LockPatternView.OnPatternListener {

    @BindView(R.id.tv_message)
    TextView tvMessage;
    @BindView(R.id.lpv_lock)
    LockPatternView lpvLock;
    @BindView(R.id.btn_forget_pwd)
    Button btnForgetPwd;
    private ACache aCache;
    private byte[] gesturePassword;
    private static final long DELAYTIME = 600l;

    private enum Status {
        //默认的状态
        DEFAULT(R.string.gesture_default, R.color.hint_color),
        //密码输入错误
        ERROR(R.string.gesture_error, R.color.colorEF2357),
        //密码输入正确
        CORRECT(R.string.gesture_correct, R.color.hint_color);

        private Status(int strId, int colorId) {
            this.strId = strId;
            this.colorId = colorId;
        }
        private int strId;
        private int colorId;
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_lock_login;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        aCache = ACache.get(this);
        //得到当前用户的手势密码
        gesturePassword = aCache.getAsBinary(Constant.GESTURE_PASSWORD);
        lpvLock.setOnPatternListener(this);
        updateStatus(Status.DEFAULT);
    }

    /**********************手势开始*************************/
    @Override
    public void onPatternStart() {
        lpvLock.removePostClearPatternRunnable();
    }

    @Override
    public void onPatternComplete(List<LockPatternView.Cell> pattern) {
        if(pattern != null){
            if(LockPatternUtil.checkPattern(pattern, gesturePassword)) {
                updateStatus(Status.CORRECT);
            } else {
                updateStatus(Status.ERROR);
            }
        }
    }
    /**********************手势结束*************************/

    /**
     * 更新状态
     * @param status
     */
    private void updateStatus(Status status) {
        tvMessage.setText(status.strId);
        tvMessage.setTextColor(getResources().getColor(status.colorId));
        switch (status) {
            case DEFAULT:
                lpvLock.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case ERROR:
                lpvLock.setPattern(LockPatternView.DisplayMode.ERROR);
                lpvLock.postClearPatternRunnable(DELAYTIME);
                break;
            case CORRECT:
                lpvLock.setPattern(LockPatternView.DisplayMode.DEFAULT);
                //验证成功
                loginGestureSuccess();
                break;
        }
    }

    /**
     * 手势登录成功（去首页）
     */
    private void loginGestureSuccess() {
        Toast.makeText(this, "手势验证成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.btn_forget_pwd)
    public void onViewClicked() {
        Intent intent = new Intent(this, CreateLockActivity.class);
        startActivity(intent);
        this.finish();
    }
}
