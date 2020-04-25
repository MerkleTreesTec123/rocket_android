package com.ifenduo.lib_http.exception;

import android.text.TextUtils;
import android.util.Log;

public class ApiException extends Exception {
    public static final String TAG = "ApiException";
    private int code;
    private String displayMessage;

    public ApiException(int code, String displayMessage) {
        super(displayMessage);
        this.code = code;
        this.displayMessage = displayMessage;
        Log.d(TAG, "ApiException: " + displayMessage);
    }

    public ApiException(int code, String message, String displayMessage) {
        super(message);
        this.code = code;
        this.displayMessage = displayMessage;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDisplayMessage() {
        if (TextUtils.isEmpty(displayMessage)) {
            return getMessage();
        }
        return displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }
}
