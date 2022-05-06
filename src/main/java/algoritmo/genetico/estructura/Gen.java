package algoritmo.genetico.estructura;

import algoritmo.shared.util.Constante;
import org.javatuples.Pair;

import java.security.InvalidParameterException;

public class Gen {
    private String nombreColumna;
    private Pair<Integer, Integer> tupla;
    private long cantidadBytes;
    private byte probabilidadEleccion;
    private byte muta;

    public Gen(String nombreColumna, int tabla, int columna, long cantidadBytes) {
        if (nombreColumna == null)
            throw new InvalidParameterException(Constante.INVALID_PARAMETER_MSG);
        if (tabla < 0 || columna < 0 || cantidadBytes < 0)
            throw new InvalidParameterException(Constante.NEGATIVE_PARAMETER_MSG);

        this.nombreColumna = nombreColumna;
        this.tupla = new Pair<>(tabla,columna);
        this.cantidadBytes = cantidadBytes;
        this.probabilidadEleccion = 0;
        this.muta = 1;
    }

    public Gen(Gen otro) {
        this.nombreColumna = otro.getNombreColumna();
        this.tupla = otro.getTupla();
        this.cantidadBytes = otro.getCantidadBytes();
        this.probabilidadEleccion = otro.getProbabilidadEleccion();
        this.muta = otro.getMuta();
    }

    public String getNombreColumna() {
        return nombreColumna;
    }

    public void setNombreColumna(String nombreColumna) {
        this.nombreColumna = nombreColumna;
    }

    public int getTuplaTabla() {
        return tupla.getValue0();
    }

    public int getTuplaColumna() {
        return tupla.getValue1();
    }

    public Pair<Integer, Integer> getTupla() {
        return tupla;
    }

    public void setTupla(Pair<Integer, Integer> tupla) {
        this.tupla = tupla;
    }

    public long getCantidadBytes() {
        return cantidadBytes;
    }

    public void setCantidadBytes(long cantidadBytes) {
        this.cantidadBytes = cantidadBytes;
    }

    public byte getProbabilidadEleccion() {
        return probabilidadEleccion;
    }

    public void setProbabilidadEleccion(byte probabilidadEleccion) {
        this.probabilidadEleccion = probabilidadEleccion;
    }

    public byte getMuta() {
        return muta;
    }

    public void setMuta(byte muta) {
        this.muta = muta;
    }

    public void printColumna(){
        System.out.println("Columna " + this.nombreColumna + " n° " + this.tupla + " con cantidad de bytes " + this.cantidadBytes
                + "y probabilidad de elección de " + this.probabilidadEleccion);
    }
}
