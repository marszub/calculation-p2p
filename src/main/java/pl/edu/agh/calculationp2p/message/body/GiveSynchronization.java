package pl.edu.agh.calculationp2p.message.body;

import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;
import pl.edu.agh.calculationp2p.message.utils.TaskStateMess;

import java.util.List;

public class GiveSynchronization implements Body{

    private List<TaskStateMess> currStateList;

    public GiveSynchronization(List<TaskStateMess> listOfTasks) {

        this.currStateList = listOfTasks;

    }

    @Override
    public String serializeType() {
        return "\"give_synchronization\"";
    }

    @Override
    public String serializeContent() {

        String result = "";
        result = result.concat("{\"tasks\":[");

        for(int i=0;i<this.currStateList.size();i++){
            result = result.concat(this.currStateList.get(i).serialize());
            if(i<this.currStateList.size()-1){
                result = result.concat(",");
            }
        }

        result = result.concat("]}");
        return result;

    }

    @Override
    public void process(int sender, MessageProcessContext context) {

    }
}
