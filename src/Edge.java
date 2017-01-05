/**
 * Created by Pipjak on 26/12/2016.
 */
public class Edge<T,V> implements InterEdge<T,V>{
    /* FIELDS */
    private V edgeValue;
    private T tail;
    private T head;

    /* CONSTRUCTOR */
    public Edge(){}

    /* METHODS */

    /* setters */
    public void setEdgeValue(V edgValue){
        this.edgeValue = edgValue;
    }

    public void setEdgeStop(T tail){
        this.tail = tail;
    }

    public void setEdgeStart(T head){
        this.head = head;
    }

    /* getters */
    public V getEdgeValue(){
        return this.edgeValue;
    }

    public T getEdgeStop(){
        return this.tail;
    }

    public T getEdgeStart(){
        return this.head;
    }

}
