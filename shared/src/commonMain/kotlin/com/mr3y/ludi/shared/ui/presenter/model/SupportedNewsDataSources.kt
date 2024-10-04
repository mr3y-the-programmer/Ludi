package com.mr3y.ludi.shared.ui.presenter.model

import com.mr3y.ludi.shared.core.model.Source
import ludi.shared.generated.resources.Res
import ludi.shared.generated.resources.brutalgamer_logo
import ludi.shared.generated.resources.eurogamer_logo
import ludi.shared.generated.resources.game_spot_logo
import ludi.shared.generated.resources.gamerant_logo
import ludi.shared.generated.resources.giant_bomb_logo
import ludi.shared.generated.resources.gloriousgaming_logo
import ludi.shared.generated.resources.ign_logo
import ludi.shared.generated.resources.pcgamer_logo
import ludi.shared.generated.resources.pcgamesn_logo
import ludi.shared.generated.resources.pcinvasion_logo
import ludi.shared.generated.resources.polygon_logo
import ludi.shared.generated.resources.rockpapershotgun_logo
import ludi.shared.generated.resources.tech_radar_logo
import ludi.shared.generated.resources.thegamer_logo
import ludi.shared.generated.resources.venturebeat_logo
import ludi.shared.generated.resources.vg247_logo

val SupportedNewsDataSources = listOf(
    NewsDataSource("Game spot", Res.drawable.game_spot_logo, Source.GameSpot),
    NewsDataSource("Giant bomb", Res.drawable.giant_bomb_logo, Source.GiantBomb),
    NewsDataSource("IGN", Res.drawable.ign_logo, Source.IGN),
    NewsDataSource("Tech Radar", Res.drawable.tech_radar_logo, Source.TechRadar),
    NewsDataSource("PCGamesN", Res.drawable.pcgamesn_logo, Source.PCGamesN),
    NewsDataSource("PCGamer", Res.drawable.pcgamer_logo, Source.PCGamer),
    NewsDataSource("PC Invasion", Res.drawable.pcinvasion_logo, Source.PCInvasion),
    NewsDataSource("Euro Gamer", Res.drawable.eurogamer_logo, Source.EuroGamer),
    NewsDataSource("VentureBeat", Res.drawable.venturebeat_logo, Source.VentureBeat),
    NewsDataSource("VG247", Res.drawable.vg247_logo, Source.VG247),
    NewsDataSource("Glorious Gaming", Res.drawable.gloriousgaming_logo, Source.GloriousGaming),
    NewsDataSource("Rock Paper Shotgun", Res.drawable.rockpapershotgun_logo, Source.RockPaperShotgun),
    NewsDataSource("Game Rant", Res.drawable.gamerant_logo, Source.GameRant),
    NewsDataSource("The Gamer", Res.drawable.thegamer_logo, Source.TheGamer),
    NewsDataSource("Polygon", Res.drawable.polygon_logo, Source.Polygon),
    NewsDataSource("Brutal Gamer", Res.drawable.brutalgamer_logo, Source.BrutalGamer)
)
