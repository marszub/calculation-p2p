package pl.edu.agh.calculationp2p.calculationTask;

import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.calculationTask.hashBreaking.HashBreakerDataPackage;
import pl.edu.agh.calculationp2p.calculationTask.hashBreaking.HashResultBuilder;
import pl.edu.agh.calculationp2p.calculationTask.utilities.DummyHashStringCreator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HashResultBuilderTest {

//    @Test
//    void checkIfResetWorksProperly()
//    {
//        ResultBuilder hashBreakingBuilder = new HashResultBuilder("AA", new DummyHashStringCreator());
//        hashBreakingBuilder.reset();
//        assertEquals(0, hashBreakingBuilder.getResult().size());
//  }
//
//  @Test
//  void checkIfReturnedResultIsProperSimple()
//  {
//        ResultBuilder hashBreakingBuilder = new HashResultBuilder("3B98E2DFFC6CB06A89DCB0D5C60A0206", new DummyHashStringCreator());
//        hashBreakingBuilder.reset();
//        TaskData data = new HashBreakerDataPackage("AA", 10);
//        hashBreakingBuilder.performComputation(data);
//        assertEquals("AA", hashBreakingBuilder.getResult().get(0));
//      assertEquals(10, hashBreakingBuilder.getResult().size());
//  }
//
//  @Test
//  void checkIfReturnedResultIsProperSimpleAnotherExample()
//  {
//      ResultBuilder hashBreakingBuilder = new HashResultBuilder("3B98E2DFFC6CB06A89DCB0D5C60A0206", new DummyHashStringCreator());
//      hashBreakingBuilder.reset();
//      TaskData data = new HashBreakerDataPackage("BB", 10);
//      hashBreakingBuilder.performComputation(data);
//      assertEquals(0, hashBreakingBuilder.getResult().size());
//  }
}
