package pl.edu.agh.calculationp2p.message.body;

import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.calculation.TaskResult;
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

        TaskRecord taskRecord = new TaskRecord();

        Body confirm = new Confirm(1, TaskState.Free, 5, taskRecord);

        String result = "{\"task_id\":1,\"state\":\"free\",\"owner\":5,\"result\":}";
        assertEquals(result, confirm.serializeContent());

    }

    @Test
    void process() {
    }
}