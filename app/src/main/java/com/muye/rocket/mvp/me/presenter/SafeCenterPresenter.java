package com.muye.rocket.mvp.me.presenter;

import android.text.TextUtils;

import com.muye.rocket.base.BasePresenter;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.muye.rocket.net.CustomResourceSubscriber;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.ApiSignTools;
import com.muye.rocket.api.RApiService;
import com.muye.rocket.entity.SafeSettingEntity;
import com.muye.rocket.mvp.me.contract.RealNameStatusContract;

import java.util.HashMap;
import java.util.Map;

public class SafeCenterPresenter extends BasePresenter<RealNameStatusContract.View> implements RealNameStatusContract.Presenter {
    public SafeCenterPresenter(RealNameStatusContract.View view) {
        super(view);
    }

    @Override
    public void fetchRealNameStatus() {
        Map<String, String> params = new HashMap<>();
        params.put("token", MMKVTools.getToken());
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .fetchSafeSetting(ApiSignTools.convertQueryMap(params))//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<SafeSettingEntity>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.bindRealNameStatus(null);
                            }

                            @Override
                            public void onNext(SafeSettingEntity safeSetting) {
                                super.onNext(safeSetting);
                                if (safeSetting != null) {
                                    MMKVTools.savePayPasswordStatus(safeSetting.isBindTrade());
                                    MMKVTools.saveHasBindGoogleCode(safeSetting.getFuser() != null && safeSetting.getFuser().isFgooglebind());
                                    MMKVTools.saveHasBindPhone(safeSetting.getFuser() != null
                                            && !TextUtils.isEmpty(safeSetting.getFuser().getFtelephone())
                                            && safeSetting.getFuser().isFistelephonebind());
                                    if (safeSetting.getFuser() != null && safeSetting.getFuser().isFhasrealvalidate()) {
                                        MMKVTools.saveRelNameStatus("10");
                                    } else {
                                        if (safeSetting.getIdentity() == null) {
                                            MMKVTools.saveRelNameStatus("");
                                        } else if ("2".equals(safeSetting.getIdentity().getFstatus())) {
                                            MMKVTools.saveRelNameStatus("2");
                                        } else if ("0".equals(safeSetting.getIdentity().getFstatus())) {
                                            MMKVTools.saveRelNameStatus("0");
                                        } else {
                                            MMKVTools.saveRelNameStatus("");
                                        }
                                    }
                                } else {
                                    MMKVTools.savePayPasswordStatus(false);
                                    MMKVTools.saveHasBindGoogleCode(false);
                                    MMKVTools.saveHasBindPhone(false);
                                    MMKVTools.saveRelNameStatus("");
                                }
                                mView.bindRealNameStatus(safeSetting);
                            }
                        }));
    }

    @Override
    public void fetchReject() {

    }
}
