package org.gvaya.ssii.dcel

import collection.mutable.ArrayBuffer
import org.gvaya.ssii.grafo._
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.physics.box2d.EdgeShape
import com.badlogic.gdx.Gdx

/**
 * User: gvaya
 * Date: 28/05/12
 * Time: 23:30
 */

class DcelConstructorException(message:String) extends Exception(message){}

class DcelConstructor(val g: Grafo) {

  var lvertices: ArrayBuffer[Vertex] = new ArrayBuffer[Vertex]()
  var laristas: ArrayBuffer[Edge] = new ArrayBuffer[Edge]()
  var lcaras: ArrayBuffer[Face] = new ArrayBuffer[Face]()

  def addVertex(v: Vertice) {
    addVertex(new Vertex(v.etiqueta, v.x, v.y))
  }

  def addVertex(v: Vertex) {
    lvertices.append(v)
  }

  def addArista(arista:Arista){
    addArista(arista, false)
  }

  def addArista(arista: Arista, trianguladora: Boolean) {
    val v1: Vertex = findVertexByCoords(arista.origen.x, arista.origen.y).get
    val v2: Vertex = findVertexByCoords(arista.destino.x, arista.destino.y).get
    val nodos = arista.nodos
    val e1: Edge = addEdge(v1, v2, nodos, arista.eti, trianguladora)
    val e2: Edge = addEdge(v2, v1, nodos.reverse, arista.eti, trianguladora)
    e1.inversa = e2
    e2.inversa = e1
  }

  def addEdge(v1: Vertex, v2: Vertex, nodos: Array[Nodo], eti:String, trianguladora:Boolean): Edge = {
    if (!lvertices.contains(v1))
      addVertex(v1)
    if (!lvertices.contains(v2))
      addVertex(v2)
    val e: Edge = new Edge(eti + "." + v1.label + "_" + v2.label, v1, v2, nodos, trianguladora)
    laristas.append(e)
    e
  }

  //Como encontrar un Vertex
  def findVertexByCoords(x: Float, y: Float): Option[Vertex] = lvertices.find(_.sonCoordenadas(x, y))

  //pregunta a cada elto. por sus coordenadas
  def findVertexByLabel(lbl: String): Option[Vertex] = lvertices.find(_.label == lbl)

  //Como encontrar un Edge
  def findEdgesFromVertex(v: Vertex): List[Edge] = laristas.filter(_.origen == v.label).toList

  def findEdgesToVertex(v: Vertex): List[Edge] = laristas.filter(_.llegaA(v)).toList

  def addFace(f: Face) {
    lcaras.append(f)
  }

  def siguienteEdge(arista:Edge):Edge={
    val l = findEdgesFromVertex(arista.destiny).filter(_.label!=arista.inversa.label) //aristas que salen del destino, menos la inversa
    val l2 = findEdgesFromVertex(arista.destiny)
    //Gdx.app.log("SiguienteEdge", "   siguientes aristas posibles: " + l.foldLeft("")((x:String, y:Edge)=> x + ", " + y.label))
    val ang = arista.entryAngle()
    if (!l.isEmpty)
      l.maxBy (e => Util.modulo2pi(e.exitAngle() - ang) )
    else
      arista.inversa //no hay otra, asi que volvemos por donde vinimos (o casi)
  }

  //funcion que devuelva una arista sin siguiente
  def primeraSinSiguiente():Edge={
    val i = laristas.indexWhere(_.tieneSiguiente)
    if (i>=0)
      laristas(i)
    else
      null
  }

