package com.idutvuk.go_maf.model


import com.idutvuk.go_maf.model.gamedata.CmdCommitType
import com.idutvuk.go_maf.model.gamedata.MafiaGameState
import com.idutvuk.go_maf.model.gamedata.MainBtnState
import com.idutvuk.go_maf.model.gamedata.Player
import com.idutvuk.go_maf.model.gamedata.StateSnapshot


class GameManager (numPlayers: Int) {

    val roles = generateRoles(numPlayers)
    val stateHistory = arrayListOf(
        MafiaGameState(
            numPlayers,
            players = Array(numPlayers, init = { Player(it, role = roles[it]) })
        )
    )
    var currentHistoryIndex = 0

    //TODO: Make ghost games possible
    fun commit(cmdCommitType: CmdCommitType): MafiaGameState {
        var gameState = stateHistory[currentHistoryIndex].copy()

        gameState.snackbarMessage = null

        gameState = cmdCommitType.cmdCommitState.changeGameState(gameState)

        if (cmdCommitType == CmdCommitType.PRESS_MAIN_BTN || //if not SKIP_DAY or SKIP_NIGHT
            cmdCommitType == CmdCommitType.PRESS_PLAYER_NUMBER
        ) gameState.nextMainBtnState()

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
            MainBtnState.START_DAY -> s += " ${(state.currentPhaseNumber)/2 + 1}"
            MainBtnState.START_NIGHT -> s += " ${(state.currentPhaseNumber)/2}"

            MainBtnState.START_SPEECH -> s = s.replace("#", state.cursor.toString())

            MainBtnState.MAFIA_KILL -> s =
                if (state.mafiaMissStreak == 0) s + " ${(state.cursor)}"
                else "Misfire ( ${state.mafiaMissStreak}/3)"

            MainBtnState.CHECK_DON -> {}//TODO
            MainBtnState.CHECK_SHR -> {}//TODO

            MainBtnState.BEST_MOVE -> {}//TODO
            else -> {}
        }
        return s
    }
}