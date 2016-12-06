package com.kejar.iman.filmpopuler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kejar.iman.filmpopuler.helper.ListFilmHelper;
import com.kejar.iman.filmpopuler.model.Favorit;
import com.kejar.iman.filmpopuler.utility.FavoriteAdapter;

import java.util.List;


public class FavoriteFragment extends Fragment {
    protected RecyclerView mRecyclerView;
    ListFilmHelper helper;
    TextView checkFavorit;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
        helper = new ListFilmHelper(getContext());
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerFavorite);
        checkFavorit = (TextView) rootView.findViewById(R.id.cek_favorit);

        List<Favorit> favorit = helper.findAllFavorit();
        if(favorit.size() ==0 ) {
            mRecyclerView.setVisibility(View.GONE);
            checkFavorit.setVisibility(View.VISIBLE);
        }else {
            checkFavorit.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.setAdapter(new FavoriteAdapter(favorit));
        }
        return rootView;

    }
}
