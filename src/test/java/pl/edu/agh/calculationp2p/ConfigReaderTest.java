package pl.edu.agh.calculationp2p;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigReaderTest {

    String path = "config/connectionConfigTest.json";

    @Test
    void getServerAddress() {

        AppConfig reader = null;
        try {
            reader = new ConfigReader(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(2137, reader.getServerAddress().getPort());
        assertEquals("127.0.0.1", reader.getServerAddress().getHostString());

    }

    @Test
    void getMyIpString() {

        AppConfig reader = null;
        try {
            reader = new ConfigReader(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("127.0.0.2", reader.getMyAddress().getAddress().toString().substring(1));

    }

    @Test
    void getMaxConnectingTime() {

        AppConfig reader = null;
        try {
            reader = new ConfigReader(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(10, reader.getMaxConnectingTime());

    }

    @Test
    void getGetProgressRetryTime() {

        AppConfig reader = null;
        try {
            reader = new ConfigReader(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(20, reader.getGetProgressRetryTime());

    }
}