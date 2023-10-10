package com.mr3y.ludi.shared.core.model

import java.time.ZonedDateTime

@JvmInline
value class Percent(val value: Int) {
    init {
        require(value in 0..100) { "Invalid Percentage value $value" }
    }
}

data class GiveawayEntry(
    val id: Long,
    val title: String,
    val worthInUsDollar: Float?,
    val thumbnailUrl: String,
    val imageUrl: String,
    val description: String,
    val instructions: String,
    val giveawayUrl: String,
    val publishedDateTime: ZonedDateTime?,
    val type: String,
    val platforms: List<String>,
    val endDateTime: ZonedDateTime?,
    val users: Int,
    val status: GiveawayEntryStatus,
    val gamerPowerUrl: String
)

enum class GiveawayEntryStatus { Active }
