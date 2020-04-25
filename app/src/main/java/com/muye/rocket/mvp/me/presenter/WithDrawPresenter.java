package com.muye.rocket.mvp.me.presenter;

import android.util.Log;

import com.ifenduo.lib_base.entity.BaseEntity;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.Response;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.RApiService;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.entity.BindWalletBean;
import com.muye.rocket.entity.exc_wallet.RocketBalance;
import com.muye.rocket.mvp.me.contract.WithDrawContract;
import com.muye.rocket.net.CustomResourceSubscriber;
import com.muye.rocket.tools.MD5Tools;
import java.util.List;

public class WithDrawPresenter extends BasePresenter<WithDrawContract.View> implements WithDrawContract.Presenter{
    public WithDrawPresenter(WithDrawContract.View view) {
        super(view);
    }
    /**
     * 提币到哥伦布钱包
     */
    @Override
    public void fetchWithDrawCat(String catId, String coinName, String rocketUid, String num,String password) {
        String userinfo = MD5Tools.MD5(System.currentTimeMillis()+ "");
        String jwt = MD5Tools.MD5(coinName+catId+rocketUid+"rocket");
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mBindWalletRetrofit.create(RApiService.class)
                        .fetchWithDraw(catId,coinName,rocketUid,num,userinfo,password,jwt)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<BaseEntity>() {

                            @Override
                            public void onError(ApiException exception) {
                                mView.hideLoadDialog();
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                            }

                            @Override
                            public void onNext(BaseEntity baseEntity) {
                                super.onNext(baseEntity);
                                mView.hideLoadDialog();
                                mView.withDrawCat(1);
                            }
                        }));
    }

    /**
     * 查询火箭余额
     */
    @Override
    public void fetchRocketBalance(BindWalletBean walletBean) {

        String currMill = System.currentTimeMillis() + "";
        String userinfo = MD5Tools.MD5(currMill);
        String jwt = MD5Tools.MD5(MMKVTools.getUID() + "getyue");
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mBindWalletRetrofit.create(RApiService.class)
                        .searchRocketBalance(MMKVTools.getUID(),userinfo,jwt)//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribeWith(new CustomResourceSubscriber<List<RocketBalance>>() {

                            @Override
                            public void onError(ApiException exception) {
                                mView.hideLoadDialog();
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.searchRocketBalance(null,walletBean);
                            }

                            @Override
                            public void onNext(List<RocketBalance> balanceList) {
                                super.onNext(balanceList);
                                mView.hideLoadDialog();
                                mView.searchRocketBalance(balanceList,walletBean);
                            }
                        }));
    }
}
