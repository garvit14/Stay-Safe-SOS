package com.example.pulkit.sos;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.lang.reflect.Method;

public class MainActivity1 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button button1,button2,button3,button4,button5;
    ImageButton d1,d2,d3,d4,d5;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        button1 = (Button) findViewById(R.id.contact1);
        button2 = (Button) findViewById(R.id.contact2);
        button3 = (Button) findViewById(R.id.contact3);
        button4 = (Button) findViewById(R.id.contact4);
        button5 = (Button) findViewById(R.id.contact5);
        d1 = (ImageButton) findViewById(R.id.d1);
        d2 = (ImageButton) findViewById(R.id.d2);
        d3 = (ImageButton) findViewById(R.id.d3);
        d4 = (ImageButton) findViewById(R.id.d4);
        d5 = (ImageButton) findViewById(R.id.d5);
        load();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isNetworkEnabled() && !isWifiEnabled()) {
                    AlertDialog.Builder detect = new AlertDialog.Builder(MainActivity1.this);
                    detect.setMessage("Please switch on your data or connect to wifi.").setPositiveButton("OK",new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    AlertDialog alert=detect.create();
                    alert.show();}
                else {
                    Snackbar.make(view, "go to your current location", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Intent intent = new Intent(MainActivity1.this, MapsActivity.class);
                    startActivity(intent);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void deleteContact(View v){
        String s;
        SharedPreferences.Editor ed = sp.edit();
        switch (v.getId()){
            case R.id.d1 :
                s = sp.getString("1_1","choose Contact 1");
                if(s!="choose Contact 1"){
                    ed.remove("1_1");
                    ed.commit();
                    ed.remove("1");
                    ed.commit();
                    button1.setText("choose Contact 1");
                }
                break;
            case R.id.d2 :
                s = sp.getString("2_1","choose Contact 2");
                if(s!="choose Contact 2"){
                    ed.remove("2_1");
                    ed.commit();
                    ed.remove("2");
                    ed.commit();
                    button2.setText("choose Contact 2");
                }
                break;
            case R.id.d3 :
                s = sp.getString("3_1","choose Contact 3");
                if(s!="choose Contact 3"){
                    ed.remove("3_1");
                    ed.commit();
                    ed.remove("3");
                    ed.commit();
                    button3.setText("choose Contact 3");
                }
                break;
            case R.id.d4 :
                s = sp.getString("4_1","choose Contact 4");
                if(s!="choose Contact 4"){
                    ed.remove("4_1");
                    ed.commit();
                    ed.remove("4");
                    ed.commit();
                    button4.setText("choose Contact 4");
                }
                break;
            case R.id.d5 :
                s = sp.getString("5_1","choose Contact 5");
                if(s!="choose Contact 5"){
                    ed.remove("5_1");
                    ed.commit();
                    ed.remove("5");
                    ed.commit();
                    button5.setText("choose Contact 5");
                }
                break;
        }
    }

    public void openContacts(View view)
    {
        // Intent intent = null;
        int choose_button;
        if(view.getId() == R.id.contact1)
            choose_button = 1;
        else if(view.getId() ==R.id.contact2)
            choose_button = 2;
        else if(view.getId() == R.id.contact3)
            choose_button = 3;
        else if(view.getId() == R.id.contact4)
            choose_button = 4;
        else
            choose_button = 5;


        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (MainActivity.this, Manifest.permission.READ_CONTACTS)) {
                Snackbar.make(findViewById(android.R.id.content),
                        "Please Grant Permissions",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission
                                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS},
                                        REQUEST_PERMISSIONS);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission
                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS},
                        REQUEST_PERMISSIONS);
            }
        } else {
            //Call whatever you want
            myMethod();
        }






        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        // Toast.makeText(this,"this is me",Toast.LENGTH_SHORT).show();
        startActivityForResult(intent, choose_button);
      //
        // Toast.makeText(this,"this is me",Toast.LENGTH_SHORT).show();

    }

    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if(resultCode == 0)
            return ;
        String cNumber=null;
        String name = null;

        if (resultCode == Activity.RESULT_OK) {

            Uri contactData = data.getData();
            Cursor c =  managedQuery(contactData, null, null, null, null);
            if (c.moveToFirst()) {


                String id =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                String hasPhone =c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                if (hasPhone.equalsIgnoreCase("1")) {
                    Cursor phones = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,
                            null, null);
                    phones.moveToFirst();
                    cNumber = phones.getString(phones.getColumnIndex("data1"));
                    System.out.println("number is:"+cNumber);
                }
                name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));


            }
        }
        storeData(name,cNumber,reqCode);
        showSelectedNumber(cNumber);
        switch (reqCode)
        {
            case 1:
                button1.setText(name);
                break;
            case 2:
                button2.setText(name);
                break;
            case 3:
                button3.setText(name);
                break;
            case 4:
                button4.setText(name);
                break;
            case 5:
                button5.setText(name);
                break;
        }
    }
    public void showSelectedNumber(String number) {
        Toast.makeText(this,"Selected contact Number: " + number, Toast.LENGTH_LONG).show();
     //   CoordinatorLayout cl = findViewById(R.id.coordinatorLayout);
    }
    public void storeData(String name,String cNumber,int choose_button)
    {
        sp = getSharedPreferences("Contacts", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed= sp.edit();
        ed.putString(Integer.toString(choose_button),cNumber);
        ed.putString(Integer.toString(choose_button)+"_1",name);
        ed.commit();
        Toast.makeText(this,"Data saved successfully",Toast.LENGTH_SHORT).show();


    }
    public void load()
    {
        sp = getSharedPreferences("Contacts", Context.MODE_PRIVATE);
        String s1= sp.getString("1_1","choose Contact 1");
        String s2= sp.getString("2_1","choose Contact 2");
        String s3= sp.getString("3_1","choose Contact 3");
        String s4= sp.getString("4_1","choose Contact 4");
        String s5= sp.getString("5_1","choose Contact 5");
        button1.setText(s1);
        button2.setText(s2);
        button3.setText(s3);
        button4.setText(s4);
        button5.setText(s5);
        return;
    }
    public boolean isNetworkEnabled(){
        Object connectivityService = getSystemService(CONNECTIVITY_SERVICE);
        ConnectivityManager cm = (ConnectivityManager) connectivityService;

        try {
            Class<?> c = Class.forName(cm.getClass().getName());
            Method m = c.getDeclaredMethod("getMobileDataEnabled");
            m.setAccessible(true);
            return (Boolean)m.invoke(cm);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }}
    public boolean isWifiEnabled(){
        final ConnectivityManager connMgr = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifi.isConnectedOrConnecting ()) {
            return true;}
        return false;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_update_contacts) {
            if(MainActivity1.this instanceof MainActivity1) {
                Toast.makeText(this,"You are on the same activity",Toast.LENGTH_SHORT).show();
               // CoordinatorLayout cl = (CoordinatorLayout) findViewById(R.layout.activity_main1);
            }
            else {
                Intent intent = new Intent(this, MainActivity1.class);
                startActivity(intent);
            }

        }
        else if(id == R.id.menu_about_us)
        {
            Intent i = new Intent(this,AboutUs.class);
            startActivity(i);
            return true;
        }
        else if(id == R.id.menu_info)
        {
            Intent i = new Intent(MainActivity1.this,HelpInfo.class);
            startActivity(i);
            return true;
        }
        else if(id == R.id.menu_rate_us)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

            if(id == R.id.nav_fall_detect)
            {
                AlertDialog.Builder detect = new AlertDialog.Builder(this);

                detect.setTitle("Fall Detection Info").setMessage("This feature of the app helps you if in any situation your phone falls on the ground.It triggers the alarm and if not paused by double tapping the screen will ultimately send sms to upto 5 emergency contacts.").setPositiveButton("OK",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alert=detect.create();
                alert.show();

            }
        else if (id == R.id.nav_edit_contact) {
                if(MainActivity1.this instanceof MainActivity1) {
                    // Toast.makeText(this,"You are on the same Activity",Toast.LENGTH_SHORT).show();
                   // Snackbar.make(getActivity().findViewById(android.R.id.content), "Go to your current location", Snackbar.LENGTH_LONG)
                     ///       .setAction("Action", null).show();
                }

                    else
                    {
                        Intent intent = new Intent(this, MainActivity1.class);
                        startActivity(intent);
                    }
            // Handle the camera action
        } else if (id == R.id.nav_open_map) {
                if(!isNetworkEnabled() && !isWifiEnabled()) {
                    AlertDialog.Builder detect = new AlertDialog.Builder(MainActivity1.this);
                    detect.setMessage("Please switch on your data or connect to wifi.").setPositiveButton("OK",new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    AlertDialog alert=detect.create();
                    alert.show();}
                else{
                    Intent intent = new Intent(this, MapsActivity.class);
                    startActivity(intent);
                }
        } else if (id == R.id.nav_view_recordings) {
            Intent i=new Intent(Intent.ACTION_GET_CONTENT);
            Uri uri=Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/SOS_Recordings/");
            i.setDataAndType(uri,"text/mp3");
            startActivity(i);

        } else if (id == R.id.nav_manage) {
                Intent i = new Intent(MainActivity1.this,HelpInfo.class);
                startActivity(i);

        } else if (id == R.id.nav_share) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT,"SOS APP");
            String str = "\n this is an awesome app ";
            i.putExtra(Intent.EXTRA_TEXT,str);
            startActivity(Intent.createChooser(i,"choose one"));

        } //else if (id == R.id.nav_send) {

        //}

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
}

