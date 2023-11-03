package ie.setu.breakdownassist.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.setu.breakdownassist.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "breakdowns.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<BreakdownModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class BreakdownJSONStore(private val context: Context) : BreakdownStore {

    var breakdowns = mutableListOf<BreakdownModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<BreakdownModel> {
        logAll()
        return breakdowns
    }

    override fun create(breakdown: BreakdownModel) {
        breakdown.id = generateRandomId()
        breakdowns.add(breakdown)
        serialize()
    }


    override fun update(breakdown: BreakdownModel) {
        val breakdownsList = findAll() as ArrayList<BreakdownModel>
        var foundBreakdown: BreakdownModel? = breakdownsList.find { p -> p.id == breakdown.id }
        if (foundBreakdown != null) {
            foundBreakdown.title = breakdown.title
            foundBreakdown.description = breakdown.description
            foundBreakdown.phone = breakdown.phone
            foundBreakdown.image = breakdown.image
            foundBreakdown.lat = breakdown.lat
            foundBreakdown.lng = breakdown.lng
            foundBreakdown.zoom = breakdown.zoom
        }
        serialize()
    }
    private fun serialize() {
        val jsonString = gsonBuilder.toJson(breakdowns, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        breakdowns = gsonBuilder.fromJson(jsonString, listType)
    }
    override fun delete(breakdown: BreakdownModel) {
        breakdowns.remove(breakdown)
        serialize()
    }

    private fun logAll() {
        breakdowns.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }

}