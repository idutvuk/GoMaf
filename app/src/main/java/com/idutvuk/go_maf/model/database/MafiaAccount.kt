package com.idutvuk.go_maf.model.database

import com.idutvuk.go_maf.R

data class MafiaAccount( //TODO extract it to individiual database of users
    val id: Int,
    val nickname: String,
    val xp: Int,
    val telegramLink: String,
    val pictureId: Int //todo maybe remove?
) {
    companion object {
        val users = listOf(
            MafiaAccount(
                id = 1,
                nickname = "CrimsonBlade",
                xp = 500,
                telegramLink = "t.me/CrimsonBlade",
                pictureId = R.drawable.black1
            ),
            MafiaAccount(
                id = 2,
                nickname = "ShadowAssassin",
                xp = 750,
                telegramLink = "t.me/ShadowAssassin",
                pictureId = R.drawable.black4
            ),
            MafiaAccount(
                id = 3,
                nickname = "NightStalker",
                xp = 600,
                telegramLink = "t.me/NightStalker",
                pictureId = R.drawable.black4
            ),
            MafiaAccount(
                id = 4,
                nickname = "ScarletViper",
                xp = 800,
                telegramLink = "t.me/ScarletViper",
                pictureId = R.drawable.black7
            ),
            MafiaAccount(
                id = 5,
                nickname = "BlackWidow",
                xp = 900,
                telegramLink = "t.me/BlackWidow",
                pictureId = R.drawable.black4
            ),
            MafiaAccount(
                id = 6,
                nickname = "RedHawk",
                xp = 400,
                telegramLink = "t.me/RedHawk",
                pictureId = R.drawable.red2
            ),
            MafiaAccount(
                id = 7,
                nickname = "BlueDragon",
                xp = 300,
                telegramLink = "t.me/BlueDragon",
                pictureId = R.drawable.red1
            ),
            MafiaAccount(
                id = 8,
                nickname = "GreenSnake",
                xp = 550,
                telegramLink = "t.me/GreenSnake",
                pictureId = R.drawable.black4
            ),
            MafiaAccount(
                id = 9,
                nickname = "WhiteWolf",
                xp = 700,
                telegramLink = "t.me/WhiteWolf",
                pictureId = R.drawable.black4
            ),
            MafiaAccount(
                id = 10,
                nickname = "GoldenEagle",
                xp = 850,
                telegramLink = "t.me/GoldenEagle",
                pictureId = R.drawable.black4
            ),
            MafiaAccount(
                id = 11,
                nickname = "SilverFox",
                xp = 650,
                telegramLink = "t.me/SilverFox",
                pictureId = R.drawable.red3
            ),
            MafiaAccount(
                id = 12,
                nickname = "ElectricSquid",
                xp = 450,
                telegramLink = "t.me/ElectricSquid",
                pictureId = R.drawable.black4
            ),
            MafiaAccount(
                id = 13,
                nickname = "PoisonIvy",
                xp = 550,
                telegramLink = "t.me/PoisonIvy",
                pictureId = R.drawable.black2
            ),
            MafiaAccount(
                id = 14,
                nickname = "SapphireKnight",
                xp = 800,
                telegramLink = "t.me/SapphireKnight",
                pictureId = R.drawable.black5
            ),
            MafiaAccount(
                id = 15,
                nickname = "AmberLion",
                xp = 750,
                telegramLink = "t.me/AmberLion",
                pictureId = R.drawable.black4
            ),
            MafiaAccount(
                id = 16,
                nickname = "PlatinumPhoenix",
                xp = 900,
                telegramLink = "t.me/PlatinumPhoenix",
                pictureId = R.drawable.black1
            ),
            MafiaAccount(
                id = 17,
                nickname = "RubyRaven",
                xp = 700,
                telegramLink = "t.me/RubyRaven",
                pictureId = R.drawable.black4
            ),
            MafiaAccount(
                id = 18,
                nickname = "EmeraldTiger",
                xp = 600,
                telegramLink = "t.me/EmeraldTiger",
                pictureId = R.drawable.black3
            ),
            MafiaAccount(
                id = 19,
                nickname = "ObsidianPanther",
                xp = 850,
                telegramLink = "t.me/ObsidianPanther",
                pictureId = R.drawable.black2
            ),
            MafiaAccount(
                id = 20,
                nickname = "DiamondDragon",
                xp = 500,
                telegramLink = "t.me/DiamondDragon",
                pictureId = R.drawable.black4
            )
        )

    }
}
