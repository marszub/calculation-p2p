package pl.edu.agh.calculationp2p.network.router;
import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.network.connection.Connection;
import pl.edu.agh.calculationp2p.network.utilities.DummyConnection;
import pl.edu.agh.calculationp2p.network.utilities.DummyMessage;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RoutingTableTest {
    @Test
    void checkIfInterfaceIsAddingItselfProperly()
    {
        RoutingTableImpl routingTable = new RoutingTableImpl();
        int id = 10;
        routingTable.addInterface(id);
        assertTrue(routingTable.getInterfaces().containsKey(id));
    }

    @Test
    void checkIfInterfacesDeletesProperly()
    {
        RoutingTableImpl routingTable = new RoutingTableImpl();
        int id = 10;
        routingTable.addInterface(id);
        routingTable.removeInterface(id);
        assertFalse(routingTable.getInterfaces().containsKey(id));
    }

    @Test
    void checkIfConnectionBindsCorrectly()
    {
        RoutingTableImpl routingTable = new RoutingTableImpl();
        int id = 10;
        routingTable.addInterface(id);
        Connection connection = new DummyConnection(true);
        routingTable.bind(id, connection);
        assertEquals(connection, routingTable.getInterfaces().get(id));
    }

    @Test
    void checkIfConnectionTrySendsMessage()
    {
        RoutingTableImpl routingTable = new RoutingTableImpl();
        int id = 10;
        routingTable.addInterface(id);
        Connection connection = new DummyConnection(true);
        routingTable.bind(id, connection);
        assertTrue(routingTable.trySend(id, new DummyMessage()));
        assertFalse(routingTable.trySend(id, new DummyMessage()));
    }

    @Test
    void checkIfMessageIsSent()
    {
        RoutingTableImpl routingTable = new RoutingTableImpl();
        int id = 10;
        Message notPassing = new DummyMessage();
        List<Message> list = new LinkedList<>();
        list.add(notPassing);
        addIdAndMakeMessageNotToBeSent(routingTable, id, list);
        assertEquals(notPassing, routingTable.getMessageInterfaceQueue().get(id).pop());
    }

    @Test
    void checkIfResendAllResendsAll()
    {
        RoutingTableImpl routingTable = new RoutingTableImpl();
        int id = 10;
        Message notPassing1 = new DummyMessage();
        Message notPassing2 = new DummyMessage();
        List<Message> list = new LinkedList<>();
        list.add(notPassing1);
        list.add(notPassing2);
        addIdAndMakeMessageNotToBeSent(routingTable, id, list);
        routingTable.resendAll();
        assertEquals(1, routingTable.getMessageInterfaceQueue().get(id).size());
        assertEquals(notPassing2, routingTable.getMessageInterfaceQueue().get(id).pop());
    }

    @Test
    void checkIfResendAllResendsAllManyConnections()
    {
        RoutingTableImpl routingTable = new RoutingTableImpl();
        int id1 = 10;
        int id2 = 40;
        Message notPassing1 = new DummyMessage();
        Message notPassing2 = new DummyMessage();
        Message notPassing3 = new DummyMessage();
        Message notPassing4= new DummyMessage();
        List<Message> list = new LinkedList<>();
        List<Message> list2 = new LinkedList<>();
        list.add(notPassing1);
        list.add(notPassing2);
        list2.add(notPassing3);
        list2.add(notPassing4);
        addIdAndMakeMessageNotToBeSent(routingTable, id1, list);
        addIdAndMakeMessageNotToBeSent(routingTable, id2, list2);
        routingTable.resendAll();
        assertEquals(1, routingTable.getMessageInterfaceQueue().get(id1).size());
        assertEquals(notPassing2, routingTable.getMessageInterfaceQueue().get(id1).pop());
        assertEquals(1, routingTable.getMessageInterfaceQueue().get(id2).size());
        assertEquals(notPassing4, routingTable.getMessageInterfaceQueue().get(id2).pop());
    }

    @Test
    void checkIfResendAllResendsAllManyMessages()
    {
        RoutingTableImpl routingTable = new RoutingTableImpl();
        int id1 = 10;
        Message notPassing1 = new DummyMessage();
        Message notPassing2 = new DummyMessage();
        Message notPassing3 = new DummyMessage();
        Message notPassing4= new DummyMessage();
        List<Message> list = new LinkedList<>();
        list.add(notPassing1);
        list.add(notPassing2);
        list.add(notPassing3);
        list.add(notPassing4);
        addIdAndMakeMessageNotToBeSent(routingTable, id1, list);
        routingTable.resendAll();
        assertEquals(3, routingTable.getMessageInterfaceQueue().get(id1).size());
        assertEquals(notPassing3, routingTable.getMessageInterfaceQueue().get(id1).pop());
        assertEquals(notPassing4, routingTable.getMessageInterfaceQueue().get(id1).pop());
        assertEquals(notPassing2, routingTable.getMessageInterfaceQueue().get(id1).pop());
    }

    @Test
    void checkDeletionOfNonExistingInterface()
    {
        RoutingTableImpl routingTable = new RoutingTableImpl();
        assertThrows(InterfaceDoesNotExistException.class, () -> {
            routingTable.removeInterface(1);
        });
    }

    @Test
    void checkSendingMessageToNonExistingInterface()
    {
        RoutingTableImpl routingTable = new RoutingTableImpl();
        assertThrows(InterfaceDoesNotExistException.class, () -> {
            routingTable.send(1, new DummyMessage());
        });
    }

    @Test
    void checkTrySendingMessageToNonExistingInterface()
    {
        RoutingTableImpl routingTable = new RoutingTableImpl();
        assertThrows(InterfaceDoesNotExistException.class, () -> {
            routingTable.trySend(1, new DummyMessage());
        });
    }

    @Test
    void checkBindingOfNonExistingInterface()
    {
        RoutingTableImpl routingTable = new RoutingTableImpl();
        assertThrows(InterfaceDoesNotExistException.class, () -> {
            routingTable.bind(1, new DummyConnection(false));
        });
    }

    @Test
    void checkCreationOfExistingInterface()
    {
        RoutingTableImpl routingTable = new RoutingTableImpl();
        routingTable.addInterface(1);
        assertThrows(InterfaceExistsException.class, () -> {
            routingTable.addInterface(1);
        });
    }

    @Test
    void checkInterfaceListContains()
    {
        RoutingTableImpl routingTable = new RoutingTableImpl();
        routingTable.addInterface(1);
        assertTrue(routingTable.interfaceListContains(1));
    }

    private void addIdAndMakeMessageNotToBeSent(RoutingTableImpl routingTable, int id, List<Message> messageList)
    {
        routingTable.addInterface(id);
        Connection connection = new DummyConnection(true);
        routingTable.bind(id, connection);
        for(Message message : messageList)
        {
            routingTable.send(id, new DummyMessage());
            routingTable.send(id, message);
        }
    }
}
