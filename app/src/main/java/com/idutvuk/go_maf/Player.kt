package com.idutvuk.go_maf

import android.util.Log

class Player(
    val number: Int,
    val role: String = "CIV",
    val nickname: String = "Debugger",
    val isGhost: Boolean = false
) {
    var alive = true
    var fouls = 0
    var isAddedForVoting = false
    internal val strNum = String.format("%02d", number + 1)
    internal val emoji = when (role) {
        "CIV" -> "🙂"
        "SHR" -> "🥸"
        "MAF" -> "🔫"
        "DON" -> "💍"
        else -> {
            "🤷‍♂️IDK "
        }
    }


    fun foul() {
        fouls++
        Log.i("GameLog", "Player #$strNum fouled. Total fouls: $fouls")
        when (fouls) {
            3 -> Game.mute(number)
            4 -> Game.kill(number)
            else -> {}

        }
    }
    override fun toString(): String {
        return strNum + emoji + role +(if (alive) "✅alive" else "💀 dead") +
                ", fouls: $fouls. Aka $nickname"
    }
}