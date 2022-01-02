package pl.edu.agh.calculationp2p.message.body;

import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import static org.junit.jupiter.api.Assertions.*;

class ConfirmTest {

    @Test
    void serializeType() {

        Body confirm = new Confirm(1, TaskState.Free, 5, new TaskRecord());

        assertEquals("\"confirm\"", confirm.serializeType());

    }

    @Test
    void serializeContent() {
    }

    @Test
    void process() {
    }
}