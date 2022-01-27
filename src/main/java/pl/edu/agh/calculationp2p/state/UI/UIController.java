package pl.edu.agh.calculationp2p.state.UI;

import pl.edu.agh.calculationp2p.calculationTask.hashBreaking.HashTaskResult;
import pl.edu.agh.calculationp2p.state.Progress;
import pl.edu.agh.calculationp2p.state.Servant;
import pl.edu.agh.calculationp2p.state.ServantImpl;
import pl.edu.agh.calculationp2p.state.publisher.CalculatedPublisher;
import pl.edu.agh.calculationp2p.state.publisher.ReservedPublisher;
import pl.edu.agh.calculationp2p.state.publisher.TaskPublisher;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import java.io.IOException;

public class UIController implements Runnable {
    UIBuilder uiBuilder;

    public void init(Servant servant, int nodeID, int size) {
        this.uiBuilder = new UIBuilder(size, nodeID, servant);

    }

    public UIBuilder getUiBuilder() {
        return uiBuilder;
    }

    @Override
    public void run() {

        while(true) {

            // IntelliJ IDEA console is not a real terminal, so there is no command to clear it from your Java code.
            String lowerOSName = System.getProperty("os.name").toLowerCase();

            if(lowerOSName.contains("window")) {
                try {
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
            System.out.println("\n\n\n\n\n\n\n\n\n\n");
            System.out.println(uiBuilder.wideLine);
            System.out.println(uiBuilder.makeFirstLine());
            System.out.println(uiBuilder.line);
            System.out.println(uiBuilder.observersTable());
            System.out.println(uiBuilder.line);
            System.out.println(uiBuilder.progressBar());
            System.out.println(uiBuilder.wideLine);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


}
