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
    (origin == other.origin && destiny == other.destiny)
  }

  def exitAngle(): Double = {
    atan2(destiny.y - origin.y, destiny.x - origin.x)
  }

  def entryAngle(): Double = {
    atan2(origin.y - destiny.y, origin.x - destiny.x)
  }

  def parteDe(v: Vertex): Boolean = origin.x == v.x && origin.y == v.y

  def llegaA(v: Vertex): Boolean = destiny.x == v.x && destiny.y == v.y

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
