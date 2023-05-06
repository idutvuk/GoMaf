package com.idutvuk.go_maf.model


import android.util.Log
import com.idutvuk.go_maf.model.gamedata.Game
import com.idutvuk.go_maf.model.gamedata.GameTime
import com.idutvuk.go_maf.model.gamedata.MafiaGameState
import com.idutvuk.go_maf.model.gamedata.PlayerSelectionMode
import com.idutvuk.go_maf.model.gamedata.Role
import com.idutvuk.go_maf.ui.game.MainButtonActionState
import java.lang.Error


object CmdManager {
    val stateHistory = arrayListOf(MafiaGameState())
    var currentHistoryIndex = 0
    //TODO: Make ghost games possible
    fun commit(cmdCommitType: CmdCommitType) : MafiaGameState {
        var gameState = stateHistory.last()
        gameState = cmdCommitType.cmdCommitState.changeGameState(gameState)
        stateHistory.add(gameState)
        currentHistoryIndex++
        return gameState
    }
}