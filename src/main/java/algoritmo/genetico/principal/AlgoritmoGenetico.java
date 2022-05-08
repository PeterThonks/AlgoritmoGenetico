package algoritmo.genetico.principal;

import algoritmo.genetico.auxiliar.Lector;
import algoritmo.genetico.estructura.Cromosoma;
import algoritmo.shared.util.Constante;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AlgoritmoGenetico {
    public static void main(String[] args) {
        String filenameTablas = "tablas_"+ Constante.DATABASE_SELECTED+"_mysql.csv",
                filenameColumnas = "columnas_"+Constante.DATABASE_SELECTED+"_mysql.csv",
                filenameQuery = "query_"+Constante.DATABASE_SELECTED+".sql";

        Lector lector = new Lector(Constante.PATH_INPUT_CSV + filenameTablas,
                Constante.PATH_INPUT_CSV + filenameColumnas,
                Path.of(Constante.PATH_INPUT_CSV + filenameQuery));
        lector.leerArchivos();

        int tamanoPoblacion = lector.getColumnasQuery().size() / 2, t = 1, maxIter = 100;
        float probMutacion = 0.15f, probCruzamiento = 0.85f, rh = 0.9f;
        long startTime, endTime;
        Cromosoma mejorAnterior, mejorActual;
        startTime = System.nanoTime();
        Poblacion poblacion = new Poblacion(tamanoPoblacion);
        poblacion.crearPoblacionInicial(lector, rh);
        poblacion.obtenerMejorSolucion(lector);
        mejorAnterior = poblacion.getMejorSolucion();
        while (t <= maxIter){
            System.out.println("Iteración n° "+t);
            List<Cromosoma> cromosomasNuevos = poblacion.crearNuevaPoblacion(lector, rh, lector.getEspacioTablasQuerys(), probMutacion, probCruzamiento);
            poblacion = new Poblacion(cromosomasNuevos, tamanoPoblacion);
            poblacion.obtenerMejorSolucion(lector);
            mejorActual = poblacion.getMejorSolucion();
            if (mejorActual.getTiempoEjecucion() < mejorAnterior.getTiempoEjecucion())
                mejorAnterior = mejorActual;
            t++;
        }
        System.out.println("Mejor solución");
        mejorAnterior.printSolucion();
        endTime = (System.nanoTime() - startTime);
        System.out.println("Tiempo total: " + TimeUnit.MINUTES.convert(endTime, TimeUnit.NANOSECONDS) + " minutos.");
    }
}
