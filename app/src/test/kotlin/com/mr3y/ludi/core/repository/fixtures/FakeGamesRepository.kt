package com.mr3y.ludi.core.repository.fixtures

import com.mr3y.ludi.core.model.GameGenre
import com.mr3y.ludi.core.model.GamesGenresPage
import com.mr3y.ludi.core.model.GamesPage
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.network.model.DetailedRAWGPlatformInfo
import com.mr3y.ludi.core.network.model.RAWGGameGenre
import com.mr3y.ludi.core.network.model.RAWGGameScreenshot
import com.mr3y.ludi.core.network.model.RAWGGameTag
import com.mr3y.ludi.core.network.model.RAWGPlatformProperties
import com.mr3y.ludi.core.network.model.RAWGPlatformRequirements
import com.mr3y.ludi.core.network.model.RAWGShallowGame
import com.mr3y.ludi.core.network.model.RAWGStoreProperties
import com.mr3y.ludi.core.network.model.ShallowRAWGStoreInfoWithId
import com.mr3y.ludi.core.network.model.toGame
import com.mr3y.ludi.core.repository.GamesRepository
import com.mr3y.ludi.core.repository.query.GamesQueryParameters

class FakeGamesRepository : GamesRepository {

    private val games = listOfNotNull(
        RAWGShallowGame(
            id = 3498,
            slug = "grand-theft-auto-v",
            name = "Grand Theft Auto V",
            releaseDate = "2013-09-17",
            toBeAnnounced = false,
            imageUrl = "https://media.rawg.io/media/games/456/456dea5e1c7e3cd07060c14e96612001.jpg",
            rating = 4.47f,
            metaCriticScore = 92,
            playtime = 73,
            suggestionsCount = 410,
            platforms = listOf(
                DetailedRAWGPlatformInfo(
                    platform = RAWGPlatformProperties(
                        id = 187,
                        name = "PlayStation 5",
                        slug = "playstation5",
                        imageUrl = null,
                        yearEnd = null,
                        yearStart = 2020,
                        gamesCount = 824,
                        imageBackground = "https://media.rawg.io/media/games/253/2534a46f3da7fa7c315f1387515ca393.jpg"
                    ),
                    releaseDate = "2013-09-17",
                    requirementsInEnglish = null
                ),
                DetailedRAWGPlatformInfo(
                    platform = RAWGPlatformProperties(
                        id = 186,
                        name = "Xbox Series S/X",
                        slug = "xbox-series-x",
                        imageUrl = null,
                        yearEnd = null,
                        yearStart = 2020,
                        gamesCount = 731,
                        imageBackground = "https://media.rawg.io/media/games/618/618c2031a07bbff6b4f611f10b6bcdbc.jpg"
                    ),
                    releaseDate = "2013-09-17",
                    requirementsInEnglish = null
                ),
                DetailedRAWGPlatformInfo(
                    platform = RAWGPlatformProperties(
                        id = 18,
                        name = "PlayStation 4",
                        slug = "playstation4",
                        imageUrl = null,
                        yearEnd = null,
                        yearStart = null,
                        gamesCount = 6597,
                        imageBackground = "https://media.rawg.io/media/games/b72/b7233d5d5b1e75e86bb860ccc7aeca85.jpg"
                    ),
                    releaseDate = "2013-09-17",
                    requirementsInEnglish = null
                ),
                DetailedRAWGPlatformInfo(
                    platform = RAWGPlatformProperties(
                        id = 4,
                        name = "PC",
                        slug = "pc",
                        imageUrl = null,
                        yearEnd = null,
                        yearStart = null,
                        gamesCount = 536875,
                        imageBackground = "https://media.rawg.io/media/games/6fc/6fcf4cd3b17c288821388e6085bb0fc9.jpg"
                    ),
                    releaseDate = "2013-09-17",
                    requirementsInEnglish = RAWGPlatformRequirements(
                        minimum = "Minimum:OS: Windows 10 64 Bit, Windows 8.1 64 Bit, Windows 8 64 Bit, Windows 7 64 Bit Service Pack 1, Windows Vista 64 Bit Service Pack 2* (*NVIDIA video card recommended if running Vista OS)Processor: Intel Core 2 Quad CPU Q6600 @ 2.40GHz (4 CPUs) / AMD Phenom 9850 Quad-Core Processor (4 CPUs) @ 2.5GHzMemory: 4 GB RAMGraphics: NVIDIA 9800 GT 1GB / AMD HD 4870 1GB (DX 10, 10.1, 11)Storage: 72 GB available spaceSound Card: 100% DirectX 10 compatibleAdditional Notes: Over time downloadable content and programming changes will change the system requirements for this game.  Please refer to your hardware manufacturer and www.rockstargames.com/support for current compatibility information. Some system components such as mobile chipsets, integrated, and AGP graphics cards may be incompatible. Unlisted specifications may not be supported by publisher.     Other requirements:  Installation and online play requires log-in to Rockstar Games Social Club (13+) network; internet connection required for activation, online play, and periodic entitlement verification; software installations required including Rockstar Games Social Club platform, DirectX , Chromium, and Microsoft Visual C++ 2008 sp1 Redistributable Package, and authentication software that recognizes certain hardware attributes for entitlement, digital rights management, system, and other support purposes.     SINGLE USE SERIAL CODE REGISTRATION VIA INTERNET REQUIRED; REGISTRATION IS LIMITED TO ONE ROCKSTAR GAMES SOCIAL CLUB ACCOUNT (13+) PER SERIAL CODE; ONLY ONE PC LOG-IN ALLOWED PER SOCIAL CLUB ACCOUNT AT ANY TIME; SERIAL CODE(S) ARE NON-TRANSFERABLE ONCE USED; SOCIAL CLUB ACCOUNTS ARE NON-TRANSFERABLE.  Partner Requirements:  Please check the terms of service of this site before purchasing this software.",
                        recommended = "Recommended:OS: Windows 10 64 Bit, Windows 8.1 64 Bit, Windows 8 64 Bit, Windows 7 64 Bit Service Pack 1Processor: Intel Core i5 3470 @ 3.2GHz (4 CPUs) / AMD X8 FX-8350 @ 4GHz (8 CPUs)Memory: 8 GB RAMGraphics: NVIDIA GTX 660 2GB / AMD HD 7870 2GBStorage: 72 GB available spaceSound Card: 100% DirectX 10 compatibleAdditional Notes:"
                    )
                ),
                DetailedRAWGPlatformInfo(
                    platform = RAWGPlatformProperties(
                        id = 16,
                        name = "PlayStation 3",
                        slug = "playstation3",
                        imageUrl = null,
                        yearEnd = null,
                        yearStart = null,
                        gamesCount = 3269,
                        imageBackground = "https://media.rawg.io/media/games/234/23410661770ae13eac11066980834367.jpg"
                    ),
                    releaseDate = "2013-09-17",
                    requirementsInEnglish = null
                ),
                DetailedRAWGPlatformInfo(
                    platform = RAWGPlatformProperties(
                        id = 14,
                        name = "Xbox 360",
                        slug = "xbox360",
                        imageUrl = null,
                        yearEnd = null,
                        yearStart = null,
                        gamesCount = 2781,
                        imageBackground = "https://media.rawg.io/media/games/5c0/5c0dd63002cb23f804aab327d40ef119.jpg"
                    ),
                    releaseDate = "2013-09-17",
                    requirementsInEnglish = null
                ),
                DetailedRAWGPlatformInfo(
                    platform = RAWGPlatformProperties(
                        id = 1,
                        name = "Xbox One",
                        slug = "xbox-one",
                        imageUrl = null,
                        yearEnd = null,
                        yearStart = null,
                        gamesCount = 5488,
                        imageBackground = "https://media.rawg.io/media/games/562/562553814dd54e001a541e4ee83a591c.jpg"
                    ),
                    releaseDate = "2013-09-17",
                    requirementsInEnglish = null
                )
            ),
            stores = listOf(
                ShallowRAWGStoreInfoWithId(
                    id = 290375,
                    store = RAWGStoreProperties(
                        id = 3,
                        name = "PlayStation Store",
                        slug = "playstation-store",
                        domain = "store.playstation.com",
                        gamesCount = 7796,
                        imageUrl = "https://media.rawg.io/media/games/bc0/bc06a29ceac58652b684deefe7d56099.jpg"
                    )
                ),
                ShallowRAWGStoreInfoWithId(
                    id = 438095,
                    store = RAWGStoreProperties(
                        id = 11,
                        name = "Epic Games",
                        slug = "epic-games",
                        domain = "epicgames.com",
                        gamesCount = 1215,
                        imageUrl = "https://media.rawg.io/media/games/b54/b54598d1d5cc31899f4f0a7e3122a7b0.jpg"
                    )
                ),
                ShallowRAWGStoreInfoWithId(
                    id = 290376,
                    store = RAWGStoreProperties(
                        id = 1,
                        name = "Steam",
                        slug = "steam",
                        domain = "store.steampowered.com",
                        gamesCount = 73115,
                        imageUrl = "https://media.rawg.io/media/games/120/1201a40e4364557b124392ee50317b99.jpg"
                    )
                ),
                ShallowRAWGStoreInfoWithId(
                    id = 290377,
                    store = RAWGStoreProperties(
                        id = 7,
                        name = "Xbox 360 Store",
                        slug = "xbox360",
                        domain = "marketplace.xbox.com",
                        gamesCount = 1914,
                        imageUrl = "https://media.rawg.io/media/games/e2d/e2d3f396b16dded0f841c17c9799a882.jpg"
                    )
                ),
                ShallowRAWGStoreInfoWithId(
                    id = 290378,
                    store = RAWGStoreProperties(
                        id = 2,
                        name = "Xbox Store",
                        slug = "xbox-store",
                        domain = "microsoft.com",
                        gamesCount = 4756,
                        imageUrl = "https://media.rawg.io/media/games/26d/26d4437715bee60138dab4a7c8c59c92.jpg"
                    )
                )
            ),
            tags = listOf(
                RAWGGameTag(
                    id = 31,
                    name = "Singleplayer",
                    slug = "singleplayer",
                    language = "eng",
                    gamesCount = 209582,
                    imageUrl = "https://media.rawg.io/media/games/73e/73eecb8909e0c39fb246f457b5d6cbbe.jpg"
                ),
                RAWGGameTag(
                    id = 40847,
                    name = "Steam Achievements",
                    slug = "steam-achievements",
                    language = "eng",
                    gamesCount = 29451,
                    imageUrl = "https://media.rawg.io/media/games/8cc/8cce7c0e99dcc43d66c8efd42f9d03e3.jpg"
                ),
                RAWGGameTag(
                    id = 7,
                    name = "Multiplayer",
                    slug = "multiplayer",
                    language = "eng",
                    gamesCount = 35586,
                    imageUrl = "https://media.rawg.io/media/games/328/3283617cb7d75d67257fc58339188742.jpg"
                ),
                RAWGGameTag(
                    id = 40836,
                    name = "Full controller support",
                    slug = "full-controller-support",
                    language = "eng",
                    gamesCount = 13833,
                    imageUrl = "https://media.rawg.io/media/games/490/49016e06ae2103881ff6373248843069.jpg"
                ),
                RAWGGameTag(
                    id = 13,
                    name = "Atmospheric",
                    slug = "atmospheric",
                    language = "eng",
                    gamesCount = 29424,
                    imageUrl = "https://media.rawg.io/media/games/7cf/7cfc9220b401b7a300e409e539c9afd5.jpg"
                ),
                RAWGGameTag(
                    id = 42,
                    name = "Great Soundtrack",
                    slug = "great-soundtrack",
                    language = "eng",
                    gamesCount = 3230,
                    imageUrl = "https://media.rawg.io/media/games/7a2/7a2500ee8b2c0e1ff268bb4479463dea.jpg"
                ),
                RAWGGameTag(
                    id = 24,
                    name = "RPG",
                    slug = "rpg",
                    language = "eng",
                    gamesCount = 16758,
                    imageUrl = "https://media.rawg.io/media/games/6cd/6cd653e0aaef5ff8bbd295bf4bcb12eb.jpg"
                ),
                RAWGGameTag(
                    id = 18,
                    name = "Co-op",
                    slug = "co-op",
                    language = "eng",
                    gamesCount = 9686,
                    imageUrl = "https://media.rawg.io/media/games/8cc/8cce7c0e99dcc43d66c8efd42f9d03e3.jpg"
                ),
                RAWGGameTag(
                    id = 36,
                    name = "Open World",
                    slug = "open-world",
                    language = "eng",
                    gamesCount = 6237,
                    imageUrl = "https://media.rawg.io/media/games/ee3/ee3e10193aafc3230ba1cae426967d10.jpg"
                ),
                RAWGGameTag(
                    id = 411,
                    name = "cooperative",
                    slug = "cooperative",
                    language = "eng",
                    gamesCount = 3925,
                    imageUrl = "https://media.rawg.io/media/games/d0f/d0f91fe1d92332147e5db74e207cfc7a.jpg"
                ),
                RAWGGameTag(
                    id = 8,
                    name = "First-Person",
                    slug = "first-person",
                    language = "eng",
                    gamesCount = 29119,
                    imageUrl = "https://media.rawg.io/media/games/c4b/c4b0cab189e73432de3a250d8cf1c84e.jpg"
                ),
                RAWGGameTag(
                    id = 149,
                    name = "Third Person",
                    slug = "third-person",
                    language = "eng",
                    gamesCount = 9288,
                    imageUrl = "https://media.rawg.io/media/games/951/951572a3dd1e42544bd39a5d5b42d234.jpg"
                ),
                RAWGGameTag(
                    id = 4,
                    name = "Funny",
                    slug = "funny",
                    language = "eng",
                    gamesCount = 23019,
                    imageUrl = "https://media.rawg.io/media/games/5bb/5bb55ccb8205aadbb6a144cf6d8963f1.jpg"
                ),
                RAWGGameTag(
                    id = 37,
                    name = "Sandbox",
                    slug = "sandbox",
                    language = "eng",
                    gamesCount = 5963,
                    imageUrl = "https://media.rawg.io/media/games/25c/25c4776ab5723d5d735d8bf617ca12d9.jpg"
                ),
                RAWGGameTag(
                    id = 123,
                    name = "Comedy",
                    slug = "comedy",
                    language = "eng",
                    gamesCount = 10907,
                    imageUrl = "https://media.rawg.io/media/games/48c/48cb04ca483be865e3a83119c94e6097.jpg"
                ),
                RAWGGameTag(
                    id = 150,
                    name = "Third-Person Shooter",
                    slug = "third-person-shooter",
                    language = "eng",
                    gamesCount = 2884,
                    imageUrl = "https://media.rawg.io/media/games/b45/b45575f34285f2c4479c9a5f719d972e.jpg"
                ),
                RAWGGameTag(
                    id = 62,
                    name = "Moddable",
                    slug = "moddable",
                    language = "eng",
                    gamesCount = 778,
                    imageUrl = "https://media.rawg.io/media/games/48e/48e63bbddeddbe9ba81942772b156664.jpg"
                ),
                RAWGGameTag(
                    id = 144,
                    name = "Crime",
                    slug = "crime",
                    language = "eng",
                    gamesCount = 2544,
                    imageUrl = "https://media.rawg.io/media/games/26d/26d4437715bee60138dab4a7c8c59c92.jpg"
                ),
                RAWGGameTag(
                    id = 62349,
                    name = "vr mod",
                    slug = "vr-mod",
                    language = "eng",
                    gamesCount = 17,
                    imageUrl = "https://media.rawg.io/media/screenshots/1bb/1bb3f78f0fe43b5d5ca2f3da5b638840.jpg"
                )
            ),
            screenshots = listOf(
                RAWGGameScreenshot(
                    id = -1,
                    imageUrl = "https://media.rawg.io/media/games/456/456dea5e1c7e3cd07060c14e96612001.jpg"
                ),
                RAWGGameScreenshot(
                    id = 1827221,
                    imageUrl = "https://media.rawg.io/media/screenshots/a7c/a7c43871a54bed6573a6a429451564ef.jpg"
                ),
                RAWGGameScreenshot(
                    id = 1827222,
                    imageUrl = "https://media.rawg.io/media/screenshots/cf4/cf4367daf6a1e33684bf19adb02d16d6.jpg"
                ),
                RAWGGameScreenshot(
                    id = 1827223,
                    imageUrl = "https://media.rawg.io/media/screenshots/f95/f9518b1d99210c0cae21fc09e95b4e31.jpg"
                ),
                RAWGGameScreenshot(
                    id = 1827225,
                    imageUrl = "https://media.rawg.io/media/screenshots/a5c/a5c95ea539c87d5f538763e16e18fb99.jpg"
                ),
                RAWGGameScreenshot(
                    id = 1827226,
                    imageUrl = "https://media.rawg.io/media/screenshots/a7e/a7e990bc574f4d34e03b5926361d1ee7.jpg"
                ),
                RAWGGameScreenshot(
                    id = 1827227,
                    imageUrl = "https://media.rawg.io/media/screenshots/592/592e2501d8734b802b2a34fee2df59fa.jpg"
                )
            ),
            genres = listOf(
                RAWGGameGenre(
                    id = 4,
                    name = "Action",
                    slug = "action",
                    gamesCount = 177875,
                    imageUrl = "https://media.rawg.io/media/games/b8c/b8c243eaa0fbac8115e0cdccac3f91dc.jpg"
                ),
                RAWGGameGenre(
                    id = 3,
                    name = "Adventure",
                    slug = "adventure",
                    gamesCount = 136825,
                    imageUrl = "https://media.rawg.io/media/games/995/9951d9d55323d08967640f7b9ab3e342.jpg"
                )
            )
        ).toGame(),
        RAWGShallowGame(
            id = 3328,
            slug = "the-witcher-3-wild-hunt",
            name = "The Witcher 3: Wild Hunt",
            releaseDate = "2015-05-18",
            toBeAnnounced = false,
            imageUrl = "https://media.rawg.io/media/games/618/618c2031a07bbff6b4f611f10b6bcdbc.jpg",
            rating = 4.66f,
            metaCriticScore = 92,
            playtime = 46,
            suggestionsCount = 649,
            platforms = listOf(
                DetailedRAWGPlatformInfo(
                    platform = RAWGPlatformProperties(
                        id = 186,
                        name = "Xbox Series S/X",
                        slug = "xbox-series-x",
                        imageUrl = null,
                        yearEnd = null,
                        yearStart = 2020,
                        gamesCount = 731,
                        imageBackground = "https://media.rawg.io/media/games/618/618c2031a07bbff6b4f611f10b6bcdbc.jpg"
                    ),
                    releaseDate = "2015-05-18",
                    requirementsInEnglish = null
                ),
                DetailedRAWGPlatformInfo(
                    platform = RAWGPlatformProperties(
                        id = 18,
                        name = "PlayStation 4",
                        slug = "playstation4",
                        imageUrl = null,
                        yearEnd = null,
                        yearStart = null,
                        gamesCount = 6597,
                        imageBackground = "https://media.rawg.io/media/games/b72/b7233d5d5b1e75e86bb860ccc7aeca85.jpg"
                    ),
                    releaseDate = "2015-05-18",
                    requirementsInEnglish = null
                ),
                DetailedRAWGPlatformInfo(
                    platform = RAWGPlatformProperties(
                        id = 7,
                        name = "Nintendo Switch",
                        slug = "nintendo-switch",
                        imageUrl = null,
                        yearEnd = null,
                        yearStart = null,
                        gamesCount = 5199,
                        imageBackground = "https://media.rawg.io/media/games/b72/b7233d5d5b1e75e86bb860ccc7aeca85.jpg"
                    ),
                    releaseDate = "2015-05-18",
                    requirementsInEnglish = null
                ),
                DetailedRAWGPlatformInfo(
                    platform = RAWGPlatformProperties(
                        id = 4,
                        name = "PC",
                        slug = "pc",
                        imageUrl = null,
                        yearEnd = null,
                        yearStart = null,
                        gamesCount = 536875,
                        imageBackground = "https://media.rawg.io/media/games/6fc/6fcf4cd3b17c288821388e6085bb0fc9.jpg"
                    ),
                    releaseDate = "2015-05-18",
                    requirementsInEnglish = null
                ),
                DetailedRAWGPlatformInfo(
                    platform = RAWGPlatformProperties(
                        id = 1,
                        name = "Xbox One",
                        slug = "xbox-one",
                        imageUrl = null,
                        yearEnd = null,
                        yearStart = null,
                        gamesCount = 5488,
                        imageBackground = "https://media.rawg.io/media/games/562/562553814dd54e001a541e4ee83a591c.jpg"
                    ),
                    releaseDate = "2015-05-18",
                    requirementsInEnglish = null
                ),
                DetailedRAWGPlatformInfo(
                    platform = RAWGPlatformProperties(
                        id = 187,
                        name = "PlayStation 5",
                        slug = "playstation5",
                        imageUrl = null,
                        yearEnd = null,
                        yearStart = 2020,
                        gamesCount = 824,
                        imageBackground = "https://media.rawg.io/media/games/253/2534a46f3da7fa7c315f1387515ca393.jpg"
                    ),
                    releaseDate = "2015-05-18",
                    requirementsInEnglish = null
                )
            ),
            stores = listOf(
                ShallowRAWGStoreInfoWithId(
                    id = 354780,
                    store = RAWGStoreProperties(
                        id = 5,
                        name = "GOG",
                        slug = "gog",
                        domain = "gog.com",
                        gamesCount = 4934,
                        imageUrl = "https://media.rawg.io/media/games/4cf/4cfc6b7f1850590a4634b08bfab308ab.jpg"
                    )
                ),
                ShallowRAWGStoreInfoWithId(
                    id = 3565,
                    store = RAWGStoreProperties(
                        id = 3,
                        name = "PlayStation Store",
                        slug = "playstation-store",
                        domain = "store.playstation.com",
                        gamesCount = 7796,
                        imageUrl = "https://media.rawg.io/media/games/bc0/bc06a29ceac58652b684deefe7d56099.jpg"
                    )
                ),
                ShallowRAWGStoreInfoWithId(
                    id = 305376,
                    store = RAWGStoreProperties(
                        id = 1,
                        name = "Steam",
                        slug = "steam",
                        domain = "store.steampowered.com",
                        gamesCount = 73115,
                        imageUrl = "https://media.rawg.io/media/games/120/1201a40e4364557b124392ee50317b99.jpg"
                    )
                ),
                ShallowRAWGStoreInfoWithId(
                    id = 312313,
                    store = RAWGStoreProperties(
                        id = 2,
                        name = "Xbox Store",
                        slug = "xbox-store",
                        domain = "microsoft.com",
                        gamesCount = 4756,
                        imageUrl = "https://media.rawg.io/media/games/26d/26d4437715bee60138dab4a7c8c59c92.jpg"
                    )
                ),
                ShallowRAWGStoreInfoWithId(
                    id = 676022,
                    store = RAWGStoreProperties(
                        id = 6,
                        name = "Nintendo Store",
                        slug = "nintendo",
                        domain = "nintendo.com",
                        gamesCount = 8862,
                        imageUrl = "https://media.rawg.io/media/games/7c4/7c448374df84b607f67ce9182a3a3ca7.jpg"
                    )
                )
            ),
            tags = listOf(
                RAWGGameTag(
                    id = 31,
                    name = "Singleplayer",
                    slug = "singleplayer",
                    language = "eng",
                    gamesCount = 209582,
                    imageUrl = "https://media.rawg.io/media/games/73e/73eecb8909e0c39fb246f457b5d6cbbe.jpg"
                ),
                RAWGGameTag(
                    id = 40836,
                    name = "Full controller support",
                    slug = "full-controller-support",
                    language = "eng",
                    gamesCount = 13833,
                    imageUrl = "https://media.rawg.io/media/games/490/49016e06ae2103881ff6373248843069.jpg"
                ),
                RAWGGameTag(
                    id = 13,
                    name = "Atmospheric",
                    slug = "atmospheric",
                    language = "eng",
                    gamesCount = 29424,
                    imageUrl = "https://media.rawg.io/media/games/7cf/7cfc9220b401b7a300e409e539c9afd5.jpg"
                ),
                RAWGGameTag(
                    id = 42,
                    name = "Great Soundtrack",
                    slug = "great-soundtrack",
                    language = "eng",
                    gamesCount = 3230,
                    imageUrl = "https://media.rawg.io/media/games/7a2/7a2500ee8b2c0e1ff268bb4479463dea.jpg"
                ),
                RAWGGameTag(
                    id = 24,
                    name = "RPG",
                    slug = "rpg",
                    language = "eng",
                    gamesCount = 16758,
                    imageUrl = "https://media.rawg.io/media/games/6cd/6cd653e0aaef5ff8bbd295bf4bcb12eb.jpg"
                ),
                RAWGGameTag(
                    id = 118,
                    name = "Story Rich",
                    slug = "story-rich",
                    language = "eng",
                    gamesCount = 18114,
                    imageUrl = "https://media.rawg.io/media/games/fc1/fc1307a2774506b5bd65d7e8424664a7.jpg"
                ),
                RAWGGameTag(
                    id = 36,
                    name = "Open World",
                    slug = "open-world",
                    language = "eng",
                    gamesCount = 6237,
                    imageUrl = "https://media.rawg.io/media/games/ee3/ee3e10193aafc3230ba1cae426967d10.jpg"
                ),
                RAWGGameTag(
                    id = 149,
                    name = "Third Person",
                    slug = "third-person",
                    language = "eng",
                    gamesCount = 9288,
                    imageUrl = "https://media.rawg.io/media/games/951/951572a3dd1e42544bd39a5d5b42d234.jpg"
                ),
                RAWGGameTag(
                    id = 64,
                    name = "Fantasy",
                    slug = "fantasy",
                    language = "eng",
                    gamesCount = 24635,
                    imageUrl = "https://media.rawg.io/media/screenshots/88b/88b5f27f07d6ca2f8a3cd0b36e03a670.jpg"
                ),
                RAWGGameTag(
                    id = 37,
                    name = "Sandbox",
                    slug = "sandbox",
                    language = "eng",
                    gamesCount = 5963,
                    imageUrl = "https://media.rawg.io/media/games/25c/25c4776ab5723d5d735d8bf617ca12d9.jpg"
                ),
                RAWGGameTag(
                    id = 97,
                    name = "Action RPG",
                    slug = "action-rpg",
                    language = "eng",
                    gamesCount = 5774,
                    imageUrl = "https://media.rawg.io/media/games/849/849414b978db37d4563ff9e4b0d3a787.jpg"
                ),
                RAWGGameTag(
                    id = 41,
                    name = "Dark",
                    slug = "dark",
                    language = "eng",
                    gamesCount = 14252,
                    imageUrl = "https://media.rawg.io/media/games/4e6/4e6e8e7f50c237d76f38f3c885dae3d2.jpg"
                ),
                RAWGGameTag(
                    id = 44,
                    name = "Nudity",
                    slug = "nudity",
                    language = "eng",
                    gamesCount = 4681,
                    imageUrl = "https://media.rawg.io/media/games/16b/16b1b7b36e2042d1128d5a3e852b3b2f.jpg"
                ),
                RAWGGameTag(
                    id = 336,
                    name = "controller support",
                    slug = "controller-support",
                    language = "eng",
                    gamesCount = 293,
                    imageUrl = "https://media.rawg.io/media/games/f46/f466571d536f2e3ea9e815ad17177501.jpg"
                ),
                RAWGGameTag(
                    id = 145,
                    name = "Choices Matter",
                    slug = "choices-matter",
                    language = "eng",
                    gamesCount = 3254,
                    imageUrl = "https://media.rawg.io/media/games/562/562553814dd54e001a541e4ee83a591c.jpg"
                ),
                RAWGGameTag(
                    id = 192,
                    name = "Mature",
                    slug = "mature",
                    language = "eng",
                    gamesCount = 2001,
                    imageUrl = "https://media.rawg.io/media/games/5fa/5fae5fec3c943179e09da67a4427d68f.jpg"
                ),
                RAWGGameTag(
                    id = 40,
                    name = "Dark Fantasy",
                    slug = "dark-fantasy",
                    language = "eng",
                    gamesCount = 3190,
                    imageUrl = "https://media.rawg.io/media/games/da1/da1b267764d77221f07a4386b6548e5a.jpg"
                ),
                RAWGGameTag(
                    id = 66,
                    name = "Medieval",
                    slug = "medieval",
                    language = "eng",
                    gamesCount = 5299,
                    imageUrl = "https://media.rawg.io/media/games/ee3/ee3e10193aafc3230ba1cae426967d10.jpg"
                ),
                RAWGGameTag(
                    id = 82,
                    name = "Magic",
                    slug = "magic",
                    language = "eng",
                    gamesCount = 8040,
                    imageUrl = "https://media.rawg.io/media/screenshots/6d3/6d367773c06886535620f2d7fb1cb866.jpg"
                ),
                RAWGGameTag(
                    id = 218,
                    name = "Multiple Endings",
                    slug = "multiple-endings",
                    language = "eng",
                    gamesCount = 6936,
                    imageUrl = "https://media.rawg.io/media/games/951/951572a3dd1e42544bd39a5d5b42d234.jpg"
                )
            ),
            screenshots = listOf(
                RAWGGameScreenshot(
                    id = -1,
                    imageUrl = "https://media.rawg.io/media/games/618/618c2031a07bbff6b4f611f10b6bcdbc.jpg"
                ),
                RAWGGameScreenshot(
                    id = 30336,
                    imageUrl = "https://media.rawg.io/media/screenshots/1ac/1ac19f31974314855ad7be266adeb500.jpg"
                ),
                RAWGGameScreenshot(
                    id = 30337,
                    imageUrl = "https://media.rawg.io/media/screenshots/6a0/6a08afca95261a2fe221ea9e01d28762.jpg"
                ),
                RAWGGameScreenshot(
                    id = 30338,
                    imageUrl = "https://media.rawg.io/media/screenshots/cdd/cdd31b6b4a687425a87b5ce231ac89d7.jpg"
                ),
                RAWGGameScreenshot(
                    id = 30339,
                    imageUrl = "https://media.rawg.io/media/screenshots/862/862397b153221a625922d3bb66337834.jpg"
                ),
                RAWGGameScreenshot(
                    id = 30340,
                    imageUrl = "https://media.rawg.io/media/screenshots/166/166787c442a45f52f4f230c33fd7d605.jpg"
                ),
                RAWGGameScreenshot(
                    id = 30342,
                    imageUrl = "https://media.rawg.io/media/screenshots/f63/f6373ee614046d81503d63f167181803.jpg"
                )
            ),
            genres = listOf(
                RAWGGameGenre(
                    id = 4,
                    name = "Action",
                    slug = "action",
                    gamesCount = 177875,
                    imageUrl = "https://media.rawg.io/media/games/b8c/b8c243eaa0fbac8115e0cdccac3f91dc.jpg"
                ),
                RAWGGameGenre(
                    id = 3,
                    name = "Adventure",
                    slug = "adventure",
                    gamesCount = 136825,
                    imageUrl = "https://media.rawg.io/media/games/995/9951d9d55323d08967640f7b9ab3e342.jpg"
                ),
                RAWGGameGenre(
                    id = 5,
                    name = "RPG",
                    slug = "role-playing-games-rpg",
                    gamesCount = 53787,
                    imageUrl = "https://media.rawg.io/media/games/d1a/d1a2e99ade53494c6330a0ed945fe823.jpg"
                )
            )
        ).toGame(),
        RAWGShallowGame(
            id = 4200,
            slug = "portal-2",
            name = "Portal 2",
            releaseDate = "2011-04-18",
            toBeAnnounced = false,
            imageUrl = "https://media.rawg.io/media/games/328/3283617cb7d75d67257fc58339188742.jpg",
            rating = 4.62f,
            metaCriticScore = 95,
            playtime = 11,
            suggestionsCount = 534,
            platforms = listOf(
                DetailedRAWGPlatformInfo(
                    platform = RAWGPlatformProperties(
                        id = 14,
                        name = "Xbox 360",
                        slug = "xbox360",
                        imageUrl = null,
                        yearEnd = null,
                        yearStart = null,
                        gamesCount = 2781,
                        imageBackground = "https://media.rawg.io/media/games/5c0/5c0dd63002cb23f804aab327d40ef119.jpg"
                    ),
                    releaseDate = "2011-04-19",
                    requirementsInEnglish = null
                ),
                DetailedRAWGPlatformInfo(
                    platform = RAWGPlatformProperties(
                        id = 6,
                        name = "Linux",
                        slug = "linux",
                        imageUrl = null,
                        yearEnd = null,
                        yearStart = null,
                        gamesCount = 79193,
                        imageBackground = "https://media.rawg.io/media/games/46d/46d98e6910fbc0706e2948a7cc9b10c5.jpg"
                    ),
                    releaseDate = "2011-04-19",
                    requirementsInEnglish = null
                ),
                DetailedRAWGPlatformInfo(
                    platform = RAWGPlatformProperties(
                        id = 5,
                        name = "macOS",
                        slug = "macos",
                        imageUrl = null,
                        yearEnd = null,
                        yearStart = null,
                        gamesCount = 106472,
                        imageBackground = "https://media.rawg.io/media/games/960/960b601d9541cec776c5fa42a00bf6c4.jpg"
                    ),
                    releaseDate = null,
                    requirementsInEnglish = null
                ),
                DetailedRAWGPlatformInfo(
                    platform = RAWGPlatformProperties(
                        id = 16,
                        name = "PlayStation 3",
                        slug = "playstation3",
                        imageUrl = null,
                        yearEnd = null,
                        yearStart = null,
                        gamesCount = 3269,
                        imageBackground = "https://media.rawg.io/media/games/234/23410661770ae13eac11066980834367.jpg"
                    ),
                    releaseDate = "2011-04-19",
                    requirementsInEnglish = null
                ),
                DetailedRAWGPlatformInfo(
                    platform = RAWGPlatformProperties(
                        id = 4,
                        name = "PC",
                        slug = "pc",
                        imageUrl = null,
                        yearEnd = null,
                        yearStart = null,
                        gamesCount = 536875,
                        imageBackground = "https://media.rawg.io/media/games/6fc/6fcf4cd3b17c288821388e6085bb0fc9.jpg"
                    ),
                    releaseDate = null,
                    requirementsInEnglish = null
                ),
                DetailedRAWGPlatformInfo(
                    platform = RAWGPlatformProperties(
                        id = 1,
                        name = "Xbox One",
                        slug = "xbox-one",
                        imageUrl = null,
                        yearEnd = null,
                        yearStart = null,
                        gamesCount = 5488,
                        imageBackground = "https://media.rawg.io/media/games/562/562553814dd54e001a541e4ee83a591c.jpg"
                    ),
                    releaseDate = "2011-04-18",
                    requirementsInEnglish = null
                )
            ),
            stores = listOf(
                ShallowRAWGStoreInfoWithId(
                    id = 465889,
                    store = RAWGStoreProperties(
                        id = 2,
                        name = "Xbox Store",
                        slug = "xbox-store",
                        domain = "microsoft.com",
                        gamesCount = 4756,
                        imageUrl = "https://media.rawg.io/media/games/26d/26d4437715bee60138dab4a7c8c59c92.jpg"
                    )
                ),
                ShallowRAWGStoreInfoWithId(
                    id = 13134,
                    store = RAWGStoreProperties(
                        id = 1,
                        name = "Steam",
                        slug = "steam",
                        domain = "store.steampowered.com",
                        gamesCount = 73115,
                        imageUrl = "https://media.rawg.io/media/games/120/1201a40e4364557b124392ee50317b99.jpg"
                    )
                ),
                ShallowRAWGStoreInfoWithId(
                    id = 4526,
                    store = RAWGStoreProperties(
                        id = 3,
                        name = "PlayStation Store",
                        slug = "playstation-store",
                        domain = "store.playstation.com",
                        gamesCount = 7796,
                        imageUrl = "https://media.rawg.io/media/games/bc0/bc06a29ceac58652b684deefe7d56099.jpg"
                    )
                ),
                ShallowRAWGStoreInfoWithId(
                    id = 33916,
                    store = RAWGStoreProperties(
                        id = 7,
                        name = "Xbox 360 Store",
                        slug = "xbox360",
                        domain = "marketplace.xbox.com",
                        gamesCount = 1914,
                        imageUrl = "https://media.rawg.io/media/games/e2d/e2d3f396b16dded0f841c17c9799a882.jpg"
                    )
                )
            ),
            tags = listOf(
                RAWGGameTag(
                    id = 31,
                    name = "Singleplayer",
                    slug = "singleplayer",
                    language = "eng",
                    gamesCount = 209582,
                    imageUrl = "https://media.rawg.io/media/games/73e/73eecb8909e0c39fb246f457b5d6cbbe.jpg"
                ),
                RAWGGameTag(
                    id = 40847,
                    name = "Steam Achievements",
                    slug = "steam-achievements",
                    language = "eng",
                    gamesCount = 29451,
                    imageUrl = "https://media.rawg.io/media/games/8cc/8cce7c0e99dcc43d66c8efd42f9d03e3.jpg"
                ),
                RAWGGameTag(
                    id = 7,
                    name = "Multiplayer",
                    slug = "multiplayer",
                    language = "eng",
                    gamesCount = 35586,
                    imageUrl = "https://media.rawg.io/media/games/328/3283617cb7d75d67257fc58339188742.jpg"
                ),
                RAWGGameTag(
                    id = 40836,
                    name = "Full controller support",
                    slug = "full-controller-support",
                    language = "eng",
                    gamesCount = 13833,
                    imageUrl = "https://media.rawg.io/media/games/490/49016e06ae2103881ff6373248843069.jpg"
                ),
                RAWGGameTag(
                    id = 13,
                    name = "Atmospheric",
                    slug = "atmospheric",
                    language = "eng",
                    gamesCount = 29424,
                    imageUrl = "https://media.rawg.io/media/games/7cf/7cfc9220b401b7a300e409e539c9afd5.jpg"
                ),
                RAWGGameTag(
                    id = 40849,
                    name = "Steam Cloud",
                    slug = "steam-cloud",
                    language = "eng",
                    gamesCount = 13564,
                    imageUrl = "https://media.rawg.io/media/games/49c/49c3dfa4ce2f6f140cc4825868e858cb.jpg"
                ),
                RAWGGameTag(
                    id = 7808,
                    name = "steam-trading-cards",
                    slug = "steam-trading-cards",
                    language = "eng",
                    gamesCount = 7582,
                    imageUrl = "https://media.rawg.io/media/games/310/3106b0e012271c5ffb16497b070be739.jpg"
                ),
                RAWGGameTag(
                    id = 18,
                    name = "Co-op",
                    slug = "co-op",
                    language = "eng",
                    gamesCount = 9686,
                    imageUrl = "https://media.rawg.io/media/games/8cc/8cce7c0e99dcc43d66c8efd42f9d03e3.jpg"
                ),
                RAWGGameTag(
                    id = 118,
                    name = "Story Rich",
                    slug = "story-rich",
                    language = "eng",
                    gamesCount = 18114,
                    imageUrl = "https://media.rawg.io/media/games/fc1/fc1307a2774506b5bd65d7e8424664a7.jpg"
                ),
                RAWGGameTag(
                    id = 411,
                    name = "cooperative",
                    slug = "cooperative",
                    language = "eng",
                    gamesCount = 3925,
                    imageUrl = "https://media.rawg.io/media/games/d0f/d0f91fe1d92332147e5db74e207cfc7a.jpg"
                ),
                RAWGGameTag(
                    id = 8,
                    name = "First-Person",
                    slug = "first-person",
                    language = "eng",
                    gamesCount = 29119,
                    imageUrl = "https://media.rawg.io/media/games/c4b/c4b0cab189e73432de3a250d8cf1c84e.jpg"
                ),
                RAWGGameTag(
                    id = 32,
                    name = "Sci-fi",
                    slug = "sci-fi",
                    language = "eng",
                    gamesCount = 17348,
                    imageUrl = "https://media.rawg.io/media/games/8e4/8e4de3f54ac659e08a7ba6a2b731682a.jpg"
                ),
                RAWGGameTag(
                    id = 30,
                    name = "FPS",
                    slug = "fps",
                    language = "eng",
                    gamesCount = 12564,
                    imageUrl = "https://media.rawg.io/media/games/120/1201a40e4364557b124392ee50317b99.jpg"
                ),
                RAWGGameTag(
                    id = 9,
                    name = "Online Co-Op",
                    slug = "online-co-op",
                    language = "eng",
                    gamesCount = 4190,
                    imageUrl = "https://media.rawg.io/media/games/d0f/d0f91fe1d92332147e5db74e207cfc7a.jpg"
                ),
                RAWGGameTag(
                    id = 4,
                    name = "Funny",
                    slug = "funny",
                    language = "eng",
                    gamesCount = 23019,
                    imageUrl = "https://media.rawg.io/media/games/5bb/5bb55ccb8205aadbb6a144cf6d8963f1.jpg"
                ),
                RAWGGameTag(
                    id = 189,
                    name = "Female Protagonist",
                    slug = "female-protagonist",
                    language = "eng",
                    gamesCount = 10429,
                    imageUrl = "https://media.rawg.io/media/games/021/021c4e21a1824d2526f925eff6324653.jpg"
                ),
                RAWGGameTag(
                    id = 123,
                    name = "Comedy",
                    slug = "comedy",
                    language = "eng",
                    gamesCount = 10907,
                    imageUrl = "https://media.rawg.io/media/games/48c/48cb04ca483be865e3a83119c94e6097.jpg"
                ),
                RAWGGameTag(
                    id = 75,
                    name = "Local Co-Op",
                    slug = "local-co-op",
                    language = "eng",
                    gamesCount = 4977,
                    imageUrl = "https://media.rawg.io/media/games/226/2262cea0b385db6cf399f4be831603b0.jpg"
                ),
                RAWGGameTag(
                    id = 11669,
                    name = "stats",
                    slug = "stats",
                    language = "eng",
                    gamesCount = 4436,
                    imageUrl = "https://media.rawg.io/media/games/179/179245a3693049a11a25b900ab18f8f7.jpg"
                ),
                RAWGGameTag(
                    id = 40852,
                    name = "Steam Workshop",
                    slug = "steam-workshop",
                    language = "eng",
                    gamesCount = 1276,
                    imageUrl = "https://media.rawg.io/media/games/f3e/f3eec35c6218dcfd93a537751e6bfa61.jpg"
                ),
                RAWGGameTag(
                    id = 25,
                    name = "Space",
                    slug = "space",
                    language = "eng",
                    gamesCount = 43383,
                    imageUrl = "https://media.rawg.io/media/games/e1f/e1ffbeb1bac25b19749ad285ca29e158.jpg"
                ),
                RAWGGameTag(
                    id = 40838,
                    name = "Includes level editor",
                    slug = "includes-level-editor",
                    language = "eng",
                    gamesCount = 1607,
                    imageUrl = "https://media.rawg.io/media/games/9cc/9cc11e2e81403186c7fa9c00c143d6e4.jpg"
                ),
                RAWGGameTag(
                    id = 40833,
                    name = "Captions available",
                    slug = "captions-available",
                    language = "eng",
                    gamesCount = 1219,
                    imageUrl = "https://media.rawg.io/media/games/a12/a12f806432cb385bc286f0935c49cd14.jpg"
                ),
                RAWGGameTag(
                    id = 40834,
                    name = "Commentary available",
                    slug = "commentary-available",
                    language = "eng",
                    gamesCount = 252,
                    imageUrl = "https://media.rawg.io/media/screenshots/405/40567fe45e6074a5b2bfbd4a3fea7809.jpg"
                ),
                RAWGGameTag(
                    id = 87,
                    name = "Science",
                    slug = "science",
                    language = "eng",
                    gamesCount = 1510,
                    imageUrl = "https://media.rawg.io/media/screenshots/f93/f93ee651619bb5b273f1b51528ee872a.jpg"
                )
            ),
            screenshots = listOf(
                RAWGGameScreenshot(
                    id = -1,
                    imageUrl = "https://media.rawg.io/media/games/328/3283617cb7d75d67257fc58339188742.jpg"
                ),
                RAWGGameScreenshot(
                    id = 99018,
                    imageUrl = "https://media.rawg.io/media/screenshots/221/221a03c11e5ff9f765d62f60d4b4cbf5.jpg"
                ),
                RAWGGameScreenshot(
                    id = 99019,
                    imageUrl = "https://media.rawg.io/media/screenshots/173/1737ff43c14f40294011a209b1012875.jpg"
                ),
                RAWGGameScreenshot(
                    id = 99020,
                    imageUrl = "https://media.rawg.io/media/screenshots/b11/b11a2ae0664f0e8a1ef2346f99df26e1.jpg"
                ),
                RAWGGameScreenshot(
                    id = 99021,
                    imageUrl = "https://media.rawg.io/media/screenshots/9b1/9b107a790909b31918ebe2f40547cc85.jpg"
                ),
                RAWGGameScreenshot(
                    id = 99022,
                    imageUrl = "https://media.rawg.io/media/screenshots/d05/d058fc7f7fa6128916c311eb14267fed.jpg"
                ),
                RAWGGameScreenshot(
                    id = 99023,
                    imageUrl = "https://media.rawg.io/media/screenshots/415/41543dcc12dffc8e97d85a56ad42cda8.jpg"
                )
            ),
            genres = listOf(
                RAWGGameGenre(
                    id = 2,
                    name = "Shooter",
                    slug = "shooter",
                    gamesCount = 62981,
                    imageUrl = "https://media.rawg.io/media/games/328/3283617cb7d75d67257fc58339188742.jpg"
                ),
                RAWGGameGenre(
                    id = 7,
                    name = "Puzzle",
                    slug = "puzzle",
                    gamesCount = 100172,
                    imageUrl = "https://media.rawg.io/media/games/2e1/2e187b31e5cee21c110bd16798d75fab.jpg"
                )
            )
        ).toGame()
    )

