package com.muye.rocket.mvp.me.presenter;

import com.ifenduo.lib_base.api.BaseURLConfig;
import com.muye.rocket.api.RURLConfig;
import com.muye.rocket.base.BasePresenter;
import com.ifenduo.lib_base.entity.BaseEntity;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.muye.rocket.net.CustomResourceSubscriber;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.ApiSignTools;
import com.muye.rocket.api.RApiService;
import com.muye.rocket.mvp.me.contract.UpdateLoginPasswordContract;

import java.util.HashMap;
import java.util.Map;

public class UpdateLoginPasswordPresenter extends BasePresenter<UpdateLoginPasswordContract.View> implements UpdateLoginPasswordContract.Presenter {
    public UpdateLoginPasswordPresenter(UpdateLoginPasswordContract.View view) {
        super(view);
    }

    @Override
    public void submitLoginPassword(String oldPassword, String password,  String googleCode) {
        Map<String, String> params = new HashMap<>();
        params.put("token", MMKVTools.getToken());
        params.put("originPwd", oldPassword);
        params.put("newPwd", password);
        params.put("reNewPwd", password);
        params.put("phoneCode", "");
        params.put("totpCode", googleCode);
        params.put("identityCode", "");
        params.put("pwdType", "0");
        ApiSignTools.createSignature(MMKVTools.getToken(), MMKVTools.getSecretKey(), "POST", BaseURLConfig.HOST, RURLConfig.URL_SUBMIT_PASSWORD, params);
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .submitPassword(params)//方法
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
                                MMKVTools.saveLoginPassword(password);
                                mView.hideLoadDialog();
                                mView.onSubmitLoginPasswordSuccess();
                            }
                        }));
    }
}
