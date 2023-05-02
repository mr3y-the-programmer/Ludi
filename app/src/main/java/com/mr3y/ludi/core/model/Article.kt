package com.mr3y.ludi.core.model

sealed interface Article {
    val title: Title
}

sealed interface Title {
    val text: String
    data class Markup(override val text: String) : Title
    data class Plain(override val text: String) : Title
}
