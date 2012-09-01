package org.gvaya.ssii.screens

import com.badlogic.gdx.{Gdx, Screen}
import com.badlogic.gdx.scenes.scene2d.{InputEvent, Stage}
import org.gvaya.ssii.grafo._
import com.badlogic.gdx.graphics.{GL10, OrthographicCamera}
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.ui._
import com.badlogic.gdx.scenes.scene2d.utils.{DragListener, ActorGestureListener}
import com.badlogic.gdx.Input.Buttons
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import math.min
import org.gvaya.ssii.MyGame
import org.gvaya.ssii.dcel.DcelConstructor

/**
 * User: guillermo
 * Date: 5/06/12
 * Time: 9:29
 */

class BaseScreen(var game: MyGame) extends Screen {
  var cam: OrthographicCamera = null
  var unprojectedVertex: Vector3 = new Vector3()
  var shape: ShapeRenderer = null
  var dcg: DcelConstructor = null
  var constructor: ProtoArista = null
  var container: Table = null
  var run: Button = null
  var skin: Skin = null
  var window: Window = null
  var stage: Stage = null


  def añadirSiCompleta() {
    if (constructor != null && constructor.estaCompleta) {
      game.g.addArista(constructor.toArista())
      //constructor.finalize()
      constructor = null
    }
  }

  override def show() {
    Gdx.app.log("Info", "Inicializando pantalla Editor")
    cam = new OrthographicCamera(Gdx.graphics.getWidth, Gdx.graphics.getHeight)
    cam.setToOrtho(false, Util.WIDTH, Util.HEIGHT)
    shape = new ShapeRenderer()
    shape.setProjectionMatrix(cam.combined)
    Gdx.app.log("Info", "Fin de lainicializacion de la pantalla")
    skin = new Skin(Gdx.files.internal("gdx_uiskin/uiskin.json"))
    stage = new Stage(Gdx.graphics.getWidth, Gdx.graphics.getHeight, false)
    window = new Window("", skin)
    window.setX(670f)
    window.setY(0f)
    window.defaults().spaceBottom(10)
    window.row().fill().expandX()
    val next: Button = new TextButton("Siguiente", skin)
    val salir: Button = new TextButton("Salir", skin)
    window.add(next).fill(0, 0)
    window.add(salir).fill(0, 0)



    window.addListener(new ActorGestureListener {
      override def touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
        event.cancel()
        Gdx.app.log("Window", "Ventana pulsada")

      }
    })

    next.addListener(new ActorGestureListener {
      override def touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
        event.cancel()
        Gdx.app.log("NEXT", "Vertices: " + game.g.vertices.foldLeft("")((str:String, v:Vertice)=> str + ", " + v.etiqueta))
        Gdx.app.log("NEXT", "Aristas: " + game.g.aristas.foldLeft("")((str:String, v:Arista)=> str + ", " + v.etiqueta))
        game.setScreen(new DCelScreen(game))
      }
    })
    salir.addListener(new ActorGestureListener {
      override def touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
        event.cancel()
        Gdx.app.exit()
      }
    })

    stage.addListener(new DragListener {

      override def touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean = {
        unprojectedVertex.set(x, y, 0)
        //cam.unproject(unprojectedVertex)
        event.cancel()
        //Gdx.app.log("Stage", "Me pulsaron!")
        var v: Vertice = null

        if (button == Buttons.LEFT) {
          if (constructor == null) //solo puede comenzar una arista si pulsamos con el izdo.
            constructor = new ProtoArista

          v = game.g.tocandoVertice(unprojectedVertex.x, unprojectedVertex.y)
          if (v != null) {
            //habia vertice donde pulsamos? lo añadimos
            constructor.addVertice(v)
            añadirSiCompleta()
          }
          else if (constructor.estaVacia) {
            // si no habia vertice y no se ha comenzado una arista, creamos el nodo
            constructor.addVertice(new Vertice(unprojectedVertex.x, unprojectedVertex.y, Util.genLabel()))
          }
          else {
            //e.o.c. añadimos un nodo mas
            constructor.addNodo(unprojectedVertex.x, unprojectedVertex.y)
          }
        }
        else //sera el derecho, entonces
        {
          v = game.g.tocandoVertice(unprojectedVertex.x, unprojectedVertex.y) //toca vertice?
          if (v == null)
            v = new Vertice(unprojectedVertex.x, unprojectedVertex.y, Util.genLabel())

          if (constructor != null) {
            if (constructor.addVertice(v))
              game.g.addVertice(v)
            añadirSiCompleta()
          }
          else
          {
            game.g.addVertice(v) //si no estamos construyendo una arista, se añade el nodo
          }
        }
        //mantenemos la funcionalidad, por si acaso
        super.touchDown(event, x, y, pointer, button)
      }

      override def touchDragged(event: InputEvent, x: Float, y: Float, pointer: Int) {
        //Gdx.app.log("Stage", "Me tiran!")
        var v: Vertice = null
        unprojectedVertex.set(x, y, 0)
        //cam.unproject(unprojectedVertex)
        if (pointer == Buttons.LEFT && constructor != null) //no puede iniciar una arista, primero se crea con touchDown
        {
          v = game.g.tocandoVertice(unprojectedVertex.x, unprojectedVertex.y)
          if (v != null) {
            //habia vertice donde pulsamos? lo añadimos
            constructor.addVertice(v)
            añadirSiCompleta()
          }
          else {
            //e.o.c. añadimos un nodo mas
            constructor.addNodo(unprojectedVertex.x, unprojectedVertex.y)
          }
        }
        super.touchDragged(event, x, y, pointer)
      }

      override def touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
        //Gdx.app.log("Stage", "Me despulsaron!")

        unprojectedVertex.set(x, y, 0)
        //cam.unproject(unprojectedVertex)
        super.touchUp(event, x, y, pointer, button)
      }
    })

    // TODO: añadir un boton para finalizar y la opcion de pulsar escape para finalizar tambien
    window.pack()
    stage.addActor(window)

    Gdx.input.setInputProcessor(stage)
    // super.show()
  }
  override def render(delta: Float) {

    Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f)
    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT)

    cam.update()
    cam.apply(Gdx.gl10) //TODO: buscar que hace esta linea!

    if (constructor != null && constructor.estaCompleta) {
      game.g.addArista(constructor.toArista())
      //constructor.finalize()
      constructor = null
    }

    // Finalmente dibujamos el grafo
    game.g.drawIntoShapeRenderer(shape)

    //y el constructor de aristas si existe
    if (constructor != null && !constructor.estaVacia) {
      shape.begin(ShapeType.Line)
      constructor.drawIntoShapeRenderer(shape)
      shape.end()
    }

    stage.act(min(delta, 1 / 50f))
    stage.draw()

    //super.render(delta:Float)
  }

  override def dispose() {
    shape.dispose()
    //super.dispose()
  }

  override def resize(p1: Int, p2: Int) {

  }

  def hide() {

  }

  def pause() {

  }

  def resume() {

  }
}
