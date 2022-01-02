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

        Body hello = new Hello("192.168.0.1");
        String result = "{\"ip\":\"192.168.0.1\"}";
        assertEquals(result, hello.serializeContent());

        Body helloNull = new Hello(null);
        String resultNull = "{\"ip\":\"null\"}";
        assertEquals(resultNull, helloNull.serializeContent());
    }

    @Test
    void process() {
    }
}