package com.example.jbstrand.probabilisticchess;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

public class activity_main extends ActionBarActivity {
    public final static String EXTRA_MESSAGE = "com.example.jbstrand.probabilisticchess.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean probabilistic = sharedPref.getBoolean("p_probabilistic", true);
        com.example.jbstrand.probabilisticchess.backend_chess.GAME_TYPE = probabilistic;

        Boolean options = sharedPref.getBoolean("p_options", true);
        com.example.jbstrand.probabilisticchess.backend_chess.GAME_OPTIONS = options;

        Boolean threats = sharedPref.getBoolean("p_threats", true);
        com.example.jbstrand.probabilisticchess.backend_chess.GAME_THREATS = threats;
        Switch switch1 = (Switch) findViewById(R.id.switch1);
        switch1.setChecked(true);
        Switch switch2 = (Switch) findViewById(R.id.switch2);
        switch2.setChecked(true);
        Switch switch3 = (Switch) findViewById(R.id.switch3);
        switch3.setChecked(false);
        Switch switch4 = (Switch) findViewById(R.id.switch4);
        switch4.setChecked(true);
        Switch switch5 = (Switch) findViewById(R.id.switch5);
        switch5.setChecked(false);





/*
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        if (isChecked) {
		        	com.example.jbstrand.probabilisticchess.backend_chess.GAME_TYPE = com.example.jbstrand.probabilisticchess.backend_chess.PROBABILISTIC;
		        } else {
                    com.example.jbstrand.probabilisticchess.backend_chess.GAME_TYPE = com.example.jbstrand.probabilisticchess.backend_chess.NORMAL;
		        }
		    }
		});
		switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        if (isChecked) {
                    com.example.jbstrand.probabilisticchess.backend_chess.GAME_HINTS = com.example.jbstrand.probabilisticchess.backend_chess.HINTS_ON;
		        } else {
                    com.example.jbstrand.probabilisticchess.backend_chess.GAME_HINTS = com.example.jbstrand.probabilisticchess.backend_chess.HINTS_OFF;
		        }
		    }
		});*/
        switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Switch switch4 = (Switch) findViewById(R.id.switch4);
                Switch switch5 = (Switch) findViewById(R.id.switch5);
                if (isChecked) {
                    com.example.jbstrand.probabilisticchess.backend_chess.GAME_MODE = com.example.jbstrand.probabilisticchess.backend_chess.ONE_PLAYER;
                    switch4.setChecked(false);
                    switch5.setChecked(false);
                }
            }
        });

        switch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Switch switch3 = (Switch) findViewById(R.id.switch3);
                Switch switch5 = (Switch) findViewById(R.id.switch5);
                if (isChecked) {
                    com.example.jbstrand.probabilisticchess.backend_chess.GAME_MODE = com.example.jbstrand.probabilisticchess.backend_chess.TWO_PLAYER;
                    switch3.setChecked(false);
                    switch5.setChecked(false);
                }
            }
        });

        switch5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Switch switch3 = (Switch) findViewById(R.id.switch3);
                Switch switch4 = (Switch) findViewById(R.id.switch4);
                if (isChecked) {
                    com.example.jbstrand.probabilisticchess.backend_chess.GAME_MODE = com.example.jbstrand.probabilisticchess.backend_chess.CHESS_WITH_FRIENDS;
                    switch3.setChecked(false);
                    switch4.setChecked(false);
                }
            }
        });
    }

    /**
     * Event Handling for Individual menu item selected
     * Identify single menu item by it's id
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.launcher:
                intent = new Intent(this, com.example.jbstrand.probabilisticchess.activity_main.class);
                startActivity(intent);
                return true;

            case R.id.settings:
                intent = new Intent(this, com.example.jbstrand.probabilisticchess.activity_settings.class);
                startActivity(intent);
                return true;

            case R.id.rules:
                intent = new Intent(this, com.example.jbstrand.probabilisticchess.activity_rules.class);
                startActivity(intent);
                return true;

            case R.id.help:
                intent = new Intent(this, com.example.jbstrand.probabilisticchess.activity_help.class);
                startActivity(intent);
                return true;

            case R.id.about:
                intent = new Intent(this, com.example.jbstrand.probabilisticchess.activity_about.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Called when the user clicks the Start button
     */
    public void start(View view) {
        Context context = this;
        Intent intent = new Intent(this, com.example.jbstrand.probabilisticchess.activity_game.class);
        startActivity(intent);
    }


    /**
     * Called when the user clicks the Start button
     */
    public void ar(View view) {
        Context context = this;
        PackageManager packageManager = context.getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == true) {
            Intent intent = new Intent(this, com.example.jbstrand.probabilisticchess.activity_augmentedreality.class);
            startActivity(intent);
        }
    }


}
