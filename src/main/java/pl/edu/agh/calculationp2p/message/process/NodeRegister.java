package pl.edu.agh.calculationp2p.message.process;

import java.net.InetSocketAddress;
import java.time.ZonedDateTime;
import java.util.*;

public class NodeRegister {
    private final int validityTime;
    private final Map<Integer, InetSocketAddress> publicNodes;
    private final List<Integer> privateNodes;
    private final Map<Integer, Long> allNodes;

    protected NodeRegister(int validityTime){
        this.allNodes = new HashMap<>();

        this.publicNodes = new HashMap<>();
        this.privateNodes = new ArrayList<>();

        this.validityTime = validityTime;
    }

    public List<Integer> getOutdatedNodes(){
        List<Integer> result = new ArrayList<>();
        for(Integer nodeId : this.allNodes.keySet()){
            if(now() - this.allNodes.get(nodeId) > validityTime){
                result.add(nodeId);
            }
        }
        result.forEach(nodeId -> {
            this.allNodes.remove(nodeId);
            this.publicNodes.remove(nodeId);
            this.privateNodes.remove(nodeId);

        });
        return result;
    }

    public void addPublicNode(int id, InetSocketAddress ip){
        this.allNodes.put(id, now());
        this.publicNodes.put(id, ip);
    }

    public void addPrivateNode(int id){
        this.allNodes.put(id, now());
        this.privateNodes.add(id);
    }

    public void updateNode(int id){
        this.allNodes.replace(id, now());
    }

    public Map<Integer, InetSocketAddress> getPublicNodes(){
        return this.publicNodes;
    }

    public List<Integer> getPrivateNodes(){
        return this.privateNodes;
    }

    public Integer getRandomNodeId(){
        List<Integer> nodes = getPrivateNodes();
        nodes.addAll(getPublicNodes().keySet().stream().toList());
        Random rand = new Random();
        return nodes.get(rand.nextInt(nodes.size()));
    }

    private long now(){
        return ZonedDateTime.now().toInstant().toEpochMilli();
    }
}
