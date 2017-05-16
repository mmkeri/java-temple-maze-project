package student;

import game.EscapeState;
import game.Node;

import java.util.*;

/**
 * Created by mmkeri on 19/03/2017.
 */
public class EscapeStrategy {

    /**
     * Current state of the maze
     */
    private EscapeState state;
    /**
     * TreeSet holding all the nodes of the current maze
     */
    private Collection<Node> mazeVertices = new TreeSet<>();
    /**
     * The Node that represents the exit from the maze
     */
    private Node exitNode;
    /**
     * Stack containing all the Nodes that must be followed, in their current
     * order as they are popped off the stack, to get to the exit from
     * the character's current position
     */
    private Stack<Node> finalPath;

    /**
     * Constructor for the EscapeStrategy object
     * @param state
     */
    public EscapeStrategy(EscapeState state) {
        this.state = state;
        mazeVertices = state.getVertices();
        exitNode = state.getExit();
    }

    /**
     * While the character has not reached the exit node the method
     * attempts to find neighbours with gold while also tracking
     * whether he has enough time to escape before the roof caves in
     */
    public void doEscape() {

        while(state.getCurrentNode() != exitNode){
            if(state.getCurrentNode().getTile().getGold() > 0){
                state.pickUpGold();
            }
            List<DijkstraAlgo> goldPaths = findNeighboursWithGold(1);
            if(goldPaths.size() == 0){
                finalPath = runDijkstra(state.getCurrentNode(), exitNode).getPath();
                while (!finalPath.empty()) {
                    state.moveTo(finalPath.pop());
                }
            } else {
                state.moveTo(goldPaths.get(0).getPath().pop());
            }
        }
    }

    /**
     * Calls the DijkstraAlgo to return the path that is the shortest from the
     * characters current position to the exit
     * @param startNode
     * @param destinationNode
     * @return Stack<Node>
     */
    public DijkstraAlgo runDijkstra(Node startNode, Node destinationNode) {
        DijkstraAlgo dijkstra = new DijkstraAlgo(mazeVertices, startNode, destinationNode);
        dijkstra.computeShortestPath();
        return dijkstra;
    }

    /**
     * Queries the nodes around the character's current position to
     * determine whether or not they have any gold present on them
     * @param requestedCount
     * @return List<DijkstraAlgo>
     */
    public List<DijkstraAlgo> findNeighboursWithGold(int requestedCount) {

        List<DijkstraAlgo> neighboursWithGold = new ArrayList<>(requestedCount);
        Set<Node> seenNodes = new HashSet<>();
        Queue<Node> unprocessedNodes = new ArrayDeque<>();

        unprocessedNodes.add(state.getCurrentNode());
        while(!unprocessedNodes.isEmpty() && neighboursWithGold.size() < requestedCount){
            Node current = unprocessedNodes.poll();

            seenNodes.add(current);
            if(current.getTile().getGold() > 0){
                DijkstraAlgo dijkstraToNode = runDijkstra(state.getCurrentNode(), current);
                DijkstraAlgo dijkstraOutFromNode = runDijkstra(current, exitNode);
                int totalPathCost = dijkstraToNode.getPathCost() + dijkstraOutFromNode.getPathCost();
                if(totalPathCost < state.getTimeRemaining()) {
                    neighboursWithGold.add(dijkstraToNode);
                }
            }
            for(Node n : current.getNeighbours()){
                if(seenNodes.contains(n)){
                    continue;
                } else {
                    unprocessedNodes.add(n);
                }
            }
        }
        return neighboursWithGold;
    }
}