package com.muye.rocket.mvp.ieo.presenter;

import android.util.Log;
import android.widget.ImageView;

import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.RApiService;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.entity.MillionCatMyData;
import com.muye.rocket.entity.MillionInviteBean;
import com.muye.rocket.entity.MyWinRecord;
import com.muye.rocket.entity.StartLotteryData;
import com.muye.rocket.entity.WinningPeopleShowData;
import com.muye.rocket.entity.ieo.IEORecord;
import com.muye.rocket.mvp.ieo.contract.MillionCatContract;
import com.muye.rocket.net.CustomResourceSubscriber;
import com.muye.rocket.tools.MD5Tools;

import java.util.List;

public class MillionCatPresenter extends BasePresenter<MillionCatContract.View> implements MillionCatContract.Presenter{
    public MillionCatPresenter(MillionCatContract.View view) {
        super(view);
    }

    @Override
    public void fetchMillionCatResult(String uid,String jwt) {
        String userInfo = MD5Tools.MD5("www.baidu.jwt.tomcat,123");
        mCompositeDisposable.add(
                mBindWalletRetrofit.create(RApiService.class)
                        .requestCanLottery(uid, jwt,userInfo)//方法  ,type 为 1 表示为交易所
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<Integer>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.millionCatResult(null);
                            }

                            @Override
                            public void onNext(Integer catMyData) {
                                super.onNext(catMyData);
                                mView.millionCatResult(catMyData);
                            }
                        }));
    }

    @Override
    public void fetchShowResult(String uid) {
        String userInfo = "www.baidu.jwt.tomcat,123";
        String uids = MD5Tools.MD5(uid);
        String jwt = MD5Tools.MD5("choujiang" + uids + "choujiang");
        mCompositeDisposable.add(
                mBindWalletRetrofit.create(RApiService.class)
                        .recordLottery(uids, jwt,1,userInfo)//方法  ,type 为 1 表示为交易所
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<List<WinningPeopleShowData>>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.winningPeoples(null);
                            }

                            @Override
                            public void onNext(List<WinningPeopleShowData> peopleLists) {
                                super.onNext(peopleLists);
                                mView.winningPeoples(peopleLists);
                            }
                        }));
    }

    /**
     * 我的抽奖记录
     * @param uid
     * @param jwt
     */
    @Override
    public void fetchMyLotteryRecord(String uid,String jwt) {
        String userInfo = "www.baidu.jwt.tomcat,123";
        mCompositeDisposable.add(
                mBindWalletRetrofit.create(RApiService.class)
                        .myWinningRecord(uid, jwt,1,userInfo)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<List<MyWinRecord>>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.myWinRecord(null);
                            }

                            @Override
                            public void onNext(List<MyWinRecord> recordLists) {
                                super.onNext(recordLists);
                                mView.myWinRecord(recordLists);
                            }
                        }));
    }

    /**
     * 点击开始抽奖
     * @param uid
     * @param jwt
     */
    @Override
    public void fetchIsLottery(String uid, String jwt, ImageView animImg,ImageView boxImg) {
        String userInfo = MD5Tools.MD5(System.currentTimeMillis()+"");
        mView.showLoadDialog("正在启动抽奖设备...");
        mCompositeDisposable.add(
                mBindWalletRetrofit.create(RApiService.class)
                        .isStartLottery(uid, jwt,1,userInfo)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<StartLotteryData>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.hideLoadDialog();
                                mView.isStartLottery(null,animImg,boxImg);
                            }

                            @Override
                            public void onNext(StartLotteryData startLotteryData) {
                                super.onNext(startLotteryData);
                                mView.hideLoadDialog();
                                mView.isStartLottery(startLotteryData,animImg,boxImg);
                            }
                        }));
    }

    @Override
    public void fetchInvite(String uid, String s) {
        String jwt = MD5Tools.MD5("yaoqing"+MMKVTools.getUID()+"yaoqing");
        String userinfo = MD5Tools.MD5(System.currentTimeMillis()+"yaoqing");
        mCompositeDisposable.add(
                mBindWalletRetrofit.create(RApiService.class)
                        .fetchMyInviteRecord(uid, jwt,userinfo)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<MillionInviteBean>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.hideLoadDialog();
                                mView.invateRecord(null);
                            }

                            @Override
                            public void onNext(MillionInviteBean inviteBeanList) {
                                super.onNext(inviteBeanList);
                                mView.hideLoadDialog();
                                mView.invateRecord(inviteBeanList);
                            }
                        }));
    }
}
