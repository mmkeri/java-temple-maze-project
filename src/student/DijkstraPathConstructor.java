package student;

import game.Edge;
import game.Node;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by mmkeri on 15/04/2017.
 */
public class DijkstraPathConstructor {

    /**
     * Stack of Nodes that represents the path out of the maze
     */
    private Stack<Node> exitPath = new Stack();
    /**
     * Map containing the Nodes with weights that have been determined by using
     * Dijsktra's algorithm
     */
    private Map<Node, Integer> weightedNodes;
    /**
     * The exit node of the current
     */
    private Node exitNode;
    private int exitValue;

    /**
     * Constructor for the DijsktraPathConstructor
     * @param exitNode
     * @param exitValue
     * @param weightedNodes
     */
    public DijkstraPathConstructor(Node exitNode, int exitValue, Map<Node, Integer> weightedNodes){
        this.exitNode = exitNode;
        this.exitValue = exitValue;
        this.weightedNodes = weightedNodes;
    }

    /**
     * Returns a stack of Nodes that represents the path the character
     * needs to take to get to the exit
     * @return Stack<Node>
     */
    public Stack<Node> constructPath(){
        reverseWalkPath(exitNode, exitValue);
        return exitPath;
    }

    /**
     * Constructs a path backwards from either a dead-end or from a point that
     * is deemed "too far away" from the exit
     * @param currentNode
     * @param currentWeight
     */
    private void reverseWalkPath(Node currentNode, int currentWeight){
        Collection<Edge> currentEdges = currentNode.getExits();
        Node nextNode = null;
        int newWeight = 0;
        if(currentWeight == 0){
            exitPath.push(currentNode);
            return;
        }
        for(Edge e : currentEdges){
            Node n = e.getDest();
            if(currentWeight - (currentNode.getEdge(n).length()) == weightedNodes.get(n)){
                nextNode = n;
                newWeight = currentWeight - (currentNode.getEdge(n).length());
            }
        }
        exitPath.push(currentNode);
        reverseWalkPath(nextNode, newWeight);
    }
}
