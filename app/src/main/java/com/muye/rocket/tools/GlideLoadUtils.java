package com.muye.rocket.tools;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * Glide 加载 简单判空封装 防止异步加载数据时调用Glide 抛出异常
 */
public class GlideLoadUtils {
    private String TAG = "GlideLoadUtils";

    /**
     * 借助内部类 实现线程安全的单例模式
     * 属于懒汉式单例，因为Java机制规定，内部类SingletonHolder只有在getInstance()
     * 方法第一次调用的时候才会被加载（实现了lazy），而且其加载过程是线程安全的。
     * 内部类加载的时候实例化一次instance。
     */
    public GlideLoadUtils() {
    }

    private static class GlideLoadUtilsHolder {
        private final static GlideLoadUtils INSTANCE = new GlideLoadUtils();
    }

    public static GlideLoadUtils getInstance() {
        return GlideLoadUtilsHolder.INSTANCE;
    }

    /**
     * Glide 加载 简单判空封装 防止异步加载数据时调用Glide 抛出异常
     *
     * @param context
     * @param url       加载图片的url地址  String
     * @param imageView 加载图片的ImageView 控件
     */
    public void glideLoad(Context context, String url, ImageView imageView) {
        if (context != null) {
            if (context instanceof FragmentActivity) {
                glideLoad((FragmentActivity) context, url, imageView);
            } else if (context instanceof Activity) {
                glideLoad((Activity) context, url, imageView);
            } else if (context instanceof ContextWrapper) {
                Glide.with(context).load(url).into(imageView);
            }
        } else {
            Log.i(TAG, "Picture loading failed,context is null");
        }
    }

    /**
     * Glide 加载 简单判空封装 防止异步加载数据时调用Glide 抛出异常
     *
     * @param context
     * @param drawableID
     * @param imageView 加载图片的ImageView 控件
     */
    public void glideLoad(Context context, @DrawableRes int drawableID, ImageView imageView) {
        if (context != null) {
            if (context instanceof FragmentActivity) {
                glideLoad((FragmentActivity) context, drawableID, imageView);
            } else if (context instanceof Activity) {
                glideLoad((Activity) context, drawableID, imageView);
            } else if (context instanceof ContextWrapper) {
                Glide.with(context).load(drawableID).into(imageView);
            }
        } else {
            Log.i(TAG, "Picture loading failed,context is null");
        }
    }

    public void glideLoad(Context context, @DrawableRes int drawableID, ImageView imageView, RequestOptions requestOptions) {
        if (context != null) {
            if (context instanceof FragmentActivity) {
                glideLoad((FragmentActivity) context, drawableID, imageView, requestOptions);
            } else if (context instanceof Activity) {
                glideLoad((Activity) context, drawableID, imageView, requestOptions);
            } else if (context instanceof ContextWrapper) {
                if (requestOptions != null) {
                    Glide.with(context).load(drawableID).apply(requestOptions).into(imageView);
                } else {
                    Glide.with(context).load(drawableID).into(imageView);
                }
            }
        } else {
            Log.i(TAG, "Picture loading failed,context is null");
        }
    }

