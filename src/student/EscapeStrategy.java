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
    private Stack<Node> finalPath;

    public EscapeStrategy(EscapeState state) {
        this.state = state;
    }
    public void doEscape() {

        mazeVertices = state.getVertices();
        exitNode = state.getExit();
        Node startNode = state.getCurrentNode();
        DijkstraAlgo pathComputer = new DijkstraAlgo(mazeVertices, startNode, exitNode);

        finalPath = pathComputer.computeShortestPath();

        while(!finalPath.empty()) {
            Node n = finalPath.pop();
            state.moveTo(n);
            if (n.getTile().getGold() > 0) {
                state.pickUpGold();
            }
        }
    }

}
