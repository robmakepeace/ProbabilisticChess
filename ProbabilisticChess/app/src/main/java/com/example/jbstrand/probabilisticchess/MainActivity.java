package com.example.jbstrand.probabilisticchess;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends ActionBarActivity {
	public final static String EXTRA_MESSAGE = "com.example.jbstrand.probabilisticchess.MESSAGE";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
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
		
		switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        if (isChecked) {
		        	Chess.GAME_TYPE = Chess.PROBABILISTIC;
		        } else {
		        	Chess.GAME_TYPE = Chess.NORMAL;
		        }
		    }
		});
		switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        if (isChecked) {
		        	Chess.GAME_HINTS = Chess.HINTS_ON;
		        } else {
		        	Chess.GAME_HINTS = Chess.HINTS_OFF;
		        }
		    }
		});
		switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        Switch switch4 = (Switch) findViewById(R.id.switch4);
		        Switch switch5 = (Switch) findViewById(R.id.switch5);   
		        if (isChecked) {
		            Chess.GAME_MODE = Chess.ONE_PLAYER;
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
		            Chess.GAME_MODE = Chess.TWO_PLAYER;
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
		            Chess.GAME_MODE = Chess.CHESS_WITH_FRIENDS;
		            switch3.setChecked(false);
		            switch4.setChecked(false);            
		        }
		    }
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	/** Called when the user clicks the Rules button */
	public void rules(View view) {
	    Intent intent = new Intent(this, RulesActivity.class);
	    startActivity(intent);	    
	}
	/** Called when the user clicks the Start button */
	public void start(View view) {
	    Intent intent = new Intent(this, GameActivity.class);
	    startActivity(intent);	    
	}		
	/** Called when the user clicks the Start button */
	public void ar(View view) {
		Context context = this;
		PackageManager packageManager = context.getPackageManager();
		if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == true) {
			Intent intent = new Intent(this, AugmentedReality.class);
			startActivity(intent);	    
		}
	}	
}
