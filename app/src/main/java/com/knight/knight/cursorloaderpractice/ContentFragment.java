package com.knight.knight.cursorloaderpractice;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

public class ContentFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final int ByAuthor = 10;
    public static final int ByGenre = 20;
    public static final int ByAllSongs = 30;
    public static final int ByAlbums = 40;
    public static final String CATEGORY_REQUEST = "category";
    private RecyclerView contentRecyclerview;
    private ContentAdapter contentAdapter;
    private ProgressBar progressbar;
    private List<DataModel> dataListByGenre = new ArrayList<>();
    private List<DataModel> dataListByAuthor = new ArrayList<>();
    private List<DataModel> dataListByAllSongs = new ArrayList<>();
    private List<DataModel> dataListByAlbums = new ArrayList<>();
    private String requestCategory;
    private boolean isVisible;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_layout, container, false);
        contentRecyclerview = (RecyclerView) view.findViewById(R.id.content_recyclerview);
        progressbar = (ProgressBar) view.findViewById(R.id.progress_bar);
        contentRecyclerview.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        contentAdapter = new ContentAdapter();
        contentRecyclerview.setAdapter(contentAdapter);
        requestCategory = getArguments().getString(CATEGORY_REQUEST);

        if (isVisible) {
            requestContentList();
        }
        Log.e("Fragment", "onCreateView");

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isResumed()) {
                Log.e("Fragment", "visible hint visible " + requestCategory);
                if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission
                        .READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    requestContentList();
                } else {
                    Log.e("Fragment", "No Permission to access Files");
                }
            } else {
                Log.e("Fragment", "not resumed ");
                isVisible = isVisibleToUser;
            }
        }

        Log.e("Fragment", "Not visible hint ");
    }

    private void requestContentList() {
        if (requestCategory.equals(getResources().getString(R.string.category_author)) &&
                (dataListByAuthor == null || dataListByAuthor.size() == 0)) {
            progressbar.setVisibility(View.VISIBLE);
            getLoaderManager().initLoader(ByAuthor, null, this);
        } else if (requestCategory.equals(getResources().getString(R.string.category_genre))
                && (dataListByGenre == null || dataListByGenre.size() == 0)) {
            progressbar.setVisibility(View.VISIBLE);
            getLoaderManager().initLoader(ByGenre, null, this);
        } else if (requestCategory.equals(getResources().getString(R.string.category_allsong))
                && (dataListByAllSongs == null || dataListByAllSongs.size() == 0)) {
            progressbar.setVisibility(View.VISIBLE);
            getLoaderManager().initLoader(ByAllSongs, null, this);
        } else if (requestCategory.equals(getResources().getString(R.string.category_albums))
                && (dataListByAlbums == null || dataListByAlbums.size() == 0)) {
            progressbar.setVisibility(View.VISIBLE);
            getLoaderManager().initLoader(ByAlbums, null, this);
        }
    }

    private List<DataModel> getDummyContentData() {
        List<DataModel> dataList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            DataModel dataModel = new DataModel();
            dataModel.title = "Title " + i;
            dataModel.displayName = "Name " + i;
            dataList.add(dataModel);
        }

        return dataList;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader;
        if (id == ByAuthor) {
            cursorLoader = new CursorLoader(this.getActivity(),
                    MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                    null,
                    null,
                    null,
                    MediaStore.Audio.ArtistColumns.ARTIST_KEY + " ASC");
        } else if (id == ByGenre) {
            cursorLoader = new CursorLoader(this.getActivity(),
                    MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI,
                    null,
                    null,
                    null,
                    MediaStore.Audio.GenresColumns.NAME + " ASC");
        } else if (id == ByAllSongs) {
            cursorLoader = new CursorLoader(this.getActivity(),
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    null,
                    MediaStore.Audio.Media.IS_MUSIC + "!=?",
                    new String[]{"0"},
                    MediaStore.Audio.Media.TITLE + " ASC");
        } else if (id == ByAlbums) {
            cursorLoader = new CursorLoader(this.getActivity(),
                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    null,
                    null,
                    null,
                    MediaStore.Audio.Albums.ALBUM + " ASC");
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
                /*for (String colName : data.getColumnNames())
                    Log.e("Content Fragment " + "Author ",
                            colName + " "
                                    + data.getColumnIndex(MediaStore.Audio.Artists.ARTIST)
                                    + " " + data.getString(1));*/


                DataModel dataModel = new DataModel();
                dataModel.title = data.getString(data.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
                dataModel.displayName = "Number of Albums: " + data.getString(data.getColumnIndex
                        (MediaStore.Audio.Artists.NUMBER_OF_ALBUMS))
                        + " Tracks: " + data.getString(data.getColumnIndex
                        (MediaStore.Audio.Artists.NUMBER_OF_TRACKS));
                dataListByAuthor.add(dataModel);
            }

            contentAdapter.addDataList(dataListByAuthor);
            Log.e("Content Fragment", "By Author Size:" + data.getCount());
        } else if (loader.getId() == ByGenre) {
            dataListByGenre.clear();
            /*for (String colName : data.getColumnNames())
                Log.e("Content Fragment " + "Genre ", colName);*/

            while (data.moveToNext()) {
                DataModel dataModel = new DataModel();
                dataModel.title = data.getString(data.getColumnIndex(MediaStore.Audio
                        .Genres.NAME));
                dataListByGenre.add(dataModel);
            }
            contentAdapter.addDataList(dataListByGenre);
            Log.e("Content Fragment", "By Genre Size:" + data.getCount());
        } else if (loader.getId() == ByAllSongs) {
            dataListByAllSongs.clear();
            /*for (String colName : data.getColumnNames())
                Log.e("Content Fragment " + "All Songs", colName);*/

            while (data.moveToNext()) {
                DataModel dataModel = new DataModel();
                dataModel.title = data.getString(data.getColumnIndex(MediaStore.Audio.Media.TITLE));
                dataModel.displayName = Utils.formatDuration(data.getString(data.getColumnIndex
                        (MediaStore.Audio.Media.DURATION)));
                dataModel.imageURI = data.getString(data.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                dataListByAllSongs.add(dataModel);
            }
            contentAdapter.addDataList(dataListByAllSongs);
            Log.e("Content Fragment", "By Songs Size:" + data.getCount());
        } else if (loader.getId() == ByAlbums) {
            dataListByAlbums.clear();

            while (data.moveToNext()) {
                DataModel dataModel = new DataModel();
                dataModel.title = data.getString(data.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
                dataModel.displayName = data.getString(data.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS));
                dataModel.imageURI = data.getInt(data.getColumnIndex(MediaStore.Audio.Albums._ID)) +
                        "";
                dataListByAlbums.add(dataModel);
            }
            contentAdapter.addDataList(dataListByAlbums);
            Log.e("Content Fragment", "By Albums Size:" + data.getCount());
        } else {
            Log.e("Content Fragment", "No Data LoaderId:" + loader.getId());
        }
        progressbar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        /*dataListByAllSongs.clear();
        dataListByAuthor.clear();
        dataListByGenre.clear();*/
    }
}
