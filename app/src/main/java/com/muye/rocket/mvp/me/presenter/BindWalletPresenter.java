package com.muye.rocket.mvp.me.presenter;

import android.util.Log;

import com.ifenduo.lib_base.entity.BaseEntity;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.RApiService;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.entity.BannerEntity;
import com.muye.rocket.entity.BindWalletBean;
import com.muye.rocket.entity.CoinBean;
import com.muye.rocket.mvp.me.contract.BindWalletContract;
import com.muye.rocket.net.CustomResourceSubscriber;
import com.muye.rocket.tools.MD5Tools;

import java.util.List;

public class BindWalletPresenter extends BasePresenter<BindWalletContract.View>  implements  BindWalletContract.Presenter{
    public BindWalletPresenter(BindWalletContract.View view) {
        super(view);
    }

    @Override
    public void fetchBindWallet(String uid,String jwt) {
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mBindWalletRetrofit.create(RApiService.class)
                        .qrcodeAddress(MMKVTools.getUID(),jwt)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<String>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.hideLoadDialog();
                                Log.d("bindWallet","------地址------------->  " + exception.getCode());
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                            }

                            @Override
                            public void onNext(String codeAdd) {
                                super.onNext(codeAdd);
                                mView.hideLoadDialog();
                                mView.bindWallet(codeAdd);
                            }
                        }));
    }

    /**
     * 查询绑定cat的列表
     * @param uid
     * @param jwt
     */
    @Override
    public void fetCatLists(String uid, String jwt) {
        mCompositeDisposable.add(
                mBindWalletRetrofit.create(RApiService.class)
                        .bindWalletLists(MMKVTools.getUID(),jwt)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<List<BindWalletBean>>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                            }

                            @Override
                            public void onNext(List<BindWalletBean> codeAdd) {
                                super.onNext(codeAdd);
                                mView.bindCatLists(codeAdd);
                            }
                        }));
    }

    @Override
    public void fetchRemaining(String uid, String jwt,BindWalletBean walletBean) {
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mBindWalletRetrofit.create(RApiService.class)
                        .requireRemain(uid,jwt)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<List<CoinBean>>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.hideLoadDialog();
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                            }

                            @Override
                            public void onNext(List<CoinBean> coinBeans) {
                                super.onNext(coinBeans);
                                mView.hideLoadDialog();
                                mView.inquireRemaining(coinBeans,walletBean);
                            }
                        }));
    }

    /**
     * 提币接口
     * @param num
     * @param cat_uid
     * @param coin_name
     * @param rocket_uid
     */
    @Override
    public void fetchReflectCoin(String num, String cat_uid, String coin_name,String password, String rocket_uid) {
        String currMill = System.currentTimeMillis()+"";
        String userInfo = MD5Tools.MD5(currMill);
        String jwt = MD5Tools.MD5(coin_name + cat_uid + rocket_uid + "cat" + userInfo);
        if (mView != null)
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mBindWalletRetrofit.create(RApiService.class)
                        .reflectCoinFreePassWord(num,cat_uid,coin_name,rocket_uid,userInfo,password,jwt)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<BaseEntity>() {
                            @Override
                            public void onError(ApiException exception) {
                                if (mView != null)
                                mView.hideLoadDialog();
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                            }

                            @Override
                            public void onNext(BaseEntity entity) {
                                super.onNext(entity);
                                if (mView != null)
                                mView.hideLoadDialog();
                                mView.reflectStatus();
                            }
                        }));
    }
}
