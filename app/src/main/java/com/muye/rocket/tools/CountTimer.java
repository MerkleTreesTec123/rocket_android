package com.muye.rocket.tools;

/**
 * @author RoreyDiu
 * @Description 时间倒计时（例如短信的60秒倒计时）
 * @date 2016-01-04
 */
public class CountTimer extends android.os.CountDownTimer {

    private CountTimerCallBack myCountTimerCallBack;

    public CountTimer(long millisInFuture, long countDownInterval, CountTimerCallBack myCountTimerCallBack) {
        /*
             * millisInFuture 总时长,countDownInterval 计时的时间间隔
			 */
        super(millisInFuture, countDownInterval);
        this.myCountTimerCallBack = myCountTimerCallBack;
    }

    //计时器启动的时候会自动回调这两个方法
    @Override
    public void onTick(long millisUntilFinished) {
        myCountTimerCallBack.timerDoing(millisUntilFinished);
    }

    @Override
    public void onFinish() {
        myCountTimerCallBack.timerDone();
    }


}
