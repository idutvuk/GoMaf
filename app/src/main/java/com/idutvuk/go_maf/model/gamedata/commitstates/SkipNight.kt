package com.idutvuk.go_maf.model.gamedata.commitstates

import com.idutvuk.go_maf.model.gamedata.MafiaGameState

class SkipNight:CmdCommitState {
    override fun changeGameState(gameState: MafiaGameState): MafiaGameState {
        gameState.skipNight()
        return gameState
    }
}