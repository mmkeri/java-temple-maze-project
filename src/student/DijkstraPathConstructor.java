package student;

import game.Edge;
import game.Node;

import java.util.Collection;
import java.util.HashMap;
import java.util.Stack;

/**
 * Created by mmkeri on 15/04/2017.
 */
public class DijkstraPathConstructor {

    private Stack<Node> exitPath = new Stack();
    private HashMap<Node, Integer> weightedNodes;
    private Node exitNode;
    private int exitValue;


    public DijkstraPathConstructor(Node exitNode, int exitValue, HashMap<Node, Integer> weightedNodes){
        this.exitNode = exitNode;
        this.exitValue = exitValue;
        this.weightedNodes = weightedNodes;
    }

    public Stack<Node> constructPath(){
        reverseWalkPath(exitNode, exitValue);
        return exitPath;
    }

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
