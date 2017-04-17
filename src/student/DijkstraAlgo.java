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
    private Node exitNode;
    private Node startNode;
    private HashMap<Node, Integer> weightedNodes = new HashMap<>();

    public DijkstraAlgo(Collection<Node> mazeVertices, Node startNode, Node exitNode) {
        this.mazeVertices = mazeVertices;
        this.startNode = startNode;
        this.exitNode = exitNode;
        for (Node n : mazeVertices) {
            weightedNodes.put(n, Integer.MAX_VALUE);
        }
        weightedNodes.replace(startNode, Integer.MAX_VALUE, 0);
    }

    public Stack<Node> computeShortestPath() {
        DijkstraCostUpdater costUpdater = new DijkstraCostUpdater(exitNode, weightedNodes);
        costUpdater.computeCosts(startNode);
        int exitValue = weightedNodes.get(exitNode);
        DijkstraPathConstructor constructedPath = new DijkstraPathConstructor(exitNode, exitValue, weightedNodes);

        Stack<Node> finalPath = constructedPath.constructPath();
        finalPath.pop();
        return finalPath;
    }
}

