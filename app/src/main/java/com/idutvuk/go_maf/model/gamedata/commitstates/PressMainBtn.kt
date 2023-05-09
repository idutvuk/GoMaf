package com.idutvuk.go_maf.model.gamedata.commitstates

import android.util.Log
import com.idutvuk.go_maf.model.gamedata.Game
import com.idutvuk.go_maf.model.gamedata.GameTime
import com.idutvuk.go_maf.model.gamedata.MafiaGameState
import com.idutvuk.go_maf.model.gamedata.PlayerSelectionMode
import com.idutvuk.go_maf.ui.game.MainBtnState.*
import java.lang.RuntimeException


class PressMainBtn:CmdCommitState {
    /**
     * тут не должно быть никакой логики по изменению Main button
     */
    override fun changeGameState(gameState: MafiaGameState): MafiaGameState {
        with(gameState) {
            when (mainBtnState) {
                START_NIGHT -> {
                    speakQueue = null
                    currentPhaseNumber++
                    secondaryMessage = "phase: $currentPhaseNumber"
                    time = GameTime.NIGHT
                }

                START_MAFIA_SPEECH -> {
                    isTimerActive = true
                }


                MAFIA_KILL -> {
                    if (selectedPlayersCopy.isNotEmpty()) {
                        mafiaKill()
                    }
                }

                CHECK_DON -> {
                    if (currentPhaseNumber != 0) {
                        if (selectedPlayersCopy.isNotEmpty()) {
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
                            livingPlayersCount() + 2 >= Game.numPlayers
                            && currentPhaseNumber == 2
                        )
                            BEST_MOVE else START_DAY


                        if (selectedPlayersCopy.isNotEmpty()) {
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
                    when(previousMainButtonActionState) {
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
                    currentPhaseNumber++
                    secondaryMessage = currentPhaseNumber.toString()
                    time = GameTime.DAY
                    firstSpokedPlayer = nextAlivePlayer(firstSpokedPlayer)
                    cursor = firstSpokedPlayer
                }


                START_SPEECH -> {
                    selectionMode = PlayerSelectionMode.SINGLE //so you can select player before the vote
                    isTimerActive = true
                    Log.d("GameLog", "Speech started. Cursor: $cursor")
                }

                ADD_TO_VOTE -> {
                    if (selectedPlayersCopy.size == 1) { //if player already selected
                        Log.d("GameLog", "(CmdM) Added to vote")
                        addToVoteList()
                    }
                }

                END_SPEECH -> {
                    isTimerActive = false
                    if (speakQueue == null) {
                        if (nextAlivePlayer(cursor) != closestAlivePlayer(firstSpokedPlayer))
                            cursor = nextAlivePlayer(cursor)
                        else
                            Log.i("GameLog", "(CmdM) all players spoke")
                    } else {
                        voteKill(cursor)
                        speakQueue!!.remove(0)
                        cursor = speakQueue!!.first()
                    }
                }

                START_VOTE -> {
                    when(voteListCopy.size) { //TODO: I deleted a lot of main button changes so I guess I broke everything
                        0 -> {
                            snackbarMessage = "Vote skipped (nobody was elected)"
                        }
                        1 -> {
                            if (currentPhaseNumber == 2) {//if today is first day
                                snackbarMessage = "Vote skipped (only one was elected at the first day)"
                            } else {
                                cursor = voteListCopy.first()
                                delayedBtnState= START_NIGHT
                                voteKill(cursor)
                            }
                        }
                        else -> {
                            cursor = voteListCopy.first()
                            mainButtonOverwriteString = "vote for #${voteListCopy.first()}"
                            selectionMode = PlayerSelectionMode.MULTIPLE
                        }
                    }
                }

                VOTE_FOR -> {
                    voteForActive()
                    if (voteListCopy.indexOf(cursor) != voteListCopy.size - 1)
                        cursor = voteListCopy.elementAt(voteListCopy.indexOf(cursor) + 1) // going to the next value

                }

                KILL_IN_VOTE -> {
                    voteKill(cursor)
                    Log.i("GameLog", "Player $cursor was killed in the vote")
                }

                FINAL_VOTE ->  {
                    finalVote()
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