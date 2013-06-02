package org.gvaya.ssii.drawgraph

import com.badlogic.gdx.graphics.{VertexAttribute, Mesh}
import com.badlogic.gdx.graphics.VertexAttributes.Usage
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import org.gvaya.ssii.grafo.Util
import collection.mutable.ArrayBuffer

/**
 * @author guillermo
 * @since 25/05/12
 */

/**
 * Clase Arista
 * @param origen Vertice de origen
 * @param destino Vertice de destino
 */
class DArista(val origen: DVertice, val destino: DVertice, val eti:String, val triangular:Boolean=false) {
  var mesh: Mesh = null

  /**
   * Lista de vertices y nodos.
   * @return lista de longitud par desde el vertice origen al destino incluyendo nodos intermedios
   */
  def getVertexList: List[Float] = {
    origen.coordenadas ::: destino.coordenadas
  }

  /**
   * Devuelve un mesh con la arista.
   * @deprecated
   * @return mesh de tipo linea
   */
  def getMesh: Mesh = {
    val len = 2
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
  def etiqueta(): String = eti + "." + origen.etiqueta + "_" + destino.etiqueta

  /**
   * Dado un shaperenderer, dibuja dentro la arista
   * @param shape shaperenderer en el que dibujar la arista con los nodos intermedios
   */
  def drawIntoShapeRenderer(shape: ShapeRenderer) {
    if (origen.show && destino.show)
      shape.line(origen.x, origen.y, destino.x, destino.y)
  }

  /**
   * listado de los segmentos de la arista
   * @return lista de tuplas definiendo los segmentos que componen la arista
   */
  def listaSegmentos(): ArrayBuffer[((Float, Float), (Float, Float))] = {
    var bl: ArrayBuffer[((Float, Float), (Float, Float))] = ArrayBuffer[((Float, Float), (Float, Float))]()
    var o: (Float, Float) = (origen.x, origen.y)
    bl.append((o, (destino.x, destino.y))) //como poco hay un segmento origen-destino
    return bl
  }

  /**
   * comprueba si una arista interseca con otra
   * @param otra la otra arista
   * @return hay interseccion?
   */
  def intersectaConOtra(otra: DArista): Boolean = {
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
