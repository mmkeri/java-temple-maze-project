package student;

import game.Node;

import java.util.Collection;
import java.util.HashMap;
import java.util.Stack;

/**
 * Created by mmkeri on 17/04/2017.
 */
public class DijkstraAlgo {

    /**
     * Created by mmkeri on 19/03/2017.
     */

    private Collection<Node> mazeVertices;
    private Node destinationNode;
    private Node startNode;
    private Stack<Node> path;
    private int pathCost = 0;

    public Node getDestinationNode(){
        return destinationNode;
    }

    public Node getStartNode(){
        return startNode;
    }
    public int getPathCost(){
        return pathCost;
    }

    public Stack<Node> getPath(){
        return path;
    }

    public DijkstraAlgo(Collection<Node> mazeVertices, Node startNode, Node exitNode) {
        this.mazeVertices = mazeVertices;
        this.startNode = startNode;
        this.destinationNode = exitNode;
    }

    public Stack<Node> computeShortestPath() {
        HashMap<Node, Integer> weightedNodes = new HashMap<>();
        for (Node n : mazeVertices) {
            weightedNodes.put(n, Integer.MAX_VALUE);
        }
        weightedNodes.replace(startNode, Integer.MAX_VALUE, 0);

        DijkstraCostUpdater costUpdater = new DijkstraCostUpdater(destinationNode, weightedNodes);
        costUpdater.computeCosts(startNode);
        pathCost = weightedNodes.get(destinationNode);
        DijkstraPathConstructor constructedPath = new DijkstraPathConstructor(destinationNode, pathCost, weightedNodes);

        path = constructedPath.constructPath();
        path.pop();
        return path;
    }
}

