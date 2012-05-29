package grafo

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import math.{sqrt,pow}

/**
 * Created with IntelliJ IDEA.
 * User: guillermo
 * Date: 25/05/12
 * Time: 9:55
 * To change this template use File | Settings | File Templates.
 */

class Vertice(x: Float, y: Float, val etiqueta: String) extends Nodo(x, y) {

  def drawIntoShapeRenderer(shape:ShapeRenderer){
    shape.circle(x,y, 3.0f)
  }

  def distance(x1:Float,y1:Float): Double = sqrt(pow(x1-x,2)+pow(y1-y,2))


}
