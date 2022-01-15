package pl.edu.agh.calculationp2p.calculationTask.interfaces;

import java.util.List;

public interface ResultBuilder {
    void reset();
    void performComputation(TaskData data);
    List<String> getResult();
}
