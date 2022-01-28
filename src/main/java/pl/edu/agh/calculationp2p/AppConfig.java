package pl.edu.agh.calculationp2p;

import java.net.InetSocketAddress;

public interface AppConfig {
    InetSocketAddress getServerAddress();

    String getMyIpString();

    int getMaxConnectingTime();

    int getGetProgressRetryTime();
}
