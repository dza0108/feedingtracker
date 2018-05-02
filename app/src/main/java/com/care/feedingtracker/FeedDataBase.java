package com.care.feedingtracker;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


@Database(entities = {Feed.class}, version = 1)
public abstract class FeedDataBase extends RoomDatabase {
    public abstract FeedDAO feedDao();

    private static FeedDataBase INSTANCE;

    static FeedDataBase getDataBase (final Context context) {
        if (INSTANCE == null ) {
            synchronized (FeedDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FeedDataBase.class, "feed_database").addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    private static class createDataBase extends AsyncTask<Void, Void, Void> {
        private final FeedDAO mDao;
        createDataBase(FeedDataBase db) {
            mDao = db.feedDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            /*mDao.deleteAll();

            Date d = Calendar.getInstance().getTime();

            Food f = new Food(180);

            Feed feed = new Feed(d.getTime(), f.getVolume(), f.getFood_name(), "Wet");
            mDao.insert(feed);

            int i = 0;
            while (i < 10) {
                f = new Food("Carrot");
                feed = new Feed(d.getTime()+123423*i, f.getVolume(), f.getFood_name(), "Both");
                i++;
                mDao.insert(feed);
            }*/

            return null;
        }
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){
                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    //new createDataBase(INSTANCE).execute();
                }
            };

}
