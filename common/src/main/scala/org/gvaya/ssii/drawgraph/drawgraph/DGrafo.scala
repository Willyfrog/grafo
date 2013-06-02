package org.gvaya.ssii.drawgraph

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import collection.mutable.ArrayBuffer
import org.gvaya.ssii.canograph.CGrafo
import org.gvaya.ssii.grafo.Util
import scala.util.Sorting
import com.badlogic.gdx.Gdx

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
  var aristas: ArrayBuffer[DArista] = new ArrayBuffer[DArista]()
  var inicial:Boolean = true //el primer paso es diferente del resto

  for (vert <- cangraph.vertices) {
    vertices.append(new DVertice(vert.x, vert.y, vert.etiqueta, vert.orden, false))
  }
  for (aris <- cangraph.aristas) {
      val verts = vertices.filter(v => ((v.etiqueta == aris.origen.etiqueta) || (v.etiqueta == aris.destino.etiqueta))) //recuperamos los dos vertices
      aristas.append(new DArista(verts(0), verts(1), aris.eti, aris.triangular))
    }

  def showVertex(v:DVertice, new_x:Int, new_y:Int) {
    v.x = Util.POS_INICIAL_X + Util.INCREMENTO * new_x
    v.y = Util.POS_INICIAL_Y + Util.INCREMENTO * new_y
    v.show = true
  }

  vertices = vertices.sortWith((x, y) => x.orden <= y.orden)

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
      if (inicial) {
        showVertex(vertices(0), 0, 0)
        showVertex(vertices(1), 2, 0)
        showVertex(vertices(2), 1, 1)
        inicial = false

        for (v <- vertices)
          Gdx.app.log("Debug", "coordenadas :" + v.coordenadas() + "para orden: " + v.orden)

      } else {
        val hidden = vertices.filter(v => v.show == false)
        var v = hidden.fold(hidden(0))((v:DVertice, w:DVertice) => if (v.orden >= w.orden) v else w) //sacamos el siguiente ordenado
        //showVertex(v)
        v.show = true
        Gdx.app.log("Debug", "Vertice " + v.etiqueta + " esta ahora como " + v.show)
      }
    }
    else {
      removeTriangulares()
      print("Fin")
    }

  }

  /**
   * Elimina las aristas triangulares.
   * No se puede deshacer
   */
  def removeTriangulares() {
    aristas = aristas.filter(a => !(a.triangular))
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
