package com.ifenduo.lib_http.response;


import android.util.Log;

import com.ifenduo.lib_http.exception.ApiException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;

/**
 * 处理数据和异常
 */
public class ResponseTransformer {
    public static final int RESPONSE_CODE_OK_JAVA = 200;
    public static final int RESPONSE_CODE_OK_PHP = 1;

    public static <T> ObservableTransformer<Response<T>, T> handleResult() {
        return upstream -> upstream
                .onErrorResumeNext(new ErrorResumeFunction<>())
                .flatMap(new ResponseFunction<>());
    }

    /**
     * 非服务器产生的异常，比如本地无无网络请求，Json数据解析错误等等。
     *
     * @param <T>
     */
    private static class ErrorResumeFunction<T> implements Function<Throwable, ObservableSource<? extends Response<T>>> {

        @Override
        public ObservableSource<? extends Response<T>> apply(Throwable throwable) throws Exception {
            return Observable.error(throwable);
        }
    }

    /**
     * 服务其返回的数据解析
     * 正常服务器返回数据和服务器可能返回的exception
     *
     * @param <T>
     */
    private static class ResponseFunction<T> implements Function<Response<T>, ObservableSource<T>> {

        @Override
        public ObservableSource<T> apply(Response<T> tResponse) throws Exception {
            int code = tResponse.getCode();
            String message = tResponse.getMsg();

            if (code == RESPONSE_CODE_OK_JAVA || code == RESPONSE_CODE_OK_PHP) {
                return Observable.just(tResponse.getData());
            } else {
                return Observable.error(new ApiException(code, message));
            }
        }
    }

    /**
     * 创建Data
     *
     * @param t
     * @param <T>
     * @return
     */
//    public static <T> Flowable<T> createData_(final T t) {
//
//        return Flowable.create(emitter -> {
//            try {
//                emitter.onNext(t);
//                emitter.onComplete();
//
//            } catch (Exception e) {
//                emitter.onError(e);
//            }
//        }, BackpressureStrategy.BUFFER);
//
//    }
    public static <T> Observable<T> createData(final T t) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                try {
                    emitter.onNext(t);
                    emitter.onComplete();

                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        });
    }
}
