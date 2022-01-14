package pl.edu.agh.calculationp2p.message.body;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GiveProgressTest {

    @Test
    void serializeType() {

        Body giveProcess = new GiveProgress(null);
        assertEquals("\"give_progress\"", giveProcess.serializeType());

    }

    @Test
    void serializeContent() {
        //TODO:
        Body giveProcess = new GiveProgress(null);

        String result = "{\"progress\":\"null\"}";
        assertEquals(result, giveProcess.serializeContent());

    }

    @Test
    void process() {
    }
}