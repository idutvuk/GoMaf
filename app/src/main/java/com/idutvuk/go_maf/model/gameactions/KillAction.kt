package com.idutvuk.go_maf.model.gameactions

import android.util.Log
import com.idutvuk.go_maf.model.Game

class KillAction(private val id: Int) : GameAction {
    override fun execute(): Int {
        if (!Game.players[id].alive) {
            Log.w("GameLog", "Attempt to kill dead person. Aboring")
            return 0
        }
        Game.players[id].alive = false
        Game.buttons[id].isEnabled = false
        Log.i("GameLog", "Player #${Game.players[id].strNum} killed")
        var redTeamCount = 0
        var blackTeamCount = 0
        for (i in 0 until Game.numPlayers)
            if (Game.players[i].alive)
                when (Game.players[i].role) {
                    "CIV", "SHR" -> redTeamCount++
                    else -> blackTeamCount++
                }
        if (blackTeamCount == 0) {
            Log.w("GameLog", "All black team members are dead.")
            Game.endGame()
            return 1
        }
        if (redTeamCount <= blackTeamCount) {
            Log.w("GameLog", "Red team is no longer in the majority")
            Game.endGame()
            return 1
        }
        return 1
        //TODO kill logic
        //TODO create an opportunity to kill multiple people
    }

    override fun undo() {
        if (Game.players[id].alive) {
            Log.w("GameLog", "Attempt to revive alive person. Aboring")
        } else {
            Game.players[id].alive = true
            Game.buttons[id].isEnabled = true
            Log.i("GameLog", "Player #${Game.players[id].strNum} revived")
        }
    }
    override fun toString(): String {
        return "killed ${Game.players[id].strNum}"
    }

}