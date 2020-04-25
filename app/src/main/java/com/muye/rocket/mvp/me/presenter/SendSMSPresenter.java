package com.muye.rocket.mvp.me.presenter;

import com.muye.rocket.base.BasePresenter;
import com.ifenduo.lib_base.entity.BaseEntity;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.muye.rocket.net.CustomResourceSubscriber;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.RApiService;
import com.muye.rocket.mvp.me.contract.SendSMSContract;

public class SendSMSPresenter extends BasePresenter<SendSMSContract.View> implements SendSMSContract.Presenter {
    public SendSMSPresenter(SendSMSContract.View view) {
        super(view);
    }

    @Override
    public void sendSMS(String area, String phone, @SendSMSContract.SMSCodeType String type) {
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .sendNotSignatureSMS(area, phone, type)
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

    /**
     * 发送邮箱验证码
     * @param email
     * @param type
     */
    @Override
    public void sendEmail(String email, String type) {
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .sendEmil(email, type)
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
                                mView.onSendSMSFail();
                            }
                        }));
    }


    @Override
    public void checkCode(String phone, String code, @SendSMSContract.SMSCodeType String type) {
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .checkSMSCode(phone, code, type)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<BaseEntity>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.hideLoadDialog();
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.onCheckCodeFail();
                            }

                            @Override
                            public void onNext(BaseEntity entity) {
                                super.onNext(entity);
                                mView.hideLoadDialog();
                                mView.onCheckCodeSuccess(code);
                            }
                        }));
    }

    /**
     * 校验邮箱验证码
     * @param email
     * @param code
     * @param type
     */
    @Override
    public void checkEmailCode(String email, String code, String type) {
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .checkEmailCode(email, code, type)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<BaseEntity>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.hideLoadDialog();
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.onCheckCodeFail();
                            }

                            @Override
                            public void onNext(BaseEntity entity) {
                                super.onNext(entity);
                                mView.hideLoadDialog();
                                mView.onCheckCodeSuccess(code);
                            }
                        }));
    }
}
