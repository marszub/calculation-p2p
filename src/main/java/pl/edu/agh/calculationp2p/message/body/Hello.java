package pl.edu.agh.calculationp2p.message.body;

import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;

import java.net.InetSocketAddress;

public class Hello implements Body{

    private final String newIp;
    //TODO: which port?
    private final int port = 2137;

    public Hello(String ip) {
        this.newIp = ip;
    }

    @Override
    public String serializeType() {
        return "\"hello\"";
    }

    @Override
    public String serializeContent() {
        String result = "";
        result = result.concat("{\"ip\":\"");
        result = this.newIp==null?result.concat("null"):result.concat(this.newIp);
        result = result.concat("\"}");
        return result;
    }

    @Override
    public void process(int sender, MessageProcessContext context) {
        if(this.newIp != null){
            context.getRouter().createInterface(sender, new InetSocketAddress(this.newIp, 2000));
        }
    }
}
