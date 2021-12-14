package pl.edu.agh.calculationp2p.network.utilities;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.network.connection.Connection;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.Selector;

public class DummyConnection implements Connection {
    private boolean sendResult;

    public DummyConnection(boolean sendResult)
    {
        this.sendResult = sendResult;
    }

    @Override
    public boolean send(Message message) {
        boolean tmp = sendResult;
        sendResult = !sendResult;
        return tmp;
    }

    @Override
    public void subscribe(Selector selector, int event) throws ClosedChannelException {
        return;
    }

    @Override
    public void close() {
        return;
    }
}
