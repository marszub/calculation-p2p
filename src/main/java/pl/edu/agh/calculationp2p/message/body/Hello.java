package pl.edu.agh.calculationp2p.message.body;

import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;

import java.net.InetSocketAddress;
import java.util.Objects;

public class Hello implements Body{

    private final InetSocketAddress newIp;

    public Hello(InetSocketAddress ip) {
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
        result = this.newIp==null?result.concat("null"):result.concat(this.newIp.getAddress().toString().substring(1));
        result = result.concat("\",\"port\":\"");
        result = this.newIp==null?result.concat("null"):result.concat(String.valueOf(this.newIp.getPort()));
        result = result.concat("\"}");
        return result;
    }

    @Override
    public void process(int sender, MessageProcessContext context) {
        boolean isPublic = context.getRouter().isPublic();
        if(isPublic || newIp == null)
            context.getRouter().createInterface(sender);
        else
            context.getRouter().createInterface(sender, this.newIp);

        if(newIp == null)
            context.getNodeRegister().addPrivateNode(sender);
        else
            context.getNodeRegister().addPublicNode(sender, newIp);
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
        return Objects.equals(message.getNewIp(), this.newIp);
    }
    public InetSocketAddress getNewIp() {
        return newIp;
    }


    @Override
    public int hashCode() {
        return Objects.hash(this.newIp);
    }
}
