package pl.edu.agh.calculationp2p;

import pl.edu.agh.calculationp2p.calculationTask.CalculationTask;
import pl.edu.agh.calculationp2p.calculationTask.CalculationTaskFactory;
import pl.edu.agh.calculationp2p.calculationTask.hashBreaking.HashBreakerFactory;
import pl.edu.agh.calculationp2p.message.MessageParser;
import pl.edu.agh.calculationp2p.message.MessageParserImpl;
import pl.edu.agh.calculationp2p.message.process.MessageProcessor;
import pl.edu.agh.calculationp2p.network.connection.ConnectionManager;
import pl.edu.agh.calculationp2p.network.connection.ConnectionManagerImpl;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageQueue;
import pl.edu.agh.calculationp2p.network.router.PrivateRouter;
import pl.edu.agh.calculationp2p.network.router.Router;
import pl.edu.agh.calculationp2p.network.router.RoutingTable;
import pl.edu.agh.calculationp2p.network.router.RoutingTableImpl;
import pl.edu.agh.calculationp2p.state.*;
import pl.edu.agh.calculationp2p.state.idle.Idle;
import pl.edu.agh.calculationp2p.state.proxy.*;
import pl.edu.agh.calculationp2p.state.publisher.CalculatedPublisher;
import pl.edu.agh.calculationp2p.state.publisher.ReservedPublisher;
import pl.edu.agh.calculationp2p.state.publisher.TaskPublisher;

public class Main {
    public static void main(String[] args) {

        // servant
        Progress progress = new Progress(100); // TODO: config
        TaskPublisher taskPublisher = new TaskPublisher();
        ReservedPublisher reservedPublisher = new ReservedPublisher();
        CalculatedPublisher calculatedPublisher = new CalculatedPublisher();
        Servant servant = new ServantImpl(progress, taskPublisher, reservedPublisher, calculatedPublisher, -1); // TODO: NodeID dostajemy od serwera!!!

        // proxy
        SchedulerImpl schedulerImpl = new SchedulerImpl(servant);
        Thread schedulerThread = new Thread(schedulerImpl); // create Thread
        StateUpdater stateUpdater = new StateUpdaterImpl(schedulerImpl);
        StatusInformer statusInformer = new StatusInformerImpl(schedulerImpl);
        TaskGiver taskGiver = new TaskGiverImpl(schedulerImpl);

        // task
        CalculationTaskFactory taskFactory = new HashBreakerFactory();
        CalculationTask task = taskFactory.createTask();

        // TODO: calculation

        // message parser
        MessageParser messageParser = new MessageParserImpl();

        // message queue
        MessageQueue messageQueue = new MessageQueue();

        // idle interrupter
        Idle idle = new Idle();

        // connection
        ConnectionManager connectionManager = new ConnectionManagerImpl(messageQueue, messageParser, idle); // TODO: IP
        Thread connectionManagerThread = new Thread(connectionManager); // create Thread

        // router
        RoutingTable routingTable = new RoutingTableImpl();
        Router router = new PrivateRouter(connectionManager, messageQueue, routingTable); // TODO: PUBLIC

        // message
        MessageProcessor messageProcessor = new MessageProcessor(router, stateUpdater, statusInformer);
        Thread messageProcessorThread = new Thread(messageProcessor); // create Thread

        // start threads
        schedulerThread.start();
        connectionManagerThread.start();
        messageProcessorThread.start();

        try {
            messageProcessorThread.join(0);
            connectionManagerThread.join(0);
            schedulerThread.join(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
