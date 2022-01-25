package pl.edu.agh.calculationp2p.calculationTask;

import pl.edu.agh.calculationp2p.calculation.utils.TaskResult;

public interface ResultBuilder {
    void reset();
    void performComputation(TaskData data);
    TaskResult getResult();
}
