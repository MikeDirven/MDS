package framework.engine.classes

import framework.engine.handlers.Application

class Route(
    val application: Application,
    val path: String = "",
    var requestHandler: MutableList<RequestHandler> = mutableListOf()
) {

}