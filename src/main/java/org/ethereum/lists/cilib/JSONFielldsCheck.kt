package org.ethereum.lists.cilib

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import java.io.File

fun File.toJSONArray() = (Parser().parse(this.reader()) as JsonArray<*>).map { it as JsonObject }

fun File.checkFields(mandatoryFields: List<String>, optionalFields: List<String> = listOf()) {
    toJSONArray().checkFields(mandatoryFields, optionalFields)
}

fun List<JsonObject>.checkFields(mandatoryFields: List<String>, optionalFields: List<String>) {
    forEach { jsonObject ->
        if (!jsonObject.keys.containsAll(mandatoryFields)) {
            throw IllegalArgumentException("$jsonObject does not contain " + mandatoryFields.minus(jsonObject.keys))
        }

        mandatoryFields.forEach {
            if (jsonObject[it] is String && jsonObject.string(it)?.isBlank() == true) {
                throw IllegalArgumentException("$jsonObject has blank value for $it")
            }
        }


        val unknownFields = jsonObject.keys.minus(mandatoryFields.plus(optionalFields))
        if (unknownFields.isNotEmpty()) {
            throw IllegalArgumentException("$jsonObject contains unknown " + unknownFields)
        }

    }

}


fun List<JsonObject>.copyFields(fields: List<String>): JsonArray<JsonObject> {
    val minimalJSONArray = JsonArray<JsonObject>()
    forEach { jsonObject ->
        val minimalJsonObject = JsonObject()
        fields.forEach {
            minimalJsonObject[it] = jsonObject[it]
        }
        minimalJSONArray.add(minimalJsonObject)
    }
    return minimalJSONArray
}
