package pl.edu.agh.calculationp2p.calculationTask.hashBreaking;

import pl.edu.agh.calculationp2p.calculationTask.TaskData;

public record HashBreakerDataPackage(String startingString, long atomicTaskSize) implements TaskData {
}
