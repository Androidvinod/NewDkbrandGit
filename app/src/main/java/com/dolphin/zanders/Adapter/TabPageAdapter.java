package com.dolphin.zanders.Adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.dolphin.zanders.Fragment.AdditionInformatonFragment;
import com.dolphin.zanders.Fragment.FeaturesFragment;
import com.dolphin.zanders.Fragment.QuickOverviewFragment;

/**
 * Created by ap6 on 29/9/18.
 */

public class TabPageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;


    public TabPageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                QuickOverviewFragment tab1 = new QuickOverviewFragment();
                return tab1;
            case 1:
                FeaturesFragment tab2 = new FeaturesFragment();
                return tab2;
            case 2:
                AdditionInformatonFragment tab3 = new AdditionInformatonFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}