    override suspend fun queryGames(queryParameters: GamesQueryParameters): Result<GamesPage, Throwable> {
        return Result.Success(
            GamesPage(
                count = 3,
                nextPageUrl = null,
                previousPageUrl = null,
                games = games
            )
        )
    }

    override suspend fun queryGamesGenres(): Result<GamesGenresPage, Throwable> {
        return Result.Success(
            GamesGenresPage(
                count = 10,
                nextPageUrl = null,
                previousPageUrl = null,
                genres = setOf(
                    GameGenre(
                        id = 1,
                        name = "Action",
                        slug = null,
                        gamesCount = 2000,
                        imageUrl = null
                    ),
                    GameGenre(
                        id = 2,
                        name = "Adventure",
                        slug = null,
                        gamesCount = 2000,
                        imageUrl = null
                    ),
                    GameGenre(
                        id = 3,
                        name = "Arcade",
                        slug = null,
                        gamesCount = 2000,
                        imageUrl = null
                    ),
                    GameGenre(
                        id = 4,
                        name = "Board games",
                        slug = null,
                        gamesCount = 2000,
                        imageUrl = null
                    ),
                    GameGenre(
                        id = 5,
                        name = "Educational",
                        slug = null,
                        gamesCount = 2000,
                        imageUrl = null
                    ),
                    GameGenre(
                        id = 6,
                        name = "Family",
                        slug = null,
                        gamesCount = 2000,
                        imageUrl = null
                    ),
                    GameGenre(
                        id = 7,
                        name = "Indie",
                        slug = null,
                        gamesCount = 2000,
                        imageUrl = null
                    ),
                    GameGenre(
                        id = 8,
                        name = "Massively Multiplayer",
                        slug = null,
                        gamesCount = 2000,
                        imageUrl = null
                    ),
                    GameGenre(
                        id = 9,
                        name = "Racing",
                        slug = null,
                        gamesCount = 2000,
                        imageUrl = null
                    ),
                    GameGenre(
                        id = 10,
                        name = "Simulation",
                        slug = null,
                        gamesCount = 2000,
                        imageUrl = null
                    )
                )
            )
        )
    }
}
