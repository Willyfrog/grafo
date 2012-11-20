package org.gvaya.ssii.grafo

import collection.mutable.ArrayBuffer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.Gdx

/**
 * @author guillermo
 * @since 29/05/12
 *        Asistente para la creacion grafica de aristas
 */

/**
 * Constructor de arista
 */
class ProtoArista {
  var origen: Vertice = null
  var destino: Vertice = null
  var nodos: ArrayBuffer[Nodo] = ArrayBuffer[Nodo]()

  /**
   * Añade un nodo a la arista siempre y cuando este a cierta distancia
   * @see Util.DISTANCE
   * @param x posicion x del nodo
   * @param y posicion y del nodo
   */
  def addNodo(x: Float, y: Float) {
    var add = true
    if (!nodos.isEmpty) {
      if (nodos.last.distance(x, y) >= Util.DISTANCE) {
        if (!intersectaSegmentos(x, y)) {
          nodos.append(new Nodo(x, y))
        }
        else {
          //Gdx.app.log("choca!", "pero le seguimos queriendo por temas de debug")
          add = false
        }
      }
      else
        add = false
    }
    else if (origen.distance(x, y) < Util.DISTANCE) {
      add = false
    }
    if (add) nodos.append(new Nodo(x, y))
  }

  /**
   * comprueba si dado un punto con el que generar un segmento este cortaria contra la misma arista
   * @param x posicion x del punto
   * @param y posicion y del punto
   * @return interseca sobre si misma?
   */
  def intersectaSegmentos(x: Float, y: Float): Boolean = {
    var intersecta: Boolean = false
    if (!nodos.isEmpty) {
      val lis = listaSegmentos()
      val last = lastCoords()
      for (e <- lis) {
        intersecta |= Util.interseccion(e._1, e._2, last, (x, y))
        //if (intersecta) Gdx.app.log("Corte", e.toString() + " - " + (last, (x,y)).toString())
      }
    }
    return intersecta
  }

  /**
   * coordenadas del ultimo nodo/vertice añadido
   * @return coordenadas
   */
  def lastCoords(): (Float, Float) = {
    if (!nodos.isEmpty)
      return (nodos.last.x, nodos.last.y)
    else
      return (origen.x, origen.y)
  }

  /**
   * lista de segmentos de la arista
   * @return
   */
  def listaSegmentos(): ArrayBuffer[((Float, Float), (Float, Float))] = {
    var bl: ArrayBuffer[((Float, Float), (Float, Float))] = ArrayBuffer[((Float, Float), (Float, Float))]()
    if (!nodos.isEmpty) {
      var o = (origen.x, origen.y)
      for (n <- nodos) {
        bl.append((o, (n.x, n.y)))
        o = (n.x, n.y)

      }

    }
    //Gdx.app.log("lista segmentos:", bl.toString())
    return bl
  }

  /**
   * lista de las coordenadas de los nodos
   * @return coordenadas de los nodos. Tiene longitud par
   */
  def listaCoordenadasNodos(): List[Float] = {
    //Goes through nodes and append coordinates as a list
    return (for (n <- nodos) yield n.coordenadas()).toList.flatten
  }

  /**
   * Añade un vertice a la arista si cumple requisitos
   * @param v vertice a añadir
   * @return añadido?
   */
  def addVertice(v: Vertice): Boolean = {
    var res: Boolean = false
    if (origen == null) {
      origen = v
      res = true
    }
    else if (origen != v) {
      if (!intersectaSegmentos(v.x, v.y)) {
        destino = v
        res = true
      }
    }
    return res
  }

  /**
   * Genera una arista a partir de la informacion del constructor
   * @return una arista o nada si no hubiera suficiente informacion
   */
  def toArista(): Arista = {
    //Gdx.app.log("Lista Segmentos:", listaSegmentos().toString())
    //val str = for (n <- nodos) yield n.coordenadas()
    //Gdx.app.log("Lista Nodos", str.toString())
    if (!estaCompleta)
      return null
    else
      return new Arista(origen, destino, nodos.toArray, Util.genLabel())
  }

  /**
   * Dibuja la arista en un shaperenderer
   * @param shape
   */
  def drawIntoShapeRenderer(shape: ShapeRenderer) {
    var ox = origen.x
    var oy = origen.y
    for (n <- nodos) {
      shape.line(ox, oy, n.x, n.y)
      ox = n.x
      oy = n.y
    }
    if (destino != null)
      shape.line(ox, oy, destino.x, destino.y)
  }

  /**
   * Comprueba si tiene toda la informacion necesaria para formar una arista
   * @return tiene origen y destino?
   */
  def estaCompleta: Boolean = (destino != null && origen != null)

  /**
   * Comprueba si hay informacion en la arista
   * @return tiene origen?
   */
  def estaVacia: Boolean = (origen == null)

}
