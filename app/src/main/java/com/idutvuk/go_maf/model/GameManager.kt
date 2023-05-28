package com.idutvuk.go_maf.model


import com.idutvuk.go_maf.model.gamedata.CmdCommitType
import com.idutvuk.go_maf.model.gamedata.MafiaGameState
import com.idutvuk.go_maf.model.gamedata.Player


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
        var gameState = stateHistory.last().copy()

        gameState.snackbarMessage = null

        gameState = cmdCommitType.cmdCommitState.changeGameState(gameState)

        if (cmdCommitType == CmdCommitType.PRESS_MAIN_BTN || //if not SKIP_DAY or SKIP_NIGHT
            cmdCommitType == CmdCommitType.PRESS_PLAYER_NUMBER
        ) gameState.nextMainBtnState()

        stateHistory.add(gameState)
        currentHistoryIndex++
        gameState.canUndo = true
        return gameState
    }
}