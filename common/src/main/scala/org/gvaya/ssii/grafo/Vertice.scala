package org.gvaya.ssii.grafo

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import math.{sqrt, pow}

/**
 * Created with IntelliJ IDEA.
 * User: guillermo
 * Date: 25/05/12
 * Time: 9:55
 * To change this template use File | Settings | File Templates.
 */

/**
 * Vértice
 * @param x Posicion en el eje x
 * @param y Posicion en el eje y
 * @param etiqueta Nombre del vértice
 */
class Vertice(x: Float, y: Float, val etiqueta: String) extends Nodo(x, y) {

  /**
   * dibuja el vértice en el shaperenderer
   * @param shape Shaperenderer en el que dibujar
   */
  def drawIntoShapeRenderer(shape: ShapeRenderer) {
    shape.circle(x, y, 3.0f)
  }


}
