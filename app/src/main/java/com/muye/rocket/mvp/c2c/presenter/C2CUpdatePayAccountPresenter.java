package com.muye.rocket.mvp.c2c.presenter;

import com.ifenduo.lib_base.entity.BaseEntity;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.C2CApiService;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.mvp.c2c.contract.C2CUpdatePayAccountContract;
import com.muye.rocket.net.CustomResourceSubscriber;

public class C2CUpdatePayAccountPresenter extends BasePresenter<C2CUpdatePayAccountContract.View> implements C2CUpdatePayAccountContract.Presenter {
    public C2CUpdatePayAccountPresenter(C2CUpdatePayAccountContract.View view) {
        super(view);
    }

    @Override
    public void submitC2CPayAccount(String bankInfo, String alipayInfo, String wechatInfo, String password) {
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mRetrofit.create(C2CApiService.class)
                        .submitC2CPayAccount(MMKVTools.getToken(), "", bankInfo, alipayInfo, wechatInfo, password)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<BaseEntity>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.hideLoadDialog();
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                            }

                            @Override
                            public void onNext(BaseEntity example) {
                                mView.hideLoadDialog();
                                super.onNext(example);
                                mView.onSubmitC2CPayAccountSuccess();
                            }
                        }));
    }
}
