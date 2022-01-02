package pl.edu.agh.calculationp2p.message.body;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HelloTest {

    @Test
    void serializeType() {

        Body hello = new Hello("192.168.0.1");
        assertEquals("\"hello\"", hello.serializeType());

    }

    @Test
    void serializeContent() {
    }

    @Test
    void process() {
    }
}