package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by eshen on 6/27/16.
 */
// Takes the tweet objects and turns them into views displayed in the list
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {
    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, 0, tweets);
    }

    //viewholder pattern

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get tweets
        Tweet tweet = getItem(position);
        // inflate template
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }
        // find subviews to fill with data
        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        // populate data into subviews
        tvUsername.setText(tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);
        // return view to be inserted into the list
        return convertView;
    }

    // Override and setup custom template
}
