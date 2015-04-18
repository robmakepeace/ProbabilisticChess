

package com.example.jbstrand.probabilisticchess;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;

public class activity_opengles20 extends Activity {

    private GLSurfaceView mGLView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity
        mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        mGLView.onResume();
    }
    
}

class MyGLSurfaceView extends GLSurfaceView {

    public final MyGL20Renderer mRenderer;
    boolean initial = false;
    public MyGLSurfaceView(Context boardFragment) {
        super(boardFragment);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new MyGL20Renderer();
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        mRenderer.colour = backend_chess.WHITE;
        if (backend_chess.GAME_MODE == backend_chess.ONE_PLAYER) {
        	backend_chessengine.setColour(backend_chess.BLACK);
        }
    }
	
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();
        int row;
        int col;
        int temp;
        
        float dx = x - mPreviousX;
        float dy = y - mPreviousY;
        if (mRenderer.colour == backend_chess.WHITE) {
            row = (int)( 4 - ((y - getHeight()/2) / (getWidth()/8)));
            col = (7 - (int)(x / (getWidth()/8)));
            mRenderer.startX = (col-4)* 0.25f+0.125f;
        	mRenderer.startY = (row-4)*0.25f+0.125f;
        } else {
            row = (int)(4 + ((y - getHeight()/2) / (getWidth()/8)));
            col = ((int)(x / (getWidth()/8)));
            mRenderer.startX = (col-4)* 0.25f+0.125f;
        	mRenderer.startY = (row-4)*0.25f+0.125f;
        }
        if (row >= 0 && row < 8 && col >= 0 && col < 8) {
        	temp = 8*row + col;
        } else {
        	temp = 0;
        }
        
        switch (e.getAction()) {
        
    		case MotionEvent.ACTION_DOWN:
    			if ((initial == false || backend_chess.colour(temp/8,temp%8) == mRenderer.colour) &&
                		mRenderer.squareStart != temp && backend_chess.moveA(mRenderer.colour, temp)) {
                	
                	initial = true;
                	mRenderer.squareStart = temp;
                }
    			requestRender();
    			return true;
    		case MotionEvent.ACTION_UP:	
                if (initial == true && mRenderer.squareStart != temp) {
                	boolean status =backend_chess.moveB(mRenderer.colour, mRenderer.squareStart,temp);
                	if (status) {
                		initial = false;
                		if (backend_chess.GAME_MODE == backend_chess.TWO_PLAYER) {
                			mRenderer.colour = (mRenderer.colour % 2) + 1;
                		} else if (backend_chess.GAME_MODE == backend_chess.ONE_PLAYER) {
                            backend_chessengine.computerMove();
                		}
                		
                	}              
                }
                mRenderer.squareStart = -1;
                requestRender();
    			return true;
            case MotionEvent.ACTION_MOVE:
                requestRender();
                return true;
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }
}
