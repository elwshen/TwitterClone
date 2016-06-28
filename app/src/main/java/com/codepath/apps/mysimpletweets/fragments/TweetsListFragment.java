package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.models.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eshen on 6/27/16.
 */
public class TweetsListFragment extends Fragment {
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    private ListView lvTweets;

    //inflation logic
    //creation lifecycle

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        //connect adapter to listview
        lvTweets.setAdapter(aTweets);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //create arraylist
        tweets = new ArrayList<>();
        //construct adapter
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);
    }

    public void addAll (List<Tweet> tweets) {
        aTweets.addAll(tweets);
    }
}
