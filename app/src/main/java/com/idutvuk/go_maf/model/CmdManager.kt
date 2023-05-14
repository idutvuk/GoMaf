package com.idutvuk.go_maf.model


import com.idutvuk.go_maf.model.gamedata.Game
import com.idutvuk.go_maf.model.gamedata.MafiaGameState
import com.idutvuk.go_maf.model.gamedata.Player
import com.idutvuk.go_maf.ui.game.MainBtnState


object CmdManager {
    val roles = generateRoles(Game.numPlayers)
    val stateHistory = arrayListOf(MafiaGameState(
        Game.numPlayers,
        players = Array(Game.numPlayers,
            init = {
                Player(
                    it,
                    role = roles[it])
            }
        )
    ))
    var currentHistoryIndex = 0
    //TODO: Make ghost games possible
    fun commit(cmdCommitType: CmdCommitType) : MafiaGameState {
        var gameState = stateHistory.last()

        gameState.snackbarMessage = null

        gameState = cmdCommitType.cmdCommitState.changeGameState(gameState)

        if (cmdCommitType == CmdCommitType.PRESS_MAIN_BTN ||
            cmdCommitType == CmdCommitType.PRESS_PLAYER_NUMBER
            ) gameState.nextMainBtnState()

        stateHistory.add(gameState)
        currentHistoryIndex++

        return gameState
    }
}