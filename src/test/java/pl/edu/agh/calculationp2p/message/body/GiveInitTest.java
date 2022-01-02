package pl.edu.agh.calculationp2p.message.body;

import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GiveInitTest {

    @Test
    void serializeType() {

        List<Integer> integerList = new ArrayList<>(5);
        Map<Integer, InetSocketAddress> map = new HashMap<>();
        map.put(5, new InetSocketAddress(2000));

        Body giveInit = new GiveInit(2, integerList, map);
        assertEquals("\"give_init\"", giveInit.serializeType());

    }

    @Test
    void serializeContent() {
    }

    @Test
    void process() {
    }
}