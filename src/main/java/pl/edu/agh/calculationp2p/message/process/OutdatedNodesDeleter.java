package pl.edu.agh.calculationp2p.message.process;

import java.time.ZonedDateTime;
import java.util.*;

public class OutdatedNodesDeleter{
    private final int validityTime;

    protected OutdatedNodesDeleter(int validityTime){
        this.validityTime = validityTime;
    }

    public List<Integer> getOutdatedNodes(Map<Integer, Long> allNodes){
        List<Integer> result = new ArrayList<>();
        for(Integer nodeId : allNodes.keySet()){
            if(now() - allNodes.get(nodeId) > validityTime){
                result.add(nodeId);
            }
        }
        return result;
    }
    private long now(){
        return ZonedDateTime.now().toInstant().toEpochMilli();
    }
}
