package org.gvaya.ssii

import com.badlogic.gdx.backends.lwjgl.LwjglApplication

import com.badlogic.gdx._
import com.badlogic.gdx.graphics.GL20
import grafo.Util

object Main {
  def main(args: Array[String]): Unit = {
    new LwjglApplication(new MyGame(), "Grafo", Util.WIDTH, Util.HEIGHT, false)
  }
}
