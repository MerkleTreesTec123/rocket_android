package com.ifenduo.common.log;

import com.orhanobut.logger.Logger;

/**
 * Created by yangxuefeng on 16/10/20.
 */

public class XCLOG {

    public XCLOG(){}

//    public static void init(LogLevel logLevel){
//        if(null == logLevel )
//           Logger.init();
//        else
//           Logger.init().logLevel(logLevel);
//    }

    public static void d(Object object){
        Logger.d(object);
    }

    public static void e(Object object){
        Logger.e(object.toString());
    }

    public static void w(Object object){
        Logger.w(object.toString());
    }

    public static void v(Object object){
        Logger.v(object.toString());
    }

    public static void json(Object object){
        Logger.json(object.toString());
    }


}
