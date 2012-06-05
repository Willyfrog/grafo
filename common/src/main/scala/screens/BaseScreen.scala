package screens

import com.badlogic.gdx.Screen
import com.badlogic.gdx.scenes.scene2d.Stage
import grafo.Util

/**
 * User: guillermo
 * Date: 5/06/12
 * Time: 9:29
 */

abstract class BaseScreen extends Screen{
  var stage = new Stage(Util.WIDTH, Util.HEIGHT, false)

}
