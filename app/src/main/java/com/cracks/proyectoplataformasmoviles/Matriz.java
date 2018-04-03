package com.cracks.proyectoplataformasmoviles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class Matriz extends AppCompatActivity {

private DatabaseReference mDatabase;
private int columnasT = 2, filasT = 1;
String img;
String genCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getSupportActionBar().hide();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matriz);

        final NumberPicker filasNP = (NumberPicker) findViewById(R.id.filasNP);
        final NumberPicker columnasNP = (NumberPicker) findViewById(R.id.columnasNP);
        img=getIntent().getStringExtra("url");
        filasNP.setMinValue(1);
        filasNP.setMaxValue(5);
        filasNP.setWrapSelectorWheel(true);

        generateCode();

        columnasNP.setMinValue(2);
        columnasNP.setMaxValue(5);
        columnasNP.setWrapSelectorWheel(true);

        final Button generarBtn = findViewById(R.id.generar_btn);
        final Button empezarBtn = findViewById(R.id.empezar_btn);
        final TextView codigoTV = findViewById(R.id.codigo_tv);


        columnasNP.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                columnasT = columnasNP.getValue();

            }
        });

        filasNP.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                filasT = filasNP.getValue();

            }
        });

        generarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                generarBtn.setClickable(false);
                generarBtn.setEnabled(false);

                genCode = generateCode();

                writeNewCuarto(genCode, filasT, columnasT, filasT, columnasT, img);
                codigoTV.setText(genCode);

                empezarBtn.setEnabled(true);

            }
        });

        empezarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("Los valores que mando son posX: " + columnasT + " posY: " + filasT + " img: " + img + " cuarto: " + codigoTV.getText().toString());

                Intent nuevoIntent = new Intent(Matriz.this, SalaEspera.class);
                nuevoIntent.putExtra("fila", filasT);
                nuevoIntent.putExtra("columna",columnasT);
                nuevoIntent.putExtra("posX",columnasT);
                nuevoIntent.putExtra("posY",filasT);
                nuevoIntent.putExtra("cuarto",codigoTV.getText().toString());
                nuevoIntent.putExtra("img",img.toString());
                startActivityForResult(nuevoIntent, 1);

            }
        });

    }

    private void writeNewCuarto(String roomName, int filasT, int columnasT, int posicionX, int posicionY, String img){
        Cuarto cuarto = new Cuarto(filasT, columnasT,posicionY ,posicionX, img);

        mDatabase.child("Cuartos").child(roomName).setValue(cuarto);
    }


    public String generateCode(){
        Random generator = new Random();

        String codigoGen = "";

        String alphaletters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String alphanum = "1234567890";

        for(int i = 0; i < 6; i++) {
            if(i < 3) {
                codigoGen += alphaletters.charAt(generator.nextInt(alphaletters.length()));
            }else{
                codigoGen += alphanum.charAt(generator.nextInt(alphanum.length()));
            }
        }
        

        return codigoGen;
    }
}
