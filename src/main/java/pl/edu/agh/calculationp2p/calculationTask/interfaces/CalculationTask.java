package pl.edu.agh.calculationp2p.calculationTask.interfaces;

public interface CalculationTask
{
    int getNumberOfTaskFragments();
    CalculationTaskIterator getFragmentOfTheTask(int number);
    ResultBuilder getResultBuilder();
}
