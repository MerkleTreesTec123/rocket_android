package com.muye.rocket.mvp.account.presenter;

import android.text.TextUtils;

import com.muye.rocket.base.BasePresenter;
import com.ifenduo.lib_base.entity.BaseEntity;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.muye.rocket.net.CustomResourceSubscriber;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.RApiService;
import com.muye.rocket.mvp.account.contract.ForgetPasswordContract;

import java.util.HashMap;
import java.util.Map;

public class ForgetPasswordPresenter extends BasePresenter<ForgetPasswordContract.View> implements ForgetPasswordContract.Presenter {
    public ForgetPasswordPresenter(ForgetPasswordContract.View view) {
        super(view);
    }


    @Override
    public void submitPassword(String area, String phone, String password, String smsCode, String googleCode) {
        Map<String, String> params = new HashMap<>();
        params.put("area", area);
        params.put("phone", phone);
        params.put("newPassword", password);
        params.put("newPassword2", password);
        params.put("code", smsCode);
        params.put("totpCode", googleCode);
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .findLoginPassword(params)//方法
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
                                String area_ = MMKVTools.getArea();
                                String phone_ = MMKVTools.getPhone();
                                if (!TextUtils.isEmpty(area_) && !TextUtils.isEmpty(phone_) && area_.equals(area) && phone_.equals(phone)) {
                                    MMKVTools.saveLoginPassword(password);
                                }
                                mView.hideLoadDialog();
                                mView.onSubmitPasswordSuccess();
                            }
                        }));
    }
}
