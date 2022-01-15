package pl.edu.agh.calculationp2p.network.connection;

public interface ConnectionManager extends Runnable {
    void addStaticConnection(StaticConnection staticConnection);

    void removeStaticConnection(StaticConnection staticConnection);

    void run();

    void start();

    void close();
}
