package pl.edu.agh.calculationp2p.calculationTask;

import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.calculationTask.hashBreaking.HashStringCreator;
import pl.edu.agh.calculationp2p.calculationTask.hashBreaking.HashStringCreatorImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HashStringCreatorTest
{
    @Test
    void checkIfGetNextWorksProperlySimple()
    {
        char[] characters = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        String startingString = "";
        HashStringCreator stringCreator = new HashStringCreatorImpl(characters);
        assertEquals("0", stringCreator.getNext(startingString));
    }

    @Test
    void checkIfGetNextWorksProperlyHarder()
    {
        char[] characters = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        String actual = "";
        HashStringCreator stringCreator = new HashStringCreatorImpl(characters);
        for(int i = 0; i < 109; i++)
        {
            actual = stringCreator.getNext(actual);
        }
        assertEquals("98", actual);
    }

    @Test
    void checkIfGetNextWorksProperlyDifferentCharacters()
    {
        char[] characters = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b'};
        String actual = "0";
        HashStringCreator stringCreator = new HashStringCreatorImpl(characters);
        for(int i = 0; i < 1234; i++)
        {
            actual = stringCreator.getNext(actual);
        }
        assertEquals("75a", actual);
    }

    @Test
    void checkIfStringOfConcreteNumberReturnsProperlySimple()
    {
        char[] characters = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b'};
        HashStringCreator stringCreator = new HashStringCreatorImpl(characters);
        assertEquals("75a", stringCreator.getString(1234));
    }

    @Test
    void checkIfStringOfConcreteNumberReturnsProperly()
    {
        char[] characters = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l'};
        HashStringCreator stringCreator = new HashStringCreatorImpl(characters);
        String actual = "";
        for(int i = 0; i<1000002; i++)
        {
            actual = stringCreator.getNext(actual);
            assertEquals(actual, stringCreator.getString(i));
        }
    }

    @Test
    void checkIfStringOfConcreteNumberReturnsProperlySecondTest()
    {
        char[] characters = {'0', '1', '2', '3', '4'};
        HashStringCreator stringCreator = new HashStringCreatorImpl(characters);
        String actual = "";
        for(int i = 0; i<1000002; i++)
        {
            actual = stringCreator.getNext(actual);
            assertEquals(actual, stringCreator.getString(i));
        }
    }
}
