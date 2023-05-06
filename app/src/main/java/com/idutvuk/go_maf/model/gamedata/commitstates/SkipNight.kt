package com.idutvuk.go_maf.model.gamedata.commitstates

import com.idutvuk.go_maf.model.gamedata.GameTime
import com.idutvuk.go_maf.model.gamedata.MafiaGameState
import com.idutvuk.go_maf.ui.game.MainButtonActionState

class SkipNight:CmdCommitState {
    override fun changeGameState(gameState: MafiaGameState): MafiaGameState {
        gameState.skipNight()
        return gameState
    }
}