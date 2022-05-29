package algoritmo.shared.util;

public class Constante {
    // Rutas estáticas
    public static final String PATH_INPUT_CSV = "src/main/resources/archivos/input/";
    public static final String PATH_OUTPUT_CSV = "src/main/resources/archivos/output/";
    public static final String PATH_INPUT_CSV_TEST = "src/test/resources/";

    // DB seleccionada
    public static final String DATABASE_SELECTED = /*"northwind"*//*"employees"*/"chinook";

    // Mnesajes de excepción
    public static final String INVALID_PARAMETER_MSG = "Uno de los parámetros enviados es nulo";
    public static final String EMPTY_LIST_PARAMETER_MSG = "La lista enviada se encuentra vacía";
    public static final String NEGATIVE_PARAMETER_MSG = "Uno de los parámetros enviados es negativo";
    public static final String INCONSISTENT_PARAMETER_MSG = "Se detectó inconsistencias en los parametros, requiere revisión";
}
