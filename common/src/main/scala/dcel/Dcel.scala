package dcel

import collection.mutable.ArrayBuffer

/**
 * Created with IntelliJ IDEA.
 * User: gvaya
 * Date: 28/05/12
 * Time: 23:30
 * To change this template use File | Settings | File Templates.
 */

abstract class Dcel { //TODO: quitar abstract, es para que compile
  var lvertices:ArrayBuffer[Vertex]
  var laristas:Array[Edge]
  var lcaras:Array[Face]
  def add_vertice(v:Vertex){
    lvertices.append(v)
  }

}
