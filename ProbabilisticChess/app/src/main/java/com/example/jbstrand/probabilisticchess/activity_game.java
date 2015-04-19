package com.example.jbstrand.probabilisticchess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class activity_game extends FragmentActivity
        implements fragment_input.OnInputSelectedListener {

    public volatile static backend_board board;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initialise();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_game, menu);
        return true;
    }

    /**
     * Called when the user clicks the Rules button
     */
    public void home(View view) {
        Intent intent = new Intent(this, activity_main.class);
        startActivity(intent);
    }

    /**
     * Called when the user clicks the Start button
     */
    public void rules(View view) {
        Intent intent = new Intent(this, activity_rules.class);
        startActivity(intent);
    }

    public void initialise() {
        backend_chess.setGameActivity(this);
        board = backend_chess.initialiseBoard();
        backend_chess.colour = backend_pieceColour.White;
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

        //line1.setText(message2);
        //line2.setText(message3);
        //line3.setText(message4);
        //line4.setText(message);
    }
}