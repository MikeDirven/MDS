package mds.engine.classes

import mds.engine.handlers.Application

class Route(
    val application: Application,
    val path: String = "",
    var requestHandler: MutableList<RequestHandler> = mutableListOf()
) {

}