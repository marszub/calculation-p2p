package pl.edu.agh.calculationp2p.network.utilities;

import pl.edu.agh.calculationp2p.network.connection.ConnectionManager;
import pl.edu.agh.calculationp2p.network.connection.StaticConnection;

public class DummyConnectionManager implements ConnectionManager {

    @Override
    public void addStaticConnection(StaticConnection staticConnection) {
    }

    @Override
    public void removeStaticConnection(StaticConnection staticConnection) {
    }

    @Override
    public void start() {
    }

    @Override
    public void run() {
    }

    @Override
    public void close() {

    }
}
