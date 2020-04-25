package com.muye.rocket.mvp.exc_wallet.presenter;

import com.ifenduo.lib_base.api.BaseURLConfig;
import com.muye.rocket.base.BasePresenter;
import com.ifenduo.lib_base.entity.BaseEntity;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.muye.rocket.net.CustomResourceSubscriber;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.ApiSignTools;
import com.muye.rocket.api.ExcWalletApiService;
import com.muye.rocket.api.ExcWalletURLConfig;
import com.muye.rocket.mvp.exc_wallet.contract.ExcSendSMS2BindPhoneContract;

import java.util.HashMap;
import java.util.Map;

public class ExcSendSMS2BindPhonePresenter extends BasePresenter<ExcSendSMS2BindPhoneContract.View> implements ExcSendSMS2BindPhoneContract.Presenter {
    public ExcSendSMS2BindPhonePresenter(ExcSendSMS2BindPhoneContract.View view) {
        super(view);
    }

    @Override
    public void sendSMS(@ExcSendSMS2BindPhoneContract.SMSType String type) {
        Map<String, String> params = new HashMap<>();
        params.put("token", MMKVTools.getToken());
        params.put("type", type);
        ApiSignTools.createSignature(MMKVTools.getToken(), MMKVTools.getSecretKey(), "POST",
                BaseURLConfig.HOST, ExcWalletURLConfig.URL_EXC_SEND_SMS_BIND, params);
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mRetrofit.create(ExcWalletApiService.class)
                        .sendSMS2BindPhone(ApiSignTools.convertQueryMap(params))//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<BaseEntity>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.hideLoadDialog();
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.onSendSMSFail();
                            }

                            @Override
                            public void onNext(BaseEntity entity) {
                                super.onNext(entity);
                                mView.hideLoadDialog();
                                mView.onSendSMSSuccess();
                            }
                        }));
    }


}
