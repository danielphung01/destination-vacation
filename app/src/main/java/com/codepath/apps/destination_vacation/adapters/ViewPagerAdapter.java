package com.codepath.apps.destination_vacation.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.codepath.apps.destination_vacation.R;

import org.jetbrains.annotations.NotNull;

public class ViewPagerAdapter extends PagerAdapter {

    public Object instantiateItem(ViewGroup collection, int position) {

        int resId = 0;
        switch (position) {
            case 0:
                resId = R.id.page_one;
                break;
            case 1:
                resId = R.id.page_two;
                break;
        }
        return collection.findViewById(resId);

    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Recent Searches";
            case 1:
                return "Bookmarks";
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return view == object;
    }
}
