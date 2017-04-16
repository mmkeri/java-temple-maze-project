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
    private int countOfMovingAway = 0;

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

    private List<Long> retraceSteps (){

        SortKey pathKey = potentialPaths.firstKey();
        List<Long> path = potentialPaths.get(pathKey);
        potentialPaths.remove(pathKey);
        long newRoute = path.remove(path.size() - 1);
        List<Long> newPath = new LinkedList<>(nodesPreviouslyVisited);
        newPath.removeAll(path);
        newPath.remove(newPath.size() - 1);
        nodesPreviouslyVisited.removeAll(newPath);
        Collections.reverse(newPath);
        newPath.add(newRoute);
        deadEndPaths.addAll(newPath);
        return newPath;
    }

    private long exploring(){
        long shortestPath = 100000;
        long previousDistanceLeft = 100000;
        NodeStatus nextNode = null;

        nodesPreviouslyVisited.add(state.getCurrentLocation());

        nodesToEvaluate.addAll(state.getNeighbours());
        nodesToEvaluate = removePreviouslyVisitedNodes(nodesToEvaluate);
        nodesToEvaluate = removeDeadEndNodes(nodesToEvaluate);
        /**
         * new code to try to reduce the distance away from goal travelled
         */
        /*
        if(countOfMovingAway > 5){
            List<Long> backtrackPAth = reversingPath(countOfMovingAway);
            long newNextNode = backtrackPAth.remove(backtrackPAth.size() - 1);
            for(Long Node : backtrackPAth){
                state.moveTo(Node);
            }
            return newNextNode;
        }
        */

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
            List<Long> newPath = retraceSteps();
            long newNextNode = newPath.remove(newPath.size()-1);
            for(Long node: newPath) {
                state.moveTo(node);
            }
            return newNextNode;
        }
        /*
        if(nextNode.getDistanceToTarget() > shortestPath){
            countOfMovingAway++;
        }
        if(state.getDistanceToTarget() > previousDistanceLeft){
            countOfMovingAway++;
        }
        previousDistanceLeft = state.getDistanceToTarget();
        */
        return nextNode.getId();
    }

    private List<Long> reversingPath(int countOfMovingAway){
        List<Long> reversePath = new LinkedList<>();
        List<Long> pathNode;
        for(int i = 0; i < countOfMovingAway; i++){
            pathNode = retraceSteps();
            reversePath.addAll(pathNode);
        }

        this.countOfMovingAway = 0;
        return reversePath;

    }


    public void doExplore() {
        while(state.getDistanceToTarget() > 0) {
            long nodeToMoveTo = exploring();
            state.moveTo(nodeToMoveTo);
        }
    }
}