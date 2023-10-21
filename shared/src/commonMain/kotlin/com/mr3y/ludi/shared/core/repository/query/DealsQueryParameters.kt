package com.mr3y.ludi.shared.core.repository.query

fun DealsQuery(
    searchQuery: String? = null,
    matchTermsExactly: Boolean? = null,
    stores: List<Int>? = null,
    sortingCriteria: DealsSorting? = null,
    sortingDirection: DealsSortingDirection? = null
) = DealsQueryParameters(searchQuery, matchTermsExactly, stores, sortingCriteria, sortingDirection)

data class DealsQueryParameters(
    val searchQuery: String?,
    val matchTermsExactly: Boolean?,
    val stores: List<Int>?,
    val sorting: DealsSorting?,
    val sortingDirection: DealsSortingDirection?
)

enum class DealsSorting(val value: String) {
    DealRating("Deal Rating"),
    Title("Title"),
    Savings("Savings"),
    Price("Price"),
    Metacritic("Metacritic"),
    Reviews("Reviews"),
    Release("Release"),
    Store("Store"),
    Recent("Recent")
}

enum class DealsSortingDirection {
    Ascending,
    Descending
}

internal fun buildDealsFullUrl(endpointUrl: String, queryParameters: DealsQueryParameters): String {
    return buildString {
        append("$endpointUrl?")
        if (queryParameters.searchQuery != null) {
            append("title=${queryParameters.searchQuery}&")
        }
        if (queryParameters.matchTermsExactly != null) {
            val intBoolean = if (queryParameters.matchTermsExactly) 1 else 0
            append("exact=$intBoolean&")
        }
        if (queryParameters.stores != null) {
            append("storeID=${queryParameters.stores.joinToString(separator = ",")}&")
        }
        if (queryParameters.sorting != null) {
            append("sortBy=${queryParameters.sorting.value}&")
        }
        if (queryParameters.sortingDirection != null) {
            val intBoolean = if (queryParameters.sortingDirection == DealsSortingDirection.Descending) 1 else 0
            append("desc=$intBoolean&")
        }
        deleteAt(lastIndex)
    }
}
