import java.util.List;

/**
 * Created by Pipjak on 26/12/2016.
 */
public interface InterNode<T> {

    /* method sets the id's of edges coming from this node */
    void setPrevEdgeID(T edgeID);

    List<T> getPrevEdgeID();

    /* method sets next node ID */
    void setNextNodeID(T next);

    /* method sets previous node ID */
    void setPrevNodeID(T prev);

    /* get the next nodes List IDs */
    List<T> getNextNodeID();

    /* get previous nodes list of IDs */
    List<T> getPrevNodeID();
}
