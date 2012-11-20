package org.gvaya.ssii.screens

import com.badlogic.gdx.{Gdx, Screen}
import org.gvaya.ssii.grafo._
import com.badlogic.gdx.graphics.{GL10, OrthographicCamera}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.ui._
import com.badlogic.gdx.scenes.scene2d.{InputEvent, Stage}
import com.badlogic.gdx.math.Vector3
import org.gvaya.ssii.MyGame
import org.gvaya.ssii.dcel._
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener
import math.min


/**
 * Created with IntelliJ IDEA.
 * User: gvaya
 * Date: 31/08/12
 * Time: 17:19
 * Crea una screen para la generación del grafo DCel a partir del viejo grafo
 * @param game Clase principal
 */
class DCelScreen(val game: MyGame) extends Screen {

  var cam: OrthographicCamera = null
  var unprojectedVertex: Vector3 = new Vector3()
  var shape: ShapeRenderer = null


  var constructor: ProtoArista = null
  var lastLabel: Int = 0
  var container: Table = null
  var run: Button = null
  var skin: Skin = null
  var window: Window = null
  var stage: Stage = null
  var dcel: DcelConstructor = null
  var triangular: DcelConstructor = null
  var loading: Boolean = true

  /**
   *
   * @param delta
   */
  def render(delta: Float) {
    Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f)
    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT)

    cam.update()
    cam.apply(Gdx.gl10) //TODO: buscar que hace esta linea!
    dcel.drawIntoShapeRenderer(shape)

    stage.act(min(delta, 1 / 50f))
    stage.draw()
  }

  def resize(p1: Int, p2: Int) {}

  def show() {
    Gdx.app.log("Info", "Inicializando pantalla Dcel")
    cam = new OrthographicCamera(Gdx.graphics.getWidth, Gdx.graphics.getHeight)
    cam.setToOrtho(false, Util.WIDTH, Util.HEIGHT)
    shape = new ShapeRenderer()
    shape.setProjectionMatrix(cam.combined)
    Gdx.app.log("Info", "Fin de lainicializacion de la pantalla")
    skin = new Skin(Gdx.files.internal("gdx_uiskin/uiskin.json"))
    stage = new Stage(Gdx.graphics.getWidth, Gdx.graphics.getHeight, false)
    window = new Window("", skin)
    window.setX(630f)
    window.setY(0f)
    window.defaults().spaceBottom(10)
    window.row().fill().expandX()
    val next: Button = new TextButton("Siguiente", skin)
    val salir: Button = new TextButton("Salir", skin)
    val reset: Button = new TextButton("Reiniciar", skin)
    window.add(next).fill(0, 0)
    window.add(salir).fill(0, 0)
    window.add(reset).fill(0, 0)

    window.addListener(new ActorGestureListener {
      override def touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
        event.cancel()
      }
    })

    salir.addListener(new ActorGestureListener {
      override def touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
        event.cancel()
        Gdx.app.exit()
      }
    })

    next.addListener(new ActorGestureListener {
      override def touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
        event.cancel()
        if (!loading){
          if (!dcel.todasTriangulares)
          {
            dcel.addTriangulo
          }
          else
            Gdx.app.log("Next", "ya son todas triangulares")
        }
      }
    })

    reset.addListener(new ActorGestureListener {
      override def touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
        event.cancel()
        dcel = null
        game.setScreen(new BaseScreen(game))
      }
    })

    window.pack()
    stage.addActor(window)
    Gdx.input.setInputProcessor(stage)

    dcel = new DcelConstructor(game.g)
    try
    {
      dcel.generar()
      loading = false //hemos terminado de cargar, podemos usar los botones
    }
    catch{
      case e:DcelConstructorException => {
        Gdx.app.log("DCEL", "Error al generar dcel")
        Gdx.app.log("DUMP", "Vertices: " + dcel.lvertices.foldLeft("")((str:String, v:Vertex)=> str + ", " + v.label))
        Gdx.app.log("DUMP", "Aristas: " + dcel.laristas.foldLeft("")((str:String, e:Edge)=> str + ", " + e.label))
        Gdx.app.log("DUMP", "Caras: " + dcel.lcaras.foldLeft("")((str:String, f:Face)=> str + ", " + f.label))
        //Gdx.app.exit()
      }

    }

  }

  def hide() {}

  def pause() {}

  def resume() {}

  def dispose() {}
}
