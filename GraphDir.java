package graphDir;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Collection;
/*
 * This class represents a directed graph. A directed graph will have vertices
 * as well as edges with weights to other vertices. A graph will be represented
 * by a TreeMap, with key V, which will be the vertex's data, and the value
 * of an adjMap object, which is defined in the adjMap class.
 */

public class GraphDir<V extends Comparable<V>> {
  // graph is a treeMap with key holding vertex data, and adjMap holding the 
  //adjacent vertices with weights
  private TreeMap<V, adjMap<V>> graph = new TreeMap<V, adjMap<V>>();
 
  @Override
  public String toString() {
      String toReturn = "";
      // goes through vertexes in graph
      for (V vertex : graph.keySet()) {
          toReturn += vertex.toString() + " is " + graph.get(vertex).toString();
      }
      return toReturn;
  }
  
  //adds a vertex with key vertexInfo. If the key already exists, returns false
  public boolean vertexAdd(V vertexInfo) {
      if (vertexInfo == null) {
          throw new IllegalArgumentException();
      }
      if (graph.containsKey(vertexInfo)) {
          return false;
      } else {
          graph.put(vertexInfo, new adjMap<V>());
          return true;
      }
  }
  
  //checks if the graph has the vertex vertexInfo
  public boolean vertexCheck(V vertexInfo) {
      if (vertexInfo == null) {
          throw new IllegalArgumentException();
      }
      
      return graph.containsKey(vertexInfo);
  }

  public Collection<V> getVertices() {
    // Arraylist implements list which is under the collections hierarchy
    ArrayList<V> returnCollect = new ArrayList<V>();
    
    for(V V : graph.keySet()) { //adding all the vertices to the arraylist
        returnCollect.add(V);
    }
    return returnCollect;
  }

  public boolean edgeAdd(V vertexFrom, V vertexTo, int weight) {
      if (vertexFrom == null || vertexTo == null) {
          throw new IllegalArgumentException();
      }
      if (weight < 0) {
          return false;
      }
      if (!graph.containsKey(vertexFrom)) {
          graph.put(vertexFrom, new adjMap<V>());
      }
      if (!graph.containsKey(vertexTo)) {
          graph.put(vertexTo, new adjMap<V>());
      }
      //adding vertex to and weight to vertexFrom's adjMap
      return graph.get(vertexFrom).edgeAdd(vertexTo, weight);
  }

  //returns the weight if there's an edge from vertex from to vertex to
  public int edgeGet(V vertexFrom, V vertexTo) {
      int retVal = -1;
      
      if (vertexFrom == null && vertexTo == null) {
          throw new IllegalArgumentException();
      }

      if (graph.containsKey(vertexFrom) && graph.containsKey(vertexTo)) {
          retVal = graph.get(vertexFrom).edgeGet(vertexTo);
      }
      return retVal;
  }
  
  //removes the edge from vertex from to vertex to by removing vertexTo
  //from vertexFrom's adjacency map
  public boolean edgeRemove(V vertexFrom, V vertexTo) {
      boolean b = false;

      if (vertexFrom == null && vertexTo == null) {
          throw new IllegalArgumentException();
      }

      if (graph.containsKey(vertexFrom) && graph.containsKey(vertexTo)) {
          if (this.edgeGet(vertexFrom, vertexTo) != -1) {
              b = graph.get(vertexFrom).edgeRemove(vertexTo);
          }
      }

      return b;
  }
  
  //deletes a vertex from the graph. It first removes all the edges that point
  //to vertexInfo and then removes the edge from vertexInfo to another vertex
  public boolean vertexDelete(V vertexInfo) {
      boolean b = false;
      
      if (vertexInfo == null) {
          throw new IllegalArgumentException();
      }
      
      if (graph.containsKey(vertexInfo)) {
      for (V vertex : graph.keySet()) {
          //removing all the edges that point to and from vertex info
          this.edgeRemove(vertex, vertexInfo);
          this.edgeRemove(vertexInfo, vertex);
      }
      //removing the vertex from the map
      graph.remove(vertexInfo);
      b = true;
      }
      return b;
  }
  
  //returns a collection of the neighbors to vertexInfo
  public Collection<V> vertexNeighbors(V vertexInfo) {
      //just returning 
      if (vertexInfo == null) {
          throw new IllegalArgumentException();
      }
      
      if (graph.containsKey(vertexInfo)) {
          return graph.get(vertexInfo).vertexNeighbors(vertexInfo);
      }
      
      return null;
  }
  
