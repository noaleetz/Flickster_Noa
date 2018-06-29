package com.noaleetz.flickster;


import org.json.JSONException;
import org.json.JSONObject;

public class Movie {
    private String title;
    private String overview;
    private String posterPath; //only path

    //initialize Json data
    public Movie(JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath  = object.getString("poster_path");
        //list of currently playing movies

    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }
}
