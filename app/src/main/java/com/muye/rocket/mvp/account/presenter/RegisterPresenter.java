package com.muye.rocket.mvp.account.presenter;

import com.muye.rocket.base.BasePresenter;
import com.ifenduo.lib_base.entity.BaseEntity;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.muye.rocket.net.CustomResourceSubscriber;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.RApiService;
import com.muye.rocket.mvp.account.contract.RegisterContract;

public class RegisterPresenter extends BasePresenter<RegisterContract.View> implements RegisterContract.Presenter {
    public RegisterPresenter(RegisterContract.View view) {
        super(view);
    }

    @Override
    public void submitRegister(String areaCode, String phone, String smsCode, String password, String payPassword, String inviteCode) {
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .submitPhoneRegister(areaCode, phone, smsCode, password, payPassword, inviteCode, "0", "")//方法
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
                                mView.onRegisterSuccess();
                            }
                        }));
    }

    /**
     * 邮箱注册
     * @param phone
     * @param smsCode
     * @param password
     * @param payPassword
     * @param inviteCode
     */
    @Override
    public void submitEmailRegister(String phone, String smsCode, String password, String payPassword, String inviteCode) {
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .submitEmailRegister(phone, smsCode, password, payPassword, inviteCode, "0", "")//方法
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
                                mView.onRegisterSuccess();
                            }
                        }));
    }
}
