package com.example.pulkit.sos;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class Settings extends AppCompatActivity {
    SharedPreferences sp;
    Switch aswitch;
    boolean checked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        aswitch= (Switch) findViewById(R.id.my_switch);
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        aswitch.setChecked(sp.getBoolean("isChecked",false));
        aswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    checked=true;
                else
                    checked=false;
                SharedPreferences.Editor ed= sp.edit();
                ed.putBoolean("isChecked",checked);
                ed.commit();
            }
        });

    }


}
