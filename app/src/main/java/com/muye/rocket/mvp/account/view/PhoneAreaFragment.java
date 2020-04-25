package com.muye.rocket.mvp.account.view;


import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.ifenduo.common.adapter.base.ViewHolder;
import com.muye.rocket.base.BaseListFragment;
import com.muye.rocket.R;
import com.muye.rocket.entity.PhoneArea;
import com.muye.rocket.mvp.account.contract.PhoneAreaContract;
import com.muye.rocket.mvp.account.presenter.PhoneAreaPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by ll on 2018/4/8.
 */

public class PhoneAreaFragment extends BaseListFragment<PhoneArea> implements PhoneAreaContract.View {
    public static final String TAG = "AreaListFragment";
    List<PhoneArea> mPhoneAreaList;
    PhoneAreaContract.Presenter mPresenter;

    public static PhoneAreaFragment newInstance() {
        Bundle args = new Bundle();
        PhoneAreaFragment fragment = new PhoneAreaFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position, PhoneArea phoneArea) {
        if (phoneArea == null) return;
        ((PhoneAreaActivity) getContext()).getCheckedArea(phoneArea);
    }

    @Override
    protected void initView(View parentView) {
        super.initView(parentView);
    }

    @Override
    public void onRequest(int page) {
        if (mPresenter == null) {
            mPresenter = new PhoneAreaPresenter(this);
        }
        if (mPhoneAreaList == null || mPhoneAreaList.size() == 0) {
            mPresenter.fetchPhoneArea();
        } else {
            dispatchResult(mPhoneAreaList);
        }
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.item_phone_area;
    }

    @Override
    public void bindPhoneArea(List<PhoneArea> areaList) {
        mPhoneAreaList = areaList;
        dispatchResult(areaList);
    }

    @Override
    public void onBindView(ViewHolder holder, PhoneArea phoneArea, int position) {
        if (phoneArea != null) {
            Locale locale;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = getResources().getConfiguration().getLocales().get(0);
            } else {
                locale = getResources().getConfiguration().locale;
            }

            if ("en".equals(locale.getLanguage())) {
                holder.setText(R.id.text_item_area_name, phoneArea.getEn());
            } else {
                holder.setText(R.id.text_item_area_name, phoneArea.getZh());
            }
            holder.setText(R.id.text_item_area_code, "+" + String.valueOf(phoneArea.getCode()));
        } else {
            holder.setText(R.id.text_item_area_name, "");
            holder.setText(R.id.text_item_area_code, "");
        }
    }

    public void search(String areaName) {
        if (!TextUtils.isEmpty(areaName)) {
            List<PhoneArea> phoneAreaList = new ArrayList<>();
            for (PhoneArea phoneArea : mPhoneAreaList) {
                if (phoneArea != null && !TextUtils.isEmpty(phoneArea.getZh()) && phoneArea.getZh().contains(areaName)) {
                    phoneAreaList.add(phoneArea);
                }
            }
            dispatchResult(phoneAreaList);
        } else {
            dispatchResult(mPhoneAreaList);
        }
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) mPresenter.dropView();
        super.onDestroyView();
    }
}
