package com.cracks.proyectoplataformasmoviles;

import android.widget.ImageView;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Rodrigo Zea on 3/19/2018.
 */

@IgnoreExtraProperties
public class Cuarto {
    private String randomCode;
    private int filasT, columnasT, posicionX, posicionY;
    private String imagen;


    public Cuarto(){

    }

    public Cuarto(int filas, int columnas, int posX, int posY,  String img)
    {
        filasT = filas;
        columnasT = columnas;
        posicionX = posX;
        posicionY = posY;
        imagen = img;
    }

    public Cuarto(int posX, int posY)
    {
        posicionX = posX;
        posicionY = posY;

    }

    public String getRandomCode() {
        return randomCode;
    }

    public void setRandomCode(String randomCode) {
        this.randomCode = randomCode;
    }

    public int getFilasT() {
        return filasT;
    }

    public void setFilasT(int filasT) {
        this.filasT = filasT;
    }

    public int getColumnasT() {
        return columnasT;
    }

    public void setColumnasT(int columnasT) {
        this.columnasT = columnasT;
    }

    public int getPosicionX() {
        return posicionX;
    }

    public void setPosicionX(int posicionX) {
        this.posicionX = posicionX;
    }

    public int getPosicionY() {
        return posicionY;
    }

    public void setPosicionY(int posicionY) {
        this.posicionY = posicionY;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
