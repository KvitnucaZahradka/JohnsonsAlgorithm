/**
 * Created by Pipjak on 26/12/2016.
 */
public interface InterEdge<T,V> {
    /* Setting the value of edge */
    void setEdgeValue(V val);

    /* Setting the tail */
    void setEdgeStop(T tail);

    /* Setting head */
    void setEdgeStart(T head);

    /* Getting the value of edge */
    V getEdgeValue();

    /* Get tail */
    T getEdgeStop();

    /* Get head */
    T getEdgeStart();

}
