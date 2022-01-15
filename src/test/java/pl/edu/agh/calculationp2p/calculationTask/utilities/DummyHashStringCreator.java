package pl.edu.agh.calculationp2p.calculationTask.utilities;

import pl.edu.agh.calculationp2p.calculationTask.hashBreaking.HashStringCreator;

public class DummyHashStringCreator implements HashStringCreator {
    public DummyHashStringCreator(){}

    @Override
    public String getNext(String string)
    {
        return string;
    }

    @Override
    public String getString(long number)
    {
        return "AAA";
    }
}
