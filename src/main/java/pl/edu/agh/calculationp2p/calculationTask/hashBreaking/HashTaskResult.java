package pl.edu.agh.calculationp2p.calculationTask.hashBreaking;

import pl.edu.agh.calculationp2p.calculationTask.TaskResult;

import java.util.LinkedList;
import java.util.List;

public class HashTaskResult implements TaskResult {
    List<String> matchedStrings = new LinkedList<>();

    public HashTaskResult() {
    }

    public void add(String string)
    {
        matchedStrings.add(string);
    }

    @Override
    public String serialize() {
        String res = "";
        for(int i=0;i<matchedStrings.size();i++){
            res = res.concat(matchedStrings.get(i));
            if(i<matchedStrings.size()-1)
                res = res.concat(":");
        }
        return res;
    }
}
