package pl.edu.agh.calculationp2p.state;

import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;

//TODO: implement

public class TaskPublisher implements Publisher{
    public TaskPublisher(){
        throw new UnsupportedOperationException("Will be implemented");
    }

    public void subscribe(int taskId, Future flag, Thread observer){
        throw new UnsupportedOperationException("Will be implemented");
    }

    public void unsubscribe(Thread thread){
        throw new UnsupportedOperationException("Will be implemented");
    }

    @Override
    public void look(Thread changing, TaskRecord previous, TaskRecord current) {
        throw new UnsupportedOperationException("Will be implemented");
    }
}
