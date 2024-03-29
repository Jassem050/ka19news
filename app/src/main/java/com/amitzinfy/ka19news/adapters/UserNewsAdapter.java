package com.amitzinfy.ka19news.adapters;

import android.content.Context;
import android.text.format.DateUtils;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class UserNewsAdapter extends RecyclerView.Adapter<UserNewsAdapter.UserNewsViewHolder> {

    private List<News> newsList;
    private Context context;
    private UserNewsClickListener userNewsClickListener;

    public static class UserNewsViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView newsTitle;
        private AppCompatImageView newsImage;
        private AppCompatTextView newsCategory;
        private AppCompatTextView newsStatus, newsTime;

        public UserNewsViewHolder(@NonNull View itemView, UserNewsClickListener userNewsClickListener) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.news_title);
            newsImage = itemView.findViewById(R.id.news_image);
            newsCategory = itemView.findViewById(R.id.news_category);
            newsStatus = itemView.findViewById(R.id.news_status);
            newsTime = itemView.findViewById(R.id.news_date);
            itemView.setOnClickListener(view -> userNewsClickListener.onNewsItemClick(getAdapterPosition()));
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
        return new UserNewsViewHolder(view, userNewsClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserNewsViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.newsTitle.setText(news.getTitle());
        GlideApp.with(context).load(NetworkUtils.IMAGE_URL + news.getImage())
                .placeholder(R.drawable.placeholder_image).into(holder.newsImage);
        holder.newsCategory.setText(news.getCategoryName());
        if (news.getWriterId() != null && news.getNewsStatus().equals("1")){
            holder.newsStatus.setText(context.getString(R.string.status_accepted));
            holder.newsStatus.setTextColor(context.getResources().getColor(R.color.accept_green));
        } else if (news.getWriterId() != null && news.getNewsStatus().equals("2")){
            holder.newsStatus.setText(context.getString(R.string.status_rejected));
            holder.newsStatus.setTextColor(context.getResources().getColor(R.color.reject_red));
        } else {
            holder.newsStatus.setText(context.getString(R.string.status_pending));
            holder.newsStatus.setTextColor(context.getResources().getColor(R.color.pending_yellow));
        }
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
        return newsList != null ? newsList.size() : 0;
    }

    public interface UserNewsClickListener{
        void onNewsItemClick(int position);
    }


}
