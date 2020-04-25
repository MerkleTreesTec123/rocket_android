package com.muye.rocket.mvp.c2c.presenter;

import com.ifenduo.lib_base.entity.BaseEntity;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.C2CApiService;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.mvp.c2c.contract.C2CUpdateReleaseContract;
import com.muye.rocket.net.CustomResourceSubscriber;

public class C2CUpdateReleasePresenter extends BasePresenter<C2CUpdateReleaseContract.View> implements C2CUpdateReleaseContract.Presenter {
    public C2CUpdateReleasePresenter(C2CUpdateReleaseContract.View view) {
        super(view);
    }

    @Override
    public void updateReleaseStatus(String id, String status) {
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mRetrofit.create(C2CApiService.class)
                        .updateC2CUpdateReleaseStatus(MMKVTools.getToken(), id, status)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<BaseEntity>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.hideLoadDialog();
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                            }

                            @Override
                            public void onNext(BaseEntity entity) {
                                mView.hideLoadDialog();
                                super.onNext(entity);
                                mView.onUpdateReleaseStatusSuccess(status);
                            }
                        }));
    }
}
