package grafo

import collection.mutable.ArrayBuffer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

/**
 * Created with IntelliJ IDEA.
 * User: guillermo
 * Date: 29/05/12
 * Time: 11:29
 * Asistente para la creacion grafica de aristas
 */

class ProtoArista {
  var origen:Vertice = null
  var destino:Vertice = null
  var nodos:ArrayBuffer[Nodo] = ArrayBuffer[Nodo]()

  def addNodo(x:Float, y:Float){
    nodos.append(new Nodo(x,y))
  }

  def addVertice(v:Vertice){
    if (origen==null)
      origen=v
    else
      destino=v
  }

  def toArista():Arista={
    if (!estaCompleta)
      return null
    else
      return new Arista(origen,destino, nodos.toArray)
  }

  def drawIntoShapeRenderer(shape:ShapeRenderer){
    var ox = origen.x
    var oy = origen.y
    for (n <- nodos){
      shape.line(ox,oy,n.x,n.y)
      ox = n.x
      oy = n.y
    }
    if (destino != null)
      shape.line(ox, oy, destino.x, destino.y)
  }

  def estaCompleta:Boolean=(destino!=null&&origen!=null)
  def estaVacia:Boolean=(origen==null)

}
