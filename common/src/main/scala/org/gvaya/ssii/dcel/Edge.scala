package org.gvaya.ssii.dcel

import math.atan2
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import org.gvaya.ssii.grafo.Nodo

/**
 * Created with IntelliJ IDEA.
 * @author gvaya
 * @since 28/05/12
 */


class Edge(val label: String, val origin: Vertex, val destiny: Vertex, val nodos: Array[Nodo]) {
  var fizd: Face = null
  var fdcha: Face = null
  var ccwo: Edge = null
  //siguiente counterclockwise en origen
  var ccwd: Edge = null
  //siguiente counterclockwise en destino
  var inversa: Edge = null
  //var angle:Double = 0

  def igual(other: Edge): Boolean = {
    (origin.label == other.origin.label && destiny.label == other.destiny.label)
  }

  def exitAngle(): Double = {
    atan2(destiny.y - origin.y, destiny.x - origin.x)
  }

  def entryAngle(): Double = {
    atan2(origin.y - destiny.y, origin.x - destiny.x)
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
