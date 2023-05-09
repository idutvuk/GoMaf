package com.idutvuk.go_maf.model.gamedata

import android.util.Log

class Player(
    val number: Int,
    val role: Role = Role.CIV,
    val nickname: String = "Debugger",
    var isEnabled: Boolean = true,

    /**
     * is player voted
     */
    var voted: Boolean = false,

    /**
     * set of players that voted in current player
     *
     * Null - Player was not voted
     * Empty set - Player was voted with 0 popularity
     */
    var votedPlayers: MutableSet<Int>? = null
//    val isGhost: Boolean = false //TODO: add ghost logic
) {
    var alive = true
        set(value) {
            assert(value != field)
            if (value) { //if want to revive
                Log.i("GameLog","Player $number revived")
            } else { //if want to make player dead
                Log.i("GameLog","Player $number killed")
            }
            isEnabled = value
            field = value
        }

    var fouls = 0
    internal val strNum = String.format("%02d", number + 1)
    internal val emoji = when (role) {
        Role.CIV -> "🙂"
        Role.SHR -> "🥸"
        Role.MAF -> "🔫"
        Role.DON -> "💍"
    }

    override fun toString(): String {
        return strNum + emoji + role +(if (alive) "✅alive" else "💀 dead") +
                ", fouls: $fouls. Aka $nickname"
    }
}