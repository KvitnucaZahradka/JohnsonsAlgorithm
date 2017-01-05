import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by Pipjak on 26/12/2016.
 */
public class Graph{

    /* FIELDS */

    /* custom static comparator for priority queue */
    static Comparator CUST = (Comparator<Node>) (Node o1, Node o2) -> {
        Integer a = o1.getDistance();
        Integer b = o2.getDistance();

        return a.compareTo(b);
    };

    /* static fields */
    private static Integer EDGEiD = 0;
    private static String FILE;

    /* dynamic fields */
    final private Edge<Integer,Integer>[] edges;
    final private Node<Integer,Integer>[] nodes;

    /* priority queue for Dijkstra's alg. */
    public Queue<Node<Integer,Integer>> priority;

    /* CONSTRUCTOR */

    /* nVert = number of original vertices, i.e. not a fake vertex */
    public Graph(Integer nVert, Integer nEdge){

        /* each vertex has also a fake edge
        *  those edges are added after regular edges
        * */
        this.edges = new Edge[nEdge+nVert];

        /* last vertex is fake */
        this.nodes = new Node[nVert+1];

        /* create instances of Node class */
        for(int i = 0; i<this.nodes.length; i++) {
            this.nodes[i] = new Node<>();
        }

        /* initialize empty priority queue with min based custom comparator */
        this.priority = new PriorityQueue<>(nVert+100,Graph.CUST);
    }


    /* METHODS */

    /* helpful methods */

    public void printGraph(int numberOfNodes){
        List<Integer> nodeIdList, edgeIdList;
        Edge ed;

        for(int i = 0; i<numberOfNodes;i++){
            System.out.println("---- new ----");
            System.out.println("node " + i + " has the following next nodes with edge weights ");
            nodeIdList = this.nodes[i].getNextNodeID();
            edgeIdList = this.nodes[i].getNextEdgeID();

            System.out.println("Printing edges: ");

            for(Integer edgeId: edgeIdList){

                ed = this.edges[edgeId];
                System.out.println(" start node : " + ((Integer) ed.getEdgeStart() + 1));
                System.out.println(" stop node : " + ((Integer)ed.getEdgeStop()+ 1));
                System.out.println(" weight : " + ed.getEdgeValue());
                System.out.println("");

            }

            System.out.println("");

            for(Integer nodeId: nodeIdList){
                System.out.println("Next node id is : " + nodeId);
            }
        }
    }

    public void reSetProcessed(int nodeID){
        this.nodes[nodeID].reSetProcessed();
    }


    public Node<Integer,Integer> getNode(int nodeId){
        return this.nodes[nodeId];
    }

    public Edge<Integer,Integer> getEdge(int edgeId){
        return  this.edges[edgeId];
    }

    /* helpful methods for Dijkstra's algorithm */

    public void setDistance(Integer vertex, Integer value){
        this.nodes[vertex].setUpDistance(value);
    }

    public void addToPriorityQ(Node node){
        this.priority.add(node);
    }

    /* reading methods */

    /* static method that reads in parameters of graph */
    public static Integer[] readInGraphSize (String baseFile) throws IOException{
        Graph.FILE = baseFile;
        Integer[] result = new Integer[2];
        String line;
        String[] arr;

        try{
            File file = new File(Graph.FILE);
            FileReader fileReader = new FileReader(file);

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuffer stringBuffer = new StringBuffer();

            /* read only first line */
            line = bufferedReader.readLine();
            arr = line.split("\\s+");

            /* fill up the result 0==nVert, 1==nEdges */
            result[0] = Integer.parseInt(arr[0]);
            result[1] = Integer.parseInt(arr[1]);

        }
        catch (IOException e){
            e.printStackTrace();
        }

        return result;
    }


    /* read in the graph structure */
    public void readInGraph() throws IOException{
        String line;
        int idA,idB,weight;
        String[] arr;

        try{
            File file = new File(Graph.FILE);
            FileReader fileReader = new FileReader(file);

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuffer stringBuffer = new StringBuffer();

            /* skip one line */
            line = bufferedReader.readLine();

            /* read next lines */
            while ((line = bufferedReader.readLine()) != null ) {

                arr = line.split("\\s+");

                /* IDs for Node A, Node B and weight are */
                idA = Integer.parseInt(arr[0])-1;
                idB = Integer.parseInt(arr[1])-1;
                weight = Integer.parseInt(arr[2]);

                /* setting up values */
                this.setValues(idA, idB, weight);
            }

            // closing the fileReader:
            fileReader.close();

            /* add the fake edges */
            this.setFakeEdges();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /* set up fake edges. The fake node is the last nodes[nVert], where nVert==nodes.length-1 */
    private void setFakeEdges(){
        int idA = this.nodes.length-1;

        /* loop through all but the last node, that is fake one and set up the edges */
        for(int idB = 0; idB<idA; idB++){
            this.setValues(idA,idB,0);
        }
    }

    /* set values: remember edge defined as:  idA --(Val, edgeID)-- > idB */
    private void setValues(int idA, int idB, int weight){

        /* setting up nodes */
        this.nodes[idA].setUpNodeID(idA);
        this.nodes[idB].setUpNodeID(idB);

        this.nodes[idA].setNextNodeID(idB);
        this.nodes[idB].setPrevNodeID(idA);

        this.nodes[idA].setNextEdgeID(Graph.EDGEiD);
        this.nodes[idB].setPrevEdgeID(Graph.EDGEiD);

        /* setting up the edge */
        this.edges[Graph.EDGEiD] = new Edge<>();
        this.edges[Graph.EDGEiD].setEdgeStart(idA);
        this.edges[Graph.EDGEiD].setEdgeStop(idB);
        this.edges[Graph.EDGEiD++].setEdgeValue(weight);

    }

}
