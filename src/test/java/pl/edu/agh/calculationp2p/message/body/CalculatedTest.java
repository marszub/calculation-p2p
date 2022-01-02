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

        String expected = "task_result";

        TaskResult taskResult = () -> expected;

        Body calculated = new Calculated(1, taskResult);

        String result = "{\"task_id\":1,\"result\":"+expected+"}";
        assertEquals(result, calculated.serializeContent());

    }

    @Test
    void process() {
    }
}