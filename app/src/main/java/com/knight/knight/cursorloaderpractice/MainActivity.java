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

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    TabLayout scrollableCategoryLayout;
    ViewPager pager;
    CategoryAdapter catAdapter;
    public static final int ByAuthor = 10;
    public static final int ByGenre = 20;
    public static final int ByAllSongs = 30;
    public static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 12;

    List<DataModel> dataListByGenre = new ArrayList<>();
    List<DataModel> dataListByAuthor = new ArrayList<>();
    List<DataModel> dataListByAllSongs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initAdapter();
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
        getSupportLoaderManager().initLoader(ByAuthor, null, this);
        getSupportLoaderManager().initLoader(ByGenre, null, this);
        getSupportLoaderManager().initLoader(ByAllSongs, null, this);
    }

    private void initViews() {
        scrollableCategoryLayout = (TabLayout) findViewById(R.id.scrollable_category_layout);
        pager = (ViewPager) findViewById(R.id.viewpager);
    }

    private void initAdapter() {
        catAdapter = new CategoryAdapter(this, getSupportFragmentManager());
        pager.setAdapter(catAdapter);

        scrollableCategoryLayout.setupWithViewPager(pager);
    }

    public List<DataModel> getListByAuthor() {
        return dataListByAuthor;
    }

    public List<DataModel> getListByGenre() {
        return dataListByGenre;
    }

    public List<DataModel> getListForAllSongs() {
        return dataListByAllSongs;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader;
        if (id == ByAuthor) {
            cursorLoader = new CursorLoader(this,
                    MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                    null,
                    null,
                    null,
                    MediaStore.Audio.ArtistColumns.ARTIST_KEY + " ASC");
        } else if (id == ByGenre) {
            cursorLoader = new CursorLoader(this,
                    MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI,
                    null,
                    null,
                    null,
                    MediaStore.Audio.GenresColumns.NAME + " ASC");
        } else if (id == ByAllSongs) {
            cursorLoader = new CursorLoader(this,
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    null,
                    MediaStore.Audio.Media.IS_MUSIC + "!=?",
                    new String[]{"0"},
                    MediaStore.Audio.Media.ARTIST+ " ASC");
        } else {
            throw new RuntimeException("No Id Match found");
        }
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == ByAuthor) {
            dataListByAuthor.clear();
            while (data.moveToNext()) {
                for (String colName : data.getColumnNames())
                    Log.e("MainActivity " + "Author ",
                            colName + " "
                                    + data.getColumnIndex(MediaStore.Audio.Artists.ARTIST)
                                    + " " + data.getString(1));


                DataModel dataModel = new DataModel();
                dataModel.title = data.getString(data.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
                dataModel.displayName = "Number of Albums: " + data.getString(data.getColumnIndex
                        (MediaStore.Audio.Artists.NUMBER_OF_ALBUMS))
                        + " Tracks: " + data.getString(data.getColumnIndex
                        (MediaStore.Audio.Artists.NUMBER_OF_TRACKS));
                dataListByAuthor.add(dataModel);
            }
            Log.e("MainActivity", "By Author Size:" + data.getCount());
        } else if (loader.getId() == ByGenre) {
            dataListByGenre.clear();
            for (String colName : data.getColumnNames())
                Log.e("MainActivity " + "Genre ", colName);

            while (data.moveToNext()) {
                DataModel dataModel = new DataModel();
                dataModel.title = data.getString(data.getColumnIndex(MediaStore.Audio
                        .Genres.NAME));
                dataListByGenre.add(dataModel);
            }

            Log.e("MainActivity", "By Genre Size:" + data.getCount());
        } else if (loader.getId() == ByAllSongs) {
            dataListByAllSongs.clear();
            for (String colName : data.getColumnNames())
                Log.e("MainActivity " + "All Songs", colName);

            while (data.moveToNext()) {
                DataModel dataModel = new DataModel();
                dataModel.title = data.getString(data.getColumnIndex(MediaStore.Audio.Media.TITLE));
                dataModel.displayName = data.getString(data.getColumnIndex(MediaStore.Audio.Media.TITLE));
                dataListByAllSongs.add(dataModel);
            }

            Log.e("MainActivity", "By Songs Size:" + data.getCount());
        } else {
            Log.e("MainActivity", "No Data LoaderId:" + loader.getId());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        dataListByAllSongs.clear();
        dataListByAuthor.clear();
        dataListByGenre.clear();
    }
}
