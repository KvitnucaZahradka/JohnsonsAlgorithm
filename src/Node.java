import java.util.LinkedList;
import java.util.List;

/**
 * Created by Pipjak on 26/12/2016.
 */
public class Node<T,V> implements InterNode<T>{

    /* FIELDS */
    private Boolean processed;
    private Integer nodeID;
    private Integer distance;
    private List<T> prevEdgeIDs;
    private List<T> nextEdgeIDs;
    private List<T> next;
    private List<T> prev;
    private V val;

    /* CONSTRUCTOR */
    public Node(){
        this.processed = false;
        this.distance = Integer.MAX_VALUE;
        this.prevEdgeIDs = new LinkedList<T>();
        this.nextEdgeIDs = new LinkedList<T>();
        this.next = new LinkedList<T>();
        this.prev = new LinkedList<T>();
    }

    @Override
    public boolean equals(Object o){
        if ((o instanceof Node) && (((Node) o).getNodeID().equals(this.nodeID))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode(){
        return 31;
    }


    /* METHODS */

    /* reset processed */
    public void reSetProcessed(){
        this.processed = false;
    }

    /* set processed */
    public void setProcessed(){
        this.processed = true;
    }

    /* is processed ? */
    public Boolean isProcessed(){
        return this.processed;
    }

    /* set up node ID */
    public void setUpNodeID(int nodeID){
        this.nodeID = nodeID;
    }

    /* get node ID */
    public Integer getNodeID(){
        return this.nodeID;
    }

    /* set up the distance */
    public void setUpDistance(int distance){
        this.distance = distance;
    }

    /* get distance */
    public Integer getDistance(){
        return this.distance;
    }

    public void setNextEdgeID(T nextEdgeID){
        this.nextEdgeIDs.add(nextEdgeID);
    }

    public List<T> getNextEdgeID(){
        return this.nextEdgeIDs;
    }

    public void setPrevEdgeID(T prevEdgeID){
        this.prevEdgeIDs.add(prevEdgeID);
    }

    public List<T> getPrevEdgeID(){
        return this.prevEdgeIDs;
    }

    public void setNextNodeID(T next){
        this.next.add(next);
    }

    public void setPrevNodeID(T prev){
        this.prev.add(prev);
    }


    public List<T> getNextNodeID(){
        return this.next;
    }

    public List<T> getPrevNodeID(){
        return this.prev;
    }

    public void setVal(V value){
        this.val = value;
    }

    public V getVal(){
        return this.val;
    }


}
