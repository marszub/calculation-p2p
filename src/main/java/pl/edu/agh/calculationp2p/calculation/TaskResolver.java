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
    private final int deadTime = 1000;

    public TaskResolver(TaskGiver taskGiver, CalculationTask calculationTask) {
        this.taskGiver = taskGiver;
        this.calculationTask = calculationTask;
    }

    @Override
    public void run() {
        int currTime = 0;
        while (true) {
            currTime = 0;
            Future<Optional<Integer>> future = this.taskGiver.getTask();
            while(!future.isReady() || future.get().isEmpty()){
                try {
                    currTime+=sleepTime;
                    if(currTime>=deadTime) return;
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Integer taskId = future.get().get();
            Future<Void> observer = taskGiver.observeTask(taskId);
            ResultBuilder resultBuilder = calculationTask.getResultBuilder();

            resultBuilder.reset();
            CalculationTaskIterator iterator  = calculationTask.getFragmentOfTheTask(taskId);
            while (iterator.hasNext()) {
                if(observer.isReady()){
                    resultBuilder.reset();
                    break;
                }
                resultBuilder.performComputation(iterator.getNext());
            }
            taskGiver.finishTask(taskId, resultBuilder.getResult()); // TODO: dont finish when interrupted
        }
    }
}

