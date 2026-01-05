package com.idutvuk.go_maf.model.gamedata

import android.util.Log
import java.lang.Error

data class Player(
    val number: Int,
    val role: Role = Role.CIV,
    var isEnabled: Boolean = true,
    var canSpeak: Boolean = true
) {
    var alive = true
        set(value) {
            if (value == field)
                Log.e("GameLog","Player ${number} already ${if(value) "alive" else "dead"}")

//            assert(value != field)

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
    internal val strNum = String.format("%02d", number)


    override fun toString(): String {
        return '\n' + strNum + role.emoji +(if (alive) "✅" else "💀") +
                ", fouls: $fouls"
    }

    fun mute() {
        canSpeak = false
    }

    fun unmute() {
        canSpeak = true
    }


}