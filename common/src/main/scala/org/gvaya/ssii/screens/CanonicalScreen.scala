package org.gvaya.ssii.screens

import com.badlogic.gdx.{Gdx, Screen}
import com.badlogic.gdx.graphics.{GL10, OrthographicCamera}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.ui._
import com.badlogic.gdx.scenes.scene2d.{InputEvent, Stage}
import com.badlogic.gdx.math.Vector3
import org.gvaya.ssii.MyGame
import org.gvaya.ssii.dcel._
import org.gvaya.ssii.grafo.Util
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener
import math.min


/**
 * Created with IntelliJ IDEA.
 * User: gvaya
 * Date: 31/08/12
 * Time: 17:19
 * Screen para la ordenacion canonica
 * @param game Clase principal
 */
class CanonicalScreen(val game: MyGame) extends Screen {
  var cam: OrthographicCamera = null
  var unprojectedVertex: Vector3 = new Vector3()
  var shape: ShapeRenderer = null

  var container: Table = null
  var run: Button = null
  var skin: Skin = null
  var window: Window = null
  var stage: Stage = null
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

    stage.act(min(delta, 1 / 50f))
    stage.draw()
  }

  def resize(p1: Int, p2: Int) {}

  def show() {
    Gdx.app.log("Info", "Inicializando pantalla Canonical ordering")
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
    val reset: Button = new TextButton("Volver", skin)
    window.add(next).fill(0, 0)
    window.add(reset).fill(0, 0)
    window.add(salir).fill(0, 0)

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
	  true
        }
      }
    })

    reset.addListener(new ActorGestureListener {
      override def touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
        event.cancel()
        game.d = null // eliminamos el dcel y volvemos a la pantalla anterior
        game.setScreen(new DCelScreen(game))
      }
    })

    window.pack()
    stage.addActor(window)
    Gdx.input.setInputProcessor(stage)

    try
    {
      loading = false //hemos terminado de cargar, podemos usar los botones
    }
    catch{
      case e:DcelConstructorException => {
        Gdx.app.exit()
      }

    }

  }

  def hide() {}

  def pause() {}

  def resume() {}

  def dispose() {}
}
