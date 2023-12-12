package mds.engine.handlers

import mds.engine.classes.ApplicationHook
import mds.engine.classes.HttpCall
import mds.engine.classes.HttpRequest
import mds.engine.classes.HttpResponse
import mds.engine.enums.Hook
import mds.engine.enums.RequestMethods
import mds.engine.interfaces.*
import mds.engine.logging.MdsLogging
import mds.engine.plugins.PluginKey
import mds.engine.logging.Tags
import mds.engine.pipelines.Pipeline
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket
import kotlin.reflect.KClass

open class Application() : Thread("Mds_Engine_Thread"), MdsEngineRequests, MdsEngineHooks, MdsEnginePlugins, MdsEngineExceptions, MdsLogging {
    override val logger: Logger = LoggerFactory.getLogger("Mds_Logger")

    override val exceptionsToCatch: MutableMap<KClass<Throwable>, ExceptionHandler> = mutableMapOf()

    override val applicationHooks: MutableList<ApplicationHook> = mutableListOf()

    override val applicationPlugins: MutableMap<PluginKey<*>, Any> = mutableMapOf()

    val routing: Routing = Routing(this)

    init {
        initLogging()
    }
}