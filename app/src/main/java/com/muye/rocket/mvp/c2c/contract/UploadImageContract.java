package com.muye.rocket.mvp.c2c.contract;


import com.muye.rocket.base.BaseView;
import com.muye.rocket.base.RxPresenter;

public interface UploadImageContract {
    interface View extends BaseView {
        void onUploadImageSuccess(String fileID, String url, int tag);

        void onUploadImageFail();
    }

    interface Presenter extends RxPresenter {
        void uploadImage(String imagePath, int tag);
    }
}
