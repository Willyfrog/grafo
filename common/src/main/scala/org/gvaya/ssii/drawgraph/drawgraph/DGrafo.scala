package org.gvaya.ssii.drawgraph

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import collection.mutable.ArrayBuffer
import org.gvaya.ssii.canograph.CGrafo

/**
 * Created with IntelliJ IDEA.
 * @author guillermo
 * @since 29/05/12
 */

/**
 * clase contenedora de la estructura del grafo
 */
class DGrafo (val cangraph: CGrafo){
  var vertices: ArrayBuffer[DVertice] = new ArrayBuffer[DVertice]()
  var pendingVertices: ArrayBuffer[DVertice] = new ArrayBuffer[DVertice]()
  val aristas: ArrayBuffer[DArista] = new ArrayBuffer[DArista]()
  var order: Int = 0

  for (vert <- cangraph.vertices) {
    vertices.append(new DVertice(vert.x, vert.y, vert.etiqueta, vert.orden, false))
  }
  for (aris <- cangraph.aristas) {
      val verts = vertices.filter(v => ((v.etiqueta == aris.origen.etiqueta) || (v.etiqueta == aris.destino.etiqueta))) //recuperamos los dos vertices
      aristas.append(new DArista(verts(0), verts(1), aris.eti, aris.triangular))
    }

  def showVertex(v:DVertice) {
    v.show = true
  }

  /**
   * Habra termnado cuando todos los vertices esten visibles
   * @return existe algun vertice invisible?
   */
  def ended: Boolean = !(vertices.exists(v => v.show==false))

  /**
   * damos un paso en el algoritmo
   * si no hay vertices, seleccionamos una arista no triangulante y empezamos por esos 2
   * e.o.c. cogemos el mas apto
   */
  def paso() {
    if (! ended) {
      val hidden = vertices.filter(v => v.show == false)
      var v = hidden.fold(hidden(0))((v:DVertice, w:DVertice) => if (v.orden >= w.orden) v else w) //sacamos el siguiente ordenado
      showVertex(v)
      print ("Vertice " + v.etiqueta + " esta ahora como " + v.show)
    }
    else
      print("Fin")
  }

  /**
   * Elimina las aristas triangulares.
   * No se puede deshacer
   */
  def removeTriangulares() {
    aristas = aristas.filter(a => !(a.triagular))
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
    for (v <- vertices) v.drawIntoShapeRenderer(shape)
  }
}
