package com.idutvuk.go_maf.model.gamedata

import android.util.Log
import java.lang.Error

data class Player(
    val number: Int,
    val role: Role = Role.CIV,
    val nickname: String = "Debugger",
    var isEnabled: Boolean = true,
    var canSpeak: Boolean = true
) {
    var alive = true
        set(value) {
            if (value == field)
                throw Error("Player already ${if(value) "alive" else "dead"}")

            assert(value != field) //todo remove unnecessary

            if (value) { //if want to revive
                Log.i("GameLog","Player $number revived")
            } else { //if want to make player dead
                Log.i("GameLog","Player $number killed")
            }
            canSpeak = canSpeak && value
            isEnabled = value
            field = value
        }

    var fouls = 0
    internal val strNum = String.format("%02d", number + 1)


    override fun toString(): String {
        return strNum + role.emoji + role +(if (alive) "✅alive" else "💀 dead") +
                ", fouls: $fouls. Aka $nickname"
    }

    fun mute() {
        canSpeak = false
    }

    fun unmute() {
        canSpeak = true
    }


}