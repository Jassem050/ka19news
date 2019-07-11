package com.amitzinfy.ka19news.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FavouriteNewsAdapter extends RecyclerView.Adapter<FavouriteNewsAdapter.FavNewsViewHolder> {

    public static class FavNewsViewHolder extends RecyclerView.ViewHolder {
        public FavNewsViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public FavNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull FavNewsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
