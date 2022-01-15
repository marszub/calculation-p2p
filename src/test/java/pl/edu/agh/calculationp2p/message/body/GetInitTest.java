package pl.edu.agh.calculationp2p.message.body;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GetInitTest {

    @Test
    void serializeType() {

        Body getInit = new GetInit();
        assertEquals("\"get_init\"", getInit.serializeType());

    }

    @Test
    void serializeContent() {

        Body getInit = new GetInit();

        String result = "{}";
        assertEquals(result, getInit.serializeContent());

    }

    @Test
    void process() {
    }
}