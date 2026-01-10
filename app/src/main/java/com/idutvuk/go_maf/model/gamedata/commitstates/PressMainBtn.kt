package com.idutvuk.go_maf.model.gamedata.commitstates

import android.util.Log
import com.idutvuk.go_maf.model.gamedata.GameTime
import com.idutvuk.go_maf.model.gamedata.MafiaGameState
import com.idutvuk.go_maf.model.gamedata.PlayerSelectionMode
import com.idutvuk.go_maf.model.gamedata.MainBtnState.*
import java.lang.RuntimeException


class PressMainBtn:CmdCommitState {
    /**
     * keep no logic for changin MainBtn state here
     */
    override fun changeGameState(gameState: MafiaGameState): MafiaGameState {
        with(gameState) {
            when (mainBtnState) {
                START_NIGHT -> {
                    clearVoteList()
                    speakQueue = null
                    currentPhaseNumber++
                    val phaseNumber = (currentPhaseNumber + 1) / 2
                    secondaryMessage = "phase: $phaseNumber"
                    primaryMessage = "Night $phaseNumber"
                    time = GameTime.NIGHT
                }

                START_MAFIA_SPEECH -> {
                    isTimerActive = true
                    primaryMessage = "Mafia speech"
                }


                MAFIA_KILL -> {
                    if (selectedPlayers.isNotEmpty()) {
                        mafiaKill()
                        primaryMessage = "Mafia kill"
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
//                            delayedBtnState = CHECK_DON
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
                    // currentPhaseNumber теперь четное (2, 4, 6...), день имеет номер (1, 2, 3...)
                    val phaseNumber = currentPhaseNumber / 2
                    secondaryMessage = "phase: $phaseNumber"
                    primaryMessage = "Day $phaseNumber"
                    time = GameTime.DAY
                    firstSpokedPlayer = nextAlivePlayer(firstSpokedPlayer)
                    cursor = firstSpokedPlayer
                }


                START_SPEECH -> {
                    selectionMode = PlayerSelectionMode.SINGLE //so you can select player before the vote
                    // Skip speech if player cannot speak (e.g., has 3 fouls)
                    if (!players[cursor].canSpeak) {
                        // Skip this player's speech immediately
                        primaryMessage = "Player ${cursor + 1} skipped (muted)"
                    } else {
                        isTimerActive = true
                        primaryMessage = "Player ${cursor + 1} speaking"
                    }
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
                        // Skip players who cannot speak
                        while (!players[cursor].canSpeak && cursor != firstSpokedPlayer) {
                            cursor = nextAlivePlayer(cursor)
                        }
                    } else {
//                        voteKill(cursor) its doublekilling somehow
                        cursor = speakQueue!!.last()
                        speakQueue!!.removeAt(speakQueue!!.lastIndex)
                    }
                    primaryMessage = "Player ${cursor + 1} finished"
                }

                START_VOTE -> {
                    primaryMessage = "Vote (${voteList.size} candidates)"
                    when(voteList.size) { //TODO: I deleted a lot of main button changes so I guess I broke everything
                        0 -> {
                            snackbarMessage = "Vote skipped (nobody was elected)"
                        }
                        1 -> {
                            if (currentPhaseNumber == 2) {//if today is first day
                                snackbarMessage = "Vote skipped (only one player was elected at the first day)"
                            } else {
                                cursor = voteList.first()
                                delayedBtnState= START_NIGHT
                                speakQueue = arrayListOf(cursor)
                                voteKill(cursor)
                            }
                        }
                        else -> {}
                    }
                }

                KILL_IN_VOTE -> {
                    primaryMessage = "Vote kill"
                    for (preyIndex in selectedPlayers) {
                        // Kill player BEFORE adding to speech queue
                        voteKill(preyIndex)
                        if (speakQueue.isNullOrEmpty()) speakQueue = arrayListOf(preyIndex)
                        else speakQueue!!.add(preyIndex)
                        cursor = selectedPlayers.elementAt(0)
                    }
                }

                END_GAME -> {prevMainBtnState = END_GAME}

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