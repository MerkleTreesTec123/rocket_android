package com.muye.rocket.mvp.account.presenter;

import com.muye.rocket.base.BasePresenter;
import com.ifenduo.lib_base.entity.BaseEntity;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.muye.rocket.net.CustomResourceSubscriber;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.RApiService;
import com.muye.rocket.mvp.account.contract.SendSMSContract;

public class SendSMSPresenter extends BasePresenter<SendSMSContract.View> implements SendSMSContract.Presenter {

    public SendSMSPresenter(SendSMSContract.View view) {
        super(view);
    }

    @Override
    public void sendNotSignatureSMS(String areaCode, String phone, String type) {
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .sendNotSignatureSMS(areaCode, phone, type)
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<BaseEntity>() {
                            @Override
                            public void onNext(BaseEntity baseModel) {
                                super.onNext(baseModel);
                                mView.hideLoadDialog();
                                mView.onSendSMSSuccess(areaCode, phone);
                            }

                            @Override
                            public void onError(ApiException exception) {
                                mView.hideLoadDialog();
                                mView.onSendSMSFail(exception.getCode(), exception.getDisplayMessage());
                            }
                        }));
    }
}
