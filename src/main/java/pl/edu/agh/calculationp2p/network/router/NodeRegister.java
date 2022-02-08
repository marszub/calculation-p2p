package pl.edu.agh.calculationp2p.network.router;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface NodeRegister {
  void addPublicNode(Integer id, InetSocketAddress ip);

  void addPrivateNode(Integer id);

  void updateNode(Integer id);

  Map<Integer, InetSocketAddress> getPublicNodes();

  List<Integer> getPrivateNodes();

  Integer getRandomNodeId();

  Set<Integer> getAllNodesKeys();

  Map<Integer, Long> getAllNodes();

  void deleteInterface(Integer id);

  boolean interfaceExists(Integer id);
}
