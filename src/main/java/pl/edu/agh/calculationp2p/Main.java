package pl.edu.agh.calculationp2p;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import pl.edu.agh.calculationp2p.ConfigReader;
import pl.edu.agh.calculationp2p.calculation.TaskResolver;
import pl.edu.agh.calculationp2p.calculationTask.CalculationTask;
import pl.edu.agh.calculationp2p.calculationTask.CalculationTaskFactory;
import pl.edu.agh.calculationp2p.calculationTask.hashBreaking.HashBreakerFactory;
import pl.edu.agh.calculationp2p.message.MessageImpl;
import pl.edu.agh.calculationp2p.message.MessageParser;
import pl.edu.agh.calculationp2p.message.MessageParserImpl;
import pl.edu.agh.calculationp2p.message.process.MessageProcessor;
import pl.edu.agh.calculationp2p.message.process.statemachine.StartState;
import pl.edu.agh.calculationp2p.network.connection.ConnectionManager;
import pl.edu.agh.calculationp2p.network.connection.ConnectionManagerImpl;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageQueue;
import pl.edu.agh.calculationp2p.network.router.*;
import pl.edu.agh.calculationp2p.state.*;
import pl.edu.agh.calculationp2p.state.UI.UIController;
import pl.edu.agh.calculationp2p.state.idle.Idle;
import pl.edu.agh.calculationp2p.state.proxy.*;
import pl.edu.agh.calculationp2p.state.publisher.CalculatedPublisher;
import pl.edu.agh.calculationp2p.state.publisher.ReservedPublisher;
import pl.edu.agh.calculationp2p.state.publisher.TaskPublisher;

import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {
        String configFile = "config/connectionConfig.json";
        String taskConfigFile = "config/taskConfig.json";
        if(args.length > 0)
            configFile = args[0];
        if(args.length > 1)
            taskConfigFile = args[1];

        ConfigReader config;
        try {
            config = new ConfigReader(configFile);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // task
        CalculationTaskFactory taskFactory = new HashBreakerFactory(taskConfigFile);
        CalculationTask task = taskFactory.createTask();

        // servant
        Progress progress = new Progress(task.getNumberOfTaskFragments());
        TaskPublisher taskPublisher = new TaskPublisher();
        ReservedPublisher reservedPublisher = new ReservedPublisher();
        CalculatedPublisher calculatedPublisher = new CalculatedPublisher();
        Servant servant = new ServantImpl(progress, taskPublisher, reservedPublisher, calculatedPublisher, -1);

        // proxy
        SchedulerImpl schedulerImpl = new SchedulerImpl(servant);
        Thread schedulerThread = new Thread(schedulerImpl); // create Thread
        StateUpdater stateUpdater = new StateUpdaterImpl(schedulerImpl);
        StatusInformer statusInformer = new StatusInformerImpl(schedulerImpl);
        TaskGiver taskGiver = new TaskGiverImpl(schedulerImpl);

        // calculation
        TaskResolver taskResolver = new TaskResolver(taskGiver, task);
        Thread taskResolverThread = new Thread(taskResolver);

        // message parser
        MessageParser messageParser = new MessageParserImpl();

        // message queue
        MessageQueue messageQueue = new MessageQueue();

        // idle interrupter
        Idle idle = new Idle();

        // connection
        InetSocketAddress myAddress = config.getMyAddress();
        if(myAddress == null)
            throw new NullPointerException("Error while reading my_address and my_port in config file.");

        ConnectionManager connectionManager = new ConnectionManagerImpl(messageQueue, messageParser, myAddress, idle);
        //Thread connectionManagerThread = new Thread(connectionManager); // create Thread

        // router
        RoutingTable routingTable = new RoutingTableImpl();
        Router router;
        if(config.getPublicFlag())
            router = new PublicRouter(connectionManager, messageQueue, routingTable);
        else
            router = new PrivateRouter(connectionManager, messageQueue, routingTable);

        // message
        MessageProcessor messageProcessor = new MessageProcessor(router, stateUpdater, statusInformer, idle, config, new StartState());
        Thread messageProcessorThread = new Thread(messageProcessor); // create Thread

        // UI
        UIController uiController = new UIController(schedulerImpl);
        Thread uiControllerThread = new Thread(uiController);

        // start threads
        schedulerThread.start();
        //connectionManagerThread.start();
        messageProcessorThread.start();
        taskResolverThread.start();
        uiControllerThread.start();

        try {
            uiControllerThread.join(0);
            taskResolverThread.join(0);
            messageProcessorThread.join(0);
            //connectionManagerThread.join(0);
            schedulerThread.join(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
