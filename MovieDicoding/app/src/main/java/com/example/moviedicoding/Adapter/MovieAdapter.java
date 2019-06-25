package com.example.moviedicoding.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.moviedicoding.Activity.DetailActivity;
import com.example.moviedicoding.Model.Movie;
import com.example.moviedicoding.R;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    // declaration general variable
    private List<Movie> movieList;
    private Context context;

    // constructur for make new movieAdapter
    public MovieAdapter(List<Movie> movieList, Context context) {
        this.movieList = movieList;
        this.context = context;
    }

    // set data and then change data in recyclerview
    public void setData(List<Movie> data) {
        this.movieList = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.view_main_list, viewGroup, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder holder, int i) {
        final Movie item = movieList.get(i);
        holder.tvTitleMovie.setText(item.getTitle());
        holder.tvPopularity.setText(String.valueOf(item.getPopularity()));
        holder.ratingbar.setRating(Float.valueOf(String.valueOf(item.getVoteAverage())) / 2);
        Glide.with(context).load("https://image.tmdb.org/t/p/w500" + item.getPosterPath()).into(holder.img);
        holder.llMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToDetail(item);
            }
        });
    }

    private void intentToDetail(Movie movie){
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_DATA,movie);
        context.startActivity(intent);
        ((Activity)context).finish();
    }

    @Override
    public int getItemCount() {
        if (movieList == null) return 0;
        return movieList.size();
    }

    // create new view holder sub class from RecyclerVuew.ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitleMovie, tvPopularity;
        private RatingBar ratingbar;
        private ImageView img;
        private LinearLayout llMovie;

        public ViewHolder(@NonNull View v) {
            super(v);
            tvTitleMovie = v.findViewById(R.id.tvTitleMovie);
            tvPopularity = v.findViewById(R.id.tvPopularityMovie);
            ratingbar = v.findViewById(R.id.ratingbar);
            img = v.findViewById(R.id.imgMovie);
            llMovie = v.findViewById(R.id.llMovie);
        }
    }
}