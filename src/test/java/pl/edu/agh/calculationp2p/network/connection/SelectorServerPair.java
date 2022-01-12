package pl.edu.agh.calculationp2p.network.connection;

import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public record SelectorServerPair(Selector selector, ServerSocketChannel server) {
}
