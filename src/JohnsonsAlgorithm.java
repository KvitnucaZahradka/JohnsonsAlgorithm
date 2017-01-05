import org.jetbrains.annotations.Contract;

import java.io.IOException;
import java.util.*;

/**
 * Created by Pipjak on 25/12/2016.
 */
public class JohnsonsAlgorithm {

    /* FIELDS */
    final private Integer originalNumberOfVertices;
    final private Integer extendedNumberOfVertices;
    private Graph graph;
    private Integer[][] a;
    private Map<Integer, Node>[] previous;
    private Queue<Dist> distances;

    /* CONSTRUCTOR */
    public JohnsonsAlgorithm() throws IOException{
        Integer[] params = Graph.readInGraphSize("g3.txt");

        this.graph = new Graph(params[0], params[1]);
        this.graph.readInGraph();

        /* print graph to see whether it was produced correctly */
       // this.graph.printGraph(3);

        /* create field a[][] and initialize
        *  remember: the last column is for the one extra run to detect negative cycle
        *  negative cycle, if a[n-1][v] == a[n][v] for all v \in V
        * */

        /* params[0] = real number of vertices */
        this.originalNumberOfVertices = params[0];
        this.extendedNumberOfVertices = this.originalNumberOfVertices + 1;

        /* a has to be bigger by one in one direction
        * this.a[0 ... params[0]-1][0 ... params[0]]
        *
        * this.a[x][] --- first index is index corresponds to number of edges of the path 0 --- params[0]-1, with a fake node its
        * 0 --- params[0], with one more loop ( to detect negative cost cycle ) it is 0 --- params[0] + 1
        *
        * this.a[][x] --- second index is index of a node, number of nodes is params[0] + 1 ( including the fake index )
        *
        * */

        /* set up for Bellman-Ford algorithm subroutine */
        this.a = new Integer[params[0]+2][params[0]+1];
        this.initializeA();

        /* set up priority queue for Dijkstra's algorithm subroutine */
        this.distances = new PriorityQueue<>();

        /* set up previous */
        this.previous = new HashMap[this.originalNumberOfVertices];
        for(int i = 0; i<this.originalNumberOfVertices;i++)
            this.previous[i] = new HashMap<>();
    }

    /* PRIVATE CLASSES */
    private class Dist implements Comparable<Dist> {
        /* fields */
        private Integer distance;
        private Integer[] idAidB;

        /* constructor */
        public Dist(int idA, int idB, int realDistance){
            this.distance = realDistance;

            this.idAidB = new Integer[2];
            this.idAidB[0] = idA;
            this.idAidB[1] = idB;
        }

        /* methods */
        private Integer getDistance(){
            return this.distance;
        }

        /* print out the minimal distance after run of nVert times of Dijkstra's algorithm */
        private void print(Optional<Integer> opt){
            if(opt.isPresent()){
                if(opt.get()==1){
                    System.out.println("There is no negative loop cycle and minimal distance is " + this.distance);
                    System.out.println("The distance is in between nodes " + (this.idAidB[0]+1) + " and "
                            + (this.idAidB[1]+1));
                }
                else{
                    System.out.println("There is no negative loop cycle and " + opt.get() + "\'th" +
                            " minimal distance is " + this.distance);
                    System.out.println("The distance is in between nodes " + (this.idAidB[0]+1) + " and "
                            + (this.idAidB[1]+1));
                }
            }
            else{
                System.out.println("The order of minimum has not been supplied, please supply it to print function");
                System.out.println("Therefore I assume that polled value was first minimum ");

                System.out.println("There is no negative loop cycle and minimal distance is " + this.distance);
                System.out.println("The distance is in between nodes " + (this.idAidB[0]+1) + " and "
                        + (this.idAidB[1]+1));
            }
        }

        private void printPath(){
            Integer start, stop;

            start = this.idAidB[1];

            Map<?,Node> temp = previous[this.idAidB[0]];

            stop = temp.get(start).getNodeID();

            assert !temp.isEmpty(): "Map is empty ";

            System.out.println("Printing path: ");

            while(true){
                assert stop!=null: "stop is null where start was " + start;

                System.out.println("from " + (start+1) + " to " + (stop+1));

                if(stop.equals(this.idAidB[0])){
                    break;
                }
                start = stop;
                stop = temp.get(start).getNodeID();
            }
        }

