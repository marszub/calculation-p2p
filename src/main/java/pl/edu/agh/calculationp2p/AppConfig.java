package pl.edu.agh.calculationp2p;

import java.net.InetSocketAddress;

public interface AppConfig {

    InetSocketAddress getServerAddress();

    InetSocketAddress getMyAddress();

    boolean getPublicFlag();

    String getMyIpString();

    int getMaxConnectingTime();

    int getGetProgressRetryTime();

    int numOfCalculationThreads();

}
