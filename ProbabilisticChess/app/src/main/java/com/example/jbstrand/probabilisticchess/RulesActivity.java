package com.example.jbstrand.probabilisticchess;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class RulesActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rules);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.rules, menu);
		return super.onCreateOptionsMenu(menu);
	}

    /**
     * Event Handling for Individual menu item selected
     * Identify single menu item by it's id
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.launcher:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.settings:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.rules:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.help:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /** Called when the user clicks the Rules button */
	public void home(View view) {
	    Intent intent = new Intent(this, MainActivity.class);
	    startActivity(intent);	    
	}
	/** Called when the user clicks the Start button */
	public void start(View view) {
	    Intent intent = new Intent(this, GameActivity.class);
	    startActivity(intent);	    
	}			

}
