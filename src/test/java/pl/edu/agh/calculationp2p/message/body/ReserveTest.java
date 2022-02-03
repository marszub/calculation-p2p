package pl.edu.agh.calculationp2p.message.body;

import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import static org.junit.jupiter.api.Assertions.*;

class ReserveTest {

    @Test
    void serializeType() {

        Body reserve = new Reserve(new TaskRecord(2, TaskState.Free, 3, null));
        assertEquals("\"reserve\"", reserve.serializeType());

    }

    // TODO: rewrite

//    @Test
//    void serializeContent() {
//
//        Body reserve = new Reserve(new TaskRecord(2, TaskState.Free, 3, null));
//        String result = "{\"task_id\":2,\"state\":\"free\",\"owner\":3,\"result\":\"null\"}";
//        assertEquals(result, reserve.serializeContent());
//
//    }

    @Test
    void process() {

    }
}