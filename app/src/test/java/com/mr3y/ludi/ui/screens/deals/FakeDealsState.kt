package com.mr3y.ludi.ui.screens.deals

import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.network.model.CheapSharkDeal
import com.mr3y.ludi.core.network.model.GamerPowerGiveawayEntry
import com.mr3y.ludi.core.network.model.GamerPowerGiveawayEntryStatus
import com.mr3y.ludi.core.network.model.toDeal
import com.mr3y.ludi.core.network.model.toGiveawayEntry
import com.mr3y.ludi.core.repository.query.DealsSorting
import com.mr3y.ludi.core.repository.query.DealsSortingDirection
import com.mr3y.ludi.core.repository.query.GiveawaysSorting
import com.mr3y.ludi.ui.presenter.model.DealStore
import com.mr3y.ludi.ui.presenter.model.DealsFiltersState
import com.mr3y.ludi.ui.presenter.model.DealsState
import com.mr3y.ludi.ui.presenter.model.GiveawayPlatform
import com.mr3y.ludi.ui.presenter.model.GiveawayStore
import com.mr3y.ludi.ui.presenter.model.GiveawaysFiltersState
import com.mr3y.ludi.ui.presenter.model.ResourceWrapper

val FakeInitialDealsFiltersState = DealsFiltersState(
    currentPage = 0,
    allStores = setOf(
        DealStore(1, "Steam"),
        DealStore(2, "GamersGate"),
        DealStore(3, "GreenManGaming"),
        DealStore(7, "GOG"),
        DealStore(8, "Origin"),
        DealStore(11, "Humble Store"),
        DealStore(13, "Uplay"),
        DealStore(15, "Fanatical"),
        DealStore(21, "WinGameStore"),
        DealStore(23, "GameBillet"),
        DealStore(24, "Voidu"),
        DealStore(25, "Epic Games"),
        DealStore(27, "Games planet"),
        DealStore(28, "Games load"),
        DealStore(29, "2Game"),
        DealStore(30, "IndieGala"),
        DealStore(31, "Blizzard Shop"),
        DealStore(33, "DLGamer"),
        DealStore(34, "Noctre"),
        DealStore(35, "DreamGame")
    ).sortedBy { it.label }.toSet(),
    selectedStores = emptySet(),
    sortingCriteria = DealsSorting.Recent,
    sortingDirection = DealsSortingDirection.Descending
)
val FakeInitialGiveawaysFiltersState = GiveawaysFiltersState(
    allPlatforms = GiveawayPlatform.values().toSortedSet(),
    selectedPlatforms = emptySet(),
    allStores = GiveawayStore.values().toSortedSet(),
    selectedStores = emptySet(),
    sortingCriteria = GiveawaysSorting.Popularity
)
val FakeDealSamples = listOf(
    ResourceWrapper.ActualResource(
        CheapSharkDeal(
            "DEUSEXHUMANREVOLUTIONDIRECTORSCUT",
            "Deus Ex: Human Revolution - Director's Cut",
            "/game/pc/deus-ex-human-revolution---directors-cut",
            "HhzMJAgQYGZ%2B%2BFPpBG%2BRFcuUQZJO3KXvlnyYYGwGUfU%3D",
            1,
            102249,
            2.99f,
            19.99f,
            true,
            85.042521,
            91,
            "Very Positive",
            92,
            17993,
            238010,
            1382400000,
            1621536418,
            9.6f,
            "https://cdn.cloudflare.steamstatic.com/steam/apps/238010/capsule_sm_120.jpg?t=1619788192"
        ).toDeal()
    ),
    ResourceWrapper.ActualResource(
        CheapSharkDeal(
            "THIEFDEADLYSHADOWS",
            "Thief: Deadly Shadows",
            "/game/pc/thief-deadly-shadows",
            "EX0oH20b7A1H2YiVjvVx5A0HH%2F4etw3x%2F6YMGVPpKbA%3D",
            1,
            396,
            0.98f,
            8.99f,
            true,
            89.098999,
            85,
            "Very Positive",
            81,
            1670,
            6980,
            1085443200,
            1621540561,
            9.4f,
            "https://cdn.cloudflare.steamstatic.com/steam/apps/6980/capsule_sm_120.jpg?t=1592493801"
        ).toDeal()
    ),
    ResourceWrapper.ActualResource(
        CheapSharkDeal(
            "CYBERNETICAFINAL",
            "Cybernetica: Final",
            "/game/pc/cybernetica-final",
            "YAm7vyXnmK5YWsFyTy22SFn5p%2F4Lrpu2eCCrrfvNO44%3D",
            1,
            226893,
            1.89f,
            18.99f,
            true,
            90.047393,
            0,
            null,
            0,
            0,
            1549710,
            1614643200,
            1621359373,
            9.1f,
            "https://cdn.cloudflare.steamstatic.com/steam/apps/1549710/capsule_sm_120.jpg?t=1614708882"
        ).toDeal()
    )
)

