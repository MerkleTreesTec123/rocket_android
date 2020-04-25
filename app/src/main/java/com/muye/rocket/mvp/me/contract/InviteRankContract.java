package com.muye.rocket.mvp.me.contract;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;
import com.muye.rocket.entity.InviteRankEntity;

public interface InviteRankContract {
    interface View extends BaseView{
        void bindInviteRank(InviteRankEntity inviteRankEntity);
    }

    interface Presenter extends RxPresenter{
        void fetchInviteRank();
    }
}
