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
        return null;
    }
}