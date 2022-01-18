package pl.edu.agh.calculationp2p.message.body;

import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;

import java.util.*;
import java.util.List;

public class GiveSynchronization implements Body{

    private final List<TaskRecord> currStateList;

    public GiveSynchronization(List<TaskRecord> listOfTasks) {
        this.currStateList = listOfTasks;
    }

    public List<TaskRecord> getCurrStateList() {
        return currStateList;
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
        //TODO:
        currStateList.forEach(record -> {
            //context.getStateUpdater().updateTask(record);
        });
    }

    @Override
    public Body clone() {
        return new GiveSynchronization(new ArrayList<>(this.currStateList));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        GiveSynchronization message = (GiveSynchronization) o;
        return compareStateList(message.getCurrStateList());
    }

    private boolean compareStateList(List<TaskRecord> list){
        boolean flag = false;
        for(TaskRecord own: this.currStateList){
            flag = false;
            for(TaskRecord out: list){
                if (own.equals(out)) {
                    flag = true;
                    break;
                }
            }
            if(!flag){
                return false;
            }
        }
        return list.size() == this.currStateList.size();
    }
}
