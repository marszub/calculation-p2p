package pl.edu.agh.calculationp2p.calculationTask.hashBreaking;

public record HashBreakerInit(String hash, long taskSize, long atomicTaskSize, int maxStringLength, char[] characters)
{
}
