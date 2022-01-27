package pl.edu.agh.calculationp2p.state.UI;

import pl.edu.agh.calculationp2p.state.Progress;
import pl.edu.agh.calculationp2p.state.Scheduler;
import pl.edu.agh.calculationp2p.state.Servant;
import pl.edu.agh.calculationp2p.state.ServantImpl;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.publisher.CalculatedPublisher;
import pl.edu.agh.calculationp2p.state.publisher.ReservedPublisher;
import pl.edu.agh.calculationp2p.state.publisher.TaskPublisher;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class UIBuilder {
    int size;
    String wideLine;
    String emptyLine;
    String line;
    String firstLine;
    String observersTable;
    String progressBar;
    UIUpdater updater;
    boolean isFinished;

    List<Future<Integer>> futures;


    public UIBuilder(Scheduler servant) {
        this.size = 100;
        this.wideLine = "";
        this.line = "";
        this.emptyLine = "";
        this.updater = new UIUpdater(servant);
        this.progressBar = "";
        this.observersTable = "";

        for (int i = 0; i < this.size; i++) {
            wideLine += "=";
            line += "-";
            emptyLine += " ";
        }
        updateAll();
    }

    public String getFrame() {
        String result = "\n\n\n\n\n\n\n\n\n\n" +
                wideLine + "\n" +
                firstLine + "\n" +
                line + "\n" +
                emptyLine + "\n" +
                observersTable + "\n" +
                line + "\n" +
                progressBar + "\n" +
                wideLine + "\n";
        updateAll();
        return result;
    }

    public void updateAll() {
        updateFirstLine();
        updateProgressBar();
        updateObserversTable();
    }


    public void updateFirstLine() {
        firstLine = "NodeID: ";
        firstLine += updater.nodeID.isReady() ? updater.nodeID.get() : -1;
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss | dd.MM.yyyy");
        Date date = new Date();
        String dateString = dateFormat.format(date);
        int actualSize = firstLine.length() + dateString.length();
        for (int i = 0; i < this.size - actualSize; i++) {
            firstLine += " ";
        }
        firstLine += dateFormat.format(date);
        updater.updateNodeID();
    }


    public void updateProgressBar() {
        if (updater.progressSize.isReady() && updater.calculatedNo.isReady()) {
            int calculatedTasks = updater.calculatedNo.get();
            int allTasks = updater.progressSize.get();

            int hashes = (int) ((calculatedTasks * 1.0) / (allTasks * 1.0) * (size * 1.0));
            String description = "Progress: " + calculatedTasks + " of " + allTasks + "\n";
            String bar = "[";
            for (int i = 0; i < hashes-2; i++) {
                bar += "#";
            }
            for (int i = bar.length(); i < (size - 1); i++) {
                bar += "-";
            }
            bar += "]";
            progressBar = description + bar;
        }
        updater.updateCalculatedNo();
        updater.updateProgressSize();
    }

    public void updateObserversTable() {
        String result = "";
        if (updater.calculatedObservers.isReady()) {
            result += "CalculatedPublisher: " + updater.calculatedObservers.get() + "\n";
        }
        if (updater.reservedObservers.isReady()) {
            result += "ReservedPublisher: " + updater.reservedObservers.get() + "\n";
        }
        if (updater.taskObservers.isReady()) {
            result += "TaskPublisher: " + updater.taskObservers.get() + "\n";
        }
        observersTable = result;
        updater.updateCalculatedObservers();
        updater.updateReservedObservers();
        updater.updateTaskObservers();
    }


}

