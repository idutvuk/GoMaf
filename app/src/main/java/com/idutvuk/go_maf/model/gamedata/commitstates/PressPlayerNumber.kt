package com.idutvuk.go_maf.model.gamedata.commitstates

import android.util.Log
import com.idutvuk.go_maf.model.gamedata.MafiaGameState
import com.idutvuk.go_maf.ui.game.MainBtnState
import java.lang.Error

class PressPlayerNumber:CmdCommitState {
    override fun changeGameState(gameState: MafiaGameState): MafiaGameState {
        with(gameState) {

            /**
             * Use for:
             * CHECK_DON
             * CHECK_SHR
             * ADD_TO_VOTE
             * MAFIA_KILL
             * TODO: FOUL (?)
             */
            when (previousMainButtonActionState) {
                MainBtnState.ADD_TO_VOTE -> {
                    addToVoteList()
                }

                MainBtnState.MAFIA_KILL -> {
                    delayedBtnState = MainBtnState.CHECK_DON
                    mafiaMissStreak = 0
                    mafiaKill()
                }

                MainBtnState.CHECK_DON -> {
                    snackbarMessage =  if (checkDon()) "shr" else "not shr"
                }

                MainBtnState.CHECK_SHR -> {
                    snackbarMessage = if (checkShr()) "RED" else "BLACK"
                }
                else -> throw Error(
                    "VM: clicked on a player button when number is requested\n" +
                            "but from undescrbed state"
                )
                //TODO: add to vote on click
            }
            clearSelection()
//            mainBtnState = delayedBtnState
        }
        return gameState
    }
}