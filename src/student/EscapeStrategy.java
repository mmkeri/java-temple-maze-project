package student;

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
    private List<Node> currentPath = new LinkedList<>();
    private List<Node> repeatedPath = new LinkedList<>();
    private Set<Node> visitedNodes = new TreeSet<>(new NodeComparator());
    private List<Node> bestPath = null;
    private List<List<Node>> collectionOfPaths = new LinkedList<>();
    private int maxDistance = 0;
    private int maxGold = 0;
    private int initialTimeRemaining = 0;

    public EscapeStrategy(EscapeState state) {
        this.state = state;
    }
    public void doEscape() {
        mazeVertices = state.getVertices();
        exitNode = state.getExit();
        Node startNode = state.getCurrentNode();
        initialTimeRemaining = state.getTimeRemaining();
        walkTheGraph(startNode, currentPath, visitedNodes);
        System.out.println("just finished walking the graph");
        bestPath.remove(startNode);
        bestPath.add(exitNode);
        for(Node n: bestPath){
            state.moveTo(n);
            if(n.getTile().getGold() > 0){
                state.pickUpGold();
            }
        }
        state.getExit();
    }

    class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            if(o1.getId() == o2.getId()) {
                return 0;
            } else {
                return -1;
            }
        }
    };

    private void walkTheGraph(Node n, List<Node> currentPath, Set<Node> visitedNodes){
        visitedNodes.add(n);
        if(currentPath.size() >= mazeVertices.size()){
            return;
        }
        if (repeatedPath.contains(n)) {
            currentPath.remove(n);
            return;
        }
        if (currentPath.contains(n)) {
            repeatedPath.add(n);
        }
        currentPath.add(n);
        if(n == exitNode){
            List<Node> copyOfPath = new LinkedList<>(currentPath);
            scoreCurrentPath(copyOfPath);
            copyOfPath.remove(n);
            return;
        }
        for(Node child: n.getNeighbours()){
            walkTheGraph(child, currentPath, visitedNodes);
        }
        currentPath.remove(n);
    }

    private void scoreCurrentPath(List<Node> path){
        collectionOfPaths.add(path);
        int distance = 0;
        int gold = 0;
        for(int i = 0; i < path.size() -1; i ++){
            gold = gold + path.get(i).getTile().getGold();
            distance = distance + path.get(i).getEdge(path.get(i + 1)).length();
        }
        if(distance >= maxDistance && distance <= initialTimeRemaining && gold >= maxGold) {
            bestPath = path;
        }
    }
}
