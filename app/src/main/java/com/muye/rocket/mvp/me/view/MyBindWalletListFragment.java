package com.muye.rocket.mvp.me.view;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hjq.toast.ToastUtils;
import com.ifenduo.common.adapter.base.ViewHolder;
import com.ifenduo.lib_base.tools.MMKVTools;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseListFragment;
import com.muye.rocket.entity.BindWalletBean;
import com.muye.rocket.entity.CoinBean;
import com.muye.rocket.entity.exc_wallet.ExcAssetsEntity;
import com.muye.rocket.entity.exc_wallet.RocketBalance;
import com.muye.rocket.event.BindCatEvent;
import com.muye.rocket.mvp.me.contract.BindWalletContract;
import com.muye.rocket.mvp.me.contract.WithDrawContract;
import com.muye.rocket.mvp.me.presenter.BindWalletPresenter;
import com.muye.rocket.mvp.me.presenter.WithDrawPresenter;
import com.muye.rocket.tools.MD5Tools;
import com.muye.rocket.widget.dialog.CoinPicker;
import com.muye.rocket.widget.dialog.SelectCoinDialog;
import com.muye.rocket.widget.dialog.TransferBindDialog;
import com.muye.rocket.widget.dialog.WithDrawColumbuDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import java.util.List;

public class MyBindWalletListFragment extends BaseListFragment<BindWalletBean> implements BindWalletContract.View,
        SelectCoinDialog.OnSelectClickListener, WithDrawContract.View {

    View mHeaderView;
    TextView headTitle;

    BindWalletPresenter walletPresenter;
    WithDrawPresenter drawPresenter;
    private TransferBindDialog bindDialog;
    private SelectCoinDialog coinDialog;

    private String type;
    private double rocketCAT,rocketUSDT;
    private WithDrawColumbuDialog withDrawDialog;

    public static MyBindWalletListFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("with_address",type);
        MyBindWalletListFragment fragment = new MyBindWalletListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public boolean isSupportLoadMore() {
        return false;
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position, BindWalletBean walletBean) {
        if (walletBean != null){
            if (type.equals("1")){
                requestData();
                String jwt = MD5Tools.MD5(MD5Tools.MD5(walletBean.getCat_uid()+"cat"));
                walletPresenter.fetchRemaining(walletBean.getCat_uid(),jwt,walletBean);
            }else {
                drawPresenter.fetchRocketBalance(walletBean);
            }
        }
    }

    /**
     * 交易所绑定账户功能中的【免费提现】按钮需要根据节点判断下，某些节点可以绑定但禁止提现
     * @return
     */

    @Override
    public View getHeaderView() {
        if (mHeaderView == null){
            mHeaderView = LayoutInflater.from(getContext()).inflate(R.layout.bind_wallet_head, mRecyclerView, false);
            headTitle = mHeaderView.findViewById(R.id.head_title);
            setBindNum(0);
        }
        return mHeaderView;
    }

    private void setBindNum(int bindNum) {
        if (!TextUtils.isEmpty(type)&&type.equals("1")) {
            headTitle.setText("已绑定 " + bindNum + " 个哥伦布账户");
        }else headTitle.setText(getString(R.string.select_columnbu_tb));
    }

    private void requestData(){
        if (walletPresenter == null){
            walletPresenter = new BindWalletPresenter(this);
        }
        if (drawPresenter == null){
            drawPresenter = new WithDrawPresenter(this);
        }
    }

    @Override
    public void onRequest(int page) {
        requestData();
        String jwt = MD5Tools.MD5(MD5Tools.MD5(MMKVTools.getUID()+"cat"));
        walletPresenter.fetCatLists(MMKVTools.getUID(),jwt);

    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getString("with_address", "");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        forceRefresh();
        onRefresh();
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.bind_wallet_item_cat;
    }

    @Override
    public void onBindView(ViewHolder holder, BindWalletBean walletBean, int position) {
        TextView userPhone = holder.getView(R.id.user_phone);
        TextView userName = holder.getView(R.id.user_name);
        TextView bindTime = holder.getView(R.id.bind_time);
        if (walletBean != null){
            userPhone.setText(walletBean.getCat_phone());
            userName.setText(walletBean.getCat_username());
            bindTime.setText(walletBean.getCreate_time());
        }else {
            userPhone.setText("--");
            userName.setText("--");
            bindTime.setText("--");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshBindCatList(BindCatEvent event) {
        notifyDataSetChanged();
    }

    @Override
    protected void initView(View parentView) {
        super.initView(parentView);
        EventBus.getDefault().register(this);
    }

    @Override
    public void bindWallet(String qrcode) {

    }

    @Override
    public void bindCatLists(List<BindWalletBean> catLists) {
        if (null != catLists){
            setBindNum(catLists.size());
            dispatchResult(catLists);
        }else {
            dispatchResult(new ArrayList<>());
        }
    }

    private void showWithDrawDialog(BindWalletBean walletBean){
        withDrawDialog = new WithDrawColumbuDialog(getContext());
        withDrawDialog.setContent(walletBean,rocketUSDT,rocketCAT);
        withDrawDialog.setOnClickTransfer(new WithDrawColumbuDialog.OnTransferClickListener() {
            @Override
            public void onTransfer(BindWalletBean walletBean) {
                withDrawDialog.dismiss();
                showSelectDialog(walletBean,2);
            }

            @Override
            public void onClose(WithDrawColumbuDialog dialog) {
                dialog.dismiss();
            }
        });
        withDrawDialog.show();
    }

    @Override
    public void inquireRemaining(List<CoinBean> remain, BindWalletBean walletBean) {
        bindDialog = new TransferBindDialog(getContext());
        int usdt = -1;
        int cat  = -1;
        for (int i = 0; i < remain.size(); i++) {
            if (remain.get(i).getCoinname().equals("USDT")){
                usdt = i;
            }
            if (remain.get(i).getCoinname().equals("CAT")){
                cat = i;
            }
        }
        int nodeId  = walletBean.getSystem_type();

        bindDialog.setContent(walletBean,remain,usdt,cat);
        bindDialog.setOnClickTransfer(new TransferBindDialog.OnTransferClickListener() {
                    @Override
                    public void onTransfer(BindWalletBean walletBean) {

                        // 节点在创世节点或者新大陆节点 都不支持体现到交易所
                        if (nodeId == 7 || (nodeId >= 10 && nodeId <= 106)){

                        }else {
//                            showToast("暂未打通钱包与交易所余额互转");
                            bindDialog.dismiss();
                            showSelectDialog(walletBean,1);
                        }
                    }

                    @Override
                    public void onClose(TransferBindDialog dialog) {
                        dialog.dismiss();
                    }
                });
        bindDialog.show();
    }

    /**
     * 提币状态
     */
    @Override
    public void reflectStatus() {
        showToast(getString(R.string.reflect_ok));
        if (coinDialog != null && coinDialog.isShowing())
            coinDialog.dismiss();
    }

    private void showSelectDialog(BindWalletBean walletBean,int type) {
        coinDialog = new SelectCoinDialog(getContext());
        coinDialog.setOnClickTransfer(this);
        coinDialog.setType(type);
        coinDialog.setWalletCoin(walletBean);
        if (coinDialog != null && !coinDialog.isShowing())
            coinDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (walletPresenter != null)
            walletPresenter.dropView();
        if (drawPresenter != null)
            drawPresenter.dropView();
    }

    /**
     * coin : 1 USDT , 2  CAT , 3  CAG
     * @param coin
     */
    @Override
    public void onSelected(int coin, BindWalletBean walletBean, EditText pasword,int type,String num) {
        if (coin != -1){
            requestData();
            String coinName = "";
            if (coin == 1)  coinName = "USDT";
            else if (coin == 2) coinName = "CAT";
            else if (coin == 3) coinName = "CAG";
            if (type == 1){
                if (TextUtils.isEmpty(num)){
                    ToastUtils.show(getString(R.string.coin_num));
                    return;
                }
                if (!TextUtils.isEmpty(pasword.getText().toString().trim())) {
                    walletPresenter.fetchReflectCoin(num, walletBean.getCat_uid(), coinName,pasword.getText().toString().trim(), walletBean.getRocket_uid());
                }else showToast(getString(R.string.cloumbu_password));
            }else {
                if (!TextUtils.isEmpty(pasword.getText().toString().trim())){
                    drawPresenter.fetchWithDrawCat(walletBean.getCat_uid(),coinName,walletBean.getRocket_uid(),num,pasword.getText().toString().trim());
                }else showToast(getString(R.string.rocket_password));
            }
        }else {
            showToast(getString(R.string.select_coin_type));
        }
    }

    @Override
    public void onClose(SelectCoinDialog dialog) {
        dialog.dismiss();
    }


    @Override
    public void withDrawCat(int status) {
        if (status == 1){
            showToast(getString(R.string.with_draw_success));
        }
        if (coinDialog != null && coinDialog.isShowing())
            coinDialog.dismiss();
    }

    @Override
    public void searchRocketBalance(List<RocketBalance> balanceList,BindWalletBean walletBean) {
        if (balanceList != null&&balanceList.size()>0){
            for (RocketBalance balance : balanceList) {
                if (balance.getCoin_name().equals("USDT")){
                    rocketUSDT = Double.parseDouble(balance.getTotal());
                }
                if (balance.getCoin_name().equals("CAT")){
                    rocketCAT = Double.parseDouble(balance.getTotal());
                }
            }
            showWithDrawDialog(walletBean);
        }
//        else showToast(getString(R.string.search_error));
    }
}
