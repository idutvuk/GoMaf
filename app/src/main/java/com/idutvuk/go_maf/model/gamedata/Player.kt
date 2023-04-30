package com.idutvuk.go_maf.model.gamedata

import android.util.Log

class Player(
    val number: Int,
    val role: Role = Role.CIV,
    val nickname: String = "Debugger",
    var isEnabled: Boolean = true,
//    val isGhost: Boolean = false //TODO: add ghost logic
) {
    var alive = true
        set(value) {
            if (value) { //if want to revive
                if (field) { //if already alive
                    Log.e("GameLog","Attempt to revive living player $number")
                } else { //if dead, but player is reviving
                    Log.i("GameLog","Player $number revived")
                    isEnabled = true
                    field = true
                }
            } else { //if want to make player dead
                if (field) { //if already alive
                    Log.i("GameLog","Player $number killed")
                    isEnabled = false
                    field = false
                } else { //if dead, but player is reviving
                    Log.e("GameLog","Attempt to kill player $number who is already dead")
                }
            }
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