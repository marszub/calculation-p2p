package pl.edu.agh.calculationp2p.calculationTask;

import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.calculationTask.hashBreaking.HashBreaker;
import pl.edu.agh.calculationp2p.calculationTask.hashBreaking.HashBreakerInit;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CalculationTaskIntegrationTest
{
    @Test
    void checkIfTaskSolvesItselfProperly()
    {
        HashBreakerInit initVar = new HashBreakerInit(
                "E0EC043B3F9E198EC09041687E4D4E8D",
                10_000_000L,
                1_000_000L,
                4,
                "abcdefghijklmnoqprstuvwxyzABCDEFGHIJKLMNOQPRSTUVWXYZ0123456789".toCharArray()
        );
        CalculationTask task = new HashBreaker(initVar);
        CalculationTaskIterator iterator = task.getFragmentOfTheTask(task.getNumberOfTaskFragments() - 1);
        ResultBuilder resultBuilder = task.getResultBuilder();
        while(iterator.hasNext())
        {
            resultBuilder.performComputation(iterator.getNext());
        }
        assertTrue(resultBuilder.getResult().serialize().contains("9999999999"));
    }

    @Test
    void checkIfTaskReturnsEmptyListIfItDidntFindAnyString()
    {
        HashBreakerInit initVar = new HashBreakerInit(
                "E0EC043B3F9E198EC09041687E4D4E8D",
                100L,
                10L,
                10,
                "0123456789".toCharArray()
        );
        CalculationTask task = new HashBreaker(initVar);
        CalculationTaskIterator iterator = task.getFragmentOfTheTask(task.getNumberOfTaskFragments() - 2);
        ResultBuilder resultBuilder = task.getResultBuilder();
        while(iterator.hasNext())
        {
            resultBuilder.performComputation(iterator.getNext());
        }
        assertEquals(0, resultBuilder.getResult().serialize().split(":").length-1);
    }

    @Test
    void solveOneFullTask()
    {
        HashBreakerInit initVar = new HashBreakerInit(
                "E0EC043B3F9E198EC09041687E4D4E8D",
                10_000_000L,
                1_000_000L,
                4,
                "abcdefghijklmnoqprstuvwxyzABCDEFGHIJKLMNOQPRSTUVWXYZ0123456789".toCharArray()
        );
        List<String> result = new LinkedList<>();
        CalculationTask task = new HashBreaker(initVar);
        ResultBuilder resultBuilder = task.getResultBuilder();
        for(int i = 0; i < task.getNumberOfTaskFragments(); i++)
        {
            resultBuilder.reset();
            CalculationTaskIterator iterator = task.getFragmentOfTheTask(i);
            while (iterator.hasNext()) {
                resultBuilder.performComputation(iterator.getNext());
            }
            result.addAll(Collections.singleton(resultBuilder.getResult().serialize()));
        }
        assertEquals(3, result.size());
        assertTrue(result.contains("[\"123\"]"));
    }
}
