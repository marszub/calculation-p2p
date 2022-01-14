package pl.edu.agh.calculationp2p.message;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import pl.edu.agh.calculationp2p.message.body.*;
import pl.edu.agh.calculationp2p.message.utils.TaskStateMess;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;

public class MessageParserImpl implements MessageParser{

    public Message parse(String messageS){

        int sender = -2;
        int receiver = -2;
        Body bodyResult = null;

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

            TypeReference<LinkedHashMap<String,Object>> typeRef = new TypeReference<>() {};

            LinkedHashMap<String,Object> jsonMap = mapper.readValue(messageS, typeRef);

            if(jsonMap != null){

                LinkedHashMap<String, Object> head = (LinkedHashMap<String, Object>) jsonMap.get("header");
                LinkedHashMap<String, Object> body = (LinkedHashMap<String, Object>) jsonMap.get("body");

                sender = Integer.parseInt(head.get("sender").toString());
                receiver = Integer.parseInt(head.get("receiver").toString());
                String messType = head.get("message_type").toString();

                switch (messType) {
                    case "get_init" -> bodyResult = new GetInit();
                    case "give_init" -> bodyResult = funGiveInit(body);
                    case "hello" -> bodyResult = funHello(body);
                    case "get_progress" -> bodyResult = new GetProgress();
                    case "give_progress" -> bodyResult = funGiveProcess(body);
                    case "heart_beat" -> bodyResult = new HeartBeat();
                    case "reserve" -> bodyResult = funReserve(body);
                    case "confirm" -> bodyResult = funConfirm(body);
                    case "calculated" -> bodyResult = funCalculated(body);
                    case "get_synchronization" -> bodyResult = funGetSynchronization(body);
                    case "give_synchronization" -> bodyResult = funGiveSynchronization(body);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new MessageImpl(sender, receiver, bodyResult);
    }
    private static GetSynchronization funGetSynchronization(HashMap<String, Object> jsonMapBody){
        String tasksArrayString = jsonMapBody.get("tasks").toString();
        List<Integer> taskArray = parseStringArrPrivate(tasksArrayString);
        return new GetSynchronization(taskArray);
    }
    private static Hello funHello(HashMap<String, Object> jsonMapBody){
        String ipS = jsonMapBody.get("ip").toString();
        if(ipS.equals("null")){
            ipS = null;
        }
        return new Hello(ipS);
    }
    private static GiveProgress funGiveProcess(HashMap<String, Object> jsonMapBody){
        //TODO: String progress = jsonMapBody.get("progress");
        return new GiveProgress(null);
    }
    private static Reserve funReserve(HashMap<String, Object> jsonMapBody){
        int taskId = Integer.parseInt(jsonMapBody.get("task_id").toString());
        return new Reserve(taskId);
    }
    private static GiveInit funGiveInit(HashMap<String, Object> jsonMapBody){

        int newId = Integer.parseInt(jsonMapBody.get("your_new_id").toString());
        String publicNodesArrayStr = jsonMapBody.get("public_nodes").toString();
        String privateNodesArrayStr = jsonMapBody.get("private_nodes").toString();

        List<Integer> privateNodes = parseStringArrPrivate(privateNodesArrayStr);
        Map<Integer, InetSocketAddress> publicNodes = parseStringArr(publicNodesArrayStr);

        return new GiveInit(newId, privateNodes, publicNodes );
    }
    private static Map<Integer, InetSocketAddress> parseStringArr(String inputArrS){
        Map<Integer, InetSocketAddress> result = new HashMap<>();

        if(inputArrS.equals("[]")){
            return result;
        }

        String[] splitArr = inputArrS.split("},");

        splitArr[0] = splitArr[0].substring(1);
        splitArr[splitArr.length-1] = splitArr[splitArr.length-1].substring(0,splitArr[splitArr.length-1].length()-1);

        for (String oneObjectString : splitArr) {
            String[] oneObject = oneObjectString.split(",");
            int id = Integer.parseInt(oneObject[0].split(":")[1]);
            String ip = oneObject[0].split(":")[1];
            result.put(id, new InetSocketAddress(ip, 2000));
        }

        return result;
    }
    private static List<Integer> parseStringArrPrivate(String inputArrS){
        List<Integer> result = new ArrayList<>();
        if(inputArrS.equals("[]")){
            return result;
        }
        String[] splitArr = inputArrS.split("},");

        splitArr[0] = splitArr[0].substring(2);
        splitArr[splitArr.length-1] = splitArr[splitArr.length-1].substring(0,splitArr[splitArr.length-1].length()-2);

        for (String oneObject : splitArr) {
            int id = Integer.parseInt(oneObject.split(":")[1]);
            result.add(id);
        }

        return result;
    }
    private static GiveSynchronization funGiveSynchronization(HashMap<String, Object> jsonMapBody){
        String tasksArray = jsonMapBody.get("tasks").toString();
        List<TaskStateMess> list = getTasksArrFromString(tasksArray);
        return new GiveSynchronization(list);
    }
    private static List<TaskStateMess> getTasksArrFromString(String input){

        List<TaskStateMess> result = new ArrayList<>();

        if(Objects.equals(input, "[]")){
            return result;
        }

        String[] splitArr = input.split("},");

        splitArr[0] = splitArr[0].substring(1);
        splitArr[splitArr.length-1] = splitArr[splitArr.length-1].substring(0,splitArr[splitArr.length-1].length()-1);

        for (String oneObjectString : splitArr) {
            String[] oneObject = oneObjectString.split(",");

            int taskId = Integer.parseInt(oneObject[0].split(":")[1]);

            TaskState state = null;
            String stateString = oneObject[2].split(":")[1];
            switch (stateString) {
                case "calculated" -> state = TaskState.Calculated;
                case "free" -> state = TaskState.Free;
                case "reserved" -> state = TaskState.Reserved;
            }
            Integer owner = Integer.parseInt(oneObject[2].split(":")[1]);
            //TODO: TaskResult taskResult = oneObject[2].split(":")[1];
            result.add(new TaskStateMess(taskId, state, owner));
        }

        return result;
    }
    private static Calculated funCalculated(HashMap<String, Object> jsonMapBody){
        //TODO:
        int taskId = Integer.parseInt(jsonMapBody.get("task_id").toString());
        //String result = jsonMapBody.get("result");
        return new Calculated(taskId, null);
    }
    private static Confirm funConfirm(HashMap<String, Object> jsonMapBody){
        int taskId = Integer.parseInt(jsonMapBody.get("task_id").toString());
        TaskState state = null;
        String stateString = jsonMapBody.get("state").toString();
        switch (stateString) {
            case "calculated" -> state = TaskState.Calculated;
            case "free" -> state = TaskState.Free;
            case "reserved" -> state = TaskState.Reserved;
        }
        String ownerStr = jsonMapBody.get("owner").toString();
        Integer owner;
        if(ownerStr.equals("null")){
            owner = null;
        } else {
            owner = Integer.parseInt(ownerStr);
        }
        //TODO:
        jsonMapBody.get("result");
        return new Confirm(taskId, state, owner, null);
    }
}
