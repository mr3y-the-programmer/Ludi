package com.mr3y.ludi.core.network.model

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
)
