package com.example.moviedicoding.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.moviedicoding.GlobalVars;
import com.example.moviedicoding.Model.Movie;
import com.example.moviedicoding.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {
    private final static String TAG = DetailActivity.class.getSimpleName();
    private TextView tvTitleMovie, tvPopularityMovie, tvReleaseMovie, tvOverviewMovie;
    private RatingBar ratingBar;
    public final static String EXTRA_DATA = "extra_data";
    private YouTubePlayerSupportFragment frag;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        tvTitleMovie = findViewById(R.id.tvTitleMovie);
        tvPopularityMovie = findViewById(R.id.tvPopularityMovie);
        tvReleaseMovie = findViewById(R.id.tvReleaseMovie);
        tvOverviewMovie = findViewById(R.id.tvOverviewMovie);
        ratingBar = findViewById(R.id.ratingbar);
        toolbar = findViewById(R.id.toolbar);

        Movie item = getIntent().getParcelableExtra(EXTRA_DATA);
        tvTitleMovie.setText(item.getTitle());
        tvPopularityMovie.setText(String.valueOf(item.getPopularity()));
        tvReleaseMovie.setText(item.getReleaseDate());
        tvOverviewMovie.setText(item.getOverview());
        ratingBar.setRating(Float.valueOf(String.valueOf(item.getVoteAverage())) / 2);
        frag = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);


        initToolbar();
        AndroidNetworking.get(GlobalVars.URL + item.getId() + "/" + GlobalVars.Video + GlobalVars.LAST_URL)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray listResult = response.getJSONArray("results");
                            final JSONObject object = listResult.getJSONObject(0);
                            frag.initialize("AIzaSyAC_1Ek1mB8k5rOdBVhmBBXSgjySQRIF3M", new YouTubePlayer.OnInitializedListener() {
                                @Override
                                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                                    if (!b) {
                                        try {
                                            youTubePlayer.loadVideo(object.getString("key"));
                                            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                                            youTubePlayer.pause();
                                        } catch (JSONException e) {
                                            Log.e(TAG, "Error: " + e);
                                            Toast.makeText(DetailActivity.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                                    Log.e(TAG, "ANError: " + youTubeInitializationResult.toString());
                                }
                            });
                        } catch (JSONException e) {
                            Log.e(TAG, "Error: " + e);
                            Toast.makeText(DetailActivity.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "ANError: " + anError);
                        Toast.makeText(DetailActivity.this, "AnError: " + anError, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private  void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.app_name));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        intentToDetail();
    }

    private void intentToDetail(){
        Intent intent = new Intent(DetailActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
