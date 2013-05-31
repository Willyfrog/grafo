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
  var orden: Int = 0
  /**
   * dibuja el vértice en el shaperenderer
   * @param shape Shaperenderer en el que dibujar
   */

  def getColor:Color = {
    var genpallete = "FFFFFFFF"
    if (orden != 0) {
      //genpallete = String.padStart(Integer.toHexString((orden*33)%16777215) + "FF" //intento de generar una paleta de colores distinguibles: http://www.hitmill.com/html/color_safe.html
      genpallete = "%06x".format((orden*33)%16777215) + "FF"
    }
    //println(genpallete)
    Color.valueOf(genpallete)
  }

  def drawIntoShapeRenderer(shape: ShapeRenderer) {
    shape.begin(ShapeType.Filled)
    shape.setColor(getColor)
    val rad = if (orden>0) 5.0f else 3.0f
    shape.circle(x, y, rad)
    shape.end()
  }


}
