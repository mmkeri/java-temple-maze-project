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
    Collection<NodeStatus> nodesToEvaluate = new ArrayList<>();
    final List<Long> nodesPreviouslyVisited = new LinkedList<>();
    final Collection<Long> deadEndPaths = new ArrayList<>();

    public ExploreStrategy(ExplorationState state) {
        this.state = state;
    }

    public long firstMoveFromStart(){
        int shortestDist = 10000;
        long nextNode = 0;

        nodesToEvaluate.addAll(state.getNeighbours());
        for(NodeStatus node: nodesToEvaluate){
            if(node.getDistanceToTarget() < shortestDist){
                shortestDist = node.getDistanceToTarget();
                nextNode = node.getId();
            }
        }
        nodesPreviouslyVisited.add(state.getCurrentLocation());
        return nextNode;
    }

    public long pickNeighbourToMoveTo() {

        nodesToEvaluate.addAll(state.getNeighbours());
        long nextNode = 0;
        int shortestDist = 100000;

        ArrayList<NodeStatus> adjustedNodes = nodesToEvaluate
                .stream().filter(x -> !nodesPreviouslyVisited.contains(x.getId()))
                .collect(Collectors.toCollection(ArrayList<NodeStatus>::new));
        nodesToEvaluate = adjustedNodes;

        ArrayList<NodeStatus> readjustedNodes = nodesToEvaluate
                .stream().filter(x -> !deadEndPaths.contains(x.getId()))
                .collect(Collectors.toCollection(ArrayList<NodeStatus>::new));
        nodesToEvaluate = readjustedNodes;

        if(nodesToEvaluate.size() == 0){
            deadEndPaths.add(state.getCurrentLocation());
            nextNode = retraceSteps();
            System.out.println("Backing up to node " + nextNode);
            //nextNode = pickNeighbourToMoveTo();

        }

        for(NodeStatus node: nodesToEvaluate){
            if(node.getDistanceToTarget() < shortestDist){
                shortestDist = node.getDistanceToTarget();
                nextNode = node.getId();
            }
        }
        nodesToEvaluate.clear();
        if(!deadEndPaths.contains(state.getCurrentLocation())) {
            nodesPreviouslyVisited.add(state.getCurrentLocation());
        }
        System.out.println("Moving to node " + nextNode);
        return nextNode;
    }

    public long retraceSteps(){
        long nextNode;
        int shortestDist = 100000;

        nextNode = nodesPreviouslyVisited.remove(nodesPreviouslyVisited.size() - 1);
        deadEndPaths.add(state.getCurrentLocation());
        System.out.println("Should be backing up to " + nextNode);
        nodesToEvaluate.clear();
        return  nextNode;
    }


    public void doExplore() {

        long firstMove = firstMoveFromStart();
        System.out.println("First move taken");
        state.moveTo(firstMove);

        while(state.getDistanceToTarget() > 0) {
            long pickedNeighbour = pickNeighbourToMoveTo();
            System.out.println("picked: "+pickedNeighbour);
            state.moveTo(pickedNeighbour);
        }
    }
}
