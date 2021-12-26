package pl.edu.agh.calculationp2p.message.body;

import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;

public interface Body {
    String serializeType();
    String serializeContent();
    void process(int sender, MessageProcessContext context);
}

