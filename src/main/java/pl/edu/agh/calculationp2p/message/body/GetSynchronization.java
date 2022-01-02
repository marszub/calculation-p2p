package pl.edu.agh.calculationp2p.message.body;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.message.MessageImpl;
import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;
import pl.edu.agh.calculationp2p.message.utils.TaskStateMess;

import java.util.ArrayList;
import java.util.List;

public class GetSynchronization implements Body{

    private final List<Integer> taskIdList;

    public GetSynchronization(List<Integer> taskIdList) {
        this.taskIdList = taskIdList;
    }

    @Override
    public String serializeType() {
        return "\"get_synchronization\"";
    }

    @Override
    public String serializeContent() {
        String result = "";
        result = result.concat("{\"tasks\":[");
        for(int i=0;i<this.taskIdList.size();i++){
            result = result.concat("{\"task_id\":"+this.taskIdList.get(i)+"}");
            if(i<this.taskIdList.size()-1){
                result = result.concat(",");
            }
        }
        result = result.concat("]}");
        return result;
    }

    @Override
    public void process(int sender, MessageProcessContext context) {

        int myId = context.getRouter().getId();
        //TODO
        //Map<Integer, TaskState> progress = context.setStateUpdater();
        List<TaskStateMess> list = new ArrayList<>();
        Message messWithStateOfTasks = new MessageImpl(myId, sender, new GiveSynchronization(list));
        context.getRouter().send(messWithStateOfTasks);

    }
}
