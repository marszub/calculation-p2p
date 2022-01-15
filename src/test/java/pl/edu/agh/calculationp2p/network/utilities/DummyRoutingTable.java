package pl.edu.agh.calculationp2p.network.utilities;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.network.connection.Connection;
import pl.edu.agh.calculationp2p.network.router.RoutingTable;

public class DummyRoutingTable implements RoutingTable {
    boolean sendResult = false;

    @Override
    public void addInterface(int id) {

    }

    @Override
    public void removeInterface(int id) {

    }

    @Override
    public void bind(int id, Connection connection) {

    }

    @Override
    public void send(int id, Message message) {

    }

    @Override
    public boolean trySend(int id, Message message) {
        return sendResult;
    }

    @Override
    public void resendAll() {

    }

    @Override
    public boolean interfaceListContains(int id) {
        return false;
    }

    public void setSendResult(boolean sendResult) {
        this.sendResult = sendResult;
    }
}
