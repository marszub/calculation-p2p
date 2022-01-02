package pl.edu.agh.calculationp2p.message.body;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GiveProcessTest {

    @Test
    void serializeType() {

        Body giveProcess = new GiveProcess();
        assertEquals("\"give_process\"", giveProcess.serializeType());

    }

    @Test
    void serializeContent() {
    }

    @Test
    void process() {
    }
}