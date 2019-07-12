package com.amitzinfy.ka19news.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.recyclerview.widget.RecyclerView;

import com.amitzinfy.ka19news.R;
import com.amitzinfy.ka19news.models.room.FavouriteNews;
import com.amitzinfy.ka19news.utils.GlideApp;
import com.amitzinfy.ka19news.utils.NetworkUtils;

import java.util.List;

public class FavouriteNewsAdapter extends RecyclerView.Adapter<FavouriteNewsAdapter.FavNewsViewHolder> {

    private final Context context;
    private List<FavouriteNews> favouriteNewsList;
    private FavNewsItemClickListener favNewsItemClickListener;

    public static class FavNewsViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView newsTitle;
        private AppCompatImageView newsImage;
        private AppCompatToggleButton favToggleButton;
        public FavNewsViewHolder(@NonNull View itemView, FavNewsItemClickListener favNewsItemClickListener) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.news_title);
            newsImage = itemView.findViewById(R.id.news_image);
            favToggleButton = itemView.findViewById(R.id.news_toggle_btn);
            favToggleButton.setOnCheckedChangeListener((compoundButton, b) -> {
                if (!b){
                    favNewsItemClickListener.onItemToggleButtonUnChecked(getAdapterPosition());
                }
            });
        }
    }

    public FavouriteNewsAdapter(Context context, FavNewsItemClickListener favNewsItemClickListener){
        this.context = context;
        this.favNewsItemClickListener = favNewsItemClickListener;
    }

    public void setFavouriteNewsList(List<FavouriteNews> favouriteNewsList){
        this.favouriteNewsList = favouriteNewsList;
    }

    @NonNull
    @Override
    public FavNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_row_1, parent, false);
        return new FavNewsViewHolder(view, favNewsItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FavNewsViewHolder holder, int position) {
        FavouriteNews favouriteNews = favouriteNewsList.get(position);

        holder.newsTitle.setText(favouriteNews.getTitle());
        GlideApp.with(context).load(NetworkUtils.IMAGE_URL + favouriteNews.getImage()).into(holder.newsImage);

    }

    @Override
    public int getItemCount() {
        return favouriteNewsList != null ? favouriteNewsList.size() : 0;
    }

    public interface FavNewsItemClickListener{
        void onItemToggleButtonUnChecked(int position);
    }
}
