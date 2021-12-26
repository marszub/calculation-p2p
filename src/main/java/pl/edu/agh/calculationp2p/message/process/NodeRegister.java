package pl.edu.agh.calculationp2p.message.process;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeRegister {

    private final int validityTime;
    private final Map<Integer, InetSocketAddress> publicNodes;
    private final List<Integer> privateNodes;
    private final Map<Integer, Integer> allNodes;

    protected NodeRegister(int validityTime){
        this.allNodes = new HashMap<>();
        this.publicNodes = new HashMap<>();
        this.privateNodes = new ArrayList<>();
        this.validityTime = validityTime;
    }

    protected List<Integer> getOutdatedNodes(){
        List<Integer> result = new ArrayList<>();
        for(Integer nodeId : this.allNodes.keySet()){
            if(this.allNodes.get(nodeId)==0){
                result.add(nodeId);
            }
        }
        for(Integer nodeId : this.allNodes.keySet()){
            this.allNodes.replace(nodeId, 0);
        }
        // TODO: delete those nodes from here
        return result;
    }
    public void addPublicNode(int id, InetSocketAddress ip){
        this.allNodes.put(id, 1);
        this.publicNodes.put(id, ip);
    }
    public void addPrivateNode(int id){
        this.allNodes.put(id, 1);
        this.privateNodes.add(id);
    }
    public void updateNode(int id){
        this.allNodes.replace(id, 1);
    }
    public Map<Integer, InetSocketAddress> getPublicNodes(){
        return this.publicNodes;
    }
    public List<Integer> getPrivateNodes(){
        return this.privateNodes;
    }

}
