package org.gvaya.ssii

import collection.mutable.ArrayBuffer
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.graphics.VertexAttributes.Usage
import com.badlogic.gdx.{Gdx, ApplicationListener}
import com.badlogic.gdx.graphics._
import com.badlogic.gdx.Input.Buttons
import glutils.ShapeRenderer
import glutils.ShapeRenderer.ShapeType
import grafo.{Nodo, ProtoArista, Vertice, Grafo}

class MyGame extends ApplicationListener {

  var cam:OrthographicCamera = null
  var unprojectedVertex:Vector3 = new Vector3()
  var shape:ShapeRenderer = null
  var g:Grafo = null
  var constructor:ProtoArista = null
  var lastLabel:Int = 0

  def needsGL20():Boolean = false

  def genLabel():String={
    lastLabel+=1
    if (lastLabel<=26)
      return 'a'.to('z')(lastLabel).toString
    else if (lastLabel<=52)
      return 'A'.to('Z')(lastLabel-26).toString
    else
      return (lastLabel-52).toString
  }

  override def create() {
    Gdx.app.log("Info", "Inicializando aplicacion")
    cam = new OrthographicCamera(Gdx.graphics.getWidth, Gdx.graphics.getHeight)
    cam.setToOrtho(false,480,320)
    g = new Grafo()
    shape = new ShapeRenderer()
    shape.setProjectionMatrix(cam.combined)
    Gdx.app.log("Info", "Fin de lainicializacion de la aplicacion")
  }
  override def render() {
    var v:Vertice = null
    Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f)
    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT)

    cam.update()
    cam.apply(Gdx.gl10) //TODO: buscar que hace esta linea!

    if (Gdx.input.isButtonPressed(Buttons.LEFT)){
      unprojectedVertex.set(Gdx.input.getX(),Gdx.input.getY(), 0 )
      cam.unproject(unprojectedVertex)

      if (constructor==null) //solo puede comenzar una arista si pulsamos con el izdo.
        constructor= new ProtoArista

      v = g.tocandoVertice(unprojectedVertex.x, unprojectedVertex.y)
      if (v!=null){
        constructor.addVertice(v)
      }
      else if (constructor.estaVacia){
        constructor.addVertice(new Vertice(unprojectedVertex.x, unprojectedVertex.y, genLabel()))
      }
      else{
        constructor.addNodo(unprojectedVertex.x, unprojectedVertex.y)
      }
    }

    if (Gdx.input.isButtonPressed(Buttons.RIGHT)){
      v = null //por si acaso acarreamos de antes
      unprojectedVertex.set(Gdx.input.getX(),Gdx.input.getY(), 0 )
      cam.unproject(unprojectedVertex)
      v = g.tocandoVertice(unprojectedVertex.x, unprojectedVertex.y) //toca vertice?
      if (v==null)
        v = new Vertice(unprojectedVertex.x, unprojectedVertex.y, "b")

      if (constructor!=null)
      {
        if (constructor.addVertice(v))
          g.addVertice(v)
      }
      else
          g.addVertice(v) //si no estamos construyendo una arista, se añade el nodo
    }

    if (constructor!=null && constructor.estaCompleta)
    {
      g.addArista(constructor.toArista())
      //constructor.finalize()
      constructor = null
    }

    // Finalmente dibujamos el grafo
    g.drawIntoShapeRenderer(shape)

    //y el constructor de aristas si existe
    if (constructor!=null && !constructor.estaVacia){
      shape.begin(ShapeType.Line)
      constructor.drawIntoShapeRenderer(shape)
      shape.end()
    }


  }
  override def dispose() {
    shape.dispose()
  }
  override def pause() {}
  override def resume() {}
  override def resize(x: Int, y: Int) {}
}
