package pl.edu.agh.calculationp2p.message.body;

import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.message.utils.TaskStateMess;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GiveSynchronizationTest {

    @Test
    void serializeType() {

        List<TaskStateMess> list = new ArrayList<>();
        Body giveSync = new GiveSynchronization(list);
        assertEquals("\"give_synchronization\"", giveSync.serializeType());

    }

    @Test
    void serializeContent() {
        //TODO: free vs Free
        List<TaskStateMess> listEmpty = new ArrayList<>();
        List<TaskStateMess> listOne = new ArrayList<>(List.of(new TaskStateMess(1, TaskState.Free, 2)));
        List<TaskStateMess> listFew = new ArrayList<>(List.of(new TaskStateMess(1, TaskState.Free, 2), new TaskStateMess(2, TaskState.Free, 3)));

        Body giveSyncEmpty = new GiveSynchronization(listEmpty);
        Body giveSyncOne = new GiveSynchronization(listOne);
        Body giveSyncFew = new GiveSynchronization(listFew);

        String resultEmpty = "{\"tasks\":[]}";
        String resultOne = "{\"tasks\":[" +
                "{" +
                "\"task_id\":1," +
                "\"state\":Free," +
                "\"owner\":2," +
                "\"result\":\"null\"" +
                "}" +
                "]}";
        String resultFew = "{\"tasks\":[" +
                "{" +
                "\"task_id\":1," +
                "\"state\":Free," +
                "\"owner\":2," +
                "\"result\":\"null\"" +
                "}," +
                "{" +
                "\"task_id\":2," +
                "\"state\":Free," +
                "\"owner\":3," +
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