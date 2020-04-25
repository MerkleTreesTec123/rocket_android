package com.muye.rocket.base;

public interface BaseView{
    void onError(int code, String message);

    void showLoadDialog(String message);

    void hideLoadDialog();
}
