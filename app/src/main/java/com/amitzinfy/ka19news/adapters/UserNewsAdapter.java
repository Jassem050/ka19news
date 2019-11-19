package com.amitzinfy.ka19news.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.amitzinfy.ka19news.R;
import com.amitzinfy.ka19news.models.retrofit.News;
import com.amitzinfy.ka19news.utils.NetworkUtils;
import com.chinalwb.are.glidesupport.GlideApp;

import java.util.List;

public class UserNewsAdapter extends RecyclerView.Adapter<UserNewsAdapter.UserNewsViewHolder> {

    private List<News> newsList;
    private Context context;
    private UserNewsClickListener userNewsClickListener;

    public static class UserNewsViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView newsTitle;
        private AppCompatImageView newsImage;
        private AppCompatTextView newsCategory;
        private AppCompatTextView newsStatus;

        public UserNewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.news_title);
            newsImage = itemView.findViewById(R.id.news_image);
            newsCategory = itemView.findViewById(R.id.news_category);
            newsStatus = itemView.findViewById(R.id.news_status);
        }
    }

    public UserNewsAdapter(Context context, UserNewsClickListener userNewsClickListener){
        this.context = context;
        this.userNewsClickListener = userNewsClickListener;
    }

    public void setNewsList(List<News> newsList){
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public UserNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_row_2, parent, false);
        return new UserNewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserNewsViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.newsTitle.setText(news.getTitle());
        GlideApp.with(context).load(NetworkUtils.IMAGE_URL + news.getImage())
                .placeholder(R.drawable.placeholder_image).into(holder.newsImage);
        holder.newsCategory.setText(news.getCategoryName());
//        if (news.)
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public interface UserNewsClickListener{
        void onNewsItemClick(int position);
    }


}
