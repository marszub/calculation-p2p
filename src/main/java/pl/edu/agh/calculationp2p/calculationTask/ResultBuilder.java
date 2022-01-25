package pl.edu.agh.calculationp2p.calculationTask;

public interface ResultBuilder {
    void reset();
    void performComputation(TaskData data);
    TaskResult getResult();
}
