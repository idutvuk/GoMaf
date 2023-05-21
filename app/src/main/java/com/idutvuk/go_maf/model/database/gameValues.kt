package com.idutvuk.go_maf.model.database

import java.sql.Date
import java.sql.Time

val games = listOf(
    MafiaGame(id = 0,date = Date(0L),time = Time(0L),isCompleted = false,duration = Time(0L))
)

val users = listOf(
    MafiaAccount(
        id = 1,
        nickname = "CrimsonBlade",
        xp = 500,
        telegramLink = "t.me/CrimsonBlade"
    ),
    MafiaAccount(
        id = 2,
        nickname = "ShadowAssassin",
        xp = 750,
        telegramLink = "t.me/ShadowAssassin"
    ),
    MafiaAccount(
        id = 3,
        nickname = "NightStalker",
        xp = 600,
        telegramLink = "t.me/NightStalker"
    ),
    MafiaAccount(
        id = 4,
        nickname = "ScarletViper",
        xp = 800,
        telegramLink = "t.me/ScarletViper"
    ),
    MafiaAccount(
        id = 5,
        nickname = "BlackWidow",
        xp = 900,
        telegramLink = "t.me/BlackWidow"
    ),
    MafiaAccount(
        id = 6,
        nickname = "RedHawk",
        xp = 400,
        telegramLink = "t.me/RedHawk"
    ),
    MafiaAccount(
        id = 7,
        nickname = "BlueDragon",
        xp = 300,
        telegramLink = "t.me/BlueDragon"
    ),
    MafiaAccount(
        id = 8,
        nickname = "GreenSnake",
        xp = 550,
        telegramLink = "t.me/GreenSnake"
    ),
    MafiaAccount(
        id = 9,
        nickname = "WhiteWolf",
        xp = 700,
        telegramLink = "t.me/WhiteWolf"
    ),
    MafiaAccount(
        id = 10,
        nickname = "GoldenEagle",
        xp = 850,
        telegramLink = "t.me/GoldenEagle"
    ),
    MafiaAccount(
        id = 11,
        nickname = "SilverFox",
        xp = 650,
        telegramLink = "t.me/SilverFox"
    ),
    MafiaAccount(
        id = 12,
        nickname = "ElectricSquid",
        xp = 450,
        telegramLink = "t.me/ElectricSquid"
    ),
    MafiaAccount(
        id = 13,
        nickname = "PoisonIvy",
        xp = 550,
        telegramLink = "t.me/PoisonIvy"
    ),
    MafiaAccount(
        id = 14,
        nickname = "SapphireKnight",
        xp = 800,
        telegramLink = "t.me/SapphireKnight"
    ),
    MafiaAccount(
        id = 15,
        nickname = "AmberLion",
        xp = 750,
        telegramLink = "t.me/AmberLion"
    ),
    MafiaAccount(
        id = 16,
        nickname = "PlatinumPhoenix",
        xp = 900,
        telegramLink = "t.me/PlatinumPhoenix"
    ),
    MafiaAccount(
        id = 17,
        nickname = "RubyRaven",
        xp = 700,
        telegramLink = "t.me/RubyRaven"
    ),
    MafiaAccount(
        id = 18,
        nickname = "EmeraldTiger",
        xp = 600,
        telegramLink = "t.me/EmeraldTiger"
    ),
    MafiaAccount(
        id = 19,
        nickname = "ObsidianPanther",
        xp = 850,
        telegramLink = "t.me/ObsidianPanther"
    ),
    MafiaAccount(
        id = 20,
        nickname = "DiamondDragon",
        xp = 500,
        telegramLink = "t.me/DiamondDragon"
    )
)
