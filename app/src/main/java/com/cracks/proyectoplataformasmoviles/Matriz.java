package com.cracks.proyectoplataformasmoviles;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.NumberPicker;

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

        columnasNP.setMinValue(1);
        columnasNP.setMaxValue(3);
        columnasNP.setWrapSelectorWheel(true);


    }
}
