package pl.edu.agh.calculationp2p.network.utilities;

import pl.edu.agh.calculationp2p.network.router.NodeRegister;

import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DummyNodeRegister implements NodeRegister {
  @Override
  public void addPublicNode(Integer id, InetSocketAddress ip) {
  }

  @Override
  public void addPrivateNode(Integer id) {

  }

  @Override
  public void updateNode(Integer id) {

  }

  @Override
  public Map<Integer, InetSocketAddress> getPublicNodes() {
    return null;
  }

  @Override
  public List<Integer> getPrivateNodes() {
    return new LinkedList<>();
  }

  @Override
  public Integer getRandomNodeId() {
    return null;
  }

  @Override
  public Set<Integer> getAllNodesKeys() {
    return null;
  }

  @Override
  public Map<Integer, Long> getAllNodes() {
    return null;
  }

  @Override
  public void deleteInterface(Integer id) {

  }

  @Override
  public boolean interfaceExists(Integer id) {
    return false;
  }
}
