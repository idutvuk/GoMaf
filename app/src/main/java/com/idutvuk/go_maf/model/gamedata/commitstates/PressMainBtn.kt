package com.idutvuk.go_maf.model.gamedata.commitstates

import android.util.Log
import com.idutvuk.go_maf.model.gamedata.Game
import com.idutvuk.go_maf.model.gamedata.GameTime
import com.idutvuk.go_maf.model.gamedata.MafiaGameState
import com.idutvuk.go_maf.model.gamedata.PlayerSelectionMode
import com.idutvuk.go_maf.model.gamedata.Role
import com.idutvuk.go_maf.ui.game.MainButtonActionState
import java.lang.RuntimeException

class PressMainBtn:CmdCommitState {
    override fun changeGameState(gameState: MafiaGameState): MafiaGameState {
        with(gameState) {
            when (mainButtonActionState) {
                MainButtonActionState.START_NIGHT -> {
                    currentPhaseNumber++
                    descriptionText = currentPhaseNumber.toString()
                    time = GameTime.NIGHT

                    if (currentPhaseNumber == 1) {
                        mainButtonActionState = MainButtonActionState.START_MAFIA_SPEECH
                    } else {
                        selectionMode = PlayerSelectionMode.SINGLE
                        mainButtonActionState = MainButtonActionState.KILL
                    }
                }

                MainButtonActionState.START_MAFIA_SPEECH -> {
                    isTimerActive = true
                    delayedMainButtonActionState = MainButtonActionState.CHECK_DON
                    mainButtonActionState = MainButtonActionState.NEXT
                }


                MainButtonActionState.KILL -> {
                    if (selectedPlayers.isNotEmpty()) {
                        mafiaMissStreak = 0
                        kill(selectedPlayers[0])
                        mainButtonActionState = if (gameOver) {
                            MainButtonActionState.END_GAME
                        } else {
                            MainButtonActionState.CHECK_DON
                        }
                    } else {
                        previousMainButtonActionState = MainButtonActionState.KILL
                        mainButtonActionState = MainButtonActionState.WAITING_FOR_CLICK
                    }

                }


                MainButtonActionState.CHECK_DON -> {
                    if (currentPhaseNumber != 0) {
                        if (selectedPlayers.isNotEmpty()) {
                            mainButtonActionState = MainButtonActionState.CHECK_SHR
                            headingText =
                                if (players[selectedPlayers[0]].role == Role.SHR)
                                    "shr" else "not shr"
                        } else {
                            previousMainButtonActionState = MainButtonActionState.CHECK_DON
                            delayedMainButtonActionState = MainButtonActionState.CHECK_SHR
                            mainButtonActionState = MainButtonActionState.WAITING_FOR_CLICK
                        }
                    } else {
                        mainButtonActionState = MainButtonActionState.CHECK_SHR
                    }
                }


                MainButtonActionState.CHECK_SHR -> {

                    if (currentPhaseNumber != 0) {
                        /**
                         * can we do best move today?
                         */
                        val nextPhase = if (
                            livingPlayersCount() + 2 >= Game.numPlayers
                            && currentPhaseNumber == 2
                        )
                            MainButtonActionState.BEST_MOVE else MainButtonActionState.START_DAY


                        if (selectedPlayers.isNotEmpty()) {
                            mainButtonActionState = nextPhase
                            headingText =
                                if (players[selectedPlayers[0]].role.isRed)
                                    "red" else "black"
                        } else {
                            previousMainButtonActionState = MainButtonActionState.CHECK_SHR

                            delayedMainButtonActionState = nextPhase

                            mainButtonActionState = MainButtonActionState.WAITING_FOR_CLICK
                        }
                    } else {
                        mainButtonActionState = MainButtonActionState.START_DAY
                    }
                }


                MainButtonActionState.BEST_MOVE -> {
                    //TODO: Add 20-sec timer

                    mainButtonActionState = MainButtonActionState.START_DAY
                }

                MainButtonActionState.START_GAME -> {
                    cursor = 0
                    gameOver = false
                    mainButtonActionState = MainButtonActionState.START_NIGHT
                }


                MainButtonActionState.NEXT -> {
                    isTimerActive = false
                    mainButtonActionState = delayedMainButtonActionState
                }

                MainButtonActionState.WAITING_FOR_CLICK -> {
                    /**
                     * When cancel button pressed instead of choosing the number
                     * Use for:
                     * CHECK_DON
                     * CHECK_SHR
                     * ADD_TO_VOTE
                     * KILL
                     * TODO: FOUL (?)
                     */
                    when(previousMainButtonActionState) {
                        MainButtonActionState.KILL -> {
                            mafiaMissStreak++
                            isMafiaMissedToday = true
                            descriptionText += " missStreak $mafiaMissStreak"
                            if (mafiaMissStreak >= 3) { //if 3 nights passed
                                delayedMainButtonActionState = MainButtonActionState.END_GAME
                            } else {
                                delayedMainButtonActionState = MainButtonActionState.CHECK_DON
                            }
                        }
                        MainButtonActionState.CHECK_DON -> {}
                        MainButtonActionState.CHECK_SHR -> {}
                        MainButtonActionState.ADD_TO_VOTE -> {}
                        else -> {

                        }
                    }
                    mainButtonActionState = delayedMainButtonActionState
                }

                MainButtonActionState.DEBUG -> {
                    Log.e("GameLog", "Bug! Activated debug button. Switching to START_GAME...")
                    //TODO: fix reaching debug
                    mainButtonActionState = MainButtonActionState.START_GAME
                }


                MainButtonActionState.START_DAY -> {
                    isMafiaMissedToday = false
                    currentPhaseNumber++
                    descriptionText = currentPhaseNumber.toString()
                    time = GameTime.DAY
                    firstSpokedPlayer = nextAlivePlayer(firstSpokedPlayer)
                    cursor = firstSpokedPlayer
                    mainButtonActionState = MainButtonActionState.START_SPEECH
                }


                MainButtonActionState.START_SPEECH -> {


                        selectionMode = PlayerSelectionMode.SINGLE //so you can select player before the vote
                        isTimerActive = true
                        Log.d("GameLog", "Speech started. Cursor: $cursor")
                        mainButtonActionState = MainButtonActionState.ADD_TO_VOTE

                    //todo figure out what is it
//                        //last speech or autocatastrophe
//                        if (leaderVoteList.isNotEmpty()) {
//
//                        } else { //last speech
//                            mainButtonActionState = MainButtonActionState.NEXT
//                        }

                }

                MainButtonActionState.ADD_TO_VOTE -> {
                    if (selectedPlayers.size == 1) { //if player already selected
                        Log.d("GameLog", "(CmdM) Added to vote")
                        addToVoteList(selectedPlayers[0])
                        mainButtonActionState = MainButtonActionState.END_SPEECH
                    } else {
                        previousMainButtonActionState = MainButtonActionState.ADD_TO_VOTE
                        delayedMainButtonActionState = MainButtonActionState.END_SPEECH
                        mainButtonActionState = MainButtonActionState.WAITING_FOR_CLICK
                    }
                }

                MainButtonActionState.END_SPEECH -> {
                    isTimerActive = false


                    if (nextAlivePlayer(cursor) != closestAlivePlayer(firstSpokedPlayer)
                    ) {//TODO: check for bugs
                        cursor = nextAlivePlayer(cursor)
                        mainButtonActionState = MainButtonActionState.START_SPEECH
                    } else {
                        mainButtonActionState = MainButtonActionState.START_VOTE
                        Log.i("GameLog", "(CmdM) all players spoke")
                    }
                }

                MainButtonActionState.START_VOTE -> {

                    when(voteListCopy.size) {
                        0 -> {
                            Log.i("GameLog", "Skip vote phase (nobody was elected)")
                            //TODO: Visual indication for it
                            mainButtonActionState = MainButtonActionState.START_NIGHT
                        }
                        1 -> {
                            if (currentPhaseNumber == 2) {//if today is first day
                                Log.i("GameLog", "Skip vote phase (only 1 nominated for a first day)")
                                //TODO: Visual indication for it
                                mainButtonActionState = MainButtonActionState.START_NIGHT
                            } else {
                                cursor = voteListCopy.first().number
                                kill(cursor)
                                delayedMainButtonActionState = if(gameOver) {
                                    MainButtonActionState.END_GAME
                                } else {
                                    MainButtonActionState.START_NIGHT
                                }
                                mainButtonActionState = MainButtonActionState.START_SPEECH
                            }
                        }
                        else -> {
                            mainButtonActionState = MainButtonActionState.VOTE_FOR
                            mainButtonOverwriteString = "vote for #${voteListCopy.first()}"
                            selectionMode = PlayerSelectionMode.MULTIPLE
                        }
                    }
                }

                MainButtonActionState.VOTE_FOR -> {
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
                            for (voter in selectedPlayers) { //TODO: add selection
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
                    if (leaderVoteList.size == 1) {
                        kill(leaderVoteList.first().number)
                        if (gameOver) mainButtonActionState= MainButtonActionState.END_GAME
                    } else {
                        mainButtonActionState = MainButtonActionState.AUTOCATASTROPHE
                    }
                }

                MainButtonActionState.KILL_IN_VOTE -> {
                    kill(cursor)
                    mainButtonActionState = if (gameOver)
                        MainButtonActionState.END_GAME
                    else {
                        Log.i("GameLog", "Player $cursor was killed in the vote")
                        MainButtonActionState.START_NIGHT
                    }
                }

                MainButtonActionState.AUTOCATASTROPHE -> {
                    TODO()
                }


                MainButtonActionState.FINAL_VOTE -> {
                    TODO()
                }

                MainButtonActionState.END_GAME -> {
                    Log.d("PressMainBtn", "end_game pressed")
                    gameOver = true //TODO: remove if it is unnecessary
                }

                MainButtonActionState.CRASH -> {
                    throw RuntimeException("Crash state should not be accessible")
                }
            }
            selectedPlayers = ArrayList()
            selectionMode = mainButtonActionState.requireNumber
        }
        return gameState
    }
}