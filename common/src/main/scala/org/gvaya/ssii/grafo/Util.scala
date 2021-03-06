package org.gvaya.ssii.grafo

import com.badlogic.gdx.Gdx

/**
 * User: guillermo
 * Date: 30/05/12
 * Time: 11:25
 */

object Util {

  val DISTANCE = 10
  val WIDTH: Int = 800
  val HEIGHT: Int = 600
  var lastLabel: Int = -1

  def mismoSigno(x: Float, y: Float): Boolean = {
    ((x >= 0 && y >= 0) || (x <= 0 && y <= 0))
  }

  def area2(a: (Float, Float), b: (Float, Float), c: (Float, Float)): Float = {
    /*
    calcula el area del triangulo
    si devuelve < 0 esta a la derecha
    si devuelve 0 esta en la misma linea
    e.o.c. esta a la izda
   */
    (b._1 - a._1) * (c._2 - a._2) - (c._1 - a._1) * (b._2 - a._2)
  }

  def genLabel(): String = {
    lastLabel += 1
    if (lastLabel <= 25)
      'a'.to('z')(lastLabel).toString
    else if (lastLabel <= 51)
      'A'.to('Z')(lastLabel - 26).toString
    else
      (lastLabel - 52).toString
  }

  def interseccion(a1: (Float, Float), a2: (Float, Float), b1: (Float, Float), b2: (Float, Float)): Boolean = {
    //Gdx.app.log("Comprobando interseccion entre", a1.toString()+ a2.toString() + b1.toString() + b2.toString())
    val sig1 = mismoSigno(area2(a1, a2, b1), area2(a1, a2, b2))
    val sig2 = mismoSigno(area2(b1, b2, a1), area2(b1, b2, a2))
    //Gdx.app.log("Resultado: estan al mismo lado: ", (sig1, sig2).toString())
    !(sig1 || sig2)
  }

  def modulo2pi(angle:Double):Double={
    if (angle<0)
      modulo2pi(angle + 2*math.Pi)
    else
      angle%(2*math.Pi)
  }

  val POS_INICIAL_X=20.0f
  val POS_INICIAL_Y=20.0f
  val INCREMENTO=10.0f

}
