package com.mr3y.ludi.shared.core.model

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
import org.jetbrains.compose.resources.DrawableResource

enum class Source {
    GiantBomb,
    GameSpot,
    IGN,
    TechRadar,
    PCGamesN,
    RockPaperShotgun,
    PCInvasion,
    GloriousGaming,
    EuroGamer,
    VG247,
    TheGamer,
    GameRant,
    BrutalGamer,
    VentureBeat,
    Polygon,
    PCGamer
}

internal fun mapTypeToIconRes(type: Source): DrawableResource {
    return when (type) {
        Source.GiantBomb -> Res.drawable.giant_bomb_logo
        Source.GameSpot -> Res.drawable.game_spot_logo
        Source.IGN -> Res.drawable.ign_logo
        Source.TechRadar -> Res.drawable.tech_radar_logo
        Source.PCGamesN -> Res.drawable.pcgamesn_logo
        Source.RockPaperShotgun -> Res.drawable.rockpapershotgun_logo
        Source.PCInvasion -> Res.drawable.pcinvasion_logo
        Source.GloriousGaming -> Res.drawable.gloriousgaming_logo
        Source.EuroGamer -> Res.drawable.eurogamer_logo
        Source.VG247 -> Res.drawable.vg247_logo
        Source.TheGamer -> Res.drawable.thegamer_logo
        Source.GameRant -> Res.drawable.gamerant_logo
        Source.BrutalGamer -> Res.drawable.brutalgamer_logo
        Source.VentureBeat -> Res.drawable.venturebeat_logo
        Source.Polygon -> Res.drawable.polygon_logo
        Source.PCGamer -> Res.drawable.pcgamer_logo
    }
}
