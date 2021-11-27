package pl.edu.agh.calculationp2p.network.connection;

import pl.edu.agh.calculationp2p.network.message.Message;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageQueue;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageQueueEntry;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageQueueExit;

import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {

        MessageQueue messageQueue1 = new MessageQueue();
        MessageQueueEntry messageQueueEntry1 = messageQueue1;
        MessageQueueExit messageQueueExit1 = messageQueue1;

        ConnectionManager connectionManager1 = new ConnectionManager(messageQueueEntry1, true);

        connectionManager1.start();


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 1. Czy ponawianie wysłania wiadomości w przypadku niepowodzenia leży po stronie StaticConnection? Jakiś timeout?
     *  Czy może zajmuje się tym ktoś inny, a CM zwraca tylko boolean'a?
     *
     * 2. Kto usuwa połączenia z CM? HeartBeat pokaże że nie działa node -> co wtedy?
     *
     * 3. StaticConnection -> usunięcie reconnect i przeniesienie go do CM
     *
     * 4. Walidacja połączenia? czy mamy konkretnie nasz program czy losowe połączenie? Hash client- serwer
     *
     * 5. MessageQueue - co tam ma być? Para (MSG, CONNECTION) <=> klasa ?
     * 
     *
     */
}
