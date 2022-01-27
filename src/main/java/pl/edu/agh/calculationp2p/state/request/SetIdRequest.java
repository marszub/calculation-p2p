package pl.edu.agh.calculationp2p.state.request;

import pl.edu.agh.calculationp2p.state.Servant;

public class SetIdRequest implements MethodRequest{
    private final Integer nodeId;
    public SetIdRequest(Integer nodeId){
        this.nodeId = nodeId;
    }

    @Override
    public void call(Servant servant) {
        servant.setNodeId(nodeId);
    }
}
