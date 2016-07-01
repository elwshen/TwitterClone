package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {
    TwitterClient client;
    EditText etTweet;
    Button btnTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        String replyTo = getIntent().getStringExtra("reply_to");

        client = TwitterApplication.getRestClient();

        etTweet = (EditText) findViewById(R.id.etTweet);
        btnTweet = (Button) findViewById(R.id.btnTweet);

        populateComposeWindow(replyTo);
    }

    public void populateComposeWindow(String replyTo) {
        if (replyTo != null && !replyTo.isEmpty()) {
            String newText = etTweet.getText().toString() + replyTo;
            etTweet.setText("");
            etTweet.append(newText);
        }
    }
    public void postTweet(View view) {
        String tweetText = etTweet.getText().toString();
        client.postTweet(tweetText, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Tweet newTweet = Tweet.fromJSON(response);
                Intent intent = new Intent();
                intent.putExtra("tweet", newTweet);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("DEBUG",responseString);
            }
        });
    }
}
