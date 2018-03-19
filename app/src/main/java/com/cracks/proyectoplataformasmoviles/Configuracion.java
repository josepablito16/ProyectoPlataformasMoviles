package com.cracks.proyectoplataformasmoviles;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Configuracion extends AppCompatActivity {
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    ImageView foto_gallery;
    Button boton;
    Button boton1;
    TextView number;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    public static final String FB_STORAGE_PATH="image/";
    public static final String FB_DATABASE_PATH="image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        String usuario = getIntent().getStringExtra("usuario");

        NumberPicker np = (NumberPicker) findViewById(R.id.np);

        int minimo = 1;
        int maximo = 10;

        np.setMinValue(minimo);
        np.setMaxValue(maximo);
        np.setWrapSelectorWheel(true);

        TextView nombreUsuario = findViewById(R.id.usuarioTV);
        nombreUsuario.setText("¡Bienvenido " + usuario + "!");

        foto_gallery = (ImageView)findViewById(R.id.imageView);
        boton = (Button)findViewById(R.id.btnImage);
        boton1 = (Button)findViewById(R.id.btnNext);
        number = (TextView)findViewById(R.id.txtNumber);

        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                Toast.makeText(getApplicationContext(), "" + newVal,Toast.LENGTH_LONG).show();
                number.setText("");
                number.setText("" + newVal);
            }
        });



        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AsyncTask<String, Integer, String> cargarImagen = new AsyncTask<String, Integer, String>() {

                    ProgressBar barraDeProgreso = findViewById(R.id.barraProgreso);
                    int progreso;

                    String estado = "";

                    @Override
                    protected void onPreExecute() {

                        boton.setClickable(false);
                        boton1.setClickable(false);
                        barraDeProgreso.setVisibility(View.VISIBLE);
                        progreso = 0;
                        Toast.makeText(getApplicationContext(),"Cargando imagen...",Toast.LENGTH_LONG).show();

                    }

                    @Override
                    protected String doInBackground(String... strings) {

                        //subir imagen a la base de datos

                        //obtiene la referencia del alamacenamiento
                        StorageReference ref=mStorageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + "."+getImageExt(imageUri));

                        // agregar archivo a referencia

                        ref.putFile(imageUri)

                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Toast.makeText(getApplicationContext(), "IMAGEN SUBIDA",Toast.LENGTH_LONG).show();

                                //guardar informacion de imagen en firebase

                                SubirImagen subirImagen= new SubirImagen("Nombre de la imagen",taskSnapshot.getDownloadUrl().toString());
                                String uploadId=mDatabaseRef.push().getKey();
                                mDatabaseRef.child(uploadId).setValue(subirImagen);

                                Intent intent = new Intent(Configuracion.this, Matriz.class);
                                intent.putExtra("persona", number.getText());
                                startActivityForResult(intent, 1);

                            }

                        }) .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                            }

                        })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                        while (progreso<100) {
                                            progreso = (int)((100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount());
                                            publishProgress(progreso);
                                            SystemClock.sleep(20);

                                        }

                                    }

                                });

                        return estado;

                    }

                    @Override
                    protected void onProgressUpdate(Integer... values) {
                        barraDeProgreso.setProgress(values[0]);
                    }

                    @Override
                    protected void onPostExecute(String s) {

                        boton.setClickable(true);
                        boton1.setClickable(true);
                        barraDeProgreso.setVisibility(View.VISIBLE);
                    }

                };

                cargarImagen.execute();
            }
        });

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });


        //STORAGE
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);


    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            foto_gallery.setImageURI(imageUri);
        }
    }

    public String getImageExt(Uri uri)
    {
        ContentResolver contentResolver =getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }




}
