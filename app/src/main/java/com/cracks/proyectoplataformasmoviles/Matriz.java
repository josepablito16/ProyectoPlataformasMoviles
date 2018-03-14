package com.cracks.proyectoplataformasmoviles;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.NumberPicker;

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

        System.out.println(codigoGen);

        return codigoGen;
    }
}
