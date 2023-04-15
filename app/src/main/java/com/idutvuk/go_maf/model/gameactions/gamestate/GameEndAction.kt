package com.idutvuk.go_maf.model.gameactions.gamestate

import android.util.Log
import com.idutvuk.go_maf.model.CmdManager
import com.idutvuk.go_maf.model.Game

class GameEndAction : ChangeGameStateAction() {
    override fun execute(): Int {
        Game.voteList.clear()
        Game.gameActive = false
        //TODO: Game end logic - export data & other things
        Log.i("GameLog", "Game ended")
        return 0
    }

    override fun undo() {
        Log.e("GameLog", "Attempt to undo game end aborted (by crashing the app)")
        throw Exception()
    }

    override fun toString(): String {
        return "🏁 game finished"
    }

    override val importance = 0
}