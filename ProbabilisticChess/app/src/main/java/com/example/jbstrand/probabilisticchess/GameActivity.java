package com.example.jbstrand.probabilisticchess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActivity extends FragmentActivity 
implements InputFragment.OnInputSelectedListener {
	
	private static int colour;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
/*
		ImageView image1A = (ImageView) findViewById(R.id.grid1A);
		ImageView image1B = (ImageView) findViewById(R.id.grid1B);
		ImageView image1C = (ImageView) findViewById(R.id.grid1C);
		ImageView image1D = (ImageView) findViewById(R.id.grid1D);
		ImageView image1E = (ImageView) findViewById(R.id.grid1E);
		ImageView image1F = (ImageView) findViewById(R.id.grid1F);
		ImageView image1G = (ImageView) findViewById(R.id.grid1G);
		ImageView image1H = (ImageView) findViewById(R.id.grid1H);
		ImageView image2A = (ImageView) findViewById(R.id.grid2A);
		ImageView image2B = (ImageView) findViewById(R.id.grid2B);
		ImageView image2C = (ImageView) findViewById(R.id.grid2C);
		ImageView image2D = (ImageView) findViewById(R.id.grid2D);
		ImageView image2E = (ImageView) findViewById(R.id.grid2E);
		ImageView image2F = (ImageView) findViewById(R.id.grid2F);
		ImageView image2G = (ImageView) findViewById(R.id.grid2G);
		ImageView image2H = (ImageView) findViewById(R.id.grid2H);
		ImageView image3A = (ImageView) findViewById(R.id.grid3A);
		ImageView image3B = (ImageView) findViewById(R.id.grid3B);
		ImageView image3C = (ImageView) findViewById(R.id.grid3C);
		ImageView image3D = (ImageView) findViewById(R.id.grid3D);
		ImageView image3E = (ImageView) findViewById(R.id.grid3E);
		ImageView image3F = (ImageView) findViewById(R.id.grid3F);
		ImageView image3G = (ImageView) findViewById(R.id.grid3G);
		ImageView image3H = (ImageView) findViewById(R.id.grid3H);
		ImageView image4A = (ImageView) findViewById(R.id.grid4A);
		ImageView image4B = (ImageView) findViewById(R.id.grid4B);
		ImageView image4C = (ImageView) findViewById(R.id.grid4C);
		ImageView image4D = (ImageView) findViewById(R.id.grid4D);
		ImageView image4E = (ImageView) findViewById(R.id.grid4E);
		ImageView image4F = (ImageView) findViewById(R.id.grid4F);
		ImageView image4G = (ImageView) findViewById(R.id.grid4G);
		ImageView image4H = (ImageView) findViewById(R.id.grid4H);
		ImageView image5A = (ImageView) findViewById(R.id.grid5A);
		ImageView image5B = (ImageView) findViewById(R.id.grid5B);
		ImageView image5C = (ImageView) findViewById(R.id.grid5C);
		ImageView image5D = (ImageView) findViewById(R.id.grid5D);
		ImageView image5E = (ImageView) findViewById(R.id.grid5E);
		ImageView image5F = (ImageView) findViewById(R.id.grid5F);
		ImageView image5G = (ImageView) findViewById(R.id.grid5G);
		ImageView image5H = (ImageView) findViewById(R.id.grid5H);
		ImageView image6A = (ImageView) findViewById(R.id.grid6A);
		ImageView image6B = (ImageView) findViewById(R.id.grid6B);
		ImageView image6C = (ImageView) findViewById(R.id.grid6C);
		ImageView image6D = (ImageView) findViewById(R.id.grid6D);
		ImageView image6E = (ImageView) findViewById(R.id.grid6E);
		ImageView image6F = (ImageView) findViewById(R.id.grid6F);
		ImageView image6G = (ImageView) findViewById(R.id.grid6G);
		ImageView image6H = (ImageView) findViewById(R.id.grid6H);
		ImageView image7A = (ImageView) findViewById(R.id.grid7A);
		ImageView image7B = (ImageView) findViewById(R.id.grid7B);
		ImageView image7C = (ImageView) findViewById(R.id.grid7C);
		ImageView image7D = (ImageView) findViewById(R.id.grid7D);
		ImageView image7E = (ImageView) findViewById(R.id.grid7E);
		ImageView image7F = (ImageView) findViewById(R.id.grid7F);
		ImageView image7G = (ImageView) findViewById(R.id.grid7G);
		ImageView image7H = (ImageView) findViewById(R.id.grid7H);
		ImageView image8A = (ImageView) findViewById(R.id.grid8A);
		ImageView image8B = (ImageView) findViewById(R.id.grid8B);
		ImageView image8C = (ImageView) findViewById(R.id.grid8C);
		ImageView image8D = (ImageView) findViewById(R.id.grid8D);
		ImageView image8E = (ImageView) findViewById(R.id.grid8E);
		ImageView image8F = (ImageView) findViewById(R.id.grid8F);
		ImageView image8G = (ImageView) findViewById(R.id.grid8G);
		ImageView image8H = (ImageView) findViewById(R.id.grid8H);
		*/
		initialise();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}
	/** Called when the user clicks the Rules button */
	public void home(View view) {
	    Intent intent = new Intent(this, MainActivity.class);
	    startActivity(intent);	    
	}
	/** Called when the user clicks the Start button */
	public void rules(View view) {
	    Intent intent = new Intent(this, RulesActivity.class);
	    startActivity(intent);	    
	}	
	public void initialise() {
		Chess.setGameActivity(this);
     	Chess.initialiseBoard();
     	//printBoard();
     	colour = Chess.WHITE;
     	printToConsole("White's turn");
	}
    public void flushConsole() {
    	printToConsole("");
    	printToConsole("");
    	printToConsole("");
    	printToConsole("");
    }
	
	
    public void printToConsole(String message) {
    	TextView line1 = (TextView) findViewById(R.id.console_line1);  	
    	TextView line2 = (TextView) findViewById(R.id.console_line2);
    	String message2 = line2.getText().toString();
    	TextView line3 = (TextView) findViewById(R.id.console_line3);
    	String message3 = line3.getText().toString();
    	TextView line4 = (TextView) findViewById(R.id.console_line4);
    	String message4 = line4.getText().toString();
    	
    	line1.setText(message2);
    	line2.setText(message3);
    	line3.setText(message4);
    	line4.setText(message);    	
    }
    public void printPiece(ImageView image, int x, int y) {
    	int piece = Chess.board[x][y];
    	if (piece == 0) {
    		if ((x + y) % 2 == 0 ) {
    			piece = 13;
    		} else 
    			piece = 14;
    	}
    	switch (piece) {
    		case 1: image.setImageResource(R.drawable.b_p); break;
    		case 2: image.setImageResource(R.drawable.b_r); break;
    		case 3: image.setImageResource(R.drawable.b_n); break;
    		case 4: image.setImageResource(R.drawable.b_b); break;
    		case 5: image.setImageResource(R.drawable.b_q); break;
    		case 6: image.setImageResource(R.drawable.b_k); break;
    		case 7: image.setImageResource(R.drawable.w_p); break;
    		case 8: image.setImageResource(R.drawable.w_r); break;
    		case 9: image.setImageResource(R.drawable.w_n); break;
    		case 10: image.setImageResource(R.drawable.w_b); break;
    		case 11: image.setImageResource(R.drawable.w_q); break;
    		case 12: image.setImageResource(R.drawable.w_k); break;
    		case 13: image.setImageResource(R.drawable.b); break;
    		case 14: image.setImageResource(R.drawable.w); break;
    		default: image.setImageResource(R.drawable.ic_launcher); break;
    	}
    }
	public void printBoard() {
		ImageView  temp ;
		/*
		temp = (ImageView) findViewById(R.id.grid1A);
		printPiece(temp,0, 0);
		temp = (ImageView) findViewById(R.id.grid1B);
		printPiece(temp,0, 1);
		temp = (ImageView) findViewById(R.id.grid1C);
		printPiece(temp,0, 2);
		temp = (ImageView) findViewById(R.id.grid1D);
		printPiece(temp,0, 3);
		temp = (ImageView) findViewById(R.id.grid1E);
		printPiece(temp,0, 4);
		temp = (ImageView) findViewById(R.id.grid1F);
		printPiece(temp,0, 5);
		temp = (ImageView) findViewById(R.id.grid1G);
		printPiece(temp,0, 6);
		temp = (ImageView) findViewById(R.id.grid1H);
		printPiece(temp,0, 7);
		temp = (ImageView) findViewById(R.id.grid2A);
		printPiece(temp,1, 0);
		temp = (ImageView) findViewById(R.id.grid2B);
		printPiece(temp,1, 1);
		temp = (ImageView) findViewById(R.id.grid2C);
		printPiece(temp,1, 2);
		temp = (ImageView) findViewById(R.id.grid2D);
		printPiece(temp,1, 3);
		temp = (ImageView) findViewById(R.id.grid2E);
		printPiece(temp,1, 4);
		temp = (ImageView) findViewById(R.id.grid2F);
		printPiece(temp,1, 5);
		temp = (ImageView) findViewById(R.id.grid2G);
		printPiece(temp,1, 6);
		temp = (ImageView) findViewById(R.id.grid2H);
		printPiece(temp,1, 7);
		temp = (ImageView) findViewById(R.id.grid3A);
		printPiece(temp,2, 0);
		temp = (ImageView) findViewById(R.id.grid3B);
		printPiece(temp,2, 1);
		temp = (ImageView) findViewById(R.id.grid3C);
		printPiece(temp,2, 2);
		temp = (ImageView) findViewById(R.id.grid3D);
		printPiece(temp,2, 3);
		temp = (ImageView) findViewById(R.id.grid3E);
		printPiece(temp,2, 4);
		temp = (ImageView) findViewById(R.id.grid3F);
		printPiece(temp,2, 5);
		temp = (ImageView) findViewById(R.id.grid3G);
		printPiece(temp,2, 6);
		temp = (ImageView) findViewById(R.id.grid3H);
		printPiece(temp,2, 7);
		temp = (ImageView) findViewById(R.id.grid4A);
		printPiece(temp,3, 0);
		temp = (ImageView) findViewById(R.id.grid4B);
		printPiece(temp,3, 1);
		temp = (ImageView) findViewById(R.id.grid4C);
		printPiece(temp,3, 2);
		temp = (ImageView) findViewById(R.id.grid4D);
		printPiece(temp,3, 3);
		temp = (ImageView) findViewById(R.id.grid4E);
		printPiece(temp,3, 4);
		temp = (ImageView) findViewById(R.id.grid4F);
		printPiece(temp,3, 5);
		temp = (ImageView) findViewById(R.id.grid4G);
		printPiece(temp,3, 6);
		temp = (ImageView) findViewById(R.id.grid4H);
		printPiece(temp,3, 7);
		temp = (ImageView) findViewById(R.id.grid5A);
		printPiece(temp,4, 0);
		temp = (ImageView) findViewById(R.id.grid5B);
		printPiece(temp,4, 1);
		temp = (ImageView) findViewById(R.id.grid5C);
		printPiece(temp,4, 2);
		temp = (ImageView) findViewById(R.id.grid5D);
		printPiece(temp,4, 3);
		temp = (ImageView) findViewById(R.id.grid5E);
		printPiece(temp,4, 4);
		temp = (ImageView) findViewById(R.id.grid5F);
		printPiece(temp,4, 5);
		temp = (ImageView) findViewById(R.id.grid5G);
		printPiece(temp,4, 6);
		temp = (ImageView) findViewById(R.id.grid5H);
		printPiece(temp,4, 7);
		temp = (ImageView) findViewById(R.id.grid6A);
		printPiece(temp,5, 0);
		temp = (ImageView) findViewById(R.id.grid6B);
		printPiece(temp,5, 1);
		temp = (ImageView) findViewById(R.id.grid6C);
		printPiece(temp,5, 2);
		temp = (ImageView) findViewById(R.id.grid6D);
		printPiece(temp,5, 3);
		temp = (ImageView) findViewById(R.id.grid6E);
		printPiece(temp,5, 4);
		temp = (ImageView) findViewById(R.id.grid6F);
		printPiece(temp,5, 5);
		temp = (ImageView) findViewById(R.id.grid6G);
		printPiece(temp,5, 6);
		temp = (ImageView) findViewById(R.id.grid6H);
		printPiece(temp,5, 7);
		temp = (ImageView) findViewById(R.id.grid7A);
		printPiece(temp,6, 0);
		temp = (ImageView) findViewById(R.id.grid7B);
		printPiece(temp,6, 1);
		temp = (ImageView) findViewById(R.id.grid7C);
		printPiece(temp,6, 2);
		temp = (ImageView) findViewById(R.id.grid7D);
		printPiece(temp,6, 3);
		temp = (ImageView) findViewById(R.id.grid7E);
		printPiece(temp,6, 4);
		temp = (ImageView) findViewById(R.id.grid7F);
		printPiece(temp,6, 5);
		temp = (ImageView) findViewById(R.id.grid7G);
		printPiece(temp,6, 6);
		temp = (ImageView) findViewById(R.id.grid7H);
		printPiece(temp,6, 7);
		temp = (ImageView) findViewById(R.id.grid8A);
		printPiece(temp,7, 0);
		temp = (ImageView) findViewById(R.id.grid8B);
		printPiece(temp,7, 1);
		temp = (ImageView) findViewById(R.id.grid8C);
		printPiece(temp,7, 2);
		temp = (ImageView) findViewById(R.id.grid8D);
		printPiece(temp,7, 3);
		temp = (ImageView) findViewById(R.id.grid8E);
		printPiece(temp,7, 4);
		temp = (ImageView) findViewById(R.id.grid8F);
		printPiece(temp,7, 5);
		temp = (ImageView) findViewById(R.id.grid8G);
		printPiece(temp,7, 6);
		temp = (ImageView) findViewById(R.id.grid8H);
		printPiece(temp,7, 7);*/
	}
}