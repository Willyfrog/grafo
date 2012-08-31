package org.gvaya.ssii.dcel

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import math.{sqrt, pow}

/**
 * @author gvaya
 * @since 28/05/12
 */

class Vertex(val label: String, var x: Float, var y: Float) {
  var incidente: Edge = null

  def coordenadas(): (Float, Float) = (x, y)

  def sonCoordenadas(x1: Float, y1: Float): Boolean = x == x1 && y == y1

  def setCoords(x1: Float, y1: Float) {
    x = x1
    y = y1
  }

  def drawIntoShapeRenderer(shape: ShapeRenderer) {
    shape.circle(x, y, 3.0f)
  }

  def distance(x1: Float, y1: Float): Double = {
    sqrt(pow(x1 - x, 2) + pow(y1 - y, 2))
  }

}
