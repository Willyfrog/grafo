package grafo

import collection.mutable.ArrayBuffer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.Gdx

/**
 * Created with IntelliJ IDEA.
 * @author guillermo
 * @since 29/05/12
 */

/**
 * clase contenedora de la estructura del grafo
 */
class Grafo {
  val vertices:ArrayBuffer[Vertice] = new ArrayBuffer[Vertice]()
  val aristas:ArrayBuffer[Arista] = new ArrayBuffer[Arista]()

  /**
   * Añade una arista si no interseca con ninguna otra
   * @param a arista a añadir
   * @return Añadida?
   */
  def addArista(a:Arista):Boolean={
    var add:Boolean = true
    for (aris<-aristas){
      add &= !aris.intersectaConOtra(a)
      if (!add)
        return add
    }
    //si ha llegado hasta el final, no interseca
    aristas.append(a)
    if (!vertices.contains(a.origen))
      addVertice(a.origen)
    if (!vertices.contains(a.destino))
      addVertice(a.destino)
    return add
  }

  /**
   * Añadir vertice al grafo
   * @param v vertice
   */
  def addVertice(v:Vertice){
    vertices.append(v)
  }

  /**
   * Dibuja el grafo en el shaperenderer
   * @param shape shaperenderer
   */
  def drawIntoShapeRenderer(shape:ShapeRenderer){
    shape.begin(ShapeType.Line)
    shape.setColor(0f,1f,0.9f,1f)
    for (a<-aristas) a.drawIntoShapeRenderer(shape)
    shape.end()
    shape.begin(ShapeType.Circle)
    shape.setColor(1f,0f,0.9f,1f)
    for (v<-vertices) v.drawIntoShapeRenderer(shape)
    shape.end()
  }

  /**
   * Comprobacion de seleccion de vertices si esta a menos de cierta distancia
   * @see Util.DISTANCE
   * @param x1 coordenadas x del punto
   * @param y1 coordenadas y del punto
   * @return está seleccionado un punto?
   */
  def tocandoVertice(x1:Float, y1:Float):Vertice={
    for (v<-vertices){
      if (v.distance(x1,y1) <= Util.DISTANCE){
        //Gdx.app.log("DEBUG", "Tocando a " + v.etiqueta)
        return v
      }
    }
    return null
  }

}
