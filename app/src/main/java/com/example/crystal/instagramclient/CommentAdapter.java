package com.example.crystal.instagramclient;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Crystal on 10/22/15.
 */
public class CommentAdapter extends ArrayAdapter<commentObject> {

    TextView username;
    TextView comment;


    public CommentAdapter(Context context, List<commentObject> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        commentObject commentObj = getItem(position);
        if(convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.comment_item, parent, false);
        }

        username = (TextView) convertView.findViewById(R.id.tvCommentUserName);
        comment = (TextView) convertView.findViewById(R.id.tvComment);
        username.setText(commentObj.commentUsername);
        comment.setText(commentObj.comments);

        return  convertView;
    }
}
