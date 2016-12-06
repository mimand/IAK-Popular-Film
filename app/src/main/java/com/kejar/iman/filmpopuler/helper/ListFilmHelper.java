package com.kejar.iman.filmpopuler.helper;

import android.content.Context;
import android.util.Log;

import com.kejar.iman.filmpopuler.model.Favorit;
import com.kejar.iman.filmpopuler.model.Film;
import com.kejar.iman.filmpopuler.model.ListFilm;
import com.kejar.iman.filmpopuler.model.Result;


import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;


/**
 * Created by iman on 03/12/2016.
 */

public class ListFilmHelper {

    private Realm realm;
    private ListFilm realmResult;
    RealmConfiguration realmConfiguration;
    private RealmResults<Favorit> favoritRealmResults;
    private Film film;
    private Result result;
    private Favorit favorit;

    public ListFilmHelper(Context mContext) {
        Realm.init(mContext);
        realmConfiguration = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        realm = Realm.getInstance(realmConfiguration);
    }


    public void addListFilm(ListFilm listFilm, String pilihMenu) {
        listFilm.setPilihMenu(pilihMenu);
        realm.beginTransaction();
        realm.insertOrUpdate(listFilm);
        realm.commitTransaction();

    }

    public ListFilm findAllRate(String pilihMenu) {
        realmResult = realm.where(ListFilm.class).equalTo("pilihMenu", pilihMenu).findFirst();
        return realmResult;
    }

    public void addFilm(Film film) {
        realm.beginTransaction();
        realm.insertOrUpdate(film);
        realm.commitTransaction();
    }

    public Film findFilm(int id) {
        film = realm.where(Film.class).equalTo("id", id).findFirst();
        return film;
    }
    public Result findResult(int id) {
        result = realm.where(Result.class).equalTo("id", id).findFirst();
        return result;
    }
    public Favorit findFavorit(int id) {
        favorit = realm.where(Favorit.class).equalTo("id", id).findFirst();
        return favorit;
    }

    public void deleteFavorit(int id) {
        RealmObject realmObject = realm.where(Favorit.class).equalTo("id", id).findFirst();
        realm.beginTransaction();
        realmObject.deleteFromRealm();
        realm.commitTransaction();
    }

    public void addFavorit(Favorit favorit) {
        realm.beginTransaction();
        realm.insertOrUpdate(favorit);
        realm.commitTransaction();
    }

    public ArrayList<Favorit> findAllFavorit() {
        ArrayList<Favorit> data = new ArrayList<>();

        favoritRealmResults = realm.where(Favorit.class).findAll();
        favoritRealmResults.sort("id");

        for (int i = 0; i < favoritRealmResults.size(); i++) {
            int id = favoritRealmResults.get(i).getId();
            Favorit favorit = new Favorit();
            favorit.setId(id);
            data.add(favorit);
        }

        return data;
    }
}
