package com.ICT.NAVJOT;

import android.animation.ObjectAnimator;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ICT.NAVJOT.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;

public class View_layout extends AppCompatActivity {
String id;
    TextView tx_title,tx_date,tx_type,tx_place,tx_duration,tx_comment;
    String imagepath="";
    ImageView img,imgpic;
    String title="",date="",type="",place="",duration="",comment="";
    DBHelper  db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
         img=(ImageView)findViewById(R.id.img_loc);
        imgpic=(ImageView)findViewById(R.id.imageView);
        id = getIntent().getExtras().getString("i_id");
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
        tx_title=(TextView)findViewById(R.id.txt_title);
        tx_date=(TextView)findViewById(R.id.txt_date);
        tx_type=(TextView)findViewById(R.id.txt_activity_type);
        tx_place=(TextView)findViewById(R.id.txt_place);
        tx_duration=(TextView)findViewById(R.id.txt_duration);
        tx_comment=(TextView)findViewById(R.id.txt_comment);
           db=new DBHelper(View_layout.this);
            Button btn_update=(Button)findViewById(R.id.buttonupdate);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {try{
                db.open();
                db.deleteContact(Integer.valueOf(id));
                db.close();
                Toast.makeText(getBaseContext(),"DELETE ID:- " ,Toast.LENGTH_LONG).show();
                finish();
            }catch (Exception e){
                Toast.makeText(View_layout.this, e.toString(), Toast.LENGTH_SHORT).show();
            }}
        });
          loadalldata();
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
        final TextView txt_place=(TextView)findViewById(R.id.txt_place);
        img.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                String strUri = "http://maps.google.com/maps?q=" + txt_place.getText().toString();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        });

        tx_title.setText(title);
        tx_date.setText(date);
        tx_type.setText(type);
        tx_place.setText(place);
        tx_duration.setText(duration);
        tx_comment.setText(comment);
        loadProfileImage(getApplication(),imagepath, (CircularImageView) imgpic);

    }
    public static void loadProfileImage(Context conn, String imageURL, CircularImageView imageView)
    {
        try
        {
            if(imageURL.equals("")  &&  imageURL.isEmpty())
            {
                Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +"://" + conn.getResources().getDrawable(R.drawable.default_user));
           //     if(AppUtills.showLogs) Log.e("AppUtils","loadImage in if imageUri..."+imageUri);
                Glide.with(conn)
                        .load(imageUri)
                        .placeholder(R.drawable.default_user)
                        .error(R.drawable.default_user)
                        .fallback(R.drawable.default_user)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .animate(getAnimatorImageLoading())
                        .thumbnail(0.1f)
                        .into(imageView);
            }
            else
            {
                Glide.with(conn)
                        .load(imageURL)
                        .placeholder(R.drawable.default_user)
                        .error(R.drawable.default_user)
                        .fallback(R.drawable.default_user)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .animate(getAnimatorImageLoading())
                        .thumbnail(0.1f)
                        .into(imageView);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static ViewPropertyAnimation.Animator getAnimatorImageLoading()
    {
        ViewPropertyAnimation.Animator animationObject=null;

        try
        {
            animationObject = new ViewPropertyAnimation.Animator()
            {
                @Override
                public void animate(View view)
                {
                    // if it's a custom view class, cast it here
                    // then find subviews and do the animations
                    // here, we just use the entire view for the fade animation
                    view.setAlpha( 0f );

                    ObjectAnimator fadeAnim = ObjectAnimator.ofFloat( view, "alpha", 0f, 1f );
                    fadeAnim.setDuration( 2500 );
                    fadeAnim.start();
                }
            };
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return animationObject;
    }
    private void loadalldata() {

        db.open();
        Cursor c = db.getContact(Integer.valueOf(id));
        if (c.moveToFirst())

        {
            title= c.getString(0);
            date= c.getString(1);
            type=c.getString(2);
            place=c.getString(3);
            duration=c.getString(4);
            comment=c.getString(5);
            imagepath=c.getString(6);

        }
        else
            Toast.makeText(getBaseContext(), "No contact found",
                    Toast.LENGTH_LONG).show();
        db.close();

    }
}
