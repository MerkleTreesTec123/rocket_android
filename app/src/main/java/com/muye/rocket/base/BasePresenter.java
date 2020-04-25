package com.muye.rocket.base;


import com.ifenduo.lib_base.net.RetrofitServiceManager;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Retrofit;

public abstract class BasePresenter<T extends BaseView> implements RxPresenter {
    protected T mView;
    //用于管理RxObservable 防止内存溢出
    protected CompositeDisposable mCompositeDisposable;
    //网络请求Retrofit
    protected Retrofit mRetrofit,mBindWalletRetrofit,mUrlRetrofit;

    public BasePresenter(T view) {
        mView = view;
        mCompositeDisposable = new CompositeDisposable();
        mRetrofit = RetrofitServiceManager.getInstance().getRetrofit();
        mBindWalletRetrofit = RetrofitServiceManager.getInstance().getmBindWalletRetrofit();
        mUrlRetrofit = RetrofitServiceManager.getInstance().getmUrlRetrofit();
    }

    @Override
    public void dropView() {
        mCompositeDisposable.clear();
    }
}
