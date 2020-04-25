package com.muye.rocket.mvp.c2c.presenter;

import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.C2CApiService;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.mvp.c2c.contract.C2CReleaseContract;
import com.muye.rocket.net.CustomResourceSubscriber;

public class C2CReleasePresenter extends BasePresenter<C2CReleaseContract.View> implements C2CReleaseContract.Presenter {
    public C2CReleasePresenter(C2CReleaseContract.View view) {
        super(view);
    }

    @Override
    public void submitC2CRelease(String coinID, String coinName, String price, String num, String min, String max, String payType, String type, String password) {
        mView.hideLoadDialog();
        mCompositeDisposable.add(
                mRetrofit.create(C2CApiService.class)
                        .c2cRelease(MMKVTools.getToken(), coinID, coinName, price, num, min, max, payType, type, password)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<String>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.hideLoadDialog();
                                mView.onC2CReleaseFail(exception.getCode(), exception.getDisplayMessage());
                            }

                            @Override
                            public void onNext(String releaseID) {
                                mView.hideLoadDialog();
                                super.onNext(releaseID);
                                mView.onC2CReleaseSuccess(releaseID);
                            }
                        }));
    }
}
