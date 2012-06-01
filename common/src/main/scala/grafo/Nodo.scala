package grafo

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import math.{sqrt, pow}

/**
 * @author guillermo
 * @since 25/05/12
 */

/**
 * Clase Nodo
 * @define THIS Nodo
 * @define Parent Ninguno
 *
 * @param x Posicion en el eje x
 * @param y Posicion en el eje y
 */

class Nodo(val x: Float, val y: Float) {

  val z = 0

  /**
   * Coordenadas
   * @return Las coordenadas x,y,z en formato de lista de floats
   */
  def coordenadas(): List[Float] = {
    return List(x, y, z)
  }

  /**
   * Distancia del nodo a un punto dado
   * @param x1 coordenada x del punto
   * @param y1 coordenada y del punto
   * @return distancia al punto. Siempre positiva
   */
  def distance(x1:Float,y1:Float): Double = sqrt(pow(x1-x,2)+pow(y1-y,2))
}
