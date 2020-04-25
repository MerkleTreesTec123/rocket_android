package com.muye.rocket.mvp.c2c.view;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ifenduo.common.adapter.base.ViewHolder;
import com.ifenduo.lib_base.tools.NumberUtil;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseListFragment;
import com.muye.rocket.entity.c2c.WalletDetailRecord;
import com.muye.rocket.mvp.c2c.contract.WalletDetailRecordListContract;
import com.muye.rocket.mvp.c2c.presenter.WalletDetailRecordListPresenter;

import java.util.List;

public class C2CTransferRecordFragment extends BaseListFragment<WalletDetailRecord> implements WalletDetailRecordListContract.View {

    String mType;
    WalletDetailRecordListContract.Presenter mRecordPresenter;

    public static C2CTransferRecordFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("type", type);
        C2CTransferRecordFragment fragment = new C2CTransferRecordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.c2c_item_transfer_record;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mType = bundle.getString("type", "90,91");
        }
        super.initData();
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position, WalletDetailRecord walletDetailRecord) {

    }

    @Override
    public void onRequest(int page) {
        if (mRecordPresenter == null) {
            mRecordPresenter = new WalletDetailRecordListPresenter(this);
        }
        mRecordPresenter.fetchWalletDetailRecord("", mType, page);
    }


    @Override
    public void onBindView(ViewHolder holder, WalletDetailRecord record, int position) {
        if (record != null) {
            holder.setText(R.id.title_text_view, record.getNote() + "  " + record.getCoin_name());
            holder.setText(R.id.num_text_view, NumberUtil.formatMoney(record.getValue()));
            holder.setText(R.id.status_text_view, getString(R.string.c2c_completed));
            holder.setText(R.id.time_text_view, record.getInputtime());
        }else {
            holder.setText(R.id.title_text_view, "--");
            holder.setText(R.id.num_text_view, "--");
            holder.setText(R.id.status_text_view, "--");
            holder.setText(R.id.time_text_view, "--");
        }
    }

    @Override
    public void bindWalletDetailRecord(List<WalletDetailRecord> recordList) {
        dispatchResult(recordList);
    }

    @Override
    public void onDestroyView() {
        if (mRecordPresenter != null) {
            mRecordPresenter.dropView();
        }
        super.onDestroyView();
    }
}
