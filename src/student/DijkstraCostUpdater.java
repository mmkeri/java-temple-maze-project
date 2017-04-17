package student;

import game.Edge;
import game.Node;

import java.util.*;

/**
 * Created by mmkeri on 15/04/2017.
 */
public class DijkstraCostUpdater {

    private Node exitNode;
    private HashMap<Node, Integer> weightedNodes;
    private Set<Node> visitedNodes = new HashSet<>();

    public DijkstraCostUpdater(Node targetNode, HashMap<Node, Integer> initialWeightedNodes){
        this.exitNode = targetNode;
        this.weightedNodes = initialWeightedNodes;
    }

    public HashMap<Node, Integer> computeCosts(Node currentNode){
        dijktrasPath(currentNode);
        return weightedNodes;
    }

    private void dijktrasPath(Node current){
        int minDistance = Integer.MAX_VALUE;
        Node nextNodeToTry = null;
        if(current.equals(exitNode)){
            return;
        }
        Collection<Edge> neighbours = new HashSet<>(current.getExits());

        int currentNodeWeight = weightedNodes.get(current);
        for(Iterator<Edge> itr = neighbours.iterator();
            itr.hasNext();){
            Edge e = itr.next();
            if(visitedNodes.contains(e.getDest())){
                itr.remove();
            }
        }
        for(Edge e : neighbours){
            int edgeWeight = e.length();
            int newWeight = currentNodeWeight + edgeWeight;
            int destinationNodeCurrentWeight = weightedNodes.get(e.getDest());

            Node thisNode = e.getDest();
            if(newWeight < destinationNodeCurrentWeight){
                weightedNodes.replace(thisNode, (newWeight));
            }
        }
        visitedNodes.add(current);
        for(Map.Entry<Node, Integer> e: weightedNodes.entrySet()){
            if(e.getValue() < minDistance && !visitedNodes.contains(e.getKey())){
                nextNodeToTry = e.getKey();
                minDistance = e.getValue();
            }
        }
        dijktrasPath(nextNodeToTry);
    }

}
