package com.example.jbstrand.probabilisticchess;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

public class MyGL20Renderer implements MyGLSurfaceView.Renderer {

    private static final String TAG = "MyGLRenderer";
    private Square[][] mSquare = new Square[8][8];
    private Piece[] mPiece = new Piece[32];

    private backend_piece[] cPiece = new backend_piece[32];
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjMatrix = new float[16];
    private final float[] mVMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];

    // Declare as volatile because we are updating it from another thread
    public volatile float mAngle;
    public volatile backend_square start_square = new backend_square(0, 0);
    public volatile backend_square stop_square = new backend_square(0, 0);
    public volatile float startX = 0;
    public volatile float startY = 0;
    public volatile float stopX = 0;
    public volatile float stopY = 0;
    public volatile backend_pieceColour colour;

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        float squareCoords[] = {
                -1f, 1f, 0.0f,   // top left
                -1f, 0.75f, 0.0f,   // bottom left
                -0.75f, 0.75f, 0.0f,   // bottom right
                -0.75f, 1f, 0.0f}; // top right
        // Set color with red, green, blue and alpha (opacity) values


        int squarenumberX = 0;
        int squarenumberY = 0;
        for (float y = -4; y < 4; y++) {
            for (float x = -4; x < 4; x++) {
                squareCoords[0] = x * 0.25f;
                squareCoords[3] = x * 0.25f;
                squareCoords[6] = x * 0.25f + 0.25f;
                squareCoords[9] = x * 0.25f + 0.25f;

                squareCoords[1] = y * 0.25f + 0.25f;
                squareCoords[4] = y * 0.25f;
                squareCoords[7] = y * 0.25f;
                squareCoords[10] = y * 0.25f + 0.25f;
                mSquare[(int) (x+4)][(int) (y+4)] = new Square(squareCoords);
            }
        }

    }

    @Override
    public void onDrawFrame(GL10 unused) {

        float colours[] = {0.5f, 0.5f, 0.5f, 1f};
        int[][] tmp;
        int j;
        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Set the camera position (View matrix)
        // Matrix.setLookAtM(mVMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        if (colour == backend_pieceColour.White) {
            Matrix.setLookAtM(mVMatrix, 0, 0, 1, -4, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        } else {
            Matrix.setLookAtM(mVMatrix, 0, 0, 1, -4, 0f, 0f, 0f, 0f, -1.0f, 0.0f);
        }
        //setLookAtM (float[] rm, int rmOffset, float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ)
        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);

        float centreCoords[] = { // in counterclockwise order:
                0.0f, 0f, 0f,   // top
        };
        int pieceCount = 0;
        backend_square square = new backend_square(0, 0);
        for (float x = -4; x < 4; x++) {
            for (float y = -4; y < 4; y++) {
                square.X = (int) (x + 4);
                square.Y = (int) (y + 4);
                if (activity_game.board.get_Board(square).get_pieceType() != backend_pieceType.None) {
                    centreCoords[0] = x * 0.25f + 0.125f;
                    centreCoords[1] = y * 0.25f + 0.125f;
                    if (square.X == start_square.X && square.Y == start_square.Y && MyGLSurfaceView.initial) {
                        centreCoords[0] = startX;
                        centreCoords[1] = startY;
                    }
                    if (pieceCount < 32) {
                        backend_piece piece = activity_game.board.get_Board(square);
                        if (piece.get_pieceType() != backend_pieceType.None) {
                            cPiece[pieceCount] = piece;
                            mPiece[pieceCount] = new Piece(centreCoords, cPiece[pieceCount]);
                            pieceCount = pieceCount + 1;
                        }
                    }
                }
            }
        }


        // Draw square
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if ((x + y) % 2 == 1) {
                    colours = setColours(0.25f, 0.25f, 0.25f, 0);
                } else {
                    colours = setColours(0.75f, 0.75f, 0.75f, 0);
                }
                mSquare[x][y].draw(mMVPMatrix, colours);
            }
        }
        if (backend_chess.GAME_THREATS) {
            if (activity_game.board.get_Board(start_square) != null) {
                tmp = backend_move.getThreats(activity_game.board, start_square);

                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        if (tmp[x][y] == 1) {
                            //Possible Moves -> GREEN
                            colours = setColours(1f, 0.5f, 0.5f, 0);
                            mSquare[x][y].draw(mMVPMatrix, colours);
                        } else if (tmp[x][y] == 2) {
                            //Possible Takes -> RED
                            colours = setColours(0.5f, 0, 0.5f, 0);
                            mSquare[x][y].draw(mMVPMatrix, colours);
                        }
                    }
                }
            }
        }
        if (MyGLSurfaceView.initial && backend_chess.GAME_OPTIONS) {
            int x, y;
            if (activity_game.board.get_Board(start_square) != null) {
                tmp = backend_move.getOptions(activity_game.board, start_square);
                for (x = 0; x < 8; x++) {
                    for (y = 0; y < 8; y++) {
                        if (tmp[x][y] == 1) {
                            //Possible Moves -> GREEN
                            colours = setColours(0, 0.5f, 0, 0);
                            mSquare[x][y].draw(mMVPMatrix, colours);
                        } else if (tmp[x][y] == 2) {
                            //Possible Takes -> RED
                            colours = setColours(0.5f, 0, 0, 0);
                            mSquare[x][y].draw(mMVPMatrix, colours);
                        }
                    }
                }
            }
        }
        if (MyGLSurfaceView.initial) {
            //Selected Piece -> BLUE
            colours = setColours(0, 0.5f, 0.5f, 0);
            mSquare[stop_square.X][stop_square.Y].draw(mMVPMatrix, colours);
            colours = setColours(0, 0, 0.5f, 0);
            mSquare[start_square.X][start_square.Y].draw(mMVPMatrix, colours);

        }
        for (int i = 0; i < 32; i++) {
            if (cPiece[i].get_pieceType() != backend_pieceType.None) {
                if (cPiece[i].get_pieceColour() == backend_pieceColour.Black) {
                    colours = setColours(0f, 0f, 0f, 1);
                } else {
                    colours = setColours(1f, 1f, 1f, 1);
                }
                mPiece[i].draw(mMVPMatrix, colours);
            }

        }

        // Create a rotation for the triangle
