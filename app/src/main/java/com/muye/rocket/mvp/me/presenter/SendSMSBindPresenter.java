package com.muye.rocket.mvp.me.presenter;

import com.ifenduo.lib_base.entity.BaseEntity;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.RApiService;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.mvp.me.contract.SendSMSBindContract;
import com.muye.rocket.net.CustomResourceSubscriber;

public class SendSMSBindPresenter extends BasePresenter<SendSMSBindContract.View> implements SendSMSBindContract.Presenter {
    public SendSMSBindPresenter(SendSMSBindContract.View view) {
        super(view);
    }

    /*
    * 发送绑定手机号验证码
    * */
    @Override
    public void sendSMS(String area, String phone, String type) {
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .sendBindPhoneSMS(area, phone, type)
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<BaseEntity>() {
                            @Override
                            public void onNext(BaseEntity baseModel) {
                                super.onNext(baseModel);
                                mView.hideLoadDialog();
                                mView.onSendSMSSuccess(area, phone);
                            }

                            @Override
                            public void onError(ApiException exception) {
                                mView.hideLoadDialog();
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.onSendSMSFail();
                            }
                        }));
    }

    /*
    * 发送绑定邮箱验证码
    * */
    @Override
    public void sendEmail(String token, String email) {
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .sendEmilJ(token, email)
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<BaseEntity>() {
                            @Override
                            public void onNext(BaseEntity baseModel) {
                                super.onNext(baseModel);
                                mView.hideLoadDialog();
                                mView.onSendEmailSuccess(email);
                            }

                            @Override
                            public void onError(ApiException exception) {
                                mView.hideLoadDialog();
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.onSendEmailFail();
                            }
                        }));
    }

    /*
    * 绑定手机号
    * */
    @Override
    public void bindPhone(String token, String area, String code, String phone) {
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .bindPhone(token, area, phone,code)
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<BaseEntity>() {
                            @Override
                            public void onNext(BaseEntity baseModel) {
                                super.onNext(baseModel);
                                mView.hideLoadDialog();
                                mView.onBindPhone(area,phone);
                            }

                            @Override
                            public void onError(ApiException exception) {
                                mView.hideLoadDialog();
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.onBindPhoneFail();
                            }
                        }));
    }

    /*
    * 绑定邮箱
    * */
    @Override
    public void bindEmail(String token, String code,String email) {
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .bindEmail(token,code)
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<BaseEntity>() {
                            @Override
                            public void onNext(BaseEntity baseModel) {
                                super.onNext(baseModel);
                                mView.hideLoadDialog();
                                mView.onBindPhone("",email);
                            }

                            @Override
                            public void onError(ApiException exception) {
                                mView.hideLoadDialog();
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.onBindPhoneFail();
                            }
                        }));
    }
}
