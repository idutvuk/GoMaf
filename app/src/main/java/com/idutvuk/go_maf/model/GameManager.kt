package com.idutvuk.go_maf.model


import android.util.Log
import com.idutvuk.go_maf.model.gamedata.CmdCommitType
import com.idutvuk.go_maf.model.gamedata.MafiaGameState
import com.idutvuk.go_maf.model.gamedata.MainBtnState
import com.idutvuk.go_maf.model.gamedata.Player
import com.idutvuk.go_maf.model.gamedata.Role
import com.idutvuk.go_maf.model.gamedata.StateSnapshot


class GameManager (numPlayers: Int, roles: Array<Role>? = null) {

    val roles = roles ?: generateRoles(numPlayers)
    val stateHistory = arrayListOf(
        MafiaGameState(
            numPlayers,
            players = Array(numPlayers, init = { Player(it, role = roles!![it]) })
        )
    )
    var currentHistoryIndex = 0

    //TODO: Make ghost games possible
    fun commit(cmdCommitType: CmdCommitType): MafiaGameState {
        var gameState = stateHistory[currentHistoryIndex].copy()

        gameState.snackbarMessage = null

        gameState = cmdCommitType.cmdCommitState.changeGameState(gameState)
        Log.d("GameLog","commited")
//        if (cmdCommitType == CmdCommitType.PRESS_MAIN_BTN || //if not SKIP_DAY or SKIP_NIGHT
//            cmdCommitType == CmdCommitType.PRESS_PLAYER_NUMBER
//        )
            gameState.nextMainBtnState()

        gameState.snapshotHistory.add(StateSnapshot(
            heading = gameActionFormatter(gameState.mainBtnState.overwriteText ?: gameState.mainBtnState.description, gameState),
            description = gameState.toString(),
            importance = gameState.mainBtnState.importance
        ))

        stateHistory.add(gameState)
        currentHistoryIndex++
        gameState.canUndo = true
        return gameState
    }

    fun undo(): MafiaGameState {
        stateHistory.removeLastOrNull()
        return stateHistory[--currentHistoryIndex]
    }

    private fun gameActionFormatter(string: String, state: MafiaGameState): String {
        var s = string
        when(state.mainBtnState) {
            MainBtnState.START_DAY -> {
                // currentPhaseNumber уже инкрементирован, день имеет четный индекс (1, 3, 5...)
                val phaseNumber = (state.currentPhaseNumber + 1) / 2
                s += " $phaseNumber"
            }
            MainBtnState.START_NIGHT -> {
                // currentPhaseNumber уже инкрементирован, ночь имеет нечетный индекс (0, 2, 4...)
                val phaseNumber = state.currentPhaseNumber / 2 + 1
                s += " $phaseNumber"
            }

            MainBtnState.START_SPEECH -> s = s.replace("#", (state.cursor + 1).toString())

            MainBtnState.MAFIA_KILL -> s =
                if (state.mafiaMissStreak == 0) s + " ${(state.cursor + 1)}"
                else "Misfire ( ${state.mafiaMissStreak}/3)"

            MainBtnState.CHECK_DON -> {}//TODO
            MainBtnState.CHECK_SHR -> {}//TODO

            MainBtnState.BEST_MOVE -> {}//TODO
            else -> {}
        }
        return s
    }
}