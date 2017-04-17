package student;

import game.EscapeState;
import game.Node;

import java.util.*;

/**
 * Created by mmkeri on 19/03/2017.
 */
public class EscapeStrategy {

    private EscapeState state;
    private Collection<Node> mazeVertices = new TreeSet<>();
    private Node exitNode;
    private HashMap<Node, Integer> weightedNodes = new HashMap<>();
    private HashMap<Node, Integer> testedWeightedNodes;
    private Stack<Node> finalPath;

    public EscapeStrategy(EscapeState state) {
        this.state = state;
    }
    public void doEscape() {

        DijkstraAlgo pathTester = new DijkstraAlgo(exitNode, weightedNodes);
        mazeVertices = state.getVertices();
        exitNode = state.getExit();
        Node startNode = state.getCurrentNode();
        initializeUnvisitedNodeMap(weightedNodes, mazeVertices);
        setStartNode(startNode);
        testedWeightedNodes = pathTester.testPaths(startNode);
        int exitValue = testedWeightedNodes.get(exitNode);
        DijkstraPathConstructor constructedPath = new DijkstraPathConstructor(exitNode, exitValue, weightedNodes);
        finalPath = constructedPath.constructPath();

        finalPath.pop();

        while(!finalPath.empty()) {
            Node n = finalPath.pop();
            state.moveTo(n);
            if (n.getTile().getGold() > 0) {
                state.pickUpGold();
            }
        }
    }

    private void initializeUnvisitedNodeMap(HashMap<Node, Integer> weightedNodes, Collection<Node> vertices) {
        for (Node n : vertices) {
            weightedNodes.put(n, Integer.MAX_VALUE);
        }
    }

    private void setStartNode(Node startNode){
        weightedNodes.replace(startNode, Integer.MAX_VALUE, 0);
    }

}
