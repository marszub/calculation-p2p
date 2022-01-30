package pl.edu.agh.calculationp2p.message.utils;

import java.util.Collections;
import java.util.Set;

public class NodeIdProvider {

    private static int nodeId = 1;


    public static int getNodeId(Set<Integer> set, int myId){
        int max = Collections.max(set);
        if(myId>max)
            max = myId;

        if(nodeId>max)
            max = nodeId;

        nodeId = max+1;
        return nodeId;
    }

}
