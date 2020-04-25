package com.muye.rocket.tools;

import android.view.View;

public class PhoneUtils {
    public static boolean checkIsNotNull(String str) {
        if (str == null || str.isEmpty() || str.trim().length() == 0 || str.equals("null")
                || str.equals("(null)") || str.equals("<null>")) {
            return false;
        }
        return true;
    }

    /**
     * 测量View的宽高
     *
     * @param view View
     */
    public static void measureWidthAndHeight(View view) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthMeasureSpec, heightMeasureSpec);
    }


}
