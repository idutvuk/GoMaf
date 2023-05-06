package com.idutvuk.go_maf.model.gamedata.commitstates

import com.idutvuk.go_maf.model.gamedata.GameTime
import com.idutvuk.go_maf.model.gamedata.MafiaGameState
import com.idutvuk.go_maf.ui.game.MainButtonActionState

class SkipNight:CmdCommitState {
    override fun changeGameState(gameState: MafiaGameState): MafiaGameState {
        with(gameState) {
            if (!isMafiaMissedToday) mafiaMissStreak++
            if (mafiaMissStreak >= 3) { //if 3 nights passed
                mainButtonActionState = MainButtonActionState.END_GAME
            } else {
                isTimerActive = false
                currentPhaseNumber++
                descriptionText = currentPhaseNumber.toString()
                time = GameTime.DAY
                firstSpokedPlayer = nextAlivePlayer(firstSpokedPlayer)
                cursor = firstSpokedPlayer
                mainButtonActionState = MainButtonActionState.START_SPEECH
            }
        }
        return gameState
    }
}