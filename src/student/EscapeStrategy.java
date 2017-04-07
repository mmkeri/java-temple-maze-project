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
    private Set<Node>  neighboursToEvaluate = new TreeSet<>();
    private Node currentNode;
    private Node nodeToMoveTo;
    private List<Node> nodesToVisit= new LinkedList<>();

    public EscapeStrategy(EscapeState state) {
        this.state = state;
    }
    public void doEscape() {
        mazeVertices = state.getVertices();
        List<Node> potentialPath = possiblePath();
        for(int i = 0; i < 3;i++) {
            System.out.println(state.getTimeRemaining());
            currentNode = state.getCurrentNode();
            neighboursToEvaluate = currentNode.getNeighbours();
            nodeToMoveTo = neighboursToEvaluate.iterator().next();
            nodesToVisit.add(nodeToMoveTo);
            state.moveTo(nodeToMoveTo);
            System.out.println(state.getTimeRemaining());
        }
        for(Node n: nodesToVisit){
            System.out.println(n);
        }
        for(Node n: potentialPath){
            System.out.println(n);
        }
    }
    private List<Node> possiblePath(){
        Node currentNode = state.getCurrentNode();
        Node nextNode = null;
        Set<Node>  neighbours;
        List<Node> possibleList = new LinkedList<>();

        for(int i = 0; i < 5; i++){
            neighbours = currentNode.getNeighbours();
            possibleList.add(neighbours.iterator().next());
            currentNode = neighbours.iterator().next();

        }
        return possibleList;
    }
}
