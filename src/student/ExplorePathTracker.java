package student;

import game.ExplorationState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mmkeri on 17/04/2017.
 */
public class ExplorePathTracker {

    final private ExplorationState state;
    final private List<Long> currentPathFromOrigin = new ArrayList<>();

    public ExplorePathTracker(ExplorationState state){
        this.state = state;
        currentPathFromOrigin.add(state.getCurrentLocation());
    }

    public List<Long> getCurrentPathFromOrigin(){
        return new ArrayList(currentPathFromOrigin);
    }

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
