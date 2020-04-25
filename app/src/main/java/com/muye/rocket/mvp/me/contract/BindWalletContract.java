package com.muye.rocket.mvp.me.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.BindWalletBean;
import com.muye.rocket.entity.CoinBean;
import com.muye.rocket.entity.SafeSettingEntity;

import java.util.List;

public interface BindWalletContract {

    interface View extends BaseView {
        void bindWallet(String qrcode);
        void bindCatLists(List<BindWalletBean> catLists);
        void inquireRemaining(List<CoinBean> coinBeans, BindWalletBean walletBean);
        void reflectStatus();
    }

    interface Presenter extends RxPresenter {
        void fetchBindWallet(String uid,String jwt);
        void fetCatLists(String uid,String jwt);
        void fetchRemaining(String uid ,String jwt,BindWalletBean walletBean);
        void fetchReflectCoin(String num,String cat_uid,String coin_name,String password,String rocket_uid);
    }

}
