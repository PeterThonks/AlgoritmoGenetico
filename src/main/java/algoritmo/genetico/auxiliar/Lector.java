package algoritmo.genetico.auxiliar;

import algoritmo.genetico.estructura.Gen;
import algoritmo.genetico.estructura.Tabla;
import algoritmo.shared.util.Constante;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Lector {
    private String rutaArchivoTablas;
    private String rutaArchivoColumnas;
    private Path rutaArchivoQuery;
    private List<Tabla> tablas;
    private List<Gen> columnas;
    private List<Gen> columnasQuery;
    private double espacioTablasQuerys;
    private List<Gen> columnaMasComunPorTabla;
    private String querys;

    public Lector(String rutaArchivoTablas, String rutaArchivoColumnas, Path rutaArchivoQuery) {
        if (rutaArchivoTablas == null || rutaArchivoColumnas == null || rutaArchivoQuery == null)
            throw new InvalidParameterException(Constante.INVALID_PARAMETER_MSG);
        this.rutaArchivoTablas = rutaArchivoTablas;
        this.rutaArchivoColumnas = rutaArchivoColumnas;
        this.rutaArchivoQuery = rutaArchivoQuery;
        this.tablas = new ArrayList<>();
        this.columnas = new ArrayList<>();
        this.columnaMasComunPorTabla = new ArrayList<>();
        this.setQuerys();
    }

    public String getRutaArchivoTablas() {
        return rutaArchivoTablas;
    }

    public String getRutaArchivoColumnas() {
        return rutaArchivoColumnas;
    }

    public Path getRutaArchivoQuery() {
        return rutaArchivoQuery;
    }

    public List<Tabla> getTablas() {
        return tablas;
    }

    public List<Gen> getColumnas() {
        return columnas;
    }

    public List<Gen> getColumnasQuery() {
        return columnasQuery;
    }

    public double getEspacioTablasQuerys() { return espacioTablasQuerys; }

    public List<Gen> getColumnaMasComunPorTabla() { return columnaMasComunPorTabla; }

    public String getQuerys() {
        return querys;
    }

    public void setQuerys() {
        String file = "";
        try{
            file = Files.readString(this.rutaArchivoQuery);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        this.querys = file.trim();
    }

    public void leerArchivos(){
        this.leerTablas();
        this.leerColumnas();
        this.setColumnasQuery();
        this.setEspacioTablasQueries();
        this.setColumnaMasComunPorTabla();
//        System.out.println("Tablas");
//        for (Tabla t : this.tablas){
//            t.printTabla();
//        }
//        System.out.println("Columnas");
//        for (Gen c : this.columnas){
//            c.printColumna();
//        }
//        System.out.println("Query: ");
//        System.out.println(this.querys);
//        System.out.println("Columnas del query");
//        for (Gen c : this.columnasQuery){
//            c.printColumna();
//        }
//        System.out.println("El espacio de la tablas del query es " + this.espacioTablasQuerys);
//        System.out.println("Columna mas comun por tabla");
//        for (Gen c : this.columnaMasComunPorTabla){
//            c.printColumna();
//        }
    }

    public void leerTablas(){
        String line = "";
        String splitBy = ",";
        try {
            //parsing a CSV file into BufferedReader class constructor
            BufferedReader br = new BufferedReader(new FileReader(this.rutaArchivoTablas));
            br.readLine(); //skipea la cabecera
            while ((line = br.readLine()) != null)
            //returns a Boolean value
            {
                String[] tabla = line.split(splitBy);
                //use comma as separator
                Tabla nuevaT = new Tabla(tabla[0], Integer.parseInt(tabla[1]), Integer.parseInt(tabla[2]), Double.parseDouble(tabla[3]));
                this.tablas.add(nuevaT);
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void leerColumnas(){
        String line = "";
        String splitBy = ",";
        try {
            //parsing a CSV file into BufferedReader class constructor
            BufferedReader br = new BufferedReader(new FileReader(this.rutaArchivoColumnas));
            br.readLine(); //skipea la cabecera
            while ((line = br.readLine()) != null)
            //returns a Boolean value
            {
                String[] columna = line.split(splitBy);
                //use comma as separator
                Gen nuevaC = new Gen(columna[0], Integer.parseInt(columna[1]), Integer.parseInt(columna[2]), Long.parseLong(columna[3]));
                this.columnas.add(nuevaC);
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void setColumnasQuery(){
        List<Gen> columnasQuery = new ArrayList<>();
        String[] querys = this.querys.split(";");
        for (int i = 0; i<querys.length; i++){
//            System.out.println(querys[i]);
            querys[i] = querys[i].trim().toLowerCase();
            String fromStatement = querys[i].substring(querys[i].indexOf("from"));
            try {
                Statement statement = CCJSqlParserUtil.parse(querys[i]);
                Select selectStatement = (Select) statement;
                TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
                List<String> tableList = tablesNamesFinder.getTableList(selectStatement);
                for (String tableName : tableList) {
                    Tabla tb = this.tablas.stream()
                            .filter(tabla -> tableName.equals(tabla.getNombreTabla().toLowerCase()))
                            .findAny()
                            .orElse(null);
                    int numeroTabla = tb.getNumeroTabla();
                    List<Gen> columnasAux = this.columnas.stream()
                            .filter(columna -> columna.getTuplaTabla() == numeroTabla)
                            .collect(Collectors.toList());
                    for (Gen col : columnasAux){
                        String nombreCol = tableName + "." + col.getNombreColumna().toLowerCase();
                        Gen columnaRepetida = columnasQuery.stream()
                                .filter(columna -> col.getTupla() == columna.getTupla())
                                .findAny()
                                .orElse(null);
                        if (querys[i].contains(nombreCol) && columnaRepetida == null){
//                            System.out.println(nombreCol);
                            Gen newCol = new Gen(col);
                            columnasQuery.add(newCol);
                        }
                    }

                }
            } catch (JSQLParserException e) {
                e.printStackTrace();
            }
        }
        this.columnasQuery = columnasQuery;
    }

    public void setEspacioTablasQueries() {
        double sum=0;
        for (Tabla t : this.tablas){
            List<Gen> aux = this.columnasQuery.stream()
                    .filter(columna -> t.getNumeroTabla() == columna.getTuplaTabla())
                    .collect(Collectors.toList());
            if (aux != null)
                sum += t.getEspacio();
        }
        this.espacioTablasQuerys = sum;
    }

    public void setColumnaMasComunPorTabla() {
        String nombreTabla, key;
        int count, index;

        for (Tabla t : this.tablas){
            List<Gen> columnasAux = this.columnasQuery.stream()
                    .filter(columna -> columna.getTuplaTabla() == t.getNumeroTabla())
                    .collect(Collectors.toList());
            if (columnasAux.size() > 0){
                int[] cantOcurrencias = new int[columnasAux.size()];
                for (int i=0; i<columnasAux.size(); i++){
                    nombreTabla = t.getNombreTabla();
                    key = nombreTabla + '.' + columnasAux.get(i).getNombreColumna();
                    //Buscar
                    count = 0;
                    index = 0;
                    while((index= this.querys.indexOf(key,index))!=-1){
                        index = index + key.length();
                        count++;
                    }
                    cantOcurrencias[i] = count;
                }
                int maxAt = 0;
                for (int i = 0; i < cantOcurrencias.length; i++) {
                    maxAt = cantOcurrencias[i] >= cantOcurrencias[maxAt] ? i : maxAt;
                }
                columnaMasComunPorTabla.add(new Gen(columnasAux.get(maxAt)));
            }
        }
    }
}
