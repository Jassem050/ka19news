package com.amitzinfy.ka19news.models.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "feed_categories")
public class FeedCategory {

    @PrimaryKey
    @ColumnInfo
    private int id;

    public FeedCategory(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
