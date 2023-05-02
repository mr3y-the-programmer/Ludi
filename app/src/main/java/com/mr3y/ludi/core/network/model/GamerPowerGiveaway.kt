package com.mr3y.ludi.core.network.model

import com.mr3y.ludi.core.model.GamerPowerGiveawayEntryStatus
import com.mr3y.ludi.core.network.serialization.NotAvailableAsNullSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GamerPowerGiveawayEntry(
    val id: Long,
    val title: String,
    @Serializable(with = NotAvailableAsNullSerializer::class)
    val worth: String?,
    @SerialName("thumbnail")
    val thumbnailUrl: String,
    @SerialName("image")
    val imageUrl: String,
    val description: String,
    val instructions: String,
    @SerialName("open_giveaway_url")
    val giveawayUrl: String,
    @SerialName("published_date")
    val publishedDateTime: String,
    val type: String,
    val platforms: String,
    @Serializable(with = NotAvailableAsNullSerializer::class)
    @SerialName("end_date")
    val endDateTime: String?,
    val users: Int,
    val status: GiveawayEntryStatus,
    @SerialName("gamerpower_url")
    val gamerPowerUrl: String
)

enum class GiveawayEntryStatus { Active }

fun GamerPowerGiveawayEntry.toCoreGamerPowerGiveawayEntry(): com.mr3y.ludi.core.model.GamerPowerGiveawayEntry {
    return com.mr3y.ludi.core.model.GamerPowerGiveawayEntry(
        id = id,
        title = title,
        worthInUsDollar = worth?.substringAfter('$')?.toFloat(),
        thumbnailUrl = thumbnailUrl,
        imageUrl = imageUrl,
        description = description,
        instructions = instructions,
        giveawayUrl = giveawayUrl,
        publishedDateTime = publishedDateTime.toZonedDateTime(pattern = Pattern.ISO_UTC_DATE_TIME, isLenient = true),
        type = type,
        platforms = platforms.split(',').map { it.trim() },
        endDateTime = endDateTime?.toZonedDateTime(pattern = Pattern.ISO_UTC_DATE_TIME, isLenient = true),
        users = users,
        status = GamerPowerGiveawayEntryStatus.Active,
        gamerPowerUrl = gamerPowerUrl
    )
}
