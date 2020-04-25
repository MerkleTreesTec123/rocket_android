package com.muye.rocket.mvp.exc_wallet.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.ifenduo.common.adapter.base.ViewHolder;
import com.muye.rocket.base.BaseListFragment;
import com.muye.rocket.R;
import com.muye.rocket.entity.exc_wallet.ExcAddress;
import com.muye.rocket.event.AddExcAddressEvent;
import com.muye.rocket.mvp.exc_wallet.contract.ExcAddressListContract;
import com.muye.rocket.mvp.exc_wallet.presenter.ExcAddressListPresenter;
import com.muye.rocket.widget.dialog.MessageDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class ExcAddressBookFragment extends BaseListFragment<ExcAddress> implements ExcAddressListContract.View {

    String mCoinID;
    String mSelectedAddressID;
    boolean isAddressPicker;
    MessageDialog mDeleteDialog;
    ExcAddressListContract.Presenter mPresenter;

    public static ExcAddressBookFragment newInstance(String coinID, String selectedAddressID, boolean isAddressPicker) {
        Bundle args = new Bundle();
        args.putString("coinID", coinID);
        args.putString("selectedAddressID", selectedAddressID);
        args.putBoolean("isAddressPicker", isAddressPicker);
        ExcAddressBookFragment fragment = new ExcAddressBookFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.exc_wal_item_address_list;
    }

    @Override
    public boolean isSupportLoadMore() {
        return false;
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position, ExcAddress excAddress) {

    }

    @Override
    protected void initView(View parentView) {
        super.initView(parentView);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mCoinID = bundle.getString("coinID", "");
            mSelectedAddressID = bundle.getString("selectedAddressID", "");
            isAddressPicker = bundle.getBoolean("isAddressPicker", false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        forceRefresh();
        onRefresh();
    }

    @Override
    public void onRequest(int page) {
        if (mPresenter == null) {
            mPresenter = new ExcAddressListPresenter(this);
        }
        mPresenter.fetchAddress(mCoinID);
    }

    @Override
    public void onBindView(ViewHolder holder, ExcAddress address, int position) {
        ConstraintLayout contentContainer = holder.getView(R.id.address_container);
        ImageView deleteButton = holder.getView(R.id.delete_button);
        if (address != null) {
            holder.setVisible(R.id.title_divider, !TextUtils.isEmpty(address.getFremark()));
            holder.setVisible(R.id.note_text_view, !TextUtils.isEmpty(address.getFremark()));

            holder.setText(R.id.title_text_view, address.getCoinName());
            holder.setText(R.id.note_text_view, address.getFremark());
            holder.setText(R.id.other_text_view, address.getFadderess());

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (address == null) return;
                    showMessageDialog(address.getFid());
                }
            });

            contentContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (address == null || !isAddressPicker) return;
                    if (getCurrentActivity() != null) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("excAddress", address);
                        Intent intent = new Intent();
                        intent.putExtras(bundle);
                        getCurrentActivity().setResult(Activity.RESULT_OK, intent);
                        getCurrentActivity().finish();
                    }
                }
            });
        } else {
            holder.setText(R.id.title_text_view, "--");
            holder.setText(R.id.note_text_view, "");
            holder.setText(R.id.other_text_view, "--");
            holder.setVisible(R.id.title_divider, false);
            holder.setVisible(R.id.note_text_view, false);
        }
    }

    @Override
    public void bindAddress(List<ExcAddress> addressList) {
        dispatchResult(addressList);
    }

    @Override
    public void onDeleteAddressSuccess(String addressID) {
        List<ExcAddress> addressList = getDataSource();
        ExcAddress address = null;
        if (addressList != null && addressList.size() > 0 && !TextUtils.isEmpty(addressID)) {
            for (int i = 0; i < addressList.size(); i++) {
                ExcAddress address_ = addressList.get(i);
                if (address_ != null && addressID.equals(address_.getFid())) {
                    address = address_;
                    break;
                }
            }
        }
        if (address != null) {
            addressList.remove(address);
        }
        notifyDataSetChanged();
        showToast(getString(R.string.exc_wal_delete_address_success));
    }

    private void showMessageDialog(String addressID) {
        if (mDeleteDialog == null) {
            mDeleteDialog = new MessageDialog(getContext())
                    .setMessage(getString(R.string.exc_wal_deletion_address_hint))
                    .setCancelText(getString(R.string.cancel))
                    .setOkText(getString(R.string.ok))
                    .setOnOkButtonClickListener(new MessageDialog.OnOkButtonClickListener() {
                        @Override
                        public void onOkButtonClick(MessageDialog dialog) {
                            dialog.dismiss();
                            deleteAddress(addressID);
                        }
                    });
        }
        if (!mDeleteDialog.isShowing()) {
            mDeleteDialog.show();
        }
    }

    private void deleteAddress(String addressID) {
        if (mPresenter == null) {
            mPresenter = new ExcAddressListPresenter(this);
        }
        mPresenter.deleteAddress(addressID);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddAddressSuccess(AddExcAddressEvent event) {
        if(!TextUtils.isEmpty(mCoinID)){
            mCoinID = event.getCoinID();
//            forceRefresh();
            onRefresh();
        }
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.dropView();
        }
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
