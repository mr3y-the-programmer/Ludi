package com.mr3y.ludi.core.network.datasources.internal

import com.mr3y.ludi.core.network.model.DetailedRAWGPlatformInfo
import com.mr3y.ludi.core.network.model.RAWGGameGenre
import com.mr3y.ludi.core.network.model.RAWGGameScreenshot
import com.mr3y.ludi.core.network.model.RAWGGameTag
import com.mr3y.ludi.core.network.model.RAWGPage
import com.mr3y.ludi.core.network.model.RAWGPlatformProperties
import com.mr3y.ludi.core.network.model.RAWGPlatformRequirements
import com.mr3y.ludi.core.network.model.RAWGShallowGame
import com.mr3y.ludi.core.network.model.RAWGStoreProperties
import com.mr3y.ludi.core.network.model.ShallowRAWGPlatformInfo
import com.mr3y.ludi.core.network.model.ShallowRAWGStoreInfo
import com.mr3y.ludi.core.network.model.ShallowRAWGStoreInfoWithId
import okhttp3.mockwebserver.MockResponse

val serializedMockResponseA = MockResponse()
    .setBody(
        """{
    "count": 886906,
    "next": "https://api.rawg.io/api/games?key=b10a96c96f124283b10cf6f1aa18f973&page=2&page_size=3",
    "previous": null,
    "results": [
        {
            "id": 3498,
            "slug": "grand-theft-auto-v",
            "name": "Grand Theft Auto V",
            "released": "2013-09-17",
            "tba": false,
            "background_image": "https://media.rawg.io/media/games/456/456dea5e1c7e3cd07060c14e96612001.jpg",
            "rating": 4.47,
            "rating_top": 5,
            "ratings": [
                {
                    "id": 5,
                    "title": "exceptional",
                    "count": 3726,
                    "percent": 59.07
                },
                {
                    "id": 4,
                    "title": "recommended",
                    "count": 2070,
                    "percent": 32.82
                },
                {
                    "id": 3,
                    "title": "meh",
                    "count": 398,
                    "percent": 6.31
                },
                {
                    "id": 1,
                    "title": "skip",
                    "count": 114,
                    "percent": 1.81
                }
            ],
            "ratings_count": 6223,
            "reviews_text_count": 47,
            "added": 19057,
            "added_by_status": {
                "yet": 492,
                "owned": 11035,
                "beaten": 5308,
                "toplay": 555,
                "dropped": 986,
                "playing": 681
            },
            "metacritic": 92,
            "playtime": 73,
            "suggestions_count": 410,
            "updated": "2023-03-18T19:06:53",
            "user_game": null,
            "reviews_count": 6308,
            "saturated_color": "0f0f0f",
            "dominant_color": "0f0f0f",
            "platforms": [
                {
                    "platform": {
                        "id": 187,
                        "name": "PlayStation 5",
                        "slug": "playstation5",
                        "image": null,
                        "year_end": null,
                        "year_start": 2020,
                        "games_count": 824,
                        "image_background": "https://media.rawg.io/media/games/253/2534a46f3da7fa7c315f1387515ca393.jpg"
                    },
                    "released_at": "2013-09-17",
                    "requirements_en": null,
                    "requirements_ru": null
                },
                {
                    "platform": {
                        "id": 186,
                        "name": "Xbox Series S/X",
                        "slug": "xbox-series-x",
                        "image": null,
                        "year_end": null,
                        "year_start": 2020,
                        "games_count": 731,
                        "image_background": "https://media.rawg.io/media/games/618/618c2031a07bbff6b4f611f10b6bcdbc.jpg"
                    },
                    "released_at": "2013-09-17",
                    "requirements_en": null,
                    "requirements_ru": null
                },
                {
                    "platform": {
                        "id": 18,
                        "name": "PlayStation 4",
                        "slug": "playstation4",
                        "image": null,
                        "year_end": null,
                        "year_start": null,
                        "games_count": 6597,
                        "image_background": "https://media.rawg.io/media/games/b72/b7233d5d5b1e75e86bb860ccc7aeca85.jpg"
                    },
                    "released_at": "2013-09-17",
                    "requirements_en": null,
                    "requirements_ru": null
                },
                {
                    "platform": {
                        "id": 4,
                        "name": "PC",
                        "slug": "pc",
                        "image": null,
                        "year_end": null,
                        "year_start": null,
                        "games_count": 536875,
                        "image_background": "https://media.rawg.io/media/games/6fc/6fcf4cd3b17c288821388e6085bb0fc9.jpg"
                    },
                    "released_at": "2013-09-17",
                    "requirements_en": {
                        "minimum": "Minimum:OS: Windows 10 64 Bit, Windows 8.1 64 Bit, Windows 8 64 Bit, Windows 7 64 Bit Service Pack 1, Windows Vista 64 Bit Service Pack 2* (*NVIDIA video card recommended if running Vista OS)Processor: Intel Core 2 Quad CPU Q6600 @ 2.40GHz (4 CPUs) / AMD Phenom 9850 Quad-Core Processor (4 CPUs) @ 2.5GHzMemory: 4 GB RAMGraphics: NVIDIA 9800 GT 1GB / AMD HD 4870 1GB (DX 10, 10.1, 11)Storage: 72 GB available spaceSound Card: 100% DirectX 10 compatibleAdditional Notes: Over time downloadable content and programming changes will change the system requirements for this game.  Please refer to your hardware manufacturer and www.rockstargames.com/support for current compatibility information. Some system components such as mobile chipsets, integrated, and AGP graphics cards may be incompatible. Unlisted specifications may not be supported by publisher.     Other requirements:  Installation and online play requires log-in to Rockstar Games Social Club (13+) network; internet connection required for activation, online play, and periodic entitlement verification; software installations required including Rockstar Games Social Club platform, DirectX , Chromium, and Microsoft Visual C++ 2008 sp1 Redistributable Package, and authentication software that recognizes certain hardware attributes for entitlement, digital rights management, system, and other support purposes.     SINGLE USE SERIAL CODE REGISTRATION VIA INTERNET REQUIRED; REGISTRATION IS LIMITED TO ONE ROCKSTAR GAMES SOCIAL CLUB ACCOUNT (13+) PER SERIAL CODE; ONLY ONE PC LOG-IN ALLOWED PER SOCIAL CLUB ACCOUNT AT ANY TIME; SERIAL CODE(S) ARE NON-TRANSFERABLE ONCE USED; SOCIAL CLUB ACCOUNTS ARE NON-TRANSFERABLE.  Partner Requirements:  Please check the terms of service of this site before purchasing this software.",
                        "recommended": "Recommended:OS: Windows 10 64 Bit, Windows 8.1 64 Bit, Windows 8 64 Bit, Windows 7 64 Bit Service Pack 1Processor: Intel Core i5 3470 @ 3.2GHz (4 CPUs) / AMD X8 FX-8350 @ 4GHz (8 CPUs)Memory: 8 GB RAMGraphics: NVIDIA GTX 660 2GB / AMD HD 7870 2GBStorage: 72 GB available spaceSound Card: 100% DirectX 10 compatibleAdditional Notes:"
                    },
                    "requirements_ru": null
                },
                {
                    "platform": {
                        "id": 16,
                        "name": "PlayStation 3",
                        "slug": "playstation3",
                        "image": null,
                        "year_end": null,
                        "year_start": null,
                        "games_count": 3269,
                        "image_background": "https://media.rawg.io/media/games/234/23410661770ae13eac11066980834367.jpg"
                    },
                    "released_at": "2013-09-17",
                    "requirements_en": null,
                    "requirements_ru": null
                },
                {
                    "platform": {
                        "id": 14,
                        "name": "Xbox 360",
                        "slug": "xbox360",
                        "image": null,
                        "year_end": null,
                        "year_start": null,
                        "games_count": 2781,
                        "image_background": "https://media.rawg.io/media/games/5c0/5c0dd63002cb23f804aab327d40ef119.jpg"
                    },
                    "released_at": "2013-09-17",
                    "requirements_en": null,
                    "requirements_ru": null
                },
                {
                    "platform": {
                        "id": 1,
                        "name": "Xbox One",
                        "slug": "xbox-one",
                        "image": null,
                        "year_end": null,
                        "year_start": null,
                        "games_count": 5488,
                        "image_background": "https://media.rawg.io/media/games/562/562553814dd54e001a541e4ee83a591c.jpg"
                    },
                    "released_at": "2013-09-17",
                    "requirements_en": null,
                    "requirements_ru": null
                }
            ],
            "parent_platforms": [
                {
                    "platform": {
                        "id": 1,
                        "name": "PC",
                        "slug": "pc"
                    }
                },
                {
                    "platform": {
                        "id": 2,
                        "name": "PlayStation",
                        "slug": "playstation"
                    }
                },
                {
                    "platform": {
                        "id": 3,
                        "name": "Xbox",
                        "slug": "xbox"
                    }
                }
            ],
            "genres": [
                {
                    "id": 4,
                    "name": "Action",
                    "slug": "action",
                    "games_count": 177875,
                    "image_background": "https://media.rawg.io/media/games/b8c/b8c243eaa0fbac8115e0cdccac3f91dc.jpg"
                },
                {
                    "id": 3,
                    "name": "Adventure",
                    "slug": "adventure",
                    "games_count": 136825,
                    "image_background": "https://media.rawg.io/media/games/995/9951d9d55323d08967640f7b9ab3e342.jpg"
                }
            ],
            "stores": [
                {
                    "id": 290375,
                    "store": {
                        "id": 3,
                        "name": "PlayStation Store",
                        "slug": "playstation-store",
                        "domain": "store.playstation.com",
                        "games_count": 7796,
                        "image_background": "https://media.rawg.io/media/games/bc0/bc06a29ceac58652b684deefe7d56099.jpg"
                    }
                },
                {
                    "id": 438095,
                    "store": {
                        "id": 11,
                        "name": "Epic Games",
                        "slug": "epic-games",
                        "domain": "epicgames.com",
                        "games_count": 1215,
                        "image_background": "https://media.rawg.io/media/games/b54/b54598d1d5cc31899f4f0a7e3122a7b0.jpg"
                    }
                },
                {
                    "id": 290376,
                    "store": {
                        "id": 1,
                        "name": "Steam",
                        "slug": "steam",
                        "domain": "store.steampowered.com",
                        "games_count": 73115,
                        "image_background": "https://media.rawg.io/media/games/120/1201a40e4364557b124392ee50317b99.jpg"
                    }
                },
                {
                    "id": 290377,
                    "store": {
                        "id": 7,
                        "name": "Xbox 360 Store",
                        "slug": "xbox360",
                        "domain": "marketplace.xbox.com",
                        "games_count": 1914,
                        "image_background": "https://media.rawg.io/media/games/e2d/e2d3f396b16dded0f841c17c9799a882.jpg"
                    }
                },
                {
                    "id": 290378,
                    "store": {
                        "id": 2,
                        "name": "Xbox Store",
                        "slug": "xbox-store",
                        "domain": "microsoft.com",
                        "games_count": 4756,
                        "image_background": "https://media.rawg.io/media/games/26d/26d4437715bee60138dab4a7c8c59c92.jpg"
                    }
                }
            ],
            "clip": null,
            "tags": [
                {
                    "id": 31,
                    "name": "Singleplayer",
                    "slug": "singleplayer",
                    "language": "eng",
                    "games_count": 209582,
                    "image_background": "https://media.rawg.io/media/games/73e/73eecb8909e0c39fb246f457b5d6cbbe.jpg"
                },
                {
                    "id": 40847,
                    "name": "Steam Achievements",
                    "slug": "steam-achievements",
                    "language": "eng",
                    "games_count": 29451,
                    "image_background": "https://media.rawg.io/media/games/8cc/8cce7c0e99dcc43d66c8efd42f9d03e3.jpg"
                },
                {
                    "id": 7,
                    "name": "Multiplayer",
                    "slug": "multiplayer",
                    "language": "eng",
                    "games_count": 35586,
                    "image_background": "https://media.rawg.io/media/games/328/3283617cb7d75d67257fc58339188742.jpg"
                },
                {
                    "id": 40836,
                    "name": "Full controller support",
                    "slug": "full-controller-support",
                    "language": "eng",
                    "games_count": 13833,
                    "image_background": "https://media.rawg.io/media/games/490/49016e06ae2103881ff6373248843069.jpg"
                },
                {
                    "id": 13,
                    "name": "Atmospheric",
                    "slug": "atmospheric",
                    "language": "eng",
                    "games_count": 29424,
                    "image_background": "https://media.rawg.io/media/games/7cf/7cfc9220b401b7a300e409e539c9afd5.jpg"
                },
                {
                    "id": 42,
                    "name": "Great Soundtrack",
                    "slug": "great-soundtrack",
                    "language": "eng",
                    "games_count": 3230,
                    "image_background": "https://media.rawg.io/media/games/7a2/7a2500ee8b2c0e1ff268bb4479463dea.jpg"
                },
                {
                    "id": 24,
                    "name": "RPG",
                    "slug": "rpg",
                    "language": "eng",
                    "games_count": 16758,
                    "image_background": "https://media.rawg.io/media/games/6cd/6cd653e0aaef5ff8bbd295bf4bcb12eb.jpg"
                },
                {
                    "id": 18,
                    "name": "Co-op",
                    "slug": "co-op",
                    "language": "eng",
                    "games_count": 9686,
                    "image_background": "https://media.rawg.io/media/games/8cc/8cce7c0e99dcc43d66c8efd42f9d03e3.jpg"
                },
                {
                    "id": 36,
                    "name": "Open World",
                    "slug": "open-world",
                    "language": "eng",
                    "games_count": 6237,
                    "image_background": "https://media.rawg.io/media/games/ee3/ee3e10193aafc3230ba1cae426967d10.jpg"
                },
                {
                    "id": 411,
                    "name": "cooperative",
                    "slug": "cooperative",
                    "language": "eng",
                    "games_count": 3925,
                    "image_background": "https://media.rawg.io/media/games/d0f/d0f91fe1d92332147e5db74e207cfc7a.jpg"
                },
                {
                    "id": 8,
                    "name": "First-Person",
                    "slug": "first-person",
                    "language": "eng",
                    "games_count": 29119,
                    "image_background": "https://media.rawg.io/media/games/c4b/c4b0cab189e73432de3a250d8cf1c84e.jpg"
                },
                {
                    "id": 149,
                    "name": "Third Person",
                    "slug": "third-person",
                    "language": "eng",
                    "games_count": 9288,
                    "image_background": "https://media.rawg.io/media/games/951/951572a3dd1e42544bd39a5d5b42d234.jpg"
                },
                {
                    "id": 4,
                    "name": "Funny",
                    "slug": "funny",
                    "language": "eng",
                    "games_count": 23019,
                    "image_background": "https://media.rawg.io/media/games/5bb/5bb55ccb8205aadbb6a144cf6d8963f1.jpg"
                },
                {
                    "id": 37,
                    "name": "Sandbox",
                    "slug": "sandbox",
                    "language": "eng",
                    "games_count": 5963,
                    "image_background": "https://media.rawg.io/media/games/25c/25c4776ab5723d5d735d8bf617ca12d9.jpg"
                },
                {
                    "id": 123,
                    "name": "Comedy",
                    "slug": "comedy",
                    "language": "eng",
                    "games_count": 10907,
                    "image_background": "https://media.rawg.io/media/games/48c/48cb04ca483be865e3a83119c94e6097.jpg"
                },
                {
                    "id": 150,
                    "name": "Third-Person Shooter",
                    "slug": "third-person-shooter",
                    "language": "eng",
                    "games_count": 2884,
                    "image_background": "https://media.rawg.io/media/games/b45/b45575f34285f2c4479c9a5f719d972e.jpg"
                },
                {
                    "id": 62,
                    "name": "Moddable",
                    "slug": "moddable",
                    "language": "eng",
                    "games_count": 778,
                    "image_background": "https://media.rawg.io/media/games/48e/48e63bbddeddbe9ba81942772b156664.jpg"
                },
                {
                    "id": 144,
                    "name": "Crime",
                    "slug": "crime",
                    "language": "eng",
                    "games_count": 2544,
                    "image_background": "https://media.rawg.io/media/games/26d/26d4437715bee60138dab4a7c8c59c92.jpg"
                },
                {
                    "id": 62349,
                    "name": "vr mod",
                    "slug": "vr-mod",
                    "language": "eng",
                    "games_count": 17,
                    "image_background": "https://media.rawg.io/media/screenshots/1bb/1bb3f78f0fe43b5d5ca2f3da5b638840.jpg"
                }
            ],
            "esrb_rating": {
                "id": 4,
                "name": "Mature",
                "slug": "mature"
            },
            "short_screenshots": [
                {
                    "id": -1,
                    "image": "https://media.rawg.io/media/games/456/456dea5e1c7e3cd07060c14e96612001.jpg"
                },
                {
                    "id": 1827221,
                    "image": "https://media.rawg.io/media/screenshots/a7c/a7c43871a54bed6573a6a429451564ef.jpg"
                },
                {
                    "id": 1827222,
                    "image": "https://media.rawg.io/media/screenshots/cf4/cf4367daf6a1e33684bf19adb02d16d6.jpg"
                },
                {
                    "id": 1827223,
                    "image": "https://media.rawg.io/media/screenshots/f95/f9518b1d99210c0cae21fc09e95b4e31.jpg"
                },
                {
                    "id": 1827225,
                    "image": "https://media.rawg.io/media/screenshots/a5c/a5c95ea539c87d5f538763e16e18fb99.jpg"
                },
                {
                    "id": 1827226,
                    "image": "https://media.rawg.io/media/screenshots/a7e/a7e990bc574f4d34e03b5926361d1ee7.jpg"
                },
                {
                    "id": 1827227,
                    "image": "https://media.rawg.io/media/screenshots/592/592e2501d8734b802b2a34fee2df59fa.jpg"
                }
            ]
        },
        {
            "id": 3328,
            "slug": "the-witcher-3-wild-hunt",
            "name": "The Witcher 3: Wild Hunt",
            "released": "2015-05-18",
            "tba": false,
            "background_image": "https://media.rawg.io/media/games/618/618c2031a07bbff6b4f611f10b6bcdbc.jpg",
            "rating": 4.66,
            "rating_top": 5,
            "ratings": [
                {
                    "id": 5,
                    "title": "exceptional",
                    "count": 4645,
                    "percent": 77.58
                },
                {
                    "id": 4,
                    "title": "recommended",
                    "count": 949,
                    "percent": 15.85
                },
                {
                    "id": 3,
                    "title": "meh",
                    "count": 244,
                    "percent": 4.08
                },
                {
                    "id": 1,
                    "title": "skip",
                    "count": 149,
                    "percent": 2.49
                }
            ],
            "ratings_count": 5901,
            "reviews_text_count": 59,
            "added": 18203,
            "added_by_status": {
                "yet": 1038,
                "owned": 10488,
                "beaten": 4302,
                "toplay": 717,
                "dropped": 820,
                "playing": 838
            },
            "metacritic": 92,
            "playtime": 46,
            "suggestions_count": 649,
            "updated": "2023-03-18T19:56:50",
            "user_game": null,
            "reviews_count": 5987,
            "saturated_color": "0f0f0f",
            "dominant_color": "0f0f0f",
            "platforms": [
                {
                    "platform": {
                        "id": 186,
                        "name": "Xbox Series S/X",
                        "slug": "xbox-series-x",
                        "image": null,
                        "year_end": null,
                        "year_start": 2020,
                        "games_count": 731,
                        "image_background": "https://media.rawg.io/media/games/618/618c2031a07bbff6b4f611f10b6bcdbc.jpg"
                    },
                    "released_at": "2015-05-18",
                    "requirements_en": null,
                    "requirements_ru": null
                },
                {
                    "platform": {
                        "id": 18,
                        "name": "PlayStation 4",
                        "slug": "playstation4",
                        "image": null,
                        "year_end": null,
                        "year_start": null,
                        "games_count": 6597,
                        "image_background": "https://media.rawg.io/media/games/b72/b7233d5d5b1e75e86bb860ccc7aeca85.jpg"
                    },
                    "released_at": "2015-05-18",
                    "requirements_en": null,
                    "requirements_ru": null
                },
                {
                    "platform": {
                        "id": 7,
                        "name": "Nintendo Switch",
                        "slug": "nintendo-switch",
                        "image": null,
                        "year_end": null,
                        "year_start": null,
                        "games_count": 5199,
                        "image_background": "https://media.rawg.io/media/games/b72/b7233d5d5b1e75e86bb860ccc7aeca85.jpg"
                    },
                    "released_at": "2015-05-18",
                    "requirements_en": null,
                    "requirements_ru": null
                },
                {
                    "platform": {
                        "id": 4,
                        "name": "PC",
                        "slug": "pc",
                        "image": null,
                        "year_end": null,
                        "year_start": null,
                        "games_count": 536875,
                        "image_background": "https://media.rawg.io/media/games/6fc/6fcf4cd3b17c288821388e6085bb0fc9.jpg"
                    },
                    "released_at": "2015-05-18",
                    "requirements_en": null,
                    "requirements_ru": null
                },
                {
                    "platform": {
                        "id": 1,
                        "name": "Xbox One",
                        "slug": "xbox-one",
                        "image": null,
                        "year_end": null,
                        "year_start": null,
                        "games_count": 5488,
                        "image_background": "https://media.rawg.io/media/games/562/562553814dd54e001a541e4ee83a591c.jpg"
                    },
                    "released_at": "2015-05-18",
                    "requirements_en": null,
                    "requirements_ru": null
                },
                {
                    "platform": {
                        "id": 187,
                        "name": "PlayStation 5",
                        "slug": "playstation5",
                        "image": null,
                        "year_end": null,
                        "year_start": 2020,
                        "games_count": 824,
                        "image_background": "https://media.rawg.io/media/games/253/2534a46f3da7fa7c315f1387515ca393.jpg"
                    },
                    "released_at": "2015-05-18",
                    "requirements_en": null,
                    "requirements_ru": null
                }
            ],
            "parent_platforms": [
                {
                    "platform": {
                        "id": 1,
                        "name": "PC",
                        "slug": "pc"
                    }
                },
                {
                    "platform": {
                        "id": 2,
                        "name": "PlayStation",
                        "slug": "playstation"
                    }
                },
                {
                    "platform": {
                        "id": 3,
                        "name": "Xbox",
                        "slug": "xbox"
                    }
                },
                {
                    "platform": {
                        "id": 7,
                        "name": "Nintendo",
                        "slug": "nintendo"
                    }
                }
            ],
            "genres": [
                {
                    "id": 4,
                    "name": "Action",
                    "slug": "action",
                    "games_count": 177875,
                    "image_background": "https://media.rawg.io/media/games/b8c/b8c243eaa0fbac8115e0cdccac3f91dc.jpg"
                },
                {
                    "id": 3,
                    "name": "Adventure",
                    "slug": "adventure",
                    "games_count": 136825,
                    "image_background": "https://media.rawg.io/media/games/995/9951d9d55323d08967640f7b9ab3e342.jpg"
                },
                {
                    "id": 5,
                    "name": "RPG",
                    "slug": "role-playing-games-rpg",
                    "games_count": 53787,
                    "image_background": "https://media.rawg.io/media/games/d1a/d1a2e99ade53494c6330a0ed945fe823.jpg"
                }
            ],
            "stores": [
                {
                    "id": 354780,
                    "store": {
                        "id": 5,
                        "name": "GOG",
                        "slug": "gog",
                        "domain": "gog.com",
                        "games_count": 4934,
                        "image_background": "https://media.rawg.io/media/games/4cf/4cfc6b7f1850590a4634b08bfab308ab.jpg"
                    }
                },
                {
                    "id": 3565,
                    "store": {
                        "id": 3,
                        "name": "PlayStation Store",
                        "slug": "playstation-store",
                        "domain": "store.playstation.com",
                        "games_count": 7796,
                        "image_background": "https://media.rawg.io/media/games/bc0/bc06a29ceac58652b684deefe7d56099.jpg"
                    }
                },
                {
                    "id": 305376,
                    "store": {
                        "id": 1,
                        "name": "Steam",
                        "slug": "steam",
                        "domain": "store.steampowered.com",
                        "games_count": 73115,
                        "image_background": "https://media.rawg.io/media/games/120/1201a40e4364557b124392ee50317b99.jpg"
                    }
                },
                {
                    "id": 312313,
                    "store": {
                        "id": 2,
                        "name": "Xbox Store",
                        "slug": "xbox-store",
                        "domain": "microsoft.com",
                        "games_count": 4756,
                        "image_background": "https://media.rawg.io/media/games/26d/26d4437715bee60138dab4a7c8c59c92.jpg"
                    }
                },
                {
                    "id": 676022,
                    "store": {
                        "id": 6,
                        "name": "Nintendo Store",
                        "slug": "nintendo",
                        "domain": "nintendo.com",
                        "games_count": 8862,
                        "image_background": "https://media.rawg.io/media/games/7c4/7c448374df84b607f67ce9182a3a3ca7.jpg"
                    }
                }
            ],
            "clip": null,
            "tags": [
                {
                    "id": 31,
                    "name": "Singleplayer",
                    "slug": "singleplayer",
                    "language": "eng",
                    "games_count": 209582,
                    "image_background": "https://media.rawg.io/media/games/73e/73eecb8909e0c39fb246f457b5d6cbbe.jpg"
                },
                {
                    "id": 40836,
                    "name": "Full controller support",
                    "slug": "full-controller-support",
                    "language": "eng",
                    "games_count": 13833,
                    "image_background": "https://media.rawg.io/media/games/490/49016e06ae2103881ff6373248843069.jpg"
                },
                {
                    "id": 13,
                    "name": "Atmospheric",
                    "slug": "atmospheric",
                    "language": "eng",
                    "games_count": 29424,
                    "image_background": "https://media.rawg.io/media/games/7cf/7cfc9220b401b7a300e409e539c9afd5.jpg"
                },
                {
                    "id": 42,
                    "name": "Great Soundtrack",
                    "slug": "great-soundtrack",
                    "language": "eng",
                    "games_count": 3230,
                    "image_background": "https://media.rawg.io/media/games/7a2/7a2500ee8b2c0e1ff268bb4479463dea.jpg"
                },
                {
                    "id": 24,
                    "name": "RPG",
                    "slug": "rpg",
                    "language": "eng",
                    "games_count": 16758,
                    "image_background": "https://media.rawg.io/media/games/6cd/6cd653e0aaef5ff8bbd295bf4bcb12eb.jpg"
                },
                {
                    "id": 118,
                    "name": "Story Rich",
                    "slug": "story-rich",
                    "language": "eng",
                    "games_count": 18114,
                    "image_background": "https://media.rawg.io/media/games/fc1/fc1307a2774506b5bd65d7e8424664a7.jpg"
                },
                {
                    "id": 36,
                    "name": "Open World",
                    "slug": "open-world",
                    "language": "eng",
                    "games_count": 6237,
                    "image_background": "https://media.rawg.io/media/games/ee3/ee3e10193aafc3230ba1cae426967d10.jpg"
                },
                {
                    "id": 149,
                    "name": "Third Person",
                    "slug": "third-person",
                    "language": "eng",
                    "games_count": 9288,
                    "image_background": "https://media.rawg.io/media/games/951/951572a3dd1e42544bd39a5d5b42d234.jpg"
                },
                {
                    "id": 64,
                    "name": "Fantasy",
                    "slug": "fantasy",
                    "language": "eng",
                    "games_count": 24635,
                    "image_background": "https://media.rawg.io/media/screenshots/88b/88b5f27f07d6ca2f8a3cd0b36e03a670.jpg"
                },
                {
                    "id": 37,
                    "name": "Sandbox",
                    "slug": "sandbox",
                    "language": "eng",
                    "games_count": 5963,
                    "image_background": "https://media.rawg.io/media/games/25c/25c4776ab5723d5d735d8bf617ca12d9.jpg"
                },
                {
                    "id": 97,
                    "name": "Action RPG",
                    "slug": "action-rpg",
                    "language": "eng",
                    "games_count": 5774,
                    "image_background": "https://media.rawg.io/media/games/849/849414b978db37d4563ff9e4b0d3a787.jpg"
                },
                {
                    "id": 41,
                    "name": "Dark",
                    "slug": "dark",
                    "language": "eng",
                    "games_count": 14252,
                    "image_background": "https://media.rawg.io/media/games/4e6/4e6e8e7f50c237d76f38f3c885dae3d2.jpg"
                },
                {
                    "id": 44,
                    "name": "Nudity",
                    "slug": "nudity",
                    "language": "eng",
                    "games_count": 4681,
                    "image_background": "https://media.rawg.io/media/games/16b/16b1b7b36e2042d1128d5a3e852b3b2f.jpg"
                },
                {
                    "id": 336,
                    "name": "controller support",
                    "slug": "controller-support",
                    "language": "eng",
                    "games_count": 293,
                    "image_background": "https://media.rawg.io/media/games/f46/f466571d536f2e3ea9e815ad17177501.jpg"
                },
                {
                    "id": 145,
                    "name": "Choices Matter",
                    "slug": "choices-matter",
                    "language": "eng",
                    "games_count": 3254,
                    "image_background": "https://media.rawg.io/media/games/562/562553814dd54e001a541e4ee83a591c.jpg"
                },
                {
                    "id": 192,
                    "name": "Mature",
                    "slug": "mature",
                    "language": "eng",
                    "games_count": 2001,
                    "image_background": "https://media.rawg.io/media/games/5fa/5fae5fec3c943179e09da67a4427d68f.jpg"
                },
                {
                    "id": 40,
                    "name": "Dark Fantasy",
                    "slug": "dark-fantasy",
                    "language": "eng",
                    "games_count": 3190,
                    "image_background": "https://media.rawg.io/media/games/da1/da1b267764d77221f07a4386b6548e5a.jpg"
                },
                {
                    "id": 66,
                    "name": "Medieval",
                    "slug": "medieval",
                    "language": "eng",
                    "games_count": 5299,
                    "image_background": "https://media.rawg.io/media/games/ee3/ee3e10193aafc3230ba1cae426967d10.jpg"
                },
                {
                    "id": 82,
                    "name": "Magic",
                    "slug": "magic",
                    "language": "eng",
                    "games_count": 8040,
                    "image_background": "https://media.rawg.io/media/screenshots/6d3/6d367773c06886535620f2d7fb1cb866.jpg"
                },
                {
                    "id": 218,
                    "name": "Multiple Endings",
                    "slug": "multiple-endings",
                    "language": "eng",
                    "games_count": 6936,
                    "image_background": "https://media.rawg.io/media/games/951/951572a3dd1e42544bd39a5d5b42d234.jpg"
                }
            ],
            "esrb_rating": {
                "id": 4,
                "name": "Mature",
                "slug": "mature"
            },
            "short_screenshots": [
                {
                    "id": -1,
                    "image": "https://media.rawg.io/media/games/618/618c2031a07bbff6b4f611f10b6bcdbc.jpg"
                },
                {
                    "id": 30336,
                    "image": "https://media.rawg.io/media/screenshots/1ac/1ac19f31974314855ad7be266adeb500.jpg"
                },
                {
                    "id": 30337,
                    "image": "https://media.rawg.io/media/screenshots/6a0/6a08afca95261a2fe221ea9e01d28762.jpg"
                },
                {
                    "id": 30338,
                    "image": "https://media.rawg.io/media/screenshots/cdd/cdd31b6b4a687425a87b5ce231ac89d7.jpg"
                },
                {
                    "id": 30339,
                    "image": "https://media.rawg.io/media/screenshots/862/862397b153221a625922d3bb66337834.jpg"
                },
                {
                    "id": 30340,
                    "image": "https://media.rawg.io/media/screenshots/166/166787c442a45f52f4f230c33fd7d605.jpg"
                },
                {
                    "id": 30342,
                    "image": "https://media.rawg.io/media/screenshots/f63/f6373ee614046d81503d63f167181803.jpg"
                }
            ]
        },
        {
            "id": 4200,
            "slug": "portal-2",
            "name": "Portal 2",
            "released": "2011-04-18",
            "tba": false,
            "background_image": "https://media.rawg.io/media/games/328/3283617cb7d75d67257fc58339188742.jpg",
            "rating": 4.62,
            "rating_top": 5,
            "ratings": [
                {
                    "id": 5,
                    "title": "exceptional",
                    "count": 3684,
                    "percent": 70.63
                },
                {
                    "id": 4,
                    "title": "recommended",
                    "count": 1285,
                    "percent": 24.64
                },
                {
                    "id": 3,
                    "title": "meh",
                    "count": 140,
                    "percent": 2.68
                },
                {
                    "id": 1,
                    "title": "skip",
                    "count": 107,
                    "percent": 2.05
                }
            ],
            "ratings_count": 5170,
            "reviews_text_count": 31,
            "added": 17148,
            "added_by_status": {
                "yet": 565,
                "owned": 10591,
                "beaten": 4996,
                "toplay": 332,
                "dropped": 526,
                "playing": 138
            },
            "metacritic": 95,
            "playtime": 11,
            "suggestions_count": 534,
            "updated": "2023-03-18T16:37:18",
            "user_game": null,
            "reviews_count": 5216,
            "saturated_color": "0f0f0f",
            "dominant_color": "0f0f0f",
            "platforms": [
                {
                    "platform": {
                        "id": 14,
                        "name": "Xbox 360",
                        "slug": "xbox360",
                        "image": null,
                        "year_end": null,
                        "year_start": null,
                        "games_count": 2781,
                        "image_background": "https://media.rawg.io/media/games/5c0/5c0dd63002cb23f804aab327d40ef119.jpg"
                    },
                    "released_at": "2011-04-19",
                    "requirements_en": null,
                    "requirements_ru": null
                },
                {
                    "platform": {
                        "id": 6,
                        "name": "Linux",
                        "slug": "linux",
                        "image": null,
                        "year_end": null,
                        "year_start": null,
                        "games_count": 79193,
                        "image_background": "https://media.rawg.io/media/games/46d/46d98e6910fbc0706e2948a7cc9b10c5.jpg"
                    },
                    "released_at": "2011-04-19",
                    "requirements_en": null,
                    "requirements_ru": null
                },
                {
                    "platform": {
                        "id": 5,
                        "name": "macOS",
                        "slug": "macos",
                        "image": null,
                        "year_end": null,
                        "year_start": null,
                        "games_count": 106472,
                        "image_background": "https://media.rawg.io/media/games/960/960b601d9541cec776c5fa42a00bf6c4.jpg"
                    },
                    "released_at": null,
                    "requirements_en": null,
                    "requirements_ru": null
                },
                {
                    "platform": {
                        "id": 16,
                        "name": "PlayStation 3",
                        "slug": "playstation3",
                        "image": null,
                        "year_end": null,
                        "year_start": null,
                        "games_count": 3269,
                        "image_background": "https://media.rawg.io/media/games/234/23410661770ae13eac11066980834367.jpg"
                    },
                    "released_at": "2011-04-19",
                    "requirements_en": null,
                    "requirements_ru": null
                },
                {
                    "platform": {
                        "id": 4,
                        "name": "PC",
                        "slug": "pc",
                        "image": null,
                        "year_end": null,
                        "year_start": null,
                        "games_count": 536875,
                        "image_background": "https://media.rawg.io/media/games/6fc/6fcf4cd3b17c288821388e6085bb0fc9.jpg"
                    },
                    "released_at": null,
                    "requirements_en": null,
                    "requirements_ru": {
                        "minimum": "Core 2 Duo/Athlon X2 2 ГГц,1 Гб памяти,GeForce 7600/Radeon X800,10 Гб на винчестере,интернет-соединение",
                        "recommended": "Core 2 Duo/Athlon X2 2.5 ГГц,2 Гб памяти,GeForce GTX 280/Radeon HD 2600,10 Гб на винчестере,интернет-соединение"
                    }
                },
                {
                    "platform": {
                        "id": 1,
                        "name": "Xbox One",
                        "slug": "xbox-one",
                        "image": null,
                        "year_end": null,
                        "year_start": null,
                        "games_count": 5488,
                        "image_background": "https://media.rawg.io/media/games/562/562553814dd54e001a541e4ee83a591c.jpg"
                    },
                    "released_at": "2011-04-18",
                    "requirements_en": null,
                    "requirements_ru": null
                }
            ],
            "parent_platforms": [
                {
                    "platform": {
                        "id": 1,
                        "name": "PC",
                        "slug": "pc"
                    }
                },
                {
                    "platform": {
                        "id": 2,
                        "name": "PlayStation",
                        "slug": "playstation"
                    }
                },
                {
                    "platform": {
                        "id": 3,
                        "name": "Xbox",
                        "slug": "xbox"
                    }
                },
                {
                    "platform": {
                        "id": 5,
                        "name": "Apple Macintosh",
                        "slug": "mac"
                    }
                },
                {
                    "platform": {
                        "id": 6,
                        "name": "Linux",
                        "slug": "linux"
                    }
                }
            ],
            "genres": [
                {
                    "id": 2,
                    "name": "Shooter",
                    "slug": "shooter",
                    "games_count": 62981,
                    "image_background": "https://media.rawg.io/media/games/328/3283617cb7d75d67257fc58339188742.jpg"
                },
                {
                    "id": 7,
                    "name": "Puzzle",
                    "slug": "puzzle",
                    "games_count": 100172,
                    "image_background": "https://media.rawg.io/media/games/2e1/2e187b31e5cee21c110bd16798d75fab.jpg"
                }
            ],
            "stores": [
                {
                    "id": 465889,
                    "store": {
                        "id": 2,
                        "name": "Xbox Store",
                        "slug": "xbox-store",
                        "domain": "microsoft.com",
                        "games_count": 4756,
                        "image_background": "https://media.rawg.io/media/games/26d/26d4437715bee60138dab4a7c8c59c92.jpg"
                    }
                },
                {
                    "id": 13134,
                    "store": {
                        "id": 1,
                        "name": "Steam",
                        "slug": "steam",
                        "domain": "store.steampowered.com",
                        "games_count": 73115,
                        "image_background": "https://media.rawg.io/media/games/120/1201a40e4364557b124392ee50317b99.jpg"
                    }
                },
                {
                    "id": 4526,
                    "store": {
                        "id": 3,
                        "name": "PlayStation Store",
                        "slug": "playstation-store",
                        "domain": "store.playstation.com",
                        "games_count": 7796,
                        "image_background": "https://media.rawg.io/media/games/bc0/bc06a29ceac58652b684deefe7d56099.jpg"
                    }
                },
                {
                    "id": 33916,
                    "store": {
                        "id": 7,
                        "name": "Xbox 360 Store",
                        "slug": "xbox360",
                        "domain": "marketplace.xbox.com",
                        "games_count": 1914,
                        "image_background": "https://media.rawg.io/media/games/e2d/e2d3f396b16dded0f841c17c9799a882.jpg"
                    }
                }
            ],
            "clip": null,
            "tags": [
                {
                    "id": 31,
                    "name": "Singleplayer",
                    "slug": "singleplayer",
                    "language": "eng",
                    "games_count": 209582,
                    "image_background": "https://media.rawg.io/media/games/73e/73eecb8909e0c39fb246f457b5d6cbbe.jpg"
                },
                {
                    "id": 40847,
                    "name": "Steam Achievements",
                    "slug": "steam-achievements",
                    "language": "eng",
                    "games_count": 29451,
                    "image_background": "https://media.rawg.io/media/games/8cc/8cce7c0e99dcc43d66c8efd42f9d03e3.jpg"
                },
                {
                    "id": 7,
                    "name": "Multiplayer",
                    "slug": "multiplayer",
                    "language": "eng",
                    "games_count": 35586,
                    "image_background": "https://media.rawg.io/media/games/328/3283617cb7d75d67257fc58339188742.jpg"
                },
                {
                    "id": 40836,
                    "name": "Full controller support",
                    "slug": "full-controller-support",
                    "language": "eng",
                    "games_count": 13833,
                    "image_background": "https://media.rawg.io/media/games/490/49016e06ae2103881ff6373248843069.jpg"
                },
                {
                    "id": 13,
                    "name": "Atmospheric",
                    "slug": "atmospheric",
                    "language": "eng",
                    "games_count": 29424,
                    "image_background": "https://media.rawg.io/media/games/7cf/7cfc9220b401b7a300e409e539c9afd5.jpg"
                },
                {
                    "id": 40849,
                    "name": "Steam Cloud",
                    "slug": "steam-cloud",
                    "language": "eng",
                    "games_count": 13564,
                    "image_background": "https://media.rawg.io/media/games/49c/49c3dfa4ce2f6f140cc4825868e858cb.jpg"
                },
                {
                    "id": 7808,
                    "name": "steam-trading-cards",
                    "slug": "steam-trading-cards",
                    "language": "eng",
                    "games_count": 7582,
                    "image_background": "https://media.rawg.io/media/games/310/3106b0e012271c5ffb16497b070be739.jpg"
                },
                {
                    "id": 18,
                    "name": "Co-op",
                    "slug": "co-op",
                    "language": "eng",
                    "games_count": 9686,
                    "image_background": "https://media.rawg.io/media/games/8cc/8cce7c0e99dcc43d66c8efd42f9d03e3.jpg"
                },
                {
                    "id": 118,
                    "name": "Story Rich",
                    "slug": "story-rich",
                    "language": "eng",
                    "games_count": 18114,
                    "image_background": "https://media.rawg.io/media/games/fc1/fc1307a2774506b5bd65d7e8424664a7.jpg"
                },
                {
                    "id": 411,
                    "name": "cooperative",
                    "slug": "cooperative",
                    "language": "eng",
                    "games_count": 3925,
                    "image_background": "https://media.rawg.io/media/games/d0f/d0f91fe1d92332147e5db74e207cfc7a.jpg"
                },
                {
                    "id": 8,
                    "name": "First-Person",
                    "slug": "first-person",
                    "language": "eng",
                    "games_count": 29119,
                    "image_background": "https://media.rawg.io/media/games/c4b/c4b0cab189e73432de3a250d8cf1c84e.jpg"
                },
                {
                    "id": 32,
                    "name": "Sci-fi",
                    "slug": "sci-fi",
                    "language": "eng",
                    "games_count": 17348,
                    "image_background": "https://media.rawg.io/media/games/8e4/8e4de3f54ac659e08a7ba6a2b731682a.jpg"
                },
                {
                    "id": 30,
                    "name": "FPS",
                    "slug": "fps",
                    "language": "eng",
                    "games_count": 12564,
                    "image_background": "https://media.rawg.io/media/games/120/1201a40e4364557b124392ee50317b99.jpg"
                },
                {
                    "id": 9,
                    "name": "Online Co-Op",
                    "slug": "online-co-op",
                    "language": "eng",
                    "games_count": 4190,
                    "image_background": "https://media.rawg.io/media/games/d0f/d0f91fe1d92332147e5db74e207cfc7a.jpg"
                },
                {
                    "id": 4,
                    "name": "Funny",
                    "slug": "funny",
                    "language": "eng",
                    "games_count": 23019,
                    "image_background": "https://media.rawg.io/media/games/5bb/5bb55ccb8205aadbb6a144cf6d8963f1.jpg"
                },
                {
                    "id": 189,
                    "name": "Female Protagonist",
                    "slug": "female-protagonist",
                    "language": "eng",
                    "games_count": 10429,
                    "image_background": "https://media.rawg.io/media/games/021/021c4e21a1824d2526f925eff6324653.jpg"
                },
                {
                    "id": 123,
                    "name": "Comedy",
                    "slug": "comedy",
                    "language": "eng",
                    "games_count": 10907,
                    "image_background": "https://media.rawg.io/media/games/48c/48cb04ca483be865e3a83119c94e6097.jpg"
                },
                {
                    "id": 75,
                    "name": "Local Co-Op",
                    "slug": "local-co-op",
                    "language": "eng",
                    "games_count": 4977,
                    "image_background": "https://media.rawg.io/media/games/226/2262cea0b385db6cf399f4be831603b0.jpg"
                },
                {
                    "id": 11669,
                    "name": "stats",
                    "slug": "stats",
                    "language": "eng",
                    "games_count": 4436,
                    "image_background": "https://media.rawg.io/media/games/179/179245a3693049a11a25b900ab18f8f7.jpg"
                },
                {
                    "id": 40852,
                    "name": "Steam Workshop",
                    "slug": "steam-workshop",
                    "language": "eng",
                    "games_count": 1276,
                    "image_background": "https://media.rawg.io/media/games/f3e/f3eec35c6218dcfd93a537751e6bfa61.jpg"
                },
                {
                    "id": 25,
                    "name": "Space",
                    "slug": "space",
                    "language": "eng",
                    "games_count": 43383,
                    "image_background": "https://media.rawg.io/media/games/e1f/e1ffbeb1bac25b19749ad285ca29e158.jpg"
                },
                {
                    "id": 40838,
                    "name": "Includes level editor",
                    "slug": "includes-level-editor",
                    "language": "eng",
                    "games_count": 1607,
                    "image_background": "https://media.rawg.io/media/games/9cc/9cc11e2e81403186c7fa9c00c143d6e4.jpg"
                },
                {
                    "id": 40833,
                    "name": "Captions available",
                    "slug": "captions-available",
                    "language": "eng",
                    "games_count": 1219,
                    "image_background": "https://media.rawg.io/media/games/a12/a12f806432cb385bc286f0935c49cd14.jpg"
                },
                {
                    "id": 40834,
                    "name": "Commentary available",
                    "slug": "commentary-available",
                    "language": "eng",
                    "games_count": 252,
                    "image_background": "https://media.rawg.io/media/screenshots/405/40567fe45e6074a5b2bfbd4a3fea7809.jpg"
                },
                {
                    "id": 87,
                    "name": "Science",
                    "slug": "science",
                    "language": "eng",
                    "games_count": 1510,
                    "image_background": "https://media.rawg.io/media/screenshots/f93/f93ee651619bb5b273f1b51528ee872a.jpg"
                }
            ],
            "esrb_rating": {
                "id": 2,
                "name": "Everyone 10+",
                "slug": "everyone-10-plus"
            },
            "short_screenshots": [
                {
                    "id": -1,
                    "image": "https://media.rawg.io/media/games/328/3283617cb7d75d67257fc58339188742.jpg"
                },
                {
                    "id": 99018,
                    "image": "https://media.rawg.io/media/screenshots/221/221a03c11e5ff9f765d62f60d4b4cbf5.jpg"
                },
                {
                    "id": 99019,
                    "image": "https://media.rawg.io/media/screenshots/173/1737ff43c14f40294011a209b1012875.jpg"
                },
                {
                    "id": 99020,
                    "image": "https://media.rawg.io/media/screenshots/b11/b11a2ae0664f0e8a1ef2346f99df26e1.jpg"
                },
                {
                    "id": 99021,
                    "image": "https://media.rawg.io/media/screenshots/9b1/9b107a790909b31918ebe2f40547cc85.jpg"
                },
                {
                    "id": 99022,
                    "image": "https://media.rawg.io/media/screenshots/d05/d058fc7f7fa6128916c311eb14267fed.jpg"
                },
                {
                    "id": 99023,
                    "image": "https://media.rawg.io/media/screenshots/415/41543dcc12dffc8e97d85a56ad42cda8.jpg"
                }
            ]
        }
    ],
    "seo_title": "All Games",
    "seo_description": "",
    "seo_keywords": "",
    "seo_h1": "All Games",
    "noindex": false,
    "nofollow": false,
    "description": "",
    "filters": {
        "years": [
            {
                "from": 2020,
                "to": 2023,
                "filter": "2020-01-01,2023-12-31",
                "decade": 2020,
                "years": [
                    {
                        "year": 2023,
                        "count": 38460,
                        "nofollow": false
                    },
                    {
                        "year": 2022,
                        "count": 177959,
                        "nofollow": false
                    },
                    {
                        "year": 2021,
                        "count": 173238,
                        "nofollow": false
                    },
                    {
                        "year": 2020,
                        "count": 133047,
                        "nofollow": false
                    }
                ],
                "nofollow": true,
                "count": 522704
            },
            {
                "from": 2010,
                "to": 2019,
                "filter": "2010-01-01,2019-12-31",
                "decade": 2010,
                "years": [
                    {
                        "year": 2019,
                        "count": 79741,
                        "nofollow": false
                    },
                    {
                        "year": 2018,
                        "count": 71561,
                        "nofollow": false
                    },
                    {
                        "year": 2017,
                        "count": 56544,
                        "nofollow": true
                    },
                    {
                        "year": 2016,
                        "count": 41363,
                        "nofollow": true
                    },
                    {
                        "year": 2015,
                        "count": 26440,
                        "nofollow": true
                    },
                    {
                        "year": 2014,
                        "count": 15620,
                        "nofollow": true
                    },
                    {
                        "year": 2013,
                        "count": 6341,
                        "nofollow": true
                    },
                    {
                        "year": 2012,
                        "count": 5379,
                        "nofollow": true
                    },
                    {
                        "year": 2011,
                        "count": 4320,
                        "nofollow": true
                    },
                    {
                        "year": 2010,
                        "count": 3881,
                        "nofollow": true
                    }
                ],
                "nofollow": true,
                "count": 311190
            },
            {
                "from": 2000,
                "to": 2009,
                "filter": "2000-01-01,2009-12-31",
                "decade": 2000,
                "years": [
                    {
                        "year": 2009,
                        "count": 3108,
                        "nofollow": true
                    },
                    {
                        "year": 2008,
                        "count": 2030,
                        "nofollow": true
                    },
                    {
                        "year": 2007,
                        "count": 1553,
                        "nofollow": true
                    },
                    {
                        "year": 2006,
                        "count": 1280,
                        "nofollow": true
                    },
                    {
                        "year": 2005,
                        "count": 1148,
                        "nofollow": true
                    },
                    {
                        "year": 2004,
                        "count": 1157,
                        "nofollow": true
                    },
                    {
                        "year": 2003,
                        "count": 1137,
                        "nofollow": true
                    },
                    {
                        "year": 2002,
                        "count": 985,
                        "nofollow": true
                    },
                    {
                        "year": 2001,
                        "count": 1108,
                        "nofollow": true
                    },
                    {
                        "year": 2000,
                        "count": 999,
                        "nofollow": true
                    }
                ],
                "nofollow": true,
                "count": 14505
            },
            {
                "from": 1990,
                "to": 1999,
                "filter": "1990-01-01,1999-12-31",
                "decade": 1990,
                "years": [
                    {
                        "year": 1999,
                        "count": 780,
                        "nofollow": true
                    },
                    {
                        "year": 1998,
                        "count": 723,
                        "nofollow": true
                    },
                    {
                        "year": 1997,
                        "count": 872,
                        "nofollow": true
                    },
                    {
                        "year": 1996,
                        "count": 759,
                        "nofollow": true
                    },
                    {
                        "year": 1995,
                        "count": 861,
                        "nofollow": true
                    },
                    {
                        "year": 1994,
                        "count": 814,
                        "nofollow": true
                    },
                    {
                        "year": 1993,
                        "count": 744,
                        "nofollow": true
                    },
                    {
                        "year": 1992,
                        "count": 648,
                        "nofollow": true
                    },
                    {
                        "year": 1991,
                        "count": 578,
                        "nofollow": true
                    },
                    {
                        "year": 1990,
                        "count": 538,
                        "nofollow": true
                    }
                ],
                "nofollow": true,
                "count": 7317
            },
            {
                "from": 1980,
                "to": 1989,
                "filter": "1980-01-01,1989-12-31",
                "decade": 1980,
                "years": [
                    {
                        "year": 1989,
                        "count": 434,
                        "nofollow": true
                    },
                    {
                        "year": 1988,
                        "count": 317,
                        "nofollow": true
                    },
                    {
                        "year": 1987,
                        "count": 339,
                        "nofollow": true
                    },
                    {
                        "year": 1986,
                        "count": 248,
                        "nofollow": true
                    },
                    {
                        "year": 1985,
                        "count": 231,
                        "nofollow": true
                    },
                    {
                        "year": 1984,
                        "count": 185,
                        "nofollow": true
                    },
                    {
                        "year": 1983,
                        "count": 206,
                        "nofollow": true
                    },
                    {
                        "year": 1982,
                        "count": 148,
                        "nofollow": true
                    },
                    {
                        "year": 1981,
                        "count": 65,
                        "nofollow": true
                    },
                    {
                        "year": 1980,
                        "count": 35,
                        "nofollow": true
                    }
                ],
                "nofollow": true,
                "count": 2208
            },
            {
                "from": 1970,
                "to": 1979,
                "filter": "1970-01-01,1979-12-31",
                "decade": 1970,
                "years": [
                    {
                        "year": 1979,
                        "count": 15,
                        "nofollow": true
                    },
                    {
                        "year": 1978,
                        "count": 17,
                        "nofollow": true
                    },
                    {
                        "year": 1977,
                        "count": 13,
                        "nofollow": true
                    },
                    {
                        "year": 1976,
                        "count": 7,
                        "nofollow": true
                    },
                    {
                        "year": 1975,
                        "count": 3,
                        "nofollow": true
                    },
                    {
                        "year": 1974,
                        "count": 2,
                        "nofollow": true
                    },
                    {
                        "year": 1973,
                        "count": 1,
                        "nofollow": true
                    },
                    {
                        "year": 1972,
                        "count": 2,
                        "nofollow": true
                    },
                    {
                        "year": 1971,
                        "count": 6,
                        "nofollow": true
                    },
                    {
                        "year": 1970,
                        "count": 1,
                        "nofollow": true
                    }
                ],
                "nofollow": true,
                "count": 67
            },
            {
                "from": 1960,
                "to": 1969,
                "filter": "1960-01-01,1969-12-31",
                "decade": 1960,
                "years": [
                    {
                        "year": 1962,
                        "count": 1,
                        "nofollow": true
                    }
                ],
                "nofollow": true,
                "count": 1
            },
            {
                "from": 1950,
                "to": 1959,
                "filter": "1950-01-01,1959-12-31",
                "decade": 1950,
                "years": [
                    {
                        "year": 1958,
                        "count": 1,
                        "nofollow": true
                    },
                    {
                        "year": 1954,
                        "count": 2,
                        "nofollow": true
                    }
                ],
                "nofollow": true,
                "count": 3
            }
        ]
    },
    "nofollow_collections": [
        "stores"
    ]
}
        """.trimIndent()
    )

