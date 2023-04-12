package com.idutvuk.go_maf.model.gameactions.kill

import android.util.Log
import com.idutvuk.go_maf.model.Game
import com.idutvuk.go_maf.model.gameactions.GameAction
import com.idutvuk.go_maf.model.gameactions.gamestate.GameEndAction

abstract class KillAction(private val id: Int) : GameAction {
    override val importance = 2
    override fun execute(): Int {
        if (!Game.players[id].alive) {
            Log.w("GameLog", "Attempt to kill dead person. Aborting")
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
            GameEndAction()
        } else if (redTeamCount <= blackTeamCount) {
            Log.w("GameLog", "Red team is no longer in the majority")
            GameEndAction()
        }
        Game.voteList.clear()
        return 1
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
        return "💀 killed ${Game.players[id].strNum}"
    }

}