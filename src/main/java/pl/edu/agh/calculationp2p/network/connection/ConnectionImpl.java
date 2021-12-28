package pl.edu.agh.calculationp2p.network.connection;

import pl.edu.agh.calculationp2p.message.Message;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;


public abstract class ConnectionImpl implements Connection
{
    SocketChannel socketChannel;
    final int bufferSize = 2048;

    @Override
    public boolean send(Message message)
    {
        try {
            trySend(message);
        }catch(ClosedChannelException e)
        {
            return false;
        }catch(IOException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void register(Selector selector, int event) throws ClosedChannelException
    {
        socketChannel.register(selector, event, this);
    }

    @Override
    public void close()
    {
        try
        {
            socketChannel.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String read() throws ConnectionLostException
    {
        ByteBuffer buf = ByteBuffer.allocate(bufferSize);
        int bytesRead = 0;
        try
        {
            bytesRead = socketChannel.read(buf);
        }catch(ClosedChannelException e)
        {
            throw new ConnectionLostException();
        }catch(IOException e)
        {
            e.printStackTrace();
            return null;
        }
        if(bytesRead == -1)
            throw new ConnectionLostException();
        return new String(buf.array()).trim();
}

    private void trySend(Message message) throws IOException
    {
        String data = message.serialize();
        ByteBuffer buff = ByteBuffer.allocate(bufferSize);
        buff.clear();
        buff.put(data.getBytes());
        buff.flip();
        while(buff.hasRemaining())
        {
            socketChannel.write(buff);
        }
    }
}
