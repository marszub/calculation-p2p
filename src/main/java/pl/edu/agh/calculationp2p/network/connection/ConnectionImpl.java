package pl.edu.agh.calculationp2p.network.connection;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import pl.edu.agh.calculationp2p.message.Message;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.List;


public abstract class ConnectionImpl implements Connection
{
    SocketChannel socketChannel;
    Selector selector;
    SelectionKey key;
    String separator = Character.toString((char) 1);
    MessageConstructor messageConstructor = new MessageConstructor(separator);
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
            Logger logger = LoggerFactory.getLogger("");
            logger.error(e.getMessage());
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
            Logger logger = LoggerFactory.getLogger("");
            logger.error(e.getMessage());
        }
    }

    @Override
    public void read(List<String> list) throws ConnectionLostException
    {
        ByteBuffer buf = ByteBuffer.allocate(bytesBufferSize);
        int bytesRead = 1;
        while(bytesRead > 0)
        {
            try
            {
                bytesRead = socketChannel.read(buf);
            } catch (ClosedChannelException e)
            {
                list.addAll(messageConstructor.getMessages());
                throw new ConnectionLostException();
            } catch (IOException e)
            {
                list.addAll(messageConstructor.getMessages());
                throw new ConnectionLostException();
            }
            if(bytesRead == -1) {
                list.addAll(messageConstructor.getMessages());
                throw new ConnectionLostException();
            }
            buf = buf.clear();
            if(bytesRead > 0)
            {
                String word = new String(buf.array());
                messageConstructor.addString(word, bytesRead);
            }
        }
        list.addAll(messageConstructor.getMessages());
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
