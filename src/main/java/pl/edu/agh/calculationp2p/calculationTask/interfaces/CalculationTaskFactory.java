package pl.edu.agh.calculationp2p.calculationTask.interfaces;

import pl.edu.agh.calculationp2p.calculationTask.interfaces.CalculationTask;

import java.io.FileInputStream;

public interface CalculationTaskFactory
{
    CalculationTask createTask();
}
