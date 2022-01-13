package pl.edu.agh.calculationp2p.network.connection;

public interface ConnectionManager extends Runnable {
    void addStaticConnection(StaticConnection staticConnection);

    void removeStaticConnection(StaticConnection staticConnection);

    void start();

    void run();

    void close();
}
