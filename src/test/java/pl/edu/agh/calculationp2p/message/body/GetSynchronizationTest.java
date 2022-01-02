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
    }

    @Test
    void process() {
    }
}