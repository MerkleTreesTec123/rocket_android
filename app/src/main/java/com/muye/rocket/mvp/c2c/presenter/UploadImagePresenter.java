package com.muye.rocket.mvp.c2c.presenter;

import com.ifenduo.lib_base.api.BaseURLConfig;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.RApiService;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.entity.UploadResult;
import com.muye.rocket.mvp.c2c.contract.UploadImageContract;
import com.muye.rocket.net.CustomResourceSubscriber;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UploadImagePresenter extends BasePresenter<UploadImageContract.View> implements UploadImageContract.Presenter {
    public UploadImagePresenter(UploadImageContract.View view) {
        super(view);
    }

    @Override
    public void uploadImage(String imagePath, final int tag) {
        File file = new File(imagePath);
        String fileName=file.getName();
        try {
            fileName= URLEncoder.encode(file.getName(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", fileName, requestBody);
        mView.showLoadDialog("");
        mCompositeDisposable.add(
                mRetrofit.create(RApiService.class)
                        .uploadImage(body, MMKVTools.getToken(), BaseURLConfig.AUTH, "jpg")//方法
//                        .uploadImage(body, MMKVTools.getToken(), BaseURLConfig.AUTH, fileName.substring(fileName.lastIndexOf(".") + 1))//方法
                        .compose(ResponseTransformer.handleResult())//处理返回结果
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换151
                        .subscribeWith(new CustomResourceSubscriber<UploadResult>() {
                            @Override
                            public void onError(ApiException exception) {
                                mView.hideLoadDialog();
                                mView.onError(exception.getCode(), exception.getDisplayMessage());
                                mView.onUploadImageFail();
                            }

                            @Override
                            public void onNext(UploadResult result) {
                                mView.hideLoadDialog();
                                super.onNext(result);
                                String fileID = result == null ? "" : result.getId();
                                String url = result == null ? "" : result.getUrl();
                                mView.onUploadImageSuccess(fileID, url, tag);
                            }
                        }));
    }
}
