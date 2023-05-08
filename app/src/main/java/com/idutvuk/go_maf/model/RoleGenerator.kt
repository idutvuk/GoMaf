package com.idutvuk.go_maf.model

import android.util.Log
import com.idutvuk.go_maf.model.gamedata.Game
import com.idutvuk.go_maf.model.gamedata.Role

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

fun generateRoles(playerCount: Int) : Array<Role> {
    val roles = Array(playerCount) { Role.CIV }
    if (playerCount == 6) {
        roles[0] = Role.MAF
    } else {
        roles[0] = Role.SHR
        roles[1] = Role.DON
        val numMaf = when (playerCount) {
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
    return roles
}