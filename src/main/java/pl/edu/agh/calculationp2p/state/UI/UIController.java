package pl.edu.agh.calculationp2p.state.UI;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import pl.edu.agh.calculationp2p.state.*;

import java.io.IOException;

// TODO: current result, my stats, global stats

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
                    Logger logger = LoggerFactory.getLogger("");
                    logger.error(e.getMessage());
                }
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
            System.out.println(uiBuilder.getFrame());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Logger logger = LoggerFactory.getLogger("");
                logger.error(e.getMessage());
            }

        }
    }

    }





