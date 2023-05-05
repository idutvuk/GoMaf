package com.idutvuk.go_maf.model


import android.util.Log
import android.widget.Toast
import com.idutvuk.go_maf.model.gamedata.Game
import com.idutvuk.go_maf.model.gamedata.GameTime
import com.idutvuk.go_maf.model.gamedata.MafiaGameState
import com.idutvuk.go_maf.model.gamedata.Player
import com.idutvuk.go_maf.model.gamedata.PlayerSelectionMode
import com.idutvuk.go_maf.model.gamedata.Role
import com.idutvuk.go_maf.ui.game.MainButtonActionState
import java.lang.Error


object CmdManager {
    val stateHistory = ArrayDeque<MafiaGameState>()
    var currentHistoryIndex = 0

    fun skipNight() : MafiaGameState{
        val gameState: MafiaGameState = if (stateHistory.isEmpty()) {
            MafiaGameState() //TODO: extract it to the game start. History should not be empty
        } else {
            stateHistory.last()
        }

        with(gameState) {
            if (!isMafiaMissedToday) mafiaMissStreak++
            if (mafiaMissStreak >= 3) { //if 3 nights passed
                mainButtonActionState = MainButtonActionState.END_GAME
            } else {
                currentPhaseNumber++
                descriptionText = currentPhaseNumber.toString()
                time = GameTime.DAY
                firstSpokedPlayer = nextAlivePlayer(firstSpokedPlayer, players)
                cursor = firstSpokedPlayer
                mainButtonActionState = MainButtonActionState.START_SPEECH
            }
        }
        return gameState
    }

