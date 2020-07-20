package com.dolphin.zanders.Adapter;

import com.dolphin.zanders.Fragment.CurrentOrder;
import com.dolphin.zanders.Fragment.PastOrder;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class MyOrdersPagerAdapter extends FragmentStatePagerAdapter {

    int tabCount;

    public MyOrdersPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount= tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                PastOrder tab1 = new PastOrder();
                return tab1;
            case 1:
                CurrentOrder tab2 = new CurrentOrder();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
