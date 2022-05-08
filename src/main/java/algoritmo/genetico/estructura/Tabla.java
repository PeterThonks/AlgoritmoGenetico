package algoritmo.genetico.estructura;

import algoritmo.shared.util.Constante;

import java.security.InvalidParameterException;

public class Tabla {
    private String nombreTabla;
    private int numeroTabla;
    private int cantidadFilas;
    private double espacio;

    public Tabla(String nombreTabla, int numeroTabla, int cantidadFilas, double espacio) {
        if (nombreTabla == null )
            throw new InvalidParameterException(Constante.INVALID_PARAMETER_MSG);
        if (numeroTabla < 0 || cantidadFilas < 0 || espacio < 0)
            throw new InvalidParameterException(Constante.NEGATIVE_PARAMETER_MSG);

        this.nombreTabla = nombreTabla;
        this.numeroTabla = numeroTabla;
        this.cantidadFilas = cantidadFilas;
        this.espacio = espacio;
    }

    public String getNombreTabla() {
        return nombreTabla;
    }

    public void setNombreTabla(String nombreTabla) {
        this.nombreTabla = nombreTabla;
    }

    public int getNumeroTabla() {
        return numeroTabla;
    }

    public void setNumeroTabla(int numeroTabla) {
        this.numeroTabla = numeroTabla;
    }

    public int getCantidadFilas() {
        return cantidadFilas;
    }

    public void setCantidadFilas(int cantidadFilas) {
        this.cantidadFilas = cantidadFilas;
    }

    public double getEspacio() {
        return espacio;
    }

    public void setEspacio(double espacio) {
        this.espacio = espacio;
    }

    public void printTabla(){
        System.out.println("Tabla " + this.nombreTabla + " n° " + this.numeroTabla + " con "
                + this.cantidadFilas + " filas y " + this.espacio + " gb de espacio.");
    }
}
