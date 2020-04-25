package com.muye.rocket.mvp.me.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.InviteRewardDetailEntity;

public interface InviteRewardDetailContract {
    interface View extends BaseView {
        void bindInviteReward(InviteRewardDetailEntity rewardEntity);
    }

    interface Presenter extends RxPresenter {
        void fetchInviteReward(String coinID, int page);
    }
}
