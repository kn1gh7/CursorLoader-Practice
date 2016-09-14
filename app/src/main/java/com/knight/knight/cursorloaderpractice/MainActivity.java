package com.knight.knight.cursorloaderpractice;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 12;
    private TabLayout scrollableCategoryLayout;
    private ViewPager pager;
    private CategoryAdapter catAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_REQUEST_CODE);
        } else {
            requestFetchList();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestFetchList();
                } else {
                    finish();
                }
                return;
            }
        }
    }

    public void requestFetchList() {
        initAdapter();
    }

    private void initViews() {
        scrollableCategoryLayout = (TabLayout) findViewById(R.id.scrollable_category_layout);
        pager = (ViewPager) findViewById(R.id.viewpager);
    }

    private void initAdapter() {
        catAdapter = new CategoryAdapter(this, getSupportFragmentManager());
        pager.setAdapter(catAdapter);
        pager.setOffscreenPageLimit(2);

        scrollableCategoryLayout.setupWithViewPager(pager);
    }
}
