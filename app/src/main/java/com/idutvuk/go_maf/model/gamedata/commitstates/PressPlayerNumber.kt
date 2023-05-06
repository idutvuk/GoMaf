package com.idutvuk.go_maf.model.gamedata.commitstates

import android.util.Log
import com.idutvuk.go_maf.model.gamedata.MafiaGameState
import com.idutvuk.go_maf.model.gamedata.Role
import com.idutvuk.go_maf.ui.game.MainButtonActionState
import java.lang.Error

class PressPlayerNumber:CmdCommitState {
    override fun changeGameState(gameState: MafiaGameState): MafiaGameState {
        with(gameState) {

            /**
             * Use for:
             * CHECK_DON
             * CHECK_SHR
             * ADD_TO_VOTE
             * KILL
             * TODO: FOUL (?)
             */
            when (previousMainButtonActionState) {
                MainButtonActionState.ADD_TO_VOTE -> {
                    Log.d("GameLog", "(CmdM) Added to vote")
                    addToVoteList(selectedPlayers[0])
                }

                MainButtonActionState.KILL -> {
                    mafiaMissStreak = 0
                    kill(selectedPlayers[0])
                    delayedMainButtonActionState =
                        if (gameOver) MainButtonActionState.END_GAME
                        else MainButtonActionState.CHECK_DON
                }

                MainButtonActionState.CHECK_DON -> {
                    headingText = if (players[selectedPlayers[0]].role == Role.SHR) "shr" else "not shr"
                    //TODO: change it to GUI response
                }

                MainButtonActionState.CHECK_SHR -> {
                    headingText = if (players[selectedPlayers[0]].role.isRed) "RED" else "BLACK"
                    //TODO: change it to GUI response
                }
                else -> throw Error(
                    "VM: clicked on a player button when number is requested\n" +
                            "but from undescrbed state"
                )
                //TODO: add to vote on click
            }
            selectedPlayers = ArrayList()
            mainButtonActionState = delayedMainButtonActionState
        }
        return gameState
    }
}