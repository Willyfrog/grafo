package grafo

import collection.mutable.ArrayBuffer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.Gdx

/**
 * Created with IntelliJ IDEA.
 * User: guillermo
 * Date: 29/05/12
 * Time: 11:56
 * To change this template use File | Settings | File Templates.
 */

class Grafo {
  val vertices:ArrayBuffer[Vertice] = new ArrayBuffer[Vertice]()
  val aristas:ArrayBuffer[Arista] = new ArrayBuffer[Arista]()

  def addArista(a:Arista){
    aristas.append(a)
    if (!vertices.contains(a.origen))
      addVertice(a.origen)
    if (!vertices.contains(a.destino))
      addVertice(a.destino)
  }

  def addVertice(v:Vertice){
    vertices.append(v)
  }

  def drawIntoShapeRenderer(shape:ShapeRenderer){
    shape.begin(ShapeType.Line)
    for (a<-aristas) a.drawIntoShapeRenderer(shape)
    shape.end()
    shape.begin(ShapeType.Circle)
    for (v<-vertices) v.drawIntoShapeRenderer(shape)
    shape.end()
  }

  def tocandoVertice(x1:Float, y1:Float):Vertice={
    for (v<-vertices){
      if (v.distance(x1,y1) <= 10){
        Gdx.app.log("DEBUG", "Tocando a " + v.etiqueta)
        return v
      }
    }
    return null
  }

}
