package co.edu.udea.compumovil.gr06.shareit.UI.model;

/**
 * Created by jaime on 14/11/2016.
 */

public class Promedio {

    private int cantidad;
    private double suma;
    private double promedio;

    public Promedio() {
        this.cantidad = 0;
        this.suma = 0;
        this.promedio = 0;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void aumentarCantidad() {
        this.cantidad++;
    }

    public double getSuma() {
        return suma;
    }

    public void sumarMas(double suma) {
        this.suma = this.suma + suma;
    }

    public double getPromedio() {
        return promedio;
    }

    public void calPromedio() {
        this.promedio = this.suma / this.cantidad;
    }
}
