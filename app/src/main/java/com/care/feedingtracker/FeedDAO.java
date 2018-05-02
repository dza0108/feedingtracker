package com.care.feedingtracker;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FeedDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Feed feed);

    @Query("DELETE FROM feed_table")
    void deleteAll();

    @Query("SELECT * FROM feed_table ORDER BY timestamp DESC")
    LiveData<List<Feed>> getAllFeed();

    @Query("DELETE FROM feed_table WHERE timestamp = :timestamp")
    void remove(long timestamp);
}
