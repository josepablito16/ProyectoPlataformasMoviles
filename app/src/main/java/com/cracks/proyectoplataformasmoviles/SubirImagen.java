package com.cracks.proyectoplataformasmoviles;

/**
 * Created by jose on 15/03/2018.
 */

public class SubirImagen
{
    public String nombre;
    public String url;

    public String getNombre() {
        return nombre;
    }

    public String getUrl() {
        return url;
    }

    public SubirImagen(String nombre, String url) {
        this.nombre = nombre;
        this.url = url;
    }
}
