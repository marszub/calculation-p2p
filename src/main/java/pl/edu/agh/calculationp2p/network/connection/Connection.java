package pl.edu.agh.calculationp2p.network.connection;


import pl.edu.agh.calculationp2p.network.message.Message;

import java.nio.channels.Selector;



// Klasa abstrakcyjna odpowiadająca połączeniu TCP.
// Umożliwia rejestrację kanału w selektorze na dane wydarzenia
// oraz wysyłanie wiadomości odpowiednim połączeniem.
public abstract class Connection {
    public abstract boolean send (Message message);
    public abstract void subscribe(Selector selector, int event);
    protected void close(){

    }

}
