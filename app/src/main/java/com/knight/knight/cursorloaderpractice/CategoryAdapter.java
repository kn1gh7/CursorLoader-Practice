package com.knight.knight.cursorloaderpractice;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class CategoryAdapter extends FragmentStatePagerAdapter {
    String[] categories;

    public CategoryAdapter(Activity context, FragmentManager fm) {
        super(fm);
        categories = context.getResources().getStringArray(R.array.categories);
    }

    @Override
    public Fragment getItem(int position) {
        ContentFragment contentFragment = new ContentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ContentFragment.CATEGORY_REQUEST, categories[position]);
        contentFragment.setArguments(bundle);
        return contentFragment;
    }

    @Override
    public int getCount() {
        return categories.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return categories[position];
    }
}
