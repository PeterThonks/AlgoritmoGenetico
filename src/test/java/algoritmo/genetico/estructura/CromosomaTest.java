package algoritmo.genetico.estructura;

import algoritmo.genetico.auxiliar.Lector;
import algoritmo.shared.util.Constante;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CromosomaTest {
    private Cromosoma l;
    private List<Gen> cols;

    @Test
    public void existenciaCromosoma(){
        cols = new ArrayList<>();
        cols.add(new Gen("idAlumno", 1, 1, 4));
        cols.add(new Gen("nombre", 1, 2, 150));
        cols.add(new Gen("apellido", 1, 3, 150));
        cols.add(new Gen("codigoPUCP", 1, 4, 4));
        cols.add(new Gen("idAlumno", 7, 2, 4));
        cols.add(new Gen("nota", 7, 5, 8));
        l = new Cromosoma("SELECT \n" +
                "    Products.ProductID,\n" +
                "    Products.ProductName \n" +
                "FROM Products \n" +
                "WHERE Products.Discontinued=0;", cols);
        assertEquals(0, l.getEspacio());
        assertEquals(1000000000, l.getTiempoEjecucion());
        assertEquals(null, l.getCreateIndexSyntax());
        assertEquals(cols, l.getColumnas());
        assertEquals(null, l.getColumnasSeleccionadas());
    }

    @Test
    public void existenciaConformidadCromosoma(){
        l = new Cromosoma("SELECT \n" +
                "    Products.ProductID,\n" +
                "    Products.ProductName \n" +
                "FROM Products \n" +
                "WHERE Products.Discontinued=0;", null);
    }

    @Test
    public void existenciaCrearCromosomaNoIndexado(){
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        do {
            l = new Cromosoma(lector.getQuerys(), lector.getColumnasQuery());
            l.crearCromosomaNoIndexado();
        } while (!l.esValido(lector.getTablas(), lector.getEspacioTablasQuerys(), 0.9f));
    }

    @Test
    public void existenciaMúltiplesEntradasCrearCromosomaPonderado(){
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        do {
            l = new Cromosoma(lector.getQuerys(), lector.getColumnasQuery());
            l.crearCromosomaPonderado(lector.getColumnaMasComunPorTabla());
        } while (!l.esValido(lector.getTablas(), lector.getEspacioTablasQuerys(), 0.9f));
    }

    @Test
    public void existenciaConfomidadCrearCromosomaPonderado(){
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        l = new Cromosoma(lector.getQuerys(), lector.getColumnasQuery());
        l.crearCromosomaPonderado(null);
    }

    @Test
    public void existenciaMúltiplesEntradasCrearCromosomaMitad(){
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        do {
            l = new Cromosoma(lector.getQuerys(), lector.getColumnasQuery());
            l.crearCromosomaMitad(lector.getTablas());
        } while (!l.esValido(lector.getTablas(), lector.getEspacioTablasQuerys(), 0.9f));
    }

    @Test
    public void existenciaConfomidadCrearCromosomaMitad(){
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        l = new Cromosoma(lector.getQuerys(), lector.getColumnasQuery());
        l.crearCromosomaMitad(null);
    }

    @Test
    public void existenciaCrearCromosomaNoIndexadoMutado(){
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        do {
            l = new Cromosoma(lector.getQuerys(), lector.getColumnasQuery());
            l.crearCromosomaNoIndexadoMutado();
        } while (!l.esValido(lector.getTablas(), lector.getEspacioTablasQuerys(), 0.9f));
    }

    @Test
    public void existenciaMúltiplesEntradasCrearCromosomaPonderadoMutado(){
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        do {
            l = new Cromosoma(lector.getQuerys(), lector.getColumnasQuery());
            l.crearCromosomaPonderadoMutado(lector.getColumnaMasComunPorTabla());
        } while (!l.esValido(lector.getTablas(), lector.getEspacioTablasQuerys(), 0.9f));
    }

    @Test
    public void existenciaConfomidadCrearCromosomaPonderadoMutado(){
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        l = new Cromosoma(lector.getQuerys(), lector.getColumnasQuery());
        l.crearCromosomaPonderadoMutado(null);
    }

    @Test
    public void existenciaMúltiplesEntradasCrearCromosomaMitadMutado(){
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        do {
            l = new Cromosoma(lector.getQuerys(), lector.getColumnasQuery());
            l.crearCromosomaMitadMutado(lector.getTablas());
        } while (!l.esValido(lector.getTablas(), lector.getEspacioTablasQuerys(), 0.9f));
    }

    @Test
    public void existenciaConfomidadCrearCromosomaMitadMutado(){
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        l = new Cromosoma(lector.getQuerys(), lector.getColumnasQuery());
        l.crearCromosomaMitadMutado(null);
    }

    @Test
    public void existenciaMúltiplesEntradasEspacio(){
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        do {
            l = new Cromosoma(lector.getQuerys(), lector.getColumnasQuery());
            l.crearCromosomaNoIndexado();
        } while (!l.esValido(lector.getTablas(), lector.getEspacioTablasQuerys(), 0.9f));
        l.setEspacio(lector.getTablas());
        System.out.println(l.getEspacio());
    }

    @Test
    public void existenciaConformidadEspacio(){
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        l = new Cromosoma(lector.getQuerys(), lector.getColumnasQuery());
        l.setEspacio(null);
    }

    @Test
    public void existenciaMúltiplesEntradasTiempo(){
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        do {
            l = new Cromosoma(lector.getQuerys(), lector.getColumnasQuery());
            l.crearCromosomaNoIndexado();
        } while (!l.esValido(lector.getTablas(), lector.getEspacioTablasQuerys(), 0.9f));
        l.setTiempoEjecucion(lector.getTablas());
        System.out.println(l.getTiempoEjecucion());
    }

    @Test
    public void existenciaConformidadTiempo(){
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        l = new Cromosoma(lector.getQuerys(), lector.getColumnasQuery());
        l.setTiempoEjecucion(null);
    }

    @Test
    public void existenciaMúltiplesEntradasPenalidad(){
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        do {
            l = new Cromosoma(lector.getQuerys(), lector.getColumnasQuery());
            l.crearCromosomaNoIndexado();
        } while (!l.esValido(lector.getTablas(), lector.getEspacioTablasQuerys(), 0.9f));
        l.setPenalidadTotal();
        System.out.println(l.getPenalidadTotal());
    }

    @Test
    public void existenciaConformidadPenalidad(){
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        l = new Cromosoma(lector.getQuerys(), lector.getColumnasQuery());
        l.setPenalidadTotal();
    }

    @Test
    public void existenciaFitness(){
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        do {
            l = new Cromosoma(lector.getQuerys(), lector.getColumnasQuery());
            l.crearCromosomaNoIndexado();
        } while (!l.esValido(lector.getTablas(), lector.getEspacioTablasQuerys(), 0.9f));
        l.setFitness(lector.getTablas());
        System.out.println(l.getFitness());
    }

    @Test
    public void existenciaSeleccionarColumnas(){
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        do {
            l = new Cromosoma(lector.getQuerys(), lector.getColumnasQuery());
            l.crearCromosomaNoIndexado();
        } while (!l.esValido(lector.getTablas(), lector.getEspacioTablasQuerys(), 0.9f));
        l.setColumnasSeleccionadas();
        for (Gen c : l.getColumnasSeleccionadas()){
            c.printColumna();
        }
    }

    @Test
    public void existenciaMúltiplesEntradasCruzar(){
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        Cromosoma padre1, padre2;
        do {
            padre1 = new Cromosoma(lector.getQuerys(), lector.getColumnasQuery());
            padre1.crearCromosomaNoIndexado();
        } while (!padre1.esValido(lector.getTablas(), lector.getEspacioTablasQuerys(), 0.9f));
        do {
            padre2 = new Cromosoma(lector.getQuerys(), lector.getColumnasQuery());
            padre2.crearCromosomaPonderado(lector.getColumnaMasComunPorTabla());
        } while (!padre2.esValido(lector.getTablas(), lector.getEspacioTablasQuerys(), 0.9f));
        padre1.cruzar(padre2);
    }

    @Test
    public void existenciaConformidadCruzar(){
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        Cromosoma padre1;
        do {
            padre1 = new Cromosoma(lector.getQuerys(), lector.getColumnasQuery());
            padre1.crearCromosomaNoIndexado();
        } while (!padre1.esValido(lector.getTablas(), lector.getEspacioTablasQuerys(), 0.9f));
        padre1.cruzar(null);
    }

    @Test
    public void existenciaMutar(){
        Lector lector = new Lector(Constante.PATH_INPUT_CSV + "tablas_northwind_mysql.csv",
                Constante.PATH_INPUT_CSV + "columnas_northwind_mysql.csv",
                Path.of(Constante.PATH_INPUT_CSV + "query_northwind.sql"));
        lector.leerArchivos();
        Cromosoma padre1;
        do {
            padre1 = new Cromosoma(lector.getQuerys(), lector.getColumnasQuery());
            padre1.crearCromosomaNoIndexado();
        } while (!padre1.esValido(lector.getTablas(), lector.getEspacioTablasQuerys(), 0.9f));
        padre1.mutar();
    }
}
