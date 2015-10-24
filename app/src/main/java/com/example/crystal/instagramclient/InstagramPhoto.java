package com.example.crystal.instagramclient;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by Crystal on 9/18/15.
 */
public class InstagramPhoto implements Serializable{
    public String username;
    public String caption;
    public String imageUrl;
    public int imageHeight;
    public int likesCount;
    public long createdTime;
    public String profilePicUrl;
    public String comments;
    public String commentUsername;
    public int commentCount;
    public ArrayList<commentObject> commentArray;

    public static ArrayList<commentObject> fromJSONArray(JSONArray array){
        ArrayList<commentObject> results = new ArrayList();
        for (int i=0; i<array.length();i++){
            try{
                commentObject item = new commentObject();
                item.comments = array.getJSONObject(i).getString("text");
                item.commentUsername= array.getJSONObject(i).getJSONObject("from").getString("username");
                results.add(item);
            }catch(JSONException e){
                e.printStackTrace();
            }
        }

        return results;
    }

}
