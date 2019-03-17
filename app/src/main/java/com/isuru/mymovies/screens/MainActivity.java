package com.isuru.mymovies.screens;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.isuru.mymovies.R;
import com.isuru.mymovies.adaptor.MovieCardAdaptor;
import com.isuru.mymovies.services.ServicesInterface;
import com.isuru.mymovies.components.SortInterface;
import com.isuru.mymovies.components.SortList;
import com.isuru.mymovies.model.Movie;
import com.isuru.mymovies.model.MovieResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Isuru Senanayake on 16/03/2019.
 *
 * -- This class handles the main landing page functions
 * -- Fetching data from https://www.themoviedb.org/documentation/api using https://square.github.io/retrofit/ and show in the Card layout
 */

public class MainActivity extends AppCompatActivity{

    ProgressBar loadProgress;
    private MovieCardAdaptor adapter;
    private RecyclerView recyclerView;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    ActionBar actionBar;
    private ImageView imgConnError;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recycler_view);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.navi_drawer_layout);
        imgConnError = findViewById(R.id.imgNoConnection);

        // Initializing the Action bar
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ico_main_menu);
        actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'> Home </font>"));

        displayResult(2);   // Selecting the default sort option as "Popular"

        // NavigationView click listener
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_home : {
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();    // Closing the left menu drawer
                        return true;
                    }
                    case R.id.nav_fav : {
                        Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();    // Closing the left menu drawer
                        return true;
                    }
                    case R.id.nav_map : {
                        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();    // Closing the left menu drawer
                        return true;
                    }
                    case R.id.nav_email : {
                        Intent intent = new Intent(MainActivity.this, FeedbackActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();    // Closing the left menu drawer
                        return true;
                    }
                }


                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Action bar click event handling.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home : drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.sortButton : {
                String[] sortItemArray = getResources().getStringArray(R.array.sort_array);
                SortList sortList = new SortList(MainActivity.this, sortItemArray);
                sortList.showSortingDialog(new SortInterface() {
                    @Override
                    public void clickedItem(int item) {
                        displayResult(item);
                    }
                });
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * fetching data using https://square.github.io/retrofit/ HTTP client
     */
    private void displayResult(int item){

        Call<MovieResponse> movieList = getURL(item);

        loadProgress = findViewById(R.id.progressBar);
        loadProgress.setVisibility(View.VISIBLE);

        /**
         * Callback function, all the processing should be handled by overriding onResponsen
         */
        movieList.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {

                if(!response.isSuccessful()){
                    loadProgress.setVisibility(View.GONE);
                    return;
                }

                List<Movie> movies = response.body().getResults();

                // Show connection error message
                if(movies.size() > 0)
                    imgConnError.setVisibility(View.GONE);

                adapter = new MovieCardAdaptor(MainActivity.this, movies);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                loadProgress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                loadProgress.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Processing the API call
     */
    private Call<MovieResponse> getURL(int option) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
            ServicesInterface servicesInterface = retrofit.create(ServicesInterface.class);

            // Showing are returning the relevant URL and the Title according to the sort criteria
            switch (option){
                case 0: actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'> Now Playing </font>"));return servicesInterface.getMovieListNowPlaying(getResources().getString(R.string.API_KEY));
                case 1: actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'> Popular </font>"));return servicesInterface.getMovieListPopular(getResources().getString(R.string.API_KEY));
                case 2: actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'> Top Rated </font>"));return servicesInterface.getMovieListTopRated(getResources().getString(R.string.API_KEY));
                case 3: actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'> Up Coming </font>"));return servicesInterface.getMovieListUpComing(getResources().getString(R.string.API_KEY));
                default:actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'> Now Playing </font>"));return servicesInterface.getMovieListNowPlaying(getResources().getString(R.string.API_KEY));
            }
    }

}