    public void glideLoad(Context context, String url, ImageView imageView, RequestOptions requestOptions) {
        if (context != null) {
            if (context instanceof FragmentActivity) {
                glideLoad((FragmentActivity) context, url, imageView, requestOptions);
            } else if (context instanceof Activity) {
                glideLoad((Activity) context, url, imageView, requestOptions);
            } else if (context instanceof ContextWrapper) {
                if (requestOptions != null) {
                    Glide.with(context).load(url).apply(requestOptions).into(imageView);
                } else {
                    Glide.with(context).load(url).into(imageView);
                }
            }
        } else {
            Log.i(TAG, "Picture loading failed,context is null");
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void glideLoad(Activity activity, String url, ImageView imageView) {
        if (!activity.isDestroyed()) {
            Glide.with(activity).load(url).into(imageView);
        } else {
            Log.i(TAG, "Picture loading failed,activity is Destroyed");
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void glideLoad(Activity activity, @DrawableRes int drawableID, ImageView imageView) {
        if (!activity.isDestroyed()) {
            Glide.with(activity).load(drawableID).into(imageView);
        } else {
            Log.i(TAG, "Picture loading failed,activity is Destroyed");
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void glideLoad(Activity activity, String url, ImageView imageView, RequestOptions requestOptions) {
        if (!activity.isDestroyed()) {
            if (requestOptions != null) {
                Glide.with(activity).load(url).apply(requestOptions).into(imageView);
            } else {
                Glide.with(activity).load(url).into(imageView);
            }
        } else {
            Log.i(TAG, "Picture loading failed,activity is Destroyed");
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void glideLoad(Activity activity, @DrawableRes int drawableID, ImageView imageView, RequestOptions requestOptions) {
        if (!activity.isDestroyed()) {
            if (requestOptions != null) {
                Glide.with(activity).load(drawableID).apply(requestOptions).into(imageView);
            } else {
                Glide.with(activity).load(drawableID).into(imageView);
            }
        } else {
            Log.i(TAG, "Picture loading failed,activity is Destroyed");
        }
    }

    public void glideLoad(Fragment fragment, String url, ImageView imageView) {
        if (fragment != null && fragment.getActivity() != null) {
            Glide.with(fragment).load(url).into(imageView);
        } else {
            Log.i(TAG, "Picture loading failed,fragment is null");
        }
    }

    public void glideLoad(Fragment fragment, @DrawableRes int drawableID, ImageView imageView) {
        if (fragment != null && fragment.getActivity() != null) {
            Glide.with(fragment).load(drawableID).into(imageView);
        } else {
            Log.i(TAG, "Picture loading failed,fragment is null");
        }
    }

    public void glideLoad(Fragment fragment, String url, ImageView imageView, RequestOptions requestOptions) {
        if (fragment != null && fragment.getActivity() != null) {
            if (requestOptions != null) {
                Glide.with(fragment).load(url).apply(requestOptions).into(imageView);
            } else {
                Glide.with(fragment).load(url).into(imageView);
            }
        } else {
            Log.i(TAG, "Picture loading failed,fragment is null");
        }
    }

    public void glideLoad(Fragment fragment, @DrawableRes int drawableID, ImageView imageView, RequestOptions requestOptions) {
        if (fragment != null && fragment.getActivity() != null) {
            if (requestOptions != null) {
                Glide.with(fragment).load(drawableID).apply(requestOptions).into(imageView);
            } else {
                Glide.with(fragment).load(drawableID).into(imageView);
            }
        } else {
            Log.i(TAG, "Picture loading failed,fragment is null");
        }
    }

    public void glideLoad(android.app.Fragment fragment, String url, ImageView imageView) {
        if (fragment != null && fragment.getActivity() != null) {
            Glide.with(fragment).load(url).into(imageView);
        } else {
            Log.i(TAG, "Picture loading failed,android.app.Fragment is null");
        }
    }

    public void glideLoad(android.app.Fragment fragment, @DrawableRes int drawableID, ImageView imageView) {
        if (fragment != null && fragment.getActivity() != null) {
            Glide.with(fragment).load(drawableID).into(imageView);
        } else {
            Log.i(TAG, "Picture loading failed,android.app.Fragment is null");
        }
    }

    public void glideLoad(android.app.Fragment fragment, String url, ImageView imageView, RequestOptions requestOptions) {
        if (fragment != null && fragment.getActivity() != null) {
            if (requestOptions != null) {
                Glide.with(fragment).load(url).apply(requestOptions).into(imageView);
            } else {
                Glide.with(fragment).load(url).into(imageView);
            }
        } else {
            Log.i(TAG, "Picture loading failed,android.app.Fragment is null");
        }
    }

    public void glideLoad(android.app.Fragment fragment, @DrawableRes int drawableID, ImageView imageView, RequestOptions requestOptions) {
        if (fragment != null && fragment.getActivity() != null) {
            if (requestOptions != null) {
                Glide.with(fragment).load(drawableID).apply(requestOptions).into(imageView);
            } else {
                Glide.with(fragment).load(drawableID).into(imageView);
            }
        } else {
            Log.i(TAG, "Picture loading failed,android.app.Fragment is null");
        }
    }
}