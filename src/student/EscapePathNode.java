package student;

import game.EscapeState;
import game.Node;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mmkeri on 07/04/2017.
 */
public class EscapePathNode implements Comparable<EscapePathNode>{

    private int goldFoundOnPath = 0;
    private List<Node> possiblePath = new LinkedList<>();
    private Node startNode = null;
    private long id = 0;

    public EscapePathNode(long id, int gold, Node startNode){
        this.id = id;
        this.goldFoundOnPath = gold;
        this.startNode = startNode;
    }

    public int getGoldFoundOnPath(){
        return goldFoundOnPath;
    }

    public List<Node> getPossiblePath() {
        return possiblePath;
    }

    public long getId(){
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(!EscapePathNode.class.isAssignableFrom(obj.getClass())){
            return false;
        }
        final EscapePathNode other = (EscapePathNode)obj;
        if(this.id == other.id && this.goldFoundOnPath == other.goldFoundOnPath){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(EscapePathNode other) {
        if(this.id == other.id){
            return 0;
        } else if (this.id < other.id){
            return -1;
        } else {
            return 1;
        }
    }
}
