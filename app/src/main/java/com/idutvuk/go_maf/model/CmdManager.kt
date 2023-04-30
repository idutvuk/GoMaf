package com.idutvuk.go_maf.model


import android.util.Log
import com.idutvuk.go_maf.model.gameactions.GameAction
import com.idutvuk.go_maf.model.gamedata.Game
import com.idutvuk.go_maf.model.gamedata.GameTime
import com.idutvuk.go_maf.model.gamedata.MafiaGameState
import com.idutvuk.go_maf.model.gamedata.Player
import com.idutvuk.go_maf.model.gamedata.Role
import com.idutvuk.go_maf.ui.game.ActionState
import java.lang.Error


object CmdManager {
    val history = ArrayDeque<GameAction>() //TODO: delete deprecated
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
                    time = GameTime.NIGHT
                    //todo: night actions
                    actionState = ActionState.START_MAFIA_SPEECH //TODO: skip if not the first night
                }

                ActionState.START_MAFIA_SPEECH -> {
                    //TODO: Start 60-sec timer
                    actionState = ActionState.CHECK_DON
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
                    //TODO: if can do best move, do BEST_MOVE
                    actionState = ActionState.BEST_MOVE
                }


                ActionState.BEST_MOVE -> {
                    //TODO: Add 20-sec timer
                    passedPhases++ //todo: extract if skipping best move
                    actionState = ActionState.START_DAY
                }

                ActionState.START_GAME -> {
                    isOver = false //TODO: remove if it is unnecessary
                    actionState = ActionState.START_NIGHT
                }


                ActionState.NEXT -> TODO()

                ActionState.DEBUG -> {
                    Log.e("GameLog","Bug! Activated debug button. Switching to START_GAME...")
                    //TODO: fix reaching debug
                    actionState = ActionState.START_GAME
                }


                ActionState.START_DAY -> {
                    time = GameTime.DAY
                    actionState = ActionState.START_SPEECH
                }


                ActionState.START_SPEECH -> {
                    //TODO: add 60-sec timer
                    Log.d("GameLog", "Speech started. Cursor: $cursor")
                    actionState = ActionState.ADD_TO_VOTE
                }

                ActionState.ADD_TO_VOTE -> {
                    Log.d("GameLog","(CmdM) Added to vote")
                    voteList.add(players[cursor])
                    actionState = ActionState.END_SPEECH
                }

                ActionState.END_SPEECH -> {
                    //TODO: EndTimer

                    cursor = nextAlivePlayer(cursor, players)
                     if (cursor >= Game.numPlayers)
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

    fun undo(): IntArray { //TODO: rewrite it to the modern logic
        val result =  IntArray(2)
        if (currentHistoryIndex ==0) {
            Log.e("GameLog", "Attempt to redo null aborted")
            result[0] = -1
            return result
        }
        history[currentHistoryIndex -1].undo()
        currentHistoryIndex--
        result[1] = 1
        Log.d("GameLog","Undo completed. size: ${history.size}, index:$currentHistoryIndex")
        if (currentHistoryIndex <=0) {
            result[0] = -1
        }
        return result
    }

    private fun nextAlivePlayer(cursor: Int, players: Array<Player>): Int {
        //check for alive players:
        var flag = false
        for (i in 0 until Game.numPlayers) {
            if (players[i].alive) {
                flag = true
                break
            }
        }
        if (!flag) throw Error("There is no alive players in the game")

        //if reached the end
        if (cursor >= Game.numPlayers - 1) return nextAlivePlayer(-1,players)

        //if next player is alive
        if (players[cursor+1].alive) return cursor+1

        //if next player is dead
        return nextAlivePlayer(cursor+2, players)
    }
}