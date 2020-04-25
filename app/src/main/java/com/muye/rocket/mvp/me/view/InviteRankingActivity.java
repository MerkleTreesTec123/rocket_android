package com.muye.rocket.mvp.me.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.ifenduo.lib_base.tools.NumberUtil;
import com.ifenduo.lib_base.widget.tablayout.TabEntity;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.entity.InviteRankEntity;
import com.muye.rocket.mvp.me.contract.InviteRankContract;
import com.muye.rocket.mvp.me.presenter.InviteRankPresenter;
import com.muye.rocket.tools.StringTools;
import com.muye.rocket.widget.dialog.DescriptionDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/*
* 邀请榜
* */
public class InviteRankingActivity extends BaseActivity implements InviteRankContract.View, OnTabSelectListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.description_text)
    TextView descriptionText;
    @BindView(R.id.tab_layout)
    CommonTabLayout tabLayout;
    @BindView(R.id.account_text_view_2)
    TextView accountTextView2;
    @BindView(R.id.account_text_view_1)
    TextView accountTextView1;
    @BindView(R.id.account_text_view_3)
    TextView accountTextView3;
    @BindView(R.id.num_text_view_1)
    TextView numTextView1;
    @BindView(R.id.num_text_view_2)
    TextView numTextView2;
    @BindView(R.id.num_text_view_3)
    TextView numTextView3;
    @BindView(R.id.my_rank_text_view)
    TextView myRankTextView;
    @BindView(R.id.invite_code_text_view)
    TextView inviteCodeTextView;
    @BindView(R.id.parent_text_view)
    TextView parentTextView;
    InviteRankContract.Presenter mPresenter;

    InviteRankedAdapter mAdapter;
    List<InviteRankEntity.WeeklyBean> mWeekRank;//周榜
    List<InviteRankEntity.HistoryBean> mHistoryRank;//人数总榜
    List<InviteRankEntity.CostMostBean> mCostRank;//支付榜

    //剪切板管理工具类
    private ClipboardManager mClipboardManager;
    //剪切板Data对象
    private ClipData mClipData;
    DescriptionDialog mDescriptionDialog;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_ranking;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setupTabLayout();
        mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        mAdapter = new InviteRankedAdapter(this);
        recyclerView.setAdapter(mAdapter);
        mPresenter = new InviteRankPresenter(this);
        mPresenter.fetchInviteRank();
        descriptionText.setVisibility(View.GONE);// 邀请说明隐藏
    }

    private void setupTabLayout() {
        ArrayList<CustomTabEntity> tabEntityList = new ArrayList<>();
        tabEntityList.add(new TabEntity(getString(R.string.invitation_week)));
        tabEntityList.add(new TabEntity(getString(R.string.invited_people_total)));
        tabEntityList.add(new TabEntity(getString(R.string.invited_pay_total)));
        tabLayout.setTabData(tabEntityList);
        tabLayout.setOnTabSelectListener(this);
    }

    @Override
    public void bindInviteRank(InviteRankEntity inviteRank) {
        initDescriptionDialog();
        if (inviteRank != null) {
            //我的周排行
            myRankTextView.setText(TextUtils.isEmpty(inviteRank.getMyWeekRank()) ? getString(R.string.no_ranking_yet) : inviteRank.getMyWeekRank());
            //我的邀请者
            parentTextView.setText(StringTools.phoneNumberFormat(inviteRank.getMyInviter()));
            //我的邀请码
            inviteCodeTextView.setText(inviteRank.getYaoqingma());
            //周榜
            mWeekRank = inviteRank.getWeekly();
            //人数总榜
            mHistoryRank = inviteRank.getHistory();
            //支付榜
            mCostRank = inviteRank.getCostMost();
            bindWeekRank();

            mAdapter.setData(inviteRank.getReward());
            mDescriptionDialog.setContent(inviteRank.getContent());
            return;
        }
        //我的周排行
        myRankTextView.setText("--");
        //我的邀请者
        parentTextView.setText("--");
        //我的邀请码
        inviteCodeTextView.setText("--");
        //周榜
        mWeekRank = null;
        //人数总榜
        mHistoryRank = null;
        //支付榜
        mCostRank = null;
        mAdapter.setData(null);
        mDescriptionDialog.setContent("");

    }

    //绑定 周榜 人数总榜 支付榜
    private void bindRank() {
        if (tabLayout.getCurrentTab() == 0) {
            bindWeekRank();
        } else if (tabLayout.getCurrentTab() == 1) {
            bindTotalMemberRank();
        } else {
            bindCostRank();
        }
    }

    //绑定周榜
    private void bindWeekRank() {
        InviteRankEntity.WeeklyBean weeklyBean1 = null;
        InviteRankEntity.WeeklyBean weeklyBean2 = null;
        InviteRankEntity.WeeklyBean weeklyBean3 = null;
        if (mWeekRank != null) {
            if (mWeekRank.size() > 0) {
                weeklyBean1 = mWeekRank.get(0);
            }
            if (mWeekRank.size() > 1) {
                weeklyBean2 = mWeekRank.get(1);
            }
            if (mWeekRank.size() > 2) {
                weeklyBean3 = mWeekRank.get(2);
            }
        }
        if (weeklyBean1 != null) {
            accountTextView1.setText(weeklyBean1.getUsername());
            numTextView1.setText(String.format(getString(R.string.invite_people_num_), weeklyBean1.getNum()));
        } else {
            accountTextView1.setText("--");
            numTextView1.setText(String.format(getString(R.string.invite_people_num_), "--"));
        }

        if (weeklyBean2 != null) {
            accountTextView2.setText(weeklyBean2.getUsername());
            numTextView2.setText(String.format(getString(R.string.invite_people_num_), weeklyBean2.getNum()));
        } else {
            accountTextView2.setText("--");
            numTextView2.setText(String.format(getString(R.string.invite_people_num_), "--"));
        }

        if (weeklyBean3 != null) {
            accountTextView3.setText(weeklyBean3.getUsername());
            numTextView3.setText(String.format(getString(R.string.invite_people_num_), weeklyBean3.getNum()));
        } else {
            accountTextView3.setText("--");
            numTextView3.setText(String.format(getString(R.string.invite_people_num_), "--"));
        }

    }

    //绑定总人数榜
    private void bindTotalMemberRank() {
        InviteRankEntity.HistoryBean historyBean1 = null;
        InviteRankEntity.HistoryBean historyBean2 = null;
        InviteRankEntity.HistoryBean historyBean3 = null;
        if (mHistoryRank != null) {
            if (mHistoryRank.size() > 0) {
                historyBean1 = mHistoryRank.get(0);
            }
            if (mHistoryRank.size() > 1) {
                historyBean2 = mHistoryRank.get(1);
            }
            if (mHistoryRank.size() > 2) {
                historyBean3 = mHistoryRank.get(2);
            }
        }
        if (historyBean1 != null) {
            accountTextView1.setText(historyBean1.getUsername());
            numTextView1.setText(String.format(getString(R.string.invite_people_num_), historyBean1.getNum()));
        } else {
            accountTextView1.setText("--");
            numTextView1.setText(String.format(getString(R.string.invite_people_num_), "--"));
        }

        if (historyBean2 != null) {
            accountTextView2.setText(historyBean2.getUsername());
            numTextView2.setText(String.format(getString(R.string.invite_people_num_), historyBean2.getNum()));
        } else {
            accountTextView2.setText("--");
            numTextView2.setText(String.format(getString(R.string.invite_people_num_), "--"));
        }

        if (historyBean3 != null) {
            accountTextView3.setText(historyBean3.getUsername());
            numTextView3.setText(String.format(getString(R.string.invite_people_num_), historyBean3.getNum()));
        } else {
            accountTextView3.setText("--");
            numTextView3.setText(String.format(getString(R.string.invite_people_num_), "--"));
        }

    }

    //绑定支付榜
    private void bindCostRank() {
        InviteRankEntity.CostMostBean costMostBean1 = null;
        InviteRankEntity.CostMostBean costMostBean2 = null;
        InviteRankEntity.CostMostBean costMostBean3 = null;
        if (mCostRank != null) {
            if (mCostRank.size() > 0) {
                costMostBean1 = mCostRank.get(0);
            }
            if (mCostRank.size() > 1) {
                costMostBean2 = mCostRank.get(1);
            }
            if (mCostRank.size() > 2) {
                costMostBean3 = mCostRank.get(2);
            }
        }
        if (costMostBean1 != null) {
            accountTextView1.setText(costMostBean1.getUsername());
            numTextView1.setText(NumberUtil.formatMoney(costMostBean1.getAmount(), 0) + " USDT");
        } else {
            accountTextView1.setText("--");
            numTextView1.setText("-- USDT");
        }

        if (costMostBean2 != null) {
            accountTextView2.setText(costMostBean2.getUsername());
            numTextView2.setText(NumberUtil.formatMoney(costMostBean2.getAmount(), 0) + " USDT");
        } else {
            accountTextView2.setText("--");
            numTextView2.setText("-- USDT");
        }

        if (costMostBean3 != null) {
            accountTextView3.setText(costMostBean3.getUsername());
            numTextView3.setText(NumberUtil.formatMoney(costMostBean3.getAmount(), 0) + " USDT");
        } else {
            accountTextView3.setText("--");
            numTextView3.setText("-- USDT");
        }

    }

    @OnClick({R.id.back_button, R.id.description_text, R.id.copy_button, R.id.record_button, R.id.poster_button})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.back_button:
                onClickBack();
                break;
            case R.id.description_text:
                if (MMKVTools.isOpenTrade()) {
                    showDescriptionDialog();
                }else {
                    showToast(getString(R.string.waiting));
                }
                break;
            case R.id.copy_button://创建一个新的文本clip对象
                mClipData = ClipData.newPlainText("ufo_copy_invite_code", inviteCodeTextView.getText().toString());
                //把clip对象放在剪贴板中
                mClipboardManager.setPrimaryClip(mClipData);
                showToast(getString(R.string.exc_wal_copy_success));
                break;
            case R.id.record_button://个人推荐记录
                intent = new Intent(this, InviteRecordActivity.class);
                startActivity(intent);
                break;
            case R.id.poster_button://专属邀请海报
                intent = new Intent(this, InviteActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void initDescriptionDialog() {
        if (mDescriptionDialog == null) {
            mDescriptionDialog = new DescriptionDialog(this);
        }
    }

    private void showDescriptionDialog() {
        initDescriptionDialog();
        if (!mDescriptionDialog.isShowing()) {
            mDescriptionDialog.show();
        }
    }

    @Override
    public void onTabSelect(int position) {
        bindRank();
    }

    @Override
    public void onTabReselect(int position) {

    }
}
