package pl.edu.agh.calculationp2p.message.body;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReserveTest {

    @Test
    void serializeType() {

        Body reserve = new Reserve(2);
        assertEquals("\"reserve\"", reserve.serializeType());

    }

    @Test
    void serializeContent() {

        Body reserve = new Reserve(2);
        String result = "{\"task_id\":2}";
        assertEquals(result, reserve.serializeContent());

    }

    @Test
    void process() {


    }
}