package com.kejar.iman.filmpopuler;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kejar.iman.filmpopuler.controller.ListFilmApiService;
import com.kejar.iman.filmpopuler.helper.ListFilmHelper;
import com.kejar.iman.filmpopuler.model.Favorit;
import com.kejar.iman.filmpopuler.model.Film;
import com.kejar.iman.filmpopuler.model.ListFilm;
import com.kejar.iman.filmpopuler.model.Thriller;
import com.kejar.iman.filmpopuler.utility.ApiClient;
import com.squareup.picasso.Picasso;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailFilmActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";
    private final static String API_KEY = "fc5bc0346df134d06ba7f8948134ed36";
    ImageView imageView;
    ImageView imagePoster;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    LinearLayout mainLayout;
    Response<Thriller> responseThriller;
    TextView film, release, duration, rating, overview, tagline;
    Button btnWeb;
    Film objectFilm;
    ListFilmHelper helper;
    private int filmId;
    Menu menu;

    Favorit favorit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_film);

        filmId = getIntent().getIntExtra("getfilm", 1);

        helper = new ListFilmHelper(getBaseContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        imageView = (ImageView) findViewById(R.id.header_film);
        mainLayout = (LinearLayout) findViewById(R.id.main);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        film = (TextView) findViewById(R.id.detail_title_film);
        release = (TextView) findViewById(R.id.detail_release_film);
        duration = (TextView) findViewById(R.id.detail_duration_film);
        rating = (TextView) findViewById(R.id.detail_rating_film);
        imagePoster = (ImageView) findViewById(R.id.detail_poster_film);
        overview = (TextView) findViewById(R.id.detail_overview_film);
        tagline = (TextView) findViewById(R.id.detail_tagline_film);
        btnWeb = (Button) findViewById(R.id.btn_web_film);

        mCollapsingToolbarLayout.setTitleEnabled(false);
        toolbar.setTitle("Detail Movie");

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Retrofit Thriller Film
        ListFilmApiService apiServiceThriller =
                ApiClient.getClient().create(ListFilmApiService.class);
        Intent intent = getIntent();
        Call<Thriller> callThriller = apiServiceThriller.getMovieThriller(intent.getIntExtra("getfilm", 1), API_KEY);
        callThriller.enqueue(new Callback<Thriller>() {
            @Override
            public void onResponse(Call<Thriller> call, Response<Thriller> response) {
                Log.d(TAG, response.body().getResults().size() + "");
                responseThriller = response;

            }

            @Override
            public void onFailure(Call<Thriller> call, Throwable t) {

            }
        });
        Film film = helper.findFilm(filmId);
        Log.d(TAG, filmId + "Film id");
        if (film == null) {
            loadDetailFilm();
        } else {
            if (!testConnection()) {
                Log.d(TAG,"koneksi tidak berhasil");
                objectFilm = film;
                viewFilm();
            } else if (testConnection()){
                Log.d(TAG,"koneksi berhasil");
                loadDetailFilm();
            }
        }
    }

    public void btnThriller(View v) {
        if (responseThriller.body().getResults().size() >= 1) {
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"
                    + responseThriller.body().getResults().get(0).getKey()));
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + responseThriller.body().getResults().get(0).getKey()));
            try {
                startActivity(appIntent);
            } catch (ActivityNotFoundException ex) {
                startActivity(webIntent);
            }
        } else {
            Toast.makeText(getBaseContext(), "Not Found Official Thriller", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (id == R.id.menu_shate){
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Tonton "+objectFilm.getOriginalTitle()+" seru banget lo";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, objectFilm.getOriginalTitle());
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
        if(id == R.id.menu_favorite){
            favorit =helper.findFavorit(filmId);
            if(favorit == null) {
                Favorit favorit1 = new Favorit();
                favorit1.setId(filmId);
                menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_favorite_white_24dp));
                helper.addFavorit(favorit1);
            }else if(favorit != null){
                menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_favorite_border_white_24dp));

                helper.deleteFavorit(filmId);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        this.menu = menu;
        favorit =helper.findFavorit(filmId);
        if(favorit != null) {
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_favorite_white_24dp));
        }else if(favorit == null){
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_favorite_border_white_24dp));
        }
        return true;
    }

    public void loadDetailFilm() {
        ListFilmApiService apiService =
                ApiClient.getClient().create(ListFilmApiService.class);
        Call<Film> call = apiService.getMovieDetails(filmId, API_KEY);
        call.enqueue(new Callback<Film>() {
            @Override
            public void onResponse(Call<Film> call, final Response<Film> response) {
                objectFilm = response.body();
                viewFilm();
                helper.addFilm(response.body());
            }

            @Override
            public void onFailure(Call<Film> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });

    }

    public boolean testConnection() {
        ConnectivityManager connec =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            // if connected with internet

            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

            Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }


    public void viewFilm() {
        String BackDropsMovies = objectFilm.getBackdropPath();
        Picasso.with(getBaseContext()).load("http://image.tmdb.org/t/p/w1000/" + BackDropsMovies)
                .resize(1000, 563).into(imageView);
        String posterMovie = objectFilm.getPosterPath();
        Picasso.with(getBaseContext()).load("http://image.tmdb.org/t/p/w185/" + posterMovie)
                .resize(240, 360).into(imagePoster);

        int hours = objectFilm.getRuntime() / 60; //since both are ints, you get an int
        int minutes = objectFilm.getRuntime() % 60;
        duration.setText(hours + " : " + minutes);

        film.setText(objectFilm.getOriginalTitle());
        release.setText(objectFilm.getReleaseDate());
        rating.setText(objectFilm.getVoteAverage() + "");

        tagline.setText("\"" + objectFilm.getTagline() + "\"");
        overview.setText("" + objectFilm.getOverview());
        btnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (objectFilm.getHomepage().length() >= 1) {
                    Intent i = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(objectFilm.getHomepage()));
                    startActivity(i);
                } else {
                    Toast.makeText(getBaseContext(), "Web Page Not Found", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}