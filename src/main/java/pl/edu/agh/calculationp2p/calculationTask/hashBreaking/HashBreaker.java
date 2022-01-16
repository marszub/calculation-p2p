package pl.edu.agh.calculationp2p.calculationTask.hashBreaking;

import pl.edu.agh.calculationp2p.calculationTask.interfaces.CalculationTask;
import pl.edu.agh.calculationp2p.calculationTask.interfaces.CalculationTaskIterator;
import pl.edu.agh.calculationp2p.calculationTask.interfaces.ResultBuilder;

public class HashBreaker implements CalculationTask {
    int maxStringLength;
    long taskSize;
    long atomicTaskSize;
    int numberOfTaskFragments;
    String hash;
    HashStringCreator hashStringCreator;


    public HashBreaker(HashBreakerInit variables)
    {
        hash = variables.hash();
        maxStringLength = variables.maxStringLength();
        taskSize = variables.taskSize();
        atomicTaskSize = variables.atomicTaskSize();
        hashStringCreator = new HashStringCreatorImpl(variables.characters());
        numberOfTaskFragments = calculateNumberOfTaskFragments(variables.characters().length);
    }

    @Override
    public int getNumberOfTaskFragments() {
        return numberOfTaskFragments;
    }

    @Override
    public CalculationTaskIterator getFragmentOfTheTask(int number)
    {
        return new HashTaskIterator(number, taskSize, atomicTaskSize, hashStringCreator);
    }

    @Override
    public ResultBuilder getResultBuilder()
    {
        return new HashResultBuilder(hash, hashStringCreator);
    }

    private int calculateNumberOfTaskFragments(int differentSymbols)
    {
        long tmp = 1;
        long numberOfStringsToBeProcessed = 0;
        for(int i = 0; i<maxStringLength; i++)
        {
            tmp = tmp * differentSymbols;
            numberOfStringsToBeProcessed = numberOfStringsToBeProcessed + tmp;
        }
        int result = (int) (numberOfStringsToBeProcessed/taskSize);
        if(numberOfStringsToBeProcessed%taskSize == 0)
        {
            return result;
        }
        return result + 1;
    }
}
