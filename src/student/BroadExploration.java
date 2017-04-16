package student;

import game.Node;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mmkeri on 16/04/2017.
 */
public class BroadExploration {

    private Node startNode;
    private Node endNode;
    private HashMap<Node, Integer> weightedNodes;
    private Collection<Node> vertices;
    private long timeRemaining;

    public BroadExploration(Node start, Node finish, HashMap<Node, Integer> weightedNodes, Collection<Node> vertices, long timeRemaining){
        startNode = start;
        endNode = finish;
        this.weightedNodes = weightedNodes;
        this.vertices = vertices;
        this.timeRemaining = timeRemaining;
    }

    private HashMap<Node, Integer> evaluatePaths(){

        HashMap<Node, Integer> pathToNode;
        HashMap<Node, Integer> pathToExit;
        HashMap<Node, Integer> longestPath = new HashMap<>();
        DijkstraAlgo pathEvaluator = new DijkstraAlgo();
        long previouslyLongestPath = Long.MIN_VALUE;

        for(Node n : vertices){
            pathToNode = pathEvaluator.testPaths(startNode, n, weightedNodes);
            pathToExit = pathEvaluator.testPaths(n, endNode, weightedNodes);
            if(pathToExit.get(endNode) < timeRemaining && pathToExit.get(endNode) > previouslyLongestPath){
                previouslyLongestPath = pathToExit.get(n);
                longestPath = pathToExit;
            }
        }
        return longestPath;
    }

}
