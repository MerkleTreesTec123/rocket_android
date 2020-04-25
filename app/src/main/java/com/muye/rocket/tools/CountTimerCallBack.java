package com.muye.rocket.tools;

/**
 * @author RoreyDiu
 * @Description 时间倒计时回调接口
 * @date 2016-01-04
 */
public interface CountTimerCallBack {
    //计时完成
    public void timerDone();

    //计时过程中
    public void timerDoing(long millisUntilFinished);
}
