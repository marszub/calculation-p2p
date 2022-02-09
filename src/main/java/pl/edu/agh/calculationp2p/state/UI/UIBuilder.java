package pl.edu.agh.calculationp2p.state.UI;

import pl.edu.agh.calculationp2p.state.Scheduler;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class UIBuilder {
    int size;
    String wideLine;
    String emptyLine;
    String line;
    String firstLine;
    String observersTable;
    String progressBar;
    String taskResult;
    String reservedTab;
    String calculatedTab;
    UIUpdater updater;

    public UIBuilder(Scheduler servant) {
        this.size = 100;
        this.wideLine = "";
        this.line = "";
        this.emptyLine = "";
        this.updater = new UIUpdater(servant);
        this.progressBar = "";
        this.observersTable = "";
        this.reservedTab = "";
        this.calculatedTab = "";

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
                taskResult + "\n" +
                emptyLine + "\n" +
                progressBar + "\n" +
                emptyLine + "\n" +
                wideLine + "\n" +
                emptyLine + "\n" +
                reservedTab + "\n" +
                emptyLine + "\n" +
                line + "\n" +
                emptyLine + "\n" +
                calculatedTab + "\n" +
                emptyLine + "\n" +
                wideLine + "\n";
        updateAll();
        return result;
    }

    public void updateAll() {
        updateFirstLine();
        updateTaskResult();
        updateProgressTab();
        updateProgressBar();
    }


    private void updateProgressTab() {
        if (updater.progress.isReady()) {
            reservedTab = "";
            calculatedTab = "";
            var reserved = new Object() {
                int reservedCounter = 0;
            };
            var calculated = new Object() {
                int calculatedCounter = 0;
            };

            List<TaskRecord> taskList = updater.progress.get().getTasks();
            taskList.stream().forEach(record -> {
                if (record.getState() != TaskState.Calculated && record.getState() != TaskState.Free) {
                    reservedTab += "|" + String.format("%4s", record.getTaskID())
                            + record.getState().firstLetter()
                            + String.format("%3s", record.getOwner()) + " ";
                    reserved.reservedCounter++;
                    if (reserved.reservedCounter % 10 == 0) {
                        reservedTab += "|\n";
                    }
                }

                if (record.getState() != TaskState.Reserved && record.getState() != TaskState.Free) {
                    calculatedTab += "|" + String.format("%4s", record.getTaskID()) +
                            record.getState().firstLetter()
                            + String.format("%3s", record.getOwner()) + " ";
                    calculated.calculatedCounter++;
                    if (calculated.calculatedCounter % 10 == 0) {
                        calculatedTab += "|\n";
                    }
                }
            });
        }
        if (!reservedTab.equals(""))
            reservedTab += "|";

        if (!calculatedTab.equals(""))
            calculatedTab += "|";
    }


    private void updateTaskResult() {
        if (updater.progress.isReady()) {
            taskResult = "Result: ";
            List<TaskRecord> taskList = updater.progress.get().getTasks();
            taskList.stream().forEach(record -> {
                if (record.getState() == TaskState.Calculated && record.getResult() != null && !Objects.equals(record.getResult().serialize(), "[]")) {
                    taskResult += record.getResult().serialize() + "\n";
                }
            });

        }
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
        if (updater.progress.isReady() && updater.calculatedNo.isReady()) {
            int calculatedTasks = updater.calculatedNo.get();
            int allTasks = updater.progress.get().size();

            int hashes = (int) ((calculatedTasks * 1.0) / (allTasks * 1.0) * (size * 1.0));
            String description = "Progress: " + calculatedTasks + " of " + allTasks + "\n";
            String bar = "[";
            for (int i = 0; i < hashes - 2; i++) {
                bar += "#";
            }
            for (int i = bar.length(); i < (size - 1); i++) {
                bar += "-";
            }
            bar += "]";
            progressBar = description + bar;
        }
        updater.updateCalculatedNo();
        updater.updateProgress();
    }


}

