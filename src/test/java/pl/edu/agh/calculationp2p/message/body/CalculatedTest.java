package pl.edu.agh.calculationp2p.message.body;

import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.calculation.TaskResult;

import static org.junit.jupiter.api.Assertions.*;

class CalculatedTest {

    @Test
    void serializeType() {

        TaskResult taskResult = () -> "task_result";

        Body calculated = new Calculated(1, taskResult);

        assertEquals("\"calculated\"", calculated.serializeType());

    }

    @Test
    void serializeContent() {



    }

    @Test
    void process() {
    }
}