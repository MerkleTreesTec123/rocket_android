package com.muye.rocket.cache;

import android.content.Context;
import android.support.annotation.StringDef;
import android.text.TextUtils;

import com.muye.rocket.entity.exchange.KLineEntity;
import com.muye.rocket.tools.SPUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class KLineCache {
private static boolean tag = true;
    public static final String K_LINE_DATA_TIME_1MIN = "60";
    public static final String K_LINE_DATA_TIME_5MIN = "300";
    public static final String K_LINE_DATA_TIME_15MIN = "900";
    public static final String K_LINE_DATA_TIME_30MIN = "1800";
    public static final String K_LINE_DATA_TIME_60MIN = "3600";
    public static final String K_LINE_DATA_TIME_1DAY = "86400";
    public static final String K_LINE_DATA_TIME_1MON = "2592000";
    public static final String K_LINE_DATA_TIME_1WEEK = "604800";

    private static ConcurrentHashMap<String, List<KLineEntity>> KLINE_DATA_CACHE_1MIN = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, List<KLineEntity>> KLINE_DATA_CACHE_5MIN = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, List<KLineEntity>> KLINE_DATA_CACHE_15MIN = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, List<KLineEntity>> KLINE_DATA_CACHE_30MIN = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, List<KLineEntity>> KLINE_DATA_CACHE_60MIN = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, List<KLineEntity>> KLINE_DATA_CACHE_1DAY = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, List<KLineEntity>> KLINE_DATA_CACHE_1MON = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, List<KLineEntity>> KLINE_DATA_CACHE_1WEEK = new ConcurrentHashMap<>();

    public static void saveKLineData(String tradingPair, @KLINE_TIME String time, List<KLineEntity> data, Context context) {
        if (TextUtils.isEmpty(time) || TextUtils.isEmpty(tradingPair)) return;

        List<KLineEntity> kLineEntityList = new ArrayList<>();
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                KLineEntity entity = data.get(i);
                if (entity != null) {
                    KLineEntity kLineEntity = new KLineEntity(entity.getDatetime(), entity.getOpenPrice(),
                            entity.getHighPrice(), entity.getLowPrice(), entity.getClosePrice(), entity.getVolume());
                    kLineEntityList.add(kLineEntity);
                }
            }
        }
        if (K_LINE_DATA_TIME_1MIN.equals(time)) {
            if (KLINE_DATA_CACHE_1MIN.containsKey(tradingPair)) {
                KLINE_DATA_CACHE_1MIN.remove(tradingPair);
            }
            KLINE_DATA_CACHE_1MIN.put(tradingPair, kLineEntityList);
        } else if (K_LINE_DATA_TIME_5MIN.equals(time)) {
            if (KLINE_DATA_CACHE_5MIN.containsKey(tradingPair)) {
                KLINE_DATA_CACHE_5MIN.remove(tradingPair);
            }
            KLINE_DATA_CACHE_5MIN.put(tradingPair, kLineEntityList);
        } else if (K_LINE_DATA_TIME_15MIN.equals(time)) {
            if (KLINE_DATA_CACHE_15MIN.containsKey(tradingPair)) {
                KLINE_DATA_CACHE_15MIN.remove(tradingPair);
            }
            KLINE_DATA_CACHE_15MIN.put(tradingPair, kLineEntityList);
//            if (tag){
                SPUtils.setDataList(context,tradingPair,kLineEntityList);//存储id和list、、15min
//                tag = false;
//            }
        } else if (K_LINE_DATA_TIME_30MIN.equals(time)) {
            if (KLINE_DATA_CACHE_30MIN.containsKey(tradingPair)) {
                KLINE_DATA_CACHE_30MIN.remove(tradingPair);
            }
            KLINE_DATA_CACHE_30MIN.put(tradingPair, kLineEntityList);
        } else if (K_LINE_DATA_TIME_60MIN.equals(time)) {
            if (KLINE_DATA_CACHE_60MIN.containsKey(tradingPair)) {
                KLINE_DATA_CACHE_60MIN.remove(tradingPair);
            }
            KLINE_DATA_CACHE_60MIN.put(tradingPair, kLineEntityList);
        } else if (K_LINE_DATA_TIME_1DAY.equals(time)) {
            if (KLINE_DATA_CACHE_1DAY.containsKey(tradingPair)) {
                KLINE_DATA_CACHE_1DAY.remove(tradingPair);
            }
            KLINE_DATA_CACHE_1DAY.put(tradingPair, kLineEntityList);
        } else if (K_LINE_DATA_TIME_1MON.equals(time)) {
            if (KLINE_DATA_CACHE_1MON.containsKey(tradingPair)) {
                KLINE_DATA_CACHE_1MON.remove(tradingPair);
            }
            KLINE_DATA_CACHE_1MON.put(tradingPair, kLineEntityList);
        } else if (K_LINE_DATA_TIME_1WEEK.equals(time)) {
            if (KLINE_DATA_CACHE_1WEEK.containsKey(tradingPair)) {
                KLINE_DATA_CACHE_1WEEK.remove(tradingPair);
            }
            KLINE_DATA_CACHE_1WEEK.put(tradingPair, kLineEntityList);
        }
    }


    public static List<KLineEntity> getKLineData(String tradingPair, @KLINE_TIME String time) {
        if (TextUtils.isEmpty(time) || TextUtils.isEmpty(tradingPair)) {
            return new ArrayList<>();
        }
        if (K_LINE_DATA_TIME_1MIN.equals(time)) {
            if (KLINE_DATA_CACHE_1MIN.containsKey(tradingPair)) {
                return KLINE_DATA_CACHE_1MIN.get(tradingPair);
            }
            return new ArrayList<>();
        } else if (K_LINE_DATA_TIME_5MIN.equals(time)) {
            if (KLINE_DATA_CACHE_5MIN.containsKey(tradingPair)) {
                return KLINE_DATA_CACHE_5MIN.get(tradingPair);
            }
            return new ArrayList<>();
        } else if (K_LINE_DATA_TIME_15MIN.equals(time)) {
            if (KLINE_DATA_CACHE_15MIN.containsKey(tradingPair)) {
                return KLINE_DATA_CACHE_15MIN.get(tradingPair);
            }
            return new ArrayList<>();
        } else if (K_LINE_DATA_TIME_30MIN.equals(time)) {
            if (KLINE_DATA_CACHE_30MIN.containsKey(tradingPair)) {
                return KLINE_DATA_CACHE_30MIN.get(tradingPair);
            }
            return new ArrayList<>();
        } else if (K_LINE_DATA_TIME_60MIN.equals(time)) {
            if (KLINE_DATA_CACHE_60MIN.containsKey(tradingPair)) {
                return KLINE_DATA_CACHE_60MIN.get(tradingPair);
            }
            return new ArrayList<>();
        } else if (K_LINE_DATA_TIME_1DAY.equals(time)) {
            if (KLINE_DATA_CACHE_1DAY.containsKey(tradingPair)) {
                return KLINE_DATA_CACHE_1DAY.get(tradingPair);
            }
            return new ArrayList<>();
        } else if (K_LINE_DATA_TIME_1MON.equals(time)) {
            if (KLINE_DATA_CACHE_1MON.containsKey(tradingPair)) {
                return KLINE_DATA_CACHE_1MON.get(tradingPair);
            }
            return new ArrayList<>();
        } else if (K_LINE_DATA_TIME_1WEEK.equals(time)) {
            if (KLINE_DATA_CACHE_1WEEK.containsKey(tradingPair)) {
                return KLINE_DATA_CACHE_1WEEK.get(tradingPair);
            }
            return new ArrayList<>();
        } else {
            return new ArrayList<>();
        }
    }

    @StringDef({
            K_LINE_DATA_TIME_1MIN, K_LINE_DATA_TIME_5MIN, K_LINE_DATA_TIME_15MIN, K_LINE_DATA_TIME_30MIN, K_LINE_DATA_TIME_60MIN,
            K_LINE_DATA_TIME_1DAY, K_LINE_DATA_TIME_1MON, K_LINE_DATA_TIME_1WEEK
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface KLINE_TIME {

    }

}
