package pl.edu.agh.calculationp2p.message;

import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.message.body.*;
import pl.edu.agh.calculationp2p.state.Progress;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
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

    List<TaskRecord> listOfTasks = new ArrayList<>();

    // TODO: rewrite

//    @Test
//    void parse() {
//        MessageParser messageParser = new MessageParserImpl();
//        int sender = 1;
//        int receiver = 2;
//        int taskId = 10;
//        TaskRecord taskRecord = new TaskRecord();
//        Message calculated = new MessageImpl(sender, receiver, new Calculated(taskRecord));
//        assertEquals(calculated, messageParser.parse(calculated.serialize()));
//
//        Integer owner = 20;
//        Message confirm = new MessageImpl(sender, receiver, new Confirm(new TaskRecord(taskId, state, owner, null)));
//        assertEquals(confirm, messageParser.parse(confirm.serialize()));
//
//        Message getInit = new MessageImpl(sender, receiver, new GetInit());
//        assertEquals(getInit, messageParser.parse(getInit.serialize()));
//
//        Message getProgress = new MessageImpl(sender, receiver, new GetProgress());
//        assertEquals(getProgress, messageParser.parse(getProgress.serialize()));
//
//        Message getSynchronization = new MessageImpl(sender, receiver, new GetSynchronization(taskIdList));
//        assertEquals(getSynchronization, messageParser.parse(getSynchronization.serialize()));
//
//        int newId = 3;
//        Message giveInit = new MessageImpl(sender, receiver, new GiveInit(newId, privateNodes, publicNode));
//        assertEquals(giveInit, messageParser.parse(giveInit.serialize()));
//
//        Message giveProgress = new MessageImpl(sender, receiver, new GiveProgress(new Progress(new ArrayList<>())));
//        assertEquals(giveProgress, messageParser.parse(giveProgress.serialize()));
//
//        Message giveSynchronization = new MessageImpl(sender, receiver, new GiveSynchronization(listOfTasks));
//        assertEquals(giveSynchronization, messageParser.parse(giveSynchronization.serialize()));
//
//        Message heartBeat = new MessageImpl(sender, receiver, new HeartBeat());
//        assertEquals(heartBeat, messageParser.parse(heartBeat.serialize()));
//
//        String ip = "192.168.0.1";
//        Message hello = new MessageImpl(sender, receiver, new Hello(new InetSocketAddress(ip, 2137)));
//        assertEquals(hello, messageParser.parse(hello.serialize()));
//
//        Message reserve = new MessageImpl(sender, receiver, new Reserve(new TaskRecord(new TaskRecord(2, TaskState.Free, 3, null))));
//        assertEquals(reserve, messageParser.parse(reserve.serialize()));
//
//    }


}