val deserializedMockResponseA = RAWGPage(
    count = 886906,
    nextPageUrl = "https://api.rawg.io/api/games?key=b10a96c96f124283b10cf6f1aa18f973&page=2&page_size=3",
    previousPageUrl = null,
    results = listOf(
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
        ),
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
        ),
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
        )
    )
)

val serializedMockResponseB = MockResponse()
    .setBody(
        """
            {
                "count": 149,
                "next": "https://api.rawg.io/api/games?dates=2019-09-01%2C2019-09-30&key=b10a96c96f124283b10cf6f1aa18f973&page=2&page_size=3&platforms=18%2C1%2C7",
                "previous": null,
                "results": [
                    {
                        "slug": "borderlands-3",
                        "name": "Borderlands 3",
                        "playtime": 10,
                        "platforms": [
                            {
                                "platform": {
                                    "id": 4,
                                    "name": "PC",
                                    "slug": "pc"
                                }
                            },
                            {
                                "platform": {
                                    "id": 187,
                                    "name": "PlayStation 5",
                                    "slug": "playstation5"
                                }
                            },
                            {
                                "platform": {
                                    "id": 1,
                                    "name": "Xbox One",
                                    "slug": "xbox-one"
                                }
                            },
                            {
                                "platform": {
                                    "id": 18,
                                    "name": "PlayStation 4",
                                    "slug": "playstation4"
                                }
                            },
                            {
                                "platform": {
                                    "id": 186,
                                    "name": "Xbox Series S/X",
                                    "slug": "xbox-series-x"
                                }
                            }
                        ],
                        "stores": [
                            {
                                "store": {
                                    "id": 1,
                                    "name": "Steam",
                                    "slug": "steam"
                                }
                            },
                            {
                                "store": {
                                    "id": 3,
                                    "name": "PlayStation Store",
                                    "slug": "playstation-store"
                                }
                            },
                            {
                                "store": {
                                    "id": 2,
                                    "name": "Xbox Store",
                                    "slug": "xbox-store"
                                }
                            },
                            {
                                "store": {
                                    "id": 11,
                                    "name": "Epic Games",
                                    "slug": "epic-games"
                                }
                            }
                        ],
                        "released": "2019-09-13",
                        "tba": false,
                        "background_image": "https://media.rawg.io/media/games/9f1/9f1891779cb20f44de93cef33b067e50.jpg",
                        "rating": 3.9,
                        "rating_top": 4,
                        "ratings": [
                            {
                                "id": 4,
                                "title": "recommended",
                                "count": 395,
                                "percent": 50.9
                            },
                            {
                                "id": 5,
                                "title": "exceptional",
                                "count": 193,
                                "percent": 24.87
                            },
                            {
                                "id": 3,
                                "title": "meh",
                                "count": 146,
                                "percent": 18.81
                            },
                            {
                                "id": 1,
                                "title": "skip",
                                "count": 42,
                                "percent": 5.41
                            }
                        ],
                        "ratings_count": 761,
                        "reviews_text_count": 12,
                        "added": 5008,
                        "added_by_status": {
                            "yet": 371,
                            "owned": 3170,
                            "beaten": 483,
                            "toplay": 547,
                            "dropped": 245,
                            "playing": 192
                        },
                        "metacritic": 83,
                        "suggestions_count": 917,
                        "updated": "2023-03-19T19:53:43",
                        "id": 58617,
                        "score": null,
                        "clip": null,
                        "tags": [
                            {
                                "id": 31,
                                "name": "Singleplayer",
                                "slug": "singleplayer",
                                "language": "eng",
                                "games_count": 209582,
                                "image_background": "https://media.rawg.io/media/games/73e/73eecb8909e0c39fb246f457b5d6cbbe.jpg"
                            },
                            {
                                "id": 42396,
                                "name": "Для одного игрока",
                                "slug": "dlia-odnogo-igroka",
                                "language": "rus",
                                "games_count": 32259,
                                "image_background": "https://media.rawg.io/media/games/618/618c2031a07bbff6b4f611f10b6bcdbc.jpg"
                            },
                            {
                                "id": 42417,
                                "name": "Экшен",
                                "slug": "ekshen",
                                "language": "rus",
                                "games_count": 30948,
                                "image_background": "https://media.rawg.io/media/games/d58/d588947d4286e7b5e0e12e1bea7d9844.jpg"
                            },
                            {
                                "id": 42392,
                                "name": "Приключение",
                                "slug": "prikliuchenie",
                                "language": "rus",
                                "games_count": 28893,
                                "image_background": "https://media.rawg.io/media/games/960/960b601d9541cec776c5fa42a00bf6c4.jpg"
                            },
                            {
                                "id": 40847,
                                "name": "Steam Achievements",
                                "slug": "steam-achievements",
                                "language": "eng",
                                "games_count": 29451,
                                "image_background": "https://media.rawg.io/media/games/8cc/8cce7c0e99dcc43d66c8efd42f9d03e3.jpg"
                            },
                            {
                                "id": 7,
                                "name": "Multiplayer",
                                "slug": "multiplayer",
                                "language": "eng",
                                "games_count": 35586,
                                "image_background": "https://media.rawg.io/media/games/328/3283617cb7d75d67257fc58339188742.jpg"
                            },
                            {
                                "id": 40836,
                                "name": "Full controller support",
                                "slug": "full-controller-support",
                                "language": "eng",
                                "games_count": 13833,
                                "image_background": "https://media.rawg.io/media/games/490/49016e06ae2103881ff6373248843069.jpg"
                            },
                            {
                                "id": 13,
                                "name": "Atmospheric",
                                "slug": "atmospheric",
                                "language": "eng",
                                "games_count": 29424,
                                "image_background": "https://media.rawg.io/media/games/7cf/7cfc9220b401b7a300e409e539c9afd5.jpg"
                            },
                            {
                                "id": 40849,
                                "name": "Steam Cloud",
                                "slug": "steam-cloud",
                                "language": "eng",
                                "games_count": 13564,
                                "image_background": "https://media.rawg.io/media/games/49c/49c3dfa4ce2f6f140cc4825868e858cb.jpg"
                            },
                            {
                                "id": 42425,
                                "name": "Для нескольких игроков",
                                "slug": "dlia-neskolkikh-igrokov",
                                "language": "rus",
                                "games_count": 7497,
                                "image_background": "https://media.rawg.io/media/games/d69/d69810315bd7e226ea2d21f9156af629.jpg"
                            },
                            {
                                "id": 42401,
                                "name": "Отличный саундтрек",
                                "slug": "otlichnyi-saundtrek",
                                "language": "rus",
                                "games_count": 4453,
                                "image_background": "https://media.rawg.io/media/games/618/618c2031a07bbff6b4f611f10b6bcdbc.jpg"
                            },
                            {
                                "id": 42,
                                "name": "Great Soundtrack",
                                "slug": "great-soundtrack",
                                "language": "eng",
                                "games_count": 3230,
                                "image_background": "https://media.rawg.io/media/games/7a2/7a2500ee8b2c0e1ff268bb4479463dea.jpg"
                            },
                            {
                                "id": 24,
                                "name": "RPG",
                                "slug": "rpg",
                                "language": "eng",
                                "games_count": 16758,
                                "image_background": "https://media.rawg.io/media/games/6cd/6cd653e0aaef5ff8bbd295bf4bcb12eb.jpg"
                            },
                            {
                                "id": 18,
                                "name": "Co-op",
                                "slug": "co-op",
                                "language": "eng",
                                "games_count": 9686,
                                "image_background": "https://media.rawg.io/media/games/8cc/8cce7c0e99dcc43d66c8efd42f9d03e3.jpg"
                            },
                            {
                                "id": 42412,
                                "name": "Ролевая игра",
                                "slug": "rolevaia-igra",
                                "language": "rus",
                                "games_count": 13092,
                                "image_background": "https://media.rawg.io/media/games/7cf/7cfc9220b401b7a300e409e539c9afd5.jpg"
                            },
                            {
                                "id": 42442,
                                "name": "Открытый мир",
                                "slug": "otkrytyi-mir",
                                "language": "rus",
                                "games_count": 4231,
                                "image_background": "https://media.rawg.io/media/games/490/49016e06ae2103881ff6373248843069.jpg"
                            },
                            {
                                "id": 36,
                                "name": "Open World",
                                "slug": "open-world",
                                "language": "eng",
                                "games_count": 6237,
                                "image_background": "https://media.rawg.io/media/games/ee3/ee3e10193aafc3230ba1cae426967d10.jpg"
                            },
                            {
                                "id": 411,
                                "name": "cooperative",
                                "slug": "cooperative",
                                "language": "eng",
                                "games_count": 3925,
                                "image_background": "https://media.rawg.io/media/games/d0f/d0f91fe1d92332147e5db74e207cfc7a.jpg"
                            },
                            {
                                "id": 42428,
                                "name": "Шутер",
                                "slug": "shuter",
                                "language": "rus",
                                "games_count": 6356,
                                "image_background": "https://media.rawg.io/media/games/fc1/fc1307a2774506b5bd65d7e8424664a7.jpg"
                            },
                            {
                                "id": 8,
                                "name": "First-Person",
                                "slug": "first-person",
                                "language": "eng",
                                "games_count": 29119,
                                "image_background": "https://media.rawg.io/media/games/c4b/c4b0cab189e73432de3a250d8cf1c84e.jpg"
                            },
                            {
                                "id": 42435,
                                "name": "Шедевр",
                                "slug": "shedevr",
                                "language": "rus",
                                "games_count": 1059,
                                "image_background": "https://media.rawg.io/media/games/9dd/9ddabb34840ea9227556670606cf8ea3.jpg"
                            },
                            {
                                "id": 42429,
                                "name": "От первого лица",
                                "slug": "ot-pervogo-litsa",
                                "language": "rus",
                                "games_count": 7085,
                                "image_background": "https://media.rawg.io/media/games/fc1/fc1307a2774506b5bd65d7e8424664a7.jpg"
                            },
                            {
                                "id": 30,
                                "name": "FPS",
                                "slug": "fps",
                                "language": "eng",
                                "games_count": 12564,
                                "image_background": "https://media.rawg.io/media/games/120/1201a40e4364557b124392ee50317b99.jpg"
                            },
                            {
                                "id": 42427,
                                "name": "Шутер от первого лица",
                                "slug": "shuter-ot-pervogo-litsa",
                                "language": "rus",
                                "games_count": 3883,
                                "image_background": "https://media.rawg.io/media/games/c4b/c4b0cab189e73432de3a250d8cf1c84e.jpg"
                            },
                            {
                                "id": 9,
                                "name": "Online Co-Op",
                                "slug": "online-co-op",
                                "language": "eng",
                                "games_count": 4190,
                                "image_background": "https://media.rawg.io/media/games/d0f/d0f91fe1d92332147e5db74e207cfc7a.jpg"
                            },
                            {
                                "id": 42491,
                                "name": "Мясо",
                                "slug": "miaso",
                                "language": "rus",
                                "games_count": 3817,
                                "image_background": "https://media.rawg.io/media/games/7f6/7f6cd70ba2ad57053b4847c13569f2d8.jpg"
                            },
                            {
                                "id": 26,
                                "name": "Gore",
                                "slug": "gore",
                                "language": "eng",
                                "games_count": 5029,
                                "image_background": "https://media.rawg.io/media/games/d58/d588947d4286e7b5e0e12e1bea7d9844.jpg"
                            },
                            {
                                "id": 6,
                                "name": "Exploration",
                                "slug": "exploration",
                                "language": "eng",
                                "games_count": 19251,
                                "image_background": "https://media.rawg.io/media/games/34b/34b1f1850a1c06fd971bc6ab3ac0ce0e.jpg"
                            },
                            {
                                "id": 42433,
                                "name": "Совместная игра по сети",
                                "slug": "sovmestnaia-igra-po-seti",
                                "language": "rus",
                                "games_count": 1229,
                                "image_background": "https://media.rawg.io/media/screenshots/88b/88b5f27f07d6ca2f8a3cd0b36e03a670.jpg"
                            },
                            {
                                "id": 42402,
                                "name": "Насилие",
                                "slug": "nasilie",
                                "language": "rus",
                                "games_count": 4717,
                                "image_background": "https://media.rawg.io/media/games/951/951572a3dd1e42544bd39a5d5b42d234.jpg"
                            },
                            {
                                "id": 34,
                                "name": "Violent",
                                "slug": "violent",
                                "language": "eng",
                                "games_count": 5852,
                                "image_background": "https://media.rawg.io/media/games/5be/5bec14622f6faf804a592176577c1347.jpg"
                            },
                            {
                                "id": 198,
                                "name": "Split Screen",
                                "slug": "split-screen",
                                "language": "eng",
                                "games_count": 5481,
                                "image_background": "https://media.rawg.io/media/games/27b/27b02ffaab6b250cc31bf43baca1fc34.jpg"
                            },
                            {
                                "id": 75,
                                "name": "Local Co-Op",
                                "slug": "local-co-op",
                                "language": "eng",
                                "games_count": 4977,
                                "image_background": "https://media.rawg.io/media/games/226/2262cea0b385db6cf399f4be831603b0.jpg"
                            },
                            {
                                "id": 72,
                                "name": "Local Multiplayer",
                                "slug": "local-multiplayer",
                                "language": "eng",
                                "games_count": 12736,
                                "image_background": "https://media.rawg.io/media/games/bbf/bbf8d74ab64440ad76294cff2f4d9cfa.jpg"
                            },
                            {
                                "id": 69,
                                "name": "Action-Adventure",
                                "slug": "action-adventure",
                                "language": "eng",
                                "games_count": 13563,
                                "image_background": "https://media.rawg.io/media/games/7f6/7f6cd70ba2ad57053b4847c13569f2d8.jpg"
                            },
                            {
                                "id": 42406,
                                "name": "Нагота",
                                "slug": "nagota",
                                "language": "rus",
                                "games_count": 4293,
                                "image_background": "https://media.rawg.io/media/games/473/473bd9a5e9522629d6cb28b701fb836a.jpg"
                            },
                            {
                                "id": 25,
                                "name": "Space",
                                "slug": "space",
                                "language": "eng",
                                "games_count": 43383,
                                "image_background": "https://media.rawg.io/media/games/e1f/e1ffbeb1bac25b19749ad285ca29e158.jpg"
                            },
                            {
                                "id": 468,
                                "name": "role-playing",
                                "slug": "role-playing",
                                "language": "eng",
                                "games_count": 1454,
                                "image_background": "https://media.rawg.io/media/games/596/596a48ef3b62b63b4cc59633e28be903.jpg"
                            },
                            {
                                "id": 40832,
                                "name": "Cross-Platform Multiplayer",
                                "slug": "cross-platform-multiplayer",
                                "language": "eng",
                                "games_count": 2169,
                                "image_background": "https://media.rawg.io/media/games/009/009e4e84975d6a60173ec1199db25aa3.jpg"
                            },
                            {
                                "id": 44,
                                "name": "Nudity",
                                "slug": "nudity",
                                "language": "eng",
                                "games_count": 4681,
                                "image_background": "https://media.rawg.io/media/games/16b/16b1b7b36e2042d1128d5a3e852b3b2f.jpg"
                            },
                            {
                                "id": 40937,
                                "name": "Steam Trading Cards",
                                "slug": "steam-trading-cards-2",
                                "language": "eng",
                                "games_count": 381,
                                "image_background": "https://media.rawg.io/media/games/6cc/6cc23249972a427f697a3d10eb57a820.jpg"
                            },
                            {
                                "id": 413,
                                "name": "online",
                                "slug": "online",
                                "language": "eng",
                                "games_count": 6624,
                                "image_background": "https://media.rawg.io/media/games/9f1/9f1891779cb20f44de93cef33b067e50.jpg"
                            },
                            {
                                "id": 130,
                                "name": "Driving",
                                "slug": "driving",
                                "language": "eng",
                                "games_count": 4710,
                                "image_background": "https://media.rawg.io/media/games/d7d/d7d33daa1892e2468cd0263d5dfc957e.jpg"
                            },
                            {
                                "id": 98,
                                "name": "Loot",
                                "slug": "loot",
                                "language": "eng",
                                "games_count": 1905,
                                "image_background": "https://media.rawg.io/media/games/c6b/c6bfece1daf8d06bc0a60632ac78e5bf.jpg"
                            },
                            {
                                "id": 42575,
                                "name": "Лут",
                                "slug": "lut",
                                "language": "rus",
                                "games_count": 710,
                                "image_background": "https://media.rawg.io/media/games/a7d/a7d57092a650030e2a868117f474efbc.jpg"
                            },
                            {
                                "id": 5816,
                                "name": "console",
                                "slug": "console",
                                "language": "eng",
                                "games_count": 1408,
                                "image_background": "https://media.rawg.io/media/games/415/41563ce6cb061a210160687a4e5d39f6.jpg"
                            },
                            {
                                "id": 744,
                                "name": "friends",
                                "slug": "friends",
                                "language": "eng",
                                "games_count": 15183,
                                "image_background": "https://media.rawg.io/media/games/415/41563ce6cb061a210160687a4e5d39f6.jpg"
                            },
                            {
                                "id": 581,
                                "name": "Epic",
                                "slug": "epic",
                                "language": "eng",
                                "games_count": 4101,
                                "image_background": "https://media.rawg.io/media/screenshots/671/6716b656707c65cdf21882ee51b4f2ff.jpg"
                            },
                            {
                                "id": 578,
                                "name": "Masterpiece",
                                "slug": "masterpiece",
                                "language": "eng",
                                "games_count": 272,
                                "image_background": "https://media.rawg.io/media/screenshots/0de/0defee61ebe38390fd3750b32213796d.jpg"
                            },
                            {
                                "id": 42611,
                                "name": "Эпичная",
                                "slug": "epichnaia",
                                "language": "rus",
                                "games_count": 95,
                                "image_background": "https://media.rawg.io/media/games/879/879c930f9c6787c920153fa2df452eb3.jpg"
                            },
                            {
                                "id": 4565,
                                "name": "offline",
                                "slug": "offline",
                                "language": "eng",
                                "games_count": 1073,
                                "image_background": "https://media.rawg.io/media/games/5dd/5dd4d2dd986d2826800bc37fff64aa4f.jpg"
                            },
                            {
                                "id": 152,
                                "name": "Western",
                                "slug": "western",
                                "language": "eng",
                                "games_count": 1260,
                                "image_background": "https://media.rawg.io/media/games/985/985dc43fe4fd5f0a7ae2a725673d6ac6.jpg"
                            },
                            {
                                "id": 42659,
                                "name": "Совместная кампания",
                                "slug": "sovmestnaia-kampaniia",
                                "language": "rus",
                                "games_count": 278,
                                "image_background": "https://media.rawg.io/media/screenshots/cfa/cfac855f997f4877b64fc908b8bda7b7.jpg"
                            },
                            {
                                "id": 163,
                                "name": "Co-op Campaign",
                                "slug": "co-op-campaign",
                                "language": "eng",
                                "games_count": 259,
                                "image_background": "https://media.rawg.io/media/games/10d/10d19e52e5e8415d16a4d344fe711874.jpg"
                            },
                            {
                                "id": 1484,
                                "name": "skill",
                                "slug": "skill",
                                "language": "eng",
                                "games_count": 3507,
                                "image_background": "https://media.rawg.io/media/games/fc8/fc839beb76bd63c2a5b176c46bdb7681.jpg"
                            },
                            {
                                "id": 500,
                                "name": "Solo",
                                "slug": "solo",
                                "language": "eng",
                                "games_count": 1675,
                                "image_background": "https://media.rawg.io/media/screenshots/643/64372c2b698ff4b0608e3d72a54adf2b.jpg"
                            },
                            {
                                "id": 2405,
                                "name": "planets",
                                "slug": "planets",
                                "language": "eng",
                                "games_count": 1513,
                                "image_background": "https://media.rawg.io/media/screenshots/839/8399a76a597fc93b43ae3103f041ea9e.jpg"
                            },
                            {
                                "id": 1753,
                                "name": "guns",
                                "slug": "guns",
                                "language": "eng",
                                "games_count": 2504,
                                "image_background": "https://media.rawg.io/media/games/662/66261db966238da20c306c4b78ae4603.jpg"
                            },
                            {
                                "id": 38844,
                                "name": "looter shooter",
                                "slug": "looter-shooter",
                                "language": "eng",
                                "games_count": 316,
                                "image_background": "https://media.rawg.io/media/screenshots/4a2/4a295497bbb0d1c5c5ef054bba31d41e.jpg"
                            },
                            {
                                "id": 499,
                                "name": "Team",
                                "slug": "team",
                                "language": "eng",
                                "games_count": 24,
                                "image_background": "https://media.rawg.io/media/screenshots/87f/87f4fe4138cc8f0829acceb37dbe1734.jpg"
                            },
                            {
                                "id": 46115,
                                "name": "LAN Co-op",
                                "slug": "lan-co-op",
                                "language": "eng",
                                "games_count": 361,
                                "image_background": "https://media.rawg.io/media/games/3cb/3cbf69d79420191a2255ffe6a580889e.jpg"
                            },
                            {
                                "id": 6755,
                                "name": "wasteland",
                                "slug": "wasteland",
                                "language": "eng",
                                "games_count": 20,
                                "image_background": "https://media.rawg.io/media/screenshots/22c/22c30aa67d6518120727170c9ac711e4.jpg"
                            },
                            {
                                "id": 2723,
                                "name": "trees",
                                "slug": "trees",
                                "language": "eng",
                                "games_count": 618,
                                "image_background": "https://media.rawg.io/media/screenshots/f40/f401985ddf9f041f039052b1d1c55fa3.jpg"
                            }
                        ],
                        "esrb_rating": {
                            "id": 4,
                            "name": "Mature",
                            "slug": "mature",
                            "name_en": "Mature",
                            "name_ru": "С 17 лет"
                        },
                        "user_game": null,
                        "reviews_count": 776,
                        "saturated_color": "0f0f0f",
                        "dominant_color": "0f0f0f",
                        "short_screenshots": [
                            {
                                "id": -1,
                                "image": "https://media.rawg.io/media/games/9f1/9f1891779cb20f44de93cef33b067e50.jpg"
                            },
                            {
                                "id": 2597139,
                                "image": "https://media.rawg.io/media/screenshots/85f/85fa0742541492cb4b2562311d455918.jpg"
                            },
                            {
                                "id": 2597140,
                                "image": "https://media.rawg.io/media/screenshots/1b6/1b6159bbc9e33c29cfd47cac82322b48.jpg"
                            },
                            {
                                "id": 2597141,
                                "image": "https://media.rawg.io/media/screenshots/825/8255610d24155b27576155b21eda167d.jpg"
                            },
                            {
                                "id": 2597142,
                                "image": "https://media.rawg.io/media/screenshots/9ab/9aba5fc11168844159e3fe83d7327294.jpg"
                            },
                            {
                                "id": 1884080,
                                "image": "https://media.rawg.io/media/screenshots/293/293c4401fd411de976aec0df8597580c.jpg"
                            },
                            {
                                "id": 1884081,
                                "image": "https://media.rawg.io/media/screenshots/c3e/c3ef63402fd812717da342ba73444ca0.jpg"
                            }
                        ],
                        "parent_platforms": [
                            {
                                "platform": {
                                    "id": 1,
                                    "name": "PC",
                                    "slug": "pc"
                                }
                            },
                            {
                                "platform": {
                                    "id": 2,
                                    "name": "PlayStation",
                                    "slug": "playstation"
                                }
                            },
                            {
                                "platform": {
                                    "id": 3,
                                    "name": "Xbox",
                                    "slug": "xbox"
                                }
                            }
                        ],
                        "genres": [
                            {
                                "id": 2,
                                "name": "Shooter",
                                "slug": "shooter"
                            },
                            {
                                "id": 3,
                                "name": "Adventure",
                                "slug": "adventure"
                            },
                            {
                                "id": 4,
                                "name": "Action",
                                "slug": "action"
                            },
                            {
                                "id": 5,
                                "name": "RPG",
                                "slug": "role-playing-games-rpg"
                            }
                        ]
                    },
                    {
                        "slug": "gears-5",
                        "name": "Gears 5",
                        "playtime": 6,
                        "platforms": [
                            {
                                "platform": {
                                    "id": 4,
                                    "name": "PC",
                                    "slug": "pc"
                                }
                            },
                            {
                                "platform": {
                                    "id": 1,
                                    "name": "Xbox One",
                                    "slug": "xbox-one"
                                }
                            },
                            {
                                "platform": {
                                    "id": 186,
                                    "name": "Xbox Series S/X",
                                    "slug": "xbox-series-x"
                                }
                            }
                        ],
                        "stores": [
                            {
                                "store": {
                                    "id": 1,
                                    "name": "Steam",
                                    "slug": "steam"
                                }
                            },
                            {
                                "store": {
                                    "id": 2,
                                    "name": "Xbox Store",
                                    "slug": "xbox-store"
                                }
                            }
                        ],
                        "released": "2019-09-10",
                        "tba": false,
                        "background_image": "https://media.rawg.io/media/games/121/1213f8b9b0a26307e672cf51f34882f8.jpg",
                        "rating": 3.93,
                        "rating_top": 4,
                        "ratings": [
                            {
                                "id": 4,
                                "title": "recommended",
                                "count": 458,
                                "percent": 58.05
                            },
                            {
                                "id": 5,
                                "title": "exceptional",
                                "count": 168,
                                "percent": 21.29
                            },
                            {
                                "id": 3,
                                "title": "meh",
                                "count": 134,
                                "percent": 16.98
                            },
                            {
                                "id": 1,
                                "title": "skip",
                                "count": 29,
                                "percent": 3.68
                            }
                        ],
                        "ratings_count": 773,
                        "reviews_text_count": 10,
                        "added": 4102,
                        "added_by_status": {
                            "yet": 255,
                            "owned": 2700,
                            "beaten": 510,
                            "toplay": 370,
                            "dropped": 160,
                            "playing": 107
                        },
                        "metacritic": 83,
                        "suggestions_count": 646,
                        "updated": "2023-03-19T14:08:47",
                        "id": 326252,
                        "score": null,
                        "clip": null,
                        "tags": [
                            {
                                "id": 31,
                                "name": "Singleplayer",
                                "slug": "singleplayer",
                                "language": "eng",
                                "games_count": 209582,
                                "image_background": "https://media.rawg.io/media/games/73e/73eecb8909e0c39fb246f457b5d6cbbe.jpg"
                            },
                            {
                                "id": 42396,
                                "name": "Для одного игрока",
                                "slug": "dlia-odnogo-igroka",
                                "language": "rus",
                                "games_count": 32259,
                                "image_background": "https://media.rawg.io/media/games/618/618c2031a07bbff6b4f611f10b6bcdbc.jpg"
                            },
                            {
                                "id": 42417,
                                "name": "Экшен",
                                "slug": "ekshen",
                                "language": "rus",
                                "games_count": 30948,
                                "image_background": "https://media.rawg.io/media/games/d58/d588947d4286e7b5e0e12e1bea7d9844.jpg"
                            },
                            {
                                "id": 7,
                                "name": "Multiplayer",
                                "slug": "multiplayer",
                                "language": "eng",
                                "games_count": 35586,
                                "image_background": "https://media.rawg.io/media/games/328/3283617cb7d75d67257fc58339188742.jpg"
                            },
                            {
                                "id": 42425,
                                "name": "Для нескольких игроков",
                                "slug": "dlia-neskolkikh-igrokov",
                                "language": "rus",
                                "games_count": 7497,
                                "image_background": "https://media.rawg.io/media/games/d69/d69810315bd7e226ea2d21f9156af629.jpg"
                            },
                            {
                                "id": 42394,
                                "name": "Глубокий сюжет",
                                "slug": "glubokii-siuzhet",
                                "language": "rus",
                                "games_count": 8424,
                                "image_background": "https://media.rawg.io/media/games/4cf/4cfc6b7f1850590a4634b08bfab308ab.jpg"
                            },
                            {
                                "id": 18,
                                "name": "Co-op",
                                "slug": "co-op",
                                "language": "eng",
                                "games_count": 9686,
                                "image_background": "https://media.rawg.io/media/games/8cc/8cce7c0e99dcc43d66c8efd42f9d03e3.jpg"
                            },
                            {
                                "id": 411,
                                "name": "cooperative",
                                "slug": "cooperative",
                                "language": "eng",
                                "games_count": 3925,
                                "image_background": "https://media.rawg.io/media/games/d0f/d0f91fe1d92332147e5db74e207cfc7a.jpg"
                            },
                            {
                                "id": 40845,
                                "name": "Partial Controller Support",
                                "slug": "partial-controller-support",
                                "language": "eng",
                                "games_count": 9487,
                                "image_background": "https://media.rawg.io/media/games/7fa/7fa0b586293c5861ee32490e953a4996.jpg"
                            },
                            {
                                "id": 9,
                                "name": "Online Co-Op",
                                "slug": "online-co-op",
                                "language": "eng",
                                "games_count": 4190,
                                "image_background": "https://media.rawg.io/media/games/d0f/d0f91fe1d92332147e5db74e207cfc7a.jpg"
                            },
                            {
                                "id": 42491,
                                "name": "Мясо",
                                "slug": "miaso",
                                "language": "rus",
                                "games_count": 3817,
                                "image_background": "https://media.rawg.io/media/games/7f6/7f6cd70ba2ad57053b4847c13569f2d8.jpg"
                            },
                            {
                                "id": 26,
                                "name": "Gore",
                                "slug": "gore",
                                "language": "eng",
                                "games_count": 5029,
                                "image_background": "https://media.rawg.io/media/games/d58/d588947d4286e7b5e0e12e1bea7d9844.jpg"
                            },
                            {
                                "id": 189,
                                "name": "Female Protagonist",
                                "slug": "female-protagonist",
                                "language": "eng",
                                "games_count": 10429,
                                "image_background": "https://media.rawg.io/media/games/021/021c4e21a1824d2526f925eff6324653.jpg"
                            },
                            {
                                "id": 42404,
                                "name": "Женщина-протагонист",
                                "slug": "zhenshchina-protagonist",
                                "language": "rus",
                                "games_count": 2416,
                                "image_background": "https://media.rawg.io/media/games/62c/62c7c8b28a27b83680b22fb9d33fc619.jpg"
                            },
                            {
                                "id": 42402,
                                "name": "Насилие",
                                "slug": "nasilie",
                                "language": "rus",
                                "games_count": 4717,
                                "image_background": "https://media.rawg.io/media/games/951/951572a3dd1e42544bd39a5d5b42d234.jpg"
                            },
                            {
                                "id": 34,
                                "name": "Violent",
                                "slug": "violent",
                                "language": "eng",
                                "games_count": 5852,
                                "image_background": "https://media.rawg.io/media/games/5be/5bec14622f6faf804a592176577c1347.jpg"
                            },
                            {
                                "id": 397,
                                "name": "Online multiplayer",
                                "slug": "online-multiplayer",
                                "language": "eng",
                                "games_count": 3811,
                                "image_background": "https://media.rawg.io/media/games/fc0/fc076b974197660a582abd34ebccc27f.jpg"
                            },
                            {
                                "id": 198,
                                "name": "Split Screen",
                                "slug": "split-screen",
                                "language": "eng",
                                "games_count": 5481,
                                "image_background": "https://media.rawg.io/media/games/27b/27b02ffaab6b250cc31bf43baca1fc34.jpg"
                            },
                            {
                                "id": 75,
                                "name": "Local Co-Op",
                                "slug": "local-co-op",
                                "language": "eng",
                                "games_count": 4977,
                                "image_background": "https://media.rawg.io/media/games/226/2262cea0b385db6cf399f4be831603b0.jpg"
                            },
                            {
                                "id": 42446,
                                "name": "Шутер от третьего лица",
                                "slug": "shuter-ot-tretego-litsa",
                                "language": "rus",
                                "games_count": 1410,
                                "image_background": "https://media.rawg.io/media/games/e2d/e2d3f396b16dded0f841c17c9799a882.jpg"
                            },
                            {
                                "id": 72,
                                "name": "Local Multiplayer",
                                "slug": "local-multiplayer",
                                "language": "eng",
                                "games_count": 12736,
                                "image_background": "https://media.rawg.io/media/games/bbf/bbf8d74ab64440ad76294cff2f4d9cfa.jpg"
                            },
                            {
                                "id": 40832,
                                "name": "Cross-Platform Multiplayer",
                                "slug": "cross-platform-multiplayer",
                                "language": "eng",
                                "games_count": 2169,
                                "image_background": "https://media.rawg.io/media/games/009/009e4e84975d6a60173ec1199db25aa3.jpg"
                            },
                            {
                                "id": 81,
                                "name": "Military",
                                "slug": "military",
                                "language": "eng",
                                "games_count": 1305,
                                "image_background": "https://media.rawg.io/media/games/ac7/ac7b8327343da12c971cfc418f390a11.jpg"
                            },
                            {
                                "id": 3068,
                                "name": "future",
                                "slug": "future",
                                "language": "eng",
                                "games_count": 3176,
                                "image_background": "https://media.rawg.io/media/screenshots/f85/f856c14f8c8d7cb72085723960f5c8d5.jpg"
                            }
                        ],
                        "esrb_rating": null,
                        "user_game": null,
                        "reviews_count": 789,
                        "saturated_color": "0f0f0f",
                        "dominant_color": "0f0f0f",
                        "short_screenshots": [
                            {
                                "id": -1,
                                "image": "https://media.rawg.io/media/games/121/1213f8b9b0a26307e672cf51f34882f8.jpg"
                            },
                            {
                                "id": 1957721,
                                "image": "https://media.rawg.io/media/screenshots/9f0/9f085738a4ee6bb44b4b26cd3eb9ef93.jpg"
                            },
                            {
                                "id": 1957722,
                                "image": "https://media.rawg.io/media/screenshots/1c8/1c8eb3c87b9396e924ade589d543790e.jpg"
                            },
                            {
                                "id": 1957723,
                                "image": "https://media.rawg.io/media/screenshots/a83/a832752f82cfde4a811c581e9cd3efd0.jpg"
                            },
                            {
                                "id": 1957724,
                                "image": "https://media.rawg.io/media/screenshots/a6f/a6fafe1183ce4b3f68287219ea3dd6c8.jpg"
                            },
                            {
                                "id": 1957725,
                                "image": "https://media.rawg.io/media/screenshots/38c/38c4c8601a72e1d0dacf6d1fe1c5dfb3.jpg"
                            },
                            {
                                "id": 778890,
                                "image": "https://media.rawg.io/media/screenshots/ae7/ae7fcd1a2d11be26e1ea72e6d5d83ecc.jpg"
                            }
                        ],
                        "parent_platforms": [
                            {
                                "platform": {
                                    "id": 1,
                                    "name": "PC",
                                    "slug": "pc"
                                }
                            },
                            {
                                "platform": {
                                    "id": 3,
                                    "name": "Xbox",
                                    "slug": "xbox"
                                }
                            }
                        ],
                        "genres": [
                            {
                                "id": 2,
                                "name": "Shooter",
                                "slug": "shooter"
                            },
                            {
                                "id": 4,
                                "name": "Action",
                                "slug": "action"
                            }
                        ]
                    },
                    {
                        "slug": "blasphemous",
                        "name": "Blasphemous",
                        "playtime": 3,
                        "platforms": [
                            {
                                "platform": {
                                    "id": 4,
                                    "name": "PC",
                                    "slug": "pc"
                                }
                            },
                            {
                                "platform": {
                                    "id": 1,
                                    "name": "Xbox One",
                                    "slug": "xbox-one"
                                }
                            },
                            {
                                "platform": {
                                    "id": 18,
                                    "name": "PlayStation 4",
                                    "slug": "playstation4"
                                }
                            },
                            {
                                "platform": {
                                    "id": 7,
                                    "name": "Nintendo Switch",
                                    "slug": "nintendo-switch"
                                }
                            },
                            {
                                "platform": {
                                    "id": 5,
                                    "name": "macOS",
                                    "slug": "macos"
                                }
                            },
                            {
                                "platform": {
                                    "id": 6,
                                    "name": "Linux",
                                    "slug": "linux"
                                }
                            }
                        ],
                        "stores": [
                            {
                                "store": {
                                    "id": 1,
                                    "name": "Steam",
                                    "slug": "steam"
                                }
                            },
                            {
                                "store": {
                                    "id": 3,
                                    "name": "PlayStation Store",
                                    "slug": "playstation-store"
                                }
                            },
                            {
                                "store": {
                                    "id": 2,
                                    "name": "Xbox Store",
                                    "slug": "xbox-store"
                                }
                            },
                            {
                                "store": {
                                    "id": 5,
                                    "name": "GOG",
                                    "slug": "gog"
                                }
                            },
                            {
                                "store": {
                                    "id": 6,
                                    "name": "Nintendo Store",
                                    "slug": "nintendo"
                                }
                            },
                            {
                                "store": {
                                    "id": 11,
                                    "name": "Epic Games",
                                    "slug": "epic-games"
                                }
                            }
                        ],
                        "released": "2019-09-09",
                        "tba": false,
                        "background_image": "https://media.rawg.io/media/games/b01/b01aa6b2d6d4f683203e9471a8b8d5b5.jpg",
                        "rating": 4.04,
                        "rating_top": 4,
                        "ratings": [
                            {
                                "id": 4,
                                "title": "recommended",
                                "count": 223,
                                "percent": 49.12
                            },
                            {
                                "id": 5,
                                "title": "exceptional",
                                "count": 148,
                                "percent": 32.6
                            },
                            {
                                "id": 3,
                                "title": "meh",
                                "count": 59,
                                "percent": 13.0
                            },
                            {
                                "id": 1,
                                "title": "skip",
                                "count": 24,
                                "percent": 5.29
                            }
                        ],
                        "ratings_count": 446,
                        "reviews_text_count": 4,
                        "added": 3255,
                        "added_by_status": {
                            "yet": 261,
                            "owned": 2199,
                            "beaten": 280,
                            "toplay": 259,
                            "dropped": 162,
                            "playing": 94
                        },
                        "metacritic": 78,
                        "suggestions_count": 393,
                        "updated": "2023-03-19T14:10:41",
                        "id": 258322,
                        "score": null,
                        "clip": null,
                        "tags": [
                            {
                                "id": 31,
                                "name": "Singleplayer",
                                "slug": "singleplayer",
                                "language": "eng",
                                "games_count": 209582,
                                "image_background": "https://media.rawg.io/media/games/73e/73eecb8909e0c39fb246f457b5d6cbbe.jpg"
                            },
                            {
                                "id": 42396,
                                "name": "Для одного игрока",
                                "slug": "dlia-odnogo-igroka",
                                "language": "rus",
                                "games_count": 32259,
                                "image_background": "https://media.rawg.io/media/games/618/618c2031a07bbff6b4f611f10b6bcdbc.jpg"
                            },
                            {
                                "id": 42417,
                                "name": "Экшен",
                                "slug": "ekshen",
                                "language": "rus",
                                "games_count": 30948,
                                "image_background": "https://media.rawg.io/media/games/d58/d588947d4286e7b5e0e12e1bea7d9844.jpg"
                            },
                            {
                                "id": 42392,
                                "name": "Приключение",
                                "slug": "prikliuchenie",
                                "language": "rus",
                                "games_count": 28893,
                                "image_background": "https://media.rawg.io/media/games/960/960b601d9541cec776c5fa42a00bf6c4.jpg"
                            },
                            {
                                "id": 40847,
                                "name": "Steam Achievements",
                                "slug": "steam-achievements",
                                "language": "eng",
                                "games_count": 29451,
                                "image_background": "https://media.rawg.io/media/games/8cc/8cce7c0e99dcc43d66c8efd42f9d03e3.jpg"
                            },
                            {
                                "id": 42398,
                                "name": "Инди",
                                "slug": "indi-2",
                                "language": "rus",
                                "games_count": 44678,
                                "image_background": "https://media.rawg.io/media/games/226/2262cea0b385db6cf399f4be831603b0.jpg"
                            },
                            {
                                "id": 40836,
                                "name": "Full controller support",
                                "slug": "full-controller-support",
                                "language": "eng",
                                "games_count": 13833,
                                "image_background": "https://media.rawg.io/media/games/490/49016e06ae2103881ff6373248843069.jpg"
                            },
                            {
                                "id": 42401,
                                "name": "Отличный саундтрек",
                                "slug": "otlichnyi-saundtrek",
                                "language": "rus",
                                "games_count": 4453,
                                "image_background": "https://media.rawg.io/media/games/618/618c2031a07bbff6b4f611f10b6bcdbc.jpg"
                            },
                            {
                                "id": 45,
                                "name": "2D",
                                "slug": "2d",
                                "language": "eng",
                                "games_count": 197659,
                                "image_background": "https://media.rawg.io/media/screenshots/c97/c97b943741f5fbc936fe054d9d58851d.jpg"
                            },
                            {
                                "id": 42420,
                                "name": "Сложная",
                                "slug": "slozhnaia",
                                "language": "rus",
                                "games_count": 4353,
                                "image_background": "https://media.rawg.io/media/games/9bf/9bfac18ff678f41a4674250fa0e04a52.jpg"
                            },
                            {
                                "id": 42491,
                                "name": "Мясо",
                                "slug": "miaso",
                                "language": "rus",
                                "games_count": 3817,
                                "image_background": "https://media.rawg.io/media/games/7f6/7f6cd70ba2ad57053b4847c13569f2d8.jpg"
                            },
                            {
                                "id": 26,
                                "name": "Gore",
                                "slug": "gore",
                                "language": "eng",
                                "games_count": 5029,
                                "image_background": "https://media.rawg.io/media/games/d58/d588947d4286e7b5e0e12e1bea7d9844.jpg"
                            },
                            {
                                "id": 49,
                                "name": "Difficult",
                                "slug": "difficult",
                                "language": "eng",
                                "games_count": 12897,
                                "image_background": "https://media.rawg.io/media/games/6cd/6cd653e0aaef5ff8bbd295bf4bcb12eb.jpg"
                            },
                            {
                                "id": 6,
                                "name": "Exploration",
                                "slug": "exploration",
                                "language": "eng",
                                "games_count": 19251,
                                "image_background": "https://media.rawg.io/media/games/34b/34b1f1850a1c06fd971bc6ab3ac0ce0e.jpg"
                            },
                            {
                                "id": 42463,
                                "name": "Платформер",
                                "slug": "platformer-2",
                                "language": "rus",
                                "games_count": 6201,
                                "image_background": "https://media.rawg.io/media/games/fc8/fc838d98c9b944e6a15176eabf40bee8.jpg"
                            },
                            {
                                "id": 42402,
                                "name": "Насилие",
                                "slug": "nasilie",
                                "language": "rus",
                                "games_count": 4717,
                                "image_background": "https://media.rawg.io/media/games/951/951572a3dd1e42544bd39a5d5b42d234.jpg"
                            },
                            {
                                "id": 34,
                                "name": "Violent",
                                "slug": "violent",
                                "language": "eng",
                                "games_count": 5852,
                                "image_background": "https://media.rawg.io/media/games/5be/5bec14622f6faf804a592176577c1347.jpg"
                            },
                            {
                                "id": 42464,
                                "name": "Исследование",
                                "slug": "issledovanie",
                                "language": "rus",
                                "games_count": 2990,
                                "image_background": "https://media.rawg.io/media/games/bce/bce62fbc7cf74bf6a1a37340993ec148.jpg"
                            },
                            {
                                "id": 42415,
                                "name": "Пиксельная графика",
                                "slug": "pikselnaia-grafika",
                                "language": "rus",
                                "games_count": 8404,
                                "image_background": "https://media.rawg.io/media/screenshots/f81/f81fd968a3161e7d35612d8c4232923e.jpg"
                            },
                            {
                                "id": 122,
                                "name": "Pixel Graphics",
                                "slug": "pixel-graphics",
                                "language": "eng",
                                "games_count": 93980,
                                "image_background": "https://media.rawg.io/media/games/501/501e7019925a3c692bf1c8062f07abe6.jpg"
                            },
                            {
                                "id": 42406,
                                "name": "Нагота",
                                "slug": "nagota",
                                "language": "rus",
                                "games_count": 4293,
                                "image_background": "https://media.rawg.io/media/games/473/473bd9a5e9522629d6cb28b701fb836a.jpg"
                            },
                            {
                                "id": 468,
                                "name": "role-playing",
                                "slug": "role-playing",
                                "language": "eng",
                                "games_count": 1454,
                                "image_background": "https://media.rawg.io/media/games/596/596a48ef3b62b63b4cc59633e28be903.jpg"
                            },
                            {
                                "id": 44,
                                "name": "Nudity",
                                "slug": "nudity",
                                "language": "eng",
                                "games_count": 4681,
                                "image_background": "https://media.rawg.io/media/games/16b/16b1b7b36e2042d1128d5a3e852b3b2f.jpg"
                            },
                            {
                                "id": 42469,
                                "name": "Вид сбоку",
                                "slug": "vid-sboku",
                                "language": "rus",
                                "games_count": 2850,
                                "image_background": "https://media.rawg.io/media/games/9cc/9cc11e2e81403186c7fa9c00c143d6e4.jpg"
                            },
                            {
                                "id": 42506,
                                "name": "Тёмное фэнтези",
                                "slug": "tiomnoe-fentezi",
                                "language": "rus",
                                "games_count": 1786,
                                "image_background": "https://media.rawg.io/media/games/789/7896837ec22a83e4007018ddd55e8c9a.jpg"
                            },
                            {
                                "id": 40,
                                "name": "Dark Fantasy",
                                "slug": "dark-fantasy",
                                "language": "eng",
                                "games_count": 3190,
                                "image_background": "https://media.rawg.io/media/games/da1/da1b267764d77221f07a4386b6548e5a.jpg"
                            },
                            {
                                "id": 259,
                                "name": "Metroidvania",
                                "slug": "metroidvania",
                                "language": "eng",
                                "games_count": 4024,
                                "image_background": "https://media.rawg.io/media/games/c40/c40f9f0a3d1b4601a7a44d230c95f126.jpg"
                            },
                            {
                                "id": 42462,
                                "name": "Метроидвания",
                                "slug": "metroidvaniia",
                                "language": "rus",
                                "games_count": 887,
                                "image_background": "https://media.rawg.io/media/games/c50/c5085506fe4b5e20fc7aa5ace842c20b.jpg"
                            },
                            {
                                "id": 42592,
                                "name": "Похожа на Dark Souls",
                                "slug": "pokhozha-na-dark-souls",
                                "language": "rus",
                                "games_count": 585,
                                "image_background": "https://media.rawg.io/media/games/d09/d096ad37b7f522e11c02848252213a9a.jpg"
                            },
                            {
                                "id": 205,
                                "name": "Lore-Rich",
                                "slug": "lore-rich",
                                "language": "eng",
                                "games_count": 718,
                                "image_background": "https://media.rawg.io/media/screenshots/c98/c988bb637b38eac52b3ea878781e73d0.jpg"
                            },
                            {
                                "id": 42594,
                                "name": "Проработанная вселенная",
                                "slug": "prorabotannaia-vselennaia",
                                "language": "rus",
                                "games_count": 745,
                                "image_background": "https://media.rawg.io/media/screenshots/525/525b5da62342fa726bfe2820e8f93c09.jpg"
                            },
                            {
                                "id": 42677,
                                "name": "Готика",
                                "slug": "gotika",
                                "language": "rus",
                                "games_count": 339,
                                "image_background": "https://media.rawg.io/media/games/af7/af7a831001c5c32c46e950cc883b8cb7.jpg"
                            },
                            {
                                "id": 580,
                                "name": "Souls-like",
                                "slug": "souls-like",
                                "language": "eng",
                                "games_count": 988,
                                "image_background": "https://media.rawg.io/media/games/3b0/3b0f57d0fbb23854f300fb203c18889b.jpg"
                            }
                        ],
                        "esrb_rating": {
                            "id": 4,
                            "name": "Mature",
                            "slug": "mature",
                            "name_en": "Mature",
                            "name_ru": "С 17 лет"
                        },
                        "user_game": null,
                        "reviews_count": 454,
                        "saturated_color": "0f0f0f",
                        "dominant_color": "0f0f0f",
                        "short_screenshots": [
                            {
                                "id": -1,
                                "image": "https://media.rawg.io/media/games/b01/b01aa6b2d6d4f683203e9471a8b8d5b5.jpg"
                            },
                            {
                                "id": 1702324,
                                "image": "https://media.rawg.io/media/screenshots/350/35004ab01b59310d9682c069efe0c0b2.jpg"
                            },
                            {
                                "id": 1702325,
                                "image": "https://media.rawg.io/media/screenshots/993/9930282406e7dd2819451ec16373a688.jpg"
                            },
                            {
                                "id": 1702329,
                                "image": "https://media.rawg.io/media/screenshots/b70/b70109ffdfabfe36e36cc43e1ad80277.jpg"
                            },
                            {
                                "id": 1702331,
                                "image": "https://media.rawg.io/media/screenshots/29d/29d417920697a7c637612a9ea7cd7d74.jpg"
                            },
                            {
                                "id": 1702332,
                                "image": "https://media.rawg.io/media/screenshots/a14/a14cacc86c817d7f039ac1f9ac2819e1.jpg"
                            },
                            {
                                "id": 1702333,
                                "image": "https://media.rawg.io/media/screenshots/1f7/1f7da2126ecea73a7a44623f768f7b94.jpg"
                            }
                        ],
                        "parent_platforms": [
                            {
                                "platform": {
                                    "id": 1,
                                    "name": "PC",
                                    "slug": "pc"
                                }
                            },
                            {
                                "platform": {
                                    "id": 2,
                                    "name": "PlayStation",
                                    "slug": "playstation"
                                }
                            },
                            {
                                "platform": {
                                    "id": 3,
                                    "name": "Xbox",
                                    "slug": "xbox"
                                }
                            },
                            {
                                "platform": {
                                    "id": 5,
                                    "name": "Apple Macintosh",
                                    "slug": "mac"
                                }
                            },
                            {
                                "platform": {
                                    "id": 6,
                                    "name": "Linux",
                                    "slug": "linux"
                                }
                            },
                            {
                                "platform": {
                                    "id": 7,
                                    "name": "Nintendo",
                                    "slug": "nintendo"
                                }
                            }
                        ],
                        "genres": [
                            {
                                "id": 51,
                                "name": "Indie",
                                "slug": "indie"
                            },
                            {
                                "id": 83,
                                "name": "Platformer",
                                "slug": "platformer"
                            },
                            {
                                "id": 3,
                                "name": "Adventure",
                                "slug": "adventure"
                            },
                            {
                                "id": 4,
                                "name": "Action",
                                "slug": "action"
                            }
                        ]
                    }
                ],
                "user_platforms": false
            }
        """.trimIndent()
    )

