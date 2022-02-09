package pl.edu.agh.calculationp2p.network.connection;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MessageConstructor {
    StringBuilder messages = new StringBuilder();
    String separator;

    public MessageConstructor(String separator)
    {
        this.separator = separator;
    }

    public void addString(String string, int length)
    {
        messages.append(string, 0, length);
    }

    public List<String> getMessages()
    {
        List<String> result;
        int size = messages.length();
        if(size == 0)
            return new LinkedList<>();
        if(separator.equals(messages.substring(size - 1)))
        {
            result = Arrays.asList(messages.toString().split(separator));
            messages = new StringBuilder();
        }
        else
        {
            String[] messagesTable = messages.toString().split(separator);
            result = new LinkedList<>();
            int listSize = messagesTable.length;
            for(int i = 0; i<listSize-1;i++)
            {
                result.add(messagesTable[i]);
            }
            if(listSize > 0)
            {
                messages = new StringBuilder(messagesTable[listSize - 1]);
            }
        }
        return result;
    }
}
