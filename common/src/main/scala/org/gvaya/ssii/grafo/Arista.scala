package org.gvaya.ssii.grafo

import com.badlogic.gdx.graphics.{VertexAttribute, Mesh}
import com.badlogic.gdx.graphics.VertexAttributes.Usage
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import collection.mutable.ArrayBuffer

/**
 * @author guillermo
 * @since 25/05/12
 */

/**
 * Clase Arista
 * @param origen Vertice de origen
 * @param destino Vertice de destino
 * @param nodos Listado de nodos intermedios si no es una linea recta entre origen y destino
 */
class Arista(val origen: Vertice, val destino: Vertice, val nodos: Array[Nodo] = Array[Nodo]()) {
  var mesh: Mesh = null

  /**
   * Lista de Coordenadas de los nodos intermedios
   * @return listado de las coordenadas de los nodos. esta lista tiene una longitud par al no ir en tuplas
   */
  def listaCoordenadasNodos(): List[Float] = {
    //Goes through nodes and append coordinates as a list
    (for (n <- nodos) yield n.coordenadas()).toList.flatten
  }

  /**
   * Lista de vertices y nodos.
   * @return lista de longitud par desde el vertice origen al destino incluyendo nodos intermedios
   */
  def getVertexList: List[Float] = {
    origen.coordenadas ::: listaCoordenadasNodos() ::: destino.coordenadas
  }

  /**
   * Devuelve un mesh con la arista.
   * @deprecated
   * @return mesh de tipo linea
   */
  def getMesh: Mesh = {
    val len = 2 + nodos.length
    if (mesh == null) {
      mesh = new Mesh(true, len, len, new VertexAttribute(Usage.Position, len, "a_position"))
      mesh.setVertices(getVertexList.toArray)

    }
    return mesh
  }

  /**
   * etiqueta de la arista
   * @return devuelve la etiqueta de la arista en forma origen_destino
   */
  def etiqueta(): String = origen.etiqueta + "_" + destino.etiqueta

  /**
   * Dado un shaperenderer, dibuja dentro la arista
   * @param shape shaperenderer en el que dibujar la arista con los nodos intermedios
   */
  def drawIntoShapeRenderer(shape: ShapeRenderer) {
    var ox = origen.x
    var oy = origen.y
    for (n <- nodos) {
      shape.line(ox, oy, n.x, n.y)
      ox = n.x
      oy = n.y
    }
    shape.line(ox, oy, destino.x, destino.y)
  }

  /**
   * listado de los segmentos de la arista
   * @return lista de tuplas definiendo los segmentos que componen la arista
   */
  def listaSegmentos(): ArrayBuffer[((Float, Float), (Float, Float))] = {
    var bl: ArrayBuffer[((Float, Float), (Float, Float))] = ArrayBuffer[((Float, Float), (Float, Float))]()
    var o: (Float, Float) = (origen.x, origen.y)
    if (!nodos.isEmpty) {
      for (n <- nodos) {
        bl.append((o, (n.x, n.y)))
        o = (n.x, n.y)
      }
    }
    bl.append((o, (destino.x, destino.y))) //como poco hay un segmento origen-destino
    //Gdx.app.log("lista segmentos:", bl.toString())
    return bl
  }

  /**
   * comprueba si una arista interseca con otra
   * @param otra la otra arista
   * @return hay interseccion?
   */
  def intersectaConOtra(otra: Arista): Boolean = {
    val otral = otra.listaSegmentos()
    val lis = listaSegmentos()
    for (s <- lis) {
      for (os <- otral) {
        if (Util.interseccion(s._1, s._2, os._1, os._2)) {
          return true // no seguimos comprobando si no hace falta
        }
      }
    }
    return false
  }
}
