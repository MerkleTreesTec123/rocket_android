package com.muye.rocket.mvp.ieo;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class IEOTools {

    private @IEOActivityStatus
    int mActivityStatus;
    private @IEORoundStatus
    int mRoundStatus;

    @Retention(SOURCE)
    @IntDef({IEOActivityStatus.IEO_ACTIVITY_STATUS_NOT_START, IEOActivityStatus.IEO_ACTIVITY_STATUS_START,
            IEOActivityStatus.IEO_ACTIVITY_STATUS_STOP})
    public @interface IEOActivityStatus {
        int IEO_ACTIVITY_STATUS_NOT_START = 1;//IEO活动尚未开启
        int IEO_ACTIVITY_STATUS_START = 2;//IEO活动已开启
        int IEO_ACTIVITY_STATUS_STOP = 3;//IEO活动已结束
    }

    @Retention(SOURCE)
    @IntDef({IEORoundStatus.IEO_ROUND_STATUS_NOT_START, IEORoundStatus.IEO_ROUND_STATUS_START,
            IEORoundStatus.IEO_ROUND_STATUS_STOP, IEORoundStatus.IEO_ROUND_STATUS_WAIT_NEXT})
    public @interface IEORoundStatus {
        int IEO_ROUND_STATUS_NOT_START = 1;//IEO兑换尚未开启
        int IEO_ROUND_STATUS_START = 2;//IEO兑换已开启
        int IEO_ROUND_STATUS_STOP = 3;//IEO兑换已结束
        int IEO_ROUND_STATUS_WAIT_NEXT = 4;//IEO下一轮兑换尚未开启
    }

    public void calculationStatus(long systemTime, long startTime, long endTime, long roundStartTime, long roundEndTime, long nextStartTime) {
        if (systemTime < startTime) {
            //活动尚未开始
            mActivityStatus = IEOActivityStatus.IEO_ACTIVITY_STATUS_NOT_START;
        } else if (systemTime < endTime) {
            //活动已开始
            mActivityStatus = IEOActivityStatus.IEO_ACTIVITY_STATUS_START;
        } else {
            //活动已结束
            mActivityStatus = IEOActivityStatus.IEO_ACTIVITY_STATUS_STOP;
        }

        if (systemTime < roundStartTime) {
            //IEO兑换尚未开启
            mRoundStatus = IEORoundStatus.IEO_ROUND_STATUS_NOT_START;
        } else if (systemTime < roundEndTime) {
            //IEO兑换已开启
            mRoundStatus = IEORoundStatus.IEO_ROUND_STATUS_START;
        } else if (systemTime < nextStartTime) {
            //IEO下一轮兑换尚未开启
            mRoundStatus = IEORoundStatus.IEO_ROUND_STATUS_WAIT_NEXT;
        }else {
            //IEO兑换已结束
            mRoundStatus = IEORoundStatus.IEO_ROUND_STATUS_STOP;
        }
    }

    public  @IEOActivityStatus int getActivityStatus() {
        return mActivityStatus;
    }

    public @IEORoundStatus int getRoundStatus() {
        return mRoundStatus;
    }
}