        /* compare to method */
        @Override
        public int compareTo(Dist o) {
            return this.distance.compareTo(o.getDistance());
        }

    }


    /* METHODS */

    /* Bellman-Ford algorithm helpful methods */

    /* initial values for Bellman-Ford algorithm */
    private void initializeA(){
        this.a[0][this.extendedNumberOfVertices-1] = 0;

        for(int i = 0; i<this.originalNumberOfVertices; i++){
            this.a[0][i] = Integer.MAX_VALUE;
        }
    }

    /* run extended (to find negative cycle loop) Bellman-Ford Algorithm */
    public void runBellmanFord(){

        /* looping through the number of edges i = 1, 2, 3, ... n: since the last node is fake loop for Neg. cycle det. */
        for(int i = 1; i<=this.extendedNumberOfVertices; i++){

            /* looping through all nodes i.e. all vertices ( including the fake vertex ) */
            for(int nodeID = 0; nodeID<this.extendedNumberOfVertices; nodeID++) {
                this.a[i][nodeID] = Math.min(this.a[i - 1][nodeID], this.min(i, nodeID));
            }
        }
    }

    @Contract(pure = true)
    private boolean checkNoNegativeLoop(){
        boolean result = true;

        for(int nodeID = 0; nodeID<this.extendedNumberOfVertices; nodeID++){
           result = result && this.a[this.extendedNumberOfVertices-1][nodeID].
                   equals(this.a[this.extendedNumberOfVertices][nodeID]);
        }
        return result;
    }

    /* min() function */
    private int min(int i, int v){
        int tempMin;
        int min = Integer.MAX_VALUE;
        Integer edgeId;

        /* iterator through the edge ids */
        Iterator iter = this.graph.getNode(v).getPrevEdgeID().iterator();

        while(iter.hasNext()){
            edgeId = (Integer) iter.next();

            /* be careful if you are dealing with positive infinity */
            if(this.a[i-1][this.graph.getEdge(edgeId).getEdgeStart()].equals(Integer.MAX_VALUE)){
                tempMin = Integer.MAX_VALUE;
            }
            else{
                tempMin = this.a[i-1][this.graph.getEdge(edgeId).getEdgeStart()]
                        + this.graph.getEdge(edgeId).getEdgeValue();
            }

            if(tempMin<min)
                min = tempMin;
        }
        return min;
    }




    /* Dijkstra's algorithm with shifted
    * weights weight_Edge' = weight_Edge + this.a[extNumVert-1][idA] - this.a[extNumVert-1][idB]
    * */

    /* set up initial distances from a startNode and set status of processed to be false */
    private void setInitialDistances(int startNodeID){

        /* seed the original vertices to have infinite values */
        for(int i = 0; i<this.originalNumberOfVertices; i++){
            if(i==startNodeID){
                this.graph.setDistance(startNodeID,0);
            }
            else{
                this.graph.setDistance(i,Integer.MAX_VALUE);
            }

            /* reset the node to not be processed */
            this.graph.reSetProcessed(i);
        }
    }


    /* main dijkstra's algorithm */
    public void runDijkstra(int startNodeID){
        Node tempNode, startNode;
        Integer idA, idB;

        /* reset node values / set up initial values for vertices */
        this.setInitialDistances(startNodeID);

        /* seed the priority queue */
        assert this.graph.priority.isEmpty() : "priority queue is not empty, something went wrong";
        this.graph.addToPriorityQ(this.graph.getNode(startNodeID));

        /* loop while priority is not empty */
        while(!this.graph.priority.isEmpty()){
            /* pick the head of a min based priority queue */
            tempNode = this.graph.priority.poll();
            startNode = this.graph.getNode(startNodeID);
            idA = startNode.getNodeID();
            idB = tempNode.getNodeID();

            assert idA!=null: "null idA";
            int realDistance = tempNode.getDistance() - this.a[this.originalNumberOfVertices][idA] +
                    this.a[this.originalNumberOfVertices][idB];

            /* after picking up the head of priority queue it is obvious that the distance hold by that node
            * is a smallest distance to the original startNode
            * */
            this.distances.add(new Dist(idA,idB, realDistance));

            /* calculate temp distances */
            this.calcTempDistances(tempNode,startNodeID);

            /* set tempNode to be processed */
            tempNode.setProcessed();
        }
    }


