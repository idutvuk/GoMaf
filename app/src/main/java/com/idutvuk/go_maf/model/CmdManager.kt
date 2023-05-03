package com.idutvuk.go_maf.model


import android.util.Log
import com.idutvuk.go_maf.model.gamedata.Game
import com.idutvuk.go_maf.model.gamedata.GameTime
import com.idutvuk.go_maf.model.gamedata.MafiaGameState
import com.idutvuk.go_maf.model.gamedata.Player
import com.idutvuk.go_maf.model.gamedata.Role
import com.idutvuk.go_maf.ui.game.ActionState
import java.lang.Error


object CmdManager {
    val stateHistory = ArrayDeque<MafiaGameState>()
    var currentHistoryIndex = 0

    fun commit(currentState: ActionState) : MafiaGameState {
        //clone old game state, when modify it
        val gameState : MafiaGameState = if (stateHistory.isEmpty()) {
            MafiaGameState()//TODO: extract it to the game start. History should not be empty
        } else {
            stateHistory.last()
        }
        with(gameState) {
            when (currentState) {

                ActionState.START_NIGHT -> {
                    passedPhases++
                    time = GameTime.NIGHT

                    if (passedPhases == 0) {
                        actionState = ActionState.START_MAFIA_SPEECH
                    } else {
                        actionState = ActionState.CHECK_DON
                    }
                }

                ActionState.START_MAFIA_SPEECH -> {
                        isTimerActive = true
                        delayedActionState = ActionState.CHECK_DON
                        actionState = ActionState.NEXT
                }


                ActionState.KILL -> {
                    //TODO: kill logic
                }


                ActionState.CHECK_DON -> {
                    if (passedPhases != 0) {
                        val response = players[cursor].role == Role.SHR

                        //TODO: replace it to the ui change
                        Log.i("GameLog", "Sheriff checked $cursor and the result is $response")
                    }
                    actionState = ActionState.CHECK_SHR
                }


                ActionState.CHECK_SHR -> {
                    if (passedPhases != 0) {
                        val response = players[cursor].role.isRed

                        //TODO: replace it to the ui change
                        Log.i("GameLog", "Sheriff checked $cursor and the result is $response")
                    }

                    actionState = if (
                        livingPlayersCount(players) + 2 >= Game.numPlayers &&
                        passedPhases == 2
                        )
                        ActionState.BEST_MOVE else ActionState.START_DAY
                }


                ActionState.BEST_MOVE -> {
                    //TODO: Add 20-sec timer

                    actionState = ActionState.START_DAY
                }

                ActionState.START_GAME -> {
                    isOver = false //TODO: remove if it is unnecessary
                    actionState = ActionState.START_NIGHT
                }


                ActionState.NEXT -> {
                    isTimerActive = false
                    actionState = delayedActionState
                }

                ActionState.DEBUG -> {
                    Log.e("GameLog","Bug! Activated debug button. Switching to START_GAME...")
                    //TODO: fix reaching debug
                    actionState = ActionState.START_GAME
                }


                ActionState.START_DAY -> {
                    passedPhases++
                    time = GameTime.DAY
                    firstSpokedPlayer = nextAlivePlayer(firstSpokedPlayer, players)
                    cursor = firstSpokedPlayer
                    actionState = ActionState.START_SPEECH
                }


                ActionState.START_SPEECH -> {
                    isTimerActive = true
                    Log.d("GameLog", "Speech started. Cursor: $cursor")
                    actionState = ActionState.ADD_TO_VOTE
                }

                ActionState.ADD_TO_VOTE -> {
                    Log.d("GameLog","(CmdM) Added to vote")
                    voteList.add(players[cursor])
                    actionState = ActionState.END_SPEECH
                }

                ActionState.END_SPEECH -> {
                    isTimerActive = false

                    cursor = nextAlivePlayer(cursor, players)
                     if (nextAlivePlayer(cursor, players) != firstSpokedPlayer) //TODO: fix this logic
                         actionState = ActionState.START_SPEECH
                    else {
                         actionState = ActionState.START_VOTE
                         Log.i("GameLog","(CmdM) all players spoke")
                     }
                }

                ActionState.START_VOTE -> {
                    //check for empty votelist
                    if(voteList.isEmpty()) {
                        Log.i("GameLog", "Skip vote phase (nobody was elected)")
                        //TODO: Visual indication for it
                        actionState = ActionState.START_NIGHT
                    } else {
                        actionState = ActionState.KILL_IN_VOTE
                    }
                }

                ActionState.VOTE_FOR -> TODO()

                ActionState.KILL_IN_VOTE -> {
                    if(kill(players,cursor))
                        actionState = ActionState.END_GAME
                    else {
                        Log.i("GameLog","Player $cursor was killed in the vote")
                        actionState = ActionState.START_NIGHT
                    }
                }

                ActionState.RE_VOTE -> TODO()


                ActionState.FINAL_VOTE -> TODO()

                ActionState.END_GAME -> {
                    isOver = true //TODO: remove if it is unnecessary
                }

            }
        }
        stateHistory.add(gameState)
//        Log.d("GameLog","(from CmdM) MB clicked: ${currentState.name}")
        return gameState
    }

    //Added this comment to track changes on the git. This is the new logic change
    //TODO: implement double, triple and more kill
    private fun kill(players: Array<Player>, cursor: Int): Boolean { //false if game over
        players[cursor].alive = false
        var redCounter = 0
        var blackCounter = 0
        for (i in 0 until Game.numPlayers) {
            if (players[i].role.isRed) redCounter++ else blackCounter++
        }
        if (redCounter<=blackCounter) return false //black wins
        if(blackCounter<=0) return false //red wins
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
    private fun nextAlivePlayer(cursor: Int, players: Array<Player>): Int {
        //check for alive players:
        val livingPlayers = livingPlayersCount(players)
        if (livingPlayers == 0) throw Error("There is no alive players in the game")
        if (livingPlayers == 1) throw Error("There is only 1 alive person in the game")

        //if reached the end
        if (cursor >= Game.numPlayers - 1) return nextAlivePlayer(-1,players)

        //if next player is alive
        if (players[cursor+1].alive) return cursor+1

        //if next player is dead
        return nextAlivePlayer(cursor+1, players)
    }
}