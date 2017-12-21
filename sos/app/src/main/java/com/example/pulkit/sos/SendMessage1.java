package com.example.pulkit.sos;
import android.*;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.DialogPreference;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.jar.*;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
public class SendMessage1 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,SensorEventListener,GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener{
    SensorManager sensorManager;
    ImageButton button1, button_map, button_stop;
    Random random;
    TextView stop_recording_text;
    Button pattern;
    MediaPlayer player;
    double  gravity=0;
    long count =0;
    Geocoder geocoder;
    SensorManager sm;
    Sensor accelerator;
    long presentTime;
    List<Address> addresses;
    boolean detect = false;
    //LocationListener ll;
    String address,city,state,country,postalCode,knownName;
    int dTap = 0;
    LocationManager location_manager;
    TextView timer;
    String finalAddress = null;
    double latitude,longitude;
    //boolean notified;
    GestureDetectorCompat mDetector;
    private static final String DEBUG_TAG = "Gestures";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_send_message1);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
//            Log.d("hehe","onCreate is called");
            timer=(TextView) findViewById(R.id.timer);
            button1 = (ImageButton) findViewById(R.id.sos_button);
            button_stop = (ImageButton) findViewById(R.id.audio_stop_button);
            button_stop.setVisibility(View.INVISIBLE);
            pattern= (Button) findViewById(R.id.pattern);
            button_map = (ImageButton) findViewById(R.id.temp_button);
            stop_recording_text = (TextView) findViewById(R.id.stop_recording_text);
            mDetector = new GestureDetectorCompat(this, this);
            mDetector.setOnDoubleTapListener(this);
            random = new Random();
//            Intent i = getIntent();
//            if(i.getBooleanExtra("status",false)){
//                dTap = 1;
//                pattern.setVisibility(View.INVISIBLE);
//            }
            SharedPreferences sp = getSharedPreferences("Contacts", Context.MODE_PRIVATE);
            String s1 = sp.getString("1", "chooseContact1");
            String s2 = sp.getString("2", "chooseContact2");
            String s3 = sp.getString("3", "chooseContact3");
            String s4 = sp.getString("4", "chooseContact4");
            String s5 = sp.getString("5", "chooseContact5");
            if(s1.equals("chooseContact1") && s2.equals("chooseContact2") && s3.equals("chooseContact3") && s4.equals("chooseContact4") && s5.equals("chooseContact5"))
            {
                AlertDialog.Builder detect = new AlertDialog.Builder(SendMessage1.this);
                detect.setMessage("Add atleast one emergency contact to continue.").setPositiveButton("OK", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        Intent in = new Intent(SendMessage1.this,MainActivity1.class);
                        startActivity(in);
                    }
                });
                detect.setCancelable(false);
                AlertDialog alert=detect.create();
                alert.show();


            }

            sm = (SensorManager) getSystemService(SENSOR_SERVICE);
            accelerator = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sm.registerListener((SensorEventListener) this, accelerator, SensorManager.SENSOR_DELAY_FASTEST);
           location_manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            x = y = z = 10000;
//            gravity = 0;
//            gravmax = 10000;
//            count = 0;
            latitude=longitude=0.0;

