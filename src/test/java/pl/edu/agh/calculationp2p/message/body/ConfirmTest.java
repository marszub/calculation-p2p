package pl.edu.agh.calculationp2p.message.body;

import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import static org.junit.jupiter.api.Assertions.*;

class ConfirmTest {

    @Test
    void serializeType() {

        Body confirm = new Confirm(new TaskRecord(1, TaskState.Free, 5, null));

        assertEquals("\"confirm\"", confirm.serializeType());

    }

    @Test
    void serializeContent() {

        Body confirm = new Confirm(new TaskRecord(1, TaskState.Free, 5, null));

        String result = "{\"task_id\":1,\"state\":\"Free\",\"owner\":5,\"result\":\"null\"}";
        assertEquals(result, confirm.serializeContent());

    }


    @Test
    void process() {
    }
}