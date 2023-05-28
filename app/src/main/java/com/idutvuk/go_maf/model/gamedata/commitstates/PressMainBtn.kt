package com.idutvuk.go_maf.model.gamedata.commitstates

import android.util.Log
import com.idutvuk.go_maf.model.gamedata.GameTime
import com.idutvuk.go_maf.model.gamedata.MafiaGameState
import com.idutvuk.go_maf.model.gamedata.PlayerSelectionMode
import com.idutvuk.go_maf.model.gamedata.MainBtnState.*
import java.lang.RuntimeException


class PressMainBtn:CmdCommitState {
    /**
     * тут не должно быть никакой логики по изменению Main button
     */
    override fun changeGameState(gameState: MafiaGameState): MafiaGameState {
        with(gameState) {
            when (mainBtnState) {
                START_NIGHT -> {
                    clearVoteList()
                    speakQueue = null
                    currentPhaseNumber++
                    secondaryMessage = "phase: $currentPhaseNumber"
                    time = GameTime.NIGHT
                }

                START_MAFIA_SPEECH -> {
                    isTimerActive = true
                }


                MAFIA_KILL -> {
                    if (selectedPlayers.isNotEmpty()) {
                        mafiaKill()
                    }
                }

                CHECK_DON -> {
                    if (currentPhaseNumber != 0) {
                        if (selectedPlayers.isNotEmpty()) {
                            mainBtnState = CHECK_SHR
                            primaryMessage = if (checkDon()) "shr" else "not shr"
                        }
                    }
                }

                CHECK_SHR -> {

                    if (currentPhaseNumber != 0) {
                        /**
                         * can we do best move today?
                         */
                        val nextPhase = if (
                            livingPlayersCount() + 2 >= numPlayers
                            && currentPhaseNumber == 2
                        )
                            BEST_MOVE else START_DAY


                        if (selectedPlayers.isNotEmpty()) {
                            primaryMessage = if (checkShr()) "red" else "black"
                        }
                    }
                }


                BEST_MOVE -> {
                    //TODO: Add 20-sec timer
                }

                START_GAME -> {
                    cursor = 0
                }


                NEXT -> {
                    isTimerActive = false
                }

                WAITING_FOR_CLICK -> {
                    /**
                     * When cancel button pressed instead of choosing the number
                     * Use for:
                     * CHECK_DON
                     * CHECK_SHR
                     * ADD_TO_VOTE
                     * MAFIA_KILL
                     * TODO: FOUL (?)
                     */
                    when(prevMainBtnState) {
                        MAFIA_KILL -> {
                            delayedBtnState = CHECK_DON
                            failedMafiaKill()
                        }
                        else -> {}
                    }
                }

                DEBUG -> {
                    Log.e("GameLog", "Bug! Activated debug button. Switching to START_GAME...")
                    //TODO: fix reaching debug
                }


                START_DAY -> {
                    isVoteCancelled = false
                    currentPhaseNumber++
                    secondaryMessage = currentPhaseNumber.toString()
                    time = GameTime.DAY
                    firstSpokedPlayer = nextAlivePlayer(firstSpokedPlayer)
                    cursor = firstSpokedPlayer
                }


                START_SPEECH -> {
                    selectionMode = PlayerSelectionMode.SINGLE //so you can select player before the vote
                    isTimerActive = true
                }

                ADD_TO_VOTE -> {
                    if (selectedPlayers.size == 1) { //if player already selected
                        addToVoteList()
                    }
                }

                END_SPEECH -> {
                    isTimerActive = false
                    if (speakQueue == null) {
                        cursor = nextAlivePlayer(cursor)
                    } else {
                        assert(!speakQueue.isNullOrEmpty()) //its not empty
                        voteKill(cursor)
                        cursor = speakQueue!!.last()
                        speakQueue!!.removeLast()
                    }
                }

                START_VOTE -> {
                    when(voteListCopy.size) { //TODO: I deleted a lot of main button changes so I guess I broke everything
                        0 -> {
                            snackbarMessage = "Vote skipped (nobody was elected)"
                        }
                        1 -> {
                            if (currentPhaseNumber == 2) {//if today is first day
                                snackbarMessage = "Vote skipped (only one player was elected at the first day)"
                            } else {
                                cursor = voteListCopy.first()
                                delayedBtnState= START_NIGHT
                                speakQueue = arrayListOf(cursor)
                                voteKill(cursor)
                            }
                        }
                        else -> {}
                    }
                }

                KILL_IN_VOTE -> for (preyIndex in selectedPlayers) {
//                    voteKill(preyIndex)
                    if (speakQueue.isNullOrEmpty()) speakQueue = arrayListOf(preyIndex)
                    else speakQueue!!.add(preyIndex)
                    cursor = selectedPlayers.elementAt(0)
                }

                END_GAME -> {}

                CRASH -> {
                    throw RuntimeException("Crash state should not be accessible")
                }
            }
            clearSelection()
            selectionMode = mainBtnState.requireNumber
        }
        return gameState
    }
}