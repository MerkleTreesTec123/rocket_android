package com.muye.rocket.mvp.me.auth;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.ifenduo.lib_base.api.BaseURLConfig;
import com.ifenduo.lib_base.entity.BaseEntity;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_http.exception.ApiException;
import com.ifenduo.lib_http.response.ResponseTransformer;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.api.ApiSignTools;
import com.muye.rocket.api.RApiService;
import com.muye.rocket.api.RURLConfig;
import com.muye.rocket.base.BasePresenter;
import com.muye.rocket.net.CustomResourceSubscriber;
import com.muye.rocket.oss.OSSManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.reactivex.Observable;

/*
* 实名认证
* */
public class RealNameAuthPresenter extends BasePresenter<RealNameAuthContract.View> implements RealNameAuthContract.Presenter {
    public RealNameAuthPresenter(RealNameAuthContract.View view) {
        super(view);
    }

    @Override
    public void uploadImage(String path, int tag) {
        String objectKey = OSSManager.OSS_FILE_PATH + UUID.randomUUID().toString() + ".jpg";
        PutObjectRequest put = new PutObjectRequest(OSSManager.OSS_BUCKET_NAME, objectKey, path);
        mCompositeDisposable.add(
                Observable.just(put)
                        .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                        .subscribe(putObjectRequest -> {
                            PutObjectResult putObjectResult = null;
                            try {
                                putObjectResult = OSSManager.getOss().putObject(putObjectRequest);
                            } catch (ClientException e) {
                                e.printStackTrace();
                                mView.onUploadImageFail(e.getMessage());
                            } catch (ServiceException e) {
                                e.printStackTrace();
                                mView.onUploadImageFail(e.getMessage());
                            }
                            if (putObjectResult != null) {
                                mView.onUploadImageSuccess(OSSManager.OSS_OUT_URL + "/" + putObjectRequest.getObjectKey(), tag);
                            } else {
                                mView.onUploadImageFail("putObjectResult is null");
                            }
                        })
        );
    }

    @Override
    public void submitRealName(String name, String idCard, String frontImage, String backImage, String handleImage,int type) {
        Map<String, String> params = new HashMap<>();
        params.put("token", MMKVTools.getToken());
        params.put("realname", name);
        params.put("identitytype", "0");
        params.put("identityno", idCard);
        params.put("address", "");
        params.put("idCardZmImgURL", frontImage);
        params.put("idCardFmImgURL", backImage);
        params.put("idCardScImgURL", handleImage);
        if (type == 1) params.put("type", type+"");
        if (type == 1){
            mView.showLoadDialog("");
            mCompositeDisposable.add(
                    mRetrofit.create(RApiService.class)
                            .submitRealNameGz(params)//方法
                            .compose(ResponseTransformer.handleResult())//处理返回结果
                            .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                            .subscribeWith(new CustomResourceSubscriber<BaseEntity>() {
                                @Override
                                public void onError(ApiException exception) {
                                    mView.hideLoadDialog();
                                    mView.onError(exception.getCode(), exception.getDisplayMessage());
                                }

                                @Override
                                public void onNext(BaseEntity entity) {
                                    super.onNext(entity);
                                    mView.hideLoadDialog();
                                    mView.onSubmitRealNameSuccess();
                                }
                            }));
        }else {
            ApiSignTools.createSignature(MMKVTools.getToken(), MMKVTools.getSecretKey(), "POST", BaseURLConfig.HOST, RURLConfig.URL_SUBMIT_REL_NAME, params);
            mView.showLoadDialog("");
            mCompositeDisposable.add(
                    mRetrofit.create(RApiService.class)
                            .submitRealName(params)//方法
                            .compose(ResponseTransformer.handleResult())//处理返回结果
                            .compose(SchedulerProvider.getInstance().applySchedulers())//线程转换
                            .subscribeWith(new CustomResourceSubscriber<BaseEntity>() {
                                @Override
                                public void onError(ApiException exception) {
                                    mView.hideLoadDialog();
                                    mView.onError(exception.getCode(), exception.getDisplayMessage());
                                }

                                @Override
                                public void onNext(BaseEntity entity) {
                                    super.onNext(entity);
                                    mView.hideLoadDialog();
                                    mView.onSubmitRealNameSuccess();
                                }
                            }));
        }

    }
}
