package pl.edu.agh.calculationp2p.message.body;

import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;

import java.net.InetSocketAddress;
import java.util.Objects;

public class Hello implements Body{

    //TODO: which port?
    private final int port = 2137;
    private final String newIp;

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
        //TODO: router isPublic()
        //boolean isPublic = context.getRouter().isPublic();
        boolean isPublic = false;
        if(isPublic || newIp == null){
            context.getRouter().createInterface(sender);
            context.getNodeRegister().addPrivateNode(sender);
        } else if(!isPublic){
            context.getRouter().createInterface(sender, new InetSocketAddress(newIp, port));
        }
    }

    @Override
    public Body clone() {
        return new Hello(this.newIp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        Hello message = (Hello) o;
        return Objects.equals(message.getNewIp(), this.newIp) && message.getPort() == this.port;
    }
    public String getNewIp() {
        return newIp;
    }

    public int getPort() {
        return port;
    }

    @Override
    public int hashCode() {
        return Objects.hash(port, newIp);
    }
}
