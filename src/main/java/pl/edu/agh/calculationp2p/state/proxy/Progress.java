package pl.edu.agh.calculationp2p.state.proxy;

import pl.edu.agh.calculationp2p.state.task.TaskRecord;

import java.util.Map;

public interface Progress {

    Map<Integer, TaskRecord> getProgress();

}
