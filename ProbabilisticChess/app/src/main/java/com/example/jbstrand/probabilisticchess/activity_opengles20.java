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
    public static boolean initial = false;

    public MyGLSurfaceView(Context boardFragment) {
        super(boardFragment);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new MyGL20Renderer();
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        mRenderer.colour = backend_pieceColour.White;
        if (backend_chess.GAME_MODE == backend_chess.ONE_PLAYER) {
            backend_chessengine.setColour(backend_pieceColour.Black);
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
        if (mRenderer.colour == backend_pieceColour.White) {
            row = (int) (4 - ((y - getHeight() / 2) / (getWidth() / 8)));
            col = (7 - (int) (x / (getWidth() / 8)));
            mRenderer.startX = (col - 4) * 0.25f + 0.125f;
            mRenderer.startY = (row - 4) * 0.25f + 0.125f;
        } else {
            row = (int) (4 + ((y - getHeight() / 2) / (getWidth() / 8)));
            col = ((int) (x / (getWidth() / 8)));
            mRenderer.startX = (col - 4) * 0.25f + 0.125f;
            mRenderer.startY = (row - 4) * 0.25f + 0.125f;

        }
        if (!initial) {
            mRenderer.start_square = new backend_square(col,row);
        } else {
            mRenderer.stop_square = new backend_square(col,row);
        }
        switch (e.getAction()) {

            case MotionEvent.ACTION_DOWN:
                backend_piece start_piece = activity_game.board.get_Board(mRenderer.start_square);
                if (start_piece.get_pieceType() != backend_pieceType.None) {
                    if (!initial && start_piece.get_pieceColour() == mRenderer.colour) {
                        if(backend_move.moveA(activity_game.board, mRenderer.start_square)) {
                            initial = true;
                        }
                    }
                }
                requestRender();
                return true;
            case MotionEvent.ACTION_UP:
                if (initial) {
                    boolean status = backend_move.moveB(activity_game.board, mRenderer.start_square,mRenderer.stop_square);
                    if (status) {
                        if (backend_chess.GAME_MODE == backend_chess.TWO_PLAYER) {
                            if (mRenderer.colour == backend_pieceColour.White) {
                                mRenderer.colour = backend_pieceColour.Black;
                            } else {
                                mRenderer.colour = backend_pieceColour.White;
                            }
                        } else if (backend_chess.GAME_MODE == backend_chess.ONE_PLAYER) {
                            backend_chessengine.computerMove(activity_game.board);
                        }
                    }
                    initial = false;
                }
                mRenderer.startX = 0;
                mRenderer.startY = 0;
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
