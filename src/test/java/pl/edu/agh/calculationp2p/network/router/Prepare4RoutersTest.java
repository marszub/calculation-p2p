package pl.edu.agh.calculationp2p.network.router;

import pl.edu.agh.calculationp2p.message.MessageParser;

import java.util.concurrent.Semaphore;

public record Prepare4RoutersTest(Router router1, Router router2, Router router3, Router router4, Semaphore semaphore1,
                                  Semaphore semaphore2, Semaphore semaphore3, Semaphore semaphore4,
                                  Semaphore semaphore5, MessageParser messageParser) {}
