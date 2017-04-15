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
    private List<Node> currentPath = new LinkedList<>();
    private Set<Node> visitedNodes = new HashSet<>();
    private HashMap<Node, Integer> weightedNodes = new HashMap<>();
    private Stack<Node> exitPath = new Stack<>();

    public EscapeStrategy(EscapeState state) {
        this.state = state;
    }
    public void doEscape() {
        mazeVertices = state.getVertices();
        exitNode = state.getExit();
        Node startNode = state.getCurrentNode();
        initializeUnvisitedNodeMap(weightedNodes, mazeVertices);
        setStartNode(startNode);
        dijktrasPath(startNode);
        int exitValue = weightedNodes.get(exitNode);
        reverseWalkPath(exitNode, exitValue);
        exitPath.pop();
        while(!exitPath.empty()) {
            Node n = exitPath.pop();
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

    private void dijktrasPath(Node current){
        int minDistance = Integer.MAX_VALUE;
        Node nextNodeToTry = null;
        if(current.equals(exitNode)){
            return;
        }
        Collection<Edge> neighbours = new HashSet<>(current.getExits());

        int currentNodeWeight = weightedNodes.get(current);
        for(Iterator<Edge> itr = neighbours.iterator();
                itr.hasNext();){
            Edge e = itr.next();
            if(visitedNodes.contains(e.getDest())){
                itr.remove();
            }
        }
        for(Edge e : neighbours){
            int edgeWeight = e.length();
            int newWeight = currentNodeWeight + edgeWeight;
            int destinationNodeCurrentWeight = weightedNodes.get(e.getDest());

            Node thisNode = e.getDest();
            if(newWeight < destinationNodeCurrentWeight){
                weightedNodes.replace(thisNode, (newWeight));
            }
        }
        visitedNodes.add(current);
        for(Map.Entry<Node, Integer> e: weightedNodes.entrySet()){
            if(e.getValue() < minDistance && !visitedNodes.contains(e.getKey())){
                nextNodeToTry = e.getKey();
                minDistance = e.getValue();
            }
        }
        dijktrasPath(nextNodeToTry);
    }

    class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            long diff = o1.getId() - o2.getId();
            if (diff == 0) {
                return 0;
            } else if (diff < 0) {
                return -1;
            } else {
                return +1;
            }
        }
    };
}
