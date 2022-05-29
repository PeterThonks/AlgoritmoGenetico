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
    private int tamanoPoblacionElitista;
    private List<Cromosoma> mejorSolucion;

    public Poblacion(int tamanoPoblacion, int tamanoPoblacionElitista) {
        if (tamanoPoblacion < 0 || tamanoPoblacionElitista < 0)
            throw new InvalidParameterException(Constante.NEGATIVE_PARAMETER_MSG);
        this.tamanoPoblacion = tamanoPoblacion;
        this.tamanoPoblacionElitista = tamanoPoblacionElitista;
    }

    public Poblacion(List<Cromosoma> poblacion, int tamanoPoblacion) {
        if (tamanoPoblacion < 0)
            throw new InvalidParameterException(Constante.NEGATIVE_PARAMETER_MSG);
        if (poblacion == null)
            throw new InvalidParameterException(Constante.INVALID_PARAMETER_MSG);
        if (poblacion.isEmpty())
            throw new InvalidParameterException(Constante.EMPTY_LIST_PARAMETER_MSG);
        this.poblacion = poblacion;
        this.tamanoPoblacion = tamanoPoblacion;
    }

    public int getTamanoPoblacion() { return tamanoPoblacion; }

    public List<Cromosoma> getMejorSolucion() { return mejorSolucion; }

    public void crearPoblacionInicial(Lector lector, float rh){
        if (lector == null)
            throw new InvalidParameterException(Constante.INVALID_PARAMETER_MSG);
        if (rh < 0f)
            throw new InvalidParameterException(Constante.NEGATIVE_PARAMETER_MSG);
        List<Gen> columnasSel = lector.getColumnasQuery();
        Cromosoma nuevoGen;
        List<Cromosoma> p = new ArrayList<>();
        do {
            nuevoGen = new Cromosoma(lector.getQuerys(), lector.getColumnasQuery());
            nuevoGen.crearCromosomaNoIndexado();
        } while (!nuevoGen.esValido(lector.getTablas(), lector.getEspacioTablasQuerys(), rh));
        p.add(new Cromosoma(nuevoGen));
        do {
            nuevoGen = new Cromosoma(lector.getQuerys(), lector.getColumnasQuery());
            nuevoGen.crearCromosomaPonderado(lector.getColumnaMasComunPorTabla());
        } while (!nuevoGen.esValido(lector.getTablas(), lector.getEspacioTablasQuerys(), rh));
        p.add(new Cromosoma(nuevoGen));
        do {
            nuevoGen = new Cromosoma(lector.getQuerys(), lector.getColumnasQuery());
            nuevoGen.crearCromosomaMitad(lector.getTablas());
        } while (!nuevoGen.esValido(lector.getTablas(), lector.getEspacioTablasQuerys(), rh));
        p.add(new Cromosoma(nuevoGen));
        do {
            nuevoGen = new Cromosoma(lector.getQuerys(), lector.getColumnasQuery());
            nuevoGen.crearCromosomaNoIndexadoMutado();
        } while (!nuevoGen.esValido(lector.getTablas(), lector.getEspacioTablasQuerys(), rh));
        p.add(new Cromosoma(nuevoGen));
        do {
            nuevoGen = new Cromosoma(lector.getQuerys(), lector.getColumnasQuery());
            nuevoGen.crearCromosomaPonderadoMutado(lector.getColumnaMasComunPorTabla());
        } while (!nuevoGen.esValido(lector.getTablas(), lector.getEspacioTablasQuerys(), rh));
        p.add(new Cromosoma(nuevoGen));
        do {
            nuevoGen = new Cromosoma(lector.getQuerys(), lector.getColumnasQuery());
            nuevoGen.crearCromosomaMitadMutado(lector.getTablas());
        } while (!nuevoGen.esValido(lector.getTablas(), lector.getEspacioTablasQuerys(), rh));
        p.add(new Cromosoma(nuevoGen));
        this.poblacion = p;
    }

    public void obtenerMejorSolucion(Lector lector) {
        if (lector == null)
            throw new InvalidParameterException(Constante.INVALID_PARAMETER_MSG);
        for (int i = 0; i<this.poblacion.size(); i++){
            if (this.poblacion.get(i).getFitness() == 1000000000){
                this.poblacion.get(i).setFitness(lector.getTablas());
            }
        }
        Collections.sort(this.poblacion);
        double tiempoMin = this.poblacion.get(0).getTiempoEjecucion();
        List<Cromosoma> poblacionTop = this.poblacion.stream()
                .filter(cromosoma -> cromosoma.getTiempoEjecucion() == tiempoMin)
                .collect(Collectors.toList());
        this.mejorSolucion = poblacionTop;
    }

    public List<Cromosoma> crearNuevaPoblacion (Lector lector, float rh, double espacioTablasQuerys, float probMutacion, float probCruzamiento) {
        if (lector == null)
            throw new InvalidParameterException(Constante.INVALID_PARAMETER_MSG);
        if (rh < 0 || espacioTablasQuerys < 0 || probMutacion < 0 || probCruzamiento < 0)
            throw new InvalidParameterException(Constante.NEGATIVE_PARAMETER_MSG);
        List<Cromosoma> poblacionMutada = new ArrayList<>(), poblacionHijos = new ArrayList<>(),
                nuevaPoblacion = new ArrayList<>(), poblacionElitista = new ArrayList<>(), totalPoblacion;

        //Mutación
        for (Cromosoma c : this.poblacion){
            if (Math.random() <= probMutacion){
                Cromosoma nuevoCromosoma = new Cromosoma(c);
                nuevoCromosoma.mutar();
                if (nuevoCromosoma.esValido(lector.getTablas(), lector.getEspacioTablasQuerys(), rh))
                    poblacionMutada.add(new Cromosoma(nuevoCromosoma));
            }
        }

        //Cruzamiento
        for (Cromosoma c : this.poblacion){
            if (Math.random() <= probCruzamiento){
                Random rand = new Random();
                int padre1Index = rand.nextInt(this.poblacion.size()), padre2Index = rand.nextInt(this.poblacion.size());
                Cromosoma padre1 = new Cromosoma(this.poblacion.get(padre1Index)), padre2 = new Cromosoma(this.poblacion.get(padre2Index)), nuevoCromosoma;
                nuevoCromosoma = padre1.cruzar(padre2);
                if (nuevoCromosoma.esValido(lector.getTablas(), lector.getEspacioTablasQuerys(), rh))
                    poblacionHijos.add(new Cromosoma(nuevoCromosoma));
            }
        }

        totalPoblacion = Stream.of(this.poblacion, poblacionMutada, poblacionHijos)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        Collections.sort(totalPoblacion);

        for (int i=0; i<this.tamanoPoblacion; i++){
            if (i<this.tamanoPoblacionElitista){
                nuevaPoblacion.add(new Cromosoma(totalPoblacion.get(i)));
            }
            else {
                Random rand = new Random();
                int competidor1Index = rand.nextInt(totalPoblacion.size()), competidor2Index = rand.nextInt(totalPoblacion.size());
                Cromosoma competidor1 = new Cromosoma(totalPoblacion.get(competidor1Index)), competidor2 = new Cromosoma(totalPoblacion.get(competidor2Index));
                if (competidor1.getTiempoEjecucion() <= competidor2.getTiempoEjecucion())
                    nuevaPoblacion.add(new Cromosoma(competidor1));
                else
                    nuevaPoblacion.add(new Cromosoma(competidor2));
            }
        }

        return nuevaPoblacion;
    }
}
