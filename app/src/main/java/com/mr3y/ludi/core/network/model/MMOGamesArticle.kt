package com.mr3y.ludi.core.network.model

import com.mr3y.ludi.core.model.MarkupText
import com.mr3y.ludi.core.model.NewsArticle
import com.mr3y.ludi.core.model.Percent
import com.mr3y.ludi.core.model.Source
import com.mr3y.ludi.core.model.Title
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MMOGamesArticle(
    val id: Long,
    val title: String,
    @SerialName("short_description")
    val description: String,
    @SerialName("thumbnail")
    val thumbnailUrl: String,
    @SerialName("main_image")
    val image: String,
    @SerialName("article_content")
    val content: String,
    @SerialName("article_url")
    val articleUrl: String
) {
    fun toNewsArticle(): NewsArticle {
        return NewsArticle(
            id = id,
            title = Title.Plain(title),
            description = MarkupText(description),
            thumbnailUrl = thumbnailUrl,
            source = Source.MMOBomb,
            sourceLinkUrl = articleUrl,
            content = MarkupText(content),
            imageUrl = image,
            author = null,
            publicationDate = null
        )
    }
}

@Serializable
data class MMOGiveawayEntry(
    val id: Long,
    val title: String,
    @SerialName("keys_left")
    val keysLeftPercent: String,
    @SerialName("thumbnail")
    val thumbnailUrl: String,
    @SerialName("main_image")
    val imageUrl: String,
    @SerialName("short_description")
    val description: String,
    @SerialName("giveaway_url")
    val giveawayUrl: String
)

fun MMOGiveawayEntry.toCoreGiveawayEntry(): com.mr3y.ludi.core.model.MMOGiveawayEntry {
    return com.mr3y.ludi.core.model.MMOGiveawayEntry(
        id = id,
        title = title,
        keysLeftPercent = Percent(keysLeftPercent.substringBefore('%').toInt()),
        thumbnailUrl,
        imageUrl,
        description,
        giveawayUrl
    )
}
