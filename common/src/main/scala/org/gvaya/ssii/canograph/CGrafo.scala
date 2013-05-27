package org.gvaya.ssii.canograph

import collection.mutable.ArrayBuffer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType

/**
 * Created with IntelliJ IDEA.
 * @author guillermo
 * @since 29/05/12
 */

/**
 * clase contenedora de la estructura del grafo
 */
class CGrafo {
  val vertices: ArrayBuffer[CVertice] = new ArrayBuffer[CVertice]()
  val aristas: ArrayBuffer[CArista] = new ArrayBuffer[CArista]()

  /**
   * Añade una arista si no interseca con ninguna otra
   * @param a arista a añadir
   */
  def addArista(a: CArista) {
    aristas.append(a)
  }

  /**
   * Añadir vertice al grafo
   * @param v vertice
   */
  def addVertice(v: CVertice) {
    vertices.append(v)
  }

  /**
   * Dibuja el grafo en el shaperenderer
   * @param shape shaperenderer
   */
  def drawIntoShapeRenderer(shape: ShapeRenderer) {
    shape.begin(ShapeType.Line)
    shape.setColor(0f, 1f, 0.9f, 1f)
    for (a <- aristas) a.drawIntoShapeRenderer(shape)
    shape.end()
    shape.begin(ShapeType.Filled)
    shape.setColor(1f, 0f, 0.9f, 1f)
    for (v <- vertices) v.drawIntoShapeRenderer(shape)
    shape.end()
  }
}
