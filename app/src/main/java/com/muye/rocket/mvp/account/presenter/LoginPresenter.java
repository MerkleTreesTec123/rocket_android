package com.muye.rocket.mvp.account.presenter;

import com.muye.rocket.Constant;
import com.muye.rocket.base.BasePresenter;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.muye.rocket.net.CustomResourceSubscriber;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.RApiService;
import com.muye.rocket.entity.LoginEntity;
import com.muye.rocket.mvp.account.contract.LoginContract;

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {
    public LoginPresenter(LoginContract.View view) {
        super(view);
    }

    @Override
    public void submitLogin(String area, String phone, String password) {
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .submitLogin(area, phone, password, phone.contains("@")?"1":"0")//方法,如果用户名包含@则为邮箱，type为1，否则为0
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<LoginEntity>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.hideLoadDialog();
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                            }

                            @Override
                            public void onNext(LoginEntity entity) {
                                super.onNext(entity);
                                mView.hideLoadDialog();
                                Constant.PAY_PASSWORD = "";
                                if (entity != null) {
                                    MMKVTools.saveSecretKey(entity.getSecretKey());
                                    MMKVTools.saveToken(entity.getToken());
                                    MMKVTools.saveUser(entity.getUserinfo());
                                    MMKVTools.saveLoginPassword(password);
                                }
                                mView.onLoginSuccess(entity);
                            }
                        }));
    }
}
