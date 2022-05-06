package algoritmo.genetico.principal;

import algoritmo.genetico.auxiliar.Lector;
import algoritmo.genetico.estructura.Cromosoma;
import algoritmo.genetico.estructura.Gen;
import algoritmo.shared.util.Constante;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Poblacion {
    private List<Cromosoma> poblacion;
    private int tamanoPoblacion;
    private Cromosoma mejorSolucion;

    public Poblacion(int tamanoPoblacion) {
        if (tamanoPoblacion < 0)
            throw new InvalidParameterException(Constante.NEGATIVE_PARAMETER_MSG);
        this.tamanoPoblacion = tamanoPoblacion;
    }

    public int getTamanoPoblacion() { return tamanoPoblacion; }

    public void crearPoblacion(Lector lector, float rh){
        if (lector == null)
            throw new InvalidParameterException(Constante.INVALID_PARAMETER_MSG);
        if (rh < 0)
            throw new InvalidParameterException(Constante.NEGATIVE_PARAMETER_MSG);
        List<Gen> columnasSel = lector.getColumnasQuery();
        Cromosoma nuevoGen;
        List<Cromosoma> p = new ArrayList<>();
        do {
            nuevoGen = new Cromosoma(lector.getQuerys(), lector.getColumnasQuery());
            nuevoGen.crearCromosomaNoIndexado();
        } while (!nuevoGen.esValido(lector.getTablas(), lector.getEspacioTablasQuerys(), rh));
        p.add(nuevoGen);
        do {
            nuevoGen = new Cromosoma(lector.getQuerys(), lector.getColumnasQuery());
            nuevoGen.crearCromosomaPonderado(lector.getColumnaMasComunPorTabla());
        } while (!nuevoGen.esValido(lector.getTablas(), lector.getEspacioTablasQuerys(), rh));
        p.add(nuevoGen);
        do {
            nuevoGen = new Cromosoma(lector.getQuerys(), lector.getColumnasQuery());
            nuevoGen.crearCromosomaMitad(lector.getTablas());
        } while (!nuevoGen.esValido(lector.getTablas(), lector.getEspacioTablasQuerys(), rh));
        p.add(nuevoGen);
        do {
            nuevoGen = new Cromosoma(lector.getQuerys(), lector.getColumnasQuery());
            nuevoGen.crearCromosomaNoIndexadoMutado();
        } while (!nuevoGen.esValido(lector.getTablas(), lector.getEspacioTablasQuerys(), rh));
        p.add(nuevoGen);
        do {
            nuevoGen = new Cromosoma(lector.getQuerys(), lector.getColumnasQuery());
            nuevoGen.crearCromosomaPonderadoMutado(lector.getColumnaMasComunPorTabla());
        } while (!nuevoGen.esValido(lector.getTablas(), lector.getEspacioTablasQuerys(), rh));
        p.add(nuevoGen);
        do {
            nuevoGen = new Cromosoma(lector.getQuerys(), lector.getColumnasQuery());
            nuevoGen.crearCromosomaMitadMutado(lector.getTablas());
        } while (!nuevoGen.esValido(lector.getTablas(), lector.getEspacioTablasQuerys(), rh));
        p.add(nuevoGen);
        this.poblacion = p;
    }

    public void obtenerMejorSolucion(Lector lector) {
        if (lector == null)
            throw new InvalidParameterException(Constante.INVALID_PARAMETER_MSG);
        for (int i = 0; i<this.poblacion.size(); i++){
            if (this.poblacion.get(i).getTiempoEjecucion() == 1000000000){
                this.poblacion.get(i).setTiempoEjecucion(lector.getTablas());
            }
        }
        Collections.sort(this.poblacion);
        this.mejorSolucion = this.poblacion.get(0);
    }

    public List<Cromosoma> crearNuevaPoblacion (Lector lector, float rh, double espacioTablasQuerys, float probMutacion, float probCruzamiento) {
        if (rh < 0 || espacioTablasQuerys < 0 || probMutacion < 0 || probCruzamiento < 0)
            throw new InvalidParameterException(Constante.NEGATIVE_PARAMETER_MSG);
        List<Cromosoma> poblacionMutada = new ArrayList<>(), poblacionHijos = new ArrayList<>(), nuevaPoblacion = new ArrayList<>(), totalPoblacion;

        //Mutación
        for (Cromosoma c : this.poblacion){
            if (Math.random() <= probMutacion){
                Cromosoma nuevoCromosoma = new Cromosoma(c);
                nuevoCromosoma.mutar();
                if (c.esValido(lector.getTablas(), lector.getEspacioTablasQuerys(), rh))
                    poblacionMutada.add(nuevoCromosoma);
            }
        }

        //Cruzamiento
        for (Cromosoma c : this.poblacion){
            if (Math.random() <= probCruzamiento){
                Random rand = new Random();
                int padre1Index = rand.nextInt(this.poblacion.size()), padre2Index = rand.nextInt(this.poblacion.size());
                Cromosoma padre1 = new Cromosoma(this.poblacion.get(padre1Index)), padre2 = new Cromosoma(this.poblacion.get(padre2Index)), nuevoCromosoma;
                nuevoCromosoma = padre1.cruzar(padre2);
                if (c.esValido(lector.getTablas(), lector.getEspacioTablasQuerys(), rh))
                    poblacionHijos.add(nuevoCromosoma);
            }
        }

        totalPoblacion = Stream.of(this.poblacion, poblacionMutada, poblacionHijos)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        for (int i=0; i<6; i++){
            Random rand = new Random();
            int competidor1Index = rand.nextInt(totalPoblacion.size()), competidor2Index = rand.nextInt(totalPoblacion.size());
            Cromosoma competidor1 = new Cromosoma(totalPoblacion.get(competidor1Index)), competidor2 = new Cromosoma(totalPoblacion.get(competidor2Index));
            if (competidor1.getTiempoEjecucion() <= competidor2.getTiempoEjecucion())
                totalPoblacion.add(competidor1);
            else
                totalPoblacion.add(competidor2);
        }

        return nuevaPoblacion;
    }
}
