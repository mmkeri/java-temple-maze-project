package student;

import game.Edge;
import game.Node;

import java.util.Collection;
import java.util.HashMap;
import java.util.Stack;

/**
 * Created by mmkeri on 15/04/2017.
 */
public class ReversePath {

    private Stack<Node> exitPath = new Stack();
    private HashMap<Node, Integer> weightedNodes = new HashMap<>();

    public void setWeightedNodes(HashMap<Node, Integer> weightedNodes){
        this.weightedNodes = weightedNodes;
    }

    public Stack<Node> plotReversePath(Node exitNode, int exitValue, HashMap<Node, Integer> weightedNodes){
        setWeightedNodes(weightedNodes);
        reverseWalkPath(exitNode, exitValue);
        return exitPath;
    }

    private void reverseWalkPath(Node exitNode, int exitWeight){
        Collection<Edge> currentEdges = exitNode.getExits();
        Node nextNode = null;
        int newExitWeight = 0;
        if(exitWeight == 0){
            exitPath.push(exitNode);
            return;
        }
        for(Edge e : currentEdges){
            Node n = e.getDest();
            if(exitWeight - (exitNode.getEdge(n).length()) == weightedNodes.get(n)){
                nextNode = n;
                newExitWeight = exitWeight - (exitNode.getEdge(n).length());
            }
        }
        exitPath.push(exitNode);
        reverseWalkPath(nextNode, newExitWeight);
    }
}
