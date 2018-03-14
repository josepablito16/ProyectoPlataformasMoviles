package com.cracks.proyectoplataformasmoviles;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Configuracion extends AppCompatActivity {
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    ImageView foto_gallery;
    Button boton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        String correo = getIntent().getStringExtra("usuario");
        String usuario = correo.substring(0,correo.indexOf("@"));

        NumberPicker np = (NumberPicker) findViewById(R.id.np);

        int minimo = 1;
        int maximo = 10;

        np.setMinValue(minimo);
        np.setMaxValue(maximo);
        np.setWrapSelectorWheel(true);

        TextView nombreUsuario = findViewById(R.id.usuarioTV);
        nombreUsuario.setText("¡Bienvenido " + usuario + "!");

        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){

            }
        });

        foto_gallery = (ImageView)findViewById(R.id.imageView);
        boton = (Button)findViewById(R.id.button2);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
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
}
