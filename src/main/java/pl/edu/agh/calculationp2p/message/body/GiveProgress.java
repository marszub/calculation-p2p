package pl.edu.agh.calculationp2p.message.body;

import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;

import java.util.Objects;

public class GiveProgress implements Body{
    public GiveProgress() {

    }

    @Override
    public String serializeType() {
        return "\"give_progress\"";
    }

    @Override
    public String serializeContent() {
        return "{\"progress\":}";
    }

    @Override
    public void process(int sender, MessageProcessContext context) {

    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        return getClass() != o.getClass();
     }
}
