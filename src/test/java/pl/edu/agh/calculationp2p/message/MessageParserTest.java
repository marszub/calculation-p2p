package pl.edu.agh.calculationp2p.message;

import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.calculationTask.TaskResult;
import pl.edu.agh.calculationp2p.calculationTask.hashBreaking.HashTaskResult;
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

    MessageParser messageParser = new MessageParserImpl();

    @Test
    void parseCalculated() {
//        TaskRecord taskRecord = new TaskRecord();
//        Message calculated = new MessageImpl(1, 2, new Calculated(taskRecord));
//        assertEquals(calculated, messageParser.parse(calculated.serialize()));
//
//        Message calculated1 = new MessageImpl(1, 2, null);
//        assertEquals(calculated1, messageParser.parse(calculated1.serialize()));
//
//        Message calculated2 = new MessageImpl(1, 2, new Calculated(null));
//        assertEquals(calculated2, messageParser.parse(calculated2.serialize()));
    }

    @Test
    void parseConfirm() {
        Message confirm = new MessageImpl(1, 2, null);
        //assertEquals(confirm, messageParser.parse(confirm.serialize()));

        Message confirm1 = new MessageImpl(1, 2, new Confirm(null));
        //assertEquals(confirm1, messageParser.parse(confirm1.serialize()));

        Message confirm2 = new MessageImpl(1, 2, new Confirm(new TaskRecord(1, TaskState.Reserved, 20, null)));
        assertEquals(confirm2, messageParser.parse(confirm2.serialize()));

        TaskResult taskResult = new HashTaskResult();
        Message confirm3 = new MessageImpl(1, 2, new Confirm(new TaskRecord(1, TaskState.Reserved, 20, taskResult)));
        assertEquals(confirm3, messageParser.parse(confirm3.serialize()));

        taskResult.add("111");
        Message confirm4 = new MessageImpl(1, 2, new Confirm(new TaskRecord(1, TaskState.Reserved, 20, taskResult)));
        assertEquals(confirm4, messageParser.parse(confirm4.serialize()));

        taskResult.add("121");
        Message confirm5 = new MessageImpl(1, 2, new Confirm(new TaskRecord(1, TaskState.Reserved, 20, taskResult)));
        assertEquals(confirm5, messageParser.parse(confirm5.serialize()));
    }


    @Test
    void parseGetInit() {
        Message getInit = new MessageImpl(1, 2, new GetInit());
        assertEquals(getInit, messageParser.parse(getInit.serialize()));
    }

    @Test
    void parseGetProgress() {
        Message getProgress = new MessageImpl(1, 2, new GetProgress());
        assertEquals(getProgress, messageParser.parse(getProgress.serialize()));
    }


    @Test
    void parseGetSynchronization() {
        Message getSynchronization = new MessageImpl(1, 2, new GetSynchronization(new ArrayList<>()));
        assertEquals(getSynchronization, messageParser.parse(getSynchronization.serialize()));

        Message getSynchronization1 = new MessageImpl(1, 2, new GetSynchronization(null));
        //assertEquals(getSynchronization1, messageParser.parse(getSynchronization1.serialize()));

        Message getSynchronization2 = new MessageImpl(1, 2, new GetSynchronization(new ArrayList<>(List.of(1,2,3,4))));
        assertEquals(getSynchronization2, messageParser.parse(getSynchronization2.serialize()));
    }

    @Test
    void parseGiveInit() {
        Message giveInit0 = new MessageImpl(1, 2, new GiveInit(1, null, null));
        //assertEquals(giveInit0, messageParser.parse(giveInit0.serialize()));

        Map<Integer, InetSocketAddress> map = new HashMap<>();
        Message giveInit = new MessageImpl(1, 2, new GiveInit(1, new ArrayList<>(), map));
        assertEquals(giveInit, messageParser.parse(giveInit.serialize()));

        map.put(5, new InetSocketAddress("127.0.0.1", 2137));
        Message giveInit1 = new MessageImpl(1, 2, new GiveInit(1, new ArrayList<>(List.of(1,2,3,4)), map));
        assertEquals(giveInit1, messageParser.parse(giveInit1.serialize()));

    }


    @Test
    void parseGiveProgress() {
        Message giveProgress = new MessageImpl(1, 2, new GiveProgress(new Progress(new ArrayList<>())));
        assertEquals(giveProgress, messageParser.parse(giveProgress.serialize()));

        Message giveProgress1 = new MessageImpl(1, 2, new GiveProgress(
                new Progress(
                        new ArrayList<>(
                                List.of(
                                        new TaskRecord(1, TaskState.Free, 2, null)
                                )
                        )
                )));
        assertEquals(giveProgress1, messageParser.parse(giveProgress1.serialize()));

        TaskResult result = new HashTaskResult();
        Message giveProgress2 = new MessageImpl(1, 2, new GiveProgress(
                new Progress(
                        new ArrayList<>(
                                List.of(
                                        new TaskRecord(1, TaskState.Free, 2, result)
                                )
                        )
                )));
        assertEquals(giveProgress2, messageParser.parse(giveProgress2.serialize()));

        result.add("111");
        Message giveProgress3 = new MessageImpl(1, 2, new GiveProgress(
                new Progress(
                        new ArrayList<>(
                                List.of(
                                        new TaskRecord(1, TaskState.Free, 2, result)
                                )
                        )
                )));
        assertEquals(giveProgress3, messageParser.parse(giveProgress3.serialize()));

        result.add("a");
        Message giveProgress4 = new MessageImpl(1, 2, new GiveProgress(
                new Progress(
                        new ArrayList<>(
                                List.of(
                                        new TaskRecord(1, TaskState.Free, 2, result)
                                )
                        )
                )));
        assertEquals(giveProgress4, messageParser.parse(giveProgress4.serialize()));
    }


    @Test
    void parseGiveSynchronization() {
        Message giveSynchronization = new MessageImpl(1, 2, new GiveSynchronization(null));
        //assertEquals(giveSynchronization, messageParser.parse(giveSynchronization.serialize()));

        Message giveSynchronization1 = new MessageImpl(1, 2, new GiveSynchronization(new ArrayList<>()));
        assertEquals(giveSynchronization1, messageParser.parse(giveSynchronization1.serialize()));

        Message giveSynchronization2 = new MessageImpl(1, 2, new GiveSynchronization(new ArrayList<>(
                List.of(new TaskRecord(1, TaskState.Free, 2, null))
        )));
        assertEquals(giveSynchronization2, messageParser.parse(giveSynchronization2.serialize()));

        TaskResult result = new HashTaskResult();
        Message giveSynchronization3 = new MessageImpl(1, 2, new GiveSynchronization(new ArrayList<>(
                List.of(new TaskRecord(1, TaskState.Free, 2, result))
        )));
        assertEquals(giveSynchronization3, messageParser.parse(giveSynchronization3.serialize()));

        result.add("111");
        Message giveSynchronization4 = new MessageImpl(1, 2, new GiveSynchronization(new ArrayList<>(
                List.of(new TaskRecord(1, TaskState.Free, 2, result))
        )));
        assertEquals(giveSynchronization4, messageParser.parse(giveSynchronization4.serialize()));
    }

    @Test
    void parseHeartBeat() {
        Message heartBeat = new MessageImpl(1, 2, new HeartBeat());
        assertEquals(heartBeat, messageParser.parse(heartBeat.serialize()));
    }

    @Test
    void parseHello() {
        String ip = "192.168.0.1";
        Message hello = new MessageImpl(1, 2, new Hello(new InetSocketAddress(ip, 2137)));
        assertEquals(hello, messageParser.parse(hello.serialize()));

        Message hello1 = new MessageImpl(1, 2, new Hello(null));
        assertEquals(hello1, messageParser.parse(hello1.serialize()));
    }

    @Test
    void parseReserve() {
        Message reserve1 = new MessageImpl(1, 2, new Reserve(new TaskRecord(new TaskRecord(2, TaskState.Free, 3, new HashTaskResult()))));
        assertEquals(reserve1, messageParser.parse(reserve1.serialize()));

        Message reserve = new MessageImpl(1, 2, new Reserve(new TaskRecord(new TaskRecord(2, TaskState.Free, 3, null))));
        assertEquals(reserve, messageParser.parse(reserve.serialize()));
    }






}