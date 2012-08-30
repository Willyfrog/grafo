package org.gvaya.ssii

import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.graphics._
import com.badlogic.gdx.Input.Buttons
import glutils.ShapeRenderer
import glutils.ShapeRenderer.ShapeType
import dcel.DcelConstructor
import com.badlogic.gdx.scenes.scene2d.utils.{DragListener, ActorGestureListener}
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle
import com.badlogic.gdx.Input.Buttons
import math.min
import com.badlogic.gdx.scenes.scene2d.ui._
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.{Actor, Stage, InputEvent}
import com.badlogic.gdx.{Game, Gdx}
import grafo._

class MyGame extends Game {

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
      'a'.to('z')(lastLabel).toString
    else if (lastLabel<=52)
      'A'.to('Z')(lastLabel-26).toString
    else
      (lastLabel-52).toString
  }

  def añadirSiCompleta()
  {
    if (constructor!=null && constructor.estaCompleta)
    {
      g.addArista(constructor.toArista())
      //constructor.finalize()
      constructor = null
    }
  }

  override def create() {
    Gdx.app.log("Info", "Inicializando aplicacion")
    cam = new OrthographicCamera(Gdx.graphics.getWidth, Gdx.graphics.getHeight)
    cam.setToOrtho(false,Util.WIDTH,Util.HEIGHT)
    g = new Grafo()
    shape = new ShapeRenderer()
    shape.setProjectionMatrix(cam.combined)
    Gdx.app.log("Info", "Fin de lainicializacion de la aplicacion")
    skin = new Skin(Gdx.files.internal("gdx_uiskin/uiskin.json"))
    stage = new Stage (Gdx.graphics.getWidth, Gdx.graphics.getHeight, false)
    window = new Window("", skin)
    window.setX(670f)
    window.setY(0f)
    window.defaults().spaceBottom(10)
    window.row().fill().expandX()
    val next:Button = new TextButton("Siguiente", skin)
    val salir:Button = new TextButton("Salir", skin)
    window.add(next).fill(0,0)
    window.add(salir).fill(0,0)



    window.addListener(new ActorGestureListener{
      override def touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button:Int){
        event.cancel()
        Gdx.app.log("Window", "Ventana pulsada")

      }
    })

    next.addListener(new ActorGestureListener {
      override def touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button:Int) {
        event.cancel()
        Gdx.app.log("NEXT", "Me pulsaron!")
      }
    })
    salir.addListener(new ActorGestureListener {
      override def touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button:Int) {
        event.cancel()
        Gdx.app.exit()
      }
    })

    stage.addListener(new DragListener {

      override def touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button:Int):Boolean ={
        unprojectedVertex.set(x,y, 0 )
        //cam.unproject(unprojectedVertex)
        event.cancel()
        //Gdx.app.log("Stage", "Me pulsaron!")
        var v:Vertice = null

        if (button == Buttons.LEFT)
        {
          if (constructor==null) //solo puede comenzar una arista si pulsamos con el izdo.
            constructor= new ProtoArista

          v = g.tocandoVertice(unprojectedVertex.x, unprojectedVertex.y)
          if (v!=null){ //habia vertice donde pulsamos? lo añadimos
            constructor.addVertice(v)
            añadirSiCompleta()
          }
          else if (constructor.estaVacia){ // si no habia vertice y no se ha comenzado una arista, creamos el nodo
            constructor.addVertice(new Vertice(unprojectedVertex.x, unprojectedVertex.y, genLabel()))
          }
          else{ //e.o.c. añadimos un nodo mas
            constructor.addNodo(unprojectedVertex.x, unprojectedVertex.y)
          }
        }
        else //sera el derecho, entonces
        {
          v = g.tocandoVertice(unprojectedVertex.x, unprojectedVertex.y) //toca vertice?
          if (v==null)
            v = new Vertice(unprojectedVertex.x, unprojectedVertex.y, genLabel())

          if (constructor!=null)
          {
            if (constructor.addVertice(v))
              g.addVertice(v)
              añadirSiCompleta()
          }
          else
            g.addVertice(v) //si no estamos construyendo una arista, se añade el nodo
        }
        //mantenemos la funcionalidad, por si acaso
        super.touchDown(event, x, y, pointer, button)
      }

      override def touchDragged(event: InputEvent, x: Float, y: Float, pointer: Int) {
        //Gdx.app.log("Stage", "Me tiran!")
        var v:Vertice = null
        unprojectedVertex.set(x,y, 0 )
        //cam.unproject(unprojectedVertex)
        if (pointer == Buttons.LEFT && constructor!=null) //no puede iniciar una arista, primero se crea con touchDown
        {
          v = g.tocandoVertice(unprojectedVertex.x, unprojectedVertex.y)
          if (v!=null){ //habia vertice donde pulsamos? lo añadimos
            constructor.addVertice(v)
            añadirSiCompleta()
          }
          else{ //e.o.c. añadimos un nodo mas
            constructor.addNodo(unprojectedVertex.x, unprojectedVertex.y)
          }
        }
        super.touchDragged(event, x, y, pointer)
      }

      override def touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
        //Gdx.app.log("Stage", "Me despulsaron!")

        unprojectedVertex.set(x,y, 0 )
        //cam.unproject(unprojectedVertex)
        super.touchUp(event, x, y, pointer, button)
      }
    })

    // TODO: añadir un boton para finalizar y la opcion de pulsar escape para finalizar tambien
    window.pack()
    stage.addActor(window)

    Gdx.input.setInputProcessor(stage)

  }
  override def render() {

    Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f)
    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT)

    cam.update()
    cam.apply(Gdx.gl10) //TODO: buscar que hace esta linea!


    /*if (Gdx.input.isButtonPressed(Buttons.LEFT)){
      unprojectedVertex.set(Gdx.input.getX,Gdx.input.getY, 0 )
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
      unprojectedVertex.set(Gdx.input.getX,Gdx.input.getY, 0 )
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
    } */

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

    stage.act(min(Gdx.graphics.getDeltaTime, 1 / 50f))
    stage.draw()

  }
  override def dispose() {
    shape.dispose()
  }
  override def pause() {}
  override def resume() {}
  override def resize(x: Int, y: Int) {}
}
