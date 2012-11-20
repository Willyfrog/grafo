package org.gvaya.ssii.dcel

import math.atan2
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import org.gvaya.ssii.grafo.{Util, Nodo}
import com.badlogic.gdx.Gdx

/**
 * Created with IntelliJ IDEA.
 * @author gvaya
 * @since 28/05/12
 */


/**
 * Clase de semiarista orientada, que forma parte de una cara. Las utilizadas para triangular, estan marcadas
 * @param label nombre, debe ser unico en el grafo ya que se usará como identificador
 * @param origin vertice de origen
 * @param destiny vertice de destino
 * @param nodos nodos internos, usados unicamente para dibujar
 * @param triangulacion indica si es una arista utilizada para la triangulación
 */
class Edge(val label: String, val origin: Vertex, val destiny: Vertex, val nodos: Array[Nodo], val triangulacion:Boolean) {
  var fizd: Face = null
  var fdcha: Face = null
  var ccwo: Edge = null
  //siguiente counterclockwise en origen
  var ccwd: Edge = null
  //siguiente counterclockwise en destino
  var inversa: Edge = null
  //var angle:Double = 0

  def this(label: String, origin: Vertex, destiny: Vertex, nodos: Array[Nodo])={
    this(label, origin, destiny, nodos, false)
  }

  def igual(other: Edge): Boolean = {
    (origin.label == other.origin.label && destiny.label == other.destiny.label)
  }

  def exitAngle(): Double = {
    var x = destiny.x
    var y = destiny.y
    if (nodos.length > 0)
    {
      //Gdx.app.log("Edge", "usamos el ultimo nodo %s".format(nodos.last.toString))
      x = nodos.last.x
      y = nodos.last.y
    }

    Util.modulo2pi(atan2(y-origin.y, x-origin.x))
  }

  def entryAngle(): Double = {
    inversa.exitAngle()
  }

  def parteDe(v: Vertex): Boolean = origin.label == v.label
  def origen:String = origin.label

  def llegaA(v: Vertex): Boolean = destiny.label == v.label
  def destino:String = destiny.label

  def tieneSiguiente: Boolean = ccwd == null
  def tieneAnterior: Boolean = ccwo == null

  def drawIntoShapeRenderer(shape: ShapeRenderer) {
    var ox = origin.x
    var oy = origin.y
    for (n <- nodos) {
      shape.line(ox, oy, n.x, n.y)
      ox = n.x
      oy = n.y
    }
    shape.line(ox, oy, destiny.x, destiny.y)
  }
}
