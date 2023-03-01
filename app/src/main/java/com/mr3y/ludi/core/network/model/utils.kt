package com.mr3y.ludi.core.network.model

internal fun String?.ignoreIfEmptyOrNull(): String? {
    return takeIf { this != null && this.isNotEmpty() }
}
