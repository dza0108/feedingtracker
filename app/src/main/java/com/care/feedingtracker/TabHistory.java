package com.care.feedingtracker;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TabHistory extends Fragment{

    private static String TAG = "TabHistory";
    private FeedViewModel mFeedData = null;

    private ScrollView scroll;

    private List<Feed> mFeedCached;

    private List<String> mList;

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab_history, container, false);

        mFeedData = ((MainActivity)getActivity()).getmFeedViewModel();

        scroll = rootView.findViewById(R.id.scroll);

        mList = new ArrayList<String>();

        final Observer<List<Feed>> nameObserver = new Observer<List<Feed>>() {
            @Override
            public void onChanged(@Nullable List<Feed> feeds) {
                mFeedCached = feeds;
                mList.clear();

                for (int i = 0; i < feeds.size(); i++) {

                    Log.d(TAG, "test: " + feeds.get(i).getTimestamp() + ", " + feeds.get(i).getMilk_volume() + ", "
                            + feeds.get(i).getSolid_name() + ", " + feeds.get(i).getDiaper());

                    Date t = new Date();
                    t.setTime(feeds.get(i).getTimestamp());
                    SimpleDateFormat formatter = new SimpleDateFormat(getResources().getString(R.string.timestamp_format));

                    StringBuilder b = new StringBuilder("Time: ");
                    b.append(formatter.format(t));

                    if (feeds.get(i).getMilk_volume() != 0) {
                        b.append("\n -> Food: " + feeds.get(i).getMilk_volume() + "ml; ");
                    } else if (!feeds.get(i).getSolid_name().isEmpty()) {
                        b.append("\n -> Food: " + feeds.get(i).getSolid_name() + "; ");
                    } else {
                        b.append("\n -> ");
                    }

                    if (!feeds.get(i).getDiaper().matches("None")) {
                        b.append("Diaper: " + feeds.get(i).getDiaper());
                    }

                    mList.add(b.toString());
                }
                updateListView();
            }
        };

        mFeedData.getAllWords().observe(this, nameObserver);

        return rootView;
    }

    private void updateListView () {

        ListView list = rootView.findViewById(R.id.list_view);

        Log.d(TAG, "size " + mList.size());

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>((MainActivity)getActivity(), android.R.layout.simple_list_item_1, mList);
        CustomAdapter adapter = new CustomAdapter((ArrayList<Feed>) mFeedCached, (MainActivity)getActivity());

        list.setAdapter(adapter);
        return;
    }


    public class CustomAdapter extends BaseAdapter implements ListAdapter {

        private ArrayList<Feed> list = new ArrayList<Feed>();
        private Context context;

        public CustomAdapter(ArrayList<Feed> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;

            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.listview_item, null);
            }

            TextView listItemText = view.findViewById(R.id.list_item);
            listItemText.setText(list.get(position).toFormattedString());

            Button deleteBtn = view.findViewById(R.id.delete);
            deleteBtn.setText("delete");
            deleteBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "position: " + position);

                    AlertDialog alert = new AlertDialog.Builder((MainActivity)getActivity())
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            })
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mFeedData.delete(list.get(position));
                                    notifyDataSetChanged();
                                }
                            })
                            .create();
                    alert.setTitle("Confirm Delete");
                    alert.setMessage("Are you sure to delete");

                    alert.show();
                }
            });

            return view;
        }
    }


}
