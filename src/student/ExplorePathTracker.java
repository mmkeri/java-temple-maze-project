package student;

import game.ExplorationState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mmkeri on 17/04/2017.
 */
public class ExplorePathTracker {

    /**
     * The current state of the maze
     */
    final ExplorationState state;
    /**
     * ArrayList that keeps track of the ID numbers of the path that the
     * character has taken so far
     */
    final List<Long> currentPathFromOrigin = new ArrayList<>();

    /**
     * Constructor for the class. Takes in the state of the maze at the
     * time of construction
     * @param state
     */
    public ExplorePathTracker(ExplorationState state){
        this.state = state;
        currentPathFromOrigin.add(state.getCurrentLocation());
    }

    /**
     * ArrayList that returns the current path from the origin when called
     * @return ArrayList of long representing the ID values of tiles
     */
    public List<Long> getCurrentPathFromOrigin(){
        return new ArrayList(currentPathFromOrigin);
    }

    /**
     * Calls the moveTo method provided to move the character to the next tile.
     * @param nodeId
     */
    public void moveTo(long nodeId){

        state.moveTo(nodeId);
        if(currentPathFromOrigin.size() >= 2 && currentPathFromOrigin.get(currentPathFromOrigin.size() - 2)
            == nodeId){
            currentPathFromOrigin.remove(currentPathFromOrigin.size() - 1); //Backing up
        } else {
            currentPathFromOrigin.add(nodeId);
        }

    }
}
