package pl.edu.agh.calculationp2p.message.utils;

import java.util.Collections;
import java.util.Set;

public class NodeIdProvider {

    private static int nodeId = 1;

    public static int getNodeId(Set<Integer> set, int myId){
        int max = nodeId;

        if(!set.isEmpty()){
            int setMax = Collections.max(set);
            if(setMax > max)
                max = setMax;
        }

        if(myId>max)
            max = myId;

        nodeId = max+1;
        return nodeId;
    }

}
