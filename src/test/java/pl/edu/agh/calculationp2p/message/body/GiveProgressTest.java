package pl.edu.agh.calculationp2p.message.body;

import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.calculationTask.hashBreaking.HashTaskResult;
import pl.edu.agh.calculationp2p.state.Progress;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GiveProgressTest {

    @Test
    void serializeType() {

        Body giveProcess = new GiveProgress(null);
        assertEquals("\"give_progress\"", giveProcess.serializeType());

    }

    @Test
    void serializeContent() {

        Body giveProcess = new GiveProgress(null);

        String result = "{\"progress\":\"null\"}";
        assertEquals(result, giveProcess.serializeContent());

    }

    @Test
    void serializeContent01() {

        Progress progress = new Progress(new ArrayList<>(List.of(new TaskRecord(2, TaskState.Reserved, 2, new HashTaskResult()))));

        Body giveProcess = new GiveProgress(progress);

        String result = "{\"progress\":[{\"task_id\":2,\"state\":\"Reserved\",\"owner\":2,\"result\":[]}]}";

        assertEquals(result, giveProcess.serializeContent());

    }


    @Test
    void process() {
    }
}