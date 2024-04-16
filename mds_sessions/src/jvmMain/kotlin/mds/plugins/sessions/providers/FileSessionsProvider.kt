package mds.plugins.sessions.providers

import mds.engine.classes.HttpHeader
import mds.engine.pipelines.subPipelines.ResponsePipeline
import mds.plugins.sessions.enums.SameSite
import mds.plugins.sessions.exceptions.SessionExceptions
import mds.plugins.sessions.interfaces.FileSessionCookieConfig
import mds.plugins.sessions.interfaces.SessionProvider
import java.io.File
import java.io.FileFilter
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.cast
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

class FileSessionsProvider(override val sessionName: String, cookieConfig: FileSessionCookieConfig.() -> Unit) : SessionProvider,
    FileSessionCookieConfig {
    override var fileLocation: File = File("sessions")

    override lateinit var domain: String
    override lateinit var expires: Date
    override var httpOnly: Boolean = false
    override var maxAgeInSeconds: Int = 3600
    override var path: String = "/"
    override var sameSite: SameSite = SameSite.LAX
    override var secure: Boolean = false

    init {
        cookieConfig()

        if(!fileLocation.exists()) fileLocation.mkdirs()
    }

    fun clearSessions() {
        fileLocation.listFiles(
            FileFilter {
                it.extension == "session"
            }
        )?.forEach {
            it.delete()
        }
    }

    private fun deSerializeObject(stringValue: String) : Map<String, Any> = buildMap {
        val fieldValues = stringValue.substringAfterLast("{").substringAfterLast("}").split(",")
        fieldValues.forEach {

        }
    }

    override fun <T : Any> getSession(clazz: KClass<T>, uuid: UUID): T {
        val sessionFile: File = File(fileLocation, "${uuid}.session")
        if (sessionFile.exists()){
            val map = mutableMapOf<String, String>()
            sessionFile.readText().lines().forEach { line ->
                if(line.isNotBlank()){
                    val (key, value) = line.split("=")
                    map[key.trim()] = value.trim()
                }
            }

            return clazz.primaryConstructor?.let {
                it.call(*it.parameters.map {
                    it.type.jvmErasure.cast(map.getValue(it.name!!))
                }
                    .toTypedArray())
            } ?: throw SessionExceptions.SessionNotFound(uuid)
        } else {
            throw SessionExceptions.SessionNotFound(uuid)
        }
    }

    override fun <T : Any> getSessionOrNull(clazz: KClass<T>, uuid: UUID): T? {
        val sessionFile: File = File(fileLocation, "${uuid}.session")
        if (sessionFile.exists()){
            val map = mutableMapOf<String, String>()
            sessionFile.readText().lines().forEach { line ->
                if(line.isNotBlank()){
                    val (key, value) = line.split("=")
                    map[key.trim()] = value.trim()
                }
            }

            return clazz.primaryConstructor?.let {
                it.call(*it.parameters.map {
                    it.type
                    map.getValue(it.name!!)
                }.toTypedArray())
            }
        } else {
            return null
        }
    }

    private fun <T> serializeObject(obj: Any, clazz: T) : String = try {
        val stringBuilder: StringBuilder = StringBuilder()
        val fields = obj::class.declaredMemberProperties

        for (dataField in fields){
            val key = dataField.name
            val dataValue = dataField.getter.call(obj)

            dataValue?.apply {
                stringBuilder.append(serializeValue(this, key, clazz))
            }
        }

        "{${stringBuilder.toString()}}"
    } catch (e: Exception) {
        throw SessionExceptions.UnableToSerializeSession(clazz.toString())
    }

    private fun <T> serializeList(obj: List<*>, clazz: T) : String = try {
        val stringBuilder: StringBuilder = StringBuilder()

        obj.forEach { listValue ->
            listValue?.apply {
                stringBuilder.append(serializeValue(this, clazz = clazz))
            }
        }

        "[${stringBuilder.toString()}]"
    } catch (e: Exception) {
        throw SessionExceptions.UnableToSerializeSession(clazz.toString())
    }

    private fun <T> serializeValue(obj: Any, field: String? = null, clazz: T) : String = try {
        val fieldName = field?.let { "$it=" } ?: ""
        when(obj){
            is String -> "S#${fieldName}${obj.toString()},"
            is Int ->"I#${fieldName}${obj.toInt()},"
            is Double -> "N#${fieldName}${obj.toDouble()},"
            is Boolean -> "B#${fieldName}${obj.toString().toBoolean()},"
            is List<*> -> "L#${fieldName}${serializeList<T>(obj, clazz)},"
            else -> "O#${fieldName}${serializeObject<T>(obj, clazz)},"
        }
    } catch (e: Exception) {
        throw SessionExceptions.UnableToSerializeSession(clazz.toString())
    }

    override fun <T : Any> setSession(Clazz: KClass<T>, session: T): UUID {
        val uuid = UUID.randomUUID()
        val stringBuilder: StringBuilder = StringBuilder()
        val fields = Clazz.declaredMemberProperties

        for (field in fields){
            val key = field.name
            val value = field.get(session)

            value?.apply {
                stringBuilder.append(serializeValue(this, key, Clazz).trimEnd(',').plus("\n"))
            }
        }

        File(fileLocation, "${uuid}.session").apply {
            if(createNewFile()) writeText(
                stringBuilder.toString()
            )
        }

        return uuid
    }

    override fun setSessionUUID(call: ResponsePipeline, uuid: UUID) {
        val cookieBuilder: StringBuilder = java.lang.StringBuilder()
        cookieBuilder.append("${sessionName}=${uuid.toString()}")
        if(this::domain.isInitialized) cookieBuilder.append("; Domain=${this.domain}")
        if(this::expires.isInitialized) cookieBuilder.append("; expires=${this.expires.toInstant().toString()}")
        if(this.httpOnly) cookieBuilder.append("; HttpOnly")
        cookieBuilder.append("; Max-Age=${this.maxAgeInSeconds}")
        cookieBuilder.append("; Path=${this.path}")
        cookieBuilder.append("; SameSite=${this.sameSite.actual}")
        if(this.secure) cookieBuilder.append("; Secure")

        call.response.headers.add(
            HttpHeader(
                "Set-Cookie",
                cookieBuilder.toString()
            )
        )
    }
}