package com.muye.rocket.tools;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtils {
    /**
     * 运行在子线程
     *
     * @param runnable Runnable
     */
    public static void runOnChildThread(@NonNull Runnable runnable) {
        ThreadPoolHolder.THREAD_POOL.submit(runnable);
    }

    /**
     * 运行在UI线程
     *
     * @param runnable Runnable
     */
    public static void runOnUiThread(@NonNull Runnable runnable) {
        UiThreadHanlderHolder.UI_THREAD_HANDLER.post(runnable);
    }

    /**
     * 获得UI线程Handler
     *
     * @return UI线程Handler
     */
    @NonNull
    public static Handler getUiThreadHandler() {
        return UiThreadHanlderHolder.UI_THREAD_HANDLER;
    }

    /**
     * 线程池Holder
     */
    private static class ThreadPoolHolder {

        /**
         * 线程池
         */
        private static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();

    }

    /**
     * UI线程HandlerHolder
     */
    private static class UiThreadHanlderHolder {

        /**
         * UI线程Handler
         */
        private static final Handler UI_THREAD_HANDLER = new Handler(Looper.getMainLooper());

    }
}
