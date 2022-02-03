package pl.edu.agh.calculationp2p.message.body;

import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import static org.junit.jupiter.api.Assertions.*;

class CalculatedTest {

    @Test
    void serializeType() {

        TaskRecord taskRecord = new TaskRecord();

        Body calculated = new Calculated(taskRecord);

        assertEquals("\"calculated\"", calculated.serializeType());

    }

// TODO: rewrite

//    @Test
//    void serializeContent() {
//
//        String expected = "task_result";
//
//        TaskRecord taskRecord = new TaskRecord(1, TaskState.Calculated, 2, null);
//
//        Body calculated = new Calculated(taskRecord);
//
//        String result = "{\"task_id\":1,\"state\":\"calculated\",\"owner\":2,\"result\":\"null\"}";
//        assertEquals(result, calculated.serializeContent());
//
//    }

    @Test
    void process() {
    }
}