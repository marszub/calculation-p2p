package pl.edu.agh.calculationp2p.calculationTask.hashBreaking;

import pl.edu.agh.calculationp2p.calculationTask.CalculationTask;
import pl.edu.agh.calculationp2p.calculationTask.CalculationTaskFactory;

public class HashBreakerFactory implements CalculationTaskFactory
{
    String hash = "7DAF6D81AE80B8930DE2AAF1140B1BE2";
    long taskSize = 1_000_000L;
    long atomicTaskSize = 10_000L;
    int maxStringLength = 6;
    char[] characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    //TMP Place to insert config

    public HashBreakerFactory() {}

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
