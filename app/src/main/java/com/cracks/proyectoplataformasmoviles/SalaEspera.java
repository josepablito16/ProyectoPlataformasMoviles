package com.cracks.proyectoplataformasmoviles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SalaEspera extends AppCompatActivity {
    private DatabaseReference mDatabase;

    int filas;
    int columnas;
    int posX;
    int posY;
    String roomName;
    String img;

    //Variables sin actualizar que el usuario va a desplegar:
    int posicionX;
    int posicionY;
    boolean control = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sala_espera);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        final Button startBtn = findViewById(R.id.StartEsclavo);

        filas = getIntent().getIntExtra("fila", 1);
        columnas = getIntent().getIntExtra("columna", 1);
        posX = getIntent().getIntExtra("posX", 1);
        posY = getIntent().getIntExtra("posY", 1);
        roomName = getIntent().getStringExtra("cuarto");
        img = getIntent().getStringExtra("img");

        posicionX = posX;
        posicionY = posY;

        if ((posX > 0) && (posY > 0)) {

            if (posX != filas || posY != columnas) {

                if (posY == 1) {

                    posX--;

                    if (posX != 1) {

                        posY = columnas;
                    }

                } else {
                    posY--;
                }

            } else {

                posY--;
            }

        } else {

            control = false;

        }

        if (control) {

            actualizarCuarto(roomName, filas, columnas, posX, posY, img);

            Intent intent = new Intent(SalaEspera.this, Display.class);
            intent.putExtra("columna", (int) columnas);
            intent.putExtra("fila", (int) filas);
            intent.putExtra("posX", (int) posicionX);
            intent.putExtra("posY", (int) posicionY);
            intent.putExtra("img", img);

            startActivityForResult(intent, 1);

        } else {

            Toast.makeText(this, "There's no more space in this room", Toast.LENGTH_SHORT).show();

        }

        startBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                if ((posX > 0) || (posY > 0)) {
//
//                    if (posX != filas || posY != columnas) {
//
//                        if (posY == 1) {
//
//                            posX--;
//
//                            if (posX != 1) {
//
//                                posY = columnas;
//                            }
//
//                        } else {
//                            posY--;
//                        }
//
//                    } else {
//
//                        posY--;
//                    }
//                }
//
//                actualizarCuarto(roomName, filas, columnas, posX,posY, img);
//
//                Intent intent = new Intent(SalaEspera.this, Display.class);
//                intent.putExtra("columna", (int) columnas);
//                intent.putExtra("fila", (int) filas);
//                intent.putExtra("posX", (int) posicionX);
//                intent.putExtra("posY", (int) posicionY);
//                intent.putExtra("img", img);
//
//                startActivityForResult(intent, 1);

            }

        });

    }

    public void asignarPosicion(int filas, int columnas, int posX, int posY){

        if ((posX-1 > 0) || (posY-1 > 0)) {

            if (posX != filas || posY != columnas) {

                if (posY == 1) {

                    posX--;

                    if (posX != 1) {

                        posY = columnas;
                    }

                } else {
                    posY--;
                }

            } else {

                posY--;
            }
        }
    }

    public void intentVariables(int filas, int columnas, int posX, int posY){

        Intent intent = new Intent(SalaEspera.this, Display.class);
        intent.putExtra("columna", (int) columnas);
        intent.putExtra("fila", (int) filas);
        intent.putExtra("posX", (int) posX);
        intent.putExtra("posY", (int) posY);

        startActivityForResult(intent, 1);

    }

    private void actualizarCuarto(String roomName, int filasT, int columnasT, int posicionX, int posicionY, String img){

        Map<String,Object> taskMap = new HashMap<String, Object>();
        taskMap.put("posicionX",posicionX);
        mDatabase.child("Cuartos").child(roomName).updateChildren(taskMap);

        Map<String,Object> taskMap2 = new HashMap<String, Object>();
        taskMap2.put("posicionY",posicionY);
        mDatabase.child("Cuartos").child(roomName).updateChildren(taskMap2);

    }
}
