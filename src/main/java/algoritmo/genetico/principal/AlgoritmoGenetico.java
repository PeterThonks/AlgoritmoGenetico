package algoritmo.genetico.principal;

import algoritmo.genetico.auxiliar.Lector;
import algoritmo.genetico.estructura.Cromosoma;
import algoritmo.genetico.estructura.Gen;
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

        int tamanoPoblacion = 30, tamanoPoblacionElitista = 8, t = 1, maxIter = 500, sinMejora = 0;
        float probMutacion = 0.15f, probCruzamiento = 0.85f, rh = 0.9f, Ht = 0.000001f;
        long startTime, endTime;
        List<Cromosoma> mejorAnterior, mejorActual;
        startTime = System.nanoTime();
        Poblacion poblacion = new Poblacion(tamanoPoblacion, tamanoPoblacionElitista);
        poblacion.crearPoblacionInicial(lector, rh);
        poblacion.obtenerMejorSolucion(lector);
        mejorAnterior = poblacion.getMejorSolucion();
        while (t <= maxIter || mejorAnterior.get(0).getFitness() > Ht){
            System.out.println("Iteración n° "+t);
            if (sinMejora == 100)
                break;
            System.out.println("Sin mejora n° "+sinMejora);
            List<Cromosoma> cromosomasNuevos = poblacion.crearNuevaPoblacion(lector, rh, lector.getEspacioTablasQuerys(), probMutacion, probCruzamiento);
            poblacion = new Poblacion(cromosomasNuevos, tamanoPoblacion);
            poblacion.obtenerMejorSolucion(lector);
            mejorActual = poblacion.getMejorSolucion();
            if (mejorActual.get(0).getFitness() < mejorAnterior.get(0).getFitness()){
                mejorAnterior = mejorActual;
                sinMejora = 0;
            }
            else
                sinMejora++;
            t++;
        }
        System.out.println("Mejor solución");
        for (Cromosoma c : mejorAnterior){
            System.out.println("Cromosoma");
            c.printSolucion();
        }
        endTime = (System.nanoTime() - startTime);
        System.out.println("Tiempo total: " + TimeUnit.MINUTES.convert(endTime, TimeUnit.NANOSECONDS) + " minutos.");
    }
}
