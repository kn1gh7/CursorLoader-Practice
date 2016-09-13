package com.knight.knight.cursorloaderpractice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ContentFragment extends Fragment {
    public static final String CATEGORY_REQUEST = "category";
    RecyclerView contentRecyclerview;
    ContentAdapter contentAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_layout, container, false);
        contentRecyclerview = (RecyclerView) view.findViewById(R.id.content_recyclerview);
        contentRecyclerview.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        contentAdapter = new ContentAdapter();
        contentRecyclerview.setAdapter(contentAdapter);
        String requestCategory = getArguments().getString(CATEGORY_REQUEST);
        String[] categories = getResources().getStringArray(R.array.categories);

        List<DataModel> dataList = null;

        if (requestCategory.equals(getResources().getString(R.string.category_author))) {
            dataList = ((MainActivity) getActivity()).getListByAuthor();
        } else if (requestCategory.equals(getResources().getString(R.string.category_genre))) {
            dataList = ((MainActivity) getActivity()).getListByGenre();
        } else if (requestCategory.equals(getResources().getString(R.string.category_allsong))) {
            dataList = ((MainActivity) getActivity()).getListForAllSongs();
        }
        contentAdapter.addDataList(dataList);
        return view;
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
}
