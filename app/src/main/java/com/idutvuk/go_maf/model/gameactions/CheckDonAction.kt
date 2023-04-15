package com.idutvuk.go_maf.model.gameactions

import android.util.Log
import com.idutvuk.go_maf.model.Game
import com.idutvuk.go_maf.model.Role

class CheckDonAction(private val playerId: Int) : GameAction {
    private var isShr: Boolean = false //will be overridden later

    override fun execute(): Int {
        // Determine whether the player is a SHR and log the action.
        isShr = Game.players[playerId].role == Role.SHR
        Log.i(
            "GameLog",
            "Player #${Game.players[playerId].number} checked by DON. Result:" +
                    if (isShr) "SHR" else "not SHR"
        )

        // Save the result of the check.


        return if (isShr) 1 else 0
    }
    override fun undo() {
        Log.i(
            "GameLog",
            "Undo DON check on player #${Game.players[playerId].number}"
        )
    }

    override fun toString(): String {
        return "💍checked by DON ${Game.players[playerId].strNum} Result:" +
                if (isShr) "👌SHR" else "👍not SHR"
    }
    override val importance = 5
}