//        long time = SystemClock.uptimeMillis() % 4000L;
//        float angle = 0.090f * ((int) time);
        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, -1.0f);

        // Combine the rotation matrix with the projection and camera view
        Matrix.multiplyMM(mMVPMatrix, 0, mRotationMatrix, 0, mMVPMatrix, 0);

        // Draw triangle
        //mPiece.draw(mMVPMatrix);


    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

    }

    public float[] setColours(float red, float green, float blue, float transparent) {
        float[] colours = {0, 0, 0, 0};
        colours[0] = red;
        colours[1] = green;
        colours[2] = blue;
        colours[3] = transparent;
        return colours;
    }

    public static int loadShader(int type, String shaderCode) {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    /**
     * Utility method for debugging OpenGL calls. Provide the name of the call
     * just after making it:
     * <p/>
     * <pre>
     * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
     * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
     *
     * If the operation is not successful, the check throws an error.
     *
     * @param glOperation - Name of the OpenGL call to check.
     */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }
}

class Triangle {

    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +

                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    "  gl_Position = vPosition * uMVPMatrix;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private final FloatBuffer vertexBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float triangleCoords[] = { // in counterclockwise order:
            0.0f, 0.622008459f, 0.0f,   // top
            -0.5f, -0.311004243f, 0.0f,   // bottom left
            0.5f, -0.311004243f, 0.0f    // bottom right
    };
    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex


    public Triangle(float triangleCoords[]) {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                triangleCoords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(triangleCoords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);

        // prepare shaders and OpenGL program
        int vertexShader = MyGL20Renderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGL20Renderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables

    }

    public void draw(float[] mvpMatrix, float[] colour) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, colour, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        MyGL20Renderer.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        MyGL20Renderer.checkGlError("glUniformMatrix4fv");

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}

class Square {

    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +

                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    "  gl_Position = vPosition * uMVPMatrix;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private final FloatBuffer vertexBuffer;
    private final ShortBuffer drawListBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;

    private final short drawOrder[] = {0, 1, 2, 0, 2, 3}; // order to draw vertices

    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex


    public Square(float squareCoords[]) {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        // prepare shaders and OpenGL program
        int vertexShader = MyGL20Renderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGL20Renderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
    }

    public void draw(float[] mvpMatrix, float colour[]) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, colour, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        MyGL20Renderer.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        MyGL20Renderer.checkGlError("glUniformMatrix4fv");

        // Draw the square
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}

class Piece {

    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +

                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    "  gl_Position = vPosition * uMVPMatrix;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private final FloatBuffer vertexBuffer;
    private final ShortBuffer drawListBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;

    private final short drawOrder[];
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    private final float pieceCoords[];

    public Piece(float[] centreCoords, backend_piece piece) {

        pieceCoords = getPieceCoords(centreCoords, piece);
        drawOrder = getPieceOrder(piece);
        // initialize vertex byte buffer for shape coordinates

        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                pieceCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(pieceCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        // prepare shaders and OpenGL program
        int vertexShader = MyGL20Renderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGL20Renderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
    }

    public void draw(float[] mvpMatrix, float colour[]) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, colour, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        MyGL20Renderer.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        MyGL20Renderer.checkGlError("glUniformMatrix4fv");

        // Draw the square
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }

