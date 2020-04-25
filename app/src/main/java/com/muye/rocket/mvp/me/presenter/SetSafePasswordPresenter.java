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
import com.muye.rocket.mvp.me.contract.SetSafePasswordContract;

import java.util.HashMap;
import java.util.Map;

public class SetSafePasswordPresenter extends BasePresenter<SetSafePasswordContract.View> implements SetSafePasswordContract.Presenter {
    public SetSafePasswordPresenter(SetSafePasswordContract.View view) {
        super(view);
    }

    @Override
    public void submitPayPassword(String oldPassword, String password, String smsCode, String googleCode, String idCard) {
        Map<String, String> params = new HashMap<>();
        params.put("token", MMKVTools.getToken());
        params.put("originPwd", oldPassword);
        params.put("newPwd", password);
        params.put("reNewPwd", password);
        params.put("phoneCode", smsCode);
        params.put("totpCode", googleCode);
        params.put("identityCode", idCard);
        params.put("pwdType", "1");
        ApiSignTools.createSignature(MMKVTools.getToken(), MMKVTools.getSecretKey(), "POST", BaseURLConfig.HOST, RURLConfig.URL_SUBMIT_PASSWORD, params);
        mView.showLoadDialog("正在修改密码");
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
                                mView.hideLoadDialog();
                                mView.onSubmitPayPasswordSuccess();
                            }
                        }));
    }

    /**
     * 修改支付密码先验证手机验证码
     * @param code
     * @param phone
     */
    @Override
    public void fetchPhoneCode(String code, String phone) {
        mView.showLoadDialog("正在校验验证码");
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .updatePayPasswordWord(MMKVTools.getToken(), phone, code,MMKVTools.getArea())
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<BaseEntity>() {
                            @Override
                            public void onNext(BaseEntity baseModel) {
                                super.onNext(baseModel);
                                mView.hideLoadDialog();
                                mView.onVerifyPhoenCode();
                            }

                            @Override
                            public void onError(ApiException exception) {
                                mView.hideLoadDialog();
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                            }
                        }));
    }
}
