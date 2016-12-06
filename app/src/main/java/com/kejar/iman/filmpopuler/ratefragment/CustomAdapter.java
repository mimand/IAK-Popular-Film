package com.kejar.iman.filmpopuler.ratefragment;


/**
 * Created by iman on 5/1/2016.
 */
/*
* Copyright (C) 2014 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

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
 * Provide views to RecyclerView with data from mDataSet.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private static ListFilm response;
    private static final String TAG = "RateAdapter";
    private static List<Result> results;
    private Context mContext;


    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
     static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        ImageView imageView =(ImageView) itemView.findViewById(R.id.rate_image);
        TextView textView =(TextView) itemView.findViewById(R.id.text_rate);
         ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    results = response.getResults();
                    Intent intent = new Intent(v.getContext(), DetailFilmActivity.class);
                    intent.putExtra("getfilm", results.get(getPosition()).getId());
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
    public CustomAdapter(ListFilm response) {
        this.response =response;
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_film_rate, viewGroup, false);
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
        viewHolder.textView.setText(results.get(position).getOriginalTitle());
        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185/"+results.get(position).getPosterPath())
                .fit().into(viewHolder.imageView);
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return response.getResults().size();
    }

}
