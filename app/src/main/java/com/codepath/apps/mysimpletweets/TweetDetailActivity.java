package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TweetDetailActivity extends AppCompatActivity {
    TextView tvUsername;
    Tweet tweet;
    User user;
    ImageView ivProfileImage;
    TextView tvName;
    TextView tvBody;
    TwitterClient client;
    ImageButton btnRetweet;
    ImageButton btnFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        client = TwitterApplication.getRestClient();

        tweet = (Tweet) getIntent().getSerializableExtra("tweet");
        user = tweet.getUser();
        getSupportActionBar().setTitle("@" + user.getScreenName() + "'s Tweet");
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvName = (TextView) findViewById(R.id.tvName);
        tvBody = (TextView) findViewById(R.id.tvBody);
        btnRetweet = (ImageButton) findViewById(R.id.btnRetweet);
        btnRetweet.setTag(tweet.getUid());

        ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(this).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);
        tvUsername.setText("@" + user.getScreenName());
        tvName.setText(user.getName());
        tvBody.setText(tweet.getBody());

        ImageButton btnReply = (ImageButton) findViewById(R.id.btnReply);
        btnReply.setTag("@" + tweet.getUser().getScreenName() + " ");
        btnReply.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(TweetDetailActivity.this, ComposeActivity.class);
                intent.putExtra("reply_to", (String) v.getTag());
                startActivity(intent);
            }
        });

        if (tweet.isRetweeted()) {
            Log.d("DEBUG", "this is retweeted!!!");
            btnRetweet.setColorFilter(Color.parseColor("#19CF86"));
        }
        else {
            btnRetweet.setColorFilter(Color.parseColor("#AAB8C2"));
        }
        btnFavorite = (ImageButton) findViewById(R.id.btnFavorite);
        btnFavorite.setTag(tweet);
        if(tweet.isFavorited()) {
            Log.d("DEBUG", "this is favorited!!!");
            btnFavorite.setColorFilter(Color.parseColor("#E81C4F"));
        }
        else {
            btnFavorite.setColorFilter(Color.parseColor("#AAB8C2"));
        }
        btnFavorite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final Tweet tweet = (Tweet) v.getTag();
                if (tweet.isFavorited()) {
                    client.unFavorite((long) (tweet.getUid()), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d("DEBUG", "unfavorited");
                            btnFavorite.setColorFilter(Color.parseColor("#AAB8C2"));
                            tweet.setFavorited(false);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Log.d("DEBUG", responseString);
                        }

                    });

                } else {
                    client.postFavorite((long) (tweet.getUid()), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d("DEBUG", "favorited");
                            btnFavorite.setColorFilter(Color.parseColor("#E81C4F"));
                            tweet.setFavorited(true);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Log.d("DEBUG", responseString);
                        }

                    });
                }
            }
        });

        btnRetweet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                    client.postRetweet((long) v.getTag(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d("DEBUG", "rt");
                            btnRetweet.setColorFilter(Color.parseColor("#19CF86"));
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Log.d("DEBUG", responseString);
                        }

                    });
                }
        });
    }

}
