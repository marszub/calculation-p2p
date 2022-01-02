package pl.edu.agh.calculationp2p.message.body;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeartBeatTest {

    @Test
    void serializeType() {

        Body heart = new HeartBeat();
        assertEquals("\"heart_beat\"", heart.serializeType());

    }

    @Test
    void serializeContent() {

        Body heart = new HeartBeat();
        assertEquals("{}", heart.serializeContent());

    }

    @Test
    void process() {
    }
}