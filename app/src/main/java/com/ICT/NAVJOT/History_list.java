package com.ICT.NAVJOT;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ICT.NAVJOT.R;

import java.util.ArrayList;

public class History_list extends AppCompatActivity {
    LinearLayout maplayoutlogin;
    Fragment fg;
    android.support.v4.app.FragmentTransaction ft;
    View view;
    private int group1Id = 1;

    int homeId = Menu.FIRST;
    int profileId = Menu.FIRST +1;
    int searchId = Menu.FIRST +2;
    int dealsId = Menu.FIRST +3;
    int helpId = Menu.FIRST +4;
    int contactusId = Menu.FIRST +5;
    add_new_user fragmentlogin = null;
    LinearLayout maplayoutlog;

    private static final int REQUEST_CODE_LOCATIONl = 102;
    Dialog dialoggps;
    private static final int REQUEST_CODE_LOCATION = 101;
    String[] locationPermissionsl = {"android.permission.ACCESS_COARSE_LOCATION","android.permission.ACCESS_FINE_LOCATION","android.permission.WRITE_EXTERNAL_STORAGE","android.permission.READ_EXTERNAL_STORAGE","android.permission.CAMERA","android.permission.CALL_PHONE"};
    public  static Fragment fragment = null;
    ListView list;
    Spinner sp;
    TextView tv;
    LinearLayout llyout;
    ImageView img;

    final static ArrayList<Customer> blist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_history_list);
      //  sp=(Spinner)findViewById(R.id.sp);
         img=(ImageView)findViewById(R.id.image);
         llyout=(LinearLayout)findViewById(R.id.layout);
         list=(ListView)findViewById(R.id.listview);
         tv=(TextView)findViewById(R.id.textView);

        Handler handler  = new Handler();
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {

                if (ActivityCompat.checkSelfPermission(History_list.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(History_list.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(History_list.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(History_list.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(History_list.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
                        )                    {
                    ActivityCompat.requestPermissions(History_list.this, locationPermissionsl, REQUEST_CODE_LOCATIONl);
                }
            }
        };
        handler.postDelayed(runnable, 300);
        if (!Home.isGpsOn(History_list.this)) {showGPSHomeCustomDialogWithThread(); }

       list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
       @Override
       public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
           String id=(((TextView)view.findViewById(R.id.textView22)).getText().toString());
       //    Toast.makeText(History_list.this,id, Toast.LENGTH_SHORT).show();
           Intent ii=new Intent(History_list.this,View_layout.class);
          ii.putExtra("i_id",id);
          startActivity(ii);
       }
   });try{
      setAmountRecord();}catch (Exception e){e.printStackTrace();

        }

    }
    public void setAmountRecord()
    {

        try
        {
            SQLiteDatabase db = openOrCreateDatabase("MyDB", Context.MODE_PRIVATE, null);
            Cursor c = db.rawQuery("select * from student", null);
            Adapter_history adapter = new Adapter_history(getApplication());
            blist.clear();
             while(c.moveToNext())
             {
                 blist.add(new Customer( String.valueOf(c.getString(4)), c.getString(1), c.getString(2),c.getString(0)));
             }
                 list.setAdapter(adapter);
                    db.close();
                }catch (Exception e)
               { //img.setVisibility(View.VISIBLE);
                   img.setImageDrawable(getResources().getDrawable(R.drawable.supplier_list_empty));
               //   Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                   Toast.makeText(this, "Please Add new Customer", Toast.LENGTH_SHORT).show();
         //        tv.setText(e.toString());
                   }
             }

          @Override
          protected void onResume() {
              try{
                  setAmountRecord();}catch (Exception e){e.printStackTrace();}
              super.onResume();
          }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,0,0,"Add New Customer");
        menu.add(1,1,1,"Setting");
        menu.add(2,2,2,"Exit");
        return super.onCreateOptionsMenu(menu);
    }*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(group1Id, homeId, homeId, "").setIcon(android.R.drawable.ic_menu_add).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(group1Id, profileId, profileId, "").setIcon(android.R.drawable.ic_menu_manage).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(group1Id, searchId, searchId, "").setIcon(android.R.drawable.ic_menu_set_as).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);


        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                // write your code here
            //    Intent i=new Intent(History_list.this,MainActivity.class);startActivity(i);
                fragmentlogin = new add_new_user();
                list.setVisibility(View.GONE);
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.layout, fragmentlogin);
                //     ft.addToBackStack(null);
                ft.commit();
                return true;

            case 2:
                    /*fragmentlogin = new add_new_user();
                   list.setVisibility(View.GONE);
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.layout, fragmentlogin);
                //     ft.addToBackStack(null);
                ft.commit();*/
                Intent i=new Intent(History_list.this,MainActivity.class);startActivity(i);
                return true;

            case 3:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Exit Application?");
                alertDialogBuilder
                        .setMessage("Click yes to exit!")
                        .setCancelable(false)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        moveTaskToBack(true);
                                        android.os.Process.killProcess(android.os.Process.myPid());
                                        System.exit(1);
                                    }
                                })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }

    }
   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case 0:
            //    fragmentlogin = new add_new_user();
             //   list.setVisibility(View.GONE);
             //   img.setVisibility(View.GONE);
               Intent i=new Intent(History_list.this,MainActivity.class);startActivity(i);
                break;
            case 1:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Exit Application?");
                alertDialogBuilder
                        .setMessage("Click yes to exit!")
                        .setCancelable(false)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        moveTaskToBack(true);
                                        android.os.Process.killProcess(android.os.Process.myPid());
                                        System.exit(1);
                                    }
                                })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            default:
                break;

        }
        if (fragmentlogin != null) {
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.layout, fragmentlogin);
            //     ft.addToBackStack(null);
            ft.commit();
        }
        return super.onOptionsItemSelected(item);
    }*/

    private void showGPSHomeCustomDialogWithThread() {

        try
        {AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("No Location Access");
            alertDialogBuilder.setMessage("Please grant Pawan Project access to your location. Turn this feature ON for accurate location from Location Service in your phone settings?");
            alertDialogBuilder.setPositiveButton("TURN ON LOCATION",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), REQUEST_CODE_LOCATION);
                        }
                    });

            alertDialogBuilder.setNegativeButton("CLOSE",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
        catch (Exception e)
        {
            //     Crashlytics.logException(e);

            e.printStackTrace();
        }

    }
   /* @Override
    protected void onStart() {
        if (!Home.isGpsOn(History_list.this)) {showGPSHomeCustomDialogWithThread(); }
        super.onStart();
    }*/

}



