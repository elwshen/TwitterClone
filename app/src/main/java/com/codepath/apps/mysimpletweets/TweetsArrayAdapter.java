package com.codepath.apps.mysimpletweets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by eshen on 6/27/16.
 */
// Takes the tweet objects and turns them into views displayed in the list
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {
    ImageView ivProfileImage;
    ImageButton btnReply;
    ImageButton btnFavorite;
    TextView tvBody;
    Tweet tweet;
    TwitterClient client;
    ImageButton btnRetweet;
    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, 0, tweets);
    }

    //viewholder pattern

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get tweets
        tweet = getItem(position);
        // inflate template
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }
        // find subviews to fill with data
        ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        TextView tvRealName = (TextView) convertView.findViewById(R.id.tvRealName);
        TextView tvElapsed = (TextView) convertView.findViewById(R.id.tvElapsed);
        btnReply = (ImageButton) convertView.findViewById(R.id.btnReply);
        btnReply.setTag("@" + tweet.getUser().getScreenName() + " ");
        btnFavorite = (ImageButton) convertView.findViewById(R.id.btnFav);
        btnFavorite.setTag(tweet);
        client = TwitterApplication.getRestClient();

        btnRetweet = (ImageButton) convertView.findViewById(R.id.btnRetweet);
        btnRetweet.setTag(tweet.getUid());
        tvBody.setTag(tweet);
        // populate data into subviews
        tvUsername.setText("@" + tweet.getUser().getScreenName());
        String formattedTime = TimeFormatter.getTimeDifference(tweet.getCreatedAt());
        tvElapsed.setText(formattedTime);
        tvBody.setText(tweet.getBody());
        tvRealName.setText(tweet.getUser().getName());
        ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);
        ivProfileImage.setTag(tweet.getUser().getScreenName());
        ivProfileImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra("screen_name", (String) v.getTag());
                getContext().startActivity(intent);
            }
        });
        btnReply.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(), ComposeActivity.class);
                intent.putExtra("reply_to", (String) v.getTag());
                getContext().startActivity(intent);
            }
        });
        tvBody.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(), TweetDetailActivity.class);
                intent.putExtra("tweet", (Serializable) v.getTag());
                ((Activity) getContext()).startActivity(intent);
            }
        });
        if(tweet.isFavorited()) {
            btnFavorite.setColorFilter(Color.parseColor("#E81C4F"));
        }
        else {
            btnFavorite.setColorFilter(Color.parseColor("#AAB8C2"));
        }
        btnFavorite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v) {
                final Tweet t = (Tweet) v.getTag();
                if (t.isFavorited()) {
                    client.unFavorite((long) (t.getUid()), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            ((ImageButton) v).setColorFilter(Color.parseColor("#AAB8C2"));
                            t.setFavorited(false);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Log.d("DEBUG", responseString);
                        }

                    });

                } else {
                    client.postFavorite((long) (t.getUid()), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            ((ImageButton) v).setColorFilter(Color.parseColor("#E81C4F"));
                            t.setFavorited(true);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Log.d("DEBUG", responseString);
                        }

                    });
                }
            }
        });
        if (tweet.isRetweeted()) {
            Log.d("DEBUG", "this is retweeted!!!");
            btnRetweet.setColorFilter(Color.parseColor("#19CF86"));
        }
        else {
            btnRetweet.setColorFilter(Color.parseColor("#AAB8C2"));
        }
        btnRetweet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v) {
                client.postRetweet((long) v.getTag(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("DEBUG", "rt");
                        ((ImageButton) v).setColorFilter(Color.parseColor("#19CF86"));
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.d("DEBUG", responseString);
                    }

                });
            }
        });


        // return view to be inserted into the list
        return convertView;
    }


}
