package com.example.aplikacija.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aplikacija.Adapter.Model.NewsModel;
import com.example.aplikacija.R;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MainViewHolder> {

    private List<NewsModel> newsList;
    private Context mContext;

    public NewsAdapter (List<NewsModel> newsModels, Context mContext) {
        this.newsList = newsModels;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //Surandamas list item elementas kuriame bus atvaizduojama informacija
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View listItem = inflater.inflate(R.layout.list_item, viewGroup, false);

        return new MainViewHolder(listItem);
    }



    @Override
    public void onBindViewHolder(@NonNull MainViewHolder mainViewHolder, int i) {
        //Uzkraunami duomenys list item elementui
        NewsModel newsModel = newsList.get(i);
        mainViewHolder.sectionName.setText(newsModel.getSectionNameNews());
        mainViewHolder.title.setText(newsModel.getTitleNews());
        mainViewHolder.webPublicationDate.setText(newsModel.getWebPublicationDateNews());

        //Atidaroma narsykle su linku i straipsni kuris buvo paspaustas
        final String url = newsModel.getWebUrl();
        mainViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                mContext.startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        //Gaunamas saraso ilgis
        return newsList.size();
    }

    class MainViewHolder extends RecyclerView.ViewHolder {

        private TextView sectionName;
        private TextView title;
        private TextView webPublicationDate;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            //Inicijuojami vaizdai
            sectionName = itemView.findViewById(R.id.sectionName);
            title = itemView.findViewById(R.id.title);
            webPublicationDate = itemView.findViewById(R.id.webPublicationDate);
        }
    }

}