  /**
   * Convierte una cara a triangular
   */
  def addTriangulo
  {
    val ind = lcaras.indexWhere(_.esTriangular==false) //localiza una cara no triangular
    var caraO = lcaras(ind)

    var siguiente = caraO.arista.ccwd
    //generamos las aristas que cortaran la cara para formar un triangulo
    var eti = Util.genLabel()
    var nod:Array[Nodo] = Array[Nodo]()
    var t1 = addEdge(caraO.arista.origin,siguiente.destiny, nod, eti,true)
    var t2 = addEdge(siguiente.destiny, caraO.arista.origin, nod, eti,true)
    t1.inversa = t2
    t2.inversa = t1
    //enlazamos t1
    t1.ccwd = siguiente.ccwd
    siguiente.ccwd.ccwo = t1
    t1.ccwo = caraO.arista.ccwo
    caraO.arista.ccwo.ccwd = t1
    //generamos la nueva cara
    val f = new Face(Util.genLabel(),t1)
    //enlazamos t2
    t2.ccwd = caraO.arista
    caraO.arista.ccwo=t2
    t2.ccwo = siguiente
    siguiente.ccwd = t2

    //guardamos aristas y cara
    laristas.append(t1,t2)
    lcaras.append(f)
  }

  def todasTriangulares:Boolean={
    lcaras.indexWhere(_.esTriangular==false)<0 //no encuentra la posicion de una cara no triangular
  }

  def generar() {

    for (v <- g.vertices)
      addVertex(v)
    for (a <- g.aristas)
      addArista(a)
    Gdx.app.log("Generar", "Lista de aristas")
    for (a:Edge <- laristas){
      var nods = "[%s|%s|%s]".format(a.origin.toString,a.nodos.foldLeft("")((a,n)=>a+n.toString),a.destiny.toString)
      Gdx.app.log("Generar", "Lbl:%s, ori:%s, dest:%s, sal:%.3f, ent:%.3f, %s".format(a.label, a.origin.label, a.destiny.label, a.exitAngle()*180/math.Pi, a.entryAngle()*180/math.Pi, nods))
    }

    //generamos caras y enlazamos aristas
    var e0 = primeraSinSiguiente()
    Gdx.app.log("DCEL", "Comenzamos por " + e0.label)
    var e1:Edge = e0
    var e2:Edge = null
    while (e0!=null)
    { //recorremos las aristas para formar las caras
      val f = new Face(Util.genLabel(), e0)
      lcaras.append(f)
      e2 = siguienteEdge(e1)
      while (e1.destino != e0.origen) //se ha cerrado el ciclo?
      {
        Gdx.app.log("DCEL", "  Procesamos a " + e2.label)
        if (e2.ccwo!=null) //asegurarnos de que no hace cosas mal (como entrar en un bucle infinito)
          throw new DcelConstructorException("Error al construir las caras, %s ya tenia siguiente".format(e2.label))
        //enlazamos
        e2.ccwo = e1
        e1.ccwd = e2
        //siguiente
        e1 = e2
        e2 = siguienteEdge(e1)
      }
      //cerramos la cara
      e0.ccwo = e1
      e1.ccwd = e0
      //a por otra cara
      e0 = primeraSinSiguiente()
      e1 = e0
      e2 = null
      if (e0 != null)
        Gdx.app.log("DCEL", "Seguimos por " + e0.label)
      else
        Gdx.app.log("DCEL", "ya no hay mas caras")
    }

    Gdx.app.log("Generar", "fin de la generacion de dcel")
  }

  def drawIntoShapeRenderer(shape: ShapeRenderer) {
    shape.begin(ShapeType.Line)
    shape.setColor(0.6f, 0.6f, 0.6f, 1f)//Dibujar aristas de triangulacion
    laristas.filter(_.triangulacion).toList.foreach(_.drawIntoShapeRenderer(shape))
    shape.end()
    shape.begin(ShapeType.Line)
    shape.setColor(0.5f, 0.5f, 0.9f, 1f)//Dibujar las originales del grafo
    laristas.filter(_.triangulacion==false).toList.foreach(_.drawIntoShapeRenderer(shape))
    shape.end()
    shape.begin(ShapeType.Circle)//Dibujar vertices
    shape.setColor(0.5f, 0.9f, 0.5f, 1f)
    lvertices.foreach(_.drawIntoShapeRenderer(shape))
    shape.end()
  }
}
