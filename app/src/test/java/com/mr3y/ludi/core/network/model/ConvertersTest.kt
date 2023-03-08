package com.mr3y.ludi.core.network.model

import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.time.ZonedDateTime

class ConvertersTest {

    @Test
    fun whenParsingRFC1123DateTimeStrings_TheResultIsValidZonedDateTime() {
        val dateTimeStringsBeforeParsing = listOf(
            "Thu, 02 Mar 2023 03:00:00 -0800",
            "Thu, 2 Mar 2023 01:30:02 +0000"
        )
        val expected = listOf(
            ZonedDateTime.parse("2023-03-02T03:00-08:00"),
            ZonedDateTime.parse("2023-03-02T01:30:02Z")
        )
        expectThat(dateTimeStringsBeforeParsing.map(String::toZonedDateTime)).isEqualTo(expected)
    }
}