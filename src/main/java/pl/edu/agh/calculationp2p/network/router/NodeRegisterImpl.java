package pl.edu.agh.calculationp2p.network.router;

import java.net.InetSocketAddress;
import java.time.ZonedDateTime;
import java.util.*;

public class NodeRegisterImpl implements NodeRegister {
    private final Map<Integer, InetSocketAddress> publicNodes;
    private final List<Integer> privateNodes;
    private final Map<Integer, Long> allNodes;

    public NodeRegisterImpl(){
        this.allNodes = new HashMap<>();

        this.publicNodes = new HashMap<>();
        this.privateNodes = new ArrayList<>();
    }

    @Override
    public void addPublicNode(Integer id, InetSocketAddress ip){
        this.allNodes.put(id, now());
        this.publicNodes.put(id, ip);
    }

    @Override
    public void addPrivateNode(Integer id){
        this.allNodes.put(id, now());
        this.privateNodes.add(id);
    }

    @Override
    public void updateNode(Integer id){
        this.allNodes.replace(id, now());
    }

    @Override
    public Map<Integer, InetSocketAddress> getPublicNodes(){
        return this.publicNodes;
    }

    @Override
    public List<Integer> getPrivateNodes(){
        return this.privateNodes;
    }

    @Override
    public Integer getRandomNodeId(){ // TODO: What if 0 nodes in net??
        List<Integer> nodes = publicNodes.keySet().stream().toList();
        if(nodes.size() == 0)
        {
            return null;
        }
        Random rand = new Random();
        return nodes.get(rand.nextInt(nodes.size()));
    }

    @Override
    public Set<Integer> getAllNodes() {
        return allNodes.keySet();
    }

    @Override
    public void deleteInterface(Integer id) {
        if(allNodes.containsKey(id))
        {
            allNodes.remove(id);
            if (privateNodes.contains(id))
            {
                privateNodes.remove(id);
                return;
            }
            publicNodes.remove(id);
        }
    }

    @Override
    public boolean interfaceExists(Integer id) {
        return allNodes.containsKey(id);
    }

    private long now(){
        return ZonedDateTime.now().toInstant().toEpochMilli();
    }
}
