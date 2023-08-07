package framework.engine.handlers

import framework.engine.classes.Route

class Routing(val application: Application) {
    val routes: MutableList<Route> = mutableListOf()
}