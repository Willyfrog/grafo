package grafo

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import math.{sqrt, pow}

/**
 * Created with IntelliJ IDEA.
 * User: guillermo
 * Date: 25/05/12
 * Time: 9:51
 * To change this template use File | Settings | File Templates.
 */

class Nodo(val x: Float, val y: Float) {
  val z = 0

  def coordenadas(): List[Float] = {
    return List(x, y, z)
  }

  def distance(x1:Float,y1:Float): Double = sqrt(pow(x1-x,2)+pow(y1-y,2))
}
