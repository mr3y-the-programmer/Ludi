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
