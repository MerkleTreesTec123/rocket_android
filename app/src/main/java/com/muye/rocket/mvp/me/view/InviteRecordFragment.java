package com.muye.rocket.mvp.me.view;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.ifenduo.common.adapter.base.ViewHolder;
import com.ifenduo.common.tool.DateTimeTool;
import com.muye.rocket.base.BaseListFragment;
import com.ifenduo.lib_base.tools.NumberUtil;
import com.muye.rocket.R;
import com.muye.rocket.entity.InviteRecord;
import com.muye.rocket.mvp.me.contract.InviteRecordContract;
import com.muye.rocket.mvp.me.presenter.InviteRecordPresenter;
import com.muye.rocket.tools.StringTools;

import java.util.List;

public class InviteRecordFragment extends BaseListFragment<InviteRecord> implements InviteRecordContract.View {
    InviteRecordContract.Presenter mRecordPresenter;

    public static InviteRecordFragment newInstance() {
        Bundle args = new Bundle();
        InviteRecordFragment fragment = new InviteRecordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position, InviteRecord inviteRecord) {

    }

    @Override
    public void onRequest(int page) {
        if (mRecordPresenter == null) {
            mRecordPresenter = new InviteRecordPresenter(this);
        }
        mRecordPresenter.fetchInviteRecord(page);
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.item_invite_record;
    }

    @Override
    public void onBindView(ViewHolder holder, InviteRecord record, int position) {
        TextView numTextView = holder.getView(R.id.num_text_view);
        if (record != null) {
            //被邀请人
            holder.setText(R.id.account_text_view, StringTools.phoneNumberFormat(record.getRname()));
            //被邀请人注册时间
            holder.setText(R.id.time_text_view, DateTimeTool.getYYYYMMDDHHMM(record.getRegtime()));
            //被邀请人 花费金额
            String numStr = "<font color='#474A57'>"
                    + getString(R.string.invite_payed)
                    + " </font><font color='#C9A063'>"
                    + NumberUtil.formatMoney(record.getCost(), 2)
                    + "</font><font color='#474A57'> USDT</font>";
            numTextView.setText(Html.fromHtml(numStr));
        } else {
            if (record != null) {
                //被邀请人
                holder.setText(R.id.account_text_view, "--");
                //被邀请人注册时间
                holder.setText(R.id.time_text_view, "--");
                //被邀请人 花费金额
                String numStr = "<font color='#474A57'>"
                        + getString(R.string.invite_payed)
                        + " </font><font color='#C9A063'>"
                        + "--"
                        + "</font><font color='#474A57'> USDT</font>";
                numTextView.setText(Html.fromHtml(numStr));
            }
        }
    }

    @Override
    public void bindInviteRecord(List<InviteRecord> recordList) {
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
