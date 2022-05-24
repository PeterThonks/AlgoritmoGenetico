package algoritmo.genetico.estructura;

import algoritmo.shared.util.Constante;
import org.javatuples.Pair;

import java.security.InvalidParameterException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Cromosoma implements Comparable<Cromosoma> {
    private String querys;
    private double tiempoEjecucion;
    private double espacio;
    private List<Gen> columnas;
    private List<Gen> columnasSeleccionadas;
    private String createIndexSyntax;

    public Cromosoma(String querys, List<Gen> columnas) {
        if (querys == null || columnas == null)
            throw new InvalidParameterException(Constante.INVALID_PARAMETER_MSG);
        if (columnas.isEmpty())
            throw new InvalidParameterException(Constante.EMPTY_LIST_PARAMETER_MSG);

        this.querys = querys;
        this.columnas = columnas;
        this.tiempoEjecucion = 1000000000;
    }

    public Cromosoma(Cromosoma otro){
        this.querys = otro.querys;
        this.tiempoEjecucion = otro.tiempoEjecucion;
        this.espacio = otro.espacio;

        List<Gen> copyList = new ArrayList<>();
        for (Gen col : otro.getColumnas()){
            copyList.add(new Gen(col));
        }
        this.columnas = copyList;
        List<Gen> copyListSeleccionadas = new ArrayList<>();
        for (Gen col : otro.getColumnasSeleccionadas()){
            copyListSeleccionadas.add(new Gen(col));
        }
        this.columnasSeleccionadas = copyListSeleccionadas;
    }

    public String getQuerys() {
        return querys;
    }

    public void setQuerys(String querys) {
        this.querys = querys;
    }

    public double getTiempoEjecucion() {
        return tiempoEjecucion;
    }

    public void setTiempoEjecucion(List<Tabla> tablas) {
        if (this.columnasSeleccionadas == null)
            throw new InvalidParameterException(Constante.INCONSISTENT_PARAMETER_MSG);
        String connectionUrl = "jdbc:mysql://localhost:3306/"+Constante.DATABASE_SELECTED+"?serverTimezone=UTC", createIndexSyntax = "", dropIndexSyntax = "", checkExistance = "";
        double startTime, endTime = 0;
        //Crear sintaxis de índice
        String indexName, indexColumns, queryOriginal = this.querys;
        for (int i=0; i<tablas.size(); i++){
            indexName = "IX_";
            indexColumns = "";
            for(Gen col : this.columnasSeleccionadas){
                if(i + 1 == col.getTuplaTabla()){
                    indexName += col.getNombreColumna() + "_";
                    indexColumns += col.getNombreColumna() + ", ";
                }
            }
            if (indexName != "IX_"){
                indexName = indexName.substring(0, indexName.length() - 1);
                if (indexName.length() >= 64)
                    indexName = indexName.substring(0, 63);
                indexColumns = indexColumns.substring(0, indexColumns.length() - 2);
                createIndexSyntax += "CREATE INDEX " + indexName + " ON " + tablas.get(i).getNombreTabla() + " (" + indexColumns + ");";
                dropIndexSyntax += "DROP INDEX " + indexName + " ON " + tablas.get(i).getNombreTabla() + ";";
                checkExistance += "SELECT 1 FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA = '"+Constante.DATABASE_SELECTED+"' " +
                        "AND TABLE_NAME='" + tablas.get(i).getNombreTabla().toLowerCase().replaceAll("[^a-zA-Z0-9]", "") + "' " +
                        "AND INDEX_NAME='" + indexName + "';";
                //Forzar usar index en query
                String[] querys = this.querys.split(";");
                for (int j = 0; j<querys.length; j++){
                    String q = querys[j].trim().toLowerCase();
                    String tableName = tablas.get(i).getNombreTabla().toLowerCase();
                    if (q.contains(tableName)){
                        tableName = " " + tableName + " ";
                        int index = q.indexOf(tableName);
                        index += tableName.length();
                        q = new StringBuilder(q).insert(index, " use index (" + indexName + ") ").toString();
                        querys[j] = q;
                    }
                }
                this.querys = String.join(";", querys);
            }
        }
        this.createIndexSyntax = createIndexSyntax != "" ? createIndexSyntax : "No necesita indice";
        //Correr querys
        try (Connection conn = DriverManager.getConnection(connectionUrl, "root", "Lobogris22-1")) {
            Statement stmt = conn.createStatement();
            String[] checkIndex = checkExistance.split(";");
            String[] createIndex = createIndexSyntax.split(";");
            if (checkExistance != ""){
                for (int i = 0; i<createIndex.length; i++){
                    ResultSet rs = stmt.executeQuery(checkIndex[i].trim());
                    rs.last();
                    if (rs.getRow()==0){
                        stmt.execute(createIndex[i].trim());
                    }
                }
            }
            String[] query = this.querys.split(";");
            for (int i = 0; i<query.length; i++){
                //Solo se cuenta el tiempo de la tercera ejecución
                stmt.execute(query[i]);
                stmt.execute(query[i]);
                startTime = System.nanoTime();
                stmt.execute(query[i]);
                endTime += (System.nanoTime() - startTime)/1000000000;
            }
            this.tiempoEjecucion = endTime;
            this.querys = queryOriginal;
            String[] dropIndex = dropIndexSyntax.split(";");
            if (dropIndexSyntax != "") {
                for (int i = 0; i<dropIndex.length; i++){
                    stmt.execute(dropIndex[i]);
                }
            }
        } catch (SQLException ex) {
            if (!ex.getMessage().contains("needed in a foreign key constraint")){
                System.out.println(ex.getMessage());
            }
        }
    }

    public double getEspacio() {
        return espacio;
    }

    public void setEspacio(List<Tabla> tablas) {
        if (tablas == null)
            throw new InvalidParameterException(Constante.INVALID_PARAMETER_MSG);
        if (tablas.isEmpty())
            throw new InvalidParameterException(Constante.EMPTY_LIST_PARAMETER_MSG);
        this.setColumnasSeleccionadas();
        if (this.columnasSeleccionadas == null)
            throw new InvalidParameterException(Constante.INCONSISTENT_PARAMETER_MSG);
        double sum = 0;
        double espTab;
        for (int i=0; i<tablas.size(); i++){
            espTab = 0;
            for(Gen col : this.columnasSeleccionadas){
                if(i + 1 == col.getTuplaTabla()){
                    espTab += col.getCantidadBytes();
                }
            }
            if(espTab != 0){
                espTab += 11; //Factor de overhead
                espTab *= tablas.get(i).getCantidadFilas();
                sum += espTab;
            }
        }
        sum = sum * 2 / 1000000000;
        this.espacio = sum;
    }

    public List<Gen> getColumnas() {
        return columnas;
    }

    public void setColumnas(List<Gen> columnas) {
        this.columnas = columnas;
    }

    public List<Gen> getColumnasSeleccionadas() {
        return columnasSeleccionadas;
    }

    public void setColumnasSeleccionadas() {
        List<Gen> colSelect = new ArrayList<>();
        for(Gen col : this.columnas){
            if(col.getProbabilidadEleccion() == 1){
                colSelect.add(col);
            }
        }
        this.columnasSeleccionadas = colSelect;
    }

    public String getCreateIndexSyntax() {
        return createIndexSyntax;
    }

    public void crearCromosomaNoIndexado() {
        for (int i=0; i<this.columnas.size(); i++){
            this.columnas.get(i).setProbabilidadEleccion((byte)0);
            this.columnas.get(i).setMuta((byte)0);
        }
    }

    public void crearCromosomaPonderado(List<Gen> columnaMasComunPorTabla) {
        if (columnaMasComunPorTabla == null)
            throw new InvalidParameterException(Constante.INVALID_PARAMETER_MSG);
        if (columnaMasComunPorTabla.isEmpty())
            throw new InvalidParameterException(Constante.EMPTY_LIST_PARAMETER_MSG);

        for (Gen g : columnaMasComunPorTabla){
            Gen columnaMasComun = this.columnas.stream()
                    .filter(columna -> g.getTupla() == columna.getTupla())
                    .findAny()
                    .orElse(null);
            columnaMasComun.setProbabilidadEleccion((byte)1);
        }
        for (int i=0; i<this.columnas.size(); i++){
            this.columnas.get(i).setMuta((byte)0);
        }
    }

    public void crearCromosomaMitad(List<Tabla> tablas) {
        if (tablas == null)
            throw new InvalidParameterException(Constante.INVALID_PARAMETER_MSG);
        if (tablas.isEmpty())
            throw new InvalidParameterException(Constante.EMPTY_LIST_PARAMETER_MSG);

        this.setColumnasSeleccionadas();
        //Cantidad de índices por tabla
        int[] indiceColumnas = new int[tablas.size()];
        int count;
        for (int i=0; i<tablas.size(); i++){
            count = 0;
            for(Gen col : this.columnasSeleccionadas){
                if(tablas.get(i).getNumeroTabla() == col.getTuplaTabla()){
                    count ++;
                }
            }
            indiceColumnas[i] = count;
        }
        for (int i=0; i<indiceColumnas.length; i++){
            if (indiceColumnas[i] > 0){
                int numeroTabla = i+1;
                List<Gen> aux = this.columnasSeleccionadas.stream()
                        .filter(gen -> gen.getTuplaTabla() == numeroTabla)
                        .collect(Collectors.toList());
                for (int j=0; j<indiceColumnas[i]/2; j++){
                    aux.get(j).setProbabilidadEleccion((byte)1);
                }
            }
        }

        for (int i=0; i<this.columnas.size(); i++){
            this.columnas.get(i).setMuta((byte)0);
        }
    }

    public void crearCromosomaNoIndexadoMutado() {
        for (int i=0; i<this.columnas.size(); i++){
            this.columnas.get(i).setProbabilidadEleccion((byte)0);
            this.columnas.get(i).setMuta((byte)1);
        }
    }

    public void crearCromosomaPonderadoMutado(List<Gen> columnaMasComunPorTabla) {
        if (columnaMasComunPorTabla == null)
            throw new InvalidParameterException(Constante.INVALID_PARAMETER_MSG);
        if (columnaMasComunPorTabla.isEmpty())
            throw new InvalidParameterException(Constante.EMPTY_LIST_PARAMETER_MSG);

        for (Gen g : columnaMasComunPorTabla){
            Gen columnaMasComun = this.columnas.stream()
                    .filter(columna -> g.getTupla() == columna.getTupla())
                    .findAny()
                    .orElse(null);
            columnaMasComun.setProbabilidadEleccion((byte)1);
        }
        for (int i=0; i<this.columnas.size(); i++){
            this.columnas.get(i).setMuta((byte)1);
        }
    }

    public void crearCromosomaMitadMutado(List<Tabla> tablas) {
        if (tablas == null)
            throw new InvalidParameterException(Constante.INVALID_PARAMETER_MSG);
        if (tablas.isEmpty())
            throw new InvalidParameterException(Constante.EMPTY_LIST_PARAMETER_MSG);

        this.setColumnasSeleccionadas();
        //Cantidad de índices por tabla
        int[] indiceColumnas = new int[tablas.size()];
        int count;
        for (int i=0; i<tablas.size(); i++){
            count = 0;
            for(Gen col : this.columnasSeleccionadas){
                if(tablas.get(i).getNumeroTabla() == col.getTuplaTabla()){
                    count ++;
                }
            }
            indiceColumnas[i] = count;
        }
        for (int i=0; i<indiceColumnas.length; i++){
            if (indiceColumnas[i] > 0){
                int numeroTabla = i+1;
                List<Gen> aux = this.columnasSeleccionadas.stream()
                        .filter(gen -> gen.getTuplaTabla() == numeroTabla)
                        .collect(Collectors.toList());
                for (int j=0; j<indiceColumnas[i]/2; j++){
                    aux.get(j).setProbabilidadEleccion((byte)1);
                }
            }
        }

        for (int i=0; i<this.columnas.size(); i++){
            this.columnas.get(i).setMuta((byte)1);
        }
    }

    public Boolean esValido (List<Tabla> tablas, double espacioTablasQuerys, float rh) {
        Boolean flagValido = true;
        this.setEspacio(tablas);

        //Ratio espacio
        if (this.espacio/espacioTablasQuerys >= rh) {
            flagValido = false;
        }
        return flagValido;
    }

    public Cromosoma cruzar(Cromosoma padre2) {
        if (padre2 == null)
            throw new InvalidParameterException(Constante.INVALID_PARAMETER_MSG);
        Random rand = new Random();
        ArrayList<Gen> columnasNuevas = new ArrayList<>();
        Cromosoma nuevoCromosoma;

        int punto = rand.nextInt(this.columnas.size());
        for (int i =0; i<this.columnas.size(); i++){
            if(i<punto) {
                columnasNuevas.add(new Gen(this.columnas.get(i)));
            }
            else {
                columnasNuevas.add(new Gen(padre2.getColumnas().get(i)));
            }
        }

        nuevoCromosoma = new Cromosoma(this.querys, columnasNuevas);
        return nuevoCromosoma;
    }

    public void mutar(){
        Random rand = new Random();
        int punto = rand.nextInt(this.columnas.size());

        Gen genMutar = this.columnas.get(punto);
        if (genMutar.getMuta() == 1){
            byte prob = this.columnas.get(punto).getProbabilidadEleccion();
            this.columnas.get(punto).setProbabilidadEleccion(prob == (byte) 1 ? (byte) 0 : (byte) 1);
        }
    }

    public void printSolucion() {
        System.out.println(this.createIndexSyntax);
    }

    public boolean mismasColumnasSeleccionadas (Cromosoma otro){
        if (this.columnasSeleccionadas.size() != otro.getColumnasSeleccionadas().size())
            return false;
        List<Pair<Integer, Integer>> cols = new ArrayList<>(), colsOtro = new ArrayList<>();
        for (int i=0; i<this.columnasSeleccionadas.size(); i++){
            cols.add(this.columnasSeleccionadas.get(i).getTupla());
            colsOtro.add(otro.getColumnasSeleccionadas().get(i).getTupla());
        }
        return cols.equals(colsOtro);
    }

    @Override
    public int compareTo(Cromosoma o) {
        if (this.getTiempoEjecucion() > o.getTiempoEjecucion()){
            return 1;
        }
        else if (this.getTiempoEjecucion() < o.getTiempoEjecucion()){
            return -1;
        }
        else {
            return 0;
        }
    }
}
