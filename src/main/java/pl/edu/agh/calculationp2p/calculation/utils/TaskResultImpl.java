package pl.edu.agh.calculationp2p.calculation.utils;

import pl.edu.agh.calculationp2p.calculation.utils.TaskResult;

import java.util.LinkedList;
import java.util.List;

public class TaskResultImpl implements TaskResult {
    List<String> matchedStrings = new LinkedList<>();

    public TaskResultImpl() {
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
