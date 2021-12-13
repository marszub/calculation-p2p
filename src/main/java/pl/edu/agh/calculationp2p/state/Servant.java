package pl.edu.agh.calculationp2p.state;

//TODO: Implement

import pl.edu.agh.calculationp2p.calculation.TaskResult;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.future.Observation;
import pl.edu.agh.calculationp2p.state.idle.IdleInterrupter;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;

public class Servant {
    private Progress progress;

    Servant(Progress progress){
        this.progress = progress;
    }

    public TaskRecord getTaskProgress(Integer taskId){
        throw new UnsupportedOperationException("Will be implemented");
    }

    public Progress getProgress(){
        throw new UnsupportedOperationException("Will be implemented");
    }

    public void observeReserved(Future<Observation> observer, IdleInterrupter interrupter){
        throw new UnsupportedOperationException("Will be implemented");
    }

    public void observeCalculated(Future<Observation> observer, IdleInterrupter interrupter){
        throw new UnsupportedOperationException("Will be implemented");
    }

    public void updateProgress(Progress progress){
        throw new UnsupportedOperationException("Will be implemented");
    }

    public Integer getTask(){
        throw new UnsupportedOperationException("Will be implemented");
    }

    public void observeTask(Integer taskId, Future<Void> flag, Thread thread){
        throw new UnsupportedOperationException("Will be implemented");
    }

    public void finishTask(Integer taskId, TaskResult result){
        throw new UnsupportedOperationException("Will be implemented");
    }

    public TaskRecord calculate(Integer taskId, Integer nodeId, TaskResult result){
        throw new UnsupportedOperationException("Will be implemented");
    }

    public TaskRecord reserve(Integer taskId, Integer nodeId){
        throw new UnsupportedOperationException("Will be implemented");
    }
}
