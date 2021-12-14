package pl.edu.agh.calculationp2p.state;

//TODO: Implement

import pl.edu.agh.calculationp2p.calculation.TaskResult;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.future.Observation;
import pl.edu.agh.calculationp2p.state.idle.IdleInterrupter;
import pl.edu.agh.calculationp2p.state.publisher.CalculatedPublisher;
import pl.edu.agh.calculationp2p.state.publisher.ReservedPublisher;
import pl.edu.agh.calculationp2p.state.publisher.TaskPublisher;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;

public class ServantImpl implements Servant {
    private final Progress progress;
    private final TaskPublisher taskPublisher;
    private final ReservedPublisher reservedPublisher;
    private final CalculatedPublisher calculatedPublisher;

    ServantImpl(Progress progress, TaskPublisher taskPublisher, ReservedPublisher reservedPublisher, CalculatedPublisher calculatedPublisher){
        this.progress = progress;
        this.taskPublisher = taskPublisher;
        this.reservedPublisher = reservedPublisher;
        this.calculatedPublisher = calculatedPublisher;
    }

    @Override
    public TaskRecord getTaskProgress(Integer taskId){
        throw new UnsupportedOperationException("Will be implemented");
    }

    @Override
    public Progress getProgress(){
        throw new UnsupportedOperationException("Will be implemented");
    }

    @Override
    public void observeReserved(Future<Observation> observer, IdleInterrupter interrupter){
        throw new UnsupportedOperationException("Will be implemented");
    }

    @Override
    public void observeCalculated(Future<Observation> observer, IdleInterrupter interrupter){
        throw new UnsupportedOperationException("Will be implemented");
    }

    @Override
    public void updateProgress(Progress progress){
        throw new UnsupportedOperationException("Will be implemented");
    }

    @Override
    public Integer getTask(){
        throw new UnsupportedOperationException("Will be implemented");
    }

    @Override
    public void observeTask(Integer taskId, Future<Void> flag, Thread thread){
        throw new UnsupportedOperationException("Will be implemented");
    }

    @Override
    public void finishTask(Integer taskId, TaskResult result){
        throw new UnsupportedOperationException("Will be implemented");
    }

    @Override
    public TaskRecord calculate(Integer taskId, Integer nodeId, TaskResult result){
        throw new UnsupportedOperationException("Will be implemented");
    }

    @Override
    public TaskRecord reserve(Integer taskId, Integer nodeId){
        throw new UnsupportedOperationException("Will be implemented");
    }
}
