package com.care.feedingtracker;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class FeedViewModel extends AndroidViewModel {

    private enum OPS {
        INSERT,
        DELETE
    }

    private FeedDAO mFeedDAO;
    private LiveData<List<Feed>> mAllFeed;

    public FeedViewModel (Application application) {
        super(application);
        FeedDataBase db = FeedDataBase.getDataBase(application);
        mFeedDAO = db.feedDao();
        mAllFeed = mFeedDAO.getAllFeed();
    }

    LiveData<List<Feed>> getAllWords() {
        return mAllFeed;
    }

    public void insert(Feed feed) {
        TaskParams task = new TaskParams(feed, OPS.INSERT);
        new opAsyncTask(mFeedDAO).execute(task);
    }

    public void delete(Feed feed) {
        TaskParams task = new TaskParams(feed, OPS.DELETE);
        new opAsyncTask(mFeedDAO).execute(task);
    }

    private static class opAsyncTask extends AsyncTask<TaskParams, OPS, Void> {

        private FeedDAO mAsyncTaskDao;

        opAsyncTask(FeedDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TaskParams... params) {
            if (params[0].ops == OPS.INSERT) {
                mAsyncTaskDao.insert(params[0].mFeed);
            } else if (params[0].ops == OPS.DELETE) {
                mAsyncTaskDao.remove(params[0].mFeed.getTimestamp());
            }

            return null;
        }
    }

    private class TaskParams{
        Feed mFeed;
        OPS ops;

        public TaskParams(Feed f, OPS o) {
            mFeed = f;
            ops = o;
        }
    }
}


