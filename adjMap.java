package graphDir;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;
/*
 * This is an adjacency map class. Each vertex will have one. This adjancency
 * map is represented by a treemap with key values being the adjacent vertex and
 * the value being integer weights representing the edges.
 */

public class adjMap<V extends Comparable<V>> {

    private TreeMap<V, Integer> adjMap;

    @Override
    public String toString() {
        String toReturn = "";
        for (V vertex : adjMap.keySet()) {
            // go through adjmap of vertex in graphdir
            toReturn += "connected to: "+ vertex.toString() + 
                    ", with weight: " + adjMap.get(vertex) + "\n";
        }
        return toReturn;
    }
    
    // constructor : V will be the vertex and integer will be the weight
    public adjMap() {
        adjMap = new TreeMap<V, Integer>();
    }

    public boolean edgeAdd(V vertexTo, int weight) {
        // this should update weight if vertex is already in the adjMap
        // or should add an edge
        adjMap.put(vertexTo, weight);
        return true;
    }
    
    public int edgeGet(V vertexTo) {
        int retVal = -1;
        //if the adjmap has vertexTo in it's keyset, it'll return the value
        // which in this case is an integer representing the weight of the edge
        if (adjMap.containsKey(vertexTo)) {
            retVal = adjMap.get(vertexTo);
        }
        return retVal;
    }
    
    public boolean edgeRemove(V vertexTo) {
        boolean b = false;
        //by removing the key, we also remove the value
        if (adjMap.containsKey(vertexTo)) {
            adjMap.remove(vertexTo);
            b = true;
        }
        return b;
    }
    // goes through the map and adds it to an arraylist which is part of the 
    // collections hierarchy
    public Collection<V> vertexNeighbors(V vertexInfo) {
        ArrayList<V> toReturn = new ArrayList<V>();
        for (V V : adjMap.keySet()) {
            toReturn.add(V);
        }
        return toReturn;
    }
    
    //checks if vertex is in the adjacency map
    public boolean edgeChecker(V vertex) {
        return adjMap.containsKey(vertex);
    }
    
    //only adds outgoing edges
    public adjMap<V> combineOutAdjMap(adjMap<V> v1, adjMap<V> v2,
            V vertex1, V vertex2){
        //add edges from v1 that aren't in v2 and isn't one of the ones we're
        //combining
        for(V V : v1.adjMap.keySet()) {
            if (!v2.adjMap.containsKey(V) && V.compareTo(vertex2) != 0) {
                adjMap.put(V, v1.adjMap.get(V));
            }
        }
        //add edges from v2 that aren't in v1 and isn't one of the ones we're
        //combining
        for(V V : v2.adjMap.keySet()) {
            if (!v1.adjMap.containsKey(V) && V.compareTo(vertex1) != 0) {
                adjMap.put(V, v2.adjMap.get(V));
            }
        }
        //add the ones that are in both but with the smallest weight
        for(V V : v1.adjMap.keySet()) {
            if (v2.adjMap.containsKey(V) && V.compareTo(vertex1) != 0
                    && V.compareTo(vertex2) != 0) {
                int toPut = v1.adjMap.get(V) < v2.adjMap.get(V)? 
                        v1.adjMap.get(V): v2.adjMap.get(V);
                adjMap.put(V, toPut);
            }
        }
        return this;
    }
    

}
