package com.example.jbstrand.probabilisticchess;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class activity_help extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_help, menu);
        return true;
    }

    /**
     * Event Handling for Individual menu item selected
     * Identify single menu item by it's id
     */
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
}
