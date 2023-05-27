package com.idutvuk.go_maf.model.gamedata.commitstates

import com.idutvuk.go_maf.model.gamedata.GameTime
import com.idutvuk.go_maf.model.gamedata.MafiaGameState
import com.idutvuk.go_maf.model.gamedata.PlayerSelectionMode
import com.idutvuk.go_maf.model.gamedata.MainBtnState

class SkipDay: CmdCommitState{
    override fun changeGameState(gameState: MafiaGameState): MafiaGameState {
        with(gameState) {
            time = GameTime.NIGHT
            clearVoteList()
            isTimerActive = false

            isVoteCancelled = false

            speakQueue = null

            currentPhaseNumber++
            secondaryMessage = currentPhaseNumber.toString()

            if (currentPhaseNumber == 1) {
                mainBtnState = MainBtnState.START_MAFIA_SPEECH
            } else {
                selectionMode = PlayerSelectionMode.SINGLE
                mainBtnState = MainBtnState.MAFIA_KILL
            }

        }
        return gameState
    }

}