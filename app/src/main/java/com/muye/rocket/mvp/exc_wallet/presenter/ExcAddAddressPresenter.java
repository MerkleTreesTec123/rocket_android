package com.muye.rocket.mvp.exc_wallet.presenter;

import android.text.TextUtils;

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
import com.muye.rocket.entity.exc_wallet.ExcAssetsEntity;
import com.muye.rocket.mvp.exc_wallet.contract.ExcAddAddressContract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcAddAddressPresenter extends BasePresenter<ExcAddAddressContract.View> implements ExcAddAddressContract.Presenter {
    public ExcAddAddressPresenter(ExcAddAddressContract.View view) {
        super(view);
    }

    @Override
    public void fetchCoinList() {
        Map<String, String> params = new HashMap<>();
        params.put("token", MMKVTools.getToken());
        params.put("base", "CNY");
        mCompositeDisposable.add(
                mRetrofit.create(ExcWalletApiService.class)
                        .fetchAssets(ApiSignTools.convertQueryMap(params))//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<ExcAssetsEntity>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.bindCoinList(null);
                            }

                            @Override
                            public void onNext(ExcAssetsEntity assets) {
                                super.onNext(assets);
                                List<ExcAssetsEntity.WalletBean> coinList = null;
                                if (assets != null) {
                                    coinList = assets.getWallet();
                                }
                                mView.bindCoinList(coinList);
                            }
                        }));
    }

    @Override
    public void submitAddAddress(String coinID, String address, String remark, String code, String password, String googleCode) {
        Map<String, String> params = new HashMap<>();
        params.put("token", MMKVTools.getToken());
        params.put("symbol", coinID);
        params.put("withdrawAddr", address);
        params.put("remark", remark);
        params.put("totpCode", TextUtils.isEmpty(googleCode) ? "" : googleCode);
        params.put("password", password);
        params.put("phoneCode", code);
        ApiSignTools.createSignature(MMKVTools.getToken(), MMKVTools.getSecretKey(), "POST",
                BaseURLConfig.HOST, ExcWalletURLConfig.URL_EXC_WALLET_ADD_ADDRESS, params);
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mRetrofit.create(ExcWalletApiService.class)
                        .submitNewAddress(params)//方法
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
                                mView.onAddAddressSuccess(coinID);
                            }
                        }));
    }
}
