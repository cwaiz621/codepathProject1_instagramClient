package com.example.crystal.instagramclient;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PhotosActivity extends ActionBarActivity {

    public static final String CLIENT_ID = "f2e497e4113044e9913817f2785308a1";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotoAdapter aPhotos;
    private SwipeRefreshLayout swipeContainer;
    private AsyncHttpClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });

        photos = new ArrayList<>();
        //create the adapter linking it to the source
        aPhotos = new InstagramPhotoAdapter(this, photos);
        //find listview from layout
        ListView lvPhotos= (ListView) findViewById(R.id.lvPhotos);
        //set adapter, binding to listview
        lvPhotos.setAdapter(aPhotos);

        //send out API request for popular photos- fetch popular photos
        fetchPopularPhotos();

    }

    public void fetchPopularPhotos(){
        //Trigger API (Network) request
    /*
    Instagram Client ID: f2e497e4113044e9913817f2785308a1
    https://api.instagram.com/v1/tags/nofilter/media/recent?client_id=CLIENT-ID
    Type: {“data” ==> [x] ==> “type”} (“image”or “video”)
    URL: {“data” => [x] => images => standard_resolution =>”url"}
    Caption: {“data” => [x] => “caption” => “text” }
    AuthorName: {“data” => [x] => “user” => “username” }
    Likes: {“data” => [x] => “likes” => “count”}/
     */
        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;

        //create async (network client)
        client = new AsyncHttpClient();

        client.get(url, null, new JsonHttpResponseHandler() {
            //on Success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Log.i("DEBUG", response.toString());
                //Iterate each photo item and decode into a Java object...itereate response through the JSON array
                aPhotos.clear();
                JSONArray photosJSON = null;
                try {
                    photosJSON = response.getJSONArray("data");//array of posts

                    //iterate array of posts
                    for (int i = 0; i < photosJSON.length(); i++) {

                        //get JSONobjet at the index
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        //decode JSON into object data model
                        InstagramPhoto photo = new InstagramPhoto();


                        //AuthorName: {“data” => [x] => “user” => “username” }
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        //Caption: {“data” => [x] => “caption” => “text” }
                        photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        //Type:{“data” ==> [x] ==> “type”} (“image”or “video”)
                        //photo.type = photoJSON.getJSONObject("type");
                        //URL: {“data” => [x] => images => standard_resolution =>”url"}
                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        //imgage height
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        //Likes: {“data” => [x] => “likes” => “count”}
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                        //getting the time
                        photo.createdTime = photoJSON.getJSONObject("caption").getInt("created_time");
                        //get profile picture
                        photo.profilePicUrl = photoJSON.getJSONObject("user").getString("profile_picture");
                        //get comment arrayCount
                        photo.commentCount = photoJSON.getJSONObject("comments").getInt("count");
                        //get comment username
                        //photo.comments= photoJSON.getJSONObject("comments").getJSONArray("data").getJSONObject(photo.commentCount).getString("username");
                        //get comments
                        JSONArray commentsArray = photoJSON.getJSONObject("comments").getJSONArray("data");
                        if (commentsArray != null) {
                            int size = commentsArray.length();

                            photo.comments = photoJSON.getJSONObject("comments").getJSONArray("data")
                                    .getJSONObject(size - 1).getString("text");
                            photo.commentUsername = photoJSON.getJSONObject("comments").getJSONArray("data")
                                    .getJSONObject(size - 1).getJSONObject("from").getString("username");

                            photo.commentArray = InstagramPhoto.fromJSONArray(photoJSON.getJSONObject("comments").getJSONArray("data"));
                            /*for(i=0; i<size; i++){
                                commentObject item = new commentObject();
                                item.comments = photoJSON.getJSONObject("comments").getJSONArray("data")
                                        .getJSONObject(i).getString("text");
                                item.commentUsername= photoJSON.getJSONObject("comments").getJSONArray("data")
                                        .getJSONObject(i).getJSONObject("from").getString("username");
                                photo.commentArray.add(item);
                            }*/
                        }


                        photos.add(photo);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //callback...after adding photos, refresh listview for photos to show up
                aPhotos.notifyDataSetChanged();

            }

            //on Failure

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                //Do Something
            }
        });
    }

    public void fetchTimelineAsync(int page) {

        /*client.get(url,0, new JsonHttpResponseHandler() {
            public void onSuccess(JSONArray json) {
                // Remember to CLEAR OUT old items before appending in the new ones
                aPhotos.clear();
                // ...the data has come back, add new items to your adapter...
                adapter.addAll(...);
                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);
            }

            public void onFailure(Throwable e) {
                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
            }
        });*/
        fetchPopularPhotos();
        swipeContainer.setRefreshing(false);
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
