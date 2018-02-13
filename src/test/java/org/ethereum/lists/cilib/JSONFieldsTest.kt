package org.ethereum.lists.cilib

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import org.junit.Test


class JSONFieldsTest {

    private fun String.parseArray() =
            (Parser().parse(reader()) as JsonArray<*>).map { it as JsonObject }

    @Test
    fun noErrorOnNoElements() {
        "[{}]".parseArray().checkFields(listOf(), listOf())
    }


    @Test(expected = IllegalArgumentException::class)
    fun errorOnMissingMandatory() {
        "[{}]".parseArray().checkFields(listOf("mandatory_missing"), listOf())
    }


    @Test
    fun noErrorWhenMandatoryIsThere() {
        """[{"mandatory":"yes"}]""".parseArray().checkFields(listOf("mandatory"), listOf())
    }


    @Test
    fun optionalIsOptional() {
        """[{"mandatory":"yes"}]""".parseArray().checkFields(listOf("mandatory"), listOf("optional"))
    }

    @Test(expected = IllegalArgumentException::class)
    fun errorOnOneOKAndOneMissing() {
        """[{"mandatory":"yes"},{}]""".parseArray().checkFields(listOf("mandatory"), listOf())
    }

    @Test(expected = IllegalArgumentException::class)
    fun emptyFieldsCountAsMissing() {
        """[{"mandatory":""}]""".parseArray().checkFields(listOf("mandatory"), listOf())
    }


}