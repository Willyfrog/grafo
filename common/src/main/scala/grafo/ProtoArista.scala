package grafo

import collection.mutable.ArrayBuffer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.Gdx

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
    var add = true
    if( !nodos.isEmpty){
      if (nodos.last.distance(x,y)>=5){
        if (!intersectaSegmentos(x,y))
        {
          nodos.append(new Nodo(x,y))
        }
        else {
          //Gdx.app.log("choca!", "pero le seguimos queriendo por temas de debug")
          add = false
        }
      }
      else
        add = false
    }
    else if (origen.distance(x,y)<5)
    {
      add = false
    }
    if (add) nodos.append(new Nodo(x,y))
  }

  def intersectaSegmentos(x:Float,y:Float):Boolean={
    var intersecta:Boolean = false
    if (!nodos.isEmpty){
      val lis = listaSegmentos()
      val last = lastCoords()
      for (e <- lis ){
        intersecta |= Util.interseccion(e._1,e._2, last, (x, y))
        //if (intersecta) Gdx.app.log("Corte", e.toString() + " - " + (last, (x,y)).toString())
      }
    }
    return intersecta
  }

  def lastCoords():(Float,Float)={
    if (!nodos.isEmpty)
      return (nodos.last.x, nodos.last.y)
    else
      return (origen.x, origen.y)
  }

  def listaSegmentos():ArrayBuffer[((Float,Float),(Float,Float))]={
    var bl:ArrayBuffer[((Float,Float),(Float,Float))] = ArrayBuffer[((Float,Float),(Float,Float))]()
    if (!nodos.isEmpty){
      var o = (origen.x, origen.y)
      for (n<-nodos){
        bl.append((o,(n.x,n.y)))
        o = (n.x, n.y)

      }

    }
    //Gdx.app.log("lista segmentos:", bl.toString())
    return bl
  }

  def listaCoordenadasNodos(): List[Float] = {
    //Goes through nodes and append coordinates as a list
    return (for (n <- nodos) yield n.coordenadas()).toList.flatten
  }

  def addVertice(v:Vertice){
    if (origen==null)
      origen=v
    else if (origen!=v)
      destino=v
  }

  def toArista():Arista={
    Gdx.app.log("Lista Segmentos:", listaSegmentos().toString())
    val str = for (n <- nodos) yield n.coordenadas()
    Gdx.app.log("Lista Nodos", str.toString())
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
