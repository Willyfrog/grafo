package org.gvaya.ssii

import com.badlogic.gdx.Game
import grafo._
import org.gvaya.ssii.screens.BaseScreen
import org.gvaya.ssii.dcel.DcelConstructor
import org.gvaya.ssii.canograph.CGrafo

class MyGame extends Game {
  var g: Grafo = null //grafo que pasaremos entre screens
  var d: DcelConstructor = null
  var c: CGrafo = null

  def needsGL20(): Boolean = false

  override def create() {
    g = new Grafo()
    setScreen(new BaseScreen(this))
  }

  override def render() {
    super.render()

  }

  override def dispose() {

  }

  override def pause() {}

  override def resume() {}

  override def resize(x: Int, y: Int) {}
}
