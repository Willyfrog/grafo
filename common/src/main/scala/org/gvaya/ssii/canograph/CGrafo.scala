package org.gvaya.ssii.canograph

import collection.mutable.ArrayBuffer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import org.gvaya.ssii.dcel.{Edge, Vertex, DcelConstructor}
import com.badlogic.gdx.Gdx

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
  var order: Int = 0

  for (vert <- dcel.lvertices) {
    pendingVertices.append(new CVertice(vert.x, vert.y, vert.label))
  }
  for (aris <- dcel.laristas) {
    if (!aristas.exists((a:CArista) => (a.origen.etiqueta == aris.destino && a.destino.etiqueta == aris.origen ||
                                     a.destino.etiqueta == aris.destino && a.origen.etiqueta == aris.origen) )) {
      val verts = pendingVertices.filter(v => v.etiqueta==aris.origen || v.etiqueta==aris.destino) //recuperamos los dos vertices
      aristas.append(new CArista(verts(0), verts(1), aris.label, aris.triangulacion))
    }
  }
  /*for (aris <- aristas)
    Gdx.app.log("DEBUG", "arista " + aris.etiqueta + " - o: " + aris.origen.etiqueta + " d: " + aris.destino.etiqueta)
    */

  def moveVertex(v:CVertice) {
    pendingVertices -= v
    vertices.append(v)
    v.orden = order
    order += 1
    /*for (v <- aristas.filter(x=> x.origen.etiqueta==v.etiqueta || x.destino.etiqueta==v.etiqueta).map(y=> if (y.origen.etiqueta !=v.etiqueta) y.origen else y.destino) ) {
      v.valor += 1
    } */
    processVertex(v)
    println(v.getColor.toString)
  }

  def getNeighbours(v:CVertice):ArrayBuffer[CVertice] = {
    val arislist = aristas.filter(a => a.origen.etiqueta == v.etiqueta || a.destino.etiqueta == v.etiqueta)

    arislist.map(x => if (x.origen.etiqueta != v.etiqueta) x.origen else x.destino)
  }

  def processVertex(v:CVertice) {
    Gdx.app.log("DEBUG", "processing " + v.etiqueta)
    val neighbourlist = getNeighbours(v)
    var neig = ""
    for (n <- neighbourlist) {
       neig = neig + " " + n.etiqueta
    }
    Gdx.app.log("DEBUG", "neighbours " + neig)
    val orderedList = neighbourlist.filter(x => x.orden > -1)
    val vlist = neighbourlist.filter(x => x.orden == -1)
    for (w <- vlist) {
      Gdx.app.log("DEBUG", w.etiqueta + " had value " + w.valor)
      if (w.valor == -1) {
        w.valor = 0
      } else if (w.valor == 0) {
        val n = (getNeighbours(w)).filter(x => x.orden > -1)(0) //si es 0, solo puede tener 1
        if (orderedList.exists(p => p.etiqueta == n.etiqueta))
          w.valor = 1
        else
          w.valor = 2
      } else {
        val ns = (getNeighbours(w)).filter(x => x.orden > -1)
        var vecs = ""
        for (dn <- ns)
          vecs = vecs + " " + dn.etiqueta
        Gdx.app.log("DEBUG", "vecinos " + vecs)
        val matches = orderedList.count(p => (p.etiqueta == ns(0).etiqueta || p.etiqueta == ns(1).etiqueta))
        Gdx.app.log("DEBUG", "matches " + matches)
        if (matches == 2) {
          w.valor -= 1
        } else if (matches == 0) {
          w.valor += 1
        }
      }
      Gdx.app.log("DEBUG", w.etiqueta + " has value " + w.valor)
    }
  }

  def ended: Boolean = pendingVertices.isEmpty
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
        //v = pendingVertices.fold(pendingVertices(0))((x:CVertice,y:CVertice)=> if (x.valor>=y.valor) x else y)
        v = pendingVertices.find(x => x.valor==1).get
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
