package org.gvaya.ssii.canograph

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType

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
class CVertice(x: Float, y: Float, val etiqueta: String, var valor: Int=0) extends CNodo(x, y) {
  var orden: Int = 0
  /**
   * dibuja el vértice en el shaperenderer
   * @param shape Shaperenderer en el que dibujar
   */
  def drawIntoShapeRenderer(shape: ShapeRenderer) {
    shape.begin(ShapeType.Filled)
    if (orden == 0) {
      shape.setColor(0.6f, 0.6f, 0.6f, 1f)
    } else {
      shape.setColor(0.9f, 0.0f, 0.9f, 1f)
    }
    shape.circle(x, y, 3.0f)
    shape.end()
  }


}
