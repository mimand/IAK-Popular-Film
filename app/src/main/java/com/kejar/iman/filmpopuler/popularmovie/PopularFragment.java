package com.kejar.iman.filmpopuler.popularmovie;

import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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


public class PopularFragment extends Fragment {

    private static final String TAG = "PopularFragment";
    private final static String API_KEY = "fc5bc0346df134d06ba7f8948134ed36";
    protected RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_popular, container, false);
        rootView.setTag(TAG);


        if (API_KEY.isEmpty()) {
            Toast.makeText(getContext(), "Please obtain your API KEY first from themoviedb.org", Toast.LENGTH_LONG).show();

        }
        mRecyclerView =(RecyclerView) rootView.findViewById(R.id.recyclerViewPopular);
        mRecyclerView.setHasFixedSize(true);
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
        else{
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        }
        ListFilmHelper helper = new ListFilmHelper(getContext());
        ListFilm listFilm = helper.findAllRate("populer");
        Bundle b = getArguments();
        boolean checkNetwork  =b.getBoolean("getConnection");
        if(listFilm == null){
            loadData();
        }else{
            if(!checkNetwork) {
                Log.d(TAG,"koneksi tidak berhasil");
                mRecyclerView.setAdapter(new PopularAdapter(helper.findAllRate("populer")));

            }else if (checkNetwork) {
                Log.d(TAG,"koneksi berhasil");
                loadData();

            }
        }

        return rootView;
    }
    public void loadData(){
        ListFilmApiService apiService =
                ApiClient.getClient().create(ListFilmApiService.class);

        Call<ListFilm> call = apiService.getPopularMovies(API_KEY);
        call.enqueue(new Callback<ListFilm>() {
            @Override
            public void onResponse(Call<ListFilm> call, Response<ListFilm> response) {
                List<Result> movies = response.body().getResults();
                ListFilmHelper helper = new ListFilmHelper(getContext());
                helper.addListFilm(response.body(),"populer");
                mRecyclerView.setAdapter(new PopularAdapter(response.body()));
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
