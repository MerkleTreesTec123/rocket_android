package com.muye.rocket.mvp.ieo.view;


import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ifenduo.common.adapter.base.ViewHolder;
import com.ifenduo.common.tool.DateTimeTool;
import com.ifenduo.lib_base.tools.NumberUtil;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseListFragment;
import com.muye.rocket.entity.ieo.IEORecordDetail;
import com.muye.rocket.mvp.ieo.contract.IEORecordDetailContract;
import com.muye.rocket.mvp.ieo.presenter.IEORecordDetailPresenter;

import java.util.ArrayList;


public class MyIeoDetailListFragment extends BaseListFragment<IEORecordDetail.LogBean> implements IEORecordDetailContract.View {

    public static MyIeoDetailListFragment newInstance(String logId) {
        Bundle args = new Bundle();
        args.putString("log_id", logId);
        MyIeoDetailListFragment fragment = new MyIeoDetailListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    TextView nameText;
    TextView buyNumText;
    TextView lockNumText;
    TextView freedTotalNumText;
    TextView freedTimeText;
    TextView freedTodayNumText;
    AppCompatTextView timeText;

    private String mLogId;
    IEORecordDetailContract.Presenter mPresenter;

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position, IEORecordDetail.LogBean s) {

    }

    @Override
    protected void initView(View parentView) {
        mLogId = getArguments().getString("log_id");
        super.initView(parentView);
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }


    @Override
    public View getHeaderView() {
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.header_ieo_detail, null);
        nameText = headerView.findViewById(R.id.name_text);
        buyNumText = headerView.findViewById(R.id.buyNum_text);
        lockNumText = headerView.findViewById(R.id.lockNum_text);
        freedTotalNumText = headerView.findViewById(R.id.freedTotalNum_text);
        freedTimeText = headerView.findViewById(R.id.freedTime_text);
        freedTodayNumText = headerView.findViewById(R.id.freedTodayNum_text);
        timeText = headerView.findViewById(R.id.time_text);
        return headerView;
    }

    @Override
    public void onRequest(int page) {
        if (mPresenter == null) {
            mPresenter = new IEORecordDetailPresenter(this);
        }
        mPresenter.fetchIEORecordDetail(mLogId, page);
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.item_my_ieo_detail;
    }

    @Override
    public void bindIEORecordDetail(IEORecordDetail recordDetail) {
        IEORecordDetail.InfoBean infoBean = null;
        if (recordDetail != null) {
            dispatchResult(recordDetail.getLog());
            infoBean = recordDetail.getInfo();

        } else {
            dispatchResult(new ArrayList<>());
        }
        if (infoBean != null) {
            nameText.setText(String.format(getString(R.string.space_plan_phase_), infoBean.getRound()));
            //购买数量
            buyNumText.setText(NumberUtil.formatMoney(infoBean.getAmount(), 0));
            //锁仓数量
            lockNumText.setText(NumberUtil.formatMoney(infoBean.getLock_amount()));
            //释放总量
            freedTotalNumText.setText(NumberUtil.formatMoney(infoBean.getRelease_amount()));
            //释放天数
            freedTimeText.setText(infoBean.getRelease_day());
            //今日释放量
            freedTodayNumText.setText(NumberUtil.formatMoney(infoBean.getRelease_today()));
            //时间
            timeText.setText(DateTimeTool.getYYYYMMDDHHMMSS(infoBean.getInputtime()));
        } else {
            nameText.setText(String.format(getString(R.string.space_plan_phase_), "--"));
            //购买数量
            buyNumText.setText("--");
            //锁仓数量
            lockNumText.setText("--");
            //释放总量
            freedTotalNumText.setText("--");
            //释放天数
            freedTimeText.setText("--");
            //今日释放量
            freedTodayNumText.setText("--");
            //时间
            timeText.setText("--");
        }
    }

    @Override
    public void onBindView(ViewHolder holder, IEORecordDetail.LogBean s, int position) {
        if (s != null) {
            //标题
            holder.setText(R.id.title_text, s.getNote());
            //数量
            holder.setText(R.id.num_text, NumberUtil.formatMoney(s.getAmount(), 4));
            //时间
            holder.setText(R.id.time_text, DateTimeTool.getYYYYMMDDHHMMSS(s.getInputtime()));
        } else {
            //标题
            holder.setText(R.id.title_text, "--");
            //数量
            holder.setText(R.id.num_text, "--");
            //时间
            holder.setText(R.id.time_text, "--");
        }
    }

}
