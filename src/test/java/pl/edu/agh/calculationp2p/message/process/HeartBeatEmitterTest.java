package pl.edu.agh.calculationp2p.message.process;

import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.network.router.*;

import java.net.InetSocketAddress;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

class HeartBeatEmitterTest {

    @Test
    void beat() {

        int sleepTime = 1500;
        int periodTime = 1000;

        Router router = new Router() {
            @Override
            public int getMainServerId() {
                return 0;
            }

            @Override
            public int getUnknownId() {
                return 0;
            }

            @Override
            public int getBroadcastId() {
                return 0;
            }

            @Override
            public void createInterface(int nodeId, InetSocketAddress ipAddress) {}
            @Override
            public void createInterface(int nodeId) {}
            @Override
            public void deleteInterface(int nodeId) {}
            @Override
            public List<Message> getMessage() {return null;}
            @Override
            public void send(Message message) {}
            @Override
            public void setId(int id) {}
            @Override
            public int getId() {return 0;}
            @Override
            public void close() {}
        };

        HeartBeatEmitter heartBeatEmitter = new HeartBeatEmitter(periodTime, router);

        assertFalse(heartBeatEmitter.beat());
        try {
            sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(heartBeatEmitter.beat());

    }

    @Test
    void nextBeatTime() {
        int timePeriod = 1000;
        int sleepTime = 500;
        Router router = new Router() {
            @Override
            public int getMainServerId() {
                return 0;
            }

            @Override
            public int getUnknownId() {
                return 0;
            }

            @Override
            public int getBroadcastId() {
                return 0;
            }

            @Override
            public void createInterface(int nodeId, InetSocketAddress ipAddress) {}
            @Override
            public void createInterface(int nodeId) {}
            @Override
            public void deleteInterface(int nodeId) {}
            @Override
            public List<Message> getMessage() {return null;}
            @Override
            public void send(Message message) {}
            @Override
            public void setId(int id) {}
            @Override
            public int getId() {return 0;}
            @Override
            public void close() {}
        };

        HeartBeatEmitter heartBeatEmitter = new HeartBeatEmitter(timePeriod, router);
        heartBeatEmitter.beat();

        try {
            sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int result = heartBeatEmitter.nextBeatTime();
        assertTrue(timePeriod-sleepTime+20>result && timePeriod-sleepTime-20<result);

    }
}