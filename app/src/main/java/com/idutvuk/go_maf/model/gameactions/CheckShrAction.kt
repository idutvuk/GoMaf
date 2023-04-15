package com.idutvuk.go_maf.model.gameactions

import android.util.Log
import com.idutvuk.go_maf.model.Game
import com.idutvuk.go_maf.model.Role

class CheckShrAction(private val playerId: Int) : GameAction {
    private var isBlack: Boolean = false //will be overridden later

    override fun execute(): Int {
        // Determine whether the player is a SHR and log the action.
        val role = Game.players[playerId].role
        isBlack = (role == Role.DON || role == Role.MAF)
        Log.i(
            "GameLog",
            "Player #${Game.players[playerId].number} (${Game.players[playerId].role}) was checked by SHR."
        )


        return if (isBlack) 1 else 0
    }
    override fun undo() {
        Log.i(
            "GameLog",
            "Undo SHR check on player #${Game.players[playerId].number}"
        )
    }

    override fun toString(): String {
        return "🔍checked by SHR ${Game.players[playerId].strNum} Result:" +
                if (isBlack) "👎black" else "👍not black"
    }
    override val importance = 3
}