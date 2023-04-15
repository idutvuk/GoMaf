package com.idutvuk.go_maf.model

import android.util.Log

/**
 * Nicknames generator for _debug_ purpose
 */
val nicknames = arrayOf(
    "Jake Green",
    "Zach",
    "Avi",
    "Doroti Macha",
    "Sorter",
    "Paul",
    "Lord John",
    "Lily Walker",
    "Slim Higgins",
    "Billy",
    "Rachel",
    "Joe",
    "Fred"
)

fun generateRoles() {
    Game.roles = Array(Game.numPlayers) { "CIV" }
    if (Game.numPlayers == 6) {
        Game.roles[0] = "MAF"
    } else {
        Game.roles[0] = "SHR"
        Game.roles[1] = "DON"
        val numMaf = when (Game.numPlayers) {
            in 7..8 -> 2
            in 9..10 -> 3
            in 11..12 -> 4
            else -> 0
        }
        for (i in 2..numMaf) {
            Game.roles[i] = "MAF"
        }
    }
    Game.roles.shuffle()
    Log.i("GameLog", "Roles created: ${Game.roles.joinToString(",")}")
}