package pl.edu.agh.calculationp2p.calculationTask;

import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.calculationTask.hashBreaking.HashBreaker;
import pl.edu.agh.calculationp2p.calculationTask.hashBreaking.HashBreakerDataPackage;
import pl.edu.agh.calculationp2p.calculationTask.hashBreaking.HashBreakerInit;
import pl.edu.agh.calculationp2p.calculationTask.interfaces.CalculationTask;
import pl.edu.agh.calculationp2p.calculationTask.interfaces.CalculationTaskIterator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HashBreakerTest {
    @org.junit.Test
    void checkIfHashBreakingReturnsProperNumberOfTaskFragmentsSimple()
    {
        HashBreakerInit initVar = new HashBreakerInit(
                "AAAAA",
                10000,
                2,
                3,
                "01234567890".toCharArray()
        );
        CalculationTask task = new HashBreaker(initVar);
        assertEquals(1, task.getNumberOfTaskFragments());
    }

    @Test
    void checkIfHashBreakingReturnsProperNumberOfTaskFragments()
    {
        HashBreakerInit initVar = new HashBreakerInit(
                "AAAAA",
                10_000_000_000L,
                1000_000L,
                18,
                "0123456789".toCharArray()
        );
        CalculationTask task = new HashBreaker(initVar);
        assertEquals(111111112, task.getNumberOfTaskFragments());
    }

    @Test
    void checkIfHashBreakingReturnsProperNumberOfTaskFragmentsWhenStringsToBeProcessedIsDivisibleByTaskSize()
    {
        HashBreakerInit initVar = new HashBreakerInit(
                "AAAAA",
                10L,
                5L,
                7,
                "0123456789".toCharArray()
        );
        CalculationTask task = new HashBreaker(initVar);
        assertEquals(1111111, task.getNumberOfTaskFragments());
    }

    @Test
    void checkIfIteratorIsProperlyCreatedSimple()
    {
        HashBreakerInit initVar = new HashBreakerInit(
                "AAAAA",
                10_000_000_000L,
                1000_000L,
                18,
                "0123456789".toCharArray()
        );
        CalculationTask task = new HashBreaker(initVar);
        CalculationTaskIterator HashIterator = task.getFragmentOfTheTask(0);
        HashBreakerDataPackage data = (HashBreakerDataPackage) HashIterator.getNext();
        assertEquals("0", data.startingString());
    }
}
