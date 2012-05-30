package dcel

import collection.mutable.ArrayBuffer
import grafo.Arista

/**
 * Created with IntelliJ IDEA.
 * User: gvaya
 * Date: 28/05/12
 * Time: 23:30
 * To change this template use File | Settings | File Templates.
 */

class Dcel {
  var lvertices:ArrayBuffer[Vertex] = new ArrayBuffer[Vertex]()
  var laristas:ArrayBuffer[Edge] = new ArrayBuffer[Edge]()
  var lcaras:ArrayBuffer[Face] = new ArrayBuffer[Face]()

  def addVertex(v:Vertex){
    lvertices.append(v)
  }
  def addEdgeDoble(v1:Vertex, v2:Vertex){
    addEdge(v1,v2)
    addEdge(v2,v1)
  }
  def addEdge(v1:Vertex, v2:Vertex){
    if (!lvertices.contains(v1))
      addVertex(v1)
    if (!lvertices.contains(v2))
      addVertex(v2)
    laristas.append(new Edge(v1.label + "_" + v2.label, v1,v2))
  }
  def addFace(f:Face)
  {
    lcaras.append(f)
  }

}
