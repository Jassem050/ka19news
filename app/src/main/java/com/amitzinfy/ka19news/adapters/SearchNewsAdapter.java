package com.amitzinfy.ka19news.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.recyclerview.widget.RecyclerView;

import com.amitzinfy.ka19news.R;
import com.amitzinfy.ka19news.models.retrofit.News;
import com.amitzinfy.ka19news.utils.GlideApp;
import com.amitzinfy.ka19news.utils.NetworkUtils;

import java.util.List;

public class SearchNewsAdapter extends RecyclerView.Adapter<SearchNewsAdapter.SearchNewsViewHolder> {

    private Context context;
    private List<News> newsList;
    private NewsItemClickListener newsItemClickListener;

    public static class SearchNewsViewHolder extends RecyclerView.ViewHolder{
        private AppCompatTextView newsTitle;
        private AppCompatImageView newsImage;
        private AppCompatToggleButton favToggleButton;

        public SearchNewsViewHolder(@NonNull View itemView, NewsItemClickListener newsItemClickListener) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.news_title);
            newsImage = itemView.findViewById(R.id.news_image);
            favToggleButton = itemView.findViewById(R.id.news_toggle_btn);

            favToggleButton.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b){
                    newsItemClickListener.onItemToggleButtonChecked(getAdapterPosition());
                } else {
                    newsItemClickListener.onItemToggleButtonUnChecked(getAdapterPosition());
                }
            });
        }
    }

    public SearchNewsAdapter(Context context, NewsItemClickListener newsItemClickListener){
        this.context = context;
        this.newsItemClickListener = newsItemClickListener;
    }

    public void setNewsList(List<News> newsList){
            this.newsList = newsList;
            notifyDataSetChanged();
    }

    public void clearAllNews(){
        if (newsList != null){
            newsList.clear();
        }
    }

    @NonNull
    @Override
    public SearchNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_row_1, parent, false);
        return new SearchNewsViewHolder(view, newsItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchNewsViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.newsTitle.setText(news.getTitle());
        GlideApp.with(context).load(NetworkUtils.IMAGE_URL + news.getImage()).into(holder.newsImage);
        newsItemClickListener.setItemToggleButton(holder.favToggleButton, holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return newsList != null ? newsList.size() : 0;
    }


    public interface NewsItemClickListener{
        void onItemToggleButtonChecked(int position);
        void onItemToggleButtonUnChecked(int position);
        void setItemToggleButton(ToggleButton toggleButton, int position);
    }

}
