package algoritmo.genetico.estructura;

import org.javatuples.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenTest {
    private Gen c;

    @Test
    public void existenciaGen(){
        c = new Gen("apellido", 1, 3, 150);
        assertEquals("apellido", c.getNombreColumna());
        assertEquals(new Pair<Integer, Integer>(1, 3), c.getTupla());
        assertEquals(150, c.getCantidadBytes());
        assertEquals(0, c.getProbabilidadEleccion());
        assertEquals(1, c.getMuta());
    }

    @Test
    public void existenciaConformidadGen(){
        c = new Gen(null, -1, -3, -150);
    }
}
