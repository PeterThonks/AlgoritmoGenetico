package algoritmo.genetico.principal;

import algoritmo.genetico.auxiliar.Lector;
import algoritmo.genetico.estructura.Cromosoma;
import algoritmo.shared.util.Constante;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PoblacionTest {
    private Poblacion p;

    @Test
    public void existenciaPoblacion(){
        p = new Poblacion(10, 5);
        assertEquals(5, p.getTamanoPoblacion());
    }

    @Test
    public void existenciaConformidadPoblacion(){
        p = new Poblacion(-1, -1);
    }

    @Test
    public void existenciaMúltiplesEntradasPoblacionInicial(){
        p = new Poblacion(10, 5);
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        p.crearPoblacionInicial(lector, 0.9f);
    }

    @Test
    public void existenciaConformidadPoblacionInicial(){
        p = new Poblacion(10, 5);
        p.crearPoblacionInicial(null, 0.9f);
    }

    @Test
    public void existenciaMúltiplesEntradasObtenerMejorSolucion(){
        p = new Poblacion(10, 5);
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        p.crearPoblacionInicial(lector, 0.9f);
        p.obtenerMejorSolucion(lector);
    }

    @Test
    public void existenciaConformidadObtenerMejorSolucion(){
        p = new Poblacion(10, 5);
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        p.crearPoblacionInicial(lector, 0.9f);
        p.obtenerMejorSolucion(null);
    }

    @Test
    public void existenciaMúltiplesEntradasCrearNuevaPoblacion(){
        p = new Poblacion(10, 5);
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        p.crearPoblacionInicial(lector, 0.9f);
        p.obtenerMejorSolucion(lector);
        List<Cromosoma> cromosomasNuevos = p.crearNuevaPoblacion(lector, 0.9f, lector.getEspacioTablasQuerys(), 0.15f, 0.85f);
    }

    @Test
    public void existenciaConformidadCrearNuevaPoblacion(){
        p = new Poblacion(10, 5);
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        p.crearPoblacionInicial(lector, 0.9f);
        p.obtenerMejorSolucion(lector);
        List<Cromosoma> cromosomasNuevos = p.crearNuevaPoblacion(lector, -0.9f, lector.getEspacioTablasQuerys(), -0.15f, -0.85f);
    }
}
