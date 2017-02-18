package com.example.android.popularmovies.reviews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.trailers.TrailerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kolev on 16-Feb-17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{

    private ArrayList<Review> review;
    private TrailerAdapter.OnItemClickListener listener;

    public ReviewAdapter (ArrayList<Review> review) {
        this.review = review;
    }

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(TrailerAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        boolean shouldAttachToParentImmediately = false;
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_review_item, viewGroup,
                shouldAttachToParentImmediately);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        viewHolder.author.setText(review.get(position).getAuthor());
        viewHolder.content.setText(review.get(position).getContent());
        viewHolder.review_url.setText(review.get(position).getUrl());
    }

    @Override
    public int getItemCount() {
        return review.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.author) TextView author;
        @BindView(R.id.content) TextView content;
        @BindView(R.id.review_url) TextView review_url;
        @BindView(R.id.full_review_url) TextView full_review_url;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            // Setting an OnClickListener on the poster image
            full_review_url.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }
    }
}
