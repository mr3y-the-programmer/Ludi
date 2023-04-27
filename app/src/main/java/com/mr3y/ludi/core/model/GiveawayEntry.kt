package com.mr3y.ludi.core.model

import java.time.ZonedDateTime

sealed interface GiveawayEntry {
    val id: Long
    val title: String
}

data class MMOGiveawayEntry(
    override val id: Long,
    override val title: String,
    val keysLeftPercent: Percent,
    val thumbnailUrl: String,
    val imageUrl: String,
    val description: String,
    val giveawayUrl: String
): GiveawayEntry

@JvmInline
value class Percent(val value: Int) {
    init {
        require(value in 0..100) { "Invalid Percentage value $value" }
    }
}

data class GamerPowerGiveawayEntry(
    override val id: Long,
    override val title: String,
    val worthInUsDollar: Float?,
    val thumbnailUrl: String,
    val imageUrl: String,
    val description: String,
    val instructions: String,
    val giveawayUrl: String,
    val publishedDateTime: ZonedDateTime,
    val type: String,
    val platforms: List<String>,
    val endDateTime: ZonedDateTime?,
    val users: Int,
    val status: GamerPowerGiveawayEntryStatus,
    val gamerPowerUrl: String
): GiveawayEntry

enum class GamerPowerGiveawayEntryStatus { Active }