    public float[] getPieceCoords(float centreCoord[], backend_piece piece) {
        float pieceCoord[];
        if (piece.get_pieceType() == backend_pieceType.Pawn) {
            final float pawnCoords[] = {
                    0f, 0f, 0f,//0 center
                    -0.1f, -0.1f, 0f,//1 bot left
                    0.06f, 0.02f, 0f,//2
                    0.08f, 0.04f, 0f,//3
                    0.06f, 0.06f, 0f,//4
                    0f, 0.1f, 0f,//5
                    -0.06f, 0.06f, 0f,//6
                    -0.08f, 0.04f, 0f,//7
                    -0.06f, 0.02f, 0f,//8
                    0.1f, -0.1f, 0f};//9
            pieceCoord = pawnCoords.clone();
        } else if (piece.get_pieceType() == backend_pieceType.Rook) {
            final float rookCoords[] = {
                    0f, 0f, 0f,
                    -0.125f, -0.125f, 0f,//1 bot left
                    0.125f, -0.125f, 0f,// 2 bot right
                    0.0625f, -0.0625f, 0f,//3
                    0.0625f, 0.025f, 0f,//4
                    0.125f, 0.075f, 0f,//5
                    0.125f, 0.125f, 0f,//6
                    0.075f, 0.125f, 0f,//7
                    0.075f, 0.075f, 0f,//8
                    0.025f, 0.075f, 0f,//9
                    0.025f, 0.125f, 0f,//10
                    -0.025f, 0.125f, 0f,//11
                    -0.025f, 0.075f, 0f,//12
                    -0.075f, 0.075f, 0f,//13
                    -0.075f, 0.125f, 0f,//14
                    -0.125f, 0.125f, 0f,//15
                    -0.125f, 0.075f, 0f,//16
                    -0.0625f, 0.025f, 0f,//17
                    -0.0625f, -0.0625f, 0f,//18
            };//9
            pieceCoord = rookCoords.clone();
        } else if (piece.get_pieceType() == backend_pieceType.Knight) {
            final float knightCoords[] = {
                    0f, 0f, 0f,//0 center
                    0.125f, -0.125f, 0f,// 1 bot right
                    0.05f, -0.05f, 0f,// 2 bot right
                    0.08f, 0f, 0f,//3
                    0.05f, 0.05f, 0f,//4
                    0.1f, 0.05f, 0f,//5
                    0.125f, 0.075f, 0f,//6
                    0.1f, 0.1f, 0f,//7
                    0.05f, 0.1f, 0f,//8
                    0.05f, 0.125f, 0f,//9
                    0f, 0.1f, 0f,//10
                    -0.033f, 0.087f, 0f,//11
                    -0.05f, 0.075f, 0f,//12
                    -0.03f, 0.025f, 0f,//13
                    -0.05f, -0.05f, 0f,//14 bot right
                    -0.125f, -0.125f, 0f};//15 bot left};//9

            pieceCoord = knightCoords.clone();
        } else if (piece.get_pieceType() == backend_pieceType.Bishop) {
            final float bishopCoords[] = {
                    0f, 0f, 0f,//0 center
                    0.125f, -0.125f, 0f,//1 bot right
                    0.0625f, 0f, 0f,//2
                    0.08f, 0f, 0f,//3
                    0.08f, 0.025f, 0f,  //4
                    0.0625f, 0.025f, 0f,//5
                    0.08f, 0.075f, 0f,//6
                    0.0625f, 0.1f, 0f,//7
                    0f, 0.125f, 0f,//8
                    -0.0425f, 0.12f, 0f,//9
                    -0.0025f, 0.06f, 0f,//11
                    -0.0225f, 0.04f, 0f,//10
                    -0.0625f, 0.1f, 0f,//12
                    -0.08f, 0.055f, 0f,//13
                    -0.0625f, 0.025f, 0f,//14
                    -0.08f, 0.025f, 0f,//15
                    -0.08f, 0f, 0f,//16
                    -0.0625f, 0f, 0f,//17
                    -0.125f, -0.125f, 0f};//18 bot left
            pieceCoord = bishopCoords.clone();
        } else if (piece.get_pieceType() == backend_pieceType.Queen) {
            final float queenCoords[] = {
                    0f, 0f, 0f,
                    -0.125f, -0.125f, 0f,//1 bot left
                    0.125f, -0.125f, 0f,// 2 bot right
                    0.0625f, -0.0625f, 0f,//3
                    0.0625f, 0.025f, 0f,//4
                    0.125f, 0.075f, 0f,//5
                    0.1f, 0.125f, 0f,//6
                    0.075f, 0.075f, 0f,//7
                    0.05f, 0.125f, 0f,//8
                    0.025f, 0.075f, 0f,//9
                    0.0f, 0.125f, 0f,//10
                    -0.025f, 0.075f, 0f,//11
                    -0.05f, 0.125f, 0f,//12
                    -0.075f, 0.075f, 0f,//13
                    -0.1f, 0.125f, 0f,//14
                    -0.125f, 0.075f, 0f,//15
                    -0.0625f, 0.025f, 0f,//16
                    -0.0625f, -0.0625f, 0f,//17
            };//9
            pieceCoord = queenCoords.clone();
        } else {
            final float kingCoords[] = {
                    0f, 0f, 0f,//0
                    0.125f, -0.125f, 0f,// 1 bot right
                    0.021f, 0f, 0f,//2
                    0.021f, 0.03125f, 0f,//3
                    0.0625f, 0.03125f, 0f,//4
                    0.0625f, 0.0625f, 0f,//5
                    0.021f, 0.0625f, 0f,//6
                    0.021f, 0.125f, 0f,//7
                    -0.021f, 0.125f, 0f,//8
                    -0.021f, 0.0625f, 0f,//9
                    -0.0625f, 0.0625f, 0f,//10
                    -0.0625f, 0.03125f, 0f,//11
                    -0.021f, 0.03125f, 0f,//12
                    -0.021f, 0f, 0f,//13
                    -0.125f, -0.125f, 0f,//14 bot left
            };
            pieceCoord = kingCoords.clone();
        }
        for (int i = 0; i < pieceCoord.length; i = i + 3) {
            pieceCoord[i] += centreCoord[0];
            if (piece.get_pieceColour() == backend_pieceColour.Black) {
                pieceCoord[i + 1] = centreCoord[1] - pieceCoord[i + 1];
            } else {
                pieceCoord[i + 1] = centreCoord[1] + pieceCoord[i + 1];
            }
            pieceCoord[i + 2] += centreCoord[2];
        }
        return pieceCoord;
    }

