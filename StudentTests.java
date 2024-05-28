package tests;

import org.junit.*;

import graphDir.GraphDir;
import graphDir.adjMap;

import static org.junit.Assert.*;

public class StudentTests {

  @Test
  public void testVertexAdd1() {
      GraphDir<String> one = new GraphDir<String>();
      System.out.println(one.vertexAdd("Joe"));
      System.out.println(one.toString());
  }
  
  @Test
  public void testVertexAdd2() {
      GraphDir<String> one = new GraphDir<String>();
      System.out.println(one.vertexAdd("Joe"));
      System.out.println(one.vertexAdd("Joe"));
      System.out.println(one.vertexAdd("joe"));
      System.out.println(one.toString());
  }
  
  @Test
  public void testVertexAdd3() {
      GraphDir<String> one = new GraphDir<String>();
      try {
      System.out.println(one.vertexAdd(null));
      }catch (IllegalArgumentException e) {
          System.out.println(e.getMessage());
      }
  }
  
  @Test
  public void testVertexCheck() {
      GraphDir<String> one = new GraphDir<String>();
      one.vertexAdd("Joe");
      System.out.println(one.vertexCheck("Joe"));
      System.out.println(one.vertexCheck("Koe"));
  }
  
  @Test
  public void testToString() {
      GraphDir<String> one = new GraphDir<String>();
      one.vertexAdd("Joe");
      one.vertexAdd("Jo");
      one.vertexAdd("Vo");
      one.edgeAdd("Jo", "Joe", 0);
      one.edgeAdd("Joe", "Jo", 5);
      one.edgeAdd("Joe", "Vo", 7);
      System.out.println(one.toString());
  }
  
  @Test
  public void testEdgeAdd() {
      GraphDir<String> one = new GraphDir<String>();
      one.vertexAdd("Joe");
      one.vertexAdd("Jo");
      one.edgeAdd("Jo", "Joe", 0);
      System.out.println(one.toString());
      //updating the weight of the edge5
      one.edgeAdd("Jo", "Joe", 5);
      System.out.println(one.toString());
  }
  
  @Test
  public void testEdgeGet() {
      GraphDir<String> one = new GraphDir<String>();
      one.vertexAdd("Joe");
      one.vertexAdd("Jo");
      one.edgeAdd("Jo", "Joe", 0);
      System.out.println(one.edgeGet("Jo", "Joe"));
      System.out.println(one.edgeGet("Joe", "Jo"));
      System.out.println(one.edgeGet("Joe","Voe"));
  }
  
  @Test
  public void testEdgeRemove() {
      GraphDir<String> one = new GraphDir<String>();
      one.vertexAdd("Joe");
      one.vertexAdd("Jo");
      one.edgeAdd("Jo", "Joe", 0);
      one.edgeRemove("Jo", "Joe");
      System.out.println(one.edgeGet("Jo", "Joe")); //-1
      System.out.println(one.toString()); //making sure the graph vertex wasn't 
      //removed and only the adjMap vertex was
  }
  
  @Test
  public void testVertexDelete1(){
      GraphDir<String> one = new GraphDir<String>();
      one.vertexAdd("Joe");
      one.vertexDelete("Joe");
      System.out.println(one.toString());
  }
  @Test
  public void testVertexDelete2() {
      GraphDir<String> one = new GraphDir<String>();
      one.vertexAdd("Joe");
      one.vertexAdd("Koe");
      one.vertexAdd("Soe");
      one.edgeAdd("Joe", "Koe", 1);
      one.edgeAdd("Koe", "Joe", 2);
      one.edgeAdd("Koe", "Soe", 3);
      one.edgeAdd("Soe", "Koe", 4);
      one.edgeAdd("Soe", "Joe", 5);
      one.edgeAdd("Joe", "Soe", 6);
      System.out.println(one.toString());
      one.vertexDelete("Joe");
      System.out.println(one.toString()); 
  }
  @Test
  public void testEdgeContract() {
      GraphDir<String> one = new GraphDir<String>();
      one.vertexAdd("Joe");
      one.vertexAdd("Koe");

      one.edgeAdd("Joe", "Koe", 1);

      one.edgeContract("Joe", "Koe");
      System.out.println(one.toString());
  }
  @Test
  public void testEdgeContract2() {
      GraphDir<String> one = new GraphDir<String>();
      one.vertexAdd("Joe");
      one.vertexAdd("Koe");
      one.vertexAdd("Soe");
      one.vertexAdd("Toe");
      one.edgeAdd("Joe", "Koe", 1);
      //edge pointing to itself
      one.edgeAdd("Joe", "Joe", 1);
      one.edgeAdd("Koe", "Joe", 2);
      one.edgeAdd("Koe", "Soe", 3);
      one.edgeAdd("Soe", "Koe", 4);
      one.edgeAdd("Soe", "Joe", 5);
      one.edgeAdd("Joe", "Soe", 6);
      one.edgeAdd("Soe", "Toe", 8);
      one.edgeAdd("Toe", "Koe", 3);
      one.edgeContract("Joe", "Koe");
      System.out.println(one.toString());
      one.edgeContract("Soe", "Joe");
      System.out.println(one.toString());
  }
}
