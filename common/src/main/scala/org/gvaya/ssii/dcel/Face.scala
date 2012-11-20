package org.gvaya.ssii.dcel

import collection.mutable.ArrayBuffer

/**
 * Created with IntelliJ IDEA.
 * User: gvaya
 * Date: 28/05/12
 * Time: 23:30
 * To change this template use File | Settings | File Templates.
 */

class Face(val label: String, val arista: Edge) {
  var triang = false

  def aristas:List[Edge] = {
    var a1:Edge = arista.ccwd
    var res:ArrayBuffer[Edge] = new ArrayBuffer[Edge]()
    res.append(arista)
    while (a1.label != arista.label){
      res.append(a1)
      a1 = a1.ccwd
    }
    res.toList //Devolvemos una lista inmutable
  }

  def vertices:List[Vertex] = aristas.map(_.destiny)

  def esTriangular:Boolean={
    if (!triang){
      if (aristas.length<=3)
        triang = true //guardamos el resultado
    }
    triang
  }

}
