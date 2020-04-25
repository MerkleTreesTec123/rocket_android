package com.muye.rocket.mvp.me.presenter;

import com.muye.rocket.base.BasePresenter;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.muye.rocket.net.CustomResourceSubscriber;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.RApiService;
import com.muye.rocket.entity.InviteRecord;
import com.muye.rocket.mvp.me.contract.InviteRecordContract;

import java.util.List;

public class InviteRecordPresenter extends BasePresenter<InviteRecordContract.View> implements InviteRecordContract.Presenter {
    public InviteRecordPresenter(InviteRecordContract.View view) {
        super(view);
    }

    @Override
    public void fetchInviteRecord(int page) {
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .fetchInviteRecord(MMKVTools.getToken(), page)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<List<InviteRecord>>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.bindInviteRecord(null);
                            }

                            @Override
                            public void onNext(List<InviteRecord> recordList) {
                                super.onNext(recordList);
                                mView.bindInviteRecord(recordList);
                            }
                        }));
    }
}
