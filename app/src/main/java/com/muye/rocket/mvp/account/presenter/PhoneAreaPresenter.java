package com.muye.rocket.mvp.account.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.muye.rocket.base.BasePresenter;
import com.ifenduo.lib_http.exception.ApiException;
import com.muye.rocket.net.CustomResourceSubscriber;
import com.ifenduo.lib_http.schedulers.SchedulerProvider;
import com.muye.rocket.RApplication;
import com.muye.rocket.entity.PhoneArea;
import com.muye.rocket.mvp.account.contract.PhoneAreaContract;
import com.muye.rocket.tools.ConvertUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class PhoneAreaPresenter extends BasePresenter<PhoneAreaContract.View> implements PhoneAreaContract.Presenter {
    public PhoneAreaPresenter(PhoneAreaContract.View view) {
        super(view);
    }

    @Override
    public void fetchPhoneArea() {
        mCompositeDisposable.add(Observable.just("area.json").map(new Function<String, List<PhoneArea>>() {
            @Override
            public List<PhoneArea> apply(String s) {
                try {
                    String json = ConvertUtils.toString(RApplication.getContext().getAssets().open(s));
                    Gson gson = new Gson();
                    List<PhoneArea> phoneAreaList = gson.fromJson(json, new TypeToken<ArrayList<PhoneArea>>() {
                    }.getType());
                    return phoneAreaList;
                } catch (IOException e) {
                    e.printStackTrace();
                    return new ArrayList<>();
                }
            }
        }).compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribeWith(new CustomResourceSubscriber<List<PhoneArea>>() {
                    @Override
                    public void onError(ApiException t) {
                        mView.bindPhoneArea(new ArrayList<>());
                    }

                    @Override
                    public void onNext(List<PhoneArea> areaList) {
                        super.onNext(areaList);
                        mView.bindPhoneArea(areaList);
                    }
                }));
    }
}
