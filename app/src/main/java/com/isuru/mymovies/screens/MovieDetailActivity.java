package com.isuru.mymovies.screens;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.isuru.mymovies.R;
import com.isuru.mymovies.data.DBAdapter;
import com.isuru.mymovies.model.Movie;

/**
 * Created by Isuru Senanayake on 16/03/2019.
 * -- This class show details of the movie
 * -- Fetching data from previous Intent through Serialized object
 */

public class MovieDetailActivity extends AppCompatActivity {

    Movie movie;
    ImageView imgFav;
    TextView txtMovieTitle, txtDesc, txtRating, txtRelDate, txtLanguage;
    private boolean isAdded = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        txtMovieTitle = findViewById(R.id.txtMovieTitle);
        txtDesc = findViewById(R.id.txtDesc);
        txtRating = findViewById(R.id.txtRating);
        txtRelDate = findViewById(R.id.txtRelDate);
        txtLanguage = findViewById(R.id.txtLanguage);
        imgFav = findViewById(R.id.imgStar);

        final ImageView imgMovieImg = findViewById(R.id.imgMovieImage);

        /**
         * Processing the Serialized movie object.
         */
        Intent intent = getIntent();
        if (intent.hasExtra("movie")) {
            movie = getIntent().getParcelableExtra("movie");
            txtMovieTitle.setText(movie.getTitle());
            txtDesc.setText(movie.getDescription());
            txtRating.setText(movie.getRating());
            txtLanguage.setText(movie.getLanguage());
            txtRelDate.setText(movie.getRelDate());
            String thumbnail = movie.getThumbImgUrl();

            String poster = getResources().getString(R.string.image_url) + thumbnail;

            // loading image using https://github.com/bumptech/glide library
            Glide.with(this)
                    .load(poster)
                    .placeholder(R.drawable.loading)
                    .into(imgMovieImg);
        }

        /**
         * Fetching favorite data from databse to change the favorite icon
         */
        DBAdapter dbAdapter = new DBAdapter(MovieDetailActivity.this);
        dbAdapter.open();
        Movie addedMovie = dbAdapter.getFavoriteMovieId(movie.getId());

        if (addedMovie.getId() != null || isAdded) {
            imgFav.setImageResource(R.drawable.ic_star);    // Chaning the icon
            isAdded = true;
        }

        imgFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAdded) {
                    /**
                     * Confirmation dialog box
                     */
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE: {
                                    DBAdapter dbAdapter = new DBAdapter(MovieDetailActivity.this);
                                    dbAdapter.open();
                                    dbAdapter.deleteFavorite(movie.getId());
                                    imgFav.setImageResource(R.drawable.ic_star_border);
                                    isAdded = false;
                                    break;
                                }
                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(MovieDetailActivity.this);
                    builder.setMessage("\nAre you sure you want to remove from favorite?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

                } else {
                    // If the movie item not in the database it will be added
                    DBAdapter dbAdapter = new DBAdapter(MovieDetailActivity.this);
                    dbAdapter.open();
                    dbAdapter.addFavorite(movie);
                    Toast.makeText(getApplicationContext(), "Added to favorite", Toast.LENGTH_LONG).show();
                    imgFav.setImageResource(R.drawable.ic_star);
                    isAdded = true;
                }

            }
        });


    }
}
