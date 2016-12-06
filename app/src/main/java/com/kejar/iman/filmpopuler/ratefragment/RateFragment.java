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


import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.kejar.iman.filmpopuler.R;
import com.kejar.iman.filmpopuler.helper.ListFilmHelper;
import com.kejar.iman.filmpopuler.utility.ApiClient;
import com.kejar.iman.filmpopuler.model.ListFilm;
import com.kejar.iman.filmpopuler.controller.ListFilmApiService;
import com.kejar.iman.filmpopuler.model.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RateFragment extends Fragment {

    private static final String TAG = "RecyclerRateFragment";
    private final static String API_KEY = "fc5bc0346df134d06ba7f8948134ed36";
    protected RecyclerView mRecyclerView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rate, container, false);

        mRecyclerView =(RecyclerView) rootView.findViewById(R.id.recyclerRate);
        mRecyclerView.setHasFixedSize(true);
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
        else{
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        }
        ListFilmHelper helper = new ListFilmHelper(getContext());
        ListFilm listFilm = helper.findAllRate("rate");
        Bundle b = getArguments();
        boolean checkNetwork  =b.getBoolean("getConnection");
        if(listFilm == null){
            loadData();
        }else{
            if(!checkNetwork) {
                mRecyclerView.setAdapter(new CustomAdapter(helper.findAllRate("rate")));

            }else if (checkNetwork) {
                loadData();


            }
        }
        return rootView;
    }
    public void loadData(){
        ListFilmApiService apiService =
                ApiClient.getClient().create(ListFilmApiService.class);

        Call<ListFilm> call = apiService.getTopRatedMovies(API_KEY);
        call.enqueue(new Callback<ListFilm>() {
            @Override
            public void onResponse(Call<ListFilm> call, Response<ListFilm> response) {
                List<Result> movies = response.body().getResults();
                ListFilmHelper helper = new ListFilmHelper(getContext());
                helper.addListFilm(response.body(),"rate");
                mRecyclerView.setAdapter(new CustomAdapter(response.body()));
                Log.d(TAG, "Number of movies received: " + movies.size());
            }

            @Override
            public void onFailure(Call<ListFilm> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }
}
