package pl.edu.agh.calculationp2p.calculation;

import pl.edu.agh.calculationp2p.calculationTask.CalculationTask;
import pl.edu.agh.calculationp2p.calculationTask.CalculationTaskIterator;
import pl.edu.agh.calculationp2p.calculationTask.ResultBuilder;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.proxy.TaskGiver;

import java.util.Optional;

public class TaskResolver extends Thread {

    private TaskGiver taskGiver;
    private CalculationTask calculationTask;

    private final int sleepTime = 50;

    public TaskResolver(TaskGiver taskGiver, CalculationTask calculationTask) {
        this.taskGiver = taskGiver;
        this.calculationTask = calculationTask;
    }

    @Override
    public void run() {

        while (true) {
            Future<Optional<Integer>> future = this.taskGiver.getTask();
            while(!future.isReady() || future.get().isEmpty()){
                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                future = this.taskGiver.getTask();
            }
            Integer taskId = future.get().get();
            ResultBuilder resultBuilder = calculationTask.getResultBuilder();

            Future<Void> observer = taskGiver.observeTask(taskId, this);

            resultBuilder.reset();
            CalculationTaskIterator iterator  = calculationTask.getFragmentOfTheTask(taskId);
            while (iterator.hasNext()) {
                if(observer.isReady()){
                    resultBuilder.reset();
                    break;
                }
                resultBuilder.performComputation(iterator.getNext());
            }
            taskGiver.finishTask(taskId, resultBuilder.getResult());
        }
    }
}

