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
import dcel.DcelConstructor
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle
import math.min
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui._
import com.badlogic.gdx.scenes.scene2d.{Actor, Stage}

class MyGame extends ApplicationListener {

  var cam:OrthographicCamera = null
  var unprojectedVertex:Vector3 = new Vector3()
  var shape:ShapeRenderer = null
  var g:Grafo = null
  var dcg:DcelConstructor = null
  var constructor:ProtoArista = null
  var lastLabel:Int = 0
  var container:Table = null
  var run:Button = null
  var skin:Skin = null
  var window:Window = null
  var stage:Stage = null


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

    skin = new Skin(Gdx.files.internal("gdx_uiskin/uiskin.json"), Gdx.files.internal("gdx_uiskin/uiskin.png"))
    stage = new Stage (Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false)
    window = new Window("", skin.getStyle(classOf[WindowStyle]),"window")
    window.x = 670f
    window.y = 0f
    window.defaults().spaceBottom(10)
    window.row().fill().expandX()
    val next:Button = new TextButton("Siguiente", skin.getStyle(classOf[TextButtonStyle]), "button-sl")
    val salir:Button = new TextButton("Salir", skin.getStyle(classOf[TextButtonStyle]), "button-sl")
    window.add(next).fill(0,0)
    window.add(salir).fill(0,0)
    window.pack()
    stage.addActor(window)
    //stage.addActor(next)

    next.setClickListener(new ClickListener {
      def click(p1: Actor, p2: Float, p3: Float) {
        Gdx.app.log("NEXT", "Me pulsaron!")
      }
    })
    salir.setClickListener(new ClickListener {
      def click(p1: Actor, p2: Float, p3: Float) {
        Gdx.app.exit()
      }
    })

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

    stage.act(min(Gdx.graphics.getDeltaTime(), 1 / 30f))
    stage.draw()

  }
  override def dispose() {
    shape.dispose()
  }
  override def pause() {}
  override def resume() {}
  override def resize(x: Int, y: Int) {}
}
