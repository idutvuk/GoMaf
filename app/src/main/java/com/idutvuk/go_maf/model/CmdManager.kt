package com.idutvuk.go_maf.model


import com.idutvuk.go_maf.model.gamedata.Game
import com.idutvuk.go_maf.model.gamedata.MafiaGameState
import com.idutvuk.go_maf.ui.game.MainBtnState


object CmdManager {
    val stateHistory = arrayListOf(MafiaGameState(Game.numPlayers))
    var currentHistoryIndex = 0
    //TODO: Make ghost games possible
    fun commit(cmdCommitType: CmdCommitType) : MafiaGameState {
        var gameState = stateHistory.last()

        gameState.snackbarMessage = null

        gameState = cmdCommitType.cmdCommitState.changeGameState(gameState)

        gameState.nextMainBtnState()

        stateHistory.add(gameState)
        currentHistoryIndex++

        return gameState
    }
}