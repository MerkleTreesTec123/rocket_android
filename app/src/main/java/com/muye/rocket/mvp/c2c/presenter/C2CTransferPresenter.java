package com.muye.rocket.mvp.c2c.presenter;

import com.ifenduo.lib_base.entity.BaseEntity;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.C2CApiService;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.mvp.c2c.contract.C2CTransferContract;
import com.muye.rocket.net.CustomResourceSubscriber;

public class C2CTransferPresenter extends BasePresenter<C2CTransferContract.View> implements C2CTransferContract.Presenter {
    public C2CTransferPresenter(C2CTransferContract.View view) {
        super(view);
    }

    @Override
    public void submitTransferSuccess(String coinID, String num, String type, String password) {
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mRetrofit.create(C2CApiService.class)
                        .submitTransfer(MMKVTools.getToken(), coinID, num, type, password)//方法
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
                                super.onNext(entity);
                                mView.hideLoadDialog();
                                mView.onTransferSuccess();
                            }
                        }));
    }
}
