package com.kejar.iman.filmpopuler.model;

import io.realm.RealmObject;

/**
 * Created by iman on 03/12/2016.
 */

public class GenreIds extends RealmObject {
    public GenreIds() {
    }

    public GenreIds(int GenreIds) {
        this.GenreIds = GenreIds;
    }
    public int getGenreIds() {
        return GenreIds;
    }

    public void setGenreIds(int genreIds) {
        GenreIds = genreIds;
    }

    private int GenreIds;
}
