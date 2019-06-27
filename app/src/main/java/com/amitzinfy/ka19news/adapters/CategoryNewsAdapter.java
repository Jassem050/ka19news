package com.amitzinfy.ka19news.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.amitzinfy.ka19news.R;
import com.amitzinfy.ka19news.models.retrofit.News;

import java.util.List;

public class CategoryNewsAdapter extends RecyclerView.Adapter<CategoryNewsAdapter.NewsViewHolder> {

    private Context context;
    private List<News> newsList;

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView newsTitle;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.news_title);
        }
    }

    public CategoryNewsAdapter(Context context){
        this.context = context;
    }

    public void setNewsList(List<News> newsList){
        if (this.newsList == null){
            this.newsList = newsList;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_row_1, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.newsTitle.setText(news.getTitle());

    }

    @Override
    public int getItemCount() {
        if (newsList != null){
            return newsList.size();
        } else {
            return 0;
        }
    }


}
