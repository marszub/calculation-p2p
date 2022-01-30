package pl.edu.agh.calculationp2p.calculationTask;

import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.calculationTask.hashBreaking.HashTaskResult;

import static org.junit.Assert.assertEquals;

public class HashTaskResultTest {
    @Test
    public void serializeEmpty(){
        HashTaskResult result = new HashTaskResult();
        assertEquals("[]", result.serialize());
    }
}
