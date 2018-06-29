package com.noaleetz.flickster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {

    String imageBaseUrl;
    //poster size
    String posterSize;

    public Config(JSONObject object) throws JSONException {
        JSONObject images = object.getJSONObject("images");
        imageBaseUrl = images.getString("secure_base_url");
        //get poster size
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        //use option at index 3 as default if none entered
        posterSize = posterSizeOptions.optString(3, "w342");


    }

    //helper method for creating url
    public String getImageUrl(String size, String path){

        return String.format("%s%s%s", imageBaseUrl,size,path);
    }

    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public String getPosterSize() {
        return posterSize;
    }
}
