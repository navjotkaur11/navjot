package com.ICT.NAVJOT;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ICT.NAVJOT.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class detalis_cust_frag extends Fragment {

    String id;
    TextView tx_title,tx_date,tx_type,tx_place,tx_duration,tx_comment;
    String imagepath="";
    String title="",date="",type="",place="",duration="",comment="";
    DBHelper  db;
    public detalis_cust_frag() {
        // Required empty public constructor
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Setting");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_detalis_cust_frag, container, false);
        ImageView img=(ImageView)view.findViewById(R.id.img_loc);
    //    id = getIntent().getExtras().getString("i_id");
        Toast.makeText(getContext(), id, Toast.LENGTH_SHORT).show();
        tx_title=(TextView)view.findViewById(R.id.txt_title);
        tx_date=(TextView)view.findViewById(R.id.txt_date);
        tx_type=(TextView)view.findViewById(R.id.txt_activity_type);
        tx_place=(TextView)view.findViewById(R.id.txt_place);
        tx_duration=(TextView)view.findViewById(R.id.txt_duration);
        tx_comment=(TextView)view.findViewById(R.id.txt_comment);
        db=new DBHelper(getContext());
        Button btn_update=(Button)view.findViewById(R.id.buttonupdate);


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {try{
                db.open();
                db.deleteContact(Integer.valueOf(id));
                db.close();
                Toast.makeText(getContext(),"DELETE ID:- " ,Toast.LENGTH_LONG).show();

            }catch (Exception e){
                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }}
        });
        loadalldata();
        Toast.makeText(getContext(), id, Toast.LENGTH_SHORT).show();
        final TextView txt_place=(TextView)view.findViewById(R.id.txt_place);
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
        return view;
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
            Toast.makeText(getContext(), "No contact found",
                    Toast.LENGTH_LONG).show();
        db.close();

    }

}
