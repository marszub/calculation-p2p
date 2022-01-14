package pl.edu.agh.calculationp2p.network.router;

import pl.edu.agh.calculationp2p.network.connection.Connection;

public record ConnectionTimestampPair(Connection connection, long timestamp) {
}
