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
import com.muye.rocket.entity.exc_wallet.ExcAssetsDetail;
import com.muye.rocket.entity.exc_wallet.ExcAssetsEntity;
import com.muye.rocket.entity.exc_wallet.ExcWithdrawSettingEntity;
import com.muye.rocket.mvp.exc_wallet.contract.ExcWithdrawContract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcWithdrawPresenter extends BasePresenter<ExcWithdrawContract.View> implements ExcWithdrawContract.Presenter {
    public ExcWithdrawPresenter(ExcWithdrawContract.View view) {
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
    public void fetchBalance(String coinID) {
        mCompositeDisposable.add(
                mRetrofit.create(ExcWalletApiService.class)
                        .fetchAssetsDetail(MMKVTools.getToken(), coinID)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<ExcAssetsDetail>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.bindBalance(null);
                            }

                            @Override
                            public void onNext(ExcAssetsDetail assetsDetail) {
                                super.onNext(assetsDetail);
                                mView.bindBalance(assetsDetail);
                            }
                        }));
    }

    @Override
    public void fetchWithdrawSetting(String coinID) {
        Map<String, String> params = new HashMap<>();
        params.put("token", MMKVTools.getToken());
        params.put("symbol", coinID);
        mCompositeDisposable.add(
                mRetrofit.create(ExcWalletApiService.class)
                        .fetchWithdrawSetting(ApiSignTools.convertQueryMap(params))//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<ExcWithdrawSettingEntity>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.bindWithdrawSetting(null);
                            }

                            @Override
                            public void onNext(ExcWithdrawSettingEntity withdrawSettingEntity) {
                                super.onNext(withdrawSettingEntity);
                                mView.bindWithdrawSetting(withdrawSettingEntity == null ? null : withdrawSettingEntity.getWithdrawSetting());
                            }
                        }));
    }

    @Override
    public void submitWithdraw(String coinID, String num, String addressID, String tag, String SMSCode, String password, String googleCode) {
        Map<String, String> params = new HashMap<>();
        params.put("token", MMKVTools.getToken());
        params.put("symbol", coinID);
        params.put("withdrawAmount", num);
        params.put("withdrawAddr", addressID);
        params.put("memo", tag);
        params.put("phoneCode", SMSCode);
        params.put("tradePwd", password);
        params.put("totpCode", TextUtils.isEmpty(googleCode) ? "" : googleCode);
        params.put("btcfeesIndex", "0");
        ApiSignTools.createSignature(MMKVTools.getToken(), MMKVTools.getSecretKey(), "POST", BaseURLConfig.HOST, ExcWalletURLConfig.URL_EXC_WITHDRAW, params);
        mCompositeDisposable.add(
                mRetrofit.create(ExcWalletApiService.class)
                        .submitWithdraw(params)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<BaseEntity>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                            }

                            @Override
                            public void onNext(BaseEntity entity) {
                                super.onNext(entity);
                                mView.onWithdrawSuccess();
                            }
                        }));
    }
}
