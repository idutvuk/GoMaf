package com.idutvuk.go_maf.model.gameactions.gamestate

import android.util.Log
import com.idutvuk.go_maf.model.CmdManager
import com.idutvuk.go_maf.model.Game
import com.idutvuk.go_maf.model.Player
import com.idutvuk.go_maf.model.gameactions.GameAction
import com.idutvuk.go_maf.model.generateRoles
import com.idutvuk.go_maf.model.nicknames

class GameStartAction : GameAction {
    override fun execute(): Int {
        val roles = generateRoles()
        nicknames.shuffle()
        Game.players = Array(Game.numPlayers) { i ->
            Player(
                number = i,
                role = roles[i],
                nickname = nicknames[i]
            )
        }
        Game.gameActive = true
        Log.i("GameLog", "Game started")
        return 0
    }

    override fun undo() {
        Log.e("GameLog", "Attempt to undo game start aborted (by crashing the app)")
        throw Exception()
    }

    override fun toString(): String {
        return "🚗 game started"
    }

    override val importance = 0
}