val deserializedMockResponseB = RAWGPage(
    count = 149,
    nextPageUrl = "https://api.rawg.io/api/games?dates=2019-09-01%2C2019-09-30&key=b10a96c96f124283b10cf6f1aa18f973&page=2&page_size=3&platforms=18%2C1%2C7",
    previousPageUrl = null,
    results = listOf(
        RAWGShallowGame(
            id = 58617,
            slug = "borderlands-3",
            name = "Borderlands 3",
            releaseDate = "2019-09-13",
            toBeAnnounced = false,
            imageUrl = "https://media.rawg.io/media/games/9f1/9f1891779cb20f44de93cef33b067e50.jpg",
            rating = 3.9f,
            metaCriticScore = 83,
            playtime = 10,
            suggestionsCount = 917,
            platforms = listOf(
                ShallowRAWGPlatformInfo(
                    platform = RAWGPlatformProperties(
                        id = 4,
                        name = "PC",
                        slug = "pc",
                        imageUrl = null,
                        yearEnd = null,
                        yearStart = null,
                        gamesCount = null,
                        imageBackground = null
                    )
                ),
                ShallowRAWGPlatformInfo(
                    platform = RAWGPlatformProperties(
                        id = 187,
                        name = "PlayStation 5",
                        slug = "playstation5",
                        imageUrl = null,
                        yearEnd = null,
                        yearStart = null,
                        gamesCount = null,
                        imageBackground = null
                    )
                ),
                ShallowRAWGPlatformInfo(
                    platform = RAWGPlatformProperties(
                        id = 1,
                        name = "Xbox One",
                        slug = "xbox-one",
                        imageUrl = null,
                        yearEnd = null,
                        yearStart = null,
                        gamesCount = null,
                        imageBackground = null
                    )
                ),
                ShallowRAWGPlatformInfo(
                    platform = RAWGPlatformProperties(
                        id = 18,
                        name = "PlayStation 4",
                        slug = "playstation4",
                        imageUrl = null,
                        yearEnd = null,
                        yearStart = null,
                        gamesCount = null,
                        imageBackground = null
                    )
                ),
                ShallowRAWGPlatformInfo(
                    platform = RAWGPlatformProperties(
                        id = 186,
                        name = "Xbox Series S/X",
                        slug = "xbox-series-x",
                        imageUrl = null,
                        yearEnd = null,
                        yearStart = null,
                        gamesCount = null,
                        imageBackground = null
                    )
                )
            ),
            stores = listOf(
                ShallowRAWGStoreInfo(
                    store = RAWGStoreProperties(
                        id = 1,
                        name = "Steam",
                        slug = "steam",
                        domain = null,
                        gamesCount = null,
                        imageUrl = null
                    )
                ),
                ShallowRAWGStoreInfo(
                    store = RAWGStoreProperties(
                        id = 3,
                        name = "PlayStation Store",
                        slug = "playstation-store",
                        domain = null,
                        gamesCount = null,
                        imageUrl = null
                    )
                ),
                ShallowRAWGStoreInfo(
                    store = RAWGStoreProperties(
                        id = 2,
                        name = "Xbox Store",
                        slug = "xbox-store",
                        domain = null,
                        gamesCount = null,
                        imageUrl = null
                    )
                ),
                ShallowRAWGStoreInfo(
                    store = RAWGStoreProperties(
                        id = 11,
                        name = "Epic Games",
                        slug = "epic-games",
                        domain = null,
                        gamesCount = null,
                        imageUrl = null
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
                    id = 42396,
                    name = "Для одного игрока",
                    slug = "dlia-odnogo-igroka",
                    language = "rus",
                    gamesCount = 32259,
                    imageUrl = "https://media.rawg.io/media/games/618/618c2031a07bbff6b4f611f10b6bcdbc.jpg"
                ),
                RAWGGameTag(
                    id = 42417,
                    name = "Экшен",
                    slug = "ekshen",
                    language = "rus",
                    gamesCount = 30948,
                    imageUrl = "https://media.rawg.io/media/games/d58/d588947d4286e7b5e0e12e1bea7d9844.jpg"
                ),
                RAWGGameTag(
                    id = 42392,
                    name = "Приключение",
                    slug = "prikliuchenie",
                    language = "rus",
                    gamesCount = 28893,
                    imageUrl = "https://media.rawg.io/media/games/960/960b601d9541cec776c5fa42a00bf6c4.jpg"
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
                    id = 42425,
                    name = "Для нескольких игроков",
                    slug = "dlia-neskolkikh-igrokov",
                    language = "rus",
                    gamesCount = 7497,
                    imageUrl = "https://media.rawg.io/media/games/d69/d69810315bd7e226ea2d21f9156af629.jpg"
                ),
                RAWGGameTag(
                    id = 42401,
                    name = "Отличный саундтрек",
                    slug = "otlichnyi-saundtrek",
                    language = "rus",
                    gamesCount = 4453,
                    imageUrl = "https://media.rawg.io/media/games/618/618c2031a07bbff6b4f611f10b6bcdbc.jpg"
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
                    id = 42412,
                    name = "Ролевая игра",
                    slug = "rolevaia-igra",
                    language = "rus",
                    gamesCount = 13092,
                    imageUrl = "https://media.rawg.io/media/games/7cf/7cfc9220b401b7a300e409e539c9afd5.jpg"
                ),
                RAWGGameTag(
                    id = 42442,
                    name = "Открытый мир",
                    slug = "otkrytyi-mir",
                    language = "rus",
                    gamesCount = 4231,
                    imageUrl = "https://media.rawg.io/media/games/490/49016e06ae2103881ff6373248843069.jpg"
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
                    id = 42428,
                    name = "Шутер",
                    slug = "shuter",
                    language = "rus",
                    gamesCount = 6356,
                    imageUrl = "https://media.rawg.io/media/games/fc1/fc1307a2774506b5bd65d7e8424664a7.jpg"
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
                    id = 42435,
                    name = "Шедевр",
                    slug = "shedevr",
                    language = "rus",
                    gamesCount = 1059,
                    imageUrl = "https://media.rawg.io/media/games/9dd/9ddabb34840ea9227556670606cf8ea3.jpg"
                ),
                RAWGGameTag(
                    id = 42429,
                    name = "От первого лица",
                    slug = "ot-pervogo-litsa",
                    language = "rus",
                    gamesCount = 7085,
                    imageUrl = "https://media.rawg.io/media/games/fc1/fc1307a2774506b5bd65d7e8424664a7.jpg"
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
                    id = 42427,
                    name = "Шутер от первого лица",
                    slug = "shuter-ot-pervogo-litsa",
                    language = "rus",
                    gamesCount = 3883,
                    imageUrl = "https://media.rawg.io/media/games/c4b/c4b0cab189e73432de3a250d8cf1c84e.jpg"
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
                    id = 42491,
                    name = "Мясо",
                    slug = "miaso",
                    language = "rus",
                    gamesCount = 3817,
                    imageUrl = "https://media.rawg.io/media/games/7f6/7f6cd70ba2ad57053b4847c13569f2d8.jpg"
                ),
                RAWGGameTag(
                    id = 26,
                    name = "Gore",
                    slug = "gore",
                    language = "eng",
                    gamesCount = 5029,
                    imageUrl = "https://media.rawg.io/media/games/d58/d588947d4286e7b5e0e12e1bea7d9844.jpg"
                ),
                RAWGGameTag(
                    id = 6,
                    name = "Exploration",
                    slug = "exploration",
                    language = "eng",
                    gamesCount = 19251,
                    imageUrl = "https://media.rawg.io/media/games/34b/34b1f1850a1c06fd971bc6ab3ac0ce0e.jpg"
                ),
                RAWGGameTag(
                    id = 42433,
                    name = "Совместная игра по сети",
                    slug = "sovmestnaia-igra-po-seti",
                    language = "rus",
                    gamesCount = 1229,
                    imageUrl = "https://media.rawg.io/media/screenshots/88b/88b5f27f07d6ca2f8a3cd0b36e03a670.jpg"
                ),
                RAWGGameTag(
                    id = 42402,
                    name = "Насилие",
                    slug = "nasilie",
                    language = "rus",
                    gamesCount = 4717,
                    imageUrl = "https://media.rawg.io/media/games/951/951572a3dd1e42544bd39a5d5b42d234.jpg"
                ),
                RAWGGameTag(
                    id = 34,
                    name = "Violent",
                    slug = "violent",
                    language = "eng",
                    gamesCount = 5852,
                    imageUrl = "https://media.rawg.io/media/games/5be/5bec14622f6faf804a592176577c1347.jpg"
                ),
                RAWGGameTag(
                    id = 198,
                    name = "Split Screen",
                    slug = "split-screen",
                    language = "eng",
                    gamesCount = 5481,
                    imageUrl = "https://media.rawg.io/media/games/27b/27b02ffaab6b250cc31bf43baca1fc34.jpg"
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
                    id = 72,
                    name = "Local Multiplayer",
                    slug = "local-multiplayer",
                    language = "eng",
                    gamesCount = 12736,
                    imageUrl = "https://media.rawg.io/media/games/bbf/bbf8d74ab64440ad76294cff2f4d9cfa.jpg"
                ),
                RAWGGameTag(
                    id = 69,
                    name = "Action-Adventure",
                    slug = "action-adventure",
                    language = "eng",
                    gamesCount = 13563,
                    imageUrl = "https://media.rawg.io/media/games/7f6/7f6cd70ba2ad57053b4847c13569f2d8.jpg"
                ),
                RAWGGameTag(
                    id = 42406,
                    name = "Нагота",
                    slug = "nagota",
                    language = "rus",
                    gamesCount = 4293,
                    imageUrl = "https://media.rawg.io/media/games/473/473bd9a5e9522629d6cb28b701fb836a.jpg"
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
                    id = 468,
                    name = "role-playing",
                    slug = "role-playing",
                    language = "eng",
                    gamesCount = 1454,
                    imageUrl = "https://media.rawg.io/media/games/596/596a48ef3b62b63b4cc59633e28be903.jpg"
                ),
                RAWGGameTag(
                    id = 40832,
                    name = "Cross-Platform Multiplayer",
                    slug = "cross-platform-multiplayer",
                    language = "eng",
                    gamesCount = 2169,
                    imageUrl = "https://media.rawg.io/media/games/009/009e4e84975d6a60173ec1199db25aa3.jpg"
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
                    id = 40937,
                    name = "Steam Trading Cards",
                    slug = "steam-trading-cards-2",
                    language = "eng",
                    gamesCount = 381,
                    imageUrl = "https://media.rawg.io/media/games/6cc/6cc23249972a427f697a3d10eb57a820.jpg"
                ),
                RAWGGameTag(
                    id = 413,
                    name = "online",
                    slug = "online",
                    language = "eng",
                    gamesCount = 6624,
                    imageUrl = "https://media.rawg.io/media/games/9f1/9f1891779cb20f44de93cef33b067e50.jpg"
                ),
                RAWGGameTag(
                    id = 130,
                    name = "Driving",
                    slug = "driving",
                    language = "eng",
                    gamesCount = 4710,
                    imageUrl = "https://media.rawg.io/media/games/d7d/d7d33daa1892e2468cd0263d5dfc957e.jpg"
                ),
                RAWGGameTag(
                    id = 98,
                    name = "Loot",
                    slug = "loot",
                    language = "eng",
                    gamesCount = 1905,
                    imageUrl = "https://media.rawg.io/media/games/c6b/c6bfece1daf8d06bc0a60632ac78e5bf.jpg"
                ),
                RAWGGameTag(
                    id = 42575,
                    name = "Лут",
                    slug = "lut",
                    language = "rus",
                    gamesCount = 710,
                    imageUrl = "https://media.rawg.io/media/games/a7d/a7d57092a650030e2a868117f474efbc.jpg"
                ),
                RAWGGameTag(
                    id = 5816,
                    name = "console",
                    slug = "console",
                    language = "eng",
                    gamesCount = 1408,
                    imageUrl = "https://media.rawg.io/media/games/415/41563ce6cb061a210160687a4e5d39f6.jpg"
                ),
                RAWGGameTag(
                    id = 744,
                    name = "friends",
                    slug = "friends",
                    language = "eng",
                    gamesCount = 15183,
                    imageUrl = "https://media.rawg.io/media/games/415/41563ce6cb061a210160687a4e5d39f6.jpg"
                ),
                RAWGGameTag(
                    id = 581,
                    name = "Epic",
                    slug = "epic",
                    language = "eng",
                    gamesCount = 4101,
                    imageUrl = "https://media.rawg.io/media/screenshots/671/6716b656707c65cdf21882ee51b4f2ff.jpg"
                ),
                RAWGGameTag(
                    id = 578,
                    name = "Masterpiece",
                    slug = "masterpiece",
                    language = "eng",
                    gamesCount = 272,
                    imageUrl = "https://media.rawg.io/media/screenshots/0de/0defee61ebe38390fd3750b32213796d.jpg"
                ),
                RAWGGameTag(
                    id = 42611,
                    name = "Эпичная",
                    slug = "epichnaia",
                    language = "rus",
                    gamesCount = 95,
                    imageUrl = "https://media.rawg.io/media/games/879/879c930f9c6787c920153fa2df452eb3.jpg"
                ),
                RAWGGameTag(
                    id = 4565,
                    name = "offline",
                    slug = "offline",
                    language = "eng",
                    gamesCount = 1073,
                    imageUrl = "https://media.rawg.io/media/games/5dd/5dd4d2dd986d2826800bc37fff64aa4f.jpg"
                ),
                RAWGGameTag(
                    id = 152,
                    name = "Western",
                    slug = "western",
                    language = "eng",
                    gamesCount = 1260,
                    imageUrl = "https://media.rawg.io/media/games/985/985dc43fe4fd5f0a7ae2a725673d6ac6.jpg"
                ),
                RAWGGameTag(
                    id = 42659,
                    name = "Совместная кампания",
                    slug = "sovmestnaia-kampaniia",
                    language = "rus",
                    gamesCount = 278,
                    imageUrl = "https://media.rawg.io/media/screenshots/cfa/cfac855f997f4877b64fc908b8bda7b7.jpg"
                ),
                RAWGGameTag(
                    id = 163,
                    name = "Co-op Campaign",
                    slug = "co-op-campaign",
                    language = "eng",
                    gamesCount = 259,
                    imageUrl = "https://media.rawg.io/media/games/10d/10d19e52e5e8415d16a4d344fe711874.jpg"
                ),
                RAWGGameTag(
                    id = 1484,
                    name = "skill",
                    slug = "skill",
                    language = "eng",
                    gamesCount = 3507,
                    imageUrl = "https://media.rawg.io/media/games/fc8/fc839beb76bd63c2a5b176c46bdb7681.jpg"
                ),
                RAWGGameTag(
                    id = 500,
                    name = "Solo",
                    slug = "solo",
                    language = "eng",
                    gamesCount = 1675,
                    imageUrl = "https://media.rawg.io/media/screenshots/643/64372c2b698ff4b0608e3d72a54adf2b.jpg"
                ),
                RAWGGameTag(
                    id = 2405,
                    name = "planets",
                    slug = "planets",
                    language = "eng",
                    gamesCount = 1513,
                    imageUrl = "https://media.rawg.io/media/screenshots/839/8399a76a597fc93b43ae3103f041ea9e.jpg"
                ),
                RAWGGameTag(
                    id = 1753,
                    name = "guns",
                    slug = "guns",
                    language = "eng",
                    gamesCount = 2504,
                    imageUrl = "https://media.rawg.io/media/games/662/66261db966238da20c306c4b78ae4603.jpg"
                ),
                RAWGGameTag(
                    id = 38844,
                    name = "looter shooter",
                    slug = "looter-shooter",
                    language = "eng",
                    gamesCount = 316,
                    imageUrl = "https://media.rawg.io/media/screenshots/4a2/4a295497bbb0d1c5c5ef054bba31d41e.jpg"
                ),
                RAWGGameTag(
                    id = 499,
                    name = "Team",
                    slug = "team",
                    language = "eng",
                    gamesCount = 24,
                    imageUrl = "https://media.rawg.io/media/screenshots/87f/87f4fe4138cc8f0829acceb37dbe1734.jpg"
                ),
                RAWGGameTag(
                    id = 46115,
                    name = "LAN Co-op",
                    slug = "lan-co-op",
                    language = "eng",
                    gamesCount = 361,
                    imageUrl = "https://media.rawg.io/media/games/3cb/3cbf69d79420191a2255ffe6a580889e.jpg"
                ),
                RAWGGameTag(
                    id = 6755,
                    name = "wasteland",
                    slug = "wasteland",
                    language = "eng",
                    gamesCount = 20,
                    imageUrl = "https://media.rawg.io/media/screenshots/22c/22c30aa67d6518120727170c9ac711e4.jpg"
                ),
                RAWGGameTag(
                    id = 2723,
                    name = "trees",
                    slug = "trees",
                    language = "eng",
                    gamesCount = 618,
                    imageUrl = "https://media.rawg.io/media/screenshots/f40/f401985ddf9f041f039052b1d1c55fa3.jpg"
                )
            ),
            screenshots = listOf(
                RAWGGameScreenshot(
                    id = -1,
                    imageUrl = "https://media.rawg.io/media/games/9f1/9f1891779cb20f44de93cef33b067e50.jpg"
                ),
                RAWGGameScreenshot(
                    id = 2597139,
                    imageUrl = "https://media.rawg.io/media/screenshots/85f/85fa0742541492cb4b2562311d455918.jpg"
                ),
                RAWGGameScreenshot(
                    id = 2597140,
                    imageUrl = "https://media.rawg.io/media/screenshots/1b6/1b6159bbc9e33c29cfd47cac82322b48.jpg"
                ),
                RAWGGameScreenshot(
                    id = 2597141,
                    imageUrl = "https://media.rawg.io/media/screenshots/825/8255610d24155b27576155b21eda167d.jpg"
                ),
                RAWGGameScreenshot(
                    id = 2597142,
                    imageUrl = "https://media.rawg.io/media/screenshots/9ab/9aba5fc11168844159e3fe83d7327294.jpg"
                ),
                RAWGGameScreenshot(
                    id = 1884080,
                    imageUrl = "https://media.rawg.io/media/screenshots/293/293c4401fd411de976aec0df8597580c.jpg"
                ),
                RAWGGameScreenshot(
                    id = 1884081,
                    imageUrl = "https://media.rawg.io/media/screenshots/c3e/c3ef63402fd812717da342ba73444ca0.jpg"
                )
            ),
            genres = listOf(
                RAWGGameGenre(
                    id = 2,
                    name = "Shooter",
                    slug = "shooter",
                    gamesCount = null,
                    imageUrl = null
                ),
                RAWGGameGenre(
                    id = 3,
                    name = "Adventure",
                    slug = "adventure",
                    gamesCount = null,
                    imageUrl = null
                ),
                RAWGGameGenre(
                    id = 4,
                    name = "Action",
                    slug = "action",
                    gamesCount = null,
                    imageUrl = null
                ),
                RAWGGameGenre(
                    id = 5,
                    name = "RPG",
                    slug = "role-playing-games-rpg",
                    gamesCount = null,
                    imageUrl = null
                )
            )
        ),
        RAWGShallowGame(
            id = 326252,
            slug = "gears-5",
            name = "Gears 5",
            releaseDate = "2019-09-10",
            toBeAnnounced = false,
            imageUrl = "https://media.rawg.io/media/games/121/1213f8b9b0a26307e672cf51f34882f8.jpg",
            rating = 3.93f,
            metaCriticScore = 83,
            playtime = 6,
            suggestionsCount = 646,
            platforms = listOf(
                ShallowRAWGPlatformInfo(
                    platform = RAWGPlatformProperties(
                        id = 4,
                        name = "PC",
                        slug = "pc",
                        imageUrl = null,
                        yearEnd = null,
                        yearStart = null,
                        gamesCount = null,
                        imageBackground = null
                    )
                ),
                ShallowRAWGPlatformInfo(
                    platform = RAWGPlatformProperties(
                        id = 1,
                        name = "Xbox One",
                        slug = "xbox-one",
                        imageUrl = null,
                        yearEnd = null,
                        yearStart = null,
                        gamesCount = null,
                        imageBackground = null
                    )
                ),
                ShallowRAWGPlatformInfo(
                    platform = RAWGPlatformProperties(
                        id = 186,
                        name = "Xbox Series S/X",
                        slug = "xbox-series-x",
                        imageUrl = null,
                        yearEnd = null,
                        yearStart = null,
                        gamesCount = null,
                        imageBackground = null
                    )
                )
            ),
            stores = listOf(
                ShallowRAWGStoreInfo(
                    store = RAWGStoreProperties(
                        id = 1,
                        name = "Steam",
                        slug = "steam",
                        domain = null,
                        gamesCount = null,
                        imageUrl = null
                    )
                ),
                ShallowRAWGStoreInfo(
                    store = RAWGStoreProperties(
                        id = 2,
                        name = "Xbox Store",
                        slug = "xbox-store",
                        domain = null,
                        gamesCount = null,
                        imageUrl = null
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
                    id = 42396,
                    name = "Для одного игрока",
                    slug = "dlia-odnogo-igroka",
                    language = "rus",
                    gamesCount = 32259,
                    imageUrl = "https://media.rawg.io/media/games/618/618c2031a07bbff6b4f611f10b6bcdbc.jpg"
                ),
                RAWGGameTag(
                    id = 42417,
                    name = "Экшен",
                    slug = "ekshen",
                    language = "rus",
                    gamesCount = 30948,
                    imageUrl = "https://media.rawg.io/media/games/d58/d588947d4286e7b5e0e12e1bea7d9844.jpg"
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
                    id = 42425,
                    name = "Для нескольких игроков",
                    slug = "dlia-neskolkikh-igrokov",
                    language = "rus",
                    gamesCount = 7497,
                    imageUrl = "https://media.rawg.io/media/games/d69/d69810315bd7e226ea2d21f9156af629.jpg"
                ),
                RAWGGameTag(
                    id = 42394,
                    name = "Глубокий сюжет",
                    slug = "glubokii-siuzhet",
                    language = "rus",
                    gamesCount = 8424,
                    imageUrl = "https://media.rawg.io/media/games/4cf/4cfc6b7f1850590a4634b08bfab308ab.jpg"
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
                    id = 411,
                    name = "cooperative",
                    slug = "cooperative",
                    language = "eng",
                    gamesCount = 3925,
                    imageUrl = "https://media.rawg.io/media/games/d0f/d0f91fe1d92332147e5db74e207cfc7a.jpg"
                ),
                RAWGGameTag(
                    id = 40845,
                    name = "Partial Controller Support",
                    slug = "partial-controller-support",
                    language = "eng",
                    gamesCount = 9487,
                    imageUrl = "https://media.rawg.io/media/games/7fa/7fa0b586293c5861ee32490e953a4996.jpg"
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
                    id = 42491,
                    name = "Мясо",
                    slug = "miaso",
                    language = "rus",
                    gamesCount = 3817,
                    imageUrl = "https://media.rawg.io/media/games/7f6/7f6cd70ba2ad57053b4847c13569f2d8.jpg"
                ),
                RAWGGameTag(
                    id = 26,
                    name = "Gore",
                    slug = "gore",
                    language = "eng",
                    gamesCount = 5029,
                    imageUrl = "https://media.rawg.io/media/games/d58/d588947d4286e7b5e0e12e1bea7d9844.jpg"
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
                    id = 42404,
                    name = "Женщина-протагонист",
                    slug = "zhenshchina-protagonist",
                    language = "rus",
                    gamesCount = 2416,
                    imageUrl = "https://media.rawg.io/media/games/62c/62c7c8b28a27b83680b22fb9d33fc619.jpg"
                ),
                RAWGGameTag(
                    id = 42402,
                    name = "Насилие",
                    slug = "nasilie",
                    language = "rus",
                    gamesCount = 4717,
                    imageUrl = "https://media.rawg.io/media/games/951/951572a3dd1e42544bd39a5d5b42d234.jpg"
                ),
                RAWGGameTag(
                    id = 34,
                    name = "Violent",
                    slug = "violent",
                    language = "eng",
                    gamesCount = 5852,
                    imageUrl = "https://media.rawg.io/media/games/5be/5bec14622f6faf804a592176577c1347.jpg"
                ),
                RAWGGameTag(
                    id = 397,
                    name = "Online multiplayer",
                    slug = "online-multiplayer",
                    language = "eng",
                    gamesCount = 3811,
                    imageUrl = "https://media.rawg.io/media/games/fc0/fc076b974197660a582abd34ebccc27f.jpg"
                ),
                RAWGGameTag(
                    id = 198,
                    name = "Split Screen",
                    slug = "split-screen",
                    language = "eng",
                    gamesCount = 5481,
                    imageUrl = "https://media.rawg.io/media/games/27b/27b02ffaab6b250cc31bf43baca1fc34.jpg"
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
                    id = 42446,
                    name = "Шутер от третьего лица",
                    slug = "shuter-ot-tretego-litsa",
                    language = "rus",
                    gamesCount = 1410,
                    imageUrl = "https://media.rawg.io/media/games/e2d/e2d3f396b16dded0f841c17c9799a882.jpg"
                ),
                RAWGGameTag(
                    id = 72,
                    name = "Local Multiplayer",
                    slug = "local-multiplayer",
                    language = "eng",
                    gamesCount = 12736,
                    imageUrl = "https://media.rawg.io/media/games/bbf/bbf8d74ab64440ad76294cff2f4d9cfa.jpg"
                ),
                RAWGGameTag(
                    id = 40832,
                    name = "Cross-Platform Multiplayer",
                    slug = "cross-platform-multiplayer",
                    language = "eng",
                    gamesCount = 2169,
                    imageUrl = "https://media.rawg.io/media/games/009/009e4e84975d6a60173ec1199db25aa3.jpg"
                ),
                RAWGGameTag(
                    id = 81,
                    name = "Military",
                    slug = "military",
                    language = "eng",
                    gamesCount = 1305,
                    imageUrl = "https://media.rawg.io/media/games/ac7/ac7b8327343da12c971cfc418f390a11.jpg"
                ),
                RAWGGameTag(
                    id = 3068,
                    name = "future",
                    slug = "future",
                    language = "eng",
                    gamesCount = 3176,
                    imageUrl = "https://media.rawg.io/media/screenshots/f85/f856c14f8c8d7cb72085723960f5c8d5.jpg"
                )
            ),
            screenshots = listOf(
                RAWGGameScreenshot(
                    id = -1,
                    imageUrl = "https://media.rawg.io/media/games/121/1213f8b9b0a26307e672cf51f34882f8.jpg"
                ),
                RAWGGameScreenshot(
                    id = 1957721,
                    imageUrl = "https://media.rawg.io/media/screenshots/9f0/9f085738a4ee6bb44b4b26cd3eb9ef93.jpg"
                ),
                RAWGGameScreenshot(
                    id = 1957722,
                    imageUrl = "https://media.rawg.io/media/screenshots/1c8/1c8eb3c87b9396e924ade589d543790e.jpg"
                ),
                RAWGGameScreenshot(
                    id = 1957723,
                    imageUrl = "https://media.rawg.io/media/screenshots/a83/a832752f82cfde4a811c581e9cd3efd0.jpg"
                ),
                RAWGGameScreenshot(
                    id = 1957724,
                    imageUrl = "https://media.rawg.io/media/screenshots/a6f/a6fafe1183ce4b3f68287219ea3dd6c8.jpg"
                ),
                RAWGGameScreenshot(
                    id = 1957725,
                    imageUrl = "https://media.rawg.io/media/screenshots/38c/38c4c8601a72e1d0dacf6d1fe1c5dfb3.jpg"
                ),
                RAWGGameScreenshot(
                    id = 778890,
                    imageUrl = "https://media.rawg.io/media/screenshots/ae7/ae7fcd1a2d11be26e1ea72e6d5d83ecc.jpg"
                )
            ),
            genres = listOf(
                RAWGGameGenre(
                    id = 2,
                    name = "Shooter",
                    slug = "shooter",
                    gamesCount = null,
                    imageUrl = null
                ),
                RAWGGameGenre(
                    id = 4,
                    name = "Action",
                    slug = "action",
                    gamesCount = null,
                    imageUrl = null
                )
            )
        ),
        RAWGShallowGame(
            id = 258322,
            slug = "blasphemous",
            name = "Blasphemous",
            releaseDate = "2019-09-09",
            toBeAnnounced = false,
            imageUrl = "https://media.rawg.io/media/games/b01/b01aa6b2d6d4f683203e9471a8b8d5b5.jpg",
            rating = 4.04f,
            metaCriticScore = 78,
            playtime = 3,
            suggestionsCount = 393,
            platforms = listOf(
                ShallowRAWGPlatformInfo(
                    platform = RAWGPlatformProperties(
                        id = 4,
                        name = "PC",
                        slug = "pc",
                        imageUrl = null,
                        yearEnd = null,
                        yearStart = null,
                        gamesCount = null,
                        imageBackground = null
                    )
                ),
                ShallowRAWGPlatformInfo(
                    platform = RAWGPlatformProperties(
                        id = 1,
                        name = "Xbox One",
                        slug = "xbox-one",
                        imageUrl = null,
                        yearEnd = null,
                        yearStart = null,
                        gamesCount = null,
                        imageBackground = null
                    )
                ),
                ShallowRAWGPlatformInfo(
                    platform = RAWGPlatformProperties(
                        id = 18,
                        name = "PlayStation 4",
                        slug = "playstation4",
                        imageUrl = null,
                        yearEnd = null,
                        yearStart = null,
                        gamesCount = null,
                        imageBackground = null
                    )
                ),
                ShallowRAWGPlatformInfo(
                    platform = RAWGPlatformProperties(
                        id = 7,
                        name = "Nintendo Switch",
                        slug = "nintendo-switch",
                        imageUrl = null,
                        yearEnd = null,
                        yearStart = null,
                        gamesCount = null,
                        imageBackground = null
                    )
                ),
                ShallowRAWGPlatformInfo(
                    platform = RAWGPlatformProperties(
                        id = 5,
                        name = "macOS",
                        slug = "macos",
                        imageUrl = null,
                        yearEnd = null,
                        yearStart = null,
                        gamesCount = null,
                        imageBackground = null
                    )
                ),
                ShallowRAWGPlatformInfo(
                    platform = RAWGPlatformProperties(
                        id = 6,
                        name = "Linux",
                        slug = "linux",
                        imageUrl = null,
                        yearEnd = null,
                        yearStart = null,
                        gamesCount = null,
                        imageBackground = null
                    )
                )
            ),
            stores = listOf(
                ShallowRAWGStoreInfo(
                    store = RAWGStoreProperties(
                        id = 1,
                        name = "Steam",
                        slug = "steam",
                        domain = null,
                        gamesCount = null,
                        imageUrl = null
                    )
                ),
                ShallowRAWGStoreInfo(
                    store = RAWGStoreProperties(
                        id = 3,
                        name = "PlayStation Store",
                        slug = "playstation-store",
                        domain = null,
                        gamesCount = null,
                        imageUrl = null
                    )
                ),
                ShallowRAWGStoreInfo(
                    store = RAWGStoreProperties(
                        id = 2,
                        name = "Xbox Store",
                        slug = "xbox-store",
                        domain = null,
                        gamesCount = null,
                        imageUrl = null
                    )
                ),
                ShallowRAWGStoreInfo(
                    store = RAWGStoreProperties(
                        id = 5,
                        name = "GOG",
                        slug = "gog",
                        domain = null,
                        gamesCount = null,
                        imageUrl = null
                    )
                ),
                ShallowRAWGStoreInfo(
                    store = RAWGStoreProperties(
                        id = 6,
                        name = "Nintendo Store",
                        slug = "nintendo",
                        domain = null,
                        gamesCount = null,
                        imageUrl = null
                    )
                ),
                ShallowRAWGStoreInfo(
                    store = RAWGStoreProperties(
                        id = 11,
                        name = "Epic Games",
                        slug = "epic-games",
                        domain = null,
                        gamesCount = null,
                        imageUrl = null
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
                    id = 42396,
                    name = "Для одного игрока",
                    slug = "dlia-odnogo-igroka",
                    language = "rus",
                    gamesCount = 32259,
                    imageUrl = "https://media.rawg.io/media/games/618/618c2031a07bbff6b4f611f10b6bcdbc.jpg"
                ),
                RAWGGameTag(
                    id = 42417,
                    name = "Экшен",
                    slug = "ekshen",
                    language = "rus",
                    gamesCount = 30948,
                    imageUrl = "https://media.rawg.io/media/games/d58/d588947d4286e7b5e0e12e1bea7d9844.jpg"
                ),
                RAWGGameTag(
                    id = 42392,
                    name = "Приключение",
                    slug = "prikliuchenie",
                    language = "rus",
                    gamesCount = 28893,
                    imageUrl = "https://media.rawg.io/media/games/960/960b601d9541cec776c5fa42a00bf6c4.jpg"
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
                    id = 42398,
                    name = "Инди",
                    slug = "indi-2",
                    language = "rus",
                    gamesCount = 44678,
                    imageUrl = "https://media.rawg.io/media/games/226/2262cea0b385db6cf399f4be831603b0.jpg"
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
                    id = 42401,
                    name = "Отличный саундтрек",
                    slug = "otlichnyi-saundtrek",
                    language = "rus",
                    gamesCount = 4453,
                    imageUrl = "https://media.rawg.io/media/games/618/618c2031a07bbff6b4f611f10b6bcdbc.jpg"
                ),
                RAWGGameTag(
                    id = 45,
                    name = "2D",
                    slug = "2d",
                    language = "eng",
                    gamesCount = 197659,
                    imageUrl = "https://media.rawg.io/media/screenshots/c97/c97b943741f5fbc936fe054d9d58851d.jpg"
                ),
                RAWGGameTag(
                    id = 42420,
                    name = "Сложная",
                    slug = "slozhnaia",
                    language = "rus",
                    gamesCount = 4353,
                    imageUrl = "https://media.rawg.io/media/games/9bf/9bfac18ff678f41a4674250fa0e04a52.jpg"
                ),
                RAWGGameTag(
                    id = 42491,
                    name = "Мясо",
                    slug = "miaso",
                    language = "rus",
                    gamesCount = 3817,
                    imageUrl = "https://media.rawg.io/media/games/7f6/7f6cd70ba2ad57053b4847c13569f2d8.jpg"
                ),
                RAWGGameTag(
                    id = 26,
                    name = "Gore",
                    slug = "gore",
                    language = "eng",
                    gamesCount = 5029,
                    imageUrl = "https://media.rawg.io/media/games/d58/d588947d4286e7b5e0e12e1bea7d9844.jpg"
                ),
                RAWGGameTag(
                    id = 49,
                    name = "Difficult",
                    slug = "difficult",
                    language = "eng",
                    gamesCount = 12897,
                    imageUrl = "https://media.rawg.io/media/games/6cd/6cd653e0aaef5ff8bbd295bf4bcb12eb.jpg"
                ),
                RAWGGameTag(
                    id = 6,
                    name = "Exploration",
                    slug = "exploration",
                    language = "eng",
                    gamesCount = 19251,
                    imageUrl = "https://media.rawg.io/media/games/34b/34b1f1850a1c06fd971bc6ab3ac0ce0e.jpg"
                ),
                RAWGGameTag(
                    id = 42463,
                    name = "Платформер",
                    slug = "platformer-2",
                    language = "rus",
                    gamesCount = 6201,
                    imageUrl = "https://media.rawg.io/media/games/fc8/fc838d98c9b944e6a15176eabf40bee8.jpg"
                ),
                RAWGGameTag(
                    id = 42402,
                    name = "Насилие",
                    slug = "nasilie",
                    language = "rus",
                    gamesCount = 4717,
                    imageUrl = "https://media.rawg.io/media/games/951/951572a3dd1e42544bd39a5d5b42d234.jpg"
                ),
                RAWGGameTag(
                    id = 34,
                    name = "Violent",
                    slug = "violent",
                    language = "eng",
                    gamesCount = 5852,
                    imageUrl = "https://media.rawg.io/media/games/5be/5bec14622f6faf804a592176577c1347.jpg"
                ),
                RAWGGameTag(
                    id = 42464,
                    name = "Исследование",
                    slug = "issledovanie",
                    language = "rus",
                    gamesCount = 2990,
                    imageUrl = "https://media.rawg.io/media/games/bce/bce62fbc7cf74bf6a1a37340993ec148.jpg"
                ),
                RAWGGameTag(
                    id = 42415,
                    name = "Пиксельная графика",
                    slug = "pikselnaia-grafika",
                    language = "rus",
                    gamesCount = 8404,
                    imageUrl = "https://media.rawg.io/media/screenshots/f81/f81fd968a3161e7d35612d8c4232923e.jpg"
                ),
                RAWGGameTag(
                    id = 122,
                    name = "Pixel Graphics",
                    slug = "pixel-graphics",
                    language = "eng",
                    gamesCount = 93980,
                    imageUrl = "https://media.rawg.io/media/games/501/501e7019925a3c692bf1c8062f07abe6.jpg"
                ),
                RAWGGameTag(
                    id = 42406,
                    name = "Нагота",
                    slug = "nagota",
                    language = "rus",
                    gamesCount = 4293,
                    imageUrl = "https://media.rawg.io/media/games/473/473bd9a5e9522629d6cb28b701fb836a.jpg"
                ),
                RAWGGameTag(
                    id = 468,
                    name = "role-playing",
                    slug = "role-playing",
                    language = "eng",
                    gamesCount = 1454,
                    imageUrl = "https://media.rawg.io/media/games/596/596a48ef3b62b63b4cc59633e28be903.jpg"
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
                    id = 42469,
                    name = "Вид сбоку",
                    slug = "vid-sboku",
                    language = "rus",
                    gamesCount = 2850,
                    imageUrl = "https://media.rawg.io/media/games/9cc/9cc11e2e81403186c7fa9c00c143d6e4.jpg"
                ),
                RAWGGameTag(
                    id = 42506,
                    name = "Тёмное фэнтези",
                    slug = "tiomnoe-fentezi",
                    language = "rus",
                    gamesCount = 1786,
                    imageUrl = "https://media.rawg.io/media/games/789/7896837ec22a83e4007018ddd55e8c9a.jpg"
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
                    id = 259,
                    name = "Metroidvania",
                    slug = "metroidvania",
                    language = "eng",
                    gamesCount = 4024,
                    imageUrl = "https://media.rawg.io/media/games/c40/c40f9f0a3d1b4601a7a44d230c95f126.jpg"
                ),
                RAWGGameTag(
                    id = 42462,
                    name = "Метроидвания",
                    slug = "metroidvaniia",
                    language = "rus",
                    gamesCount = 887,
                    imageUrl = "https://media.rawg.io/media/games/c50/c5085506fe4b5e20fc7aa5ace842c20b.jpg"
                ),
                RAWGGameTag(
                    id = 42592,
                    name = "Похожа на Dark Souls",
                    slug = "pokhozha-na-dark-souls",
                    language = "rus",
                    gamesCount = 585,
                    imageUrl = "https://media.rawg.io/media/games/d09/d096ad37b7f522e11c02848252213a9a.jpg"
                ),
                RAWGGameTag(
                    id = 205,
                    name = "Lore-Rich",
                    slug = "lore-rich",
                    language = "eng",
                    gamesCount = 718,
                    imageUrl = "https://media.rawg.io/media/screenshots/c98/c988bb637b38eac52b3ea878781e73d0.jpg"
                ),
                RAWGGameTag(
                    id = 42594,
                    name = "Проработанная вселенная",
                    slug = "prorabotannaia-vselennaia",
                    language = "rus",
                    gamesCount = 745,
                    imageUrl = "https://media.rawg.io/media/screenshots/525/525b5da62342fa726bfe2820e8f93c09.jpg"
                ),
                RAWGGameTag(
                    id = 42677,
                    name = "Готика",
                    slug = "gotika",
                    language = "rus",
                    gamesCount = 339,
                    imageUrl = "https://media.rawg.io/media/games/af7/af7a831001c5c32c46e950cc883b8cb7.jpg"
                ),
                RAWGGameTag(
                    id = 580,
                    name = "Souls-like",
                    slug = "souls-like",
                    language = "eng",
                    gamesCount = 988,
                    imageUrl = "https://media.rawg.io/media/games/3b0/3b0f57d0fbb23854f300fb203c18889b.jpg"
                )
            ),
            screenshots = listOf(
                RAWGGameScreenshot(
                    id = -1,
                    imageUrl = "https://media.rawg.io/media/games/b01/b01aa6b2d6d4f683203e9471a8b8d5b5.jpg"
                ),
                RAWGGameScreenshot(
                    id = 1702324,
                    imageUrl = "https://media.rawg.io/media/screenshots/350/35004ab01b59310d9682c069efe0c0b2.jpg"
                ),
                RAWGGameScreenshot(
                    id = 1702325,
                    imageUrl = "https://media.rawg.io/media/screenshots/993/9930282406e7dd2819451ec16373a688.jpg"
                ),
                RAWGGameScreenshot(
                    id = 1702329,
                    imageUrl = "https://media.rawg.io/media/screenshots/b70/b70109ffdfabfe36e36cc43e1ad80277.jpg"
                ),
                RAWGGameScreenshot(
                    id = 1702331,
                    imageUrl = "https://media.rawg.io/media/screenshots/29d/29d417920697a7c637612a9ea7cd7d74.jpg"
                ),
                RAWGGameScreenshot(
                    id = 1702332,
                    imageUrl = "https://media.rawg.io/media/screenshots/a14/a14cacc86c817d7f039ac1f9ac2819e1.jpg"
                ),
                RAWGGameScreenshot(
                    id = 1702333,
                    imageUrl = "https://media.rawg.io/media/screenshots/1f7/1f7da2126ecea73a7a44623f768f7b94.jpg"
                )
            ),
            genres = listOf(
                RAWGGameGenre(
                    id = 51,
                    name = "Indie",
                    slug = "indie",
                    gamesCount = null,
                    imageUrl = null
                ),
                RAWGGameGenre(
                    id = 83,
                    name = "Platformer",
                    slug = "platformer",
                    gamesCount = null,
                    imageUrl = null
                ),
                RAWGGameGenre(
                    id = 3,
                    name = "Adventure",
                    slug = "adventure",
                    gamesCount = null,
                    imageUrl = null
                ),
                RAWGGameGenre(
                    id = 4,
                    name = "Action",
                    slug = "action",
                    gamesCount = null,
                    imageUrl = null
                )
            )
        )
    )
)

// TODO: write a ksp plugin to generate this boilerplate automatically
/**
 * Returns the model with all string values in it wrapped in quotes.
 * This is typically useful in copying & pasting large fake data
 */
internal fun RAWGPage.quotify(): RAWGPage {
    return let {
        RAWGPage(
            count = it.count,
            nextPageUrl = "\"${it.nextPageUrl}\"",
            previousPageUrl = it.previousPageUrl?.let { "\"$it\"" },
            results = it.results.map {
                RAWGShallowGame(
                    id = it.id,
                    name = "\"${it.name}\"",
                    slug = "\"${it.slug}\"",
                    releaseDate = "\"${it.releaseDate}\"",
                    toBeAnnounced = it.toBeAnnounced,
                    imageUrl = "\"${it.imageUrl}\"",
                    rating = it.rating,
                    metaCriticScore = it.metaCriticScore,
                    playtime = it.playtime,
                    suggestionsCount = it.suggestionsCount,
                    platforms = it.platforms?.map {
                        ShallowRAWGPlatformInfo(
                            platform = RAWGPlatformProperties(
                                id = it.platform.id,
                                name = "\"${it.platform.name}\"",
                                slug = "\"${it.platform.slug}\"",
                                imageUrl = it.platform.imageUrl?.let { "\"$it\"" },
                                yearEnd = it.platform.yearEnd,
                                yearStart = it.platform.yearStart,
                                gamesCount = it.platform.gamesCount,
                                imageBackground = it.platform.imageBackground?.let { "\"$it\"" }
                            )
//                            releaseDate = (it as DetailedRAWGPlatformInfo).releaseDate?.let { "\"$it\"" },
//                            requirementsInEnglish = if (it.requirementsInEnglish != null) RAWGPlatformRequirements(
//                                minimum = "\"${it.requirementsInEnglish!!.minimum}\"",
//                                recommended = "\"${it.requirementsInEnglish!!.recommended}\"",
//                            ) else null
                        )
                    },
                    stores = it.stores?.map {
//                        it as ShallowRAWGStoreInfoWithId
                        ShallowRAWGStoreInfo(
//                            id = it.id,
                            store = RAWGStoreProperties(
                                id = it.store.id,
                                name = "\"${it.store.name}\"",
                                slug = "\"${it.store.slug}\"",
                                domain = it.store.domain?.let { "\"$it\"" },
                                gamesCount = it.store.gamesCount,
                                imageUrl = it.store.imageUrl?.let { "\"$it\"" }
                            )
                        )
                    },
                    tags = it.tags.map {
                        RAWGGameTag(
                            id = it.id,
                            name = "\"${it.name}\"",
                            slug = "\"${it.slug}\"",
                            language = "\"${it.language}\"",
                            gamesCount = it.gamesCount,
                            imageUrl = it.imageUrl.let { "\"$it\"" }
                        )
                    },
                    screenshots = it.screenshots.map {
                        RAWGGameScreenshot(
                            id = it.id,
                            imageUrl = "\"${it.imageUrl}\""
                        )
                    },
                    genres = it.genres.map {
                        RAWGGameGenre(
                            id = it.id,
                            name = "\"${it.name}\"",
                            slug = "\"${it.slug}\"",
                            gamesCount = it.gamesCount,
                            imageUrl = it.imageUrl?.let { "\"$it\"" }
                        )
                    }
                )
            }
        )
    }
}
