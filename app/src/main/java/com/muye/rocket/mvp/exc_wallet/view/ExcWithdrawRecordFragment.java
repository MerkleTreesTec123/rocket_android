package com.muye.rocket.mvp.exc_wallet.view;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ifenduo.common.adapter.base.ViewHolder;
import com.ifenduo.common.tool.DateTimeTool;
import com.muye.rocket.base.BaseListFragment;
import com.ifenduo.lib_base.tools.NumberUtil;
import com.muye.rocket.R;
import com.muye.rocket.entity.exc_wallet.ExcWalletRecord;
import com.muye.rocket.mvp.exc_wallet.contract.ExcWalletRecordContract;
import com.muye.rocket.mvp.exc_wallet.presenter.ExcWalletRecordPresenter;

import java.util.List;

public class ExcWithdrawRecordFragment extends BaseListFragment<ExcWalletRecord> implements ExcWalletRecordContract.View {
    String mCoinID;
    ExcWalletRecordContract.Presenter mRecordPresenter;

    public static ExcWithdrawRecordFragment newInstance(String coinID) {
        Bundle args = new Bundle();
        args.putString("coinID", coinID);
        ExcWithdrawRecordFragment fragment = new ExcWithdrawRecordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mCoinID = bundle.getString("coinID", "");
        }
        super.initData();
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position, ExcWalletRecord record) {

    }

    @Override
    public void onRequest(int page) {
        if (mRecordPresenter == null) {
            mRecordPresenter = new ExcWalletRecordPresenter(this);
        }
        mRecordPresenter.fetchWalletRecord(mCoinID, ExcWalletRecordContract.RECORD_TYPE_WITHDRAW, page);
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.exc_wal_item_withdraw_record;
    }

    @Override
    public void onBindView(ViewHolder holder, ExcWalletRecord record, int position) {
        if (record != null) {
            holder.setText(R.id.title_text_view, record.getOperationDesc());
            holder.setText(R.id.num_text_view, NumberUtil.formatMoney(record.getAmount()));
            holder.setText(R.id.status_text_view, record.getStatusDesc());
            holder.setText(R.id.time_text_view, DateTimeTool.getYYYYMMDDHHMMSS(record.getCreateDate()));
        } else {
            holder.setText(R.id.title_text_view, "--");
            holder.setText(R.id.num_text_view, "--");
            holder.setText(R.id.status_text_view, "--");
            holder.setText(R.id.time_text_view, "--");
        }
    }

    @Override
    public void bindWalletRecord(List<ExcWalletRecord> recordList, String recordType) {
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
