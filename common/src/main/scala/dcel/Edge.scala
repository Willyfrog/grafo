package dcel

import math.atan2

/**
 * Created with IntelliJ IDEA.
 * @author gvaya
 * @since 28/05/12
 */


class Edge (val label:String, val origin:Vertex, val destiny:Vertex){
  var fizd:Face = null
  var fdcha:Face = null
  var ccwo:Edge = null //siguiente counterclockwise en origen
  var ccwd:Edge = null //siguiente counterclockwise en destino
  //var angle:Double = 0

  def igual(other:Edge):Boolean={
  (origin == other.origin && destiny == other.destiny)
  }

  def angle():Double={
    atan2(destiny.y - origin.y, destiny.x - origin.x)
  }
}
