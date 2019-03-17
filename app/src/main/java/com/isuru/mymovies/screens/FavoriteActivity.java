package com.isuru.mymovies.screens;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.isuru.mymovies.R;
import com.isuru.mymovies.adaptor.MovieCardAdaptor;
import com.isuru.mymovies.data.DBAdapter;
import com.isuru.mymovies.model.Movie;

import java.util.List;

/**
 * Created by Isuru Senanayake on 17/03/2019.
 *
 * -- Activity which handles all the functions of favorite list
 * -- Select, Insert, Delete needs to be handle by the database too
 */

public class FavoriteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieCardAdaptor adapter;
    private TextView txtEmptyMsg;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        recyclerView = findViewById(R.id.recycler_view);
        txtEmptyMsg = findViewById(R.id.txtEmptyMsg);

        // Creating the Database adapter to make the connection to the database
        DBAdapter dbAdapter = new DBAdapter(FavoriteActivity.this);
        dbAdapter.open();


        /**
         * Fetching the data from the database and initializing card view through the MovieCardAdapter.
         */
        List<Movie> movies =  dbAdapter.getAllFavoriteMovies();

        // Show empty message
        if(movies.size() != 0){
            txtEmptyMsg.setVisibility(View.GONE);
        }

        adapter = new MovieCardAdaptor(FavoriteActivity.this, movies);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(FavoriteActivity.this, 2));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged(); // Make visible the changes


    }

}
