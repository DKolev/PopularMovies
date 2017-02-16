package com.example.android.popularmovies.trailers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kolev on 28-Dec-16.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    // Defining some variables
    private Context context;
    private ArrayList<Trailer> trailer;
    private OnItemClickListener listener;

    /**
     * Creates a Movie Adapter
     */
    public TrailerAdapter(ArrayList<Trailer> trailer) {
        this.trailer = trailer;
    }

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * This gets called when each new ViewHolder is created.
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType
     * @return A new MovieAdapterViewHolder that holds the Views for each grid item
     */
    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        boolean shouldAttachToParentImmediately = false;
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_trailer_item, viewGroup,
                shouldAttachToParentImmediately);
        return new ViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position.
     * @param viewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(final TrailerAdapter.ViewHolder viewHolder, int position) {

        viewHolder.trailer_url.setText(trailer.get(position).getKey());
        viewHolder.trailer_name.setText(trailer.get(position).getName());

    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     * @return The number of items available.
     */
    @Override
    public int getItemCount() {
        return trailer.size();
    }

    /**
     * Cache of the children views for a movie grid item.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.trailer_name) TextView trailer_name;
        @BindView(R.id.trailer_url) TextView trailer_url;


        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            // Setting an OnClickListener on the poster image
            trailer_name.setOnClickListener(new View.OnClickListener() {
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
