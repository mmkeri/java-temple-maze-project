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
        mazeVertices = state.getVertices();
        exitNode = state.getExit();
    }

    public void doEscape() {

        Node startNode = state.getCurrentNode();

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

    public DijkstraAlgo runDijkstra(Node startNode, Node destinationNode) {
        DijkstraAlgo dijkstra = new DijkstraAlgo(mazeVertices, startNode, destinationNode);
        dijkstra.computeShortestPath();
        return dijkstra;
    }

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