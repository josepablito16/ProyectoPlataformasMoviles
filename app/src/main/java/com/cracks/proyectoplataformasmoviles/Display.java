package com.cracks.proyectoplataformasmoviles;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//import com.bumptech.glide.Glide;
//import com.squareup.picasso.Picasso;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Display extends AppCompatActivity {
    //private DatabaseReference mDatabase;

    int filas;
    int columnas;
    int posX;
    int posY;
    String img;


    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mVisible = true;

        filas = getIntent().getIntExtra("fila", 1);
        columnas = getIntent().getIntExtra("columna", 1);
        posX = getIntent().getIntExtra("posX", 1);
        posY = getIntent().getIntExtra("posY", 1);
        img = getIntent().getStringExtra("img");

        Toast.makeText(this, "PosX: " + posX + " PosY: " + posY, Toast.LENGTH_SHORT).show();

        getBitmapFromURL(filas, columnas, posX - 1, posY - 1, img);
    }

    /**
     * Thread que accede a obtener la imagen y desplegarla en la pantalla.
     */
    public void getBitmapFromURL(final int filas, final int columnas,final int posX,final int posY,final String img){

        AsyncTask<Void, Void, Bitmap> conseguirImg = new AsyncTask<Void, Void, Bitmap>() {

            protected Bitmap doInBackground(Void... params) {


                Bitmap myBitmap = null;

                try {

                    URL url = new URL(img);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    myBitmap = BitmapFactory.decodeStream(input);

                } catch (IOException e) {
                    e.printStackTrace();

                }

                return myBitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {

                final ImageView imagen = findViewById(R.id.display_IV);

                int height = bitmap.getHeight()/filas;
                int width = bitmap.getWidth()/columnas;

                bitmap = Bitmap.createBitmap(bitmap,posX*width,posY*height,width,height);
                imagen.setImageBitmap(Bitmap.createScaledBitmap(bitmap,imagen.getWidth(),imagen.getHeight(),true));

            }

        };

        conseguirImg.execute();

//        try {
//            Thread.sleep(10000);
//
//            if (conseguirImg.getStatus() == AsyncTask.Status.FINISHED) {
//                conseguirImg.cancel(true);
//            } else {
//                Toast.makeText(this, "No he terminado :(", Toast.LENGTH_LONG).show();
//            }
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }






        //        final ImageView imagen = findViewById(R.id.display_IV);
//
//        Bitmap bitmap = null;
//        try {
//            bitmap = new myTask().execute().get();
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//
//        int height = bitmap.getHeight()/filas;
//        int width = bitmap.getWidth()/columnas;
//
//        bitmap = Bitmap.createBitmap(bitmap,posX*width,posY*height,width,height);
//        imagen.setImageBitmap(Bitmap.createScaledBitmap(bitmap,imagen.getWidth(),imagen.getHeight(),true));


    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    private class myTask extends AsyncTask<Void,Void,Bitmap> implements Runnable{

        String img = getIntent().getStringExtra("img");

        protected Bitmap doInBackground(Void... params) {

            Bitmap myBitmap = null;

            try {

                URL url = new URL(img);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                myBitmap = BitmapFactory.decodeStream(input);

            } catch (IOException e) {
                e.printStackTrace();

            }

            return myBitmap;

        }

        @Override
        protected void onPostExecute(Bitmap result) {

            Thread.currentThread().interrupt();

        }

        @Override
        public void run() {

        }
    }

    public void ponerImagen1(){

        Toast.makeText(this, "EMPEZEMOS :)", Toast.LENGTH_LONG).show();

//        mDatabase = FirebaseDatabase.getInstance().getReference();
//
//        int filas = getIntent().getIntExtra("fila", 1);
//        int columnas = getIntent().getIntExtra("columna", 1);
//        int posX = getIntent().getIntExtra("posX", 1);
//        int posY = getIntent().getIntExtra("posY", 1);
//
//        String img = getIntent().getStringExtra("img");
//        String roomName = getIntent().getStringExtra("cuarto");
//
//        if ((posX > 0) && (posY > 0)) {
//
//            getBitmapFromURL(filas, columnas, posX - 1, posY - 1, img);
//
//            if (posX != filas && posY != columnas) {
//
//                if (posY == 1) {
//                    posX--;
//
//                    if (posX != 1) {
//
//                        posY = columnas;
//                    }
//                } else {
//                    posY--;
//                }
//
//            } else {
//
//                posX--;
//            }
//
//        }
//
//        actualizarCuarto(roomName, filas, columnas, posX, posY, img);

    }

    public void ponerImagen(){

        getBitmapFromURL(filas, columnas, posX, posY, img);
}

}

