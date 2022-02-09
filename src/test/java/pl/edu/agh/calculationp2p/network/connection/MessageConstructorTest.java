package pl.edu.agh.calculationp2p.network.connection;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MessageConstructorTest {

    @Test
    void addStringEndingWithSeparatorTest()
    {
        MessageConstructor constructor = new MessageConstructor("A");
        String string = "BBBBBBBBBBBBA";
        String stringResult = "BBBBBBBBBBBB";
        constructor.addString(string, string.length());
        List result = constructor.getMessages();
        assertEquals(1,result.size());
        assertEquals(stringResult,result.get(0));
    }

    @Test
    void addStringNotEndingWithSeparatorTest()
    {
        MessageConstructor constructor = new MessageConstructor("A");
        String string = "BBBBBBBBBBBB";
        constructor.addString(string, string.length());
        List result = constructor.getMessages();
        assertEquals(0,result.size());
    }

    @Test
    void addStringNotEndingWithSeparatorTestAndThenTheRestOfTheMessageArrives()
    {
        MessageConstructor constructor = new MessageConstructor("A");
        String string1 = "BBBBBBBBBBBB";
        String string2 = "BBBBBB";
        String string3 = "BBBBBBB";
        constructor.addString(string1, string1.length());
        List result = constructor.getMessages();
        assertEquals(0,result.size());
        constructor.addString(string2 + "A" + string3, string2.length()+1+string3.length());
        result = constructor.getMessages();
        assertEquals(1,result.size());
        assertEquals(string1+string2,result.get(0));
    }

    @Test
    void addStringNotEndingWithSeparatorTestAndThenTheRestOfTheMessageArrivesTwoMessages()
    {
        MessageConstructor constructor = new MessageConstructor("A");
        String string1 = "BBBBBBBBBBBB";
        String string2 = "BBBBBB";
        String string3 = "BBBBBBB";
        constructor.addString(string1, string1.length());
        List result = constructor.getMessages();
        assertEquals(0,result.size());
        constructor.addString(string2 + "A" + string3 + "A", string2.length()+1+string3.length()+1);
        result = constructor.getMessages();
        assertEquals(2,result.size());
        assertEquals(string1+string2,result.get(0));
        assertEquals(string3,result.get(1));
    }
}