package com.example.crystal.instagramclient;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class commentList extends ActionBarActivity {
    private CommentAdapter aComments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);

        InstagramPhoto photo = (InstagramPhoto) getIntent().getSerializableExtra("photo");
        ArrayList<commentObject> commentObjects = new ArrayList<commentObject>();
        commentObjects = photo.commentArray;
        /*ArrayList<commentObject> dummArry = new ArrayList<commentObject>();
        commentObject dumObject = new commentObject();
        dumObject.commentUsername="1";
        dumObject.comments ="2";
        dummArry.add(dumObject);*/


//        ArrayList<String> items = new ArrayList<>();/
//      items.add("la");
    //    ArrayAdapter<String> itemsAdapter =
    //            new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);

        aComments = new CommentAdapter(this, commentObjects);
        //aComments = new CommentAdapter(this, dummArry);

        ListView listView = (ListView) findViewById(R.id.lvComments);
        listView.setAdapter(aComments);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comment_list, menu);
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
