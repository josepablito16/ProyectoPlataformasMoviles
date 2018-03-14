package com.cracks.proyectoplataformasmoviles;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Random;

public class Matriz extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matriz);

        NumberPicker filasNP = (NumberPicker) findViewById(R.id.filasNP);
        NumberPicker columnasNP = (NumberPicker) findViewById(R.id.columnasNP);

        filasNP.setMinValue(2);
        filasNP.setMaxValue(6);
        filasNP.setWrapSelectorWheel(true);

        generateCode();

        columnasNP.setMinValue(1);
        columnasNP.setMaxValue(3);
        columnasNP.setWrapSelectorWheel(true);

        final Button generarBtn = findViewById(R.id.generar_btn);
        final TextView codigoTV = findViewById(R.id.codigo_tv);

        generarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codigoTV.setText(generateCode());
            }
        });


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