    /* calculate temporary distance */
    private void calcTempDistances(Node node, Integer startNodeID){
        Iterator iter = node.getNextEdgeID().iterator();
        Edge tempEdge;
        Node tempNode;
        Integer idA, idB;
        Integer tempDistance, shiftedEdgeWeight;
        Integer nextEdgeID;

        while(iter.hasNext()){

            nextEdgeID = (Integer) iter.next();
            tempEdge = this.graph.getEdge(nextEdgeID);

            idA = (Integer) tempEdge.getEdgeStart();
            idB = (Integer) tempEdge.getEdgeStop();

            assert idA.equals(node.getNodeID()): "idA is not equal nodeID, something is wrong! ";

            /* this is node corresponding to end of the edge */
            tempNode = this.graph.getNode(idB);

            /* this is important, here you are shifting edge weights, that might be negative, to be strictly positive
            *  this is essential to Johnson's algorithm
            * */
            shiftedEdgeWeight = (Integer) tempEdge.getEdgeValue()
                    - this.a[this.originalNumberOfVertices][idB] + this.a[this.originalNumberOfVertices][idA];

            tempDistance = node.getDistance() + shiftedEdgeWeight;

            if(shiftedEdgeWeight<0){
                System.out.println("shifted weight is " + shiftedEdgeWeight);
                System.out.println("from " + (idA+1) + " to " + (idB+1) + " with edge value " + tempEdge.getEdgeValue());
                System.out.println("the start node shift is " + this.a[this.originalNumberOfVertices][idA]);
                System.out.println("the stop node shift is " + (this.a[this.originalNumberOfVertices][idB]));
            }
            assert shiftedEdgeWeight>=0:"distance is not positive something went wrong check on ";
            /* remove from priority queue and update distance BUT only if it has not been processed yet */
            if(tempDistance<tempNode.getDistance() && !tempNode.isProcessed()){

                /* remove from min-based priority queue */
                this.graph.priority.remove(tempNode);

                /* change temp distance in tempNode */
                tempNode.setUpDistance(tempDistance);

                /* reinsert back to priority queue */
                this.graph.priority.add(tempNode);

                /* set node == node that is picked out of queue, to be previous to all other nodes
                * hash map: maps the nodeID --> previous node
                * previous node is "node"
                * */
                this.previous[startNodeID].put(idB, node);
            }
        }
    }

    /* FINAL PART: JOHNSON's ALGORITHM */
    public void johnson(){
        long start, estimatedTime;

        /* STEP I: run Bellman-Ford algorithm once */
        System.out.println("STARTing: Bellman-Ford algorithm ");
        start = System.currentTimeMillis();

        /* run Bellman-Ford algorithm */
        this.runBellmanFord();

        estimatedTime = System.currentTimeMillis() - start;
        System.out.println("elapsed time (in millsec) in Bellman-Ford algorithm " + estimatedTime);
        //System.out.println("distance is " + this.a[0][idA]);


        /* STEP II: check negative loop */
        if(!this.checkNoNegativeLoop()){
            System.out.println("There is negative loop cycle");
        }
        else{
            int mod = 75;
            /* STEP III:  for all nodes run Dijkstra's algorithm */
            for(int i = 0; i<this.originalNumberOfVertices;i++){
                if(i%mod==0) {
                    System.out.println("STARTing: Dijkstra's on " + i + "-th vertex");


                start = System.currentTimeMillis();
                }

                /* run Dijkstra's algorithm */
                this.runDijkstra(i);

                if(i%mod==0) {
                    estimatedTime = System.currentTimeMillis() - start;
                    System.out.println("elapsed time (in millsec) in one run of Dijkstra's algorithm " + estimatedTime);
                }

            }

            /* STEP IV: pull out the minimum and print it */
            this.distances.peek().print(Optional.empty());
            this.distances.poll().printPath();

            /* OPT.STEP: print second shortest distance */
            /*
            this.distances.poll().print(Optional.of(2));
            this.distances.poll().print(Optional.of(3));
            */
        }
    }

    public static void main(String[] args) throws IOException {

        JohnsonsAlgorithm johny = new JohnsonsAlgorithm();
        johny.johnson();


        /*
        LinkedList<Integer> lst = new LinkedList<>();
        lst.add(1);
        lst.add(2);
        lst.add(3);
        Iterator iter = lst.iterator();
        Integer in;
        while(iter.hasNext()){
            in = (Integer) iter.next();
            System.out.println("1: in is " + in);
            System.out.println("2: in is " + in);
        }
        */
    }
}
