package pl.edu.agh.calculationp2p.calculationTask;

import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.calculationTask.hashBreaking.HashBreakerDataPackage;
import pl.edu.agh.calculationp2p.calculationTask.hashBreaking.HashTaskIterator;
import pl.edu.agh.calculationp2p.calculationTask.interfaces.CalculationTaskIterator;
import pl.edu.agh.calculationp2p.calculationTask.utilities.DummyHashStringCreator;

import static org.junit.jupiter.api.Assertions.*;


public class HashTaskIteratorTest
{
      @Test
      void checkIfHasNextWorksProperly()
      {
          CalculationTaskIterator iterator = new HashTaskIterator(1, 10, 5, new DummyHashStringCreator());
          assertTrue(iterator.hasNext());
      }

    @Test
    void checkIfHasNextWorksProperlyEndingTaskSimple()
    {
        CalculationTaskIterator iterator = new HashTaskIterator(1, 10, 10, new DummyHashStringCreator());
        iterator.getNext();
        assertFalse(iterator.hasNext());
    }

    @Test
    void checkIfHasNextWorksProperlyEndingTask()
    {
        CalculationTaskIterator iterator = new HashTaskIterator(1, 10, 7, new DummyHashStringCreator());
        iterator.getNext();
        iterator.getNext();
        assertFalse(iterator.hasNext());
    }

    @Test
    void checkIfIteratorReturnsProperTaskDataSimple()
    {
        CalculationTaskIterator iterator = new HashTaskIterator(1, 10, 7, new DummyHashStringCreator());
        HashBreakerDataPackage dataPackage = (HashBreakerDataPackage) iterator.getNext();
        assertEquals("AAA", dataPackage.startingString());
        assertEquals(7, dataPackage.atomicTaskSize());
    }

    @Test
    void checkIfIteratorReturnsProperTaskData()
    {
        CalculationTaskIterator iterator = new HashTaskIterator(1, 10, 7, new DummyHashStringCreator());
        iterator.getNext();
        HashBreakerDataPackage dataPackage = (HashBreakerDataPackage) iterator.getNext();
        assertEquals("AAA", dataPackage.startingString());
        assertEquals(3, dataPackage.atomicTaskSize());
    }

    @Test
    void checkIfIteratorReturnsProperTaskDataIfTaskEnded()
    {
        CalculationTaskIterator iterator = new HashTaskIterator(1, 10, 7, new DummyHashStringCreator());
        iterator.getNext();
        iterator.getNext();
        HashBreakerDataPackage dataPackage = (HashBreakerDataPackage) iterator.getNext();
        assertEquals("AAA", dataPackage.startingString());
        assertEquals(0, dataPackage.atomicTaskSize());
    }
}
