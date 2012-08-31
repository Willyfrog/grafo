package org.gvaya.ssii.dcel

import collection.mutable.ArrayBuffer
import org.gvaya.ssii.grafo.{Vertice, Arista, Nodo, Grafo}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType

/**
 * User: gvaya
 * Date: 28/05/12
 * Time: 23:30
 */

class DcelConstructor(val g: Grafo) {
  var lvertices: ArrayBuffer[Vertex] = new ArrayBuffer[Vertex]()
  var laristas: ArrayBuffer[Edge] = new ArrayBuffer[Edge]()
  var lcaras: ArrayBuffer[Face] = new ArrayBuffer[Face]()

  def addVertex(v: Vertice) {
    addVertex(new Vertex(v.etiqueta, v.x, v.y))
  }

  def addVertex(v: Vertex) {
    lvertices.append(v)
  }

  def addArista(arista: Arista) {
    val v1: Vertex = findVertexByCoords(arista.origen.x, arista.origen.y).get
    val v2: Vertex = findVertexByCoords(arista.destino.x, arista.destino.y).get
    val nodos = arista.nodos
    val e1: Edge = addEdge(v1, v2, nodos)
    val e2: Edge = addEdge(v2, v1, nodos.reverse)
    e1.inversa = e2
    e2.inversa = e1
  }

  def addEdge(v1: Vertex, v2: Vertex, nodos: Array[Nodo]): Edge = {
    if (!lvertices.contains(v1))
      addVertex(v1)
    if (!lvertices.contains(v2))
      addVertex(v2)
    val e: Edge = new Edge(v1.label + "_" + v2.label, v1, v2, nodos)
    laristas.append(e)
    e
  }

  //Como encontrar un Vertex
  def findVertexByCoords(x: Float, y: Float): Option[Vertex] = lvertices.find(_.sonCoordenadas(x, y))

  //pregunta a cada elto. por sus coordenadas
  def findVertexByLabel(lbl: String): Option[Vertex] = lvertices.find(_.label == lbl)

  //Como encontrar un Edge
  def findEdgeFromVertex(v: Vertex): Edge = laristas.find(_.parteDe(v)).get

  def findEdgeToVertex(v: Vertex): Edge = laristas.find(_.llegaA(v)).get

  def addFace(f: Face) {
    lcaras.append(f)
  }

  def generar() {
    for (v <- g.vertices)
      addVertex(v)
    for (a <- g.aristas)
      addArista(a)
  }

  def drawIntoShapeRenderer(shape: ShapeRenderer) {
    shape.begin(ShapeType.Line)
    shape.setColor(0f, 1f, 0.9f, 1f)
    laristas.foreach(_.drawIntoShapeRenderer(shape))
    shape.end()
    shape.begin(ShapeType.Circle)
    shape.setColor(1f, 0f, 0.9f, 1f)
    lvertices.foreach(_.drawIntoShapeRenderer(shape))
    shape.end()
  }
}
