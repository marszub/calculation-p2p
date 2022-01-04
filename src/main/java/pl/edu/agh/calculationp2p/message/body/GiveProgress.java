package pl.edu.agh.calculationp2p.message.body;

import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;
import pl.edu.agh.calculationp2p.state.Progress;

import java.util.Objects;

public class GiveProgress implements Body{

    private final Progress progress;

    public GiveProgress(Progress progress) {
        this.progress = progress;
    }

    @Override
    public String serializeType() {
        return "\"give_progress\"";
    }

    @Override
    public String serializeContent() {
        String result = "";
        result = result.concat("{\"progress\":");
        result = this.progress==null?result.concat("\"null\""):result.concat(this.progress.serialize());
        result = result.concat("}");
        return result;
    }

    @Override
    public void process(int sender, MessageProcessContext context) {

    }

    @Override
    public Body clone() {
        //TODO: deep copy in progress?
        return new GiveProgress(this.progress);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        return getClass() == o.getClass();
     }
}
