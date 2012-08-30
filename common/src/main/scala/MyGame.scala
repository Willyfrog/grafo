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
import org.gvaya.ssii.screens.BaseScreen

class MyGame extends Game {

  def needsGL20():Boolean = false

  override def create() {
    setScreen(new BaseScreen())
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
