package com.idutvuk.go_maf.model.gamedata.commitstates

import android.util.Log
import com.idutvuk.go_maf.model.gamedata.Game
import com.idutvuk.go_maf.model.gamedata.GameTime
import com.idutvuk.go_maf.model.gamedata.MafiaGameState
import com.idutvuk.go_maf.model.gamedata.PlayerSelectionMode
import com.idutvuk.go_maf.ui.game.MainBtnState
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
                    currentPhaseNumber++
                    descriptionText = "phase: $currentPhaseNumber"
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
                            headingText = if (checkDon()) "shr" else "not shr"
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
                            headingText = if (checkShr()) "red" else "black"
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
                    descriptionText = currentPhaseNumber.toString()
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
                    if (nextAlivePlayer(cursor) != closestAlivePlayer(firstSpokedPlayer))
                        cursor = nextAlivePlayer(cursor)
                    else
                        Log.i("GameLog", "(CmdM) all players spoke")

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
                                cursor = voteListCopy.first().number
                                delayedBtnState= START_NIGHT
                                voteKill(cursor)
                            }
                        }
                        else -> {
                            mainButtonOverwriteString = "vote for #${voteListCopy.first()}"
                            selectionMode = PlayerSelectionMode.MULTIPLE
                        }
                    }
                }

                VOTE_FOR -> {
                    mainButtonOverwriteString = "vote for #${voteListCopy.first()}"
                    for (i in 0 until voteListCopy.size) {
                        if (i == voteListCopy.size - 1) { //last player remaining
                            for (player in players) {
                                if (player.alive && !player.voted) {
                                    voteListCopy.last().votedPlayers!!.add(player)
                                }
                            }
                        } else if (voteListCopy.elementAt(i).votedPlayers == null) { //player was not voted
                            voteListCopy.elementAt(i).votedPlayers = mutableSetOf()
                            for (voter in selectedPlayersCopy) { //TODO: add selection
                                voteListCopy.elementAt(i).votedPlayers!!.add(players[voter])
                                players[voter].voted = true
                            }
                            break
                        }
                    }  //all players voted
                    var maxVotes = 0
                    for (candidate in voteListCopy) {
                        val popularity = candidate.votedPlayers!!.size
                        leaderVoteList = mutableSetOf(candidate)
                        if (popularity > maxVotes) {
                            maxVotes = popularity
                            leaderVoteList = mutableSetOf(candidate)
                        } else if(popularity == maxVotes) {
                            leaderVoteList.add(candidate)
                        }
                    }
                    mainBtnState = AUTOCATASTROPHE
                    if (leaderVoteList.size == 1) {}
                        voteKill(leaderVoteList.first().number)
                }

                KILL_IN_VOTE -> {
                    mainBtnState = START_NIGHT
                    voteKill(cursor)
                    Log.i("GameLog", "Player $cursor was killed in the vote")
                }

                AUTOCATASTROPHE -> TODO()

                FINAL_VOTE -> TODO()


                END_GAME -> {}

                CRASH -> {
                    throw RuntimeException("Crash state should not be accessible")
                }

                START_MAFIA_DEAD_SPEECH -> TODO()
                START_VOTE_DEAD_SPEECH -> TODO()
            }
            clearSelection()
            selectionMode = mainBtnState.requireNumber
        }
        return gameState
    }
}