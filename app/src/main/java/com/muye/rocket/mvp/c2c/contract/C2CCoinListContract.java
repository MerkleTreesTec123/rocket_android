package com.muye.rocket.mvp.c2c.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.c2c.C2CCoin;

import java.util.List;

public interface C2CCoinListContract {
    interface View extends BaseView{
        void bindCoinList(List<C2CCoin> coinList);
    }

    interface Presenter extends RxPresenter{
        void fetchCoinList();
    }
}
