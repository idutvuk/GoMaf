package com.idutvuk.go_maf.model.gamedata.commitstates

import com.idutvuk.go_maf.model.gamedata.MafiaGameState

interface CmdCommitState {//TODO: rename
    fun changeGameState(gameState: MafiaGameState): MafiaGameState
}