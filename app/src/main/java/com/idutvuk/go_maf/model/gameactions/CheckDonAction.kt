package com.idutvuk.go_maf.model.gameactions

import android.util.Log
import com.idutvuk.go_maf.model.Game

class CheckDonAction(private val playerId: Int) : GameAction {
    private var wasShr = false

    override fun execute(): Int {
        // Determine whether the player is a SHR and log the action.
        val isShr = Game.players[playerId].role == "SHR"
        Log.i(
            "GameLog",
            "Player #${Game.players[playerId].number} (${Game.players[playerId].role}) was checked by DON."
        )

        // Save the result of the check.
        wasShr = isShr

        return if (isShr) 1 else 0
    }
    override fun undo() {
        Log.i(
            "GameLog",
            "Undo DON check on player #${Game.players[playerId].number}. Result: ${if (wasShr) "SHR" else "not SHR"}"
        )
    }

    override fun toString(): String {
        return "checkDON"
    }
}