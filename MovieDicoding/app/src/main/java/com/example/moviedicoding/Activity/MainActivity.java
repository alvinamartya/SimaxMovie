package com.example.moviedicoding.Activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.moviedicoding.GlobalVars;
import com.example.moviedicoding.Model.Movie;
import com.example.moviedicoding.Adapter.MovieAdapter;
import com.example.moviedicoding.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;


public class MainActivity extends AppCompatActivity {
    // general variable declaration
    private final static String TAG = MainActivity.class.getSimpleName();
    private List<Movie> movieList;
    private MovieAdapter adapter;
    RecyclerView rvMovie;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private TextView tvError;
    private Button btnRetry;
    private LinearLayout llError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // bind component to code
        rvMovie = findViewById(R.id.rvMovie);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        progressBar = findViewById(R.id.progressbar);
        tvError = findViewById(R.id.tvError);
        btnRetry = findViewById(R.id.btnRetry);
        llError = findViewById(R.id.llError);

        movieList = new ArrayList<>();
        adapter = new MovieAdapter(movieList, this);
        rvMovie.setAdapter(adapter);

        refreshData();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshData();
            }
        });
    }

    private void refreshData(){
        hideErrorView();
        swipeRefreshLayout.setRefreshing(false);
        if(movieList != null) movieList.clear();
        AndroidNetworking.get(GlobalVars.URL + GlobalVars.NOW_PLAYING + GlobalVars.LAST_URL)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray listResult = response.getJSONArray("results");
                            for (int i = 0; i < listResult.length(); i++) {
                                JSONObject object = listResult.getJSONObject(i);
                                movieList.add(
                                        new Movie(
                                                object.getInt("id"),
                                                object.getString("original_title"),
                                                object.getDouble("popularity"),
                                                object.getString("overview"),
                                                object.getString("release_date"),
                                                object.getDouble("vote_average"),
                                                object.getString("poster_path")
                                        ));
                            }

                            if(movieList.isEmpty()){
                                rvMovie.setVisibility(View.GONE);
                                llError.setVisibility(View.VISIBLE);
                                tvError.setText(getString(R.string.error_msg_not_have_data));
                            } else {
                                adapter.setData(movieList);
                            }
                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            Log.e(TAG, "Error: " + e);
                            showErrorView(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "ANError: " + anError);
                        showErrorView(anError);
                    }
                });
    }

    private void hideErrorView() {
        if (llError.getVisibility() == View.VISIBLE) {
            llError.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void showErrorView(Throwable throwable) {
        if (llError.getVisibility() == View.GONE) {
            llError.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            tvError.setText(fetchErrorMessage(throwable));
        }
    }

    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = "";
        if (!isNetworkConnected()) {
            errorMsg = getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.error_msg_timeout);
        } else
            errorMsg = throwable.getMessage();

        return errorMsg;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
