package com.example.android.popularmovies;

/**
 * Created by Kolev on 11-Feb-17.
 */

public class Trailer {

    private int key;
    private String name;

    public Trailer (int key, String name) {
        this.key = key;
        this.name = name;

    }

    public int getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

}
