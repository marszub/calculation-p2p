package pl.edu.agh.calculationp2p.calculationTask;

import pl.edu.agh.calculationp2p.calculation.TaskResult;

import java.util.List;

public interface ResultBuilder {
    void reset();
    void performComputation(TaskData data);
    TaskResult getResult();
}
