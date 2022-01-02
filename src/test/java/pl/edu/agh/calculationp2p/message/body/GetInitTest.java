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
    }

    @Test
    void process() {
    }
}