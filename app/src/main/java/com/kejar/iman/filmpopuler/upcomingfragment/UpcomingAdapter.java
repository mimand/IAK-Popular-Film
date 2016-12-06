package com.kejar.iman.filmpopuler.upcomingfragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kejar.iman.filmpopuler.DetailFilmActivity;
import com.kejar.iman.filmpopuler.R;
import com.kejar.iman.filmpopuler.model.ListFilm;
import com.kejar.iman.filmpopuler.model.Result;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Response;

/**
 * Created by iman on 01/12/2016.
 */

public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.ViewHolder>{

    private static ListFilm response;
    private static final String TAG = "PopularAdapter";
    private static List<Result> results;
    Context mContext;


    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        ImageView imageView =(ImageView) itemView.findViewById(R.id.upcoming_gambar);
        TextView textViewJudul =(TextView) itemView.findViewById(R.id.upcoming_judul);
        TextView textViewDeskripsi =(TextView) itemView.findViewById(R.id.upcoming_deskripsi);
        TextView textViewTanggal =(TextView) itemView.findViewById(R.id.upcoming_release);

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    results = response.getResults();
                    Intent intent = new Intent(v.getContext(), DetailFilmActivity.class);
                    intent.putExtra("getfilm", results.get(getAdapterPosition()).getId());
                    v.getContext().startActivity(intent);
                }
            });
        }


        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick" + getPosition());
        }
    }
    // END_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param response String[] containing the data to populate views to be used by RecyclerView.
     */
    public UpcomingAdapter(ListFilm response) {
        this.response =response;

    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_film_upcoming, viewGroup, false);
        mContext = viewGroup.getContext();

        return new ViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        results = response.getResults();

        viewHolder.textViewJudul.setText(results.get(position).getOriginalTitle());
        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w300/"+results.get(position).getPosterPath())
                .fit().centerCrop().into(viewHolder.imageView);
        viewHolder.textViewDeskripsi.setText(results.get(position).getOverview());
        viewHolder.textViewTanggal.setText(results.get(position).getReleaseDate());
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return response.getResults().size();
    }

}
