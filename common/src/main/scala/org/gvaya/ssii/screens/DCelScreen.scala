package org.gvaya.ssii.screens

import com.badlogic.gdx.{Gdx, Screen}
import org.gvaya.ssii.grafo.{Util, ProtoArista, Grafo}
import com.badlogic.gdx.graphics.{GL10, OrthographicCamera}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.ui._
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.math.Vector3
import org.gvaya.ssii.MyGame
import org.gvaya.ssii.dcel.DcelConstructor

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

  var dcg: DcelConstructor = null
  var constructor: ProtoArista = null
  var lastLabel: Int = 0
  var container: Table = null
  var run: Button = null
  var skin: Skin = null
  var window: Window = null
  var stage: Stage = null
  var dcel: DcelConstructor = null

  /**
   *
   * @param p1
   */
  def render(p1: Float) {
    Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f)
    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT)

    cam.update()
    cam.apply(Gdx.gl10) //TODO: buscar que hace esta linea!
    dcel.drawIntoShapeRenderer(shape)
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
    window.setX(670f)
    window.setY(0f)
    window.defaults().spaceBottom(10)
    window.row().fill().expandX()
    val next: Button = new TextButton("Siguiente", skin)
    val salir: Button = new TextButton("Salir", skin)
    window.add(next).fill(0, 0)
    window.add(salir).fill(0, 0)
    dcel = new DcelConstructor(game.g)
    dcel.generar()
  }

  def hide() {}

  def pause() {}

  def resume() {}

  def dispose() {}
}
