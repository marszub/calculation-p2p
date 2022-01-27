package pl.edu.agh.calculationp2p.state.UI;

import pl.edu.agh.calculationp2p.state.Progress;
import pl.edu.agh.calculationp2p.state.Servant;
import pl.edu.agh.calculationp2p.state.ServantImpl;
import pl.edu.agh.calculationp2p.state.publisher.CalculatedPublisher;
import pl.edu.agh.calculationp2p.state.publisher.ReservedPublisher;
import pl.edu.agh.calculationp2p.state.publisher.TaskPublisher;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UIBuilder {
    int size;
    int nodeID;
    String wideLine;
    String emptyLine;
    String line;
    String firstLine;
    Servant servant;


    public UIBuilder(int size, int nodeID, Servant servant) {
        this.size = size;
        this.nodeID = nodeID;
        this.wideLine = "";
        this.line = "";
        this.emptyLine= "";
        this.servant = servant;
        for (int i = 0; i < this.size; i++) {
            wideLine+="=";
            line+="-";
            emptyLine +=" ";
        }
        firstLine = "NodeID: " + nodeID;
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss | dd.MM.yyyy");
        Date date = new Date();
        String dateString = dateFormat.format(date);
        int actualSize = firstLine.length() + dateString.length();
        for (int i = 0; i < this.size - actualSize; i++) {
            firstLine+=" ";
        }

    }
    public String makeFirstLine(){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss | dd.MM.yyyy");
        Date date = new Date();
        return firstLine + dateFormat.format(date);
    }

    public String progressBar(){
        int calculatedTasks = servant.getProgress().countCompleted();
        int allTasks = servant.getProgress().getTasks().size();
        int hashes = (int)((calculatedTasks*1.0)/(allTasks*1.0) * (size*1.0));
        String description = "Progress: " + calculatedTasks + " of " + allTasks + "\n";
        String bar = "[";
        for (int i = 0; i < hashes; i++) {
            bar +="#";
        }
        for (int i = bar.length(); i < (size-1); i++) {
            bar+="-";
        }
        bar+="]";

        return description + bar;

    }
    public String observersTable(){
        int calculatedP = servant.getCalculatedPublisher().numberOfObservers();
        int reservedP = servant.getReservedPublisher().numberOfObservers();
        int taskP = servant.getTaskPublisher().numberOfObservers();
        return "CalculatedPublisher: " + calculatedP + "\n" +
                "ReservedPublisher: " + reservedP + "\n" +
                "TaskPublisher: " + taskP;
    }









}

