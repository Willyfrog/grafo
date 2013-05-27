package org.gvaya.ssii.canograph

import com.badlogic.gdx.graphics.glutils.ShapeRenderer

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
class CVertice(x: Float, y: Float, val etiqueta: String, orden: Int) extends CNodo(x, y) {

  /**
   * dibuja el vértice en el shaperenderer
   * @param shape Shaperenderer en el que dibujar
   */
  def drawIntoShapeRenderer(shape: ShapeRenderer) {
    shape.circle(x, y, 3.0f)
  }


}
