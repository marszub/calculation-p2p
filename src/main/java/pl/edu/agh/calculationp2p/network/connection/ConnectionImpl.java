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
    final int bufferSize = 1024;

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
//    public Message read()
    public String read()
    {
        ByteBuffer buf = ByteBuffer.allocate(bufferSize);
        try
        {
            socketChannel.read(buf);
        }catch(ClosedChannelException e)
        {
            return null;
        }catch(IOException e)
        {
            e.printStackTrace();
            return null;
        }
        //return ;
        String msg = new String(buf.array()).trim();
        return msg;
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
