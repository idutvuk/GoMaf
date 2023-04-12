package com.idutvuk.go_maf.model.gameactions

import android.util.Log
import com.idutvuk.go_maf.model.Game
import com.idutvuk.go_maf.model.gameactions.kill.BanAction
import com.idutvuk.go_maf.model.gameactions.kill.KillAction
import com.idutvuk.go_maf.model.gameactions.kill.KillByMafiaAction

class FoulAction(private val id: Int) : GameAction {
    private var wasMuted = false
    private var wasKilled = false

    override fun execute(): Int {
        // Increase fouls and log the action.
        Game.players[id].fouls++
        Log.i(
            "GameLog",
            "Player #${Game.players[id].number} fouled. Total fouls: ${Game.players[id].fouls}"
        )

        // Check if the player should be muted or killed.
        when (Game.players[id].fouls) {
            3 -> {
                Game.mute(id)
                wasMuted = true
            }

            4 -> {
                BanAction(id).execute()
                wasKilled = true
            }

            else -> {}
        }
        return Game.players[id].fouls
    }

    override fun undo() {
        // Decrease fouls and log the action.
        Game.players[id].fouls--
        Log.i(
            "GameLog",
            "Undo foul on player #${Game.players[id].number}. Total fouls: ${Game.players[id].fouls}"
        )

        if (wasKilled) {
            BanAction(id).undo()
        } else if (wasMuted) {
            Log.i("GameLog", "Player #${Game.players[id].strNum} unmuted.")
            //TODO unmute logic
        }
    }
    override fun toString(): String {
        return (if (Game.players[id].fouls==4) "❗fouled" else "‼️banned") + Game.players[id].strNum
    }

    override val importance = 5
}
