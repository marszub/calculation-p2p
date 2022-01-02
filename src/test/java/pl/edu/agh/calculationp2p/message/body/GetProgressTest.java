package pl.edu.agh.calculationp2p.message.body;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GetProgressTest {

    @Test
    void serializeType() {

        Body getProgress = new GetProgress();
        assertEquals("\"get_progress\"", getProgress.serializeType());

    }

    @Test
    void serializeContent() {
    }

    @Test
    void process() {
    }
}