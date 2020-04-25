package com.muye.rocket.mvp.me.presenter;

import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.muye.rocket.net.CustomResourceSubscriber;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.RApiService;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.entity.InviteRewardDetailEntity;
import com.muye.rocket.mvp.me.contract.InviteRewardDetailContract;

public class InviteRewardDetailPresenter extends BasePresenter<InviteRewardDetailContract.View> implements InviteRewardDetailContract.Presenter {
    public InviteRewardDetailPresenter(InviteRewardDetailContract.View view) {
        super(view);
    }

    @Override
    public void fetchInviteReward(String coinID, int page) {
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .fetchInviteRewardDetail(MMKVTools.getToken(), coinID, page)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<InviteRewardDetailEntity>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.bindInviteReward(null);
                            }

                            @Override
                            public void onNext(InviteRewardDetailEntity entity) {
                                super.onNext(entity);
                                mView.bindInviteReward(entity);
                            }
                        }));
    }
}
