package pl.edu.agh.calculationp2p.network.connection;

import pl.edu.agh.calculationp2p.message.Message;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.List;


public abstract class ConnectionImpl implements Connection
{
    SocketChannel socketChannel;
    Selector selector;
    SelectionKey key;
    static final String separator = Character.toString((char) 1);
    static final int bytesBufferSize = 2048;

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
    public void register(Selector selector) throws ClosedChannelException
    {
        this.selector = selector;
        key = socketChannel.register(selector, SelectionKey.OP_READ, this);
    }

    @Override
    public void close()
    {
        try
        {
            socketChannel.close();
            if(key != null)
                key.cancel();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void read(List<String> list) throws ConnectionLostException
    {
        StringBuilder messages = new StringBuilder();
        ByteBuffer buf = ByteBuffer.allocate(bytesBufferSize);
        int bytesRead = 1;
        while(bytesRead > 0)
        {
            try
            {
                bytesRead = socketChannel.read(buf);
            } catch (ClosedChannelException e)
            {
                list.addAll(Arrays.asList(messages.toString().split(separator)));
                throw new ConnectionLostException();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            if(bytesRead == -1) {
                list.addAll(Arrays.asList(messages.toString().split(separator)));
                throw new ConnectionLostException();
            }
            buf = buf.clear();
            if(bytesRead > 0)
            {
                String word = new String(buf.array());
                messages.append(word.substring(0, bytesRead));
            }
        }
        list.addAll(Arrays.asList(messages.toString().split(separator)));
}

    private void trySend(Message message) throws IOException
    {
        String data = message.serialize();
        data = data + separator;
        ByteBuffer buff = ByteBuffer.allocate(data.length());
        buff.clear();
        buff.put(data.getBytes());
        buff.flip();
        while (buff.hasRemaining()) {
            socketChannel.write(buff);
        }
    }
}
