package com.cracks.proyectoplataformasmoviles;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
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

        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        String usuario = getIntent().getStringExtra("usuario");
        TextView nombreUsuario = findViewById(R.id.usuarioTV);
        nombreUsuario.setText("¡Bienvenido " + usuario + "!");

        foto_gallery = (ImageView)findViewById(R.id.imageView);
        boton = (Button)findViewById(R.id.btnImage);
        boton1 = (Button)findViewById(R.id.btnNext);

        boton1.setOnClickListener(new View.OnClickListener() {

            ProgressBar progressBar = findViewById(R.id.barraProgreso);

            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);
                boton.setClickable(false);
                boton1.setClickable(false);

                //subir imagen a la base de datos

                //obtiene la referencia del alamacenamiento
                StorageReference ref=mStorageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + "."+getImageExt(imageUri));

                // agregar archivo a referencia

                ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        //guardar informacion de imagen en firebase

                        String uploadId = mDatabaseRef.push().getKey();
                        //mDatabaseRef.child(uploadId).setValue(subirImagen);

                        Intent intent = new Intent(Configuracion.this, Matriz.class);
                        intent.putExtra("persona", number.getText());
                        intent.putExtra("url",taskSnapshot.getDownloadUrl().toString());
                        startActivityForResult(intent, 1);

                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(getApplicationContext(),"ERROR UPLOADING IMAGE",Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                boton.setClickable(true);
                                boton1.setClickable(true);

                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                double progress =(100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                                progressBar.setProgress((int) progress);

                            }
                        });
            }

        });

        boton.setOnClickListener(new View.OnClickListener() {

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
