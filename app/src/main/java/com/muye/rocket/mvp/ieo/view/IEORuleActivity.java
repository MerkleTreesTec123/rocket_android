package com.muye.rocket.mvp.ieo.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ifenduo.common.tool.DimensionTool;
import com.muye.rocket.R;
import com.muye.rocket.base.BaseActivity;

import butterknife.BindView;

public class IEORuleActivity extends BaseActivity {

    @BindView(R.id.image_view)
    ImageView imageView;
    int mScreenWidth;
    String mRuleImageURL;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_ieo_rule;
    }

    public static void openIEORuleActivity(Context context, String ruleImageURL) {
        Intent intent = new Intent(context, IEORuleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("ruleImageURL", ruleImageURL);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreateViewAfter(Bundle savedInstanceState) {
        super.onCreateViewAfter(savedInstanceState);
        setNavigationCenter(getString(R.string.rule_description));
        mScreenWidth = DimensionTool.getScreenWidth(this);
        loadImage();
    }

    @Override
    protected void handleIntent(Bundle bundle) {
        super.handleIntent(bundle);
        if (bundle != null) {
            mRuleImageURL = bundle.getString("ruleImageURL", "");
        }
    }

    private void loadImage() {
        Glide.with(this).asBitmap().load(mRuleImageURL).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                if (resource != null) {
                    int imageHeight = (int) (mScreenWidth * (resource.getHeight() * 1f / resource.getWidth()) * 1f);
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) imageView.getLayoutParams();
                    layoutParams.height = imageHeight;
                    imageView.setLayoutParams(layoutParams);
                    imageView.setImageBitmap(resource);
                }
            }
        });
    }
}
