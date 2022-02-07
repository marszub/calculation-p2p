package pl.edu.agh.calculationp2p.message;

import org.json.JSONArray;
import org.json.JSONObject;
import pl.edu.agh.calculationp2p.calculationTask.TaskResult;
import pl.edu.agh.calculationp2p.calculationTask.hashBreaking.HashTaskResult;
import pl.edu.agh.calculationp2p.message.body.*;
import pl.edu.agh.calculationp2p.state.Progress;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import java.net.InetSocketAddress;
import java.util.*;

public class MessageParserImpl implements MessageParser{

    public Message parse(String messageS){
        int sender = -2;
        int receiver = -2;
        Body bodyResult = null;
        JSONObject jsonObject = new JSONObject(messageS);
        JSONObject headObj = jsonObject.getJSONObject("header");
        JSONObject bodyObj = jsonObject.getJSONObject("body");
        sender = headObj.getInt("sender");
        receiver = headObj.getInt("receiver");
        String messType = headObj.getString("message_type");
        switch (messType) {
            case "get_init" -> bodyResult = new GetInit();
            case "give_init" -> bodyResult = funGiveInit(bodyObj);
            case "hello" -> bodyResult = funHello(bodyObj);
            case "get_progress" -> bodyResult = new GetProgress();
            case "give_progress" -> bodyResult = funGiveProcess(bodyObj);
            case "heart_beat" -> bodyResult = new HeartBeat();
            case "reserve" -> bodyResult = funReserve(bodyObj);
            case "confirm" -> bodyResult = funConfirm(bodyObj);
            case "calculated" -> bodyResult = funCalculated(bodyObj);
            case "get_synchronization" -> bodyResult = funGetSynchronization(bodyObj);
            case "give_synchronization" -> bodyResult = funGiveSynchronization(bodyObj);
        }
        return new MessageImpl(sender, receiver, bodyResult);
    }
    private static GetSynchronization funGetSynchronization(JSONObject jsonMapBody){
        JSONArray tasksArrayString = jsonMapBody.getJSONArray("tasks");
        List<Integer> result = new ArrayList<>();
        for(int i=0;i<tasksArrayString.length(); i++){
            result.add(tasksArrayString.getJSONObject(i).getInt("task_id"));
        }
        return new GetSynchronization(result);
    }

    private static Hello funHello(JSONObject jsonMapBody){

        String ipS = jsonMapBody.getString("ip");
        if(ipS.equals("null"))
            return new Hello(null);
        int portS = jsonMapBody.getInt("port");
        return new Hello(new InetSocketAddress(ipS, portS));
    }


    private static GiveProgress funGiveProcess(JSONObject jsonMapBody){
        JSONArray progress = jsonMapBody.getJSONArray("progress");
        if(progress.isEmpty()){
            return new GiveProgress(new Progress(new ArrayList<>()));
        }
        List<TaskRecord> result = new ArrayList<>();
        for(int i=0;i<progress.length();i++) {
            JSONObject record = progress.getJSONObject(i);
            JSONArray taskRecordResultStr = record.getJSONArray("result");
            List<String> resultArray = new ArrayList<>();
            for(int j=0;j<taskRecordResultStr.length();i++){
                resultArray.add(taskRecordResultStr.getString(i));
            }
            result.add(new TaskRecord(
                    record.getInt("task_id"),
                    TaskState.valueOf(record.getString("state")),
                    record.getInt("owner"),
                    new HashTaskResult(resultArray)

            ));
        }
        return new GiveProgress(new Progress(result));
    }

    private static Reserve funReserve(JSONObject jsonMapBody){
        int taskId = jsonMapBody.getInt("task_id");
        int owner = jsonMapBody.getInt("owner");
        String stateStr = jsonMapBody.getString("state");
        TaskState taskState = TaskState.valueOf(stateStr);
        //TODO:

        return new Reserve(new TaskRecord(taskId, taskState, owner, null));
    }

    private static GiveInit funGiveInit(JSONObject jsonMapBody){
        int newId = jsonMapBody.getInt("your_new_id");
        JSONArray privateNodesArrayStr = jsonMapBody.getJSONArray("private_nodes");
        List<Integer> privateNodes = new ArrayList<>();
        for(int i=0;i<privateNodesArrayStr.length();i++){
            privateNodes.add(privateNodesArrayStr.getJSONObject(i).getInt("id"));
        }
        JSONArray publicNodes = jsonMapBody.getJSONArray("public_nodes");
        Map<Integer, InetSocketAddress> publicNodesRes = new HashMap<>();
        for(int i=0;i<publicNodes.length();i++){
            JSONObject record = publicNodes.getJSONObject(i);
            publicNodesRes.put(record.getInt("id"), new InetSocketAddress(record.getString("ip_address"), record.getInt("port")));
        }
        return new GiveInit(newId, privateNodes, publicNodesRes);
    }

    private static GiveSynchronization funGiveSynchronization(JSONObject jsonMapBody){
        JSONArray tasksArray = jsonMapBody.getJSONArray("tasks");
        List<TaskRecord> list = new ArrayList<>();
        for(int i=0;i<tasksArray.length();i++){
            JSONObject record = tasksArray.getJSONObject(i);
            TaskResult res = new HashTaskResult();
            if(record.get("result").toString().equals("null")){
                res = new HashTaskResult();
            } else {
                JSONArray jsonArray = record.getJSONArray("result");
                for(int j=0;j<jsonArray.length();j++) {
                    res.add(String.valueOf(jsonArray.getInt(i)));
                }
            }
            list.add(new TaskRecord(record.getInt("task_id"), TaskState.valueOf(record.getString("state")), record.getInt("owner"), res));
        }
        return new GiveSynchronization(list);
    }

    private static Calculated funCalculated(JSONObject jsonMapBody){
        int taskId = jsonMapBody.getInt("task_id");
        int owner = jsonMapBody.getInt("owner");
        TaskState taskState = TaskState.valueOf(jsonMapBody.getString("state"));
        JSONArray taskResultStr = jsonMapBody.getJSONArray("result");
        List<String> taskResultList = new ArrayList<>();
        for(int i=0;i<taskResultStr.length();i++){
            taskResultList.add(taskResultStr.getString(i));

        }
        TaskResult taskResult = new HashTaskResult();
        taskResultList.forEach(taskResult::add);
        TaskRecord result = new TaskRecord(taskId, taskState, owner, taskResult);
        return new Calculated(result);
    }

    private static Confirm funConfirm(JSONObject jsonMapBody){
        int taskId = jsonMapBody.getInt("task_id");
        TaskState state = TaskState.valueOf(jsonMapBody.getString("state"));
        int owner = jsonMapBody.getInt("owner");
        TaskResult taskResult;
        if(jsonMapBody.get("result").toString().equals("null")){
            taskResult = null;

        } else {
            JSONArray taskResultStr = jsonMapBody.getJSONArray("result");
            List<String> res = new ArrayList<>();
            for(int i=0;i<taskResultStr.length();i++){
                res.add(String.valueOf(taskResultStr.getInt(i)));
            }
            taskResult = new HashTaskResult();
            res.forEach(taskResult::add);
        }
        return new Confirm(new TaskRecord(taskId, state, owner, taskResult));
    }
}
