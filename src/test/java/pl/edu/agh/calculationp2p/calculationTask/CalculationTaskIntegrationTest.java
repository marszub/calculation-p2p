package pl.edu.agh.calculationp2p.calculationTask;

import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.calculationTask.hashBreaking.HashBreaker;
import pl.edu.agh.calculationp2p.calculationTask.hashBreaking.HashBreakerInit;
import pl.edu.agh.calculationp2p.calculationTask.interfaces.CalculationTask;
import pl.edu.agh.calculationp2p.calculationTask.interfaces.CalculationTaskIterator;
import pl.edu.agh.calculationp2p.calculationTask.interfaces.ResultBuilder;

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
                100L,
                10L,
                10,
                "0123456789".toCharArray()
        );
        CalculationTask task = new HashBreaker(initVar);
        CalculationTaskIterator iterator = task.getFragmentOfTheTask(task.getNumberOfTaskFragments() - 1);
        ResultBuilder resultBuilder = task.getResultBuilder();
        while(iterator.hasNext())
        {
            resultBuilder.performComputation(iterator.getNext());
        }
        assertTrue(resultBuilder.getResult().contains("9999999999"));
        assertEquals(1, resultBuilder.getResult().size());
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
        assertEquals(0, resultBuilder.getResult().size());
    }

    @Test
    void solveOneFullTask()
    {
        HashBreakerInit initVar = new HashBreakerInit(
                "202CB962AC59075B964B07152D234B70",
                100L,
                10L,
                3,
                "012345".toCharArray()
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
            result.addAll(resultBuilder.getResult());
        }
        assertEquals(1, result.size());
        assertTrue(result.contains("123"));
    }
}
