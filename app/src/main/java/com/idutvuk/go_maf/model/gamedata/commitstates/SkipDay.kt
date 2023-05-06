package com.idutvuk.go_maf.model.gamedata.commitstates

import com.idutvuk.go_maf.model.gamedata.GameTime
import com.idutvuk.go_maf.model.gamedata.MafiaGameState
import com.idutvuk.go_maf.model.gamedata.PlayerSelectionMode
import com.idutvuk.go_maf.ui.game.MainButtonActionState

class SkipDay: CmdCommitState{
    override fun changeGameState(gameState: MafiaGameState): MafiaGameState {
        with(gameState) {
            time = GameTime.NIGHT
            clearVoteList()
            isTimerActive = false

            currentPhaseNumber++
            descriptionText = currentPhaseNumber.toString()

            if (currentPhaseNumber == 1) {
                mainButtonActionState = MainButtonActionState.START_MAFIA_SPEECH
            } else {
                selectionMode = PlayerSelectionMode.SINGLE
                mainButtonActionState = MainButtonActionState.KILL
            }

        }
        return gameState
    }

}