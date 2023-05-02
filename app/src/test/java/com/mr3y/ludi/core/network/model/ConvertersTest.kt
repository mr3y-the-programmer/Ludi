package com.mr3y.ludi.core.network.model

import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
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

    @Test
    fun whenParsingISOUTCDateTime_TheResultIsValidZonedDateTime() {
        val dateTimeStringsBeforeParsing = listOf(
            "2023-03-24 15:16:31",
            "2021-09-16T15:16:31"
        )
        val expected = listOf(
            ZonedDateTime.of(LocalDate.of(2023, 3, 24), LocalTime.of(15, 16, 31), ZoneId.systemDefault()),
            ZonedDateTime.of(LocalDate.of(2021, 9, 16), LocalTime.of(15, 16, 31), ZoneId.systemDefault())
        )
        expectThat(dateTimeStringsBeforeParsing.map { it.toZonedDateTime(pattern = Pattern.ISO_UTC_DATE_TIME, isLenient = true) }).isEqualTo(expected)
    }

    @Test
    fun whenParsingISO8601DateString_TheResultIsValidZonedDateTime() {
        val dateStringBeforeParsing = "2023-03-20"
        val expected = ZonedDateTime.of(2023, 3, 20, 0, 0, 0, 0, ZoneId.systemDefault())
        expectThat(dateStringBeforeParsing.toZonedDate()).isEqualTo(expected)
    }
}
