package pl.edu.agh.calculationp2p.state.future;

import pl.edu.agh.calculationp2p.state.task.TaskRecord;

public class Observation {
    private final TaskRecord task;
    private final Future<Observation> nextObservation;

    public Observation(TaskRecord task, Future<Observation> nextObservation) {
        this.task = task;
        this.nextObservation = nextObservation;
    }

    public TaskRecord getTask(){
        return task;
    }

    public Future<Observation> getNextObservation(){
        return nextObservation;
    }
}
