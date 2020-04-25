package com.muye.rocket.mvp.ieo.contract;

import android.content.Intent;
import android.widget.ImageView;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.MillionCatMyData;
import com.muye.rocket.entity.MillionInviteBean;
import com.muye.rocket.entity.MyWinRecord;
import com.muye.rocket.entity.StartLotteryData;
import com.muye.rocket.entity.WinningPeopleShowData;

import java.util.List;

public interface MillionCatContract {
    interface View extends BaseView {
        void millionCatResult(Integer cat);
        void winningPeoples(List<WinningPeopleShowData> peopleShowData);
        void myWinRecord(List<MyWinRecord> recordList);
        void isStartLottery(StartLotteryData lotteryData, ImageView animImg, ImageView boxImg);
        void invateRecord(MillionInviteBean inviteBeanList);
    }

    interface Presenter extends RxPresenter {
        // 查询抽奖结果
        void fetchMillionCatResult(String uid,String jwt);
        // 查询界面展示中奖手机号以及中奖结果
        void fetchShowResult(String uid);
        // 查询我的抽奖记录
        void fetchMyLotteryRecord(String uid,String jwt);
        // 查询是否可以进行抽奖
        void fetchIsLottery(String uid,String jwt ,ImageView animImg,ImageView boxImg);
        // 查询我的邀请记录
        void fetchInvite(String uid,String jwt);
    }
}
