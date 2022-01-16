package pl.edu.agh.calculationp2p.calculationTask.hashBreaking;

import pl.edu.agh.calculationp2p.calculationTask.interfaces.CalculationTask;
import pl.edu.agh.calculationp2p.calculationTask.interfaces.CalculationTaskFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class HashBreakerFactory implements CalculationTaskFactory
{
    String hash = "";
    long taskSize = 1_000_000_000L;
    long atomicTaskSize = 10_000_000L;
    int maxStringLength = 7; //98 tasks
    char[] characters = "01234567890abcdefghijklmnopqrstuvwxyz".toCharArray();
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
