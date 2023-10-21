package com.mr3y.ludi.shared.core.repository.query

data class GiveawaysQueryParameters(
    val platforms: List<GiveawayPlatform>?,
    val sorting: GiveawaysSorting?
)

enum class GiveawayPlatform(val value: String) {
    PC("pc"),
    Playstation4("ps4"),
    Playstation5("ps5"),
    XboxOne("xbox-one"),
    XboxSeriesXs("xbox-series-xs"),
    Xbox360("xbox-360"),
    Android("android"),
    IOS("ios"),
    NintendoSwitch("switch"),
    Steam("steam"),
    EpicGames("epic-games-store"),
    Ubisoft("ubisoft"),
    GOG("gog"),
    Itchio("itchio"),
    Origin("origin"),
    Battlenet("battlenet")
}

enum class GiveawaysSorting(val value: String) {
    Value("value"),
    Popularity("popularity"),
    Date("date")
}
