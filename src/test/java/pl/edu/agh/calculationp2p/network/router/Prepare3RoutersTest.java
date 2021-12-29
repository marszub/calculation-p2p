package pl.edu.agh.calculationp2p.network.router;

import pl.edu.agh.calculationp2p.message.MessageParser;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageQueue;

import java.util.concurrent.Semaphore;

public record Prepare3RoutersTest(Router router1, Router router2, Router router3, Semaphore semaphore1,
                                  Semaphore semaphore2, Semaphore semaphore3, MessageParser messageParser,
                                  MessageQueue messageQueue) { }
