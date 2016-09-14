package com.knight.knight.cursorloaderpractice;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.MyViewHolder> {
    List<DataModel> dataList;

    @Override
    public ContentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item_layout,
                null);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContentAdapter.MyViewHolder holder, int position) {
        holder.titletextView.setText(dataList.get(position).title);
        holder.displayNametextView.setText(dataList.get(position).displayName);

        if (dataList.get(position).imageURI != null && !dataList.get(position).imageURI.equals("")) {
            Uri sArtworkUri = Uri
                    .parse("content://media/external/audio/albumart");

            Uri imgUri = ContentUris.withAppendedId(sArtworkUri,
                    Integer.parseInt(dataList.get(position).imageURI));
            Glide.with(holder.itemView.getContext())
                    .load(imgUri)
                    .into(holder.imgView);
        }
    }

    @Override
    public int getItemCount() {
        if (dataList == null)
            return 0;
        else
            return dataList.size();
    }

    public void addDataList(List<DataModel> dataList) {
        if (this.dataList == null)
            this.dataList = new ArrayList<>();

        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgView;
        public TextView titletextView, displayNametextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            titletextView = (TextView) itemView.findViewById(R.id.title_textview);
            displayNametextView = (TextView) itemView.findViewById(R.id.display_name_textview);
            imgView = (ImageView) itemView.findViewById(R.id.imgview);
        }
    }
}
