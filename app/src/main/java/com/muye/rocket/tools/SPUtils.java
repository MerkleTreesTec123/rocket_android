package com.muye.rocket.tools;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.muye.rocket.RApplication;
import com.muye.rocket.entity.exchange.ExcTradingArea;
import com.muye.rocket.entity.exchange.ExcTradingPair;
import com.muye.rocket.entity.exchange.KLineEntity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class SPUtils {
    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;
    private static final String FILE_NAME = "SpUtils";
    public static final String CURRENT_USER_REGISRID = "rocket_user_registid";
    public static final String JPUSH_IS_ONCLICK_DIALOG = "push_notification_click";
    public static final String JPUSH_IS_ONCLICK_WENURL = "push_notific_weburl_click";


    private static SharedPreferences getSp(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences("SpUtils", Context.MODE_PRIVATE);
            editor = sp.edit();
        }
        return sp;
    }

    /**
     * 保存数据到文件
     *
     * @param context
     * @param key
     * @param data
     */
    public static void saveData(Context context, String key, Object data) {

        String type = data.getClass().getSimpleName();
        android.content.SharedPreferences sharedPreferences = context
                .getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();

        if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) data);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) data);
        } else if ("String".equals(type)) {
            editor.putString(key, (String) data);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) data);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) data);
        }

        editor.commit();
    }

    /**
     * 从文件中读取数据
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static Object getData(Context context, String key, Object defValue) {

        String type = defValue.getClass().getSimpleName();
        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences
                (FILE_NAME, Context.MODE_PRIVATE);

        //defValue为为默认值，如果当前获取不到数据就返回它
        if ("Integer".equals(type)) {
            return sharedPreferences.getInt(key, (Integer) defValue);
        } else if ("Boolean".equals(type)) {
            return sharedPreferences.getBoolean(key, (Boolean) defValue);
        } else if ("String".equals(type)) {
            return sharedPreferences.getString(key, (String) defValue);
        } else if ("Float".equals(type)) {
            return sharedPreferences.getFloat(key, (Float) defValue);
        } else if ("Long".equals(type)) {
            return sharedPreferences.getLong(key, (Long) defValue);
        }
        return null;
    }

    /*private static SharedPreferences getSp2(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences("SpUtils2", Context.MODE_PRIVATE);
            editor = sp.edit();
        }
        return sp;
    }*/

    /**
     * 存入字符串
     *
     * @param context 上下文
     * @param key     字符串的键
     * @param value   字符串的值
     */
    public static void putString(Context context, String key, String value) {
        SharedPreferences preferences = getSp(context);
        //存入数据
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 获取字符串
     *
     * @param context 上下文
     * @param key     字符串的键
     * @return 得到的字符串
     */
    public static String getString(Context context, String key) {
        SharedPreferences preferences = getSp(context);
        return preferences.getString(key, "");
    }

    /**
     * 获取字符串
     *
     * @param context 上下文
     * @param key     字符串的键
     * @param value   字符串的默认值
     * @return 得到的字符串
     */
    public static String getString(Context context, String key, String value) {
        SharedPreferences preferences = getSp(context);
        return preferences.getString(key, value);
    }

    /**
     * 保存布尔值
     *
     * @param context 上下文
     * @param key     键
     * @param value   值
     */
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = getSp(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 获取布尔值
     *
     * @param context  上下文
     * @param key      键
     * @param defValue 默认值
     * @return 返回保存的值
     */
    public static boolean getBoolean(Context context, String key, boolean defValue) {
        SharedPreferences sp = getSp(context);
        return sp.getBoolean(key, defValue);
    }

    /**
     * 保存long值
     *
     * @param context 上下文
     * @param key     键
     * @param value   值
     */
    public static void putLong(Context context, String key, long value) {
        SharedPreferences sp = getSp(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 获取long值
     *
     * @param context  上下文
     * @param key      键
     * @param defValue 默认值
     * @return 保存的值
     */
    public static long getLong(Context context, String key, long defValue) {
        SharedPreferences sp = getSp(context);
        return sp.getLong(key, defValue);
    }

    /**
     * 保存int值
     *
     * @param context 上下文
     * @param key     键
     * @param value   值
     */
    public static void putInt(Context context, String key, int value) {
        SharedPreferences sp = getSp(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 获取long值
     *
     * @param context  上下文
     * @param key      键
     * @param defValue 默认值
     * @return 保存的值
     */
    public static int getInt(Context context, String key, int defValue) {
        SharedPreferences sp = getSp(context);
        return sp.getInt(key, defValue);
    }

    /**
     * 保存对象
     *
     * @param context 上下文
     * @param key     键
     * @param obj     要保存的对象（Serializable的子类）
     * @param <T>     泛型定义
     */
    public static <T extends Serializable> void putObject(Context context, String key, T obj) {
        try {
            put(context, key, obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取对象
     *
     * @param context 上下文
     * @param key     键
     * @param <T>     指定泛型
     * @return 泛型对象
     */
    public static <T extends Serializable> T getObject(Context context, String key) {
        try {
            return (T) get(context, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    * 保存首页三个区域
    * */
    /*public static void setTradingArea(Context context,String tag, List<ExcTradingArea> datalist) {
        if (null == datalist || datalist.size() <= 0)
            return;
        SharedPreferences sp = getSp2(context);
        SharedPreferences.Editor edit = sp.edit();
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        edit.clear();
        edit.putString(tag, strJson);
        edit.commit();
    }*/

    /*
     * 获取首页三个区域
     * */
    /*public static List<ExcTradingArea> getTradingArea(Context context,String tag) {
        List<ExcTradingArea> datalist = new ArrayList<>();
        SharedPreferences sp = getSp2(context);
        String strJson = sp.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<ExcTradingArea>>() {
        }.getType());
        return datalist;
    }*/

    /*
     * 保存首页交易对列表
     * */
    public static void setTradingPair(Context context,String tag, List<ExcTradingPair> datalist) {
        if (null == datalist || datalist.size() <= 0)
            return;
        SharedPreferences sp = getSp(context);
        SharedPreferences.Editor edit = sp.edit();
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
//        edit.clear();
        edit.remove(tag);
        edit.putString(tag, strJson);
        edit.commit();
    }

    /*
     * 获取首页交易对列表
     * */
    public static List<ExcTradingPair> getTradingPair(Context context,String tag) {
        List<ExcTradingPair> datalist = new ArrayList<>();
        SharedPreferences sp = getSp(context);
        String strJson = sp.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<ExcTradingPair>>() {
        }.getType());
        return datalist;
    }

    /**
     * 保存List
     *
     * @param tag
     * @param datalist
     */
    public static void setDataList(Context context,String tag, List<KLineEntity> datalist) {
        if (null == datalist || datalist.size() <= 0)
            return;
        SharedPreferences sp = getSp(context);
        SharedPreferences.Editor edit = sp.edit();
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
//        edit.clear();
        edit.remove(tag);
        edit.putString(tag, strJson);
        edit.commit();

    }

    /**
     * 获取List
     *
     * @param tag
     * @return
     */
    public static List<KLineEntity> getDataList(Context context,String tag) {
        List<KLineEntity> datalist = new ArrayList<>();
        SharedPreferences sp = getSp(context);
        String strJson = sp.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<KLineEntity>>() {
        }.getType());
        return datalist;

    }

    /**
     * 存储Map集合
     *
     * @param key 键
     * @param map 存储的集合
     * @param <K> 指定Map的键
     * @param <T> 指定Map的值
     */

    public static <K, T> void setMap(Context context,String key, ConcurrentHashMap<K, T> map) {
        if (map == null || map.isEmpty() || map.size() < 1) {
            return;
        }
        SharedPreferences sp = getSp(context);
        SharedPreferences.Editor edit = sp.edit();
        Gson gson = new Gson();
        String strJson = gson.toJson(map);
        edit.clear();
        edit.putString(key, strJson);
        edit.commit();
    }

    /**
     * 获取Map集合
     */
    public static <K, T> ConcurrentHashMap<K, T> getMap(Context context, String key) {
        ConcurrentHashMap<K, T> map = new ConcurrentHashMap<>();
        SharedPreferences sp = getSp(context);
        String strJson = sp.getString(key, null);
        if (strJson == null) {
            return map;
        }
        Gson gson = new Gson();
        map = gson.fromJson(strJson, new TypeToken<ConcurrentHashMap<K, T>>() {}.getType());
        return map;
    }

    /**
     * 存储对象
     */
    private static void put(Context context, String key, Object obj) throws IOException {
        if (obj == null) {//判断对象是否为空
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        // 将对象放到OutputStream中
        // 将对象转换成byte数组，并将其进行base64编码
        String objectStr = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
        baos.close();
        oos.close();

        putString(context, key, objectStr);
    }

    /**
     * 获取对象
     */
    private static Object get(Context context, String key)
            throws IOException, ClassNotFoundException {
        String wordBase64 = getString(context, key);
        // 将base64格式字符串还原成byte数组
        if (TextUtils.isEmpty(wordBase64)) { //不可少，否则在下面会报java.io.StreamCorruptedException
            return null;
        }
        byte[] objBytes = Base64.decode(wordBase64.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream bais = new ByteArrayInputStream(objBytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        // 将byte数组转换成product对象
        Object obj = ois.readObject();
        bais.close();
        ois.close();
        return obj;
    }

    /**
     * 存储 推送id
     * @param registId
     */
    public static void setRegistId(String registId){
        saveData(RApplication.getContext(),CURRENT_USER_REGISRID,registId);
    }

    /**
     * 获取用户注册 Registid
     * @return
     */
    public static String getRegistId(){
        String value = (String) getData(RApplication.getContext(),CURRENT_USER_REGISRID,"");
        return value;
    }

    /**
     * 保存是否点击了推送的弹框通知状态
     */
    public static void savePushNotificClick(boolean click){
        saveData(RApplication.getContext(),JPUSH_IS_ONCLICK_DIALOG,click);
    }

    /**
     * 获取推送弹框通知是否点击的状态
     */
    public static boolean isClickPushNotific(){
        boolean isClick = (boolean) getData(RApplication.getContext(),JPUSH_IS_ONCLICK_DIALOG,false);
        if (isClick){
            savePushNotificClick(false); // 若已经点击过了，就修改点击状态
        }
        return isClick;
    }

    /**
     * 保存是否点击了推送的网页状态
     */
    public static void savePushWebUrlClick(boolean click){
        saveData(RApplication.getContext(),JPUSH_IS_ONCLICK_WENURL,click);
    }

    /**
     * 获取推送的网页是否被点击
     */
    public static boolean isClickPushWeb(){
        boolean isClick = (boolean) getData(RApplication.getContext(),JPUSH_IS_ONCLICK_WENURL,false);
        if (isClick){
            savePushWebUrlClick(false);
        }
        return isClick;
    }
}
