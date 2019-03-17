package com.isuru.mymovies.adaptor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.isuru.mymovies.R;
import com.isuru.mymovies.model.Movie;
import com.isuru.mymovies.screens.MovieDetailActivity;

import java.util.List;

/**
 * Created by Isuru Senanayake on 16/03/2019.
 *
 * -- Using the Adaptor to create the card layout of the main landing screen
 */

public class MovieCardAdaptor extends RecyclerView.Adapter<MovieCardAdaptor.MyViewHolder> {

    private Context mContext;
    private List<Movie> movieList;

    public MovieCardAdaptor(Context mContext, List<Movie> movieList){
        this.mContext = mContext;
        this.movieList = movieList;
    }

    @Override
    public MovieCardAdaptor.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_layout, viewGroup, false);   // Inflating the card_layut.xml

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieCardAdaptor.MyViewHolder viewHolder, int i){
        viewHolder.txtTitle.setText(movieList.get(i).getTitle());
        viewHolder.txtRating.setText(movieList.get(i).getRating());
        String poster = mContext.getResources().getString(R.string.image_url)  + movieList.get(i).getThumbImgUrl(); //Taking the image URL from String resources

        // loading image using https://github.com/bumptech/glide library
        Glide.with(mContext)
                .load(poster)
                .placeholder(R.drawable.loading)
                .into(viewHolder.imgThumbnail);

    }

    @Override
    public int getItemCount(){
        return movieList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView txtTitle, txtRating;
        public ImageView imgThumbnail;

        public MyViewHolder(View view){
            super(view);
            txtTitle = view.findViewById(R.id.txtTitle);
            imgThumbnail = view.findViewById(R.id.imgThumbnail);
            txtRating = view.findViewById(R.id.txtRating);

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){   //Checking empty click
                        Movie clickedDataItem = movieList.get(pos);
                        Intent intent = new Intent(mContext, MovieDetailActivity.class);
                        intent.putExtra("movie", clickedDataItem);  //Adding movie object to pass MovieDetailActivity
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }
}
