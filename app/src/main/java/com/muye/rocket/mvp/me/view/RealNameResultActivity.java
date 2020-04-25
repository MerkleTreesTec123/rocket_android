package com.muye.rocket.mvp.me.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;
import com.muye.rocket.entity.SafeSettingEntity;
import com.muye.rocket.mvp.me.auth.RealNameAuthActivity;
import com.muye.rocket.mvp.me.contract.RealNameStatusContract;
import com.muye.rocket.mvp.me.presenter.RealNameStatusPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/*
* 实名认证结果
* */
public class RealNameResultActivity extends BaseActivity implements RealNameStatusContract.View {
    @BindView(R.id.submit_success_text_view)
    TextView submitSuccessTextView;
    @BindView(R.id.review_text_view)
    TextView reviewTextView;
    @BindView(R.id.textView42)
    TextView resultTextView;
    @BindView(R.id.submit_success_image_view)
    ImageView submitSuccessImageView;
    @BindView(R.id.review_image_view)
    ImageView reviewImageView;
    @BindView(R.id.result_image_view)
    ImageView resultImageView;
    @BindView(R.id.divider_1)
    View divider1;
    @BindView(R.id.divider_2)
    View divider2;
    @BindView(R.id.status_image_view)
    ImageView statusImageView;
    @BindView(R.id.status_text_view)
    TextView statusTextView;
    @BindView(R.id.status_hint_text_view)
    TextView statusHintTextView;
    @BindView(R.id.submit_button)
    Button submitButton;
    RealNameStatusContract.Presenter mRealNameStatusPresenter;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_real_name_result;
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.review));
        fetchRealNameStatus();
    }

    @OnClick(R.id.submit_button)
    public void onViewClicked() {
        Intent intent = new Intent(this, RealNameAuthActivity.class);
        startActivity(intent);
        finish();
    }

    private void fetchRealNameStatus() {
        if (mRealNameStatusPresenter == null) {
            mRealNameStatusPresenter = new RealNameStatusPresenter(this);
        }
        mRealNameStatusPresenter.fetchRealNameStatus();
    }

    @Override
    public void bindRealNameStatus(SafeSettingEntity safeSetting) {
        if (safeSetting != null) {
            if (safeSetting.getFuser() != null && safeSetting.getFuser().isFhasrealvalidate()) {
                showSuccessStep();
            } else {
                if (safeSetting.getIdentity() == null) {
                    showEmpty();
                } else if ("2".equals(safeSetting.getIdentity().getFstatus())) {
                    showFailStep();
                    mRealNameStatusPresenter.fetchReject();
                } else if ("0".equals(safeSetting.getIdentity().getFstatus())) {
                    showSecondStep();
                } else {
                    showFirstStep();
                }
            }
        } else {
            showEmpty();
        }
    }

    @Override
    public void bindRealResult(String reason) {
        if (null != reason) statusTextView.setText(getString(R.string.reject_reason) + reason);
    }

    private void showEmpty() {
        submitSuccessImageView.setImageResource(R.drawable.ic_bind_google_gray_point);
        reviewImageView.setImageResource(R.drawable.ic_bind_google_gray_point);
        resultImageView.setImageResource(R.drawable.ic_bind_google_gray_point);
        divider1.setBackgroundColor(getResources().getColor(R.color.colorF1F1F1));
        divider2.setBackgroundColor(getResources().getColor(R.color.colorF1F1F1));

        submitSuccessTextView.setTextColor(getResources().getColor(R.color.colorB7B7C4));
        reviewTextView.setTextColor(getResources().getColor(R.color.colorB7B7C4));
        resultTextView.setTextColor(getResources().getColor(R.color.colorB7B7C4));

        statusImageView.setImageDrawable(null);
        statusTextView.setText("");
        statusHintTextView.setText("");

        submitButton.setVisibility(View.GONE);
    }

    private void showFirstStep() {
        submitSuccessImageView.setImageResource(R.drawable.ic_bind_google_blue_point);
        reviewImageView.setImageResource(R.drawable.ic_bind_google_gray_point);
        resultImageView.setImageResource(R.drawable.ic_bind_google_gray_point);
        divider1.setBackgroundColor(getResources().getColor(R.color.colorF1F1F1));
        divider2.setBackgroundColor(getResources().getColor(R.color.colorF1F1F1));

        submitSuccessTextView.setTextColor(getResources().getColor(R.color.color8488F5));
        reviewTextView.setTextColor(getResources().getColor(R.color.colorB7B7C4));
        resultTextView.setTextColor(getResources().getColor(R.color.colorB7B7C4));

        statusImageView.setImageResource(R.drawable.image_real_name_sub_suc);
        statusTextView.setText(R.string.exc_wal_submit_success);
        statusHintTextView.setText(R.string.real_name_status_submit_success_hint);

        submitButton.setVisibility(View.GONE);
    }

    private void showSecondStep() {
        submitSuccessImageView.setImageResource(R.drawable.ic_bind_google_blue_point);
        reviewImageView.setImageResource(R.drawable.ic_bind_google_blue_point);
        resultImageView.setImageResource(R.drawable.ic_bind_google_gray_point);
        divider1.setBackgroundColor(getResources().getColor(R.color.color8488F5));
        divider2.setBackgroundColor(getResources().getColor(R.color.colorF1F1F1));

        submitSuccessTextView.setTextColor(getResources().getColor(R.color.color8488F5));
        reviewTextView.setTextColor(getResources().getColor(R.color.color8488F5));
        resultTextView.setTextColor(getResources().getColor(R.color.colorB7B7C4));

        statusImageView.setImageResource(R.drawable.image_real_name_under_review);
        statusTextView.setText(R.string.under_review);
        statusHintTextView.setText(R.string.real_name_status_under_review);

        submitButton.setVisibility(View.GONE);
    }

    private void showSuccessStep() {
        submitSuccessImageView.setImageResource(R.drawable.ic_bind_google_blue_point);
        reviewImageView.setImageResource(R.drawable.ic_bind_google_blue_point);
        resultImageView.setImageResource(R.drawable.ic_bind_google_blue_point);
        divider1.setBackgroundColor(getResources().getColor(R.color.color8488F5));
        divider2.setBackgroundColor(getResources().getColor(R.color.color8488F5));

        submitSuccessTextView.setTextColor(getResources().getColor(R.color.color8488F5));
        reviewTextView.setTextColor(getResources().getColor(R.color.color8488F5));
        resultTextView.setTextColor(getResources().getColor(R.color.color8488F5));

        statusImageView.setImageResource(R.drawable.image_real_name_success);
        statusTextView.setText(R.string.successful_review);
        statusHintTextView.setText(R.string.real_name_status_success_hint);

        submitButton.setVisibility(View.GONE);
    }

    private void showFailStep() {
        submitSuccessImageView.setImageResource(R.drawable.ic_bind_google_blue_point);
        reviewImageView.setImageResource(R.drawable.ic_bind_google_blue_point);
        resultImageView.setImageResource(R.drawable.ic_bind_google_blue_point);
        divider1.setBackgroundColor(getResources().getColor(R.color.color8488F5));
        divider2.setBackgroundColor(getResources().getColor(R.color.color8488F5));

        submitSuccessTextView.setTextColor(getResources().getColor(R.color.color8488F5));
        reviewTextView.setTextColor(getResources().getColor(R.color.color8488F5));
        resultTextView.setTextColor(getResources().getColor(R.color.color8488F5));

        statusImageView.setImageResource(R.drawable.image_real_name_fail);
        statusTextView.setText(R.string.audit_failure);
        statusHintTextView.setText(R.string.rel_name_status_fail_hint);

        submitButton.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        if (mRealNameStatusPresenter != null) {
            mRealNameStatusPresenter.dropView();
        }
        super.onDestroy();
    }

}
