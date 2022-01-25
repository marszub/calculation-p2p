package pl.edu.agh.calculationp2p.message.body;

import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GiveSynchronizationTest {

    @Test
    void serializeType() {

        List<TaskRecord> list = new ArrayList<>();
        Body giveSync = new GiveSynchronization(list);
        assertEquals("\"give_synchronization\"", giveSync.serializeType());

    }

    @Test
    void serializeContent() {

        List<TaskRecord> listEmpty = new ArrayList<>();
        List<TaskRecord> listOne = new ArrayList<>(List.of(new TaskRecord(1, TaskState.Free, 5, null)));
        List<TaskRecord> listFew = new ArrayList<>(List.of(new TaskRecord(1, TaskState.Free, 5, null), new TaskRecord(2, TaskState.Free, 5, null)));

        Body giveSyncEmpty = new GiveSynchronization(listEmpty);
        Body giveSyncOne = new GiveSynchronization(listOne);
        Body giveSyncFew = new GiveSynchronization(listFew);

        String resultEmpty = "{\"tasks\":[]}";
        String resultOne = "{\"tasks\":[" +
                "{" +
                "\"task_id\":1," +
                "\"state\":\"free\"," +
                "\"owner\":5," +
                "\"result\":\"null\"" +
                "}" +
                "]}";
        String resultFew = "{\"tasks\":[" +
                "{" +
                "\"task_id\":1," +
                "\"state\":\"free\"," +
                "\"owner\":5," +
                "\"result\":\"null\"" +
                "}," +
                "{" +
                "\"task_id\":2," +
                "\"state\":\"free\"," +
                "\"owner\":5," +
                "\"result\":\"null\"" +
                "}" +
                "]}";

        assertEquals(resultEmpty, giveSyncEmpty.serializeContent());
        assertEquals(resultOne, giveSyncOne.serializeContent());
        assertEquals(resultFew, giveSyncFew.serializeContent());
    }

    @Test
    void process() {
    }
}