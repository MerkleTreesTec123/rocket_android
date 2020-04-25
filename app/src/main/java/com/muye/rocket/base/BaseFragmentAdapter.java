package com.muye.rocket.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

public class BaseFragmentAdapter extends FragmentPagerAdapter {


    private List<Fragment> mFragments;
    private List<String> mTitles;

    public BaseFragmentAdapter(FragmentManager fragmentManager, List<Fragment> fragments, List<String> titles){
        super(fragmentManager);
        mFragments=fragments;
        mTitles=titles;
    }

    public BaseFragmentAdapter(FragmentManager fragmentManager, List<Fragment> fragments){
        super(fragmentManager);
        mFragments=fragments;
    }


    //配合TabLayout使用
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }


    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }
}
