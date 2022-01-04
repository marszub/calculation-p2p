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

class MessageParserTest {

    private final TaskState state = TaskState.Calculated;

    private final List<Integer> taskIdList = new ArrayList<>();

    private final List<Integer> privateNodes = new ArrayList<>();
    private final Map<Integer, InetSocketAddress> publicNode = new HashMap<>();

    List<TaskStateMess> listOfTasks = new ArrayList<>();

    @Test
    void parse() {
        int sender = 1;
        int receiver = 2;
        int taskId = 10;
        Message calculated = new MessageImpl(sender, receiver, new Calculated(taskId, null));
        assertEquals(calculated, MessageParser.parse(calculated.serialize()));

        Integer owner = 20;
        Message confirm = new MessageImpl(sender, receiver, new Confirm(taskId, state, owner, null));
        assertEquals(confirm, MessageParser.parse(confirm.serialize()));

        Message getInit = new MessageImpl(sender, receiver, new GetInit());
        assertEquals(getInit, MessageParser.parse(getInit.serialize()));

        Message getProgress = new MessageImpl(sender, receiver, new GetProgress());
        assertEquals(getProgress, MessageParser.parse(getProgress.serialize()));

        Message getSynchronization = new MessageImpl(sender, receiver, new GetSynchronization(taskIdList));
        assertEquals(getSynchronization, MessageParser.parse(getSynchronization.serialize()));

        int newId = 3;
        Message giveInit = new MessageImpl(sender, receiver, new GiveInit(newId, privateNodes, publicNode));
        assertEquals(giveInit, MessageParser.parse(giveInit.serialize()));

        Message giveProgress = new MessageImpl(sender, receiver, new GiveProgress(null));
        assertEquals(giveProgress, MessageParser.parse(giveProgress.serialize()));

        Message giveSynchronization = new MessageImpl(sender, receiver, new GiveSynchronization(listOfTasks));
        assertEquals(giveSynchronization, MessageParser.parse(giveSynchronization.serialize()));

        Message heartBeat = new MessageImpl(sender, receiver, new HeartBeat());
        assertEquals(heartBeat, MessageParser.parse(heartBeat.serialize()));

        String ip = "192.168.0.1";
        Message hello = new MessageImpl(sender, receiver, new Hello(ip));
        assertEquals(hello, MessageParser.parse(hello.serialize()));

        Message reserve = new MessageImpl(sender, receiver, new Reserve(taskId));
        assertEquals(reserve, MessageParser.parse(reserve.serialize()));

    }


}