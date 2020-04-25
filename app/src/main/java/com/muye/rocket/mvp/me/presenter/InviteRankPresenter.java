package com.muye.rocket.mvp.me.presenter;

import com.muye.rocket.base.BasePresenter;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.muye.rocket.net.CustomResourceSubscriber;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.RApiService;
import com.muye.rocket.entity.InviteRankEntity;
import com.muye.rocket.mvp.me.contract.InviteRankContract;

public class InviteRankPresenter extends BasePresenter<InviteRankContract.View> implements InviteRankContract.Presenter {
    public InviteRankPresenter(InviteRankContract.View view) {
        super(view);
    }

    @Override
    public void fetchInviteRank() {
        if (mView != null)
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .fetchInviteRank(MMKVTools.getToken())//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<InviteRankEntity>() {
                            @Override
                            public void onError(ApiException exception) {
                                if (mView != null)
                                mView.hideLoadDialog();
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.bindInviteRank(null);
                            }

                            @Override
                            public void onNext(InviteRankEntity inviteRank) {
                                super.onNext(inviteRank);
                                if (mView != null)
                                mView.hideLoadDialog();
                                mView.bindInviteRank(inviteRank);
                            }
                        }));
    }
}
