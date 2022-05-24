package algoritmo.genetico.auxiliar;

import algoritmo.genetico.estructura.Gen;
import algoritmo.genetico.estructura.Tabla;
import algoritmo.shared.util.Constante;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LectorTest {
    private Lector l;

    @Test
    public void existenciaLector(){
        l = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV_TEST + "query_test.sql"));
        assertEquals(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv", l.getRutaArchivoTablas());
        assertEquals(Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv", l.getRutaArchivoColumnas());
        assertEquals(Path.of(Constante.PATH_INPUT_CSV_TEST + "query_test.sql"), l.getRutaArchivoQuery());
    }

    @Test
    public void existenciaConformidadLector(){
        l = new Lector(null,null,null);
    }

    @Test
    public void existenciaConformidadLeerTablas(){
        l = new Lector(Constante.PATH_INPUT_CSV_TEST + "tablas_test.csv",
                Constante.PATH_INPUT_CSV_TEST + "columnas_test.csv",
                Path.of(Constante.PATH_INPUT_CSV_TEST + "query_test.sql"));
        l.leerTablas();
    }

    @Test
    public void existenciaMultiplesEntradasLeerTablas(){
        l = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV_TEST + "query_test.sql"));
        l.leerTablas();
        for (Tabla t : l.getTablas()){
            t.printTabla();
        }
    }

    @Test
    public void existenciaConformidadLeerColumnas(){
        l = new Lector(Constante.PATH_INPUT_CSV_TEST + "tablas_test.csv",
                Constante.PATH_INPUT_CSV_TEST + "columnas_test.csv",
                Path.of(Constante.PATH_INPUT_CSV_TEST + "query_test.sql"));
        l.leerColumnas();
    }

    @Test
    public void existenciaMultiplesEntradasLeerColumnas(){
        l = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV_TEST + "query_test.sql"));
        l.leerColumnas();
        for (Gen c : l.getColumnas()){
            c.printColumna();
        }
    }

    @Test
    public void existenciaConformidadLeerColumnasQuery(){
        l = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV_TEST + "query_test.sql"));
        l.leerTablas();
        l.leerColumnas();
        l.setColumnasQuery();
    }

    @Test
    public void existenciaMultiplesEntradasLeerColumnasQuery(){
        l = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        l.leerTablas();
        l.leerColumnas();
        l.setColumnasQuery();
        for (Gen c : l.getColumnasQuery()){
            c.printColumna();
        }
    }

    @Test
    public void existenciaSetEspacioQueries(){
        l = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        l.leerTablas();
        l.leerColumnas();
        l.setColumnasQuery();
        l.setEspacioTablasQueries();
        System.out.println("El espacio de la tablas del query es " + l.getEspacioTablasQuerys());
    }

    @Test
    public void existenciaSetColumnasMasComunPorTabla(){
        l = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        l.leerTablas();
        l.leerColumnas();
        l.setColumnasQuery();
        l.setEspacioTablasQueries();
        l.setColumnaMasComunPorTabla();
        for (Gen c : l.getColumnaMasComunPorTabla()){
            c.printColumna();
        }
    }
}