val FakeGiveawaysSamples = listOf(
    ResourceWrapper.ActualResource(
        GamerPowerGiveawayEntry(
            2301,
            "World of Warships - Starter Pack: Ishizuchi",
            "$23.47",
            "https://www.gamerpower.com/offers/1/641df70fc9883.jpg",
            "https://www.gamerpower.com/offers/1b/641df70fc9883.jpg",
            "Claim your free World of Warships - Starter Pack and unlock the Premium battleship Ishizuchi, Japanese Tier IV, plus camouflages and bonuses.",
            "1- Download this DLC content directly via Epic Games Store before the offer expires.\r\n2 - Please note the base game World of Warships (free-to-play) is required to enjoy this content.",
            "https://www.gamerpower.com/open/world-of-warships-starter-pack-ishizuchi",
            "2023-03-24 15:16:31",
            "DLC",
            "PC, Epic Games Store",
            "2023-04-28 23:59:00",
            930,
            GamerPowerGiveawayEntryStatus.Active,
            "https://www.gamerpower.com/world-of-warships-starter-pack-ishizuchi"
        ).toGiveawayEntry()
    ),
    ResourceWrapper.ActualResource(
        GamerPowerGiveawayEntry(
            2300,
            "Spandex Force: Champion Rising",
            "$9.99",
            "https://www.gamerpower.com/offers/1/641de37e3e819.jpg",
            "https://www.gamerpower.com/offers/1b/641de37e3e819.jpg",
            "Grab Spandex Force: Champion Rising for free and create your own hero today!",
            "1. Login into your free IndieGala account.\r\n2. Scroll down and click the \"Add to Your Library\" button to add the game to your library.\r\n3. Go to \"My Library\" to find your game.",
            "https://www.gamerpower.com/open/spandex-force-champion-rising",
            "2023-05-04 23:59:00",
            "Game",
            "PC, DRM-Free",
            null,
            1480,
            GamerPowerGiveawayEntryStatus.Active,
            "https://www.gamerpower.com/spandex-force-champion-rising"
        ).toGiveawayEntry()
    ),
    ResourceWrapper.ActualResource(
        GamerPowerGiveawayEntry(
            1178,
            "Dungeons and Dragons Online: Free Quest Packs",
            null,
            "https://www.gamerpower.com/offers/1/61698e2ca90cf.jpg",
            "https://www.gamerpower.com/offers/1b/61698e2ca90cf.jpg",
            "Claim the code below and unlock lots of Dungeons and Dragons quest packs, raids, and adventure packs permanently on your account for free! This special code is available to redeem through April 23rd, 2023 so you will need to act fast!\r\n\r\nCode: DUNGEONCRAWL",
            "1. Redeem your code before April 23rd, 2023.\r\n2. Enjoy!",
            "https://www.gamerpower.com/open/dungeons-and-dragons-online-all-quest-packs",
            "2023-05-31 23:59:00",
            "DLC",
            "PC",
            "2023-04-23 23:59:00",
            3540,
            GamerPowerGiveawayEntryStatus.Active,
            "https://www.gamerpower.com/dungeons-and-dragons-online-all-quest-packs"
        ).toGiveawayEntry()
    )
)

val FakeDealsState = DealsState(
    "",
    Result.Success(FakeDealSamples),
    Result.Success(FakeGiveawaysSamples),
    FakeInitialDealsFiltersState,
    FakeInitialGiveawaysFiltersState
)
