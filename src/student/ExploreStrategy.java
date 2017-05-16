package student;

import game.ExplorationState;
import game.NodeStatus;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implements the explore part of the Explorer class
 */
public class ExploreStrategy {

    /**
     * Current state of the maze in exploration phase
     */
    final ExplorationState state;
    /**
     * HashSet of the nodes that the character has visited so far
     */
    final HashSet<Long> nodesPreviouslyVisited = new HashSet<>();
    /**
     * ArrayList that keeps track of any paths that are determined to be
     * dead-ends
     */
    final Collection<Long> deadEndPaths = new ArrayList<>();
    /**
     * TreeMap that contains the potential paths that the character could take from
     * his current position
     */
    private TreeMap<SortKey, List<Long> > potentialPaths = new TreeMap<>();
    /**
     * Instance of the ExplorePathTracker object for use in this class
     */
    final private ExplorePathTracker pathTracker;

    /**
     * Creates a new explorePathTracker object which is used to track the
     * best curent path to date
     * @param state
     */
    public ExploreStrategy(ExplorationState state) {
        this.state = state;
        pathTracker = new ExplorePathTracker(state);
    }

    /**
     * sorting method to evaluate different NodeStatus objects
     */
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

    /**
     * Removes nodes that have previously been visited from the list
     * @param nodeList
     * @return  ArrayList<NodeStatus>
     */
    private ArrayList<NodeStatus> removePreviouslyVisitedNodes(ArrayList<NodeStatus> nodeList){

        ArrayList<NodeStatus> adjustedNodes = nodeList
                .stream().filter(x -> !nodesPreviouslyVisited.contains(x.getId()))
                .collect(Collectors.toCollection(ArrayList<NodeStatus>::new));
        return adjustedNodes;
    }

    /**
     * Removes any nodes that are part of a path that has been determined to end in a
     * dead-end
     * @param nodeList
     * @return ArrayList<NodeStatus>
     */
    private ArrayList<NodeStatus> removeDeadEndNodes(ArrayList<NodeStatus> nodeList){

        ArrayList<NodeStatus> readjustedNodes = nodeList
                .stream().filter(x -> !deadEndPaths.contains(x.getId()))
                .collect(Collectors.toCollection(ArrayList<NodeStatus>::new));
        return readjustedNodes;
    }

    /**
     * Retraces the path through the maze when called
     * either because the character has become stuck or
     * because he has moved too far away from the target
     * @return List of tile ID numbers as longs
     */
    private List<Long> retraceSteps (){

        SortKey pathKey = potentialPaths.firstKey();
        List<Long> path = potentialPaths.get(pathKey);
        potentialPaths.remove(pathKey);
        List<Long> currentPath = pathTracker.getCurrentPathFromOrigin();
        final int highestCommonIndex = findHighestCommonIndex(path, currentPath);
        List<Long> retracePath = new ArrayList<>();
        for(int i = currentPath.size() - 2; i > highestCommonIndex; i--){
            retracePath.add(currentPath.get(i));
        }
        retracePath.add(currentPath.get(highestCommonIndex));
        for(int i = highestCommonIndex + 1; i < path.size(); i++){
            retracePath.add(path.get(i));
        }
        return retracePath;
    }

    /**
     * Finds the point in the list where two paths start to diverge. Used by the
     * retraceSteps method to allow the character to move back out of a dead-end
     * path or when he was moving too far away from the target
     * @param path
     * @param currentPath
     * @return int
     */
    private int findHighestCommonIndex(List<Long> path, List<Long> currentPath) {
        int i = 0;
        while(currentPath.get(i).equals(path.get(i))){
            i++;
        }
        return i - 1;
    }

    /**
     * Obtains a list of the nodes surrounding the character currently and evaluates
     * the possible paths they represent. Eventually returns the ID number of the
     * node that should be moved to next
     * @return long
     */
    private long exploring(){
        long shortestPath = 100000;
        long previousDistanceLeft = 100000;
        NodeStatus nextNode = null;

        nodesPreviouslyVisited.add(state.getCurrentLocation());

        ArrayList<NodeStatus> nodesToEvaluate = new ArrayList<>(state.getNeighbours());
        nodesToEvaluate = removePreviouslyVisitedNodes(nodesToEvaluate);
        //nodesToEvaluate = removeDeadEndNodes(nodesToEvaluate);

        for(NodeStatus n: nodesToEvaluate){
            if(n.getDistanceToTarget() < shortestPath){
                nextNode = n;
                shortestPath = n.getDistanceToTarget();
            }
        }
        if(potentialPaths.size() > 0) {
            SortKey bestAlternative = potentialPaths.firstKey();
            if (bestAlternative.distance < shortestPath) {

                for(NodeStatus n: nodesToEvaluate){
                    List<Long> possiblePath = pathTracker.getCurrentPathFromOrigin();
                    possiblePath.add(n.getId());
                    potentialPaths.put(new SortKey(n), possiblePath);
                }

                List<Long> newPath = retraceSteps();
                long newNextNode = newPath.remove(newPath.size() - 1);
                for (Long node : newPath) {
                    pathTracker.moveTo(node);
                }
                return newNextNode;
            }
        }

        nodesToEvaluate.remove(nextNode);

        for(NodeStatus n: nodesToEvaluate){
            List<Long> possiblePath = pathTracker.getCurrentPathFromOrigin();
            possiblePath.add(n.getId());
            potentialPaths.put(new SortKey(n), possiblePath);
        }
        if(nextNode == null){
            List<Long> newPath = retraceSteps();
            long newNextNode = newPath.remove(newPath.size()-1);
            for(Long node: newPath) {
                pathTracker.moveTo(node);
            }
            return newNextNode;
        }
        return nextNode.getId();
    }

    /**
     * Base method for the exploration that calls the explore and move methods as long
     * as the character has not reached the target
     */
    public void doExplore() {
        while(state.getDistanceToTarget() > 0) {
            long nodeToMoveTo = exploring();
            pathTracker.moveTo(nodeToMoveTo);
        }
    }
}