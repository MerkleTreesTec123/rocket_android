package com.muye.rocket.mvp.me.auth;

import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;

public interface RealNameAuthContract {
    interface View extends BaseView {
        void onUploadImageSuccess(String imageUrl, int tag);

        void onUploadImageFail(String error);

        void onSubmitRealNameSuccess();
    }

    interface Presenter extends RxPresenter {
        void uploadImage(String path, int tag);

        void submitRealName(String name, String idCard, String frontImage, String backImage, String handleImage, int type);
    }
}
