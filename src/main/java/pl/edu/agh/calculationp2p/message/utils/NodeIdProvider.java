package pl.edu.agh.calculationp2p.message.utils;

public class NodeIdProvider {

    private static int nodeId;

    public NodeIdProvider(){

    }

    public void setNodeId(int newId){
        nodeId = newId;
    }

    public int getNodeId(){
        return nodeId;
    }

}
