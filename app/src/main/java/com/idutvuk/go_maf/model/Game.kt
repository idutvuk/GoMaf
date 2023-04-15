package com.idutvuk.go_maf.model

import android.util.Log
import com.google.android.material.button.MaterialButton
import com.idutvuk.go_maf.model.gameactions.gamestate.GameEndAction
import com.idutvuk.go_maf.model.gameactions.gamestate.GameStartAction

/**
 * [Game] is a singleton object that holds game status inside of it like positions, roles.
 * May be replaced by the local database
 */
object Game {
    const val minPlayers = 6
    const val maxPlayers = 12
    var numPlayers = -1
        set(value) {
            if (numPlayers==-1) {
                field = value
                Log.i("GameLog", "Players number is set as $value")
            } else {
                Log.e("GameLog","Attempt to overwrite numPlayers aborted")
            }
        }

    //TODO: Make ghost games possible
    var ghost = false

    lateinit var positions: Array<Int>
    lateinit var roles: Array<String>
    lateinit var players: Array<Player>
    lateinit var buttons: List<MaterialButton>
    var voteList = ArrayList<Int>()

    /**
     * idk why I need this but let it be as is
     */
    var gameActive = false

    /**
     * Prints game state to the [Log]
     */
    fun printState() {
            var msg = "Game status: " + if (gameActive) "active\n" else "not active\n"
            msg += "Current action: ${CmdManager.currentIndex}. Actions:\n"
            for (i in 0 until CmdManager.history.size) {
                msg += (if (CmdManager.currentIndex == i + 1) '>' else ' ') +
                        (if (i < 10) "0$i " else "$i ") +
                        CmdManager.history[i] + '\n'
            }
            msg += "Players:\n"
            for (i in 0 until numPlayers) {
                msg += players[i].toString() + '\n'
            }
            Log.i("GameLog", msg)
    }

    fun mute(id: Int) {
        Log.i("GameLog", "Player #${players[id].strNum} muted.")
        //TODO mute logic
    }
}