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

        List<Integer> integerListEmpty = new ArrayList<>();
        Map<Integer, InetSocketAddress> mapEmpty = new HashMap<>();

        List<Integer> integerListOne = new ArrayList<>(List.of(1));
        Map<Integer, InetSocketAddress> mapOne = new HashMap<>();
        mapOne.put(2, new InetSocketAddress("192.168.0.1", 2000));

        List<Integer> integerListFew = new ArrayList<>(List.of(1,2));
        Map<Integer, InetSocketAddress> mapFew = new HashMap<>();
        mapFew.put(3, new InetSocketAddress("192.168.0.2", 2000));
        mapFew.put(4, new InetSocketAddress("192.168.0.3", 2000));

        Body giveInitEmpty = new GiveInit(5, integerListEmpty, mapEmpty);
        Body giveInitOne = new GiveInit(5, integerListOne, mapOne);
        Body giveInitFew = new GiveInit(5, integerListFew, mapFew);

        String resultEmpty = "{\"your_new_id\":5,\"public_nodes\":[],\"private_nodes\":[]}";
        String resultOne = "{\"your_new_id\":5,\"public_nodes\":[{\"id\":2,\"ip_address\":\"192.168.0.1\",\"port\":\"2000\"}],\"private_nodes\":[{\"id\":1}]}";
        String resultFew = "{\"your_new_id\":5,\"public_nodes\":[{\"id\":4,\"ip_address\":\"192.168.0.3\",\"port\":\"2000\"},{\"id\":3,\"ip_address\":\"192.168.0.2\",\"port\":\"2000\"}],\"private_nodes\":[{\"id\":1},{\"id\":2}]}";
        String resultFewFlip = "{\"your_new_id\":5,\"public_nodes\":[{\"id\":3,\"ip_address\":\"192.168.0.2\",\"port\":\"2000\"},{\"id\":4,\"ip_address\":\"192.168.0.3\",\"port\":\"2000\"}],\"private_nodes\":[{\"id\":1},{\"id\":2}]}";

        assertEquals(resultEmpty, giveInitEmpty.serializeContent());
        assertEquals(resultOne, giveInitOne.serializeContent());

        assertTrue(resultFew.equals(giveInitFew.serializeContent()) || resultFewFlip.equals(giveInitFew.serializeContent()));

    }

    @Test
    void process() {
    }
}