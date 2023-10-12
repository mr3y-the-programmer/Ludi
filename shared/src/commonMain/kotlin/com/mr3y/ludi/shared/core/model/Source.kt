package com.mr3y.ludi.shared.core.model

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

internal fun mapTypeToIconRes(type: Source): String {
    return when(type) {
        Source.GiantBomb -> "giant_bomb_logo.xml"
        Source.GameSpot -> "game_spot_logo.xml"
        Source.IGN -> "ign_logo.xml"
        Source.TechRadar -> "tech_radar_logo.xml"
        Source.PCGamesN -> "pcgamesn_logo.xml"
        Source.RockPaperShotgun -> "rockpapershotgun_logo.xml"
        Source.PCInvasion -> "pcinvasion_logo.xml"
        Source.GloriousGaming -> "gloriousgaming_logo.xml"
        Source.EuroGamer -> "eurogamer_logo.xml"
        Source.VG247 -> "vg247_logo.xml"
        Source.TheGamer -> "thegamer_logo.xml"
        Source.GameRant -> "gamerant_logo.xml"
        Source.BrutalGamer -> "brutalgamer_logo.xml"
        Source.VentureBeat -> "venturebeat_logo.xml"
        Source.Polygon -> "polygon_logo.xml"
        Source.PCGamer -> "pcgamer_logo.xml"
    }
}
