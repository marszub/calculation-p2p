package pl.edu.agh.calculationp2p.state.UI;

import pl.edu.agh.calculationp2p.calculationTask.hashBreaking.HashTaskResult;
import pl.edu.agh.calculationp2p.state.*;
import pl.edu.agh.calculationp2p.state.publisher.CalculatedPublisher;
import pl.edu.agh.calculationp2p.state.publisher.ReservedPublisher;
import pl.edu.agh.calculationp2p.state.publisher.TaskPublisher;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import java.io.IOException;

public class UIController implements Runnable {
    UIBuilder uiBuilder;

    public UIController(Scheduler scheduler) {
        this.uiBuilder = new UIBuilder(scheduler);
    }

    public UIBuilder getUiBuilder() {
        return uiBuilder;
    }

    @Override
    public void run() {

        while (true) {

            // IntelliJ IDEA console is not a real terminal, so there is no command to clear it from your Java code.
            String lowerOSName = System.getProperty("os.name").toLowerCase();

            if (lowerOSName.contains("window")) {
                try {
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
            System.out.println(uiBuilder.getFrame());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    }