    public short[] getPieceOrder(backend_piece piece) {

        final short pieceOrder[];
        if (piece.get_pieceType() == backend_pieceType.Pawn) {
            final short pawnOrder[] = {0, 9, 1, 0, 1, 2, 0, 2, 4, 4, 2, 3, 0, 4, 5, 0, 5, 6, 0, 6, 8, 8, 6, 7, 0, 8, 9};
            pieceOrder = pawnOrder.clone();
        } else if (piece.get_pieceType() == backend_pieceType.Rook) {
            final short rookOrder[] = {0, 1, 2, 0, 2, 3, 0, 3, 4, 0, 4, 5, 8, 5, 6, 8, 6, 7, 12, 9, 10, 12, 10, 11, 16, 13, 14, 16, 14, 15, 0, 16, 17, 0, 17, 18, 0, 18, 1, 0, 5, 16};
            pieceOrder = rookOrder.clone();
        } else if (piece.get_pieceType() == backend_pieceType.Knight) {
            final short knightOrder[] = {0, 15, 1, 0, 1, 2, 0, 2, 3, 0, 3, 4, 4, 5, 7, 4, 7, 8, 5, 6, 7, 10, 8, 9, 10, 4, 8, 10, 11, 4, 11, 12, 4, 0, 4, 12, 0, 12, 13, 0, 13, 14, 0, 14, 15};
            pieceOrder = knightOrder.clone();
        } else if (piece.get_pieceType() == backend_pieceType.Bishop) {
            final short bishopOrder[] = {0, 18, 1, 0, 1, 2, 16, 3, 4, 16, 4, 15, 14, 5, 11, 11, 5, 10, 10, 5, 6, 10, 6, 7, 10, 7, 8, 10, 8, 9, 13, 14, 11, 13, 11, 12, 18, 0, 17};
            pieceOrder = bishopOrder.clone();
        } else if (piece.get_pieceType() == backend_pieceType.Queen) {
            final short queenOrder[] = {0, 1, 2, 0, 2, 3, 0, 3, 4, 0, 4, 5, 7, 5, 6, 9, 7, 8, 11, 9, 10, 13, 11, 12, 15, 13, 14, 0, 5, 15, 0, 15, 16, 0, 16, 17, 0, 17, 1, 0, 5, 16};
            pieceOrder = queenOrder.clone();
        } else {
            final short kingOrder[] = {0, 14, 1, 0, 1, 2, 0, 13, 14, 13, 2, 3, 13, 3, 12, 3, 4, 5, 3, 5, 6, 12, 3, 6, 12, 6, 9, 11, 12, 9, 11, 9, 10, 9, 6, 7, 9, 7, 8};
            pieceOrder = kingOrder.clone();
        }
        return pieceOrder;
    }

}

