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
        return null;
    }
}
