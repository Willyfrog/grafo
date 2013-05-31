package org.gvaya.ssii.canograph

import collection.mutable.ArrayBuffer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import org.gvaya.ssii.dcel.{Vertex, DcelConstructor}

/**
 * Created with IntelliJ IDEA.
 * @author guillermo
 * @since 29/05/12
 */

/**
 * clase contenedora de la estructura del grafo
 */
class CGrafo (val dcel: DcelConstructor){

  var vertices: ArrayBuffer[CVertice] = new ArrayBuffer[CVertice]()
  var pendingVertices: ArrayBuffer[CVertice] = new ArrayBuffer[CVertice]()
  val aristas: ArrayBuffer[CArista] = new ArrayBuffer[CArista]()
  var order: Int = 1

  for (vert <- dcel.lvertices) {
    pendingVertices.append(new CVertice(vert.x, vert.y, vert.label))
  }
  for (aris <- dcel.laristas) {
    if (!aristas.exists(a => a.etiqueta == aris.inversa.label)) {
      val verts = pendingVertices.filter(v => v.etiqueta==aris.origen || v.etiqueta==aris.destino) //recuperamos los dos vertices
      aristas.append(new CArista(verts(0), verts(1), aris.label, aris.triangulacion))
    }

  }

  def moveVertex(v:CVertice) {
    try {
      pendingVertices -= v
      vertices.append(v)
    }
    v.orden = order
    order += 1
    for (v <- aristas.filter(x=> x.origen.etiqueta==v.etiqueta || x.destino.etiqueta==v.etiqueta).map(y=> if (y.origen.etiqueta !=v.etiqueta) y.origen else y.destino) ) {
      v.valor += 1
    }
  }

  /**
   * damos un paso en el algoritmo
   * si no hay vertices, seleccionamos una arista no triangulante y empezamos por esos 2
   * e.o.c. cogemos el mas apto
   */
  def paso() {
    if (vertices.isEmpty) {
      var a = aristas.filter(x => !(x.triangular))(0)
      moveVertex(a.origen)
      moveVertex(a.destino)
    }
    else {
      var v:CVertice = null
      if (pendingVertices.isEmpty)
        print("Fin ordenacion")
      else if (pendingVertices.length > 1) {
        v = pendingVertices.fold(pendingVertices(0))((x:CVertice,y:CVertice)=> if (x.valor>=y.valor) x else y)
        moveVertex(v)
      }
      else {
        moveVertex(pendingVertices(0))
      }
    }
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
    for (v <- pendingVertices) v.drawIntoShapeRenderer(shape)
  }
}
