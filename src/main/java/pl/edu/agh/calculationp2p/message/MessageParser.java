package pl.edu.agh.calculationp2p.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import pl.edu.agh.calculationp2p.message.body.*;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MessageParser {

    static Message parse(String messageS){

        int sender = -2;
        int receiver = -2;
        Body bodyResult = null;

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        Map<String,String> jsonMap = null;
        try {
            jsonMap = objectMapper.readValue(messageS, Map.class);
            if(jsonMap != null){
                String head = jsonMap.get("header");
                String body = jsonMap.get("body");

                Map<String,String> jsonMapHead = objectMapper.readValue(head, Map.class);
                Map<String,String> jsonMapBody = objectMapper.readValue(body, Map.class);

                sender = Integer.parseInt(jsonMapHead.get("sender"));
                receiver = Integer.parseInt(jsonMapHead.get("receiver"));
                String messType = jsonMapHead.get("message_type");

                switch (messType) {
                    case "get_init" -> bodyResult = new GetInit();
                    case "give_init" -> bodyResult = funGiveInit(jsonMapBody);
                    case "hello" -> bodyResult = funHello(jsonMapBody);
                    case "get_progress" -> bodyResult = new GetProgress();
                    case "give_progress" -> bodyResult = funGiveProcess(jsonMapBody);
                    case "heart_beat" -> bodyResult = new HeartBeat();
                    case "reserve" -> bodyResult = funReserve(jsonMapBody);
                    case "confirm" -> bodyResult = funConfirm(jsonMapBody);
                    case "calculated" -> bodyResult = funCalculated(jsonMapBody);
                    case "get_synchronization" -> bodyResult = funGetSynchronization(jsonMapBody);
                    case "give_synchronization" -> bodyResult = funGiveSynchronization(jsonMapBody);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new MessageImpl(sender, receiver, bodyResult);
    }
    private static Hello funHello(Map<String, String> jsonMapBody){
        String ipS = jsonMapBody.get("ip");
        if(ipS.equals("null")){
            ipS = null;
        }
        return new Hello(ipS);
    }
    private static Reserve funReserve(Map<String, String> jsonMapBody){
        int taskId = Integer.parseInt(jsonMapBody.get("task_id"));
        return new Reserve(taskId);
    }


    private static GiveInit funGiveInit(Map<String, String> jsonMapBody){
        //    "public_nodes":
        //    [
        //        {"id": <node_id>, "ip_address":<ip_address>}
        //    ],
        //    "private_nodes":
        //    [
        //        {"id": <node_id>}
        //    ]
        int newId = Integer.parseInt(jsonMapBody.get("your_new_id"));
        String publicNodesArrayStr = jsonMapBody.get("public_nodes");
        String privateNodesArrayStr = jsonMapBody.get("private_nodes");
        List<Integer> privateNodes = null;
        Map<Integer, InetSocketAddress> publicNodes = null;
        return new GiveInit(newId, privateNodes, publicNodes );
    }
    private static Confirm funConfirm(Map<String, String> jsonMapBody){
        int taskId = Integer.parseInt(jsonMapBody.get("task_id"));

        jsonMapBody.get("state");

        String ownerStr = jsonMapBody.get("owner");
        Integer owner;
        if(ownerStr == "null"){
            owner = null;
        } else {
            owner = Integer.parseInt(ownerStr);
        }

        jsonMapBody.get("result");

        //"body":
        //{
        //    "task_id": <task_id>,
        //    "state": <"free" | "reserved" | "calculated">,
        //    "owner": <null | node_id>,
        //    "result": <null | result_obj>
        //}
        return null;
        //return new Confirm(new TaskRecord());
    }
    private static GiveSynchronization funGiveSynchronization(Map<String, String> jsonMapBody){
        //    "tasks": [
        //        {
        //            "task_id": <task_id>,
        //            "state": <"free" | "reserved" | "calculated">,
        //            "owner": <null | node_id>,
        //            "result": <null | result_obj>
        //        }
        //    ]
        String tasksArray = jsonMapBody.get("tasks");
        return new GiveSynchronization();
    }
    private static GetSynchronization funGetSynchronization(Map<String, String> jsonMapBody){
        String tasksArray = jsonMapBody.get("tasks");
        return new GetSynchronization(new ArrayList<>());
    }
    private static GiveProcess funGiveProcess(Map<String, String> jsonMapBody){
        String progress = jsonMapBody.get("progress");
        return new GiveProcess();
    }
    private static Calculated funCalculated(Map<String, String> jsonMapBody){
        int taskId = Integer.parseInt(jsonMapBody.get("task_id"));
        String result = jsonMapBody.get("result");
        return null;
        //return new Calculated(taskId, new TaskResult());
    }


}

