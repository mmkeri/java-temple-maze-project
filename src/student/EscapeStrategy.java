package student;

import game.Edge;
import game.EscapeState;
import game.Node;
import game.NodeStatus;

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
    private Stack<Node> reversePath;

    public EscapeStrategy(EscapeState state) {
        this.state = state;
    }
    public void doEscape() {
        DijkstraAlgo pathTester = new DijkstraAlgo();
        ReversePath backwardsPath = new ReversePath();
        mazeVertices = state.getVertices();
        exitNode = state.getExit();
        Node startNode = state.getCurrentNode();
        initializeUnvisitedNodeMap(weightedNodes, mazeVertices);
        setStartNode(startNode);
        testedWeightedNodes = pathTester.testPaths(startNode, exitNode, weightedNodes);
        int exitValue = testedWeightedNodes.get(exitNode);
        reversePath = backwardsPath.plotReversePath(exitNode, exitValue, testedWeightedNodes);

        BroadExploration testPaths = new BroadExploration(startNode, exitNode, weightedNodes, mazeVertices, state.getTimeRemaining());

        reversePath.pop();
        while(!reversePath.empty()) {
            Node n = reversePath.pop();
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
