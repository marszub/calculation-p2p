package pl.edu.agh.calculationp2p.message.body;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GetSynchronizationTest {

    @Test
    void serializeType() {

        List<Integer> integerList = new ArrayList<Integer>(1);
        Body getSynchronization = new GetSynchronization(integerList);
        assertEquals("\"get_synchronization\"", getSynchronization.serializeType());

    }

    @Test
    void serializeContent() {

        List<Integer> integerListEmpty = new ArrayList<>();
        Body getSynchronizationEmpty = new GetSynchronization(integerListEmpty);

        List<Integer> integerListOne = new ArrayList<>(List.of(1));
        Body getSynchronizationOne = new GetSynchronization(integerListOne);

        List<Integer> integerListFew = new ArrayList<>(List.of(1,2));
        Body getSynchronizationFew = new GetSynchronization(integerListFew);

        String resultEmpty = "{\"tasks\":[]}";
        String resultOne = "{\"tasks\":[{\"task_id\":1}]}";
        String resultFew = "{\"tasks\":[{\"task_id\":1},{\"task_id\":2}]}";

        assertEquals(resultEmpty, getSynchronizationEmpty.serializeContent());
        assertEquals(resultOne, getSynchronizationOne.serializeContent());
        assertEquals(resultFew, getSynchronizationFew.serializeContent());

    }

    @Test
    void process() {
    }
}