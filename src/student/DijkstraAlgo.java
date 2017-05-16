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
    /**
     * Collection of Nodes
     */
    private Collection<Node> mazeVertices;
    /**
     * Node representing the target node for the escape strategy which is later set to be
     * the exit
     */
    private Node destinationNode;
    /**
     * Node where the character begins the escape phase from
     */
    private Node startNode;
    /**
     * Stack of Nodes representing the path calculated using Dijkstra's Algorithm to
     * determine the weighted values of the nodes in the graph then identifying the
     * shortest path from the current position to the exit
     */
    private Stack<Node> path;
    /**
     * Variable used to track the value of the current path
     */
    private int pathCost = 0;

    /**
     * Returns the current cost for the path that has been calculated so far
     * @return
     */
    public int getPathCost(){
        return pathCost;
    }

    /**
     * Returns the Stack containing the Nodes in the order they need to be
     * followed to get from the current position to the exit
     * @return
     */
    public Stack<Node> getPath(){
        return path;
    }

    /**
     * constructor for the DijkstraAlgo class
     * @param mazeVertices
     * @param startNode
     * @param exitNode
     */
    public DijkstraAlgo(Collection<Node> mazeVertices, Node startNode, Node exitNode) {
        this.mazeVertices = mazeVertices;
        this.startNode = startNode;
        this.destinationNode = exitNode;
    }

    /**
     * First calculates the weighted values for the graph using the Dijkstra algorithm
     * via the DijkstraCostUpdater class. This new Map is then passed to the
     * DijkstraPathConstructor which returns a stack containing the Nodes in
     * the order that the character needs to follow to get to the target
     * @return Stack<Node>
     */
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

