package org.gvaya.ssii.canograph

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.graphics.Color

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
  var orden: Int = -1

  /**
   * Si no tiene orden, pinta de blanco el nodo. Si lo tiene, entonces intenta genarar un color de una paleta considerada como distinguible:
   * http://www.hitmill.com/html/color_safe.html
   * @return Color
   */

  def getColor:Color = {
    var genpallete = "FFFFFFFF"
    if (orden != -1) {
      val safeorder = (orden % 214) + 1 // no queremos ni blanco ni negro
      // convertimos a base 6 y despues multiplicamos por 51 (33 hex) cada digito para generar la paleta mencionada en el articulo
      val u = (safeorder % 6) * 51
      val d = ((safeorder / 6) % 6) * 51
      val c = ((safeorder / 36) % 6) * 51
      genpallete = "%02x".format(c) + "%02x".format(d) + "%02x".format(u) + "FF"
    }
    //println(genpallete)
    Color.valueOf(genpallete)
  }

  /**
   * dibuja el vértice en el shaperenderer
   * @param shape Shaperenderer en el que dibujar
   */

  def drawIntoShapeRenderer(shape: ShapeRenderer) {
    shape.begin(ShapeType.Filled)
    shape.setColor(getColor)
    val rad = if (orden>=0) 5.0f else 3.0f
    shape.circle(x, y, rad)
    shape.end()
  }


}
