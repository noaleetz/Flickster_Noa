package com.noaleetz.flickster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieListActivity extends AppCompatActivity {

    //constants
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";

    // name for API key
    public final static String API_KEY_PARAM = "api_key";

     //tag for logging activity
    public final static String TAG = "MovieListActivity";
    AsyncHttpClient client;

    //url for loading images

    ArrayList<Movie> movies;
    //recycler view
    RecyclerView rvMovies;
    //adapter wired to recyler view
    MovieAdapter adapter;
    //image config
    Config config;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        client = new AsyncHttpClient();
        //initialize list of movies
        movies = new ArrayList<>();

        //init the adapter
        adapter = new MovieAdapter(movies);

        //resolve recycler view
        rvMovies = (RecyclerView) findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);



        getConfiguration();

    }
    //get list of movies playing
    private void getNowPlaying() {
        String url = API_BASE_URL + "/movie/now_playing";
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        client.get(url,params, new JsonHttpResponseHandler() {


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //load results
                try {
                    JSONArray results = response.getJSONArray("results");

                    //iterate through result
                    for (int i = 0; i <results.length();i++) {
                        Movie movie = new Movie(results.getJSONObject(i));
                        movies.add(movie);
                        //notify adapter of change
                        adapter.notifyItemInserted(movies.size() - 1);


                    }
                    Log.i(TAG,String.format("loaded %s movies ",results.length()));


                } catch (JSONException e) {
                    logError("failed to parse playing movies",e, true);
                }
            }


        });

    }

    //get stuff from API
    private void getConfiguration() {
        String url = API_BASE_URL + "/configuration";

        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        //get
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    config = new Config(response);
                     Log.i(TAG,
                             String.format("loaded configuration with imageBaseUrl %s and posterSize %s",
                             config.getImageBaseUrl(),
                             config.getPosterSize()));


                    adapter.setConfig(config);

                    getNowPlaying();


                }
                catch (JSONException e) {
                    logError("Failed to parse configuration",e,true);

                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("failed to get configuration", throwable,true );
            }

        });

    }

    //handle error
    private void logError(String message, Throwable error, boolean alertUser) {
        //log
        Log.e(TAG,message,error);
        //alert user
        if (alertUser){
            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

        }
    }


}
