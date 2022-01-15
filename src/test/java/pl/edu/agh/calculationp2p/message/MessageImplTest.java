package pl.edu.agh.calculationp2p.message;

import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.message.body.*;
import pl.edu.agh.calculationp2p.message.utils.TaskStateMess;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MessageImplTest {
    private final TaskState state = TaskState.Calculated;

    private final List<Integer> taskIdList = new ArrayList<>();

    private final List<Integer> privateNodes = new ArrayList<>();
    private final Map<Integer, InetSocketAddress> publicNode = new HashMap<>();

    List<TaskStateMess> listOfTasks = new ArrayList<>();


    @Test
    void testClone() {

        int sender = 1;
        int receiver = 2;
        int newReceiver = 5;
        int taskId = 10;
        Message calculated = new MessageImpl(sender, receiver, new Calculated(taskId, null));
        Message calculatedClone = calculated.clone(newReceiver);
        // because of changed receiver
        assertNotEquals(calculated, calculatedClone);
        assertNotSame(calculated, calculatedClone);
        assertEquals(newReceiver, calculatedClone.getReceiver());

        Integer owner = 20;
        Message confirm = new MessageImpl(sender, receiver, new Confirm(taskId, state, owner, null));
        Message confirmClone = confirm.clone(newReceiver);
        assertNotSame(confirm, confirmClone);
        assertEquals(newReceiver, confirmClone.getReceiver());


        Message getInit = new MessageImpl(sender, receiver, new GetInit());
        Message getInitClone = getInit.clone(newReceiver);
        assertNotSame(getInit, getInitClone);
        assertEquals(newReceiver, getInitClone.getReceiver());

        Message getProgress = new MessageImpl(sender, receiver, new GetProgress());
        Message getProgressClone = getProgress.clone(newReceiver);
        assertNotSame(getProgress, getProgressClone);
        assertEquals(newReceiver, getProgressClone.getReceiver());

        Message getSynchronization = new MessageImpl(sender, receiver, new GetSynchronization(taskIdList));
        Message getSynchronizationClone = getSynchronization.clone(newReceiver);
        assertNotSame(getSynchronization, getSynchronizationClone);
        assertEquals(newReceiver, getSynchronizationClone.getReceiver());

        int newId = 3;
        Message giveInit = new MessageImpl(sender, receiver, new GiveInit(newId, privateNodes, publicNode));
        Message giveInitClone = giveInit.clone(newReceiver);
        assertNotSame(giveInit, giveInitClone);
        assertEquals(newReceiver, giveInitClone.getReceiver());

        Message giveProgress = new MessageImpl(sender, receiver, new GiveProgress(null));
        Message giveProgressClone = giveProgress.clone(newReceiver);
        assertNotSame(giveProgress, giveProgressClone);
        assertEquals(newReceiver, giveProgressClone.getReceiver());

        Message giveSynchronization = new MessageImpl(sender, receiver, new GiveSynchronization(listOfTasks));
        Message giveSynchronizationClone = giveSynchronization.clone(newReceiver);
        assertNotSame(giveSynchronization, giveSynchronizationClone);
        assertEquals(newReceiver, giveSynchronizationClone.getReceiver());

        Message heartBeat = new MessageImpl(sender, receiver, new HeartBeat());
        Message heartBeatClone = heartBeat.clone(newReceiver);
        assertNotSame(heartBeat, heartBeatClone);
        assertEquals(newReceiver, heartBeatClone.getReceiver());

        String ip = "192.168.0.1";
        Message hello = new MessageImpl(sender, receiver, new Hello(ip));
        Message helloClone = hello.clone(newReceiver);
        assertNotSame(hello, helloClone);
        assertEquals(newReceiver, helloClone.getReceiver());

        Message reserve = new MessageImpl(sender, receiver, new Reserve(taskId));
        Message reserveClone = reserve.clone(newReceiver);
        assertNotSame(reserve, reserveClone);
        assertEquals(newReceiver, reserveClone.getReceiver());

    }

}