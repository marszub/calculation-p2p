package pl.edu.agh.calculationp2p.message.process;

import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

class NodeRegisterTest {

    @Test
    void getOutdatedNodes() {

        int validityTime = 1000;

        NodeRegister nodeRegister = new NodeRegister(validityTime);

        nodeRegister.addPrivateNode(1);
        nodeRegister.addPrivateNode(2);
        nodeRegister.addPrivateNode(3);

        nodeRegister.addPublicNode(4, new InetSocketAddress("127.0.0.1", 2137));

        List<Integer> outdated = nodeRegister.getOutdatedNodes();
        assertEquals(outdated, new ArrayList<>());

        try {
            sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        outdated = nodeRegister.getOutdatedNodes();
        assertEquals(outdated, new ArrayList<>(List.of(1,2,3,4)));

    }

    @Test
    void updateNode() {

        int validityTime = 1000;

        NodeRegister nodeRegister = new NodeRegister(validityTime);

        nodeRegister.addPrivateNode(1);
        nodeRegister.addPrivateNode(2);
        nodeRegister.addPrivateNode(3);

        nodeRegister.addPublicNode(4, new InetSocketAddress("127.0.0.1", 2137));

        List<Integer> outdated = nodeRegister.getOutdatedNodes();
        assertEquals(outdated, new ArrayList<>());

        try {
            sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        nodeRegister.updateNode(2);
        outdated = nodeRegister.getOutdatedNodes();
        assertEquals(outdated, new ArrayList<>(List.of(1,3,4)));

    }


}