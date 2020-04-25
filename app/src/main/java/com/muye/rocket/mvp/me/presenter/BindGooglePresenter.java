package com.muye.rocket.mvp.me.presenter;

import android.util.Log;

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
import com.muye.rocket.entity.GoogleDevice;
import com.muye.rocket.mvp.me.contract.BindGoogleContract;

import java.util.HashMap;
import java.util.Map;

public class BindGooglePresenter extends BasePresenter<BindGoogleContract.View> implements BindGoogleContract.Presenter {
    public BindGooglePresenter(BindGoogleContract.View view) {
        super(view);
    }

    @Override
    public void fetchGoogleDevice() {
        Map<String, String> params = new HashMap<>();
        params.put("token", MMKVTools.getToken());
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .fetchGoogleDevice(ApiSignTools.convertQueryMap(params))//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<GoogleDevice>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.hideLoadDialog();
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.bindGoogleDevice(null);
                            }

                            @Override
                            public void onNext(GoogleDevice device) {
                                super.onNext(device);
                                mView.hideLoadDialog();
                                mView.bindGoogleDevice(device);
                            }
                        }));
    }

    @Override
    public void bindGoogle(String code, String key) {
        Log.d("TAG", "bindGoogle: "+code+" "+key);
        Map<String, String> params = new HashMap<>();
        params.put("token", MMKVTools.getToken());
        params.put("code", code);
        params.put("totpKey", key);
        ApiSignTools.createSignature(MMKVTools.getToken(), MMKVTools.getSecretKey(), "POST", BaseURLConfig.HOST, RURLConfig.URL_BIND_GOOGLE_CODE, params);
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .bindGoogle(params)//方法
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
                                mView.onBindGoogleSuccess();
                            }
                        }));
    }
}
