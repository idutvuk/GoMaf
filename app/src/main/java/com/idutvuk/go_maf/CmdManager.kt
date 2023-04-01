import android.util.Log
import android.view.View
import com.idutvuk.go_maf.Game
import com.idutvuk.go_maf.GameFragment
import com.idutvuk.go_maf.SmartTV

// Define a base interface for all game actions.
interface GameAction {
    fun execute(): Int
    fun undo()

}

class CheckShrAction(private val playerId: Int) : GameAction {
    override fun execute(): Int {
        TODO("Not yet implemented")
    }

    override fun undo() {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "checkSHR"
    }

}

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
        SmartTV.foulTV(id)

        // Check if the player should be muted or killed.
        when (Game.players[id].fouls) {
            3 -> {
                Game.mute(id)
                wasMuted = true
            }

            4 -> {
                KillAction(id).execute()
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
            KillAction(id).undo()
        } else if (wasMuted) {
            Log.i("GameLog", "Player #${Game.players[id].strNum} unmuted.")
            //TODO unmute logic
        }
    }
    override fun toString(): String {
        return "fouled ${Game.players[id].strNum}"
    }
}

object CmdManager {
    lateinit var gameFragment: GameFragment
    val history = mutableListOf<GameAction>()
    var currentIndex = 0


    fun commit(command: GameAction): Int {
        val commandCode = command.execute()
        if (history.size != currentIndex) {
            repeat(history.size - currentIndex) {
                history.removeLastOrNull()
            }
            gameFragment.redoButtonState(false)
        }
        history.add(command)
        currentIndex++
        gameFragment.undoButtonState(true)
        return commandCode
    }

    fun undo() {
        if (currentIndex==0) {
            Log.e("GameLog", "Attempt to redo null aborted")
            return
        }
        history[currentIndex-1].undo()
        currentIndex--
        gameFragment.redoButtonState(true)
        Log.d("GameLog","Undo completed. size: ${history.size}, index:$currentIndex")
        if (currentIndex<=0) {
            gameFragment.undoButtonState(false)
        }
    }

    fun redo() {
        if (currentIndex >= history.size) {
            Log.e("GameLog", "Attempt to redo null aborted")
            return
        }
        history[currentIndex].execute()
        Log.i("GameLog", "Action redone")
        currentIndex++
        gameFragment.undoButtonState(true)
        if (currentIndex >= history.size) {
            gameFragment.redoButtonState(false)
        }

    }
}