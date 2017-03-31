package student;

import game.ExplorationState;
import game.NodeStatus;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implements the explore part of the Explorer class
 */
public class ExploreStrategy {
    final ExplorationState state;
    ArrayList<NodeStatus> nodesToEvaluate = new ArrayList<>();
    private List<Long> possiblePath = null;
    final List<Long> nodesPreviouslyVisited = new LinkedList<>();
    final Collection<Long> deadEndPaths = new ArrayList<>();
    private TreeMap<SortKey, List<Long> > potentialPaths = new TreeMap<>();

    public ExploreStrategy(ExplorationState state) {
        this.state = state;
    }

    class SortKey implements Comparable<SortKey>  {
        final int distance;
        final long id;
        public SortKey(NodeStatus status) {
            this.distance = status.getDistanceToTarget();
            this.id = status.getId();
        }

        @Override
        public int compareTo(SortKey other) {
            int distanceDiff = this.distance - other.distance;
            if (distanceDiff != 0) {
                return distanceDiff;
            }

            // okay, distance is equal. We still need keys from different IDs to be considered
            // different keys
            if (this.id < other.id) {
                return -1;
            } else if(this.id == other.id) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    private ArrayList<NodeStatus> removePreviouslyVisitedNodes(ArrayList<NodeStatus> nodeList){

        ArrayList<NodeStatus> adjustedNodes = nodesToEvaluate
                .stream().filter(x -> !nodesPreviouslyVisited.contains(x.getId()))
                .collect(Collectors.toCollection(ArrayList<NodeStatus>::new));
        return adjustedNodes;
    }

    private ArrayList<NodeStatus> removeDeadEndNodes(ArrayList<NodeStatus> nodeList){

        ArrayList<NodeStatus> readjustedNodes = nodesToEvaluate
                .stream().filter(x -> !deadEndPaths.contains(x.getId()))
                .collect(Collectors.toCollection(ArrayList<NodeStatus>::new));
        return readjustedNodes;
    }

    private void retraceSteps (){

        SortKey pathKey = potentialPaths.firstKey();
        List<Long> path = potentialPaths.get(pathKey);
        potentialPaths.remove(pathKey);
        long newRoute = path.remove(path.size() - 1);
        long lastCommon = path.remove(path.size() - 1);
        List<Long> newPath = new LinkedList<>(nodesPreviouslyVisited);
        newPath.removeAll(path);
        newPath.remove(newPath.size() - 1);
        nodesPreviouslyVisited.removeAll(newPath);
        Collections.reverse(newPath);
        newPath.add(newRoute);
        deadEndPaths.addAll(newPath);
        for(Long node: newPath){
            state.moveTo(node);
        }
        doExplore();
    }

    private long exploring(){
        long shortestPath = 100000;
        NodeStatus nextNode = null;

        nodesPreviouslyVisited.add(state.getCurrentLocation());

        nodesToEvaluate.addAll(state.getNeighbours());
        nodesToEvaluate = removePreviouslyVisitedNodes(nodesToEvaluate);
        nodesToEvaluate = removeDeadEndNodes(nodesToEvaluate);

        for(NodeStatus n: nodesToEvaluate){
            if(n.getDistanceToTarget() < shortestPath){
                nextNode = n;
                shortestPath = n.getDistanceToTarget();
            }
        }
        nodesToEvaluate.remove(nextNode);

        for(NodeStatus n: nodesToEvaluate){
            possiblePath = new LinkedList<>(nodesPreviouslyVisited);
            possiblePath.add(n.getId());
            potentialPaths.put(new SortKey(n), possiblePath);
        }
        nodesToEvaluate.clear();
        if(nextNode == null){
            retraceSteps();
        }
        return nextNode.getId();
    }


    public void doExplore() {
        while(state.getDistanceToTarget() > 0) {
            long nodeToMoveTo = exploring();
            state.moveTo(nodeToMoveTo);
        }
    }
}