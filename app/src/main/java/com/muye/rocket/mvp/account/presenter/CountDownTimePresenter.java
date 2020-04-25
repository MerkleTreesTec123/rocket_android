package com.muye.rocket.mvp.account.presenter;


import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.mvp.account.contract.CountDownTimeContract;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class CountDownTimePresenter extends BasePresenter<CountDownTimeContract.View> implements CountDownTimeContract.Presenter {
    public CountDownTimePresenter(CountDownTimeContract.View view) {
        super(view);
    }

    @Override
    public void startCountDownTime(int totalSeconds) {
        mView.onCountDownTimeStart();
        mCompositeDisposable.add(Observable.intervalRange(0, totalSeconds, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        mView.showCountDownTime(totalSeconds - aLong.intValue());
                    }
                }).doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        mView.onCountDownTimeEnd();
                    }
                }).subscribe());
    }
}
