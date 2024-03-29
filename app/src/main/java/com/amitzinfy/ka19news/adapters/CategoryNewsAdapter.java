package com.amitzinfy.ka19news.adapters;

import android.content.Context;
import android.text.format.DateUtils;
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
import com.amitzinfy.ka19news.utils.NetworkUtils;
import com.chinalwb.are.glidesupport.GlideApp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class CategoryNewsAdapter extends RecyclerView.Adapter<CategoryNewsAdapter.NewsViewHolder> {

    private Context context;
    private List<News> newsList;
    private NewsItemClickListener newsItemClickListener;

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView newsTitle;
        private AppCompatImageView newsImage;
        private AppCompatToggleButton favToggleButton;
        private AppCompatTextView newsCategory;
        private AppCompatTextView newsShareBtn, newsTime;

        public NewsViewHolder(@NonNull View itemView, NewsItemClickListener newsItemClickListener) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.news_title);
            newsImage = itemView.findViewById(R.id.news_image);
            favToggleButton = itemView.findViewById(R.id.news_toggle_btn);
            newsCategory = itemView.findViewById(R.id.news_category);
            newsShareBtn = itemView.findViewById(R.id.news_share_btn);
            newsTime = itemView.findViewById(R.id.news_date);

            favToggleButton.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b){
                    newsItemClickListener.onItemToggleButtonChecked(getAdapterPosition());
                } else {
                    newsItemClickListener.onItemToggleButtonUnChecked(getAdapterPosition());
                }
            });
        }
    }

    public CategoryNewsAdapter(Context context, NewsItemClickListener newsItemClickListener){
        this.context = context;
        this.newsItemClickListener = newsItemClickListener;
    }

    public void setNewsList(List<News> newsList){
            this.newsList = newsList;
            notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_row_1, parent, false);
        return new NewsViewHolder(view, newsItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.newsTitle.setText(news.getTitle());
        GlideApp.with(context).load(NetworkUtils.IMAGE_URL + news.getImage())
                .placeholder(R.drawable.placeholder_image).into(holder.newsImage);
        newsItemClickListener.setItemToggleButton(holder.favToggleButton, holder.getAdapterPosition());
        holder.newsCategory.setText(news.getCategoryName());
        holder.itemView.setOnClickListener(view -> newsItemClickListener.onItemClicked(holder.getAdapterPosition()));
        holder.newsShareBtn.setOnClickListener(view -> newsItemClickListener.onShareButtonClicked(holder.getAdapterPosition()));

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            cal.setTime(sdf.parse(news.getDate() + " " + news.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long timeInMillis = cal.getTimeInMillis();
        String time = DateUtils.getRelativeTimeSpanString(timeInMillis,  System.currentTimeMillis(),DateUtils.SECOND_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_MONTH).toString();
        holder.newsTime.setText(time);
    }

    @Override
    public int getItemCount() {
        if (newsList != null){
            return newsList.size();
        } else {
            return 0;
        }
    }

    public interface NewsItemClickListener{
        void onItemToggleButtonChecked(int position);
        void onItemToggleButtonUnChecked(int position);
        void setItemToggleButton(ToggleButton toggleButton, int position);
        void onItemClicked(int position);
        void onShareButtonClicked(int position);
    }
}
