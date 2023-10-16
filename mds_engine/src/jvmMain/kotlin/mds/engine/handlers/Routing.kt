package mds.engine.handlers

import mds.engine.classes.Route

class Routing(val application: Application) {
    val routes: MutableList<Route> = mutableListOf()
}