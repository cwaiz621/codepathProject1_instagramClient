package com.example.crystal.instagramclient;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Transformation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Crystal on 9/18/15.
 */
public class InstagramPhotoAdapter extends ArrayAdapter<InstagramPhoto>{
    //constructor - what data do we need from the activity
    //Context, Datasource
    public InstagramPhotoAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    //What our item looks like
    //Use template to display each photo

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the data item for this position
        InstagramPhoto photo = getItem(position);
        //Check if we are using a recycled view, if not need to inflate - recycled use view, not generate duplicate memory
        if (convertView == null) {
            //create new view from template
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }
        // Lookup the views for populating the data (image, caption)
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        ImageView ivPhoto= (ImageView) convertView.findViewById(R.id.ivPhoto);
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        TextView tvRelativeTime = (TextView) convertView.findViewById(R.id.tvRelativeTime);
        TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikesCount);
        ImageView ivProfile = (ImageView) convertView.findViewById(R.id.ivProfilePic);
        TextView tvCuserName = (TextView) convertView.findViewById(R.id.tvCusername);
        // insert the model data into each of the view items
        //Insert the text from caption
        tvCaption.setText(photo.caption);
        //clear out the ImageView
        ivPhoto.setImageResource(0);
        //Insert the image using picasso
        //When loading with Picasso, experiment with different loading behaviors such as .fit().centerInside()
        // or .resize(x, y) specified before the .into(...) command.
        /*To resize an image and preserve aspect ratio, as of Picasso 2.4.0 you can simply supply 0 for one of
         the arguments i.e .resize(150, 0)
        If you want to determine the width of the screen, you can check the width of the layout item or use this
        device dimensions helper
        In the adapter, you can calculate the aspect ratio of the image (width / height) and then set
        the ImageView width to screen width and the height to correct calculated height to maintain the aspect ratio
        of the image.*/
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;;
        int ratio = (width / photo.imageHeight);
        int newHeight = ratio*photo.imageHeight;
        Picasso.with(getContext()).load(photo.imageUrl).resize(width,newHeight).into(ivPhoto);
        //Insert the text from username
        tvUsername.setText(photo.username);
        //Insert Likes count
        String heart = "\u2764";
        DecimalFormat formated = new DecimalFormat("#,###,###");
        String formatedLikes = formated.format(photo.likesCount);
        tvLikes.setText(heart + " " + formatedLikes + " likes");
        //Insert the time from photo
        tvRelativeTime.setText(RelativeTime(photo.createdTime));

        //clear our profile pic image view
        ivProfile.setImageResource(0);
        com.squareup.picasso.Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(30)
                .oval(false)
                .build();

        Picasso.with(getContext())
                .load(photo.profilePicUrl)
                .fit()
                .transform(transformation)
                .into(ivProfile);

        //Picasso.with(getContext()).load(photo.profilePicUrl).into(ivProfile);

        //insert comment username
        //String formatedText = <font color="#1414c0"> photo.commentUsername </font> + " " +photo.comments;
        //tvCuserName.setText(Html.fromHtml(formatedText));

        //tvCuserName.setText(photo.commentUsername + " ");
        //insert comments
        //tvcomments.setText(photo.comments);

        String firstWord = "Hello";
        String secondWord = "World!";

    // Create a span that will make the text red
        ForegroundColorSpan blueText = new ForegroundColorSpan( -16776961);

        // Use a SpannableStringBuilder so that both the text and the spans are mutable
        SpannableStringBuilder ssb = new SpannableStringBuilder(photo.commentUsername);

        // Apply the color span
        ssb.setSpan(
                blueText,            // the span to add
                0,                                 // the start of the span (inclusive)
                ssb.length(),                      // the end of the span (exclusive)
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // behavior when text is later inserted into the SpannableStringBuilder
        // SPAN_EXCLUSIVE_EXCLUSIVE means to not extend the span when additional
        // text is added in later

// Add a blank space
        ssb.append(" ");

// Add the secondWord and apply the strikethrough span to only the second word
        ssb.append(photo.comments);
        ssb.setSpan(
                null,
                ssb.length() - secondWord.length(),
                ssb.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

// Set the TextView text and denote that it is Editable
// since it's a SpannableStringBuilder
        tvCuserName.setText(ssb, TextView.BufferType.EDITABLE);

        // return the created item as a view
        return convertView;
    }

    public String RelativeTime(long InstagramTime){
        long unixTime = System.currentTimeMillis()/1000L;
        int secondsPast= (int)(unixTime - InstagramTime) /1000;

        if(secondsPast < 60){
            return String.valueOf(secondsPast + "s");
        }else{
        if(secondsPast < 3600){
            return String.valueOf(secondsPast/60 + "m");
        }else{
        if(secondsPast <= 86400){
            return String.valueOf(secondsPast/3600 + "h");
        }else{
        if(secondsPast > 86400){
            return String.valueOf(secondsPast/86400)+"days";
        }}}}

        return String.valueOf(secondsPast + "error");
    }


}
