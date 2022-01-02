package pl.edu.agh.calculationp2p.message.body;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GiveSynchronizationTest {

    @Test
    void serializeType() {

        Body giveSync = new GiveSynchronization();
        assertEquals("\"give_synchronization\"", giveSync.serializeType());

    }

    @Test
    void serializeContent() {
    }

    @Test
    void process() {
    }
}