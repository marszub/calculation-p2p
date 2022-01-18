package pl.edu.agh.calculationp2p.message.body;

import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;

import java.net.InetSocketAddress;
import java.util.*;

public class GiveInit implements Body{

    private final List<Integer> privateNodes;
    private final Map<Integer, InetSocketAddress> publicNodes;
    private final int newId;

    public GiveInit(int newId, List<Integer> privateNodes, Map<Integer, InetSocketAddress> publicNodes) {
        this.newId = newId;
        this.privateNodes = privateNodes;
        this.publicNodes = publicNodes;
    }

    @Override
    public String serializeType() {
        return "\"give_init\"";
    }

    @Override
    public String serializeContent() {

        String result = "";
        result = result.concat("{\"your_new_id\":");
        result = result.concat(String.valueOf(this.newId));
        result = result.concat(",\"public_nodes\":[");

        List<Integer> keys = new ArrayList<>();
        this.publicNodes.forEach((id, ip)-> keys.add(id));

        for(int i=0;i<keys.size();i++){
            result = result.concat("{\"id\":");
            result = result.concat(String.valueOf(keys.get(i)));
            result = result.concat(",\"ip_address\":\"");
            result = result.concat(String.valueOf(this.publicNodes.get(keys.get(i)).getAddress()));
            result = result.concat("\"}");
            if(i<keys.size()-1){
                result = result.concat(",");
            }
        }
        result = result.concat("],\"private_nodes\":[");
        for(int i=0;i<this.privateNodes.size();i++){
            result = result.concat("{\"id\":"+this.privateNodes.get(i)+"}");
            if(i<this.privateNodes.size()-1){
                result = result.concat(",");
            }
        }
        result = result.concat("]}");
        return result;
    }

    @Override
    public void process(int sender, MessageProcessContext context) {
        this.privateNodes.forEach(node -> {
            context.getRouter().createInterface(node);
            context.getNodeRegister().addPrivateNode(node);
        });
        this.publicNodes.forEach((nodeId, ip) -> {
            context.getRouter().createInterface(nodeId, ip);
            context.getNodeRegister().addPublicNode(nodeId, ip);
        });
        context.getRouter().setId(this.newId);
    }

    @Override
    public Body clone() {
        return new GiveInit(this.newId, new ArrayList<>(this.privateNodes), new HashMap<>(this.publicNodes));
    }

    public List<Integer> getPrivateNodes() {
        return privateNodes;
    }

    public Map<Integer, InetSocketAddress> getPublicNodes() {
        return publicNodes;
    }

    public int getNewId() {
        return newId;
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
        GiveInit message = (GiveInit) o;
        return message.getNewId() == this.newId &&
                comparePrivateNodes(message.getPrivateNodes()) &&
                message.getPublicNodes().equals(this.publicNodes);
    }

    private boolean comparePrivateNodes(List<Integer> privateNodes){
        Set<Integer> privateNodesSet = new HashSet<>(privateNodes);
        Set<Integer> thisPrivateNodesSet = new HashSet<>(this.privateNodes);
        for(Integer own: thisPrivateNodesSet){
            for(Integer out: privateNodesSet){
                if(Objects.equals(out, own)){
                    privateNodesSet.remove(out);
                    break;
                } else {
                    return false;
                }
            }
        }
        return privateNodes.size() == this.privateNodes.size();
    }

}
