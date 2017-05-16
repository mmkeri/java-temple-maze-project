package student;

import game.Edge;
import game.Node;

import java.util.*;

/**
 * Created by mmkeri on 15/04/2017.
 */
public class DijkstraCostUpdater {

    private Node exitNode;
    private Map<Node, Integer> weightedNodes;
    private Set<Node> visitedNodes = new HashSet<>();

    /**
     * constructor for the DijkstraCostUpdater class.
     * @param targetNode
     * @param initialWeightedNodes
     */
    public DijkstraCostUpdater(Node targetNode, Map<Node, Integer> initialWeightedNodes){
        this.exitNode = targetNode;
        this.weightedNodes = initialWeightedNodes;
    }

    /**
     * Method that returns the map of weighted nodes after Dijkstra's algorithm has
     * been used to evaluate the initial graph
     * @param currentNode
     * @return Map<Node, Integer>
     */
    public Map<Node, Integer> computeCosts(Node currentNode){
        dijktrasPath(currentNode);
        return weightedNodes;
    }

    /**
     * Method that uses Dijkstra's algorithm to calculate the weighted value of
     * each node in the graph. Updates the values into the weightedNodes map
     * @param current
     */
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
