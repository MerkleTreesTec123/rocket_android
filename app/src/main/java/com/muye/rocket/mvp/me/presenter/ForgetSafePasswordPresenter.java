package com.muye.rocket.mvp.me.presenter;

import com.muye.rocket.base.BasePresenter;
import com.ifenduo.lib_base.entity.BaseEntity;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.muye.rocket.net.CustomResourceSubscriber;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.RApiService;
import com.muye.rocket.mvp.me.contract.ForgetSafePasswordContract;

import java.util.HashMap;
import java.util.Map;

public class ForgetSafePasswordPresenter extends BasePresenter<ForgetSafePasswordContract.View> implements ForgetSafePasswordContract.Presenter {
    public ForgetSafePasswordPresenter(ForgetSafePasswordContract.View view) {
        super(view);
    }


    @Override
    public void submitPayPassword(String password, String smsCode, String googleCode) {
        Map<String, String> params = new HashMap<>();
        params.put("token", MMKVTools.getToken());
        params.put("area", MMKVTools.getArea());
        params.put("phone", MMKVTools.getPhone());
        params.put("newPassword", password);
        params.put("newPassword2", password);
        params.put("code", smsCode);
        params.put("totpCode", googleCode);
//        ApiSignTools.createSignature(MMKVTools.getToken(),MMKVTools.getSecretKey(),"POST",BaseURLConfig.HOST,RURLConfig.URL_FIND_TRANS_PASSWORD,params);
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .findSafePassword(params)//方法
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
}
