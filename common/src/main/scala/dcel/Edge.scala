package dcel

/**
 * Created with IntelliJ IDEA.
 * User: gvaya
 * Date: 28/05/12
 * Time: 23:30
 * To change this template use File | Settings | File Templates.
 */

class Edge (val label:String, val origin:Vertex, val destiny:Vertex){
  var fizd:Face = null
  var fdcha:Face = null
  var ccwo:Edge = null //siguiente counterclockwise en origen
  var ccwd:Edge = null //siguiente counterclockwise en destino
  var angle:Double = 0

  def igual(other:Edge):Boolean={
  return (origin == other.origin && destiny == other.destiny)
  }
}