//        ll = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                Log.d("hehe","location changed");
//                notified = true;
//            }
//            @Override
//            public void onStatusChanged(String s, int i, Bundle bundle) {}
//            @Override
//            public void onProviderEnabled(String s) {}
//            @Override
//            public void onProviderDisabled(String s) {}
//        };


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isNetworkEnabled() && !isWifiEnabled()) {
                    AlertDialog.Builder detect = new AlertDialog.Builder(SendMessage1.this);
                    detect.setMessage("Please switch on your data or connect to Wifi.").setPositiveButton("OK",new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    AlertDialog alert=detect.create();
                    alert.show();

                }
                    else {
                    Snackbar.make(view, "Go to your current location", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    Intent intent = new Intent(SendMessage1.this, MapsActivity.class);
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

        boolean is_network_enabled = isNetworkEnabled();
        if(!is_network_enabled && !isWifiEnabled())
            create_dialog(2);

        boolean is_gps_enabled = isGPSEnabled();
        if(!is_gps_enabled)
            create_dialog(1);
//        new locationTask().execute();

        new locationTask().execute();
//        Intent i = new Intent(this,AlarmService.class);
//        i.putExtra("Detect Fall",detect);
//        startService(i);
    }

    public boolean isGPSEnabled(){

        if(location_manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            return true;
            return false;
    }

    public boolean isWifiEnabled(){
        final ConnectivityManager connMgr = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifi.isConnectedOrConnecting ()) {
            return true;
        }
        return false;
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
        }
    }
    public void create_dialog(int type){
        String Title,Message;
        final String sendTo;
        if(type==1){//GPS dialog
            Title="Enable GPS";
            Message="GPS";
            sendTo=Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        }
        else {
            Title="Enable Data";
            Message="Data";
            sendTo=Settings.ACTION_WIRELESS_SETTINGS;
        }

        AlertDialog.Builder gps_dialog=new AlertDialog.Builder(this);
        gps_dialog.setTitle(Title)
                .setMessage("Sending your current location to Emergency contacts require  "+Message+" to be Enabled.")
                .setPositiveButton("Settings", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent in=new Intent(sendTo);
                        startActivity(in);
                    }
                });
        if(Message == "Data") {
        gps_dialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        }

        AlertDialog alert=gps_dialog.create();
        alert.show();
    }

    public void drawPattern(View v){
        Intent i = new Intent(this,Custom_Drawing.class);
        i.putExtra("presentTime",presentTime);
        startActivityForResult(i,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            if(data.getBooleanExtra("status",false)){
                dTap=1;
                timer.setVisibility(View.INVISIBLE);
                pattern.setVisibility(View.INVISIBLE);
            }
            else
                dTap=0;
        }
    }

    public void sendMessage(View view) throws IOException, InterruptedException {
        button1.setEnabled(false);
        button1.setImageResource(R.drawable.sosbtn2);
        timer.setVisibility(View.VISIBLE);
        timer.setText("00:00:20");
        pattern.setVisibility(View.VISIBLE);
        Snackbar.make(view, "Double Tap the screen within 20 seconds to cancel this action.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        SendMessageTask task = new SendMessageTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    String audio_path = null;
    MediaRecorder mediaRecorder;

    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;


    public void stopRecording(View view) {
        mediaRecorder.stop();
        button_stop.setVisibility(View.INVISIBLE);
        stop_recording_text.setVisibility(View.INVISIBLE);
        button1.setEnabled(true);
        button1.setImageResource(R.drawable.sosbtn);
     //   Toast.makeText(SendMessage1.this, "Recording complete", Toast.LENGTH_SHORT).show();
        Snackbar.make(view, "Recording Complete", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void MediaRecorderReady() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(audio_path);
    }

    public String CreateRandomAudioFileName(int string) {
        StringBuilder stringBuilder = new StringBuilder(string);
        int i = 0;
        while (i < string) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++;
        }
        return stringBuilder.toString();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(SendMessage1.this, new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(SendMessage1.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SendMessage1.this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
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
        getMenuInflater().inflate(R.menu.send_message1, menu);
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

            Intent intent = new Intent(this, MainActivity1.class);
            startActivity(intent);
            return true;
        } if (id == R.id.menu_about_us) {
            Intent i = new Intent(this,AboutUs.class);
            startActivity(i);
            return true;
        } if (id == R.id.menu_info) {
            Intent i = new Intent(SendMessage1.this,HelpInfo.class);
            startActivity(i);
            return true;
        } if (id == R.id.menu_rate_us) {
            return true;
        } if(id == R.id.settings)
        {
            Intent i = new Intent(SendMessage1.this, com.example.pulkit.sos.Settings.class);
            startActivity(i);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    //     return super.onOptionsItemSelected(item);


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
            Intent intent = new Intent(this, MainActivity1.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_open_map) {
            if(!isNetworkEnabled() && !isWifiEnabled()) {
                AlertDialog.Builder detect = new AlertDialog.Builder(SendMessage1.this);
                detect.setMessage("Please switch on your data or connect to Wifi.").setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alert = detect.create();
                alert.show();
            }
            else {
                Intent intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
            }

        } else if (id == R.id.nav_view_recordings) {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/SOS_Recordings/");
            i.setDataAndType(uri, "text/mp3");
            startActivity(i);

        } else if (id == R.id.nav_manage) {
            Intent i = new Intent(SendMessage1.this,HelpInfo.class);
            startActivity(i);

        } else if (id == R.id.nav_share) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "SOS APP");
            String str = "\n this is an awesome app ";
            i.putExtra(Intent.EXTRA_TEXT, str);
            startActivity(Intent.createChooser(i, "Share via"));
        } //else if (id == R.id.nav_send) {

        //}


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        //Log.d(DEBUG_TAG,"onDown: " + event.toString());
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        //Log.d(DEBUG_TAG, "onFling: " + event1.toString()+event2.toString());
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        //Log.d(DEBUG_TAG, "onLongPress: " + event.toString());
        //Toast.makeText(this,"long pressed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        //Log.d(DEBUG_TAG, "onScroll: " + e1.toString()+e2.toString());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        //Log.d(DEBUG_TAG, "onShowPress: " + event.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        //Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
//        Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString());

       // Toast.makeText(this, "Double tap", Toast.LENGTH_SHORT).show();

        dTap = 1;
        timer.setVisibility(View.INVISIBLE);
        pattern.setVisibility(View.INVISIBLE);
        if(player != null && player.isPlaying())
            player.stop();
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        //Log.d(DEBUG_TAG, "onDoubleTapEvent: " + event.toString());
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        //Log.d(DEBUG_TAG, "onSingleTapConfirmed: " + event.toString());
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //L'accelerometro ha cambiato stato
            fall(event);
        }
    }
    public void fall(SensorEvent event){

        double value = Math.sqrt((event.values[0] * event.values[0]) + (event.values[1] * event.values[1]) + (event.values[2] * event.values[2]));
        if (value < 1) {
            count++;
        }
        if (value > gravity) {
            gravity = value;
        }
        if ( gravity >=90 &&  count >=40) {
            gravity = count = 0;
            try {
                sendMessage(button1);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }



            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {


                    AssetFileDescriptor afd = null;
                    try {
                        afd = getAssets().openFd("Alarm.mp3");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    player = new MediaPlayer();
                    try {
                        player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        player.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    player.start();

                    try {
                        Thread.sleep(20000);
                        player.stop();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //      player.stop();
                }
            });
            SharedPreferences sp=getSharedPreferences("Settings",MODE_PRIVATE);
            boolean checked = sp.getBoolean("isChecked",false);
            if(checked)
                t.start();

        }



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


//    @Override
//    public void onSensorChanged(SensorEvent event) {
//
//        double value = Math.sqrt((event.values[0] * event.values[0]) + (event.values[1] * event.values[1]) + (event.values[2] * event.values[2]));
//        if (event.values[0] < x)
//            x = event.values[0];
//        if (event.values[1] < y)
//            y = event.values[1];
//        if (event.values[2] < z) {
//            z = event.values[2];
//        }
//        if (value < 1) {
//            //grav =  Math.sqrt((event.values[0] * event.values[0]) + (event.values[1] * event.values[1]) + (event.values[2] * event.values[2]));
//            count++;
//        }
//        if (value > gravity)
//            gravity = value;
//
//        if (gravity >= 90 && count >= 40) {
//            fall = true;
//            if (fall) {
//                Thread t = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        MediaPlayer player;
//                        AssetFileDescriptor afd = null;
//                        try {
//                            afd = getAssets().openFd("Alarm.mp3");
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        player = new MediaPlayer();
//                        try {
//                            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        try {
//                            player.prepare();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        player.start();
//                        try {
//                            Thread.sleep(5000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        //      player.stop();
//                    }
//                });
//                t.start();
//
//                //fallen.setText("free fall");
//                Toast.makeText(this, "free fall", Toast.LENGTH_SHORT).show();
//            }
//            gravity = count = 0;
//        } else {
//            //fallen.setText("you are safe");
//            //Toast.makeText(this,"you are safe",Toast.LENGTH_SHORT).show();
//        }
//    }

//    @Override
//    public void onAccuracyChanged(Sensor sensor, int i) {
//
//    }


    class SendMessageTask extends AsyncTask<Void, Integer, Void> { //I have changed the input from View to Void
        View view;
        String address,city,state,country,postal_code,known_name;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... view) {

            dTap = 0;
            for (int i = 1; i <= 40; i++) {
                try {
                    Thread.sleep(500);
                    presentTime=i*500;
                    if(i%2 == 0)
                    {
                        publishProgress(500*i/1000+10);
                    }
                 //   Log.d("hehe","thread is running");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (dTap == 1) {
                    dTap=0;
                   // Log.d("hehe","dtap is 1");
                    publishProgress(2);
                    return null;
                }
            }

            publishProgress(1);



          //  Log.d("hehe","starting recording");
//            Log.d("hehe","Send Message loc: "+finalAddress);
            startRecording();
            SharedPreferences sp = getSharedPreferences("Contacts", Context.MODE_PRIVATE);
            String s1 = sp.getString("1", "chooseContact1");
            String s2 = sp.getString("2", "chooseContact2");
            String s3 = sp.getString("3", "chooseContact3");
            String s4 = sp.getString("4", "chooseContact4");
            String s5 = sp.getString("5", "chooseContact5");
            SmsManager sms = SmsManager.getDefault();

            s1 = s1.replaceAll("\\s+","");
            s2 = s2.replaceAll("\\s+","");
            s3 = s3.replaceAll("\\s+","");
            s4 = s4.replaceAll("\\s+","");
            s5 = s5.replaceAll("\\s+","");

            if(finalAddress.equals("Hey.. I am in Danger. Please, help me ASAP!!")){
                publishProgress(500);
            }
//            Log.d("hehe",s1);
            if(!s1.equals("chooseContact1")) {
               // Toast.makeText(SendMessage1.this,"this is contact one "+ finalAddress,Toast.LENGTH_SHORT).show();
//                    Log.d("hehe",finalAddress+"this is sms 1");

                sms.sendTextMessage(s1,null,finalAddress,null,null);
//              Log.d("hehe","sending sms"+finalAddress);
            }
            if (!s2.equals("chooseContact2"))
                sms.sendTextMessage(s2, null,finalAddress, null, null); //must be uncommented
            if (!s3.equals("chooseContact3"))
                sms.sendTextMessage(s3, null,finalAddress, null, null);
            if (!s4.equals("chooseContact4"))
                sms.sendTextMessage(s4, null,finalAddress, null, null);
            if (!s5.equals("chooseContact5"))
                sms.sendTextMessage(s5, null,finalAddress, null, null);

            //Geocoder geoCoder ;

            //  Toast.makeText(this,ans +"this is the address",Toast.LENGTH_SHORT).show();


            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            if (values[0] == 1) {
                button1.setEnabled(false);
                button1.setImageResource(R.drawable.sosbtn2);
            } else if(values[0]==2){
                button1.setEnabled(true);
                button1.setImageResource(R.drawable.sosbtn);
            }
            else if(values[0]==3)
            {
                pattern.setVisibility(View.INVISIBLE);
                button_stop.setVisibility(View.VISIBLE);
                stop_recording_text.setVisibility(View.VISIBLE);
            }
            else if (values[0]==500){
//                Log.d("hehe","making toast");
                Toast.makeText(SendMessage1.this,"Unable to find location.",Toast.LENGTH_SHORT).show();
            }
            else {

                if(30-values[0] < 10 ) {

                    timer.setText("00:00:0" + (30 - values[0]));
                    if(30-values[0] == 0)
                        timer.setVisibility(View.INVISIBLE);
                }
                else
                    timer.setText("00:00:"+(30-values[0]));
            }

        }



        public void startRecording() {

            if (checkPermission()) {
                String folder_name = "SOS_Recordings";
                File f = new File(Environment.getExternalStorageDirectory(), folder_name);
                if (!f.exists())
                    f.mkdirs();
                audio_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SOS_Recordings/" + "SOS" + CreateRandomAudioFileName(3) + "Recording.3gp";
              //  Toast.makeText(SendMessage1.this, audio_path, Toast.LENGTH_SHORT).show();
                MediaRecorderReady();

                try {
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
               // Toast.makeText(SendMessage1.this, "Recording started", Toast.LENGTH_SHORT).show();
                // button_stop.setVisibility(View.VISIBLE);
                publishProgress(3);
            } else {
                requestPermission();
            }
        }


    }

    class locationTask extends AsyncTask<Void,Integer,Void>{
        LocationListener ll;
        String provider;
        Location location;
        boolean notified;
        locationTask(){
            notified=false;

//            ll = new LocationListener() {
//                @Override
//                public void onLocationChanged(Location location) {
//                    Log.d("hehe","location changed");
//                    notified = true;
//                }
//                @Override
//                public void onStatusChanged(String s, int i, Bundle bundle) {}
//                @Override
//                public void onProviderEnabled(String s) {}
//                @Override
//                public void onProviderDisabled(String s) {}
//            };
        }
        @Override
        protected void onPreExecute() {
        //    Log.d("hehe","location listener created again");
            ll = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                   // Log.d("hehe","location changed");
                    notified = true;
                }
                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {}
                @Override
                public void onProviderEnabled(String s) {}
                @Override
                public void onProviderDisabled(String s) {}
            };
            //super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

          finalAddress="Hey.. I am in Danger. Please, help me ASAP!!";
          //  Log.d("hehe","hereeeeee");
            Log.d("hehe","GPS is:"+isGPSEnabled());
            while(!isGPSEnabled()){}
            Log.d("hehe","here again");
            if(isGPSEnabled()) {
                getLatAndLong(1);
                Log.d("hehe","got location");
            }
            else if(isNetworkEnabled())
                getLatAndLong(2);
            else
                publishProgress(1);
            finalAddress = finalAddress + "Latitiudes: "+latitude+" Longitudes: "+longitude;
            Log.d("hehe","lat and long : "+finalAddress);
            if(isNetworkEnabled() || isWifiEnabled()){
                //Geocoder geocoder;
                addresses=null;
                geocoder = new Geocoder(SendMessage1.this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                  //  Log.d("hehe",addresses);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                city = addresses.get(0).getLocality();
                state = addresses.get(0).getAdminArea();
                country = addresses.get(0).getCountryName();
                postalCode = addresses.get(0).getPostalCode();
                knownName = addresses.get(0).getFeatureName();
                finalAddress="Hey.. I am in Danger. Please, help me ASAP!! Latitudes : "+latitude+" Longitudes : "+longitude+" and my location is : "+address+" "+city+" "+state+" "+country+" "+postalCode+""+knownName ;
                Log.d("hehe",finalAddress);
            }
            return null;
        }

        void getLatAndLong(int type){

            if(type==1) {//GPS provider
                provider = LocationManager.GPS_PROVIDER;
            } else {
                provider = LocationManager.NETWORK_PROVIDER;
            }


            try {
                publishProgress(2);
              //  Log.d("hehe","progress published");
                while (!notified){}  //blocking position
                //Log.d("hehe","notified");
//                location_manager.requestLocationUpdates(provider,0,0,ll);
                if(location_manager!=null){
                    location = location_manager.getLastKnownLocation(provider);
                }
                else
                 Log.d("hehe","Location Manager is null");
                if (location != null){
                    latitude=location.getLatitude();
                    longitude=location.getLongitude();
                   Log.d("hehe",""+latitude+" "+longitude);
                }
                else
                    Log.d("hehe","location is null");
            } catch (SecurityException e) {
                e.printStackTrace();

            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (values[0]==1){
                Toast.makeText(SendMessage1.this, "Neither GPS nor NETWORK is enabled..Unable to find location", Toast.LENGTH_SHORT).show();
            }
            else if(values[0]==2){
                try {
                    //Log.d("hehe","requesting location");
                    location_manager.requestLocationUpdates(provider,0,0,ll);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}