    fun skipDay() : MafiaGameState{
        //TODO: auto-kill single-voted person
        val gameState: MafiaGameState = if (stateHistory.isEmpty()) {
            MafiaGameState() //TODO: extract it to the game start. History should not be empty
        } else {
            stateHistory.last()
        }

        with(gameState) {
            time = GameTime.NIGHT
            voteList.clear()

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

    fun pressPlayerNumber(prevState: MainButtonActionState): MafiaGameState {
        //clone old game state, when modify it
        val gameState: MafiaGameState = stateHistory.last()
        with(gameState) {

            /**
             * Use for:
             * CHECK_DON
             * CHECK_SHR
             * ADD_TO_VOTE
             * KILL
             * TODO: FOUL (?)
             */
            when (prevState) {
                MainButtonActionState.ADD_TO_VOTE -> {
                    Log.d("GameLog", "(CmdM) Added to vote")
                    voteList.add(selectedPlayers[0])
                }

                MainButtonActionState.KILL -> {
                    mafiaMissStreak = 0
                    delayedMainButtonActionState = if (!kill(players, selectedPlayers[0]))
                        MainButtonActionState.END_GAME
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

        stateHistory.add(gameState)
        return gameState
    }

    fun pressMainBtn(currentState: MainButtonActionState): MafiaGameState {
        //clone old game state, when modify it
        val gameState: MafiaGameState = if (stateHistory.isEmpty()) {
            MafiaGameState() //TODO: extract it to the game start. History should not be empty
        } else {
            stateHistory.last()
        }

        with(gameState) {

            when (currentState) {

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
                        mainButtonActionState = if (!kill(players, selectedPlayers[0])) {
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
                            livingPlayersCount(players) + 2 >= Game.numPlayers
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
                    isOver = false
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
                    firstSpokedPlayer = nextAlivePlayer(firstSpokedPlayer, players)
                    cursor = firstSpokedPlayer
                    mainButtonActionState = MainButtonActionState.START_SPEECH
                }


                MainButtonActionState.START_SPEECH -> {
                    selectionMode = PlayerSelectionMode.SINGLE //so you can select player before the vote
                    isTimerActive = true
                    Log.d("GameLog", "Speech started. Cursor: $cursor")
                    mainButtonActionState = MainButtonActionState.ADD_TO_VOTE
                }

                MainButtonActionState.ADD_TO_VOTE -> {
                    if (selectedPlayers.size == 1) { //if player already selected
                        Log.d("GameLog", "(CmdM) Added to vote")
                        voteList.add(selectedPlayers[0])
                        mainButtonActionState = MainButtonActionState.END_SPEECH
                    } else {
                        previousMainButtonActionState = MainButtonActionState.ADD_TO_VOTE
                        delayedMainButtonActionState = MainButtonActionState.END_SPEECH
                        mainButtonActionState = MainButtonActionState.WAITING_FOR_CLICK
                    }
                }

                MainButtonActionState.END_SPEECH -> {
                    isTimerActive = false


                    if (nextAlivePlayer(cursor, players) != closestAlivePlayer(
                            firstSpokedPlayer,
                            players
                        )
                    ) {//TODO: check for bugs
                        cursor = nextAlivePlayer(cursor, players)
                        mainButtonActionState = MainButtonActionState.START_SPEECH
                    } else {
                        mainButtonActionState = MainButtonActionState.START_VOTE
                        Log.i("GameLog", "(CmdM) all players spoke")
                    }
                }

                MainButtonActionState.START_VOTE -> {
                    //check for empty votelist
                    mainButtonActionState = if (voteList.isEmpty()) {
                        Log.i("GameLog", "Skip vote phase (nobody was elected)")
                        //TODO: Visual indication for it
                        MainButtonActionState.START_NIGHT
                    } else {
                        MainButtonActionState.KILL_IN_VOTE
                    }
                }

                MainButtonActionState.VOTE_FOR -> {
                    TODO()
                }

                MainButtonActionState.KILL_IN_VOTE -> {
                    mainButtonActionState = if (kill(players, cursor))
                        MainButtonActionState.END_GAME
                    else {
                        Log.i("GameLog", "Player $cursor was killed in the vote")
                        MainButtonActionState.START_NIGHT
                    }
                }

                MainButtonActionState.RE_VOTE -> {
                    TODO()
                }


                MainButtonActionState.FINAL_VOTE -> {
                    TODO()
                }

                MainButtonActionState.END_GAME -> {
                    isOver = true //TODO: remove if it is unnecessary
                }

            }
            selectedPlayers = ArrayList()
            selectionMode = mainButtonActionState.requireNumber
        }
        stateHistory.add(gameState)
//        Log.d("GameLog","(from CmdM) MB clicked: ${currentState.name}")
        return gameState
    }

    //Added this comment to track changes on the git. This is the new logic change
    //TODO: implement double, triple and more kill
    private fun kill(players: Array<Player>, index: Int): Boolean { //false if game over
        players[index].alive = false
        return true //TODO: remove (early return only for the debug purposes!)
        var redCounter = 0
        var blackCounter = 0
        for (i in 0 until Game.numPlayers) {
            if (players[i].role.isRed) redCounter++ else blackCounter++
        }
        if (redCounter <= blackCounter) return false //black wins
        if (blackCounter <= 0) return false //red wins
        return true //game continues
    }

//    fun undo(): IntArray { //TODO: rewrite it to the modern logic
//        val result =  IntArray(2)
//        if (currentHistoryIndex ==0) {
//            Log.e("GameLog", "Attempt to redo null aborted")
//            result[0] = -1
//            return result
//        }
//        history[currentHistoryIndex -1].undo()
//        currentHistoryIndex--
//        result[1] = 1
//        Log.d("GameLog","Undo completed. size: ${history.size}, index:$currentHistoryIndex")
//        if (currentHistoryIndex <=0) {
//            result[0] = -1
//        }
//        return result
//    }

    private fun livingPlayersCount(players: Array<Player>): Int {
        var livingPlayers = 0
        for (i in 0 until Game.numPlayers) {
            if (players[i].alive) {
                livingPlayers++
            }
        }
        return livingPlayers
    }

    /**
     * Returns closest alive player to selected player
     * _Example:_
     * 0-1-2-3-4-5-[6]-7-8-9
     * Returns 6 if 6 is alive
     * Returns 7 if 6 is dead and 7 is alive
     */
    private fun closestAlivePlayer(cursor: Int, players: Array<Player>): Int {
        if (players[cursor].alive) return cursor
        return nextAlivePlayer(cursor, players)
    }

    /**
     * returns next alive player
     */
    private fun nextAlivePlayer(cursor: Int, players: Array<Player>): Int {
        //check for alive players:
        val livingPlayers = livingPlayersCount(players)
        if (livingPlayers == 0) throw Error("There is no alive players in the game")
        if (livingPlayers == 1) throw Error("There is only 1 alive person in the game")

        //if reached the end
        if (cursor >= Game.numPlayers - 1) return nextAlivePlayer(-1, players)

        //if next player is alive
        if (players[cursor + 1].alive) return cursor + 1

        //if next player is dead
        return nextAlivePlayer(cursor + 1, players)
    }
}