  //combines vertex1 and vertex2 if they're neighbors only. The smaller vertex
  // value will be the value of the combined vertex.
  public boolean edgeContract(V vertex1, V vertex2) {
      boolean toReturn = false;
      
      if (vertex1 == null || vertex2 == null) {
          throw new IllegalArgumentException();
      }
      
      if (!graph.containsKey(vertex1) || !graph.containsKey(vertex2)) {
          return toReturn;
      }
      
      if (!graph.get(vertex1).edgeChecker(vertex2) && 
              !graph.get(vertex2).edgeChecker(vertex1)) {
          return toReturn;
      }
      
      if (vertex1.compareTo(vertex2) == 0 &&
              this.edgeGet(vertex1, vertex2) == -1) {
          return toReturn;
      } 
      
      //returning the smaller vertex
      V combinedVertex = this.whichIsSmaller(vertex1, vertex2);
      
      //getting the adjacency map of vertex1 and vertex2 and combining them
      adjMap<V> combinedOutAdjMap = this.combineOutAdjMap(vertex1, vertex2);
      
      //getting the adjacency map of all vertexes with edges that point to 
      //vertex1 and vertex 2
      TreeMap<V, Integer> incomingAdjMap = this.incomingMap(vertex1, vertex2);
      
      //getting map if vertex1 and vertex2 have edges to themselves
      TreeMap<V,Integer> selfEdgeMap = this.selfEdgeMap(vertex1, vertex2);
      
      // deleting vertexes after we stored which vertexes pointed to and from
      //vertex 1 and 2
      this.vertexDelete(vertex1);
      this.vertexDelete(vertex2);
      
      //adding the combined vertex and it's outgoing adjacency map
      this.graph.put(combinedVertex, combinedOutAdjMap);
      
      //adding the incoming adjacency map to vertexes that once had edges to
      // either vertex 1 or vertex 2
      for(V V : incomingAdjMap.keySet()) {
          if (V.compareTo(combinedVertex) != 0) {
              this.edgeAdd(V, combinedVertex, incomingAdjMap.get(V));
          }
      }
      
      //adding the self vertex edge (vertex 1 and/or vertex 2 had an edge that 
      //pointed to itself)
      for(V V : selfEdgeMap.keySet()) {
          this.edgeAdd(V, combinedVertex, selfEdgeMap.get(V));
      }
      
      toReturn = true;
      return toReturn;
  }
  
  private adjMap<V> combineOutAdjMap(V vertex1, V vertex2) {
      //only does outgoing vertices
      adjMap<V> toReturn = new adjMap<V>();
      toReturn.combineOutAdjMap(graph.get(vertex1), graph.get(vertex2),
              vertex1, vertex2);
      return toReturn;
  }
  
  //returns which vertex is smaller with a ternary operator
  private V whichIsSmaller(V vertex1, V vertex2) {
      V toReturn;
      toReturn = vertex1.compareTo(vertex2) < 0 ? vertex1: vertex2;
      return toReturn;
  }
  
  //returns a treemap of vertexes with vertex1 and/or vertex2 in it's adjacency
  //map
  private TreeMap<V, Integer> incomingMap(V vertex1, V vertex2) {
      TreeMap<V, Integer> toReturn = new TreeMap<V, Integer>();
      
      for (V vertex : graph.keySet()) {
          if (vertex.compareTo(vertex1) != 0 && vertex.compareTo(vertex2) != 0) {
              // vertices that point to vertex 1 only 
              if (graph.get(vertex).edgeChecker(vertex1) && 
                      !graph.get(vertex).edgeChecker(vertex2)) {
                  toReturn.put(vertex, this.edgeGet(vertex, vertex1));
              }
              
              // vertices that point to vertex 2 only 
              if (graph.get(vertex).edgeChecker(vertex2) && 
                      !graph.get(vertex).edgeChecker(vertex1)) {
                  toReturn.put(vertex, this.edgeGet(vertex, vertex2));
              }
              
              // vertices that point to both vertex 1 and vertex 2
              if (graph.get(vertex).edgeChecker(vertex1) && 
                      graph.get(vertex).edgeChecker(vertex2)) {
                  // ternary operator getting the smaller weight
                  int smaller = this.edgeGet(vertex, vertex1) < 
                       this.edgeGet(vertex, vertex2) ? 
                            this.edgeGet(vertex, vertex1): 
                                this.edgeGet(vertex, vertex2);

                  toReturn.put(vertex, smaller);
              }
          }
      }
      return toReturn;
  }

  private TreeMap<V, Integer> selfEdgeMap(V vertex1, V vertex2) {
      //treemap of self directed vertexes
      TreeMap<V, Integer> toReturn = new TreeMap<V, Integer>();
      int v1Self = this.edgeGet(vertex1, vertex1);
      int v2Self = this.edgeGet(vertex2, vertex2);
      
      //if vertex 1 points to itself and vertex 2 points to itself
      if (v1Self != -1 && v2Self != -1) {
          int smaller = v1Self < v2Self ? v1Self : v2Self;
          toReturn.put(this.whichIsSmaller(vertex1, vertex2), smaller);
          
      } else {
          
          //vertex 1 only
          if (v1Self != -1) {
              toReturn.put(this.whichIsSmaller(vertex1, vertex2), v1Self);
          }
          
          //vertex 2 only
          if (v2Self != -1) {
              toReturn.put(this.whichIsSmaller(vertex1, vertex2), v2Self);
          }

      }
      return toReturn;
  }

}
