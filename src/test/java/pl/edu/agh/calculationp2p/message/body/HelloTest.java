package pl.edu.agh.calculationp2p.message.body;

import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;

import static org.junit.jupiter.api.Assertions.*;

class HelloTest {

    @Test
    void serializeType() {

        Body hello = new Hello(new InetSocketAddress("192.168.0.1", 2137));
        assertEquals("\"hello\"", hello.serializeType());

    }

    @Test
    void serializeContent() {

        Body hello = new Hello(new InetSocketAddress("192.168.0.1", 2137));
        String result = "{\"ip\":\"192.168.0.1\",\"port\":\"2137\"}";
        assertEquals(result, hello.serializeContent());

        Body helloNull = new Hello(null);
        String resultNull = "{\"ip\":\"null\",\"port\":\"null\"}";
        assertEquals(resultNull, helloNull.serializeContent());
    }

    @Test
    void process() {
    }
}