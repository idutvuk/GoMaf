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

fun generateRoles() : Array<Role> {
    val roles = Array(Game.numPlayers) { Role.CIV }
    if (Game.numPlayers == 6) {
        roles[0] = Role.MAF
    } else {
        roles[0] = Role.SHR
        roles[1] = Role.DON
        val numMaf = when (Game.numPlayers) {
            in 7..8 -> 2
            in 9..10 -> 3
            in 11..12 -> 4
            else -> 0
        }
        for (i in 2..numMaf) {
            roles[i] = Role.MAF
        }
    }
    roles.shuffle()
    Log.i("GameLog", "Roles created: ${roles.joinToString(",")}")
    return roles
}