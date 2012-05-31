package grafo

import com.badlogic.gdx.Gdx

/**
 * Created with IntelliJ IDEA.
 * User: guillermo
 * Date: 30/05/12
 * Time: 11:25
 * To change this template use File | Settings | File Templates.
 */

object Util {

  val DISTANCE = 10

  def mismoSigno(x:Float,y:Float):Boolean={
    ((x>=0 && y>=0) || (x<=0 && y<=0))
  }

  def area2(a:(Float, Float), b:(Float, Float), c:(Float, Float)):Float={
    /*
    calcula el area del triangulo
    si devuelve < 0 esta a la derecha
    si devuelve 0 esta en la misma linea
    e.o.c. esta a la izda
   */
    (b._1 - a._1)*(c._2 - a._2) - (c._1 - a._1)*(b._2 - a._2)
  }

  def interseccion(a1:(Float,Float), a2:(Float,Float), b1:(Float,Float), b2:(Float,Float)):Boolean={
    Gdx.app.log("Comprobando interseccion entre", a1.toString()+ a2.toString() + b1.toString() + b2.toString())
    val sig1 = mismoSigno(area2(a1,a2,b1),area2(a1,a2,b2))
    val sig2 = mismoSigno(area2(b1,b2,a1),area2(b1,b2,a2))
    Gdx.app.log("Resultado: estan al mismo lado: ", (sig1, sig2).toString())
    return  !sig1 || !sig2
  }

}
