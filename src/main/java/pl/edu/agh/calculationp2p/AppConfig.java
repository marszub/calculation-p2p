package pl.edu.agh.calculationp2p;

import java.net.InetSocketAddress;

public interface AppConfig {

    InetSocketAddress getServerAddress();

    InetSocketAddress getMyAddress();

    InetSocketAddress getMySocketAddress();

    boolean getPublicFlag();

    int getMaxConnectingTime();

    int getGetProgressRetryTime();

    int getHeartBeatPeriod();

    int getHeartBeatLifetime();

    String getTaskConfigPath();

    int numOfCalculationThreads();

}
