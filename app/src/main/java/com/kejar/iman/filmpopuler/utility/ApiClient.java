package com.kejar.iman.filmpopuler.utility;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.kejar.iman.filmpopuler.model.GenreIds;

import java.io.IOException;

import io.realm.RealmList;
import io.realm.RealmObject;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class ApiClient {

    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .registerTypeAdapter(new TypeToken<RealmList<GenreIds>>() {}.getType(), new TypeAdapter<RealmList<GenreIds>>() {

                    @Override
                    public void write(JsonWriter out, RealmList<GenreIds> value) throws IOException {
                        // Ignore
                    }

                    @Override
                    public RealmList<GenreIds> read(JsonReader in) throws IOException {
                        RealmList<GenreIds> list = new RealmList<GenreIds>();
                        in.beginArray();
                        while (in.hasNext()) {
                            list.add(new GenreIds(in.nextInt()));
                        }
                        in.endArray();
                        return list;
                    }
                }).create();
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

}