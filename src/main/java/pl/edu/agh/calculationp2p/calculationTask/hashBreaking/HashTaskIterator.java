package pl.edu.agh.calculationp2p.calculationTask.hashBreaking;

import pl.edu.agh.calculationp2p.calculationTask.interfaces.CalculationTaskIterator;
import pl.edu.agh.calculationp2p.calculationTask.interfaces.TaskData;

public class HashTaskIterator implements CalculationTaskIterator {
    HashStringCreator hashStringCreator;
    long taskSize;
    long atomicTaskSize;
    long currentProgress = 0;
    long startingPosition;

    public HashTaskIterator(int taskNumber, long taskSize, long atomicTaskSize, HashStringCreator hashStringCreator)
    {
        this.hashStringCreator = hashStringCreator;
        this.taskSize = taskSize;
        this.atomicTaskSize = atomicTaskSize;
        startingPosition = taskSize * taskNumber;
    }

    @Override
    public boolean hasNext()
    {
        return currentProgress < taskSize;
    }

    @Override
    public TaskData getNext()
    {
        String nextStartingString = hashStringCreator.getString(startingPosition + currentProgress);
        long nextTaskSize = atomicTaskSize;
        if(currentProgress + nextTaskSize > taskSize)
        {
            nextTaskSize = taskSize - currentProgress;
        }
        currentProgress = currentProgress + nextTaskSize;
        return new HashBreakerDataPackage(nextStartingString, nextTaskSize);
    }
}
