package pl.edu.agh.calculataionp2p.calculationTask.hashBreaking;

import pl.edu.agh.calculationp2p.calculationTask.hashBreaking.HashBreaker;
import pl.edu.agh.calculationp2p.calculationTask.hashBreaking.HashBreakerInit;
import pl.edu.agh.calculationp2p.calculationTask.interfaces.CalculationTask;
import pl.edu.agh.calculationp2p.calculationTask.interfaces.CalculationTaskFactory;

public class HashBreakerFactory implements CalculationTaskFactory
{
    String hash = "";
    long taskSize = 1_000_000_000L;
    long atomicTaskSize = 10_000_000L;
    int maxStringLength = 7; //98 tasks
    char[] characters = "01234567890abcdefghijklmnopqrstuvwxyz".toCharArray();
    //TMP Place to insert config

    @Override
    public CalculationTask createTask()
    {
        HashBreakerInit initVariable = prepareInitVariables();
        return new HashBreaker(initVariable);
    }

    private HashBreakerInit prepareInitVariables()
    {
        return new HashBreakerInit(
                hash,
                taskSize,
                atomicTaskSize,
                maxStringLength,
                characters
        